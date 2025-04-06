//package starspot.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import starspot.model.Weather;
//import starspot.service.weather.WeatherService;
//
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Map;
//import org.springframework.http.HttpStatus;
//
//@RestController
//@RequestMapping("/api/weather")
//@CrossOrigin
//public class WeatherController {
//
//    @Autowired
//    private WeatherService weatherService;
//
//    @GetMapping("/{city}")
//    public ResponseEntity<?> getWeatherForCity(@PathVariable String city) {
//        System.out.println("Fetching weather data for city: " + city);
//        try {
//            Weather weather = weatherService.getWeatherData(city);
//            System.out.println("Successfully retrieved weather data for " + city + ": " + weather.getTemperature() + "°C");
//            return ResponseEntity.ok(weather);
//        } catch (IllegalArgumentException e) {
//            System.out.println("Requested unsupported city: " + city);
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body("City not supported: " + city);
//        } catch (Exception e) {
//            System.err.println("Error fetching weather data for " + city + ": " + e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error fetching weather data: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Weather>> getAllCitiesWeather() {
//        System.out.println("Fetching weather data for all cities");
//        List<Weather> allWeather = new ArrayList<>();
//        List<String> failedCities = new ArrayList<>();
//
//        try {
//            Map<String, double[]> validatedCities = weatherService.getValidatedCities();
//            System.out.println("Found " + validatedCities.size() + " validated cities");
//
//            for (String city : validatedCities.keySet()) {
//                try {
//                    Weather weather = weatherService.getWeatherData(city);
//                    allWeather.add(weather);
//                    System.out.println("Successfully added weather data for " + city);
//                } catch (Exception e) {
//                    System.err.println("Failed to fetch weather data for " + city + ": " + e.getMessage());
//                    failedCities.add(city);
//                }
//            }
//
//            if (!failedCities.isEmpty()) {
//                System.out.println("Failed to fetch data for " + failedCities.size() + " cities: " + failedCities);
//            }
//
//            System.out.println("Successfully fetched weather data for " +
//                    (validatedCities.size() - failedCities.size()) + "/" + validatedCities.size() + " cities");
//            return ResponseEntity.ok(allWeather);
//
//        } catch (Exception e) {
//            System.err.println("Error fetching all cities weather data: " + e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ArrayList<>());
//        }
//    }
//
//    @GetMapping("/cities")
//    public ResponseEntity<Map<String, double[]>> getAvailableCities() {
//        System.out.println("Fetching available cities list");
//        try {
//            Map<String, double[]> cities = weatherService.getValidatedCities();
//            System.out.println("Successfully retrieved " + cities.size() + " cities");
//            return ResponseEntity.ok(cities);
//        } catch (Exception e) {
//            System.err.println("Error fetching available cities: " + e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null);
//        }
//    }
//}

/**
 * work for all weather related front end request
 */
package starspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starspot.model.Weather;
import starspot.service.weather.WeatherService;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin
public class WeatherController {

    //auto find the weather service
    @Autowired
    private WeatherService weatherService;

    /**
     * get a given city data
     * @param city city name
     * @return return the city weather data or the error message
     */
    @GetMapping("/{city}")
    public ResponseEntity<?> getWeatherForCity(@PathVariable String city) {
        System.out.println("GET DATA OF: " + city);

        try {
            //invoke the weatherService's method
            Weather weather = weatherService.getWeatherData(city);
            //check if the data valid
            if (weather.getCityName() == null || weather.getTemperature() == 0) {
                System.out.println("Cannot find city: " + city);
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Cannot support: " + city);
            }

            System.out.println("Successfully get" + city + "'s weather data': " +
                    weather.getTemperature() + "°C, cloud cover: " +
                    weather.getCloudCover() + "%");
            return ResponseEntity.ok(weather);
        }

        catch (Exception e) {
            System.err.println("Error when getting" + city + "'s weather data: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error when getting data: " + e.getMessage());
        }
    }

    /**
     * return all cities' data
     * @return all cities' data
     */
    @GetMapping("/all")
    public ResponseEntity<List<Weather>> getAllCitiesWeather() {
        System.out.println("Getting all the cities' data");

        try {
            //invoke service and return all the data
            Map<String, Weather> weatherMap = weatherService.getAllWeatherData();
            //change map to list, better the front end use the data
            List<Weather> allWeather = new ArrayList<>(weatherMap.values());

            System.out.println("Successfully get" + allWeather.size() + "number of cities' data");
            return ResponseEntity.ok(allWeather);
        }

        catch (Exception e) {
            System.err.println("Error when getting data of: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ArrayList<>());
        }
    }

    /**
     * get all cities' longitude and latitude
     * @return all cities' and long/la
     */
    @GetMapping("/cities")
    public ResponseEntity<Map<String, double[]>> getAvailableCities() {
        System.out.println("Getting all the cities");
        try {
            Map<String, Weather> weatherMap = weatherService.getAllWeatherData();
            Map<String, double[]> cities = new HashMap<>();

            //store city name and long/la double
            for (Weather weather : weatherMap.values()) {
                cities.put(weather.getCityName(),
                        new double[]{weather.getLatitude(), weather.getLongitude()});
            }

            System.out.println("Successful get" + cities.size() + "cities longitude and latitude");
            return ResponseEntity.ok(cities);
        }

        catch (Exception e) {
            System.err.println("Error when  get cities long/la: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HashMap<>());
        }
    }
}