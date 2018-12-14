package com.synoptic.weather;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class ApplicationStart {

    private final static Logger logger = Logger.getLogger(ApplicationStart.class);

    /**
     * App entry point
     * */
    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure("src/main/resources/log4j.properties");
            SpringApplication.run(ApplicationStart.class, args);
            logger.info("Main method started. " + TimeZone.getDefault());
        }catch (Throwable e){
            logger.error("Main method crashed " + e.getMessage());
            e.printStackTrace();
        }
    }
}
