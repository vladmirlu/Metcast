package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.UserDao;
import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dto.UserDTO;
import com.synoptic.weather.database.dto.WeatherCardDTO;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import com.synoptic.weather.exception.ResourceNotFoundException;
import com.synoptic.weather.exception.UserNotFoundException;
import com.synoptic.weather.exception.WeatherCardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelManager {

    @Autowired
    private UserDao userDao;

    @Autowired
    private WeatherCardDao cardDao;

    public User getUserOrExit(String username) {
        User user = userDao.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return user;
    }

    public WeatherCard getWeatherCardOrError(String location) {

        WeatherCard weatherCard = cardDao.findWeatherCardByLocation(location);
        if(weatherCard != null){
            return weatherCard;
        }
        throw new WeatherCardNotFoundException();
    }

    public WeatherCardDTO weatherCardToDTO(WeatherCard weatherCard){

        return WeatherCardDTO.builder().id(weatherCard.getId()).location(weatherCard.getLocation())
                .userDTO(userToDTO(weatherCard.getUser())).build();
    }

    private UserDTO userToDTO(User user){
        return UserDTO.builder().email(user.getEmail()).build();
    }

}
