package ch.epfl.sweng.groupup.lib;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

public final class Database {

    private static final String EMPTY_FIELD = "EMPTY_FIELD";

    private static final String NODE_USERS_LIST = "users";
    private static final String NODE_USER_GIVEN_NAME = "GivenName";
    private static final String NODE_USER_FAMILY_NAME = "FamilyName";
    private static final String NODE_USER_DISPLAY_NAME = "DisplayName";
    private static final String NODE_USER_EMAIL = "email";

    private static final String NODE_EVENTS_LIST = "events";
    private static final String NODE_EVENT_NAME = "Name";
    private static final String NODE_EVENT_DESCRIPTION = "Description";
    private static final String NODE_EVENT_DATETIME_START = "DatetimeStart";
    private static final String NODE_EVENT_DATETIME_END = "DatetimeEnd";
    private static final String NODE_EVENT_MEMBERS = "Members";

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();

    private Database() {
        // Not instantiable.
    }

    public static void setUpDatabase() {
        // TODO: call
        database.setPersistenceEnabled(true);

        databaseRef.child(NODE_USERS_LIST).addValueEventListener(getUsersListener());
        databaseRef.child(NODE_EVENTS_LIST).addValueEventListener(getEventsListener());
    }

    public static void storeUser(Account account) {
        // TODO: id
        storeUser("USER_ID", account.getGivenName().getOrElse(EMPTY_FIELD),
                  account.getFamilyName().getOrElse(EMPTY_FIELD),
                  account.getDisplayName().getOrElse(EMPTY_FIELD),
                  account.getEmail().getOrElse(EMPTY_FIELD));
    }

    public static void storeUser(Member member) {
        // TODO: id
        storeUser("USER_ID", member.getGivenName().getOrElse(EMPTY_FIELD),
                  member.getFamilyName().getOrElse(EMPTY_FIELD),
                  member.getDisplayName().getOrElse(EMPTY_FIELD),
                  member.getEmail().getOrElse(EMPTY_FIELD));
    }

    private static void storeUser(String uid, String givenName, String familyName, String
            displayName, String email) {
        DatabaseReference users = databaseRef.child(NODE_USERS_LIST);
        DatabaseReference currentUser = users.child(uid);

        currentUser.child(NODE_USER_GIVEN_NAME).setValue(givenName);
        currentUser.child(NODE_USER_FAMILY_NAME).setValue(familyName);
        currentUser.child(NODE_USER_DISPLAY_NAME).setValue(displayName);
        currentUser.child(NODE_USER_EMAIL).setValue(email);
    }

    public static void storeEvent(Event event) {
        // TODO: id and desc
        storeEvent("EVENT_ID", event.getEventName(),
                   "DESCRIPTION",
                   event.getStartTime().toString(),
                   event.getEndTime().toString(),
                   new ArrayList<String>());
    }

    private static void storeEvent(String uid, String name, String description, String
            dateTimeStart, String dateTimeEnd, List<String> members) {
        DatabaseReference events = databaseRef.child(NODE_EVENTS_LIST);
        DatabaseReference currentEvent = events.child(uid);

        currentEvent.child(NODE_EVENT_NAME).setValue(name);
        currentEvent.child(NODE_EVENT_DESCRIPTION).setValue(description);
        currentEvent.child(NODE_EVENT_DATETIME_START).setValue(dateTimeStart);
        currentEvent.child(NODE_EVENT_DATETIME_END).setValue(dateTimeEnd);

        // TODO: update list of members
    }

    private static ValueEventListener getUsersListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: Change info in account or member class
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: eses
            }
        };
    }

    private static ValueEventListener getEventsListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: Change info in the event list of account or add new event
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: uawz
            }
        };
    }
}
