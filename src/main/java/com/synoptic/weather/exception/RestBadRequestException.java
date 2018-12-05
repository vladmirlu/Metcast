package com.synoptic.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestBadRequestException extends RuntimeException {

    public RestBadRequestException(String message) {
        super(message);
    }
}