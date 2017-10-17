package ch.epfl.sweng.groupup.lib.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.account.User;
import ch.epfl.sweng.groupup.object.event.Event;

public final class Database {

    // TODO: invite state

    private static final String EMPTY_FIELD = "EMPTY_FIELD";

    private static final String NODE_USERS_LIST = "users";

    private static final String NODE_EVENTS_LIST = "events";
    private static final String NODE_EVENT_MEMBERS_STATUS_PENDING = "pending";
    private static final String NODE_EVENT_MEMBERS_STATUS_ACCEPTED = "accepted";
    private static final String NODE_EVENT_MEMBERS_STATUS_REFUSED = "refused";

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference databaseRef = database.getReference();

    private Database() {
        // Not instantiable.
    }

    public static void setUpDatabase() {
        database.setPersistenceEnabled(true);

        databaseRef.child(NODE_USERS_LIST).addValueEventListener(getUsersListener());
        databaseRef.child(NODE_EVENTS_LIST).addValueEventListener(getEventsListener());
    }

    public static void storeUser(User user) {
        DatabaseUser userToStore = new DatabaseUser(user.getGivenName().getOrElse(EMPTY_FIELD),
                                                    user.getFamilyName().getOrElse(EMPTY_FIELD),
                                                    user.getDisplayName().getOrElse(EMPTY_FIELD),
                                                    user.getEmail().getOrElse(EMPTY_FIELD),
                                                    user.getUUID().getOrElse(EMPTY_FIELD));

        storeUser(userToStore);
    }

    private static void storeUser(DatabaseUser databaseUser) {
        DatabaseReference users = databaseRef.child(NODE_USERS_LIST);
        DatabaseReference currentUser = users.child(databaseUser.uuid);

        currentUser.setValue(databaseUser);
    }

    public static void storeEvent(Event event) {
        List<String> members = new ArrayList<>();

        for (Member member : event.getEventMembers()) {
            members.add(member.getUUID().getOrElse(EMPTY_FIELD));
        }

        DatabaseEvent eventToStore = new DatabaseEvent(event.getEventName(),
                                                       event.getDescription(),
                                                       event.getStartTime().toString(),
                                                       event.getEndTime().toString(),
                                                       event.getUUID(),
                                                       members);

        storeEvent(eventToStore);
    }

    private static void storeEvent(DatabaseEvent databaseEvent) {
        DatabaseReference events = databaseRef.child(NODE_EVENTS_LIST);
        DatabaseReference currentEvent = events.child(databaseEvent.uuid);

        currentEvent.setValue(databaseEvent);
    }

    private static ValueEventListener getUsersListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: Change info in account or member class
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DatabaseUser user = userSnapshot.getValue(DatabaseUser.class);

                    if (user != null) {
                        if (user.uuid.equals(Account.shared.getUUID().getOrElse(EMPTY_FIELD))) {
                            Account.shared
                                    .withGivenName(user.given_name)
                                    .withFamilyName(user.family_name)
                                    .withDisplayName(user.display_name)
                                    .withEmail(user.email)
                                    .withEmail(user.uuid);
                        }

                        // TODO: update the members in the event from the Account.shared
                    }
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
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    DatabaseEvent event = eventSnapshot.getValue(DatabaseEvent.class);

                    if (event != null &&
                        event.members.contains(Account.shared.getUUID().getOrElse(EMPTY_FIELD))) {

                        // TODO: list of members, we only have the UUID of the members
                        Event tempEvent =
                                new Event(event.name,
                                          LocalDateTime.parse(event.datetime_start),
                                          LocalDateTime.parse(event.datetime_end),
                                          event.description, new ArrayList<Member>());

                        // TODO: duplicates ? (remove the old if already contained, do this in
                        // TODO: Account class)
                        Account.shared.addEvent(tempEvent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: uawz
            }
        };
    }
}
