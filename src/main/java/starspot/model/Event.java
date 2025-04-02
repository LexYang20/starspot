package starspot.model;

import lombok.Data;

@Data
public class Event {
    private String eventId;
    private EventType type;
    private String startDate;
    private String eventTime;
    private String description;

    public Event() {}

    public Event(String eventId, EventType type, String startDate, String eventTime, String description) {
        this.eventId = eventId;
        this.type = type;
        this.eventTime = eventTime;
        this.startDate = startDate;
        this.description = description;
    }
}
