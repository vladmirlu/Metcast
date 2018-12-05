package com.synoptic.weather.database.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WeatherCardDTO {

    private long id;

    private String location;

    private List<WeatherUnit> weatherUnits;
}
