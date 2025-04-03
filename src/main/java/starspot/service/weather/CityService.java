package starspot.service.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

//@Service
public class CityService {

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    private Map<String, double[]> cities;
//
//    @PostConstruct
//    public void initializeCities() {
//        cities = new HashMap<>();
//        String sql = "SELECT city_ascii, lat, lng FROM canadacities";
//
//        jdbcTemplate.query(sql, (rs, rowNum) -> {
//            String city = rs.getString("city_ascii");
//            double lat = Double.parseDouble(rs.getString("lat"));
//            double lng = Double.parseDouble(rs.getString("lng"));
//            cities.put(city, new double[]{lat, lng});
//            return null;
//        });
//        System.out.println("Loaded " + cities.size() + " cities from database");
//    }
//
//    public Map<String, double[]> getAllCities() {
//        return cities;
//    }
}
