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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        User user = entityProviderBuilder.getUserOrExit(username);
        List<Long> cardIds = new ArrayList<>();
        for (String location : locations) {

            WeatherCardDTO cardDTO = weatherDataBuilder.fillWeatherCardDTO(WeatherCardDTO.builder().location(location).build());
            WeatherCard card = entityProviderBuilder.getExistingCardOrNewCreated(location, user);
            cardIds.add(updateOrCreateWeatherCard(card, cardDTO, user).getId());
            logger.info("Weather card  of geographic location: " + location + " is adjusted");
        }
        return ResponseEntity.ok(cardIds);
    }

    /**
     * Creates and sets the new weather card or updates it exists and updates cached data
     *
     * @param card    model entity to store weather locations
     * @param cardDTO weather data transfer object to transport data from backend to frontend (DTO)
     * @return filled weather DTO
     * @user current user
     */
    @CachePut
    @CacheEvict(value = "cardDTOs", allEntries = true)
    public WeatherCardDTO updateOrCreateWeatherCard(WeatherCard card, WeatherCardDTO cardDTO, User user) {

        if (card.getId() == null) {
            cardDao.save(card);
            cardDTO.setId(card.getId());
            cardDTOs.add(cardDTO);
            logger.debug("Create new weather card: " + card);
        } else {
            if (!card.getUsers().contains(user)) {
                card.getUsers().add(user);
                cardDao.save(card);
                logger.debug("Update weather card: " + card);
            }
            for (int i = 0; i < cardDTOs.size(); i++) {
                if (cardDTOs.get(i).getLocation().equals(cardDTO.getLocation()))
                    cardDTOs.set(i, cardDTO);
                logger.debug("Update weather card DTO: " + cardDTO + " of user " + user);
            }
        }
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
        List<WeatherCard> cards = cardDao.findAllByUsersContains(entityProviderBuilder.getUserOrExit(username));
        cardDTOs = new ArrayList<>();

        for (WeatherCard card : cards) {
            logger.debug("Adding to list DTO of weather card " + card);
            cardDTOs.add(entityProviderBuilder.weatherCardToDTO(card));
        }
        logger.debug("Filling each weather card DTO with weather data in list: " + cardDTOs);
        cardDTOs.forEach(dto -> weatherDataBuilder.fillWeatherCardDTO(dto));

        return ResponseEntity.ok(cardDTOs);
    }

    /**
     * Evicts cached weather data and prints informational message
     */
    @CacheEvict(allEntries = true, value = "cardDTOs")
    public void reportCacheEvict() {
        logger.info("Cleaning of weather data caches");
        System.out.println("Flush Cache " + LocalDateTime.now().format(weatherDataBuilder.formatter));
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
    @CacheEvict(value = "cardDTOs", allEntries = true)
    public ResponseEntity<WeatherCardDTO> removeWeatherCardDTO(String location, String username) {

        WeatherCard weatherCard = entityProviderBuilder.getWeatherCardOrError(location);
        User user = entityProviderBuilder.getUserOrExit(username);
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
}
