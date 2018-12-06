package com.synoptic.weather.api;

import com.synoptic.weather.model.entity.dto.WeatherCardDTO;
import com.synoptic.weather.model.entity.dto.WeatherUnit;
import org.apache.log4j.Logger;
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

    /**
     * 'World Weather Online' weather data provider API url
     * */
    @Value("${WWO_url}")
    private String WWO_Url;

    /**
     * 'World Weather Online' weather data provider API key
     * */
    @Value("${WWO_api_kay}")
    private String WWO_ApiKey;

    /**
     *Days count to weather forecast using 'World Weather Online'
     * */
    @Value("${WWO_days_forecast}")
    private String WWO_DaysForecast;

    /**
     * Date and time formatter
     * */
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m");

    @Autowired
    SynopticHttpClient client;

    private static final Logger logger = Logger.getLogger(WeatherDataBuilder.class);

    /**
     * Fills weather DTO with data from json
     *
     * @param cardDTO data transfer object(DTO)
     * @return filled weather card DTO
     */
    public WeatherCardDTO fillWeatherCardDTO(WeatherCardDTO cardDTO) {

        String weatherSourceUrl = WWO_Url + cardDTO.getLocation() + WWO_ApiKey + WWO_DaysForecast;
        logger.info("Sending request to receive data from weather provider: " + weatherSourceUrl);
        JSONObject jsonObject = new JSONObject(client.provideWeatherData(weatherSourceUrl));
        logger.debug("Received weather data from " + weatherSourceUrl);
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
        JSONArray currentConditionJson = jsonObject.getJSONArray("current_condition");
        map.put(currentConditionJson.getJSONObject(0).getString("observation_time"), currentConditionJson);
        logger.debug("Received and set weather data of current weather condition: " + currentConditionJson);
        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        for (int i = 1; i <= 3; i++) {

            map.put(jsonArray.getJSONObject(i).getString("date"), jsonArray.getJSONObject(i).getJSONArray("hourly"));
            logger.debug("\n Received weather data of the date: " + jsonArray.getJSONObject(i).getString("date") + " data : " + jsonArray.getJSONObject(i).getJSONArray("hourly"));
        }
        return map;
    }

    /**
     * Puts weather units of today and next 3 days into weather card DTO
     *
     * @param cardDTO   data transfer object(DTO)
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
