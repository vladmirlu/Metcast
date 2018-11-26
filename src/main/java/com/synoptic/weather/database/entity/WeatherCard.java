package com.synoptic.weather.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany
    private List <Weather> weatherForecasts;

    @ManyToOne
    private User user;

}
