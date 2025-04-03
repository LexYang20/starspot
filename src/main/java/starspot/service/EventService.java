package starspot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starspot.model.Event;
import starspot.model.EventType;
import starspot.model.MeteorShower;
import starspot.repository.EventRepository;
import starspot.repository.MeteorShowerRepository;
import starspot.utils.DateUtil;

import java.time.Instant;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final MeteorShowerRepository meteorShowerRepository;

    @Autowired
    public EventService(EventRepository eventRepository,
                        MeteorShowerRepository meteorShowerRepository) {
        this.eventRepository = eventRepository;
        this.meteorShowerRepository = meteorShowerRepository;
    }

    // 插入事件
    public void insertEvent(Event event) {
        eventRepository.save(event);
    }

    public List<Event> getEvents() {
        return eventRepository.getDataFromFirebase();
    }

    public void insertMeteorShowerToEvents() {
        meteorShowerRepository.getAllMeteorShowers().thenAccept(meteorShowers -> {
            System.out.println("meteor showers size: " + meteorShowers.size());
            for (MeteorShower me : meteorShowers) {
                Event event = new Event(me.getEventId(), EventType.METEOR,
                        DateUtil.getFormattedStartDate(Instant.ofEpochSecond(me.getPeakTime())),
                        DateUtil.getFormattedEventTime(Instant.ofEpochSecond(me.getPeakTime())),
                        me.getDescription());
                eventRepository.save(event);
            }
        });
    }
}

