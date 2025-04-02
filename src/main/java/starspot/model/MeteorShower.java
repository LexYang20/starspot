package starspot.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Data;

@Data
public class MeteorShower {
    private String eventId;
    private long peakTime; //suggest to view the meteor
    private String meteorHeading;
    private String activePeriod;
    private String peakPeriod;
    private String radiant;
    private String zhr; //max number of meteors per hour in the shower
    private String velocity;
    private String parentObject;
    private String moonFull;
    private String description;

    public MeteorShower() {}
    // 有参构造函数
    public MeteorShower(String meteorHeading, Long peakTime) {
        this.eventId =  generateEventId(EventType.METEOR.name(), String.valueOf(peakTime), meteorHeading);
        this.meteorHeading = meteorHeading;
        this.peakTime = peakTime;
    }

    public String generateEventId(String eventType, String eventTime, String eventName) {
        try {
            // handle when eventName is null
            String uniqueString = eventTime + "_" + (eventName.isEmpty() ? "unknown" : eventName);

            // cal hash
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(uniqueString.getBytes());

            //turn to hexadecimal, pick first 8 digits
            StringBuilder hashString = new StringBuilder();
            for (int i = 0; i < 4; i++) {  //
                hashString.append(String.format("%02x", hashBytes[i]));
            }

            return eventType + "_" + hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 Algorithm not found", e);
        }
    }
}
