package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.UserDao;
import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dto.WeatherCardDTO;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import com.synoptic.weather.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class ModelManager {

    @Autowired
    private UserDao userDao;

    @Autowired
    private WeatherCardDao cardDao;

    public User getUserOrExit(String username) {

        return userDao.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public WeatherCard getWeatherCardOrError(String location) {

       return cardDao.findWeatherCardByLocation(location)
                .orElseThrow(() -> new ResourceNotFoundException("WeatherCard", "location", location));
    }

    public WeatherCard getExistingCardOrNewCreated(String location, User user){
       return cardDao.findWeatherCardByLocation(location)
                .orElse(WeatherCard.builder().location(location).users(new ArrayList<>(Collections.singletonList(user))).build());
    }

    public WeatherCardDTO weatherCardToDTO(WeatherCard weatherCard){

        return WeatherCardDTO.builder().id(weatherCard.getId()).location(weatherCard.getLocation()).build();
    }

}
