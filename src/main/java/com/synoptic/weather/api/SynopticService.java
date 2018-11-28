package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dao.WeatherUnitDao;
import com.synoptic.weather.database.dto.UserDTO;
import com.synoptic.weather.database.dto.WeatherCardDTO;
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

import java.io.IOException;
import java.util.ArrayList;
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

    public ResponseEntity<WeatherCardDTO> saveWeatherCard(WeatherCardDTO cardDto) {

        WeatherCard weatherCard;
        try {
            User user = validator.getUserOrExit(cardDto.getUserDTO().getEmail());
            weatherCard = updateOrCreateWeatherCard(cardDto, user);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException io) {
            io.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(weatherCardToDTO(weatherCard));
    }

    private WeatherCardDTO weatherCardToDTO(WeatherCard weatherCard){

        return WeatherCardDTO.builder().location(weatherCard.getLocation())
                .weatherUnits(weatherCard.getWeatherUnits())
                .userDTO(userToDTO(weatherCard.getUser())).build();
    }

    private UserDTO userToDTO(User user){
        return UserDTO.builder().email(user.getEmail()).build();
    }

    private WeatherCard updateOrCreateWeatherCard(WeatherCardDTO cardDto, User user) throws IOException {

        if (validator.isWeatherCardExist(cardDto.getLocation())) {
            WeatherCard card = cardDao.findWeatherCardByLocation(cardDto.getLocation());
            List<WeatherUnit> units = card.getWeatherUnits();
            card = metcastBuilder.fillWeatherCard(card);
            unitDao.saveAll(card.getWeatherUnits());
            unitDao.deleteAll(units);
            cardDao.save(card);
            return card;
        } else {
            WeatherCard weatherCard = metcastBuilder.fillWeatherCard(WeatherCard.builder().location(cardDto.getLocation()).user(user).build());
            unitDao.saveAll(weatherCard.getWeatherUnits());
            cardDao.save(weatherCard);
            return weatherCard;
        }
    }

    public ResponseEntity<Set<WeatherCardDTO>> saveWeatherCardList(Set<WeatherCardDTO> cardDTOs) {
        try {
            User user = validator.getUserOrExit(cardDTOs.iterator().next().getUserDTO().getEmail());
            for (WeatherCardDTO cardDTO : cardDTOs) {
                cardDTO = weatherCardToDTO(updateOrCreateWeatherCard(cardDTO, user));
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(cardDTOs);
    }

    public ResponseEntity<List<WeatherCardDTO>> findUserAllWeatherCards(String email) {
        try {
            List<WeatherCard> cards = cardDao.findAllByUser(validator.getUserOrExit(email));
            List<WeatherCardDTO> cardDTOs = new ArrayList<>();
            cards.forEach(card-> cardDTOs.add(weatherCardToDTO(card)));
            return ResponseEntity.ok(cardDTOs);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity deleteWeatherCard(WeatherCardDTO cardDto) {
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
