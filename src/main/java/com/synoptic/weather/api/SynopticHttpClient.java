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

/**
 * Http client for connecting with API of weather data provider
 */
@Component
public class SynopticHttpClient {

    /**
     * Gets and provides weather data as string via API of weather data provider
     *
     * @param apiUrl weather data provider API url
     * @return received weather data as json string
     */
    public String provideWeatherData(String apiUrl) {
        String entityStr = "";
        try {
            CloseableHttpClient client = buildHttpClient();
            HttpResponse response = client.execute(new HttpGet(apiUrl));
            if (response.getEntity() != null) {
                entityStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(response.getEntity());
                client.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return entityStr;
    }

    /**
     * Builds closable http client
     *
     * @return built new http client
     */
    private CloseableHttpClient buildHttpClient() {
        return HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
    }
}
