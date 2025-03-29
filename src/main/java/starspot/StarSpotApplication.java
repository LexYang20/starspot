package starspot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StarSpotApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarSpotApplication.class, args);
    }
}
