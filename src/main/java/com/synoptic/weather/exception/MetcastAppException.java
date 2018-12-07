package com.synoptic.weather.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *Customised exception to handle event of internal server error
 * */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MetcastAppException extends RuntimeException {

    private static final Logger logger = Logger.getLogger(MetcastAppException.class);

    public MetcastAppException(String message) {
        super(message);
        logger.error("MetcastAppException cached: " + message);
    }
}