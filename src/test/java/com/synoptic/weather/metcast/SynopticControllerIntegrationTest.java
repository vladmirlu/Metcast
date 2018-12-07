package com.synoptic.weather.metcast;

import com.synoptic.weather.authentication.security.WebMvcConfig;
import com.synoptic.weather.model.entity.dto.WeatherCardDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class})
public class SynopticControllerIntegrationTest {

    private String baseURL;

    private String username;

    private String location;

    List<WeatherCardDTO> cardDTOS;

    Set<String> locations;

    List<Long> cardIds;

    @Autowired
    private SynopticService synopticService;

    private WeatherCardDTO cardDTO;

    @Before
    public void init(){

        MockitoAnnotations.initMocks(this);

        synopticService = new SynopticService();

        baseURL = "http://localhost:8181/api/weather/cards/";
        username = "vlad";
        location = "Buffalo";
        locations = Collections.singleton("Kansas");
        cardDTO = WeatherCardDTO.builder().id(1L).location(location).weatherUnitDTOS(Collections.emptyList()).build();
        cardDTOS = Arrays.asList(cardDTO);
        cardIds = Arrays.asList(1L, 2L, 3L);

    }

    @Test
    public void getUserAllWeatherCardsTest() {

        ResponseEntity<List<WeatherCardDTO>> responseEntity = synopticService.findUserAllWeatherCards(username);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

   /* public void validateCORSHttpHeaders(HttpHeaders headers){
        assertThat(headers.getAccessControlAllowOrigin(), is("*"));
        assertThat(headers.getAccessControlAllowHeaders(), hasItem("*"));
        assertThat(headers.getAccessControlMaxAge(), is(3600L));
        assertThat(headers.getAccessControlAllowMethods(), hasItems(
                HttpMethod.GET,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.OPTIONS,
                HttpMethod.DELETE));
    }*/
}
