package utilities;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Utility class for interacting with the Firebase Database.
 */
public class Database {

    /**
     * Interface for handling database read events.
     */
    public interface OnGetDataListener {
        void onStart();
        void onSuccess(DataSnapshot data);
        void onFailed(DatabaseError databaseError);
    }

    /**
     * Reads data from the specified database path.
     *
     * @param child The database path to read from.
     * @param listener The {@link OnGetDataListener} for handling different states in
     *                the database call
     */
    public void mReadDataOnce(String child, final OnGetDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference().child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onSuccess(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed(error);
            }
        });
    }
}
