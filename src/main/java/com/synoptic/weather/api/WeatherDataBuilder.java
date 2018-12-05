package com.synoptic.weather.api;

import com.synoptic.weather.database.dto.WeatherCardDTO;
import com.synoptic.weather.database.dto.WeatherUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Component to read and convert data from the weather provider
 */
@Service
public class WeatherDataBuilder {

    @Value("${WWO_url}")
    private String WWO_url;

    @Value("${WWO_api_kay}")
    private String WWO_api_kay;

    @Value("${WWO_days_forecast}")
    private String WWO_days_forecast;

    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m");

    @Autowired
    SynopticHttpClient client;

    /**
     * Fills weather DTO with data from json
     *
     * @param cardDTO data transfer object(DTO)
     * @return filled weather card DTO
     */
    public WeatherCardDTO fillWeatherCardDTO(WeatherCardDTO cardDTO) {

        JSONObject jsonObject = new JSONObject(client.provideWeatherData(WWO_url + cardDTO.getLocation() + WWO_api_kay + WWO_days_forecast));
        jsonObject = jsonObject.getJSONObject("data");
        Map<String, JSONArray> jsonObjectsMap = selectDataFromJSON(jsonObject);
        return putWeatherUnitsIntoDTO(cardDTO, jsonObjectsMap);
    }

    /**
     * Selects data of today and next 3 days from json
     *
     * @param jsonObject weather data json object
     * @return map of selected weather data from json
     */
    public Map<String, JSONArray> selectDataFromJSON(JSONObject jsonObject) {

        Map<String, JSONArray> map = new LinkedHashMap<>();
        map.put(jsonObject.getJSONArray("current_condition").getJSONObject(0).getString("observation_time"), jsonObject.getJSONArray("current_condition"));
        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        for (int i = 1; i <= 3; i++) {
            map.put(jsonArray.getJSONObject(i).getString("date"), jsonArray.getJSONObject(i).getJSONArray("hourly"));
        }
        return map;
    }

    /**
     * Puts weather units of today and next 3 days into weather card DTO
     *
     * @param cardDTO        data transfer object(DTO)
     * @param jsonObjectsMap map of stored weather data
     * @return filled weather card DTO with weather units of today and next 3 days
     */
    public WeatherCardDTO putWeatherUnitsIntoDTO(WeatherCardDTO cardDTO, Map<String, JSONArray> jsonObjectsMap) {

        JSONObject weatherObjJSON = jsonObjectsMap.remove(jsonObjectsMap.keySet().stream().findFirst().get()).getJSONObject(0);
        cardDTO.setWeatherUnits(new ArrayList<>(Collections.singletonList(buildWeatherUnit(weatherObjJSON, formatDateTime(LocalDateTime.now())))));

        for (Map.Entry<String, JSONArray> entry : jsonObjectsMap.entrySet()) {
            JSONObject jsonObject = entry.getValue().getJSONObject(0);
            cardDTO.getWeatherUnits().add(buildWeatherUnit(jsonObject, parseStringToDateTime(jsonObject.getString("time"), entry.getKey())));
            jsonObject = entry.getValue().getJSONObject(4);
            cardDTO.getWeatherUnits().add(buildWeatherUnit(jsonObject, parseStringToDateTime(jsonObject.getString("time"), entry.getKey())));
        }
        return cardDTO;
    }

    /**
     * Builds new weather unit and fills it with data from json
     *
     * @param weatherObjJSON json of weather data
     * @param dateTime       date and time of weather unit
     * @return new built weather unit of exact day period
     */
    private WeatherUnit buildWeatherUnit(JSONObject weatherObjJSON, LocalDateTime dateTime) {

        return WeatherUnit.builder().dateTime(dateTime)
                .weatherDescription(weatherObjJSON.getJSONArray("weatherDesc").getJSONObject(0).getString("value"))
                .tempCelsius(dateTime.isAfter(formatDateTime(LocalDateTime.now())) ? weatherObjJSON.getInt("tempC") : weatherObjJSON.getInt("temp_C"))
                .precipitationMM(weatherObjJSON.getFloat("precipMM"))
                .pressureMillibars(weatherObjJSON.getInt("pressure"))
                .humidityPercent(weatherObjJSON.getInt("humidity"))
                .visibilityKm(weatherObjJSON.getInt("visibility"))
                .cloudCoverPercent(weatherObjJSON.getInt("cloudcover"))
                .windSpeedKmPerHour(weatherObjJSON.getInt("windspeedKmph"))
                .weatherIconUrl(weatherObjJSON.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value")).build();

    }

    /**
     * Parses date string to LocalDateTime
     *
     * @param timeStr time as string
     * @param dateStr date as string
     * @return parsed local date time
     */
    private LocalDateTime parseStringToDateTime(String timeStr, String dateStr) {
        int time = Integer.parseInt(timeStr);
        return LocalDateTime.parse(dateStr + " " + time / 100 + ":" + time % 100, formatter);
    }

    /**
     * Formats LocalDateTime
     *
     * @param dateTime local date and time
     * @return formatted local date and time in format of "yyyy-MM-dd H:m"
     */
    private LocalDateTime formatDateTime(LocalDateTime dateTime) {
        return LocalDateTime.parse(dateTime.format(formatter), formatter);
    }
}
