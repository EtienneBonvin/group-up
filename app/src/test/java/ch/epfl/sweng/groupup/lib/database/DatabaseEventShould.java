package ch.epfl.sweng.groupup.lib.database;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class DatabaseEventShould {
    @Test
    public void overrideDefaultConstructorAndHaveDefaultValuesAssigned() throws Exception {
        DatabaseEvent databaseEvent = new DatabaseEvent();

        assertEquals(Database.EMPTY_FIELD, databaseEvent.name);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.description);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.datetimeStart);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.datetimeEnd);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.uuid);
        assertEquals(new HashMap<String, DatabaseUser>(),
                     databaseEvent.members);
    }

    @Test
    public void assignEachPropertyCorrectlyUsingPreciseConstructor() throws Exception {
        String name = "GroupUp Event";
        String description = "This is going to be fun.";
        String datetimeStart = DateTime.now().toString();
        String datetimeEnd = DateTime.now().plusDays(1).toString();
        String uuid = "myEventUuidIsReallyComplex";

        HashMap<String, DatabaseUser> members = new HashMap<>();
        members.put("userUuid01", new DatabaseUser());
        members.put("userUuid02", new DatabaseUser());
        members.put("userUuid03", new DatabaseUser());

        HashMap<String, DatabasePointOfInterest> pointsOfInterest = new
                HashMap<>();
        pointsOfInterest.put("poiUuid01", new DatabasePointOfInterest());
        pointsOfInterest.put("poiUuid02", new DatabasePointOfInterest());
        pointsOfInterest.put("poiUuid03", new DatabasePointOfInterest());

        DatabaseEvent databaseEvent =
                new DatabaseEvent(name, description, datetimeStart,
                                  datetimeEnd, uuid,
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
}
