package com.synoptic.weather.database.dto;

import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherUnit;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WeatherCardDto {

    private Long id;

    private String location;

    private List<WeatherUnit> weatherUnits;

    private User user;
}
