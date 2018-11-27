package com.synoptic.weather.validation;

import com.synoptic.weather.database.dao.UserDao;
import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import com.synoptic.weather.exception.UserNotFoundException;
import com.synoptic.weather.exception.WeatherCardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Validator {

    @Autowired
    private UserDao userDao;

    @Autowired
    private WeatherCardDao cardDao;

    public User getUserOrExit(String email) throws UserNotFoundException{
        User user = userDao.findByEmail(email);
        if(user != null){
            return user;
        }
        throw new UserNotFoundException();
    }

    public WeatherCard getWeatherCardOrError(String location) throws WeatherCardNotFoundException{

        WeatherCard weatherCard = cardDao.findWeatherCardByLocation(location);
        if(weatherCard != null){
            return weatherCard;
        }
        throw new WeatherCardNotFoundException();
    }

    public boolean isWeatherCardExist(String location){
        return cardDao.findWeatherCardByLocation(location) != null;
    }
}
