package com.synoptic.weather.api;

import com.synoptic.weather.database.dto.WeatherCardDto;
import com.synoptic.weather.database.entity.WeatherCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SynopticController {

    @Autowired
    private SynopticService synopticService;

    @RequestMapping(value = "api/weather/{email}/weather_card/get_all")
    public ResponseEntity<List<WeatherCard>> getAllWeatherCard(@PathVariable ("email") String email){
          return synopticService.findUserAllWeatherCards(email);
    }

    @RequestMapping(value = "api/weather/weather_card/add", method = RequestMethod.POST)
    public ResponseEntity addWeatherCard(@RequestBody WeatherCardDto weatherCardDto){
             return synopticService.saveWeatherCard(weatherCardDto);
    }

    @RequestMapping(value = "api/weather/weather_card/add_few", method = RequestMethod.POST)
    public ResponseEntity addFewWeatherCards(@RequestBody List<WeatherCardDto> cardDtos){
        return synopticService.saveWeatherCardList(cardDtos);
    }

    @RequestMapping(value = "api/weather/weather_card/delete", method = RequestMethod.POST)
    public ResponseEntity deleteCard(@RequestBody WeatherCardDto weatherCardDto){
        return synopticService.deleteWeatherCard(weatherCardDto);
    }

}
