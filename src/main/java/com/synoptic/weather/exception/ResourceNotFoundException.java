package com.synoptic.weather.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Customised exception to handle event when desired resource not found
 */
@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

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
    }
}