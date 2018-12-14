package com.synoptic.weather.authentication.response;

import lombok.Data;

/**
 * Response for jwt authentication
 * */
@Data
public class JwtAuthenticationResponse {

    /**
     * jwt access token
     * */
    private String accessToken;

    /**
     * Token type
     * */
    private String tokenType = "Metcast";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
