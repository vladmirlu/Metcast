package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dao.WeatherUnitDao;
import com.synoptic.weather.database.dto.WeatherCardDto;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import com.synoptic.weather.database.entity.WeatherUnit;
import com.synoptic.weather.exception.UserNotFoundException;
import com.synoptic.weather.exception.WeatherCardNotFoundException;
import com.synoptic.weather.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SynopticService {

    @Autowired
    private WeatherCardDao cardDao;

    @Autowired
    private WeatherUnitDao unitDao;

    @Autowired
    private Validator validator;

    @Autowired
    private MetcastBuilder metcastBuilder;

    /*@PostConstruct
    public void print() throws Exception {
        String location = "Kiev";
        if (validator.isWeatherCardExist(location)) {
            WeatherCard weatherCard = cardDao.findWeatherCardByLocation(location);
            List<WeatherUnit> units = weatherCard.getWeatherUnits();
            weatherCard = metcastBuilder.fillWeatherCard(weatherCard);
            unitDao.saveAll(weatherCard.getWeatherUnits());
            cardDao.save(weatherCard);
            unitDao.deleteAll(units);
        } else {
            WeatherCard weatherCard = metcastBuilder.fillWeatherCard(WeatherCard.builder().location(location).build());
            unitDao.saveAll(weatherCard.getWeatherUnits());
            cardDao.save(weatherCard);
        }
    }*/


    public ResponseEntity<WeatherCard> saveWeatherCard(WeatherCardDto cardDto) {

        WeatherCard weatherCard;
        try {
            User user = validator.getUserOrExit(cardDto.getUser().getEmail());
            weatherCard = updateOrCreateWeatherCard(cardDto, user);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException io) {
            io.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(weatherCard);
    }

    private WeatherCard updateOrCreateWeatherCard(WeatherCardDto cardDto, User user) throws IOException {

        if (validator.isWeatherCardExist(cardDto.getLocation())) {
            WeatherCard weatherCard = cardDao.findWeatherCardByLocation(cardDto.getLocation());
            List<WeatherUnit> units = weatherCard.getWeatherUnits();
            weatherCard = metcastBuilder.fillWeatherCard(weatherCard);
            unitDao.saveAll(weatherCard.getWeatherUnits());
            cardDao.save(weatherCard);
            unitDao.deleteAll(units);
            return weatherCard;
        } else {
            WeatherCard weatherCard = metcastBuilder.fillWeatherCard(WeatherCard.builder().location(cardDto.getLocation()).user(user).build());
            unitDao.saveAll(weatherCard.getWeatherUnits());
            cardDao.save(weatherCard);
            return weatherCard;
        }
    }

    public ResponseEntity<Set<WeatherCard>> saveWeatherCardList(List<WeatherCardDto> cardDtos) {

        Set<WeatherCard> weatherCards = new HashSet<>();
        try {
            User user = validator.getUserOrExit(cardDtos.get(0).getUser().getEmail());
            for (WeatherCardDto cardDto : cardDtos) {
                weatherCards.add(updateOrCreateWeatherCard(cardDto, user));
            }
            cardDao.saveAll(weatherCards);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(weatherCards);
    }

    public ResponseEntity<List<WeatherCard>> findUserAllWeatherCards(String email) {
        try {
            User user = validator.getUserOrExit(email);
            return ResponseEntity.ok(cardDao.findAllByUser(user));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity deleteWeatherCard(WeatherCardDto cardDto) {
        try {
            WeatherCard weatherCard = validator.getWeatherCardOrError(cardDto.getLocation());
            cardDao.delete(weatherCard);
        } catch (WeatherCardNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
