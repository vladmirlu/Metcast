package com.synoptic.weather.model.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Weather time condition unit
 */
@Data
@Builder
public class WeatherUnit {

    /**
     * local date and time
     */
    private LocalDateTime dateTime;

    /**
     * weather short description
     */
    private String weatherDescription;

    /**
     * temperature in celsius
     */
    private int tempCelsius;

    /**
     * precipitation in mm
     */
    private float precipitationMM;

    /**
     * pressure in millibars
     */
    private int pressureMillibars;

    /**
     * humidity in percents
     */
    private int humidityPercent;

    /**
     * visibility in kilometers
     */
    private int visibilityKm;

    /**
     * cloud coverage in percents
     */
    private int cloudCoverPercent;

    /**
     * wind speed in kilometers per hour
     */
    private int windSpeedKmPerHour;

    /**
     * weather icon url
     */
    private String weatherIconUrl;
}
