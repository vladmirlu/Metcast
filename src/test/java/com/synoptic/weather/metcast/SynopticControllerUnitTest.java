package com.synoptic.weather.metcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synoptic.weather.authentication.security.jwt.JwtAuthenticationFilter;
import com.synoptic.weather.model.entity.dto.WeatherCardDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class SynopticControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private SynopticService synopticService;

    @InjectMocks
    private SynopticController synopticController;

    private String baseURL;

    private String username;

    private String location;

    List<WeatherCardDTO> cardDTOS;

    Set<String> locations;

    List<Long> cardIds;

    @Before
    public void init(){

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(synopticController)
                .build();

        baseURL = "http://localhost:8181/api/weather/cards/";
        username = "Brandon";
        location = "Buffalo";
        locations = Collections.singleton("Kansas");
        cardDTOS = Arrays.asList(WeatherCardDTO.builder().id(1L).location(location).weatherUnitDTOS(Collections.emptyList()).build());
        cardIds = Arrays.asList(1L, 2L, 3L);
    }

    @Test
   public void getUserAllWeatherCardsTest() throws Exception {

        when(synopticService.findUserAllWeatherCards(username)).thenReturn(cardDTOS);
        mockMvc.perform(get(baseURL + username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].location", is(location)));

        verify(synopticService, times(1)).findUserAllWeatherCards(username);
        verifyNoMoreInteractions(synopticService);
        assertEquals(cardDTOS, synopticController.getUserAllWeatherCards(username).getBody());
    }

    @Test
    public void adjustWeatherCardsTest() throws Exception {

        when(synopticService.adjustWeatherCardList(locations, username)).thenReturn(ResponseEntity.ok(cardIds));
        mockMvc.perform(post(baseURL  + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(locations)))
                .andExpect(status().isOk());

        verify(synopticService, times(1)).adjustWeatherCardList(locations, username);
        verifyNoMoreInteractions(synopticService);
        assertEquals(cardIds, synopticController.adjustWeatherCards(username, locations).getBody());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
