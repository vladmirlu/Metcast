package com.synoptic.weather.api;

import com.synoptic.weather.database.dto.WeatherCardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Rest controller of current weather API
 * */
@RestController
@RequestMapping("api/weather/cards/")
@Transactional
public class SynopticController {

    @Autowired
    private SynopticService synopticService;

    /**
     * Gets all weather cards of current user with weather data
     *
     * @param username current user username
     * @return response entity of all user weather cards data
     * */
    @GetMapping(value = "get-all/{username}")
    public ResponseEntity<List<WeatherCardDTO>> getUserAllWeatherCard(@PathVariable ("username") String username){
          return synopticService.findUserAllWeatherCards(username);
    }

    /**
     * Adjusts weather cards of current user with weather data
     *
     * @param username current user username
     * @param locations weather location to adjust weather data
     * @return response entity of all adjusted weather cards data of current user
     * */
    @PostMapping(value = "add/{username}")
    public ResponseEntity<List<Long>> adjustWeatherCards(@PathVariable ("username") String username, @RequestBody Set<String> locations){
        return synopticService.adjustWeatherCardList(locations, username);
    }

    /**
     * Deletes current location weather data of current user
     *
     * @param username current user username
     * @param location weather location to remove weather data
     * @return response entity of deleted weather data of current user
     * */
    @DeleteMapping(value = "delete/{location}/{username}")
    public ResponseEntity<WeatherCardDTO> deleteCard(@PathVariable("location") String location, @PathVariable("username") String username){
        return synopticService.removeWeatherCardDTO(location, username);
    }
}
