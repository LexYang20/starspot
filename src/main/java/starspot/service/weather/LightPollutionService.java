package starspot.service.weather;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LightPollutionService {

    private final FirebaseDatabase firebaseDatabase;

    // Automatically injects the instance created in Config
    @Autowired
    public LightPollutionService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void addRecord(int num) {
        DatabaseReference ref = firebaseDatabase.getReference("light_pollution");
        // generate ID automatically
        String key = ref.push().getKey();
        ref.child(key).setValueAsync(num);
    }
}
