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

    private String username;

    /**
     *Weather time conditions list
     * */
    private List<WeatherUnitDTO> weatherUnitDTOS;

    /**
     * Customised to string method
     *
     * @return current object as string
     */
    @Override
    public String toString(){
        return new StringBuilder().append(" WeatherCardDTO: { id: ").append(id)
                .append(", location: ").append(location).append(", weatherUnitDTOS: ")
                .append(weatherUnitDTOS).toString();
    }
}
