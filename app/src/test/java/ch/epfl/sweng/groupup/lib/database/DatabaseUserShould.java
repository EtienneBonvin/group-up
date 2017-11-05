package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import ch.epfl.sweng.groupup.lib.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseUserShould {
    @Test
    public void overrideDefaultConstructorAndHaveDefaultValuesAssigned() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser();

        assertEquals(Database.EMPTY_FIELD, databaseUser.given_name);
        assertEquals(Database.EMPTY_FIELD, databaseUser.family_name);
        assertEquals(Database.EMPTY_FIELD, databaseUser.display_name);
        assertEquals(Database.EMPTY_FIELD, databaseUser.email);
        assertEquals(Database.EMPTY_FIELD, databaseUser.uuid);
    }

    @Test
    public void assignEachPropertyCorrectlyUsingPreciseConstructor() throws Exception {
        String givenName = "Group";
        String familyName = "Up";
        String displayName = "GroupUp";
        String email = "groupup@flyingmonkeys.com";
        String uuid = "myUserUuidIsVeryComplex";

        DatabaseUser databaseUser0 = new DatabaseUser(Optional.from(givenName),
                                                      Optional.from(familyName),
                                                      Optional.from(displayName),
                                                      Optional.from(email),
                                                      Optional.from(uuid),
                                                      Optional.<Location>empty());

        Location myTestLocation = new Location(LocationManager.GPS_PROVIDER);
        DatabaseUser databaseUser1 = new DatabaseUser(Optional.from(givenName),
                                                      Optional.from(familyName),
                                                      Optional.from(displayName),
                                                      Optional.from(email),
                                                      Optional.from(uuid),
                                                      Optional.from(
                                                              myTestLocation));

        assertEquals(givenName, databaseUser1.given_name);
        assertEquals(familyName, databaseUser1.family_name);
        assertEquals(displayName, databaseUser1.display_name);
        assertEquals(email, databaseUser1.email);
        assertEquals(uuid, databaseUser1.uuid);
        assertEquals(myTestLocation, databaseUser1.getLocation().get());
    }

    @Test
    public void getGivenNameCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getGivenName().isEmpty());

        databaseUser = new DatabaseUser(Optional.from("NOT_EMPTY"),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getGivenName().isEmpty());
    }

    @Test
    public void getFamilyNameCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getFamilyName().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getFamilyName().isEmpty());
    }

    @Test
    public void getDisplayNameCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getDisplayName().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getDisplayName().isEmpty());
    }

    @Test
    public void getEmailCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getEmail().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getEmail().isEmpty());
    }

    @Test
    public void getUUIDCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getUUID().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getUUID().isEmpty());
    }

    @Test
    public void getLocationCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getLocation().isEmpty());

        Location myTestLocation = new Location(LocationManager.GPS_PROVIDER);
        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.from(myTestLocation));

        assertTrue(!databaseUser.getLocation().isEmpty());
    }
}
