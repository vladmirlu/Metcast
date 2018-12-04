package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dto.WeatherCardDTO;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@CacheConfig(cacheNames={"cardDTOs"})
public class SynopticService {

    @Autowired
    private WeatherCardDao cardDao;

    @Autowired
    private ModelManager modelManager;

    @Autowired
    private MetcastBuilder metcastBuilder;

    @CachePut
    public ResponseEntity<List<Long>> saveWeatherCardList(Set<String> locations, String username) {

        User user = modelManager.getUserOrExit(username);
        List<Long> cardIds = new ArrayList<>();
        for (String location : locations) {
            cardIds.add(updateOrCreateWeatherCard(location, user).getId());
        }
        return ResponseEntity.ok(cardIds);
    }

    private WeatherCardDTO updateOrCreateWeatherCard(String location, User user) {

        WeatherCard card = cardDao.findWeatherCardByLocation(location);
        WeatherCardDTO cardDTO;
        if (card != null) {
            card = metcastBuilder.fillWeatherCard(card);
        } else {
            card = metcastBuilder.fillWeatherCard(WeatherCard.builder().location(location).user(user).build());
        }
        cardDao.save(card);
        return card;
    }


    public ResponseEntity<List<WeatherCardDTO>> findUserAllWeatherCards(String username) {

        List<WeatherCardDTO> cardDTOs = new ArrayList<>();
        List<WeatherCard> weatherCards = cardDao.findAllByUser(modelManager.getUserOrExit(username));
        for(WeatherCard card: weatherCards){
            cardDTOs.add(modelManager.weatherCardToDTO(card));
        }
        cardDTOs = fillWeatherCardDTOs(cardDTOs);

        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS)).body(cardDTOs);
    }

    @Cacheable
        public List<WeatherCardDTO> fillWeatherCardDTOs(List<WeatherCardDTO> cardDTOs){
        simulateSlowService();
        cardDTOs.forEach(dto -> metcastBuilder.fillWeatherCard(dto));
        return cardDTOs;
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @CachePut
    public ResponseEntity<WeatherCardDTO> deleteWeatherCard(String location) {

        WeatherCard weatherCard = modelManager.getWeatherCardOrError(location);
        cardDao.delete(weatherCard);
        return ResponseEntity.ok(modelManager.weatherCardToDTO(weatherCard));
    }
}
