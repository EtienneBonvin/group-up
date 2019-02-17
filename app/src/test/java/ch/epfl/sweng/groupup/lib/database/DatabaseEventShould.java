package ch.epfl.sweng.groupup.lib.database;

import static org.junit.Assert.*;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;
import java.util.HashMap;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.*;


public class DatabaseEventShould {

    @Test
    public void assignEachPropertyCorrectlyUsingPreciseConstructor() throws Exception {
        String name = "GroupUp Event";
        String description = "This is going to be fun.";
        String datetimeStart = DateTime.now()
                                       .toString();
        String datetimeEnd = DateTime.now()
                                     .plusDays(1)
                                     .toString();
        String uuid = "myEventUuidIsReallyComplex";

        HashMap<String, DatabaseUser> members = new HashMap<>();
        members.put("userUuid01", new DatabaseUser());
        members.put("userUuid02", new DatabaseUser());
        members.put("userUuid03", new DatabaseUser());

        HashMap<String, DatabasePointOfInterest> pointsOfInterest = new HashMap<>();
        pointsOfInterest.put("poiUuid01", new DatabasePointOfInterest());
        pointsOfInterest.put("poiUuid02", new DatabasePointOfInterest());
        pointsOfInterest.put("poiUuid03", new DatabasePointOfInterest());

        DatabaseEvent databaseEvent = new DatabaseEvent(name,
                                                        description,
                                                        datetimeStart,
                                                        datetimeEnd,
                                                        uuid,
                                                        members,
                                                        pointsOfInterest);

        assertEquals(name, databaseEvent.getName());
        assertEquals(description, databaseEvent.getDescription());
        assertEquals(datetimeStart, databaseEvent.getDatetimeStart());
        assertEquals(datetimeEnd, databaseEvent.getDatetimeEnd());
        assertEquals(uuid, databaseEvent.getUuid());
        assertEquals(members, databaseEvent.getMembers());
        assertEquals(pointsOfInterest, databaseEvent.getPointsOfInterest());
    }


    @Test
    public void correctlyConvertToEvent() {
        Account.shared.withUUID("ACCOUNT_UUID")
                      .withEmail("account@email.com");
        DatabaseUser invitedUser = new DatabaseUser();
        invitedUser.email = Account.shared.getEmail()
                                          .get();

        String name = "GroupUp Event";
        String description = "This is going to be fun.";
        String datetimeStart = LocalDateTime.now()
                                            .toString();
        String datetimeEnd = LocalDateTime.now()
                                          .plusDays(1)
                                          .toString();
        String uuid = "myEventUuidIsReallyComplex";

        HashMap<String, DatabaseUser> members = new HashMap<>();
        members.put("userUuid01", new DatabaseUser());
        members.put("userUuid02", new DatabaseUser());
        members.put("userUuid03", new DatabaseUser());
        members.put(Account.shared.getUUID()
                                  .get(),
                    Account.shared.toMember()
                                  .toDatabaseUser());
        members.put("INVITED_UUID", invitedUser);

        HashMap<String, DatabasePointOfInterest> pointsOfInterest = new HashMap<>();
        pointsOfInterest.put("poiUuid01", new DatabasePointOfInterest());

        DatabaseEvent databaseEvent = new DatabaseEvent(name,
                                                        description,
                                                        datetimeStart,
                                                        datetimeEnd,
                                                        uuid,
                                                        members,
                                                        pointsOfInterest);
        Event event = databaseEvent.toEvent();

        assertEquals(name, event.getEventName());
        assertEquals(description, event.getDescription());
        assertEquals(LocalDateTime.parse(datetimeStart), event.getStartTime());
        assertEquals(LocalDateTime.parse(datetimeEnd), event.getEndTime());
        assertEquals(uuid, event.getUUID());
        assertEquals(uuid, event.getUUID());
        assertEquals(uuid, event.getUUID());
        assertEquals(members.size(),
                     event.getEventMembers()
                          .size());
        assertEquals(pointsOfInterest.size(),
                     event.getPointsOfInterest()
                          .size());

        Account.shared.clear();
    }


    @Test
    public void correctlyTellIfWeAreContainedInTheEvent() {
        Account.shared.withUUID("ACCOUNT_UUID")
                      .withEmail("account@email.com");

        String name = "GroupUp Event";
        String description = "This is going to be fun.";
        String datetimeStart = DateTime.now()
                                       .toString();
        String datetimeEnd = DateTime.now()
                                     .plusDays(1)
                                     .toString();
        String uuid = "myEventUuidIsReallyComplex";

        HashMap<String, DatabaseUser> members = new HashMap<>();
        members.put("userUuid01", new DatabaseUser());
        members.put("userUuid02", new DatabaseUser());
        members.put("userUuid03", new DatabaseUser());

        HashMap<String, DatabasePointOfInterest> pointsOfInterest = new HashMap<>();
        pointsOfInterest.put("poiUuid01", new DatabasePointOfInterest());
        pointsOfInterest.put("poiUuid02", new DatabasePointOfInterest());
        pointsOfInterest.put("poiUuid03", new DatabasePointOfInterest());

        DatabaseEvent databaseEvent = new DatabaseEvent(name,
                                                        description,
                                                        datetimeStart,
                                                        datetimeEnd,
                                                        uuid,
                                                        members,
                                                        pointsOfInterest);

        assertFalse(databaseEvent.containedAsMember());

        members.put(Account.shared.getUUID()
                                  .get(),
                    Account.shared.toMember()
                                  .toDatabaseUser());
        databaseEvent.members = members;
        assertTrue(databaseEvent.containedAsMember());
        databaseEvent.members = new HashMap<>();

        members = new HashMap<>();
        DatabaseUser userWithOnlyEmail = new DatabaseUser();
        userWithOnlyEmail.email = Account.shared.getEmail()
                                                .get();
        members.put("NO_UUID", userWithOnlyEmail);
        databaseEvent.members = members;
        assertTrue(databaseEvent.containedAsMember());

        Account.shared.clear();
    }


    @Test
    public void overrideDefaultConstructorAndHaveDefaultValuesAssigned() throws Exception {
        DatabaseEvent databaseEvent = new DatabaseEvent();

        assertEquals(Database.EMPTY_FIELD, databaseEvent.name);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.description);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.datetimeStart);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.datetimeEnd);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.uuid);
        assertEquals(new HashMap<String, DatabaseUser>(), databaseEvent.members);
    }
}
