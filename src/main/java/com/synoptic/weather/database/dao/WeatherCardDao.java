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

@Transactional
public interface WeatherCardDao extends CrudRepository<WeatherCard, Long> {

    List<WeatherCard> findAllByUser(User user);

    WeatherCard findWeatherCardByLocation(String location);

    List <WeatherCard> findAllByLocationIn (Set<String> locations);
}
