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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "location", unique = true, nullable = false)
    private String location;

    @OneToMany(fetch = FetchType.EAGER)
    private List <WeatherUnit> weatherUnits;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
