package com.synoptic.weather.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weather_units")
public class WeatherUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private LocalDateTime dateTime;

    @Column
    private String weatherDescription;

    @Column
    private int tempCelsius;

    @Column
    private float precipitationMM;

    @Column
    private int pressureMillibars;

    @Column
    private int humidityPercent;

    @Column
    private int visibilityKm;

    @Column
    private int cloudCoverPercent;

    @Column
    private int windSpeedKmPerHour;

    @Column
    private String weatherIconUrl;


}
