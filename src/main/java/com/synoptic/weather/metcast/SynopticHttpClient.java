package com.synoptic.weather.metcast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Http client for connecting with API of weather data provider
 */
@Component
public class SynopticHttpClient {

    private static final Logger logger = Logger.getLogger(SynopticHttpClient.class);

    /**
     * Gets and provides weather data as string via API of weather data provider
     *
     * @param weatherApiUrl weather data provider API url
     * @return received weather data as json string
     */
    public String provideWeatherData(String weatherApiUrl) {
        String entityStr = "";
        logger.debug("Sending request to receive data from weather provider: " + weatherApiUrl);
        try {
            CloseableHttpClient client = buildHttpClient();
            HttpResponse response = client.execute(new HttpGet(weatherApiUrl));
            logger.debug("Received response status: " + response.getStatusLine());
            if (response.getEntity() != null) {
                logger.debug("Received response entity: " + response.getEntity().getContentEncoding());
                entityStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(response.getEntity());
                client.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("Cached ClientProtocolException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException io) {
            logger.error("Cached IOException: " + io.getMessage());
            io.printStackTrace();
        }
        logger.info("Data from weather provider:" + weatherApiUrl + " received successfully");
        return entityStr;
    }

    /**
     * Builds closable http client
     *
     * @return built new http client
     */
    private CloseableHttpClient buildHttpClient() {
        logger.info("Creating new CloseableHttpClient with CookieSpecs: " + CookieSpecs.IGNORE_COOKIES);
        return HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
    }
}
