package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;

import org.junit.Test;

import ch.epfl.sweng.groupup.lib.Optional;

import static org.junit.Assert.assertEquals;

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
    }
}
