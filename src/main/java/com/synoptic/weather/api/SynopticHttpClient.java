package com.synoptic.weather.api;

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
import java.util.*;

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

    private CloseableHttpClient buildHttpClient() {
        return HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
    }
}
