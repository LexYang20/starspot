//package starspot.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import starspot.model.Weather;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class WeatherService {
//
//    @Value("${weather.api.url}")
//    private String apiUrl;
//
//    @Autowired
//    private RedisTemplate<String, Weather> redisTemplate;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    private Map<String, double[]> validatedCities;
//
//    @PostConstruct
//    public void initializeCities() {
//        validatedCities = new HashMap<>();
//        String sql = "SELECT city_ascii, lat, lng FROM canadacities LIMIT 100";
//
//        jdbcTemplate.query(sql, (rs, rowNum) -> {
//            String city = rs.getString("city_ascii");
//            double lat = Double.parseDouble(rs.getString("lat"));
//            double lng = Double.parseDouble(rs.getString("lng"));
//            validatedCities.put(city, new double[]{lat, lng});
//            return null;
//        });
//        System.out.println("Loaded " + validatedCities.size() + " cities from database");
//    }
//
//    public Map<String, double[]> getValidatedCities() {
//        return validatedCities;
//    }
//
//    private Weather fetchWeatherFromAPI(String city) {
//        double[] coordinates = validatedCities.get(city);
//        String url = String.format("%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high",
//                apiUrl, coordinates[0], coordinates[1]);
//
//        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
//        JsonNode current = response.path("current");
//
//        Weather weather = new Weather();
//        weather.setCityName(city);
//        weather.setLatitude(coordinates[0]);
//        weather.setLongitude(coordinates[1]);
//        weather.setTemperature(current.path("temperature_2m").asDouble());
//        weather.setCloudCover(current.path("cloud_cover").asDouble());
//        weather.setCloudCoverLow(current.path("cloud_cover_low").asDouble());
//        weather.setCloudCoverMid(current.path("cloud_cover_mid").asDouble());
//        weather.setCloudCoverHigh(current.path("cloud_cover_high").asDouble());
//        weather.setTimestamp(System.currentTimeMillis());
//
//        return weather;
//    }
//
//    public Weather getWeatherData(String city) {
//        Weather weather = redisTemplate.opsForValue().get("weatherData::" + city);
//        if (weather == null) {
//            weather = fetchWeatherFromAPI(city);
//            redisTemplate.opsForValue().set("weatherData::" + city, weather, 6, TimeUnit.HOURS);
//        }
//        return weather;
//    }
//
//    //@Scheduled(fixedRate = 21600000) // 6小时更新一次
//    public void updateWeatherData() {
//        for (String city : validatedCities.keySet()) {
//            try {
//                //Thread.sleep(500);  // 避免API限流
//                Weather weather = fetchWeatherFromAPI(city);
//                redisTemplate.opsForValue().set("weatherData::" + city, weather);
//            } catch (Exception e) {
//                System.err.println("Error updating weather for " + city + ": " + e.getMessage());
//            }
//        }
//    }
//}


/**
 * Store data to Firebase
 */
//package starspot.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import starspot.model.Weather;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class WeatherService {
//
//    @Value("${weather.api.url}")
//    private String apiUrl;
//
//    @Autowired
//    private FirebaseDatabase firebaseDatabase;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final DatabaseReference cityRef;
//    private Map<String, double[]> validatedCities;
//
//    public WeatherService(FirebaseDatabase firebaseDatabase) {
//        this.cityRef = firebaseDatabase.getReference("cityyyyy");
//    }
//
//    @PostConstruct
//    public void initializeCities() {
//        validatedCities = new HashMap<>();
//        String sql = "SELECT city_ascii, lat, lng FROM canadacities LIMIT 100";
//
//        jdbcTemplate.query(sql, (rs, rowNum) -> {
//            String city = rs.getString("city_ascii");
//            double lat = Double.parseDouble(rs.getString("lat"));
//            double lng = Double.parseDouble(rs.getString("lng"));
//            validatedCities.put(city, new double[]{lat, lng});
//            return null;
//        });
//        System.out.println("Loaded " + validatedCities.size() + " cities from database");
//    }
//
//    public Map<String, double[]> getValidatedCities() {
//        return validatedCities;
//    }
//
//    private Weather fetchWeatherFromAPI(String city) {
//        double[] coordinates = validatedCities.get(city);
//        String url = String.format("%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high",
//                apiUrl, coordinates[0], coordinates[1]);
//
//        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
//        JsonNode current = response.path("current");
//
//        Weather weather = new Weather();
//        weather.setCityName(city);
//        weather.setLatitude(coordinates[0]);
//        weather.setLongitude(coordinates[1]);
//        weather.setTemperature(current.path("temperature_2m").asDouble());
//        weather.setCloudCover(current.path("cloud_cover").asDouble());
//        weather.setCloudCoverLow(current.path("cloud_cover_low").asDouble());
//        weather.setCloudCoverMid(current.path("cloud_cover_mid").asDouble());
//        weather.setCloudCoverHigh(current.path("cloud_cover_high").asDouble());
//        weather.setTimestamp(System.currentTimeMillis());
//
//        // 存储到Firebase
//        cityRef.child(city).setValueAsync(weather);
//
//        return weather;
//    }
//
//    public Weather getWeatherData(String city) {
//        return fetchWeatherFromAPI(city);
//    }
//}

/**
 * read data from firebase for all cities or a given city
 */
package starspot.service.weather;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starspot.model.Weather;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class WeatherService {

    //auto get the instance of firebase
    @Autowired
    private FirebaseDatabase firebaseDatabase;

    //cities reference of firebase
    private final DatabaseReference citiesRef;
    //reference for city name
    private final DatabaseReference cityNameIndexRef;

    /**
     * construct the firebase
     */
    public WeatherService(FirebaseDatabase firebaseDatabase) {
        this.citiesRef = firebaseDatabase.getReference("cities");
        this.cityNameIndexRef = firebaseDatabase.getReference("cityNameIndex");
    }

    /**
     * get weather data of specific city
     * @param city name
     * @return cities' weather data
     */
    public Weather getWeatherData(String city) {
        //completableFuture use for asynchronization store the data
        CompletableFuture<Weather> future = new CompletableFuture<>();
        //normalized the city name
        String cityName = city.toLowerCase().replace(".", "_");

        //get cities information by city name from citiesIndex table
        getCityIdByName(city, cityName, future);

        try {
            //wait for the future to finish
            return future.get();
        }
        //if meet problem when getting data from firebase, print the error
        catch (Exception e) {
            System.err.println("Error getting weather data from Firebase for " + city + ": " + e.getMessage());
            Weather weather = new Weather();
            weather.setCityName(city);
            return weather;
        }
    }

    /**
     * get city id by city name from citiesIndex table
     * @param city original city name
     * @param cityName normalized city name for query
     * @param future CompletableFuture to store the result
     */
    private void getCityIdByName(String city, String cityName, CompletableFuture<Weather> future) {
        cityNameIndexRef.child(cityName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot indexSnapshot) {
                //get id by city name
                String cityId = indexSnapshot.getValue(String.class);

                //if find the id, then get the weather information from cities table
                if (cityId != null) {
                    getWeatherById(cityId, future);
                }
                //if cannot find the city's weather data then just store the city name
                else {
                    Weather weather = new Weather();
                    weather.setCityName(city);
                    future.complete(weather);
                }
            }

            //if meet problem when read the data from firebase
            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
    }

    /**
     * get weather data by city id from cities table
     * @param cityId the id of city
     * @param future CompletableFuture to store the result
     */
    private void getWeatherById(String cityId, CompletableFuture<Weather> future) {
        citiesRef.child(cityId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot citySnapshot) {
                //create weather and stored
                Weather weather = new Weather();
                weather.setCityName(citySnapshot.child("cityName").getValue(String.class));
                weather.setLatitude(citySnapshot.child("latitude").getValue(Double.class));
                weather.setLongitude(citySnapshot.child("longitude").getValue(Double.class));
                weather.setTemperature(citySnapshot.child("temperature").getValue(Double.class));
                weather.setCloudCover(citySnapshot.child("cloudCover").getValue(Double.class));
                weather.setCloudCoverLow(citySnapshot.child("cloudCoverLow").getValue(Double.class));
                weather.setCloudCoverMid(citySnapshot.child("cloudCoverMid").getValue(Double.class));
                weather.setCloudCoverHigh(citySnapshot.child("cloudCoverHigh").getValue(Double.class));
                weather.setTimestamp(citySnapshot.child("timestamp").getValue(Long.class));
                weather.setLightPollution(citySnapshot.child("lightPollution").getValue(Integer.class));
                //finished get data
                future.complete(weather);
            }

            //if meet problem when read the data from firebase
            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
    }

    /**
     * get all the cities' weather data
     * @return all cities' data
     */
    public Map<String, Weather> getAllWeatherData() {
        CompletableFuture<Map<String, Weather>> future = new CompletableFuture<>();
        //create map for city name and it's weather data
        Map<String, Weather> weatherMap = new HashMap<>();

        citiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //loop find all the datas
                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
                    Weather weather = new Weather();
                    weather.setCityName(citySnapshot.child("cityName").getValue(String.class));
                    weather.setLatitude(citySnapshot.child("latitude").getValue(Double.class));
                    weather.setLongitude(citySnapshot.child("longitude").getValue(Double.class));
                    weather.setTemperature(citySnapshot.child("temperature").getValue(Double.class));
                    weather.setCloudCover(citySnapshot.child("cloudCover").getValue(Double.class));
                    weather.setCloudCoverLow(citySnapshot.child("cloudCoverLow").getValue(Double.class));
                    weather.setCloudCoverMid(citySnapshot.child("cloudCoverMid").getValue(Double.class));
                    weather.setCloudCoverHigh(citySnapshot.child("cloudCoverHigh").getValue(Double.class));
                    weather.setTimestamp(citySnapshot.child("timestamp").getValue(Long.class));
                    weather.setLightPollution(citySnapshot.child("lightPollution").getValue(Integer.class));
                    weatherMap.put(weather.getCityName(), weather);
                }
                future.complete(weatherMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        try {
            return future.get();
        }
        catch (Exception e) {
            System.err.println("Error getting all weather data from Firebase: " + e.getMessage());
            return new HashMap<>();
        }
    }
}