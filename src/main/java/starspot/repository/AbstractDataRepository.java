package starspot.repository;

import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public abstract class AbstractDataRepository<T> implements DataRepositoryInterface<T> {

    protected final DatabaseReference databaseReference;

    @Autowired
    public AbstractDataRepository(FirebaseDatabase firebaseDatabase, @Value("${firebase.path}") String path) {
        this.databaseReference = firebaseDatabase.getReference(path);
    }

    public <T> CompletableFuture<List<T>> getDataFromFirebase(Class<T> clazz) {
        CompletableFuture<List<T>> future = new CompletableFuture<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<T> resultList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    T object = snapshot.getValue(clazz);
                    if (object != null) {
                        resultList.add(object);
                    }
                }
                future.complete(resultList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

}
