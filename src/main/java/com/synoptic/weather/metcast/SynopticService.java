package com.synoptic.weather.metcast;

import com.synoptic.weather.model.repository.WeatherCardDao;
import com.synoptic.weather.model.entity.dto.WeatherCardDTO;
import com.synoptic.weather.model.entity.User;
import com.synoptic.weather.model.entity.WeatherCard;
import com.synoptic.weather.provider.EntityProviderBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Service for organising weather data for rest controller
 */
@Service
@CacheConfig(cacheNames = {"cardDTOs"})
public class SynopticService {

    private static final Logger logger = Logger.getLogger(SynopticService.class);

    @Autowired
    private WeatherCardDao cardDao;

    @Autowired
    private EntityProviderBuilder entityProviderBuilder;

    @Autowired
    private WeatherDataBuilder weatherDataBuilder;

    /**
     * List of weather data transfer objects
     */
    private List<WeatherCardDTO> cardDTOs;

    /**
     * Adjusts weather data lists for exact locations
     *
     * @param locations exact weather locations
     * @param username  username of current user
     * @return response entity with locations iDs
     */
    public ResponseEntity<List<Long>> adjustWeatherCardList(Set<String> locations, String username) {

        User user = entityProviderBuilder.getUserByUsername(username);
        List<Long> cardIds = new ArrayList<>();
        for (String location : locations) {

            WeatherCardDTO cardDTO = setWeather(WeatherCardDTO.builder().location(location).build());
            WeatherCard card = entityProviderBuilder.getExistingCardOrCreated(location, user);
            cardIds.add(saveWeatherCard(card, cardDTO, user).getId());
            logger.info("Weather card  of geographic location: " + location + " is adjusted");
        }
        return ResponseEntity.ok(cardIds);
    }

    /**
     * Creates and sets the new weather card or updates it exists
     *
     * @param card    model entity to store weather locations
     * @param cardDTO weather data transfer object to transport data from backend to frontend (DTO)
     * @return filled weather DTO
     * @user current user
     */
    public WeatherCardDTO saveWeatherCard(WeatherCard card, WeatherCardDTO cardDTO, User user) {

        if (!card.getUsers().contains(user)) {
            card.getUsers().add(user);
        }
        logger.debug("Update weather card: " + card);
        cardDao.save(card);
        cardDTO.setId(card.getId());

        logger.debug("Update List<WeatherCardDTO> :" + cardDTOs + " with WeatherCardDTO: " + cardDTO + " of user: " + user);

        return updateUserCardDTOs(cardDTO);
    }

    /**
     * Sets or adds weather card DTO to DTO list and updates it exists and updates cached data
     *
     * @param cardDTO weather data transfer object to transport data from backend to frontend (DTO)
     * @return filled weather DTO
     */
    @CachePut
    public WeatherCardDTO updateUserCardDTOs(WeatherCardDTO cardDTO) {

        boolean exists = false;
        for (int i = 0; i < cardDTOs.size(); i++) {
            logger.debug("Update List<WeatherCardDTO> :" + cardDTOs + " with WeatherCardDTO: " + cardDTO);
            if (cardDTOs.get(i).getLocation().equals(cardDTO.getLocation())) {
                cardDTOs.set(i, cardDTO);
                exists = true;
            }
        }
        if(!exists) cardDTOs.add(cardDTO);
        return cardDTO;
    }

    /**
     * Finds all weather cards of current user
     *
     * @param username current user username
     * @return response entity of weather card DTOs
     */
    @Cacheable
    public ResponseEntity<List<WeatherCardDTO>> findUserAllWeatherCards(String username) {

        logger.debug("Weather cards of user " + username + " are searching");
        List<WeatherCard> cards = cardDao.findAllByUsersContaining(entityProviderBuilder.getUserByUsername(username));
        cardDTOs = new ArrayList<>();

        for (WeatherCard card : cards) {
            logger.debug("Adding to list DTO of weather card " + card);
            cardDTOs.add(entityProviderBuilder.weatherCardToDTO(card));
        }
        logger.debug("Filling each weather card DTO with weather data in list: " + cardDTOs);
        cardDTOs.forEach(dto -> dto = setWeather(dto));

        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS)).body(cardDTOs);
    }

    /**
     * Removes current user weather card data,
     * Deletes weather card from model if it is not uses by other users
     * removes weather data from weather DTO list and updates cached data
     *
     * @param location current weather location
     * @param username current user username
     * @return response entity of deleted weather card DTO
     */
    @CachePut
    public ResponseEntity<WeatherCardDTO> removeWeatherCardDTO(String location, String username) {

        WeatherCard weatherCard = entityProviderBuilder.getWeatherCardByLocation(location);
        User user = entityProviderBuilder.getUserByUsername(username);
        logger.debug("Received data from database: user:" + user + "; weather card: " + weatherCard);

        if (weatherCard.getUsers().contains(user)) {
            weatherCard.getUsers().remove(user);
            cardDao.save(weatherCard);
            logger.debug("Delete user " + user + " from users list of weather card " + weatherCard);
        }
        if (weatherCard.getUsers().isEmpty()) {
            cardDao.delete(weatherCard);
            logger.debug("Delete user weather card " + weatherCard);
        }
        cardDTOs.remove(cardDTOs.stream().filter(dto -> dto.getLocation().equals(location)).findAny().get());
        logger.debug("Delete weather card DTO of location " + location + " from weather card DTO list of user " + user);

        return ResponseEntity.ok(entityProviderBuilder.weatherCardToDTO(weatherCard));
    }

    public WeatherCardDTO setWeather(WeatherCardDTO cardDTO){

        return weatherDataBuilder.fillWeatherCardDTO(cardDTO);
    }

    /**
     * Evicts cached weather data and prints informational message
     */
    @CacheEvict(allEntries = true, value = "cardDTOs")
    public void reportCacheEvict() {
        logger.info("Cleaning of weather data caches");
        System.out.println("Flush Cache " + LocalDateTime.now());
    }

}
