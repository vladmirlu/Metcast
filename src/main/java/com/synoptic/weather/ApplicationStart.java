package com.synoptic.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@ImportResource("classpath:spring-scheduler.xml")
public class ApplicationStart {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class, args);
    }
}
