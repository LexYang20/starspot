package starspot.repository;

import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import starspot.model.Event;

import java.util.List;

@Repository
public class EventRepository extends AbstractDataRepository<Event> {

    @Autowired
    public EventRepository(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase, "events");
    }

    @Override
    public void save(Event event) {
        if (event == null || event.getEventId() == null || event.getEventId().isEmpty()) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        databaseReference.child(event.getEventId()).setValueAsync(event);
        System.out.println("Event saved: " + event.getEventId());
    }

    public List<Event> getDataFromFirebase() {
        return super.getDataFromFirebase(Event.class)
                .thenApply(eventList -> {
                    return eventList;
                }).join();
    }
}
