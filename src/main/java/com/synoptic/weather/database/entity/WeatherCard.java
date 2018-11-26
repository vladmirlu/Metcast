package com.synoptic.weather.database.entity;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "weather_cards")
public class WeatherCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "location")
    private String location;

    @OneToMany
    private List <WeatherUnit> weatherUnits;

    @ManyToOne
    private User user;

}
