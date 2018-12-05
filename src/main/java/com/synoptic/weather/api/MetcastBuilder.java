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

@Service
public class MetcastBuilder {

    @Value("${WWO_url}")
    private String WWO_url;

    @Value("${WWO_api_kay}")
    private String WWO_api_kay;

    @Value("${WWO_days_forecast}")
    private String WWO_days_forecast;

    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m");

    @Autowired
    SynopticHttpClient client;

    public WeatherCardDTO fillWeatherCard(WeatherCardDTO cardDTO)  {

        Map<String, JSONArray> jsonObjectsMap = selectDataFromJSON(WWO_url + cardDTO.getLocation() + WWO_api_kay + WWO_days_forecast);
        JSONObject weatherObjJSON = jsonObjectsMap.remove(jsonObjectsMap.keySet().stream().findFirst().get()).getJSONObject(0);
        WeatherUnit unit = buildWeatherUnit(weatherObjJSON, formatDateTime(LocalDateTime.now()));
        cardDTO.setWeatherUnits(new ArrayList<>(Collections.singletonList(unit)));
        return putWeatherUnitsInto(cardDTO, jsonObjectsMap);
    }

    public WeatherCardDTO putWeatherUnitsInto(WeatherCardDTO cardDTO, Map<String, JSONArray> jsonObjectsMap){

        for (Map.Entry<String, JSONArray> entry : jsonObjectsMap.entrySet()) {
            JSONObject jsonObject = entry.getValue().getJSONObject(0);
            cardDTO.getWeatherUnits().add(buildWeatherUnit(jsonObject, parseStringToDateTime(jsonObject, entry.getKey())));
            jsonObject = entry.getValue().getJSONObject(4);
            cardDTO.getWeatherUnits().add(buildWeatherUnit(jsonObject, parseStringToDateTime(jsonObject, entry.getKey())));
        }
        return cardDTO;
    }

    public Map<String, JSONArray> selectDataFromJSON(String url) {
        JSONObject jsonObject = new JSONObject(client.findWeatherData(url));
        jsonObject = jsonObject.getJSONObject("data");
        Map<String, JSONArray> map = new LinkedHashMap<>();
        map.put(jsonObject.getJSONArray("current_condition").getJSONObject(0).getString("observation_time"), jsonObject.getJSONArray("current_condition"));
        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        for(int i = 1; i <= 3; i++ ) {
            map.put(jsonArray.getJSONObject(i).getString("date"), jsonArray.getJSONObject(i).getJSONArray("hourly"));
        }
        return map;
    }

    private WeatherUnit buildWeatherUnit(JSONObject weatherObjJSON, LocalDateTime dateTime){

        return  WeatherUnit.builder().dateTime(dateTime)
                .weatherDescription(weatherObjJSON.getJSONArray("weatherDesc").getJSONObject(0).getString("value"))
                .tempCelsius(dateTime.isAfter(formatDateTime(LocalDateTime.now())) ? weatherObjJSON.getInt("tempC") : weatherObjJSON.getInt("temp_C"))
                .precipitationMM(weatherObjJSON.getFloat("precipMM"))
                .pressureMillibars(weatherObjJSON.getInt("pressure"))
                .humidityPercent(weatherObjJSON.getInt("humidity"))
                .visibilityKm(weatherObjJSON.getInt("visibility"))
                .cloudCoverPercent(weatherObjJSON.getInt("cloudcover"))
                .windSpeedKmPerHour(weatherObjJSON.getInt("windspeedKmph"))
                .weatherIconUrl( weatherObjJSON.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value")).build();

    }

    private LocalDateTime parseStringToDateTime(JSONObject jsonObj, String date){

        int time = Integer.parseInt(jsonObj.getString("time"));
       return LocalDateTime.parse(date + " " + time/100 + ":" + time%100, formatter);
    }

    private LocalDateTime formatDateTime(LocalDateTime dateTime){
        return LocalDateTime.parse(dateTime.format(formatter), formatter);
    }
}
