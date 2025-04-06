package starspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import starspot.model.Event;
import starspot.model.MeteorShower;
import starspot.service.EventService;
import starspot.service.MeteorShowerService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {
    private final EventService eventService;
    private final MeteorShowerService meteorShowerService;

    @Autowired
    public EventController(EventService eventService, MeteorShowerService meteorShowerService) {
        this.eventService = eventService;
        this.meteorShowerService = meteorShowerService;
    }
    @GetMapping("/events")
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("meteorshowers")
    public List<MeteorShower> getMeteorShowers() {
        return meteorShowerService.getMeteorShowers();
    }

}
