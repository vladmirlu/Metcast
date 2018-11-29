package com.synoptic.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Error! Weather data not found")
public class WeatherUnitNotFoundexception extends RuntimeException {
}
