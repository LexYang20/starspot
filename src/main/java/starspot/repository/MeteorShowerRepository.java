package starspot.repository;

import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import starspot.model.MeteorShower;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class MeteorShowerRepository extends AbstractDataRepository<MeteorShower> {

    @Autowired
    public MeteorShowerRepository(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase, "meteorShowers");
    }

    // Insert data
    public void save(MeteorShower meteorShower) {
        if (meteorShower == null || meteorShower.getEventId() == null || meteorShower.getEventId().isEmpty()) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        databaseReference.child(meteorShower.getEventId()).setValueAsync(meteorShower);
    }

    public CompletableFuture<List<MeteorShower>> getAllMeteorShowers() {
        CompletableFuture<List<MeteorShower>> future = new CompletableFuture<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MeteorShower> meteorShowers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MeteorShower meteorShower = snapshot.getValue(MeteorShower.class);
                    meteorShowers.add(meteorShower);
                }
                future.complete(meteorShowers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException("Read data error：" + databaseError.getMessage()));
            }
        });

        return future;
    }
}
