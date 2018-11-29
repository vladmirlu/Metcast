package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dao.WeatherUnitDao;
import com.synoptic.weather.database.dto.WeatherCardDTO;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private ModelManager modelManager;

    @Autowired
    private MetcastBuilder metcastBuilder;

    public ResponseEntity<WeatherCardDTO> saveWeatherCard(WeatherCardDTO cardDTO) {

        User user = modelManager.getUserOrExit(cardDTO.getUserDTO().getEmail());
        return ResponseEntity.ok(modelManager.weatherCardToDTO(updateOrCreateWeatherCard(cardDTO, user)));
    }

    public ResponseEntity<Set<WeatherCardDTO>> saveWeatherCardList(Set<WeatherCardDTO> cardDTOs) {

        User user = modelManager.getUserOrExit(cardDTOs.iterator().next().getUserDTO().getEmail());
        for (WeatherCardDTO cardDTO : cardDTOs) {
            cardDTOs.remove(cardDTO);
            cardDTOs.add(modelManager.weatherCardToDTO(updateOrCreateWeatherCard(cardDTO, user)));
        }
        return ResponseEntity.ok(cardDTOs);
    }

    private WeatherCard updateOrCreateWeatherCard(WeatherCardDTO cardDto, User user) {

        WeatherCard card = cardDao.findWeatherCardByLocation(cardDto.getLocation());
        if (card != null) {
            unitDao.deleteAll(card.getWeatherUnits());
            card = metcastBuilder.fillWeatherCard(card);

        } else {
            card = metcastBuilder.fillWeatherCard(WeatherCard.builder().location(cardDto.getLocation()).user(user).build());
        }
        unitDao.saveAll(card.getWeatherUnits());
        cardDao.save(card);
        return card;
    }

    public ResponseEntity<List<WeatherCardDTO>> findUserAllWeatherCards(String email) {

        List<WeatherCardDTO> cardDTOs = new ArrayList<>();
        cardDao.findAllByUser(modelManager.getUserOrExit(email)).forEach(card -> cardDTOs.add(modelManager.weatherCardToDTO(card)));
        return ResponseEntity.ok(cardDTOs);
    }

    public ResponseEntity deleteWeatherCard(WeatherCardDTO cardDto) {

        WeatherCard weatherCard = modelManager.getWeatherCardOrError(cardDto.getLocation());
        unitDao.deleteAll(weatherCard.getWeatherUnits());
        cardDao.delete(weatherCard);
        return new ResponseEntity(HttpStatus.OK);
    }
}
