package ch.epfl.sweng.groupup.lib.database;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public final class Database {

    /**
     * Static fields to help using the database.
     */
    public static final String EMPTY_FIELD = "EMPTY_FIELD";
    private static final String NODE_EVENTS_LIST = "events";
    private static FirebaseDatabase database;
    private static DatabaseReference eventsChild;


    /**
     * Function to set up the database. Has to be called at the start of the app.
     */
    public static void setUp() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }

        eventsChild = database.getReference()
                              .child(NODE_EVENTS_LIST);
    }


    /**
     * Function to set up the listener for the "events" node in the database. Has to be set up
     * once when we are ready to receive updates from the database.
     *
     * @param listener - the wanted event lister, pass in null to use default one
     */
    public static void setUpEventListener(ValueEventListener listener) {
        if (listener == null) {
            eventsChild.addValueEventListener(getEventsListener());
        } else {
            eventsChild.addValueEventListener(listener);
        }
    }


    /**
     * Function to get the EventsListener that gets called called every time we receive an update
     * from the database.
     *
     * @return the ValueEventListener to listen on database changes
     */
    private static ValueEventListener getEventsListener() {
        return new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // UNUSED
            }


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!Account.shared.getUUID()
                                   .isEmpty() && !Account.shared.getEmail()
                                                                .isEmpty()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        DatabaseEvent databaseEvent = eventSnapshot.getValue(DatabaseEvent.class);

                        if (databaseEvent != null
                            && !databaseEvent.getUuid()
                                             .equals(Database.EMPTY_FIELD)
                            && databaseEvent.containedAsMember()) {
                            Account.shared.addOrUpdateEvent(databaseEvent.toEvent());
                        }
                    }
                    Account.shared.notifyAllWatchers();
                }
            }
        };
    }


    /**
     * Function called every time we want to update the information stored in the database.
     */
    public static void update() {
        for (Event event : Account.shared.getEvents()) {
            storeEvent(event.toDatabaseEvent());
        }
    }


    /**
     * Helper function to store the events.
     *
     * @param databaseEvent - the "DatabaseEvent" object
     */
    private static void storeEvent(DatabaseEvent databaseEvent) {
        DatabaseReference currentEvent = eventsChild.child(databaseEvent.getUuid());

        if (databaseEvent.getMembers()
                         .size() > 0) {
            // We update the event.
            currentEvent.setValue(databaseEvent);
        } else {
            // We delete the event from the database if the last member left.
            currentEvent.removeValue();
        }
    }
}
