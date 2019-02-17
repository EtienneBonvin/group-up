package ch.epfl.sweng.groupup.object.account;

import static ch.epfl.sweng.groupup.object.account.Account.shared;
import static org.junit.Assert.*;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.joda.time.LocalDateTime;
import org.junit.*;


/**
 * Created by alix on 10/8/17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class AccountShould {

    private Event current = new Event("Test1234",
                                      new LocalDateTime().minusDays(1),
                                      new LocalDateTime().plusSeconds(2),
                                      "",
                                      new ArrayList<Member>(),
                                      false);
    private Event future = new Event("Test",
                                     new LocalDateTime().plusSeconds(2),
                                     new LocalDateTime().plusDays(2),
                                     "",
                                     new ArrayList<Member>(),
                                     false);
    private Event past = new Event("Test",
                                   new LocalDateTime().minusDays(2),
                                   new LocalDateTime().minusDays(1),
                                   "",
                                   new ArrayList<Member>(),
                                   false);


    @Test
    public void addCurrentEventOK() {
        shared.addOrUpdateEvent(current);
        assertEquals(shared.getCurrentEvents()
                           .get(0)
                           .getEventStatus(), EventStatus.CURRENT);
        shared.clear();
    }


    @Test
    public void addFutureEventOK() {
        shared.addOrUpdateEvent(future);
        assertEquals(shared.getFutureEvents()
                           .get(0)
                           .getEventStatus(), EventStatus.FUTURE);
        shared.clear();
    }


    @Test
    public void addPastEventOK() {
        shared.addOrUpdateEvent(past);
        assertEquals(shared.getPastEvents()
                           .get(0), past);
        shared.clear();
    }


    @Test
    public void clearTest() {
        shared.withFamilyName("Test")
              .withUUID("1")
              .withDisplayName("tested")
              .withEmail("test@test.test")
              .withGivenName("tester")
              .addOrUpdateEvent(current)
              .addOrUpdateEvent(future)
              .addOrUpdateEvent(past);
        shared.clear();
        assertEquals(shared.getUUID(), Optional.<String>empty());
        assertEquals(shared.getFamilyName(), Optional.<String>empty());
        assertEquals(shared.getDisplayName(), Optional.<String>empty());
        assertEquals(shared.getGivenName(), Optional.<String>empty());
        assertEquals(shared.getEmail(), Optional.<String>empty());
        assertEquals(shared.getCurrentEvents(), new ArrayList<Event>());
        assertEquals(shared.getFutureEvents(), new ArrayList<Event>());
        assertEquals(shared.getPastEvents(), new ArrayList<Event>());
    }


    @Test
    public void futureEventsOrderedCorrectly() {

        // Add future events in an unordered fashion
        shared.addOrUpdateEvent(new Event("Test days 1",
                                          new LocalDateTime().plusDays(34),
                                          new LocalDateTime().plusDays(39),
                                          "",
                                          new ArrayList<Member>(),
                                          false));

        shared.addOrUpdateEvent(new Event("Test years 1",
                                          new LocalDateTime().plusYears(1),
                                          new LocalDateTime().plusYears(2),
                                          "",
                                          new ArrayList<Member>(),
                                          false));

        shared.addOrUpdateEvent(new Event("Test minutes 1",
                                          new LocalDateTime().plusMinutes(10),
                                          new LocalDateTime().plusMinutes(13),
                                          "",
                                          new ArrayList<Member>(),
                                          false));

        // Create future events in an ordered fashion
        List<Event> correctlyOrderedFutureEvents = new ArrayList<>();

        correctlyOrderedFutureEvents.add(new Event("Test years 1",
                                                   new LocalDateTime().plusYears(1),
                                                   new LocalDateTime().plusYears(2),
                                                   "",
                                                   new ArrayList<Member>(),
                                                   false));
        correctlyOrderedFutureEvents.add(new Event("Test days 1",
                                                   new LocalDateTime().plusDays(34),
                                                   new LocalDateTime().plusDays(39),
                                                   "",
                                                   new ArrayList<Member>(),
                                                   false));
        correctlyOrderedFutureEvents.add(new Event("Test minutes 1",
                                                   new LocalDateTime().plusMinutes(10),
                                                   new LocalDateTime().plusMinutes(13),
                                                   "",
                                                   new ArrayList<Member>(),
                                                   false));

        for (int i = 0; i < correctlyOrderedFutureEvents.size(); i++) {
            assertEquals(shared.getFutureEvents()
                               .get(i)
                               .getEventName(),
                         correctlyOrderedFutureEvents.get(i)
                                                     .getEventName());
        }
        shared.clear();
    }


    @Test
    public void getAllEventsOnlyOnce() {
        // add past and future event
        shared.withPastEvents(Collections.singletonList(past));
        shared.withFutureEvents(Collections.singletonList(future));

        int size = Account.shared.getPastEvents()
                                 .size() + Account.shared.getFutureEvents()
                                                         .size();
        assertEquals(Account.shared.getEvents()
                                   .size(), size);

        // add current event
        shared.withCurrentEvent(Collections.singletonList(current));
        assertEquals(Account.shared.getEvents()
                                   .size(), size + 1);

        // re-accessing the list should not increase size
        int amountOfEvents = Account.shared.getEvents()
                                           .size();
        assertEquals(amountOfEvents,
                     Account.shared.getEvents()
                                   .size());
        shared.clear();
    }


    /**
     * That was used to debug, we keep it even if it is not very, we are keeping it just in case
     */
    @Test
    public void getAllPastEvents() {
        shared.withPastEvents(Arrays.asList(past,
                                            new Event("PastEvent2",
                                                      LocalDateTime.now()
                                                                   .minusDays(3),
                                                      LocalDateTime.now()
                                                                   .minusDays(1),
                                                      "Description",
                                                      new ArrayList<Member>(),
                                                      false),
                                            new Event("PastEvent3",
                                                      LocalDateTime.now()
                                                                   .minusDays(5),
                                                      LocalDateTime.now()
                                                                   .minusDays(1),
                                                      "Description",
                                                      new ArrayList<Member>(),
                                                      false)));

        int amountPastEvents = 0;
        for (Event e : Account.shared.getEvents()) {
            if (e.getEventStatus()
                 .equals(EventStatus.PAST)) {
                amountPastEvents++;
            }
        }
        assertEquals(Account.shared.getPastEvents()
                                   .size(), amountPastEvents);
        shared.clear();
    }


    @Test
    public void getOnlyCurrentEvent() {
        shared.withPastEvents(Collections.singletonList(past));
        shared.withFutureEvents(Collections.singletonList(future));
        shared.withCurrentEvent(Collections.singletonList(current));
        for (Event e : shared.getCurrentEvents()) {
            assertEquals(e.getEventStatus(), EventStatus.CURRENT);
        }
        shared.clear();
    }


    @Test
    public void getOnlyFutureEvents() {
        shared.withPastEvents(Collections.singletonList(past));
        shared.withFutureEvents(Collections.singletonList(future));
        shared.withCurrentEvent(Collections.singletonList(current));

        for (Event e : shared.getFutureEvents()) {
            assertEquals(e.getEventStatus(), EventStatus.FUTURE);
        }
        shared.clear();
    }


    @Test
    public void getOnlyPastEvents() {
        shared.withPastEvents(Collections.singletonList(past));
        shared.withFutureEvents(Collections.singletonList(future));
        shared.withCurrentEvent(Collections.singletonList(current));

        for (Event e : shared.getPastEvents()) {
            assertEquals(e.getEventStatus(), EventStatus.PAST);
        }
        shared.clear();
    }


    @Test
    public void numberOfEventsUnchangedAfterCurrentToPastTransition() {
        shared.clear();
        shared.withCurrentEvent(Collections.singletonList(current));
        int size = Account.shared.getEvents()
                                 .size();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(size,
                     Account.shared.getEvents()
                                   .size());
        shared.clear();
    }


    @Test
    public void numberOfEventsUnchangedAfterFutureToCurrentTransition() {
        shared.clear();
        shared.withFutureEvents(Collections.singletonList(future));
        int size = Account.shared.getEvents()
                                 .size();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Account.shared.getEvents()
                                   .size(), size);
        shared.clear();
    }


    @Test
    public void toStringShortTest() {
        shared.clear();
        shared.withCurrentEvent(Collections.singletonList(current));
        String expected = "Account{"
                          + "givenName='"
                          + shared.getGivenName()
                          + '\''
                          + ", familyName='"
                          + shared.getFamilyName()
                          + '\''
                          + ", email='"
                          + shared.email
                          + '\''
                          + ", currentEvent="
                          + Collections.singleton(current.toString())
                          + '}';
        assertEquals(shared.toStringShort(), expected);
        shared.clear();
    }


    @Test
    public void toStringTest() {
        shared.clear();
        shared.withCurrentEvent(Collections.singletonList(current));
        String expected = "Account{"
                          + "UUID='"
                          + shared.getUUID()
                          + '\''
                          + "displayName='"
                          + shared.getDisplayName()
                          + '\''
                          + "givenName='"
                          + shared.getGivenName()
                          + '\''
                          + ", familyName='"
                          + shared.getFamilyName()
                          + '\''
                          + ", email='"
                          + shared.getEmail()
                          + '\''
                          + ", currentEvent="
                          + Collections.singleton(current.toString())
                          + ", pastEvents="
                          + shared.getPastEvents()
                          + ", futureEvents="
                          + shared.getFutureEvents()
                          + '}';
        assertEquals(shared.toString(), expected);
        shared.clear();
    }


    @Test
    public void updateEventsCorrectly() {
        // Test for past event
        shared.addOrUpdateEvent(past);
        int c1 = shared.getPastEvents()
                       .size();
        shared.addOrUpdateEvent(new Event("Test2",
                                          new LocalDateTime().minusDays(3),
                                          new LocalDateTime().minusDays(2),
                                          "",
                                          new ArrayList<Member>(),
                                          false));

        assertEquals(shared.getPastEvents()
                           .get(c1), past);

        // Test for future event
        shared.addOrUpdateEvent(future);
        int c2 = shared.getPastEvents()
                       .size();
        shared.addOrUpdateEvent(new Event("UUID",
                                          "Test3",
                                          new LocalDateTime().plusDays(1),
                                          new LocalDateTime().plusDays(2),
                                          "",
                                          new ArrayList<Member>(),
                                          new HashSet<PointOfInterest>(),
                                          false));

        assertEquals(shared.getFutureEvents()
                           .get(c2 - 1), future);

        // Test for current event
        shared.withCurrentEvent(Collections.singletonList(current));
        assertEquals(shared.getCurrentEvents()
                           .get(0), current);
        shared.clear();
    }


    @Test
    public void withDisplayNameOK() {
        shared.withDisplayName("Tester");
        assertEquals(shared.getDisplayName()
                           .get(), "Tester");
        shared.clear();
    }


    @Test(expected = IllegalArgumentException.class)
    public void withEmailNotOK() {
        shared.withEmail("13415.com");
        shared.clear();
    }


    @Test
    public void withEmailOK() {
        shared.withEmail("test@testedtest.com");
        assertEquals(shared.getEmail()
                           .get(), "test@testedtest.com");
        shared.clear();
    }


    @Test
    public void withFirstNameOK() {
        shared.withGivenName("Tester");
        assertEquals(shared.getGivenName()
                           .get(), "Tester");
        shared.clear();
    }


    @Test
    public void withLastNameOK() {
        shared.withFamilyName("Test");
        assertEquals(shared.getFamilyName()
                           .get(), "Test");
        shared.clear();
    }


    @Test
    public void withLocationOK() {
        shared.withLocation(null);
        assertTrue(shared.getLocation()
                         .isEmpty());
        shared.clear();
    }


    @Test
    public void withUUIDOK() {
        shared.withUUID("UUID2");
        assertEquals(shared.getUUID()
                           .get(), "UUID2");
        shared.clear();
    }
}
