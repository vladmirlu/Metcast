package com.synoptic.weather.database.dao;

import com.synoptic.weather.database.entity.WeatherImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface WeatherImageDao extends CrudRepository<WeatherImage, Long> {
}
