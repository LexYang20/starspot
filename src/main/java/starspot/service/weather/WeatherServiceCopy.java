package starspot.service.weather;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import starspot.model.Weather;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//@Service
public class WeatherServiceCopy {

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
//        String sql = "SELECT city_ascii, lat, lng FROM canadacities LIMIT 200";
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
//    @Scheduled(fixedRate = 21600000) // 6小时更新一次
//    public void updateWeatherData() {
//        for (String city : validatedCities.keySet()) {
//            try {
//                Thread.sleep(500);  // 避免API限流
//                Weather weather = fetchWeatherFromAPI(city);
//                redisTemplate.opsForValue().set("weatherData::" + city, weather, 6, TimeUnit.HOURS);
//            } catch (Exception e) {
//                System.err.println("Error updating weather for " + city + ": " + e.getMessage());
//            }
//        }
//    }
}