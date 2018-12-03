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

    public ResponseEntity<List<Long>> saveWeatherCardList(Set<String> locations, String username) {

        User user = modelManager.getUserOrExit(username);
        List<Long> cardIds = new ArrayList<>();
        for (String location : locations) {
            cardIds.add(updateOrCreateWeatherCard(location, user).getId());
        }
        return ResponseEntity.ok(cardIds);
    }

    private WeatherCard updateOrCreateWeatherCard(String location, User user) {

        WeatherCard card = cardDao.findWeatherCardByLocation(location);
        if (card != null) {
            unitDao.deleteAll(card.getWeatherUnits());
            card = metcastBuilder.fillWeatherCard(card);

        } else {
            card = metcastBuilder.fillWeatherCard(WeatherCard.builder().location(location).user(user).build());
        }
        unitDao.saveAll(card.getWeatherUnits());
        cardDao.save(card);
        return card;
    }

    public ResponseEntity<List<WeatherCardDTO>> findUserAllWeatherCards(String username) {

        List<WeatherCardDTO> cardDTOs = new ArrayList<>();
        cardDao.findAllByUser(modelManager.getUserOrExit(username)).forEach(card -> cardDTOs.add(modelManager.weatherCardToDTO(card)));
        return ResponseEntity.ok(cardDTOs);
    }

    public ResponseEntity<WeatherCardDTO> deleteWeatherCard(String location) {

        WeatherCard weatherCard = modelManager.getWeatherCardOrError(location);
        unitDao.deleteAll(weatherCard.getWeatherUnits());
        cardDao.delete(weatherCard);
        return ResponseEntity.ok(modelManager.weatherCardToDTO(weatherCard));
    }
}
