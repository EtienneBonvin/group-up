package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseShould {
    @Rule
    public final ActivityTestRule<ToolbarActivity> mActivityRule =
            new ActivityTestRule<>(ToolbarActivity.class);

    @Test
    public void exposeSetUpMethod() throws Exception {
        Database.setUpDatabase();

        assertTrue(true);
    }

    @Test
    public void exposeSetUpListenerForDefaultAndOwnListener() throws Exception {
        Database.setUpDatabase();

        Database.setUpEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Do nothing
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });

        assertTrue(true);
    }

    @Test
    public void exposeAnUpdateMethod() throws Exception {
        Database.setUpDatabase();

        Database.update();

        assertTrue(true);
    }

    @Test
    public void updateDatabaseCorrectlyAccordingToTheUsersAccount() throws Exception {
        // Database set up.
        Database.setUpDatabase();

        // Account initialization.
        String givenName = "Group";
        String familyName = "Up";
        String displayName = "GroupUp";
        String email = "groupup@flyingmonkeys.com";
        String uuid = "myAccountUuidIsVeryComplex";
        Account.shared.withGivenName(givenName)
                .withFamilyName(familyName)
                .withDisplayName(displayName)
                .withEmail(email)
                .withUUID(uuid);

        // Add of current event.
        String nameCurrent = "Current Event";
        String descriptionCurrent = "DescriptionCurrent";
        LocalDateTime startCurrent = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endCurrent = LocalDateTime.now().plusDays(1);
        final String uuidCurrent = "uuidCurrent";
        List<Member> membersCurrent = new ArrayList<>();
        String userName01 = "User";
        String userFamilyName01 = "01";
        String userDisplayName01 = "User01";
        String userEmail01 = "user@01.com";
        membersCurrent.add(new Member(Optional.from(userName01),
                                      Optional.from(userFamilyName01),
                                      Optional.from(userDisplayName01),
                                      Optional.from(userEmail01),
                                      Optional.from(uuid),
                                      Optional.<Location>empty()));
        Set<PointOfInterest> poiCurrent = new HashSet<>();
        String poiUuid01 = "PoIUuid01";
        String poiName01 = "PoIName01";
        String poiDesc01 = "PoIDesc01";
        Location poiLocation01 = new Location(LocationManager.GPS_PROVIDER);
        poiCurrent.add(new PointOfInterest(poiUuid01,
                                           poiName01,
                                           poiDesc01,
                                           poiLocation01));
        final Event eventCurrent = new Event(uuidCurrent,
                                             nameCurrent,
                                             startCurrent,
                                             endCurrent,
                                             descriptionCurrent,
                                             membersCurrent,
                                             poiCurrent,
                                             false);
        Account.shared.addOrUpdateEvent(eventCurrent);

        // Add of past event.
        String namePast = "Past Event";
        String descriptionPast = "Description Past";
        LocalDateTime startPast = LocalDateTime.now().minusDays(2);
        LocalDateTime endPast = LocalDateTime.now().minusDays(1);
        final String uuidPast = "uuidPast";
        List<Member> membersPast = new ArrayList<>();
        String userName02 = "User";
        String userFamilyName02 = "02";
        String userDisplayName02 = "User02";
        String userEmail02 = "user@02.com";
        String userUuid02 = "myUserUuid02";
        membersPast.add(new Member(Optional.from(userName02),
                                   Optional.from(userFamilyName02),
                                   Optional.from(userDisplayName02),
                                   Optional.from(userEmail02),
                                   Optional.from(userUuid02),
                                   Optional.<Location>empty()));
        Set<PointOfInterest> poiPast = new HashSet<>();
        String poiUuid02 = "PoIUuid02";
        String poiName02 = "PoIName02";
        String poiDesc02 = "PoIDesc02";
        Location poiLocation02 = new Location(LocationManager.GPS_PROVIDER);
        poiPast.add(new PointOfInterest(poiUuid02,
                                        poiName02,
                                        poiDesc02,
                                        poiLocation02));
        final Event eventPast = new Event(uuidPast,
                                          namePast,
                                          startPast,
                                          endPast,
                                          descriptionPast,
                                          membersPast,
                                          poiPast,
                                          false);
        Account.shared.addOrUpdateEvent(eventPast);

        // Add of future event.
        String nameFuture = "Future Event";
        String descriptionFuture = "Description Future";
        LocalDateTime startFuture = LocalDateTime.now().plusDays(2);
        LocalDateTime endFuture = LocalDateTime.now().plusDays(3);
        final String uuidFuture = "uuidFuture";
        List<Member> membersFuture = new ArrayList<>();
        String userName03 = "User";
        String userFamilyName03 = "03";
        String userDisplayName03 = "User03";
        String userEmail03 = "user@03.com";
        String userUuid03 = "myUserUuid03";
        membersFuture.add(new Member(Optional.from(userName03),
                                     Optional.from(userFamilyName03),
                                     Optional.from(userDisplayName03),
                                     Optional.from(userEmail03),
                                     Optional.from(userUuid03),
                                     Optional.<Location>empty()));
        Set<PointOfInterest> poiFuture = new HashSet<>();
        String poiUuid03 = "PoIUuid03";
        String poiName03 = "PoIName03";
        String poiDesc03 = "PoIDesc03";
        Location poiLocation03 = new Location(LocationManager.GPS_PROVIDER);
        poiFuture.add(new PointOfInterest(poiUuid03,
                                          poiName03,
                                          poiDesc03,
                                          poiLocation03));
        final Event eventFuture = new Event(uuidFuture,
                                            nameFuture,
                                            startFuture,
                                            endFuture,
                                            descriptionFuture,
                                            membersFuture,
                                            poiFuture,
                                            false);
        final String uuidEmpty = "EmptyUUID";
        final Event eventEmpty = new Event(uuidEmpty,
                                           "Event",
                                           LocalDateTime.now().plusDays(7),
                                           LocalDateTime.now().plusDays(9),
                                           descriptionFuture,
                                           new ArrayList<Member>(),
                                           new HashSet<PointOfInterest>(),
                                           false);
        Account.shared.addOrUpdateEvent(eventFuture);
        Account.shared.addOrUpdateEvent(eventEmpty);

        Database.update();

        // Event listener set up.
        Database.setUpEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    DatabaseEvent
                            event =
                            eventSnapshot.getValue(DatabaseEvent.class);

                    if (event != null &&
                        !event.uuid.equals(Database.EMPTY_FIELD)) {
                        if (event.uuid.equals(uuidCurrent) ||
                            event.uuid.equals(uuidPast) ||
                            event.uuid.equals(uuidFuture)) {

                            List<Member> members = new ArrayList<>();
                            for (DatabaseUser user : event.members.values()) {
                                members.add(new Member(user.getOptUuid(),
                                                       user.getOptDisplayName(),
                                                       user.getOptGivenName(),
                                                       user.getOptFamilyName(),
                                                       user.getOptEmail(),
                                                       user.getOptLocation()));
                            }

                            HashSet<PointOfInterest> pointsOfInterest = new
                                    HashSet<>();
                            for (DatabasePointOfInterest poi : event
                                    .pointsOfInterest.values()) {
                                pointsOfInterest.add(new PointOfInterest(poi.uuid,
                                                                         poi.name,
                                                                         poi.description,
                                                                         poi.getLocation()));
                            }

                            Event tempEvent = new Event(
                                    event.uuid,
                                    event.name,
                                    LocalDateTime.parse(event.datetimeStart),
                                    LocalDateTime.parse(event.datetimeEnd),
                                    event.description, members,
                                    pointsOfInterest,
                                    false);

                            switch (event.uuid) {
                                case uuidCurrent:
                                    assertEquals(eventCurrent, tempEvent);
                                    break;
                                case uuidPast:
                                    assertEquals(eventPast, tempEvent);
                                    break;
                                case uuidFuture:
                                    assertEquals(eventFuture, tempEvent);
                                    break;
                                case uuidEmpty:
                                    assertEquals(eventEmpty, tempEvent);
                                    break;
                                default:
                                    throw new Error(
                                            "default case in switch statement");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Nothing
            }
        });

        Database.update();
        Database.setUpEventListener(null);
        Database.update();

        Thread.sleep(2000);
    }
}
