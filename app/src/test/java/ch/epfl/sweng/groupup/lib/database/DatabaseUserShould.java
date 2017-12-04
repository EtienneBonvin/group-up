package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;

import org.junit.Test;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class DatabaseUserShould {

    @Test
    public void overrideDefaultConstructorAndHaveDefaultValuesAssigned() throws Exception {
        DatabaseUser databaseUser = new DatabaseUser();

        assertEquals(Database.EMPTY_FIELD, databaseUser.givenName);
        assertEquals(Database.EMPTY_FIELD, databaseUser.familyName);
        assertEquals(Database.EMPTY_FIELD, databaseUser.displayName);
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

        assertEquals(givenName, databaseUser.givenName);
        assertEquals(familyName, databaseUser.familyName);
        assertEquals(displayName, databaseUser.displayName);
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

        assertTrue(databaseUser.getOptUuid().isEmpty());

        databaseUser = new DatabaseUser(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.from("NOT_EMPTY"),
                                        Optional.<Location>empty());

        assertTrue(!databaseUser.getOptUuid().isEmpty());
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


    @Test
    public void correctlyClearLocation() {
        DatabaseUser databaseUser = new DatabaseUser(Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<String>empty(),
                                                     Optional.<Location>empty());
        databaseUser.latitude = "NOT_EMPTY";
        databaseUser.longitude = "NOT_EMPTY";
        databaseUser.provider = "NOT_EMPTY";

        databaseUser.clearLocation();

        assertEquals(Database.EMPTY_FIELD, databaseUser.getLatitude());
        assertEquals(Database.EMPTY_FIELD, databaseUser.getLongitude());
        assertEquals(Database.EMPTY_FIELD, databaseUser.getProvider());
    }


    @Test
    public void onlyBeEqualIfReallyEqual() {
        DatabaseUser user01 = new DatabaseUser(Optional.from("NOT_EMPTY"),
                                               Optional.from("NOT_EMPTY"),
                                               Optional.from("NOT_EMPTY"),
                                               Optional.from("NOT_EMPTY"),
                                               Optional.from("NOT_EMPTY"),
                                               Optional.<Location>empty());

        DatabaseUser user02 = new DatabaseUser(Optional.from("NOT_EMPTY"),
                                               Optional.from("NOT_EMPTY"),
                                               Optional.from("NOT_EMPTY"),
                                               Optional.from("NOT_EMPTY"),
                                               Optional.from("NOT_EMPTY"),
                                               Optional.<Location>empty());

        assertEquals(user01, user02);

        user02.givenName = "OTHER_STUFF";
        assertNotEquals(user01, user02);
        user02.givenName = "NOT_EMPTY";

        user02.familyName = "OTHER_STUFF";
        assertNotEquals(user01, user02);
        user02.familyName = "NOT_EMPTY";

        user02.displayName = "OTHER_STUFF";
        assertNotEquals(user01, user02);
        user02.displayName = "NOT_EMPTY";

        user02.email = "OTHER_STUFF";
        assertNotEquals(user01, user02);
        user02.email = "NOT_EMPTY";

        user02.uuid = "OTHER_STUFF";
        assertNotEquals(user01, user02);
        user02.uuid = "NOT_EMPTY";

        user02.latitude = "OTHER_STUFF";
        assertNotEquals(user01, user02);
        user02.latitude = "NOT_EMPTY";

        user02.longitude = "OTHER_STUFF";
        assertNotEquals(user01, user02);
        user02.longitude = "NOT_EMPTY";
    }


    @Test
    public void correctlyBeConvertedToMember() {
        DatabaseUser databaseUser = new DatabaseUser(Optional.from("NOT_EMPTY01"),
                                                     Optional.from("NOT_EMPTY02"),
                                                     Optional.from("NOT_EMPTY03"),
                                                     Optional.from("NOT_EMPTY04"),
                                                     Optional.from("NOT_EMPTY05"),
                                                     Optional.<Location>empty());
        Member member = databaseUser.toMember();

        assertEquals(databaseUser.getGivenName(), member.getGivenName().get());
        assertEquals(databaseUser.getFamilyName(), member.getFamilyName().get());
        assertEquals(databaseUser.getDisplayName(), member.getDisplayName().get());
        assertEquals(databaseUser.getEmail(), member.getEmail().get());
        assertEquals(databaseUser.getUuid(), member.getUUID().get());
        assertTrue(databaseUser.getOptLocation().isEmpty());
    }


    @Test
    public void onlyReturnTrueIfTheUserIsReallyAccount() {
        String uuid = "MY_UUID";
        String email = "email@email.email";

        Account.shared.withUUID(uuid).withEmail(email);

        DatabaseUser databaseUser = new DatabaseUser(Optional.from("NOT_EMPTY01"),
                                                     Optional.from("NOT_EMPTY02"),
                                                     Optional.from("NOT_EMPTY03"),
                                                     Optional.from("NOT_EMPTY04"),
                                                     Optional.from(uuid),
                                                     Optional.<Location>empty());

        assertTrue(databaseUser.isAccount());

        databaseUser.uuid = "NOT_SAME_UUID_ANYMORE";
        databaseUser.email = email;

        assertTrue(databaseUser.isAccount());

        databaseUser.email = "NOT_SAME_EMAIL_ANYMORE";

        assertFalse(databaseUser.isAccount());

        Account.shared.clear();
    }
}
