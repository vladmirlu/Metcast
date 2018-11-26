package com.synoptic.weather.api;

import com.synoptic.weather.database.entity.WeatherCard;
import com.synoptic.weather.database.entity.WeatherUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

@Service
public class MetcastBuilder {

    @Value("${WWO_url}")
    private String WWO_url;

    @Value("${WWO_api_kay}")
    private String WWO_api_kay;

    private  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

    @Autowired
    SynopticHttpClient client;

    @PostConstruct
    public void print() throws IOException{
       WeatherCard weatherCard =  createWeatherCard("Kiev");
       weatherCard.getWeatherUnits().forEach(weatherUnit -> System.out.println(weatherUnit));
    }

    public WeatherCard createWeatherCard(String location) throws IOException {

        Map<String, JSONArray> jsonObjectsMap = client.selectDataFromJSON(WWO_url + location + WWO_api_kay);
        String weatherTime = jsonObjectsMap.keySet().stream().findFirst().get();
        LocalDateTime weatherDateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);

        JSONObject weatherObjJSON = jsonObjectsMap.remove(weatherTime).getJSONObject(0);
        WeatherUnit unit = buildWeatherUnit(weatherObjJSON, weatherDateTime, "temp_C");

        WeatherCard weatherCard = WeatherCard.builder().weatherUnits(new ArrayList<>()).build();
        weatherCard.getWeatherUnits().add(unit);

       return putWeatherUnitsInto(weatherCard, jsonObjectsMap);
    }

    private WeatherUnit buildWeatherUnit(JSONObject weatherObjJSON, LocalDateTime dateTime, String tempC){
        return  WeatherUnit.builder().dateTime(dateTime)
                .weatherDesc(weatherObjJSON.getJSONArray("weatherDesc").getJSONObject(0).getString("value"))
                .tempC(weatherObjJSON.getInt(tempC))
                .precipMM(weatherObjJSON.getFloat("precipMM"))
                .pressure(weatherObjJSON.getInt("pressure"))
                .humidity(weatherObjJSON.getInt("humidity"))
                .visibility(weatherObjJSON.getInt("visibility"))
                .cloudCover(weatherObjJSON.getInt("cloudcover"))
                .windspeedKmph(weatherObjJSON.getInt("windspeedKmph")).build();
    }

    public WeatherCard putWeatherUnitsInto(WeatherCard weatherCard, Map<String, JSONArray> jsonObjectsMap){
        for (Map.Entry<String, JSONArray> entry : jsonObjectsMap.entrySet()) {
            JSONObject weatherObjJSON = entry.getValue().getJSONObject(0);
            LocalDateTime weatherDateTime = LocalDateTime.parse(entry.getKey() + " 00:00 AM", formatter);
            weatherCard.getWeatherUnits().add(buildWeatherUnit(weatherObjJSON, weatherDateTime, "tempC"));

            weatherObjJSON = entry.getValue().getJSONObject(4);
            weatherDateTime = LocalDateTime.parse(entry.getKey() + weatherObjJSON.getString("time"), formatter);
            weatherCard.getWeatherUnits().add(buildWeatherUnit(weatherObjJSON, weatherDateTime, "tempC"));
        }
        return weatherCard;
    }
}
