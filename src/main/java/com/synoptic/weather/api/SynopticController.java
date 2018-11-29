package com.synoptic.weather.api;

import com.synoptic.weather.database.dto.WeatherCardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/weather/card/")
@Transactional
@CrossOrigin(origins = "http://localhost:3000")
public class SynopticController {

    @Autowired
    private SynopticService synopticService;

    @RequestMapping(value = "get-all/{email}")
    public ResponseEntity<List<WeatherCardDTO>> getAllWeatherCard(@PathVariable ("email") String email){
          return synopticService.findUserAllWeatherCards(email);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<WeatherCardDTO> addWeatherCard(@RequestBody WeatherCardDTO cardDTO){
             return synopticService.saveWeatherCard(cardDTO);
    }

    @RequestMapping(value = "add-several", method = RequestMethod.POST)
    public ResponseEntity<Set<WeatherCardDTO>> addFewWeatherCards(@RequestBody Set<WeatherCardDTO> cardDTOs){
        return synopticService.saveWeatherCardList(cardDTOs);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResponseEntity deleteCard(@RequestBody WeatherCardDTO cardDTO){
        return synopticService.deleteWeatherCard(cardDTO);
    }
}
