package com.synoptic.weather;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
@ImportResource("classpath:spring-scheduler.xml")
public class ApplicationStart {

    @Value("${log4jResourcesPath}")
   private static String log4jResourcesPath;

    private final static Logger logger = Logger.getLogger(ApplicationStart.class);

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure(log4jResourcesPath);
            SpringApplication.run(ApplicationStart.class, args);
            logger.info("Main method started. " + TimeZone.getDefault());
        }catch (Throwable e){
            logger.error("Main method crashed " + e.getMessage());
            e.printStackTrace();
        }
    }
}
