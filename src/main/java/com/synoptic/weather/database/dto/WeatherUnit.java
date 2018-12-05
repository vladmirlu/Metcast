package com.synoptic.weather.database.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WeatherUnit {

    private LocalDateTime dateTime;

    private String weatherDescription;

    private int tempCelsius;

    private float precipitationMM;

    private int pressureMillibars;

    private int humidityPercent;

    private int visibilityKm;

    private int cloudCoverPercent;

    private int windSpeedKmPerHour;

    private String weatherIconUrl;
}
