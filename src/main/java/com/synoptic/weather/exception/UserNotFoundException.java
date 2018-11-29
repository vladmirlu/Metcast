package com.synoptic.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Error! The user with such email not found")
public class UserNotFoundException extends  RuntimeException {
}
