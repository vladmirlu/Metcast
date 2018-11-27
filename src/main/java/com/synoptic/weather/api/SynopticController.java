package com.synoptic.weather.api;

import com.synoptic.weather.database.dto.WeatherCardDto;
import com.synoptic.weather.database.entity.WeatherCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/weather//weather_card/")
@CrossOrigin(origins = "http://localhost:3000")
public class SynopticController {

    @Autowired
    private SynopticService synopticService;

    @RequestMapping(value = "get_all/{email}")
    public ResponseEntity<List<WeatherCard>> getAllWeatherCard(@PathVariable ("email") String email){
          return synopticService.findUserAllWeatherCards(email);
    }

    @RequestMapping(value = "add_one", method = RequestMethod.POST)
    public ResponseEntity<WeatherCard> addWeatherCard(@RequestBody WeatherCardDto weatherCardDto){
             return synopticService.saveWeatherCard(weatherCardDto);
    }

    @RequestMapping(value = "add_few", method = RequestMethod.POST)
    public ResponseEntity<Set<WeatherCard>> addFewWeatherCards(@RequestBody List<WeatherCardDto> cardDtos){
        return synopticService.saveWeatherCardList(cardDtos);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseEntity deleteCard(@RequestBody WeatherCardDto weatherCardDto){
        return synopticService.deleteWeatherCard(weatherCardDto);
    }

}
