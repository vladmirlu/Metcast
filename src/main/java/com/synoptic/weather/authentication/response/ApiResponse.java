package com.synoptic.weather.authentication.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *Current API customised response
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

/**
 * Successfully response indicator
 * */
    private Boolean success;

    /**
     * Response message
     * */
    private String message;

}
