package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dto.UserDTO;
import com.synoptic.weather.database.dto.WeatherCardDTO;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import com.synoptic.weather.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = {"cardDTOs"})
public class SynopticService {

    @Autowired
    private WeatherCardDao cardDao;

    @Autowired
    private ModelManager modelManager;

    @Autowired
    private MetcastBuilder metcastBuilder;

    private List<WeatherCardDTO> cardDTOs;


    public ResponseEntity<List<Long>> saveWeatherCardList(Set<String> locations, String username) {

        User user = modelManager.getUserOrExit(username);
        List<Long> cardIds = new ArrayList<>();
        for (String location : locations) {
            cardIds.add(getOrCreateWeatherCard(location, user).getId());
        }
        return ResponseEntity.ok(cardIds);
    }

    @CachePut
    @CacheEvict(value = "cardDTOs", allEntries=true)
    public WeatherCardDTO getOrCreateWeatherCard(String location, User user) {

        WeatherCardDTO cardDTO = metcastBuilder.fillWeatherCard(WeatherCardDTO.builder()
                .location(location).userDTO(UserDTO.builder().username(user.getUsername()).build()).build());

        WeatherCard card = cardDao.findWeatherCardByLocation(location)
                .orElse(WeatherCard.builder().location(location).user(user).build());

        if (card.getId() == null) {
            cardDao.save(card);
            cardDTO.setId(card.getId());
            cardDTOs.add(cardDTO);
        }
        else {
            for (int i = 0; i < cardDTOs.size(); i++) {
                if (cardDTOs.get(i).getLocation().equals(cardDTO.getLocation()))
                    cardDTOs.set(i, cardDTO);
            }
        }
       return cardDTO;
    }

    @Cacheable
    public ResponseEntity<List<WeatherCardDTO>> findUserAllWeatherCards(String username) {

        simulateSlowService();
        List<WeatherCard> cards = cardDao.findAllByUser(modelManager.getUserOrExit(username));
        cardDTOs  = new ArrayList<>();
        for (WeatherCard card : cards) {
            cardDTOs.add(modelManager.weatherCardToDTO(card));
        }
        cardDTOs.forEach(dto -> metcastBuilder.fillWeatherCard(dto));

        return ResponseEntity.ok(cardDTOs);
    }

    @CacheEvict(allEntries = true, value = "cardDTOs")
    @Scheduled(fixedDelay =  1000 ,  initialDelay = 500)
    public void reportCacheEvict() {
        System.out.println("Flush Cache " + LocalDateTime.now().format(metcastBuilder.formatter));
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @CachePut
    @CacheEvict(value = "cardDTOs", allEntries=true)
    public ResponseEntity<WeatherCardDTO> deleteWeatherCard(String location) {

          WeatherCard weatherCard = modelManager.getWeatherCardOrError(location);
          cardDao.delete(weatherCard);
          cardDTOs.remove(cardDTOs.stream().filter(dto -> dto.getLocation().equals(location)).findAny().get());

        return ResponseEntity.ok(modelManager.weatherCardToDTO(weatherCard));
    }
}
