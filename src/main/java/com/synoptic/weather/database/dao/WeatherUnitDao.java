package com.synoptic.weather.database.dao;

import com.synoptic.weather.database.entity.WeatherUnit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface WeatherUnitDao extends CrudRepository<WeatherUnit, Long> {
}
