package com.synoptic.weather.com.synoptic.weather.meteoapi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SynopticHttpClient {

    public String findWeatherData(String url) throws IOException{

        try {
            CloseableHttpClient client = buildHttpClient();
            HttpResponse response = client.execute(new HttpGet(url));
            if (response.getEntity() != null) {
                String entityStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(response.getEntity());
                client.close();
                return entityStr;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        throw new HttpResponseException(404, "Response Entity not found");
    }

    public Map<String, JSONObject> responseToJSON(String url) throws IOException {
        JSONObject jsonObject = new JSONObject(findWeatherData(url));
        jsonObject = jsonObject.getJSONObject("data");

        Map<String, JSONObject> map = new HashMap<>();
        map.put(jsonObject.getJSONArray("current_condition").getJSONObject(0).getString("observation_time"), jsonObject.getJSONArray("current_condition").getJSONObject(0));
        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        map.put(jsonArray.getJSONObject(1).getString("date"), jsonArray.getJSONObject(1).getJSONArray("hourly").getJSONObject(6));
        map.put(jsonArray.getJSONObject(2).getString("date"), jsonArray.getJSONObject(2).getJSONArray("hourly").getJSONObject(6));
        map.put(jsonArray.getJSONObject(3).getString("date"), jsonArray.getJSONObject(3).getJSONArray("hourly").getJSONObject(6));
        return map;
    }


    private CloseableHttpClient buildHttpClient() {
        return HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
    }
}
