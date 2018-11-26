package com.synoptic.weather.database.dao;

import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface WeatherCardDao extends CrudRepository<WeatherCard, Long> {

    List<WeatherCard> findAllByUser(User user);

    WeatherCard findWeatherCardById(Long id);
}
