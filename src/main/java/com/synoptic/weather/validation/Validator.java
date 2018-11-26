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

    public WeatherCard getWeatherCardOrError(long id) throws WeatherCardNotFoundException{

        WeatherCard weatherCard = cardDao.findWeatherCardById(id);
        if(weatherCard != null){
            return weatherCard;
        }
        throw new WeatherCardNotFoundException();
    }
}
