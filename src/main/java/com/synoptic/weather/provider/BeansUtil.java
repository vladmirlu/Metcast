package com.synoptic.weather.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Beans storage
 * */
@Component
public class BeansUtil {

    /**
     * Object to get text from message property file
     * @return new resource bundle of exact file
     * */
    @Bean
    public ResourceBundle resourceBundle() throws  IOException {
        return new PropertyResourceBundle(new FileInputStream("src/main/resources/message.properties"));
    }
}
