/**
 * Store data to Firebase
 */
package starspot.service.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import starspot.model.Weather;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

//@Service
public class StoreWeatherData {

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
}