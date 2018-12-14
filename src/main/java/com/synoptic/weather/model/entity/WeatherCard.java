package com.synoptic.weather.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Weather card entities
 * */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weather_cards")
public class WeatherCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    /**
     * Geographic location
     * */
    @Column(unique = true, nullable = false)
    private String location;

    /**
     * Users list referenced on current weather card
     * */
    @ManyToMany
    private List <User> users;

    /**
     * Customised to string method
     *
     * @return current object as string
     */
    @Override
    public String toString(){
        return new StringBuilder().append(" WeatherCardDTO: { id: ").append(id)
                .append(", location: ").append(location).append(", users: ")
                .append(users).toString();
    }
}
