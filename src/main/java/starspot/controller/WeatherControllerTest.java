/**
 * For City Test
 */
package starspot.controller;

//@RestController
//@RequestMapping("/api/weather")
//@CrossOrigin
//@Slf4j
public class WeatherControllerTest {

//    @Autowired
//    private WeatherService weatherService;
//
//    @GetMapping("/{city}")
//    public ResponseEntity<?> getWeatherForCity(@PathVariable String city) {
//        try {
//            Weather weather = weatherService.getWeatherData(city);
//            return ResponseEntity.ok(weather);
//        } catch (IllegalArgumentException e) {
//            //log.warn("请求了不支持的城市: {}", city);
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body("City not supported or API not available: " + city);
//        } catch (Exception e) {
//            //log.error("获取城市 {} 天气数据时发生错误: {}", city, e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error fetching weather data: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Weather>> getAllCitiesWeather() {
//        List<Weather> allWeather = new ArrayList<>();
//        List<String> failedCities = new ArrayList<>();
//
//        try {
//            // 使用验证过的城市列表
//            Map<String, double[]> validatedCities = weatherService.getValidatedCities();
//
//            for (String city : validatedCities.keySet()) {
//                try {
//                    Weather weather = weatherService.getWeatherData(city);
//                    allWeather.add(weather);
//                } catch (Exception e) {
//                    //log.error("获取城市 {} 天气数据时发生错误: {}", city, e.getMessage());
//                    failedCities.add(city);
//                }
//            }
//
//            if (!failedCities.isEmpty()) {
//                //log.warn("部分城市数据获取失败: {}", failedCities);
//            }
//
//            return ResponseEntity.ok(allWeather);
//
//        } catch (Exception e) {
//            //log.error("获取所有城市天气数据时发生错误: {}", e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ArrayList<>());
//        }
//    }
//
//    @GetMapping("/cities")
//    public ResponseEntity<Map<String, double[]>> getAvailableCities() {
//        try {
//            return ResponseEntity.ok(weatherService.getValidatedCities());
//        } catch (Exception e) {
//            //log.error("获取可用城市列表时发生错误: {}", e.getMessage());
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null);
//        }
//    }
}