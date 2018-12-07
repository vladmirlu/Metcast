package com.synoptic.weather.model.repository;

import com.synoptic.weather.model.entity.User;
import com.synoptic.weather.model.entity.WeatherCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * WeatherCard repository to interact with model
 */
@Transactional
public interface WeatherCardDao extends CrudRepository<WeatherCard, Long> {

    /**
     * Finds all weather cards by user
     *
     * @param user user contained in weather card list
     * */
    List<WeatherCard> findAllByUsersContaining(User user);

    /**
     * Finds optional weather card by location
     *
     * @param location weather location
     * */
    Optional<WeatherCard> findWeatherCardByLocation(String location);

    /**
     * Checks if weather card exists by location
     *
     * @param location location
     * */
    Boolean existsByLocation(String location);
}
