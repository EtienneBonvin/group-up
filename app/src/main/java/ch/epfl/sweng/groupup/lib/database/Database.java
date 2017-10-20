package ch.epfl.sweng.groupup.lib.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

public final class Database {

    /**
     * Static fields to help using the database.
     */
    static final String EMPTY_FIELD = "EMPTY_FIELD";

    private static final String NODE_EVENTS_LIST = "events";

    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;

    /**
     * Private constructor, we don't want to instantiate this class.
     */
    private Database() {
        // Not instantiable.
    }

    /**
     * Function to set up the database. Has to be called at the start of the app.
     */
    public static void setUpDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }

        databaseRef = database.getReference();
    }

    /**
     * Function to set up the listener for the "events" node in the database. Has to be set up
     * once when we are ready to receive updates from the database.
     */
    public static void setUpEventListener() {
        databaseRef.child(NODE_EVENTS_LIST).addValueEventListener(getEventsListener());
    }

    /**
     * Function called every time we want to update the information stored in the database.
     */
    public static void update() {
        for (Event event : Account.shared.getFutureEvents()) {
            storeEvent(event);
        }
        for (Event event : Account.shared.getPastEvents()) {
            storeEvent(event);
        }
        if (!Account.shared.getCurrentEvent().isEmpty()) {
            storeEvent(Account.shared.getCurrentEvent().get());
        }
    }

    /**
     * Helper function to store the events.
     *
     * @param event - the real "Event" object
     */
    private static void storeEvent(Event event) {
        if (event == null) {
            throw new NullPointerException("event cannot be null");
        }

        HashMap<String, DatabaseUser> uuidToUserMap = new HashMap<>();
        for (Member memberToStore : event.getEventMembers()) {
            DatabaseUser databaseUser =
                    new DatabaseUser(memberToStore.getGivenName().getOrElse(EMPTY_FIELD),
                                     memberToStore.getFamilyName().getOrElse(EMPTY_FIELD),
                                     memberToStore.getDisplayName().getOrElse(EMPTY_FIELD),
                                     memberToStore.getEmail().getOrElse(EMPTY_FIELD),
                                     memberToStore.getUUID().getOrElse(EMPTY_FIELD));

            uuidToUserMap.put(databaseUser.uuid, databaseUser);
        }

        DatabaseEvent eventToStore = new DatabaseEvent(event.getEventName(),
                                                       event.getDescription(),
                                                       event.getStartTime().toString(),
                                                       event.getEndTime().toString(),
                                                       event.getUUID(),
                                                       uuidToUserMap);
        storeEvent(eventToStore);
    }

    /**
     * Helper function to store the events.
     *
     * @param databaseEvent - the "DatabaseEvent" object
     */
    private static void storeEvent(DatabaseEvent databaseEvent) {
        if (databaseEvent == null) {
            throw new NullPointerException("databaseEvent cannot be null");
        }

        DatabaseReference events = databaseRef.child(NODE_EVENTS_LIST);
        DatabaseReference currentEvent = events.child(databaseEvent.uuid);

        currentEvent.setValue(databaseEvent);
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                onDataChangeCallback(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: onCancelled
            }
        };
    }

    /**
     * Callback function for the onDataChange event for the "events" node.
     *
     * @param dataSnapshot - the data snapshot received
     */
    private static void onDataChangeCallback(DataSnapshot dataSnapshot) {
        /*
        This variable defines if we need to update ourselves in the database and fill in
        our information.
         */
        boolean needToUpdateMyself = false;

        // We for over all the events received.
        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
            // Parsing of the database data into the object class.
            DatabaseEvent event = eventSnapshot.getValue(DatabaseEvent.class);

            if (event != null && !event.uuid.equals(Database.EMPTY_FIELD)) {

                Set<String> uuidsOfMembers = event.members.keySet();
                if (uuidsOfMembers.contains(
                        Account.shared.getUUID().getOrElse(EMPTY_FIELD))) {

                    // We transform every DatabaseUser to a Member.
                    List<Member> members = new ArrayList<>();
                    for (DatabaseUser user : event.members.values()) {
                        Member memberToAdd = new Member(user.uuid,
                                                        user.display_name,
                                                        user.given_name,
                                                        user.family_name,
                                                        user.email);

                        // We check if the member we are about to add is Account.shared.
                        if (user.uuid.equals(
                                Account.shared.getUUID().getOrElse(EMPTY_FIELD))) {

                            Member mySelfAsMember = Account.shared.toMember();

                            // If we have to update our selves we update the member.
                            if (!memberToAdd.equals(mySelfAsMember)) {
                                memberToAdd = mySelfAsMember;
                                needToUpdateMyself = true;
                            }
                        }

                        // We add all the members.
                        members.add(memberToAdd);
                    }

                    // We create the event that we want to store in the account.
                    Event tempEvent = new Event(
                            event.uuid,
                            event.name,
                            LocalDateTime.parse(event.datetime_start),
                            LocalDateTime.parse(event.datetime_end),
                            event.description, members);

                    // We add or update the event.
                    Account.shared.addOrUpdateEvent(tempEvent);
                }
            }
        }

        /*
        If we updated our information in one of the events we have to update it in the
        database as well.
         */
        if (needToUpdateMyself) {
            Database.update();
        }
    }
}