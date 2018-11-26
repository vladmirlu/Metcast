package com.synoptic.weather.com.synoptic.weather.meteoapi;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class SynopticRestService {

    @Value("${WWO_url}")
    private String WWO_url;

    @Value("${WWO_api_kay}")
    private String WWO_api_kay;

    private List<String> locations;

     @Autowired
     SynopticHttpClient client;

     @PostConstruct
        public void printWeatherData() throws IOException{
        Map<String, JSONObject> jsonObjects = client.responseToJSON(WWO_url + "Kiev" + WWO_api_kay);
         for (Map.Entry<String, JSONObject> entry : jsonObjects.entrySet()) {
             System.out.println(entry.getKey()+" : "+entry.getValue());
         }
    }
}
