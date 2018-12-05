package com.synoptic.weather.api;

import com.synoptic.weather.database.dao.WeatherCardDao;
import com.synoptic.weather.database.dto.WeatherCardDTO;
import com.synoptic.weather.database.entity.User;
import com.synoptic.weather.database.entity.WeatherCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service for organising weather data for rest controller
 *
 * */
@Service
@CacheConfig(cacheNames = {"cardDTOs"})
public class SynopticService {

    @Autowired
    private WeatherCardDao cardDao;

    @Autowired
    private ModelManager modelManager;

    @Autowired
    private WeatherDataBuilder weatherDataBuilder;

    /**
     * List of weather data transfer objects
     * */
    private List<WeatherCardDTO> cardDTOs;

    /**
     * Adjusts weather data lists for exact locations
     *
     *@param locations exact weather locations
     * @param username username of current user
     * @return response entity with locations iDs
     * */
    public ResponseEntity<List<Long>> adjustWeatherCardList(Set<String> locations, String username) {

        User user = modelManager.getUserOrExit(username);
        List<Long> cardIds = new ArrayList<>();
        for (String location : locations) {

            WeatherCardDTO cardDTO = weatherDataBuilder.fillWeatherCardDTO(WeatherCardDTO.builder().location(location).build());
            WeatherCard card = modelManager.getExistingCardOrNewCreated(location, user);
            cardIds.add(updateOrCreateWeatherCard(card, cardDTO, user).getId());
        }
        return ResponseEntity.ok(cardIds);
    }

    /**
     *Creates and sets the new weather card or updates it exists and updates cached data
     *
     * @param card database entity to store weather locations
     * @param cardDTO weather data transfer object to transport data from backend to frontend (DTO)
     * @user current user
     * @return filled weather DTO
     * */
    @CachePut
    @CacheEvict(value = "cardDTOs", allEntries=true)
    public WeatherCardDTO updateOrCreateWeatherCard(WeatherCard card, WeatherCardDTO cardDTO, User user) {

        if (card.getId() == null) {
            cardDao.save(card);
            cardDTO.setId(card.getId());
            cardDTOs.add(cardDTO);
        }
        else {
            if(!card.getUsers().contains(user)){
                card.getUsers().add(user);
                cardDao.save(card);
            }
            for (int i = 0; i < cardDTOs.size(); i++) {
                if (cardDTOs.get(i).getLocation().equals(cardDTO.getLocation()))
                    cardDTOs.set(i, cardDTO);
            }
        }
       return cardDTO;
    }

    /**
     * Finds all weather cards of current user
     *
     * @param username current user username
     * @return response entity of weather card DTOs
     * */
    @Cacheable
    public ResponseEntity<List<WeatherCardDTO>> findUserAllWeatherCards(String username) {

        List<WeatherCard> cards = cardDao.findAllByUsersContains(modelManager.getUserOrExit(username));
        cardDTOs  = new ArrayList<>();
        for (WeatherCard card : cards) {
            cardDTOs.add(modelManager.weatherCardToDTO(card));
        }
        cardDTOs.forEach(dto -> weatherDataBuilder.fillWeatherCardDTO(dto));

        return ResponseEntity.ok(cardDTOs);
    }

    /**
     *Evicts cached weather data and prints informational message
     * */
    @CacheEvict(allEntries = true, value = "cardDTOs")
    public void reportCacheEvict() {
        System.out.println("Flush Cache " + LocalDateTime.now().format(weatherDataBuilder.formatter));
    }

    /**
     * Removes current user weather card data,
     * Deletes weather card from database if it is not uses by other users
     * removes weather data from weather DTO list and updates cached data
     *
     * @param location current weather location
     * @param username current user username
     * @return response entity of deleted weather card DTO
     * */
    @CachePut
    @CacheEvict(value = "cardDTOs", allEntries=true)
    public ResponseEntity<WeatherCardDTO> removeWeatherCardDTO(String location, String username) {

          WeatherCard weatherCard = modelManager.getWeatherCardOrError(location);
          User user = modelManager.getUserOrExit(username);

          if(weatherCard.getUsers().contains(user)){
              weatherCard.getUsers().remove(user);
              cardDao.save(weatherCard);
          }
          if(weatherCard.getUsers().isEmpty()){
              cardDao.delete(weatherCard);
          }
          cardDTOs.remove(cardDTOs.stream().filter(dto -> dto.getLocation().equals(location)).findAny().get());

        return ResponseEntity.ok(modelManager.weatherCardToDTO(weatherCard));
    }
}
