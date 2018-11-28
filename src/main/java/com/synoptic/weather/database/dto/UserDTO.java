package com.synoptic.weather.database.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String email;

    private String userPassword;
}
