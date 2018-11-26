package com.synoptic.weather.database.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Weather {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private LocalTime time;

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
    private int windSpeedMeterSec;

    @OneToOne
    private WeatherImage weatherImage;


}
