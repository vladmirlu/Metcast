package com.synoptic.weather.metcast;

import com.synoptic.weather.model.entity.dto.WeatherCardDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Rest controller of current weather API
 */
@RestController
@RequestMapping("api/weather/cards/")
@Transactional
public class SynopticController {

    private static final Logger logger = Logger.getLogger(SynopticController.class);

    @Autowired
    private SynopticService synopticService;

    /**
     * Gets all weather cards of current user with weather data
     *
     * @param username current user username
     * @return response entity of all user weather cards data
     */
    @GetMapping(value = "{username}")
    public ResponseEntity<List<WeatherCardDTO>> getUserAllWeatherCards(@PathVariable("username") String username) {

        logger.info("Weather cards of user " + username + " are requested");
        return synopticService.findUserAllWeatherCards(username);
    }

    /**
     * Adjusts weather cards of current user with weather data
     *
     * @param username  current user username
     * @param locations weather location to adjust weather data
     * @return response entity of all adjusted weather cards data of current user
     */
    @PostMapping(value = "{username}")
    public ResponseEntity<List<Long>> adjustWeatherCards(@PathVariable("username") String username, @RequestBody Set<String> locations) {

        logger.info("Weather cards locations to add or update " + locations + " of user " + username + " are received ");
        return synopticService.adjustWeatherCardList(locations, username);
    }

    /**
     * Deletes current location weather data of current user
     *
     * @param username current user username
     * @param location weather location to remove weather data
     * @return response entity of deleted weather data of current user
     */
    @DeleteMapping(value = "{location}/{username}")
    public ResponseEntity<WeatherCardDTO> deleteCard(@PathVariable("location") String location, @PathVariable("username") String username) {

        logger.info("Weather card location to delete " + location + " of user " + username + " are received");
        return synopticService.removeWeatherCardDTO(location, username);
    }
}
