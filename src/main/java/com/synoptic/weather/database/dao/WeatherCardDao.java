package com.synoptic.weather.database.dao;

import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * WeatherCard repository to interact with database
 */
@Transactional
public interface WeatherCardDao extends CrudRepository<WeatherCard, Long> {

    /**
     * Finds all weather cards by user
     *
     * @param user user contained in weather card list
     * */
    List<WeatherCard> findAllByUsersContains(User user);

    /**
     * Finds optional weather card by location
     *
     * @param location weather location
     * */
    Optional<WeatherCard> findWeatherCardByLocation(String location);
}
