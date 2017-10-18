package ch.epfl.sweng.groupup.lib.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

public final class Database {

    // TODO: invite state, event ID renew each time we add the event.

    private static final String EMPTY_FIELD = "EMPTY_FIELD";

    private static final String NODE_USERS_LIST = "users";

    private static final String NODE_EVENTS_LIST = "events";
    private static final String NODE_EVENT_MEMBERS = "members";
    private static final String NODE_EVENT_MEMBERS_STATUS_PENDING = "pending";
    private static final String NODE_EVENT_MEMBERS_STATUS_ACCEPTED = "accepted";
    private static final String NODE_EVENT_MEMBERS_STATUS_REFUSED = "refused";

    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;

    private Database() {
        // Not instantiable.
    }

    public static void setUpDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }

        databaseRef = database.getReference();
    }

    public static void setUpEventListener() {
        //databaseRef.child(NODE_USERS_LIST).addValueEventListener(getUsersListener());
        databaseRef.child(NODE_EVENTS_LIST).addValueEventListener(getEventsListener());
    }

    public static void storeAccount(Account account) {
        // Storing the Account as a user.
        /*
        DatabaseUser userToStore = new DatabaseUser(account.getGivenName().getOrElse(EMPTY_FIELD),
                                                    account.getFamilyName().getOrElse(EMPTY_FIELD),
                                                    account.getDisplayName().getOrElse(EMPTY_FIELD),
                                                    account.getEmail().getOrElse(EMPTY_FIELD),
                                                    account.getUUID().getOrElse(EMPTY_FIELD));
                                                    */
        //storeAccount(userToStore);

        // Storing his events in order to update himself as a member.
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

    private static void storeAccount(DatabaseUser databaseUser) {
        DatabaseReference users = databaseRef.child(NODE_USERS_LIST);
        DatabaseReference currentUser = users.child(databaseUser.uuid);

        currentUser.setValue(databaseUser);
    }

    private static void storeMemberOfEvent(DatabaseReference eventMembersRef, DatabaseUser
            databaseUser) {
        DatabaseReference currentMemberRef = eventMembersRef.child(databaseUser.uuid);

        currentMemberRef.setValue(databaseUser);
    }

    private static void storeEvent(Event event) {
        DatabaseEvent eventToStore = new DatabaseEvent(event.getEventName(),
                                                       event.getDescription(),
                                                       event.getStartTime().toString(),
                                                       event.getEndTime().toString(),
                                                       event.getUUID(),
                                                       new HashMap<String, DatabaseUser>());
        storeEvent(eventToStore);

        DatabaseReference membersRef = databaseRef.child(NODE_EVENTS_LIST).child(event.getUUID())
                .child(NODE_EVENT_MEMBERS);
        for (Member memberToStore : event.getEventMembers()) {
            DatabaseUser
                    databaseUser =
                    new DatabaseUser(memberToStore.getGivenName().getOrElse(EMPTY_FIELD),
                                     memberToStore.getFamilyName().getOrElse(EMPTY_FIELD),
                                     memberToStore.getDisplayName().getOrElse(EMPTY_FIELD),
                                     memberToStore.getEmail().getOrElse(EMPTY_FIELD),
                                     memberToStore.getUUID().getOrElse(EMPTY_FIELD));

            storeMemberOfEvent(membersRef, databaseUser);
        }

        /*
        List<DatabaseUser> members = new ArrayList<>();

        for (Member member : event.getEventMembers()) {
            DatabaseUser databaseUser;

            if (member.getUUID().getOrElse(EMPTY_FIELD).equals(
                    Account.shared.getUUID().getOrElse(EMPTY_FIELD))) {
                databaseUser =
                        new DatabaseUser(Account.shared.getGivenName().getOrElse(EMPTY_FIELD),
                                         Account.shared.getFamilyName().getOrElse(EMPTY_FIELD),
                                         Account.shared.getDisplayName().getOrElse(EMPTY_FIELD),
                                         Account.shared.getEmail().getOrElse(EMPTY_FIELD),
                                         Account.shared.getUUID().getOrElse(EMPTY_FIELD));
            } else {
                databaseUser = new DatabaseUser(member.getGivenName().getOrElse(EMPTY_FIELD),
                                                member.getFamilyName().getOrElse(EMPTY_FIELD),
                                                member.getDisplayName().getOrElse(EMPTY_FIELD),
                                                member.getEmail().getOrElse(EMPTY_FIELD),
                                                member.getUUID().getOrElse(EMPTY_FIELD));
            }

            members.add(databaseUser);
        }

        DatabaseEvent eventToStore = new DatabaseEvent(event.getEventName(),
                                                       event.getDescription(),
                                                       event.getStartTime().toString(),
                                                       event.getEndTime().toString(),
                                                       event.getUUID(),
                                                       members);

        storeEvent(eventToStore);
        */
    }

    private static void storeEvent(DatabaseEvent databaseEvent) {
        DatabaseReference events = databaseRef.child(NODE_EVENTS_LIST);
        DatabaseReference currentEvent = events.child(databaseEvent.uuid);

        currentEvent.setValue(databaseEvent);
    }

    /*
    private static ValueEventListener getUsersListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
    */

    private static ValueEventListener getEventsListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    DatabaseEvent event = eventSnapshot.getValue(DatabaseEvent.class);

                    if (event != null) {
                        List<String> uuids = new ArrayList<>();
                        for (DatabaseUser user : event.members.values()) {
                            uuids.add(user.uuid);
                        }

                        if (uuids.contains(Account.shared.getUUID().getOrElse(EMPTY_FIELD))) {

                            Log.e("###", event.uuid);

                            List<Member> members = new ArrayList<>();

                            for (DatabaseUser user : event.members.values()) {
                                Member memberToAdd = new Member(user.uuid,
                                                                user.display_name,
                                                                user.given_name,
                                                                user.family_name,
                                                                user.email);
                                members.add(memberToAdd);
                            }

                            Event tempEvent =
                                    new Event(event.name,
                                              LocalDateTime.parse(event.datetime_start),
                                              LocalDateTime.parse(event.datetime_end),
                                              event.description, members);
                            Account.shared.addEvent(tempEvent);

                            for (Event event1 : Account.shared.getPastEvents()) {
                                Log.e("###", event1.toString());
                            }
                            for (Event event1 : Account.shared.getFutureEvents()) {
                                Log.e("###", event1.toString());
                            }
                            Log.e("###", Account.shared.getCurrentEvent().toString());
                        }
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
