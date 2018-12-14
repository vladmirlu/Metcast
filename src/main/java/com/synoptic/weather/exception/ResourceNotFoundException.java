package com.synoptic.weather.exception;

import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Customised exception to handle event when desired resource not found
 */
@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final Logger logger = Logger.getLogger(ResourceNotFoundException.class);

    /**
     * Resource entity name
     */
    private String resourceName;

    /**
     * Resource entity field name
     */
    private String fieldName;

    /**
     * Resource entity field value
     */
    private Object fieldValue;

    /**
     * Builds exception body
     *
     * @param resourceName Resource entity name
     * @param fieldName    entity field name
     * @param fieldValue   field value
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {

        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        logger.error(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}