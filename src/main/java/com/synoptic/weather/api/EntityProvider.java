package com.synoptic.weather.api;

import com.synoptic.weather.model.repository.UserDao;
import com.synoptic.weather.model.repository.WeatherCardDao;
import com.synoptic.weather.model.entity.dto.WeatherCardDTO;
import com.synoptic.weather.model.entity.User;
import com.synoptic.weather.model.entity.WeatherCard;
import com.synoptic.weather.exception.ResourceNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Component to provide entities
 * */
@Component
public class EntityProvider {

    private static final Logger logger = Logger.getLogger(EntityProvider.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private WeatherCardDao cardDao;

    /**
     * Gets user by username
     *
     * @param username current user username
     * @return existing user or goes throw ResourceNotFoundException exception
     * */
    public User getUserOrExit(String username) {

        logger.debug("The user " + username + " is providing");
        return userDao.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    /**
     * Gets weather card  by concrete location name
     *
     * @param location current location
     * @return existing weather card or goes throw ResourceNotFoundException exception
     * */
    public WeatherCard getWeatherCardOrError(String location) {

        logger.debug("The geographic location " + location + " is providing");
       return cardDao.findWeatherCardByLocation(location)
                .orElseThrow(() -> new ResourceNotFoundException("WeatherCard", "location", location));
    }

    /**
     * Gets existing weather card or creates the new one and sets current user into
     *
     * @param location current location name
     * @param user current user
     * @return existing weather card or new created
     * */
    public WeatherCard getExistingCardOrNewCreated(String location, User user){

        logger.debug("The user " + user + " and geographic location" + location + " are providing");
       return cardDao.findWeatherCardByLocation(location)
                .orElse(WeatherCard.builder().location(location).users(new ArrayList<>(Collections.singletonList(user))).build());
    }

    /**
     * Converts weather card to weather card data transfer object (DTO)
     *
     * @param weatherCard current weather card
     * @return new built weather card DTO
     * */
    public WeatherCardDTO weatherCardToDTO(WeatherCard weatherCard){

        logger.info("The data transfer object of weather card " + weatherCard + " is creating");
        return WeatherCardDTO.builder().id(weatherCard.getId()).location(weatherCard.getLocation()).build();
    }

}
