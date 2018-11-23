package com.synoptic.weather.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weather_cards")
public class WeatherCard {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "current_weather")
    private String currentWeather;

    @Column(name = "forecast_weather")
    private String forecastWeather;

    @ManyToOne
    private User user;

}
