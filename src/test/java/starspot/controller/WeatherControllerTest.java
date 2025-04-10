/**
 * white box Unit tests for WeatherController
 * Mocks the service layer to focus on controller response handling
 */
package starspot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import starspot.model.Weather;
import starspot.service.weather.WeatherService;
import starspot.controller.WeatherController;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the successful retrieval of weather data for a city
     * Verifies correct status code and response content
     */
    @Test
    void testGetWeatherForCityFound() {
        // Arrange
        Weather mockWeather = new Weather();
        mockWeather.setCityName("NewYork");
        mockWeather.setTemperature(25.0);
        mockWeather.setCloudCover(50.0);
        when(weatherService.getWeatherData("NewYork")).thenReturn(mockWeather);

        // Act
        ResponseEntity<?> response = weatherController.getWeatherForCity("NewYork");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NewYork", ((Weather) response.getBody()).getCityName());
        assertEquals(25.0, ((Weather) response.getBody()).getTemperature());
    }

    /**
     * Tests handling of requests for non-existent cities
     * Ensures 404 status is returned with appropriate error message
     */
    @Test
    void testGetWeatherForCityNotFound() {
        Weather mockWeather = new Weather();
        mockWeather.setCityName("UnknownCity");
        when(weatherService.getWeatherData("UnknownCity")).thenReturn(mockWeather);

        ResponseEntity<?> response = weatherController.getWeatherForCity("UnknownCity");

        //check correctness
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cannot support: UnknownCity", response.getBody());
    }

    /**
     * Tests fetching weather data for all cities
     * Checks if response list size and status code match expectations
     */
    @Test
    void testGetAllCitiesWeather() {
        Map<String, Weather> mockWeatherMap = new HashMap<>();
        Weather weather1 = new Weather();
        weather1.setCityName("City1");
        weather1.setTemperature(20.0);
        Weather weather2 = new Weather();
        weather2.setCityName("City2");
        weather2.setTemperature(25.0);
        mockWeatherMap.put("City1", weather1);
        mockWeatherMap.put("City2", weather2);

        when(weatherService.getAllWeatherData()).thenReturn(mockWeatherMap);

        ResponseEntity<List<Weather>> response = weatherController.getAllCitiesWeather();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}