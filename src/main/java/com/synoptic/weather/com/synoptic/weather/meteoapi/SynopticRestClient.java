package com.synoptic.weather.com.synoptic.weather.meteoapi;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SynopticRestClient {

    @Value("${open_weather_api_kay}")
    private String open_weather_api_kay;

    @Value("${current_weather_url}")
    private String current_weather_url;

    @Value("${forecast_weather_url}")
    private String forecast_weather_url;

    private String country;

    private List<String> cities;

    public void printWeatherData(){

    }

    public String    FindWeatherData(String url) throws IOException{
        CloseableHttpClient client = buildHttpClient();
        String entity = null;
        try {
                HttpResponse response = client.execute(new HttpGet(url));
                entity = EntityUtils.toString(response.getEntity());

                if (response.getEntity() != null)
                    EntityUtils.consume(response.getEntity());
            }catch (ClientProtocolException e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
        return entity;
    }


    private CloseableHttpClient buildHttpClient() {
        return HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
    }
}
