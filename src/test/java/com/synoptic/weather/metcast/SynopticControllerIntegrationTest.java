package com.synoptic.weather.metcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synoptic.weather.ApplicationStart;
import com.synoptic.weather.model.entity.WeatherCard;
import com.synoptic.weather.model.entity.dto.WeatherCardDTO;
import com.synoptic.weather.model.entity.dto.WeatherUnitDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@TestPropertySource("classpath:application.properties")
@SpringBootTest(classes = {ApplicationStart.class})
@WebAppConfiguration
//@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SynopticControllerIntegrationTest {

    private String baseURL;

    private String username;

    private String location;

    List<WeatherCardDTO> cardDTOS;

    Set<String> locations;

    @Mock
    private SynopticService synopticService;

    @InjectMocks
    private SynopticController synopticController;

    private WeatherCardDTO cardDTO;

    private MockMvc mockMvc;

    @Before
    public void init(){

        mockMvc = MockMvcBuilders.standaloneSetup(synopticController).build();

        baseURL = "http://localhost:8181/api/weather/cards/";
        username = "vlad";
        location = "Kiev";
        locations = Collections.singleton("Kiev");
        cardDTO = WeatherCardDTO.builder().id(1L).location(location).weatherUnitDTOS(Collections.emptyList()).build();
        cardDTOS = Arrays.asList(cardDTO);

        when(synopticService.setWeather(Mockito.any(WeatherCardDTO.class))).thenReturn(cardDTO);
    }

    @Test
    public void getUserAllWeatherCardsTest() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(baseURL + username))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200,
                mvcResult.getResponse().getStatus());
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
