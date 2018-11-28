package com.synoptic.weather.database.dto;

import com.synoptic.weather.database.entity.WeatherUnit;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WeatherCardDTO {

    private String location;

    private List<WeatherUnit> weatherUnits;

    private UserDTO userDTO;
}
