package com.synoptic.weather.authentication.security;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web mvc configuration object
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Logger logger = Logger.getLogger(WebMvcConfig.class);

    /**
     * Configures mapping, origins and allows http methods
     *
     * @param registry spring web registry object
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(3600);
        logger.info("Adds web mvc configuration " + registry.toString());
    }
}
