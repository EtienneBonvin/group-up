package ch.epfl.sweng.groupup.object.account;

import android.util.Log;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.experimental.categories.Categories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ch.epfl.sweng.groupup.lib.Optional.from;
import static org.junit.Assert.*;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

import static ch.epfl.sweng.groupup.object.account.Account.shared;

/**
 * Created by alix on 10/8/17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class AccountShould {

    @Test
    public void withUUIDOK() {
        shared.withUUID("UUID2");
        assertEquals(shared.getUUID().get(), "UUID2");
        shared.clear();
    }

    @Test
    public void withDisplayNameOK(){
        shared.withDisplayName("Tester");
        assertEquals(shared.getDisplayName().get(),"Tester");
        shared.clear();
    }

    @Test
    public void withFirstNameOK(){
        shared.withGivenName("Tester");
        assertEquals(shared.getGivenName().get(),"Tester");
        shared.clear();
    }

    @Test
    public void withLastNameOK(){
        shared.withFamilyName("Test");
        assertEquals(shared.getFamilyName().get(),"Test");
        shared.clear();
    }

    @Test
    public void withEmailOK(){
        shared.withEmail("test@testedtest.com");
        assertEquals(shared.getEmail().get(),"test@testedtest.com");
        shared.clear();
    }

    @Test(expected=IllegalArgumentException.class)
    public void withEmailNotOK(){
        shared.withEmail("13415.com");
        shared.clear();
    }

    @Test
    public void withCurrentEventOK(){
        shared.withCurrentEvent(from(new Event("Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
        shared.clear();
    }
    @Test(expected = IllegalArgumentException.class)
    public void withCurrentEventNotOKWithPastEvent(){
        shared.withCurrentEvent(from(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>())));
    }

    @Test
    public void addPastEventOK(){
        shared.addOrUpdatePastEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>()));
        assertEquals(shared.getPastEvents().get(0).getEventStatus(),EventStatus.PAST);
        shared.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPastEventNotOKWithCurrentEvent() {
        shared.addOrUpdatePastEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>()));
        shared.clear();
    }
    @Test
    public void addFutureEventOK(){
        shared.addOrUpdateFutureEvent(new Event("Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), "", new ArrayList<Member>()));
        assertEquals(shared.getFutureEvents().get(0).getEventStatus(),EventStatus.FUTURE);
        shared.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureEventNotOKWithCurrentEvent() {
        shared.addOrUpdateFutureEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureEventNotOKWithPastEvent() {
        shared.addOrUpdateFutureEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>()));
        shared.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPastEventNotOKWithFutureEvent() {
        shared.addOrUpdatePastEvent(new Event("Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), "", new ArrayList<Member>()));
        shared.clear();
    }

    @Test
    public void addEventsCorrectly(){
        // Test for past event
        shared.addOrUpdatePastEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>()));
        assertEquals(shared.getPastEvents().get(0).getEventStatus(),EventStatus.PAST);

        // Test for future event
        shared.addOrUpdateFutureEvent(new Event("Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), "", new ArrayList<Member>()));
        assertEquals(shared.getFutureEvents().get(0).getEventStatus(),EventStatus.FUTURE);

        // Test for current event
        shared.withCurrentEvent(from(new Event("Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
        shared.clear();
    }

    @Test
    public void getOnlyFutureEvents(){
        shared.withPastEvents(Arrays.asList(new Event("PastEvent", LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), "Description", new ArrayList<Member>())));
        shared.withFutureEvents(Arrays.asList(new Event("FutureEvent", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), "Description", new ArrayList<Member>())));
        shared.withCurrentEvent(from(new Event("Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));

        for (Event e : shared.getFutureEvents()){
            assertEquals(e.getEventStatus(),EventStatus.FUTURE);
        }
        shared.clear();
    }

    @Test
    public void getOnlyPastEvents(){
        shared.withPastEvents(Arrays.asList(new Event("PastEvent", LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), "Description", new ArrayList<Member>())));
        shared.withFutureEvents(Arrays.asList(new Event("FutureEvent", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), "Description", new ArrayList<Member>())));
        shared.withCurrentEvent(from(new Event("Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));

        for (Event e : shared.getPastEvents()){
            assertEquals(e.getEventStatus(),EventStatus.PAST);
        }
        shared.clear();
    }

    @Test
    public void getAllPastEvents(){
        shared.withPastEvents(Arrays.asList(new Event("PastEvent1", LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), "Description", new ArrayList<Member>())));
        shared.withPastEvents(Arrays.asList(new Event("PastEvent2", LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(1), "Description", new ArrayList<Member>())));
        shared.withPastEvents(Arrays.asList(new Event("PastEvent3", LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(1), "Description", new ArrayList<Member>())));

        int amoutPastEvents=0;
        for (Event e : Account.shared.getEvents()){
            if (e.getEventStatus().equals(EventStatus.PAST)){
                amoutPastEvents++;
            }
        }
        assertEquals(Account.shared.getPastEvents().size(),amoutPastEvents);
        shared.clear();
    }

    @Test
    public void getOnlyCurrentEvent(){
        shared.withPastEvents(Arrays.asList(new Event("PastEvent", LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), "Description", new ArrayList<Member>())));
        shared.withFutureEvents(Arrays.asList(new Event("FutureEvent", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), "Description", new ArrayList<Member>())));
        shared.withCurrentEvent(from(new Event("Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
        shared.clear();
    }

    @Test
    public void getAllEventsOnlyOnce(){
        // add past and future event
        shared.withPastEvents(Arrays.asList(new Event("PastEvent", LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), "Description", new ArrayList<Member>())));
        shared.withFutureEvents(Arrays.asList(new Event("FutureEvent", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), "Description", new ArrayList<Member>())));

        int size = Account.shared.getPastEvents().size() + Account.shared.getFutureEvents().size();
        assertEquals(Account.shared.getEvents().size(), size);

        // add current event
        shared.withCurrentEvent(from(new Event("Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        assertEquals(Account.shared.getEvents().size(), size + 1);

        // re-accessing the list should not increase size
        int amountOfEvents = Account.shared.getEvents().size();
        assertEquals(amountOfEvents, Account.shared.getEvents().size());
        shared.clear();
    }

    // Annoying because need to wait for 10 sec
    @Test
    public void numberOfEventsUnchangedAfterFutureToCurrentTransition(){
        shared.withFutureEvents(Arrays.asList(new Event("FutureEvent", LocalDateTime.now().plusSeconds(5),
                LocalDateTime.now().plusMinutes(10), "Description", new ArrayList<Member>())));
        int size = Account.shared.getEvents().size();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Account.shared.getEvents().size(), size);
        shared.clear();
    }

    @Test
    public void numberOfEventsUnchangedAfterCurrentToPastTransition(){
        shared.clear();
        shared.withCurrentEvent(Optional.from(new Event("CurrentEvent", LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusSeconds(5), "Description", new ArrayList<Member>())));
        int size = Account.shared.getEvents().size();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(size, Account.shared.getEvents().size());
        shared.clear();
    }

    @Test
    public void updateEventsCorrectly(){
        // Test for past event
        shared.addOrUpdatePastEvent(new Event("UUID", "Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>()));
        int c1 = shared.getPastEvents().size();
        shared.addOrUpdatePastEvent(new Event("UUID", "Test2", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>()));

        assertEquals(c1, shared.getPastEvents().size());
        assertEquals(shared.getPastEvents().get(c1-1).getEventName(), "Test2");
        assertEquals(shared.getPastEvents().get(c1-1).getEventStatus(),EventStatus.PAST);

        // Test for future event
        shared.addOrUpdateFutureEvent(new Event("UUID", "Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), "", new ArrayList<Member>()));
        int c2 = shared.getPastEvents().size();
        shared.addOrUpdateFutureEvent(new Event("UUID", "Test3", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), "", new ArrayList<Member>()));

        assertEquals(c2, shared.getFutureEvents().size());
        assertEquals(shared.getFutureEvents().get(c2-1).getEventName(), "Test3");
        assertEquals(shared.getFutureEvents().get(c2-1).getEventStatus(),EventStatus.FUTURE);

        // Test for current event
        shared.withCurrentEvent(from(new Event("UUID", "Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        shared.withCurrentEvent(from(new Event("UUID", "Test4", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        assertEquals(shared.getCurrentEvent().get().getEventName(), "Test4");
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
        shared.clear();
    }

    @Test
    public void toStringShortTest(){
        shared.clear();
        shared.withCurrentEvent(Optional.from(new Event("1","inm", LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2),"Du travail, toujours du travail",
                new ArrayList<Member>())));
        String expected="Account{" +
             "givenName='" + shared.getGivenName() + '\'' +
             ", familyName='" + shared.getFamilyName() + '\'' +
             ", email='" + shared.email + '\'' +
             ", currentEvent=" + shared.getCurrentEvent().get().toString() +
             '}';
        assertEquals(shared.toStringShort(),expected);
        shared.clear();
    }
    @Test
    public void toStringTest(){
        shared.withCurrentEvent(Optional.from(new Event("1","inm", LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2),"Du travail, toujours du travail",
                new ArrayList<Member>())));
        String expected = "Account{" +
                "UUID='" + shared.getUUID() + '\'' +
                "displayName='" + shared.getDisplayName() + '\'' +
                "givenName='" + shared.getGivenName() + '\'' +
                ", familyName='" + shared.getFamilyName() + '\'' +
                ", email='" + shared.getEmail() + '\'' +
                ", currentEvent=" + shared.getCurrentEvent().get().toString() +
                ", pastEvents=" + shared.getPastEvents() +
                ", futureEvents=" + shared.getFutureEvents()+
                '}';
        assertEquals(shared.toString(),expected);
        shared.clear();
    }

    @Test
    public void clearTest(){
        shared.withFamilyName("Test").withUUID("1").withDisplayName("tested").withEmail(
                "test@test.test").withGivenName("tester").withCurrentEvent(from(
                new Event("1","inm", LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusDays(2),"Du travail, toujours du travail",
                        new ArrayList<Member>()))).
                addOrUpdateFutureEvent(new Event("UUID", "Test", new LocalDateTime().plusDays(1),
                        new LocalDateTime().plusDays(2), "", new ArrayList<Member>())).
                addOrUpdatePastEvent(new Event("UUID", "Test", new LocalDateTime().minusDays(2),
                        new LocalDateTime().minusDays(1), "", new ArrayList<Member>()));
        shared.clear();
        assertEquals(shared.getUUID(),Optional.<String>empty());
        assertEquals(shared.getFamilyName(),Optional.<String>empty());
        assertEquals(shared.getDisplayName(),Optional.<String>empty());
        assertEquals(shared.getGivenName(),Optional.<String>empty());
        assertEquals(shared.getEmail(),Optional.<String>empty());
        assertEquals(shared.getCurrentEvent(),Optional.<Event>empty());
        assertEquals(shared.getFutureEvents(),new ArrayList<Event>());
        assertEquals(shared.getPastEvents(),new ArrayList<Event>());
    }

    // Fails in Jenkins.
    // In Jenkins: ch.epfl.sweng.groupup.object.account.AccountShould >
    // futureEventsOrderedCorrectly FAILED
    // But when running in Android Studio the test passes.

    /*@Test
    public void futureEventsOrderedCorrectly(){

        // Add future events in an unordered fashion
        shared.addOrUpdateFutureEvent(new Event("Test days 1", new LocalDateTime().plusDays(34),
                new LocalDateTime().plusDays(39), "", new ArrayList<Member>()));

        shared.addOrUpdateFutureEvent(new Event("Test years 1", new LocalDateTime().plusYears(1),
                new LocalDateTime().plusYears(2), "", new ArrayList<Member>()));

        shared.addOrUpdateFutureEvent(new Event("Test minutes 1", new LocalDateTime().plusMinutes(10),
                new LocalDateTime().plusMinutes(13), "", new ArrayList<Member>()));

        // Create future events in an ordered fashion
        List<Event> correctlyOrderedFutureEvents = new ArrayList<>();

        correctlyOrderedFutureEvents.add(new Event("Test years 1", new LocalDateTime().plusYears(1),
                new LocalDateTime().plusYears(2), "", new ArrayList<Member>()));
        correctlyOrderedFutureEvents.add(new Event("Test days 1", new LocalDateTime().plusDays(34),
                new LocalDateTime().plusDays(39), "", new ArrayList<Member>()));
        correctlyOrderedFutureEvents.add(new Event("Test minutes 1", new LocalDateTime().plusMinutes(10),
                new LocalDateTime().plusMinutes(13), "", new ArrayList<Member>()));

        for(int i = 0; i < correctlyOrderedFutureEvents.size(); i++) {
            assertEquals(shared.getFutureEvents().get(i).getEventName(), correctlyOrderedFutureEvents.get(i).getEventName());
        }
        shared.clear();
    }*/
}
