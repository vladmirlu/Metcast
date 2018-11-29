package com.synoptic.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Error! The weather card of such location not found")
public class WeatherCardNotFoundException extends RuntimeException {
}
