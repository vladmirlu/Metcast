package com.synoptic.weather.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Component
public class BeansUtil {

    @Bean
    public ResourceBundle resourceBundle() throws  IOException {
        return new PropertyResourceBundle(new FileInputStream("src/main/resources/message.properties"));
    }
}
