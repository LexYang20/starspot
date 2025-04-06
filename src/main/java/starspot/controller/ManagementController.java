package starspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import starspot.service.EventService;
import starspot.service.MeteorShowerService;

@RestController
@RequestMapping("/api/management")
public class ManagementController {
    private final EventService eventService;
    private final MeteorShowerService meteorShowerService;

    @Autowired
    public ManagementController(EventService eventService,
                                MeteorShowerService meteorShowerService) {
        this.eventService = eventService;
        this.meteorShowerService = meteorShowerService;
    }

    @GetMapping("/add-event")
    public String addEvent() {
        eventService.insertMeteorShowerToEvents();
        return "success";
    }

    @GetMapping("/add-meteorshowers")
    public String addMeteorShowers() {
        meteorShowerService.initMeteorShowerDataFromCSV();
        return "success";
    }

    @GetMapping("/add-cities")
    public String addCities() {
//        meteorShowerService.initMeteorShowerDataFromCSV();
        return "success";
    }
}
