package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dao.WeatherUnitDao;
import com.synoptic.weather.database.dto.WeatherCardDto;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import com.synoptic.weather.exception.UserNotFoundException;
import com.synoptic.weather.exception.WeatherCardNotFoundException;
import com.synoptic.weather.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


    public ResponseEntity saveWeatherCard(WeatherCardDto cardDto){
        try {
            User user = validator.getUserOrExit(cardDto.getUser().getEmail());
            WeatherCard weatherCard = metcastBuilder.createWeatherCard(cardDto.getLocation());
            weatherCard.setUser(user);
            unitDao.saveAll(weatherCard.getWeatherUnits());
            cardDao.save(weatherCard);
        }catch (UserNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }catch (IOException io){
            io.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
         return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity saveWeatherCardList(List<WeatherCardDto> cardDtos){
        try{
            User user = validator.getUserOrExit(cardDtos.get(0).getUser().getEmail());
            List<WeatherCard> weatherCards = new ArrayList<>();
            for(WeatherCardDto cardDto: cardDtos){
                WeatherCard weatherCard = metcastBuilder.createWeatherCard(cardDto.getLocation());
                weatherCard.setUser(user);
                weatherCards.add(weatherCard);
            }
           cardDao.saveAll(weatherCards);
        }catch (UserNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity<List<WeatherCard>> findUserAllWeatherCards(String email){
        try {
            User user = validator.getUserOrExit(email);
            return ResponseEntity.ok(cardDao.findAllByUser(user));
        }catch (UserNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity deleteWeatherCard(WeatherCardDto cardDto){
        try {
            WeatherCard weatherCard = validator.getWeatherCardOrError(cardDto.getId());
            cardDao.delete(weatherCard);
        }catch (WeatherCardNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
