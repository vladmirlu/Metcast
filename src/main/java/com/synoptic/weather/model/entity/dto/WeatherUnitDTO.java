package com.synoptic.weather.model.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Weather time condition unit
 */
@Data
@Builder
public class WeatherUnitDTO {

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

    /**
     * Customised to string method
     *
     * @return current object as string
     */
    @Override
    public String toString() {
        return new StringBuilder().append(" WeatherUnitDTO: { dateTime: ").append(dateTime)
                .append(", weatherDescription: ").append(weatherDescription)
                .append(", tempCelsius: ").append(tempCelsius)
                .append(", precipitationMM: ").append(precipitationMM)
                .append(", pressureMillibars: ").append(pressureMillibars)
                .append(", humidityPercent: ").append(humidityPercent)
                .append(", visibilityKm: ").append(visibilityKm)
                .append(", cloudCoverPercent: ").append(cloudCoverPercent)
                .append(", windSpeedKmPerHour: ").append(windSpeedKmPerHour)
                .append(", weatherIconUrl: ").append(weatherIconUrl).append(" } ").toString();
    }
}