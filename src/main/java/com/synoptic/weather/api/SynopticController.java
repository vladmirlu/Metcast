package com.synoptic.weather.api;

import com.synoptic.weather.database.dto.WeatherCardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/weather/cards/")
@Transactional
@CrossOrigin(origins = "http://localhost:3000")
public class SynopticController {

    @Autowired
    private SynopticService synopticService;

    @RequestMapping(value = "get-all/{username}")
    public ResponseEntity<List<WeatherCardDTO>> getAllWeatherCard(@PathVariable ("username") String username){
          return synopticService.findUserAllWeatherCards(username);
    }

    @RequestMapping(value = "add/{username}", method = RequestMethod.POST)
    public ResponseEntity<List<Long>> addFewWeatherCards(@PathVariable ("username") String username, @RequestBody Set<String> locations){
        return synopticService.saveWeatherCardList(locations, username);
    }

    @RequestMapping(value = "delete/{location}", method = RequestMethod.DELETE)
    public ResponseEntity<WeatherCardDTO> deleteCard(@PathVariable("location") String location){
        return synopticService.deleteWeatherCard(location);
    }
}
