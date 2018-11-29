package com.synoptic.weather.authentication.payload;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

}
