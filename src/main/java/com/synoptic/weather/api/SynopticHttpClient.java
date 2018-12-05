package com.synoptic.weather.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SynopticHttpClient {

    public String findWeatherData(String url) {
        String entityStr = "";
        try {
            CloseableHttpClient client = buildHttpClient();
            HttpResponse response = client.execute(new HttpGet(url));
            if (response.getEntity() != null) {
                entityStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(response.getEntity());
                client.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException io){
            io.printStackTrace();
        }
        return entityStr;
    }

    private CloseableHttpClient buildHttpClient() {
        return HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
    }
}
