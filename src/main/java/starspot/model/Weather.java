package starspot.model;


import lombok.*;

import java.io.Serializable;

@Data
public class Weather implements Serializable {
    private String cityName;
    private double temperature;
    private double cloudCover;
    private double cloudCoverLow;
    private double cloudCoverMid;
    private double cloudCoverHigh;
    private double latitude;
    private double longitude;
    private long timestamp;
    private int lightPollution;

}