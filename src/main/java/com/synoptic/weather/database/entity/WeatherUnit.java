package com.synoptic.weather.database.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "weather_units")
public class WeatherUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private LocalDateTime dateTime;

    @Column
    private String weatherDesc;

    @Column
    private int tempC;

    @Column
    private float precipMM;

    @Column
    private int pressure;

    @Column
    private int humidity;

    @Column
    private int visibility;

    @Column
    private int cloudCover;

    @Column
    private int windspeedKmph;

    @OneToOne
    private WeatherImage weatherImage;


}
