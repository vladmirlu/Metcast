package com.synoptic.weather.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Customised exception server bad request
 * */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestBadRequestException extends RuntimeException {

    private static final Logger logger = Logger.getLogger(RestBadRequestException.class);

    public RestBadRequestException(String message) {

        super(message);
        logger.error("RestBadRequestException cached: " + message);
    }
}