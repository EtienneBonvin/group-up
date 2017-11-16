package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;

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

        DatabaseUser databaseUser = new DatabaseUser(Optional.from(givenName),
                                                      Optional.from(familyName),
                                                      Optional.from(displayName),
                                                      Optional.from(email),
                                                      Optional.from(uuid),
                                                      Optional.<Location>empty());

        assertEquals(givenName, databaseUser.given_name);
        assertEquals(familyName, databaseUser.family_name);
        assertEquals(displayName, databaseUser.display_name);
        assertEquals(email, databaseUser.email);
        assertEquals(uuid, databaseUser.uuid);
        assertTrue(databaseUser.getOptLocation().isEmpty());
    }

    @Test
    public void getGivenNameCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getOptGivenName().isEmpty());

        databaseUser = new DatabaseUser(Optional.from("NOT_EMPTY"),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getOptGivenName().isEmpty());
    }

    @Test
    public void getFamilyNameCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getOptFamilyName().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getOptFamilyName().isEmpty());
    }

    @Test
    public void getDisplayNameCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getOptDisplayName().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getOptDisplayName().isEmpty());
    }

    @Test
    public void getEmailCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getOptEmail().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getOptEmail().isEmpty());
    }

    @Test
    public void getUUIDCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getOptUUID().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getOptUUID().isEmpty());
    }

    @Test
    public void getLocationCorrectly() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.getOptLocation().isEmpty());
    }
}
