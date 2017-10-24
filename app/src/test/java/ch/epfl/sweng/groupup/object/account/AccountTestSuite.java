package ch.epfl.sweng.groupup.object.account;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ch.epfl.sweng.groupup.lib.Optional.from;
import static org.junit.Assert.*;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

import static ch.epfl.sweng.groupup.object.account.Account.shared;

/**
 * Created by alix on 10/8/17.
 */

public class AccountTestSuite {

    private Member member = new Member("UUID", "Even monkeys can fly", "Tester", "Test","test@test.test");
    @Test
    public void withUUIDOK() {
        shared.withUUID("UUID2");
        assertEquals(shared.getUUID().get(), "UUID2");
    }

    @Test
    public void withDisplayNameOK(){
        shared.withDisplayName("Tester");
        assertEquals(shared.getDisplayName().get(),"Tester");
    }

    @Test
    public void withFirstNameOK(){
        shared.withGivenName("Tester");
        assertEquals(shared.getGivenName().get(),"Tester");
    }

    @Test
    public void withLastNameOK(){
        shared.withFamilyName("Test");
        assertEquals(shared.getFamilyName().get(),"Test");
    }

    @Test
    public void withEmailOK(){
        shared.withEmail("test@testedtest.com");
        assertEquals(shared.getEmail().get(),"test@testedtest.com");
    }

    @Test(expected=IllegalArgumentException.class)
    public void withEmailNotOK(){
        shared.withEmail("13415.com");
    }

    @Test
    public void withCurrentEventOK(){
        shared.withCurrentEvent(Optional.from(new Event("Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
    }
    @Test(expected = IllegalArgumentException.class)
    public void withCurrentEventNotOKWithPastEvent(){
        shared.withCurrentEvent(Optional.from(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>())));
    }

    @Test
    public void addPastEventOK(){
        shared.addOrUpdatePastEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>()));
        assertEquals(shared.getPastEvents().get(0).getEventStatus(),EventStatus.PAST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPastEventNotOKWithCurrentEvent() {
        shared.addOrUpdatePastEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>()));
    }
    @Test
    public void addFutureEventOK(){
        shared.addOrUpdateFutureEvent(new Event("Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), "", new ArrayList<Member>()));
        assertEquals(shared.getFutureEvents().get(0).getEventStatus(),EventStatus.FUTURE);
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
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPastEventNotOKWithFutureEvent() {
        shared.addOrUpdatePastEvent(new Event("Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), "", new ArrayList<Member>()));
    }

    @Test
    public void addEventsCorrectly(){
        // Test for past event
        shared.addOrUpdatePastEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), "", new ArrayList<Member>()));
        assertEquals(shared.getPastEvents().get(1).getEventStatus(),EventStatus.PAST);

        // Test for future event
        shared.addOrUpdateFutureEvent(new Event("Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), "", new ArrayList<Member>()));
        assertEquals(shared.getFutureEvents().get(1).getEventStatus(),EventStatus.FUTURE);

        // Test for current event
        shared.withCurrentEvent(Optional.from(new Event("Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
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
        shared.withCurrentEvent(Optional.from(new Event("UUID", "Test", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        shared.withCurrentEvent(Optional.from(new Event("UUID", "Test4", new LocalDateTime().minusDays(1),
                new LocalDateTime().plusDays(1), "", new ArrayList<Member>())));
        assertEquals(shared.getCurrentEvent().get().getEventName(), "Test4");
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
    }

    @Test
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
    }
}
