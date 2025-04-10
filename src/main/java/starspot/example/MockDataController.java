package starspot.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MockDataController {
    @GetMapping("/api/mock-data")
    public Map<String, String> getMockData() {
        return Map.of("message", "Hello from backend!");
    }

    @GetMapping("/test")
    public String getTestData() {
        return "Hello world!!";
    }
}
