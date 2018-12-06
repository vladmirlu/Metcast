package com.synoptic.weather.model.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Weather card data transfer object
 * */
@Data
@Builder
public class WeatherCardDTO {

    private long id;

    private String location;

    /**
     *Weather time conditions list
     * */
    private List<WeatherUnit> weatherUnits;
}
