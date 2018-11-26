package com.synoptic.weather.database.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserDTO {

    private String email;

    private String firstName;

    private String lastName;
}
