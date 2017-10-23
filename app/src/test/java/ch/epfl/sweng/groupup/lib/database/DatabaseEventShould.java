package ch.epfl.sweng.groupup.lib.database;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DatabaseEventShould {
    @Test
    public void overrideDefaultConstructorAndHaveDefaultValuesAssigned() throws Exception {
        DatabaseEvent databaseEvent = new DatabaseEvent();

        assertEquals(Database.EMPTY_FIELD, databaseEvent.name);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.description);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.datetime_start);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.datetime_end);
        assertEquals(Database.EMPTY_FIELD, databaseEvent.uuid);
        assertEquals(new HashMap<String, DatabaseUser>(), databaseEvent.members);
    }

    @Test
    public void assignEachPropertyCorrectlyUsingPreciseConstructor() throws Exception {
        String name = "GroupUp Event";
        String description = "This is going to be fun.";
        String datetime_start = DateTime.now().toString();
        String datetime_end = DateTime.now().toString();
        String uuid = "myEventUuidIsReallyComplex";

        HashMap<String, DatabaseUser> members = new HashMap<>();
        members.put("userUuid1", new DatabaseUser());
        members.put("userUuid2", new DatabaseUser());
        members.put("userUuid3", new DatabaseUser());

        DatabaseEvent databaseEvent = new DatabaseEvent(name, description, datetime_start,
                                                        datetime_end, uuid, members);

        assertEquals(name, databaseEvent.name);
        assertEquals(description, databaseEvent.description);
        assertEquals(datetime_start, databaseEvent.datetime_start);
        assertEquals(datetime_end, databaseEvent.datetime_end);
        assertEquals(uuid, databaseEvent.uuid);
        assertEquals(members, databaseEvent.members);
    }
}
