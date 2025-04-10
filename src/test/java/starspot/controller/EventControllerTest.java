package starspot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import starspot.model.Event;
import starspot.model.EventType;
import starspot.model.MeteorShower;
import starspot.service.EventService;
import starspot.service.MeteorShowerService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(EventController.class)  //  Use WebMvcTest to correctly mock Spring context
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc correctly initialized

    @MockBean  // Mocking MeteorShowerService so Spring doesn’t try to inject a real one
    private MeteorShowerService meteorShowerService;

    @MockBean  // Mock EventService as well
    private EventService eventService;

    @Test
    public void getEvents_ShouldReturnEventsList() throws Exception {
        // Arrange (Mock Data)
        List<Event> events = Arrays.asList(
                new Event("1", EventType.METEOR, "2023-01-01", "00:00:00", "Event 1"),
                new Event("2", EventType.LUNAR, "2023-02-01", "07:00:00", "Event 2")
        );
        when(eventService.getEvents()).thenReturn(events);  // Mocked service call

        // Act & Assert
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Event 1"))
                .andExpect(jsonPath("$[1].description").value("Event 2"));
    }

    @Test
    public void getMeteorShowers_ShouldReturnMeteorShowersList() throws Exception {
        // Arrange (Mock Data)
        List<MeteorShower> meteorShowers = Arrays.asList(
                new MeteorShower("Heading1", 1672531200L),
                new MeteorShower("Heading2", 1675209600L)
        );
        when(meteorShowerService.getMeteorShowers()).thenReturn(meteorShowers);

        // Act & Assert
        mockMvc.perform(get("/api/meteorshowers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].meteorHeading").value("Heading1"))
                .andExpect(jsonPath("$[1].meteorHeading").value("Heading2"));
    }
}
