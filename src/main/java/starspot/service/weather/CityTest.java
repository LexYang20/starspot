//package starspot.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import starspot.model.Weather;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Service
//@Slf4j
//public class CityTest {
//
//    @Value("${weather.api.url:https://api.open-meteo.com/v1/forecast}")
//    private String apiUrl;
//
//    @Autowired
//    private RedisTemplate<String, Weather> redisTemplate;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    private Map<String, double[]> validatedCities;
//
//    private static final String INVALID_CITIES_FILE = "invalid_cities.txt";
//
//    @PostConstruct
//    public void init() {
//        //log.info("开始验证城市API可访问性...");
//        validateCitiesFromSQL();
//    }
//    public Map<String, double[]> getValidatedCities() {
//        return Collections.unmodifiableMap(validatedCities);
//    }
//    private void validateCitiesFromSQL() {
//        try {
//            System.out.println("开始执行 validateCitiesFromSQL 方法");
//
//            // 打印当前工作目录
//            System.out.println("当前工作目录: " + System.getProperty("user.dir"));
//
//            // 检查 SQL 文件是否存在
//            Path sqlPath = Paths.get("D:\\Backup\\Downloads\\ece651Project\\starSpot\\src\\main\\sql\\canadacities.sql");
//            System.out.println("SQL 文件是否存在: " + Files.exists(sqlPath));
//
//            // 处理invalid_cities.txt文件
//            File invalidCitiesFile = new File(INVALID_CITIES_FILE);
//            System.out.println("Invalid Cities 文件的绝对路径: " + invalidCitiesFile.getAbsolutePath());
//
//            if (invalidCitiesFile.exists()) {
//                System.out.println("已存在旧的 " + INVALID_CITIES_FILE + "，准备删除");
//                Files.delete(invalidCitiesFile.toPath());
//            }
//
//            boolean created = invalidCitiesFile.createNewFile();
//            System.out.println("新文件创建结果: " + created);
//
//            // 读取SQL文件
//            String sqlContent = new String(Files.readAllBytes(sqlPath));
//            System.out.println("SQL文件读取成功，内容长度: " + sqlContent.length());
//
//            List<String> invalidCities = new ArrayList<>();
//            Map<String, double[]> tempCities = new HashMap<>();
//            AtomicInteger successCount = new AtomicInteger(0);
//            AtomicInteger failCount = new AtomicInteger(0);
//
//            // 解析SQL中的INSERT语句
//            Pattern pattern = Pattern.compile("INSERT INTO `canadacities` VALUES \\((.+?)\\);");
//            Matcher matcher = pattern.matcher(sqlContent);
//
//            System.out.println("开始解析城市数据...");
//
//            while (matcher.find()) {
//                String[] values = matcher.group(1).split(",");
//                String cityName = values[0].replaceAll("'", "").trim();
//                String lat = values[4].replaceAll("'", "").trim();
//                String lng = values[5].replaceAll("'", "").trim();
//
//                System.out.println("正在测试城市: " + cityName + " (坐标: " + lat + ", " + lng + ")");
//
//                try {
//                    String url = String.format("%s?latitude=%s&longitude=%s&current=temperature_2m",
//                            apiUrl, lat, lng);
//
//                    restTemplate.getForObject(url, JsonNode.class);
//                    tempCities.put(cityName, new double[]{Double.parseDouble(lat), Double.parseDouble(lng)});
//                    successCount.incrementAndGet();
//                    System.out.println("城市API测试成功: " + cityName);
//
//                } catch (Exception e) {
//                    invalidCities.add(cityName);
//                    failCount.incrementAndGet();
//                    System.err.println("城市API测试失败: " + cityName + " - 错误: " + e.getMessage());
//                }
//
//                // 添加延迟避免请求过快
//                Thread.sleep(500);
//            }
//
//            // 保存测试结果
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(invalidCitiesFile))) {
//                for (String city : invalidCities) {
//                    writer.write(city);
//                    writer.newLine();
//                }
//            }
//
//            // 更新有效城市列表
//            this.validatedCities = tempCities;
//
//            System.out.println("\n城市API测试完成!");
//            System.out.println("测试结果统计:");
//            System.out.println("总城市数: " + (successCount.get() + failCount.get()));
//            System.out.println("成功数: " + successCount.get());
//            System.out.println("失败数: " + failCount.get());
//            System.out.println("无效的城市已保存到 " + INVALID_CITIES_FILE);
//
//        } catch (Exception e) {
//            System.err.println("验证过程发生错误: ");
//            e.printStackTrace();
//        }
//    }
//
//    // 使用验证过的城市列表
//    public Weather getWeatherData(String city) {
//        if (!validatedCities.containsKey(city)) {
//            throw new IllegalArgumentException("City not supported or API not available");
//        }
//
//        Weather weather = redisTemplate.opsForValue().get("weatherData::" + city);
//        if (weather == null) {
//           // log.info("Cache miss for city: {}", city);
//            weather = fetchWeatherFromAPI(city);
//            redisTemplate.opsForValue().set("weatherData::" + city, weather, 30, TimeUnit.MINUTES);
//        } else {
//           // log.info("Cache hit for city: {}", city);
//        }
//        return weather;
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
//}
package starspot.service.weather;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class CityTest {
        //implements CommandLineRunner {
//    private static final String API_URL = "https://api.open-meteo.com/v1/forecast";
//    private static final String CSV_FILE_PATH = "D:\\Backup\\Downloads\\ece651Project\\starSpot\\Data\\canadian_cities_coordinates.csv";
//    private static final String INVALID_CITIES_FILE = "invalid_cities.txt";
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @Autowired
//    private FirebaseDatabase firebaseDatabase;
//
//    @Override
//    public void run(String... args) throws Exception {
//        try {
//            System.out.println("开始执行城市数据处理");
//
//            // 处理 invalid_cities.txt 文件
//            File invalidCitiesFile = new File(INVALID_CITIES_FILE);
//            System.out.println("Invalid Cities 文件的绝对路径: " + invalidCitiesFile.getAbsolutePath());
//
//            if (invalidCitiesFile.exists()) {
//                System.out.println("已存在旧的 " + INVALID_CITIES_FILE + "，准备删除");
//                Files.delete(invalidCitiesFile.toPath());
//            }
//
//            boolean created = invalidCitiesFile.createNewFile();
//            System.out.println("新文件创建结果: " + created);
//
//            // 读取CSV文件
//            List<CityData> cities = readCitiesFromCSV();
//            List<String> invalidCities = new ArrayList<>();
//            AtomicInteger successCount = new AtomicInteger(0);
//            AtomicInteger failCount = new AtomicInteger(0);
//
//            // 获取天气数据并存储到Firebase
//            DatabaseReference citiesRef = firebaseDatabase.getReference("cities");
//            DatabaseReference cityNameIndexRef = firebaseDatabase.getReference("cityNameIndex");
//
//            for (CityData city : cities) {
//                try {
//                    // 调用天气API
//                    String url = String.format("%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high",
//                            API_URL, city.latitude, city.longitude);
//
//                    System.out.println("正在处理城市: " + city.cityName +
//                            " (坐标: " + city.latitude + ", " + city.longitude + ")");
//
//                    JsonNode response = restTemplate.getForObject(url, JsonNode.class);
//                    JsonNode current = response.path("current");
//
//                    // 更新城市数据
//                    city.temperature = current.path("temperature_2m").asDouble();
//                    city.cloudCover = current.path("cloud_cover").asDouble();
//                    city.cloudCoverLow = current.path("cloud_cover_low").asDouble();
//                    city.cloudCoverMid = current.path("cloud_cover_mid").asDouble();
//                    city.cloudCoverHigh = current.path("cloud_cover_high").asDouble();
//                    city.timestamp = System.currentTimeMillis();
//
//                    // 生成唯一ID并存储到Firebase
//                    String uniqueId = citiesRef.push().getKey();
//                    citiesRef.child(uniqueId).setValueAsync(city.toMap());
//
//                    // 创建城市名到ID的索引
//                    String normalizedCityName = city.cityName.toLowerCase().replace(".", "_");
//                    cityNameIndexRef.child(normalizedCityName).setValueAsync(uniqueId);
//
//                    successCount.incrementAndGet();
//                    System.out.println("城市处理成功: " + city.cityName + " (ID: " + uniqueId + ")");
//
//                } catch (Exception e) {
//                    invalidCities.add(city.cityName);
//                    failCount.incrementAndGet();
//                    System.err.println("城市处理失败: " + city.cityName + " - 错误: " + e.getMessage());
//                }
//
//                // 添加延迟避免请求过快
//                Thread.sleep(500);
//            }
//
//            // 保存无效城市列表
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(invalidCitiesFile))) {
//                for (String city : invalidCities) {
//                    writer.write(city);
//                    writer.newLine();
//                }
//            }
//
//            // 输出统计信息
//            System.out.println("\n城市处理完成!");
//            System.out.println("处理结果统计:");
//            System.out.println("总城市数: " + (successCount.get() + failCount.get()));
//            System.out.println("成功数: " + successCount.get());
//            System.out.println("失败数: " + failCount.get());
//            System.out.println("无效的城市已保存到 " + INVALID_CITIES_FILE);
//
//        } catch (Exception e) {
//            System.err.println("数据处理过程发生错误: ");
//            e.printStackTrace();
//        }
//    }
//
//    private List<CityData> readCitiesFromCSV() throws IOException, CsvValidationException {
//        List<CityData> cities = new ArrayList<>();
//
//        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
//            // 跳过表头
//            reader.skip(1);
//
//            // 读取所有行
//            String[] line;
//            while ((line = reader.readNext()) != null) {
//                try {
//                    CityData city = new CityData(
//                            line[0], // cityName
//                            Integer.parseInt(line[1]), // population
//                            Double.parseDouble(line[2]), // latitude
//                            Double.parseDouble(line[3])  // longitude
//                    );
//                    cities.add(city);
//                } catch (NumberFormatException e) {
//                    System.err.println("跳过无效数据行: " + String.join(", ", line));
//                }
//            }
//        }
//
//        return cities;
//    }
//
//    static class CityData {
//        String cityName;
//        int population;
//        double latitude;
//        double longitude;
//        double temperature;
//        double cloudCover;
//        double cloudCoverLow;
//        double cloudCoverMid;
//        double cloudCoverHigh;
//        long timestamp;
//
//        public CityData(String cityName, int population, double latitude, double longitude) {
//            this.cityName = cityName;
//            this.population = population;
//            this.latitude = latitude;
//            this.longitude = longitude;
//        }
//
//        public Map<String, Object> toMap() {
//            Map<String, Object> map = new HashMap<>();
//            map.put("cityName", cityName);
//            map.put("population", population);
//            map.put("latitude", latitude);
//            map.put("longitude", longitude);
//            map.put("temperature", temperature);
//            map.put("cloudCover", cloudCover);
//            map.put("cloudCoverLow", cloudCoverLow);
//            map.put("cloudCoverMid", cloudCoverMid);
//            map.put("cloudCoverHigh", cloudCoverHigh);
//            map.put("timestamp", timestamp);
//            return map;
//        }
//    }
}