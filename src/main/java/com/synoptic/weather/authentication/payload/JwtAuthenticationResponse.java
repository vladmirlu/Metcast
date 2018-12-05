package com.synoptic.weather.authentication.payload;


import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Metcast";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
