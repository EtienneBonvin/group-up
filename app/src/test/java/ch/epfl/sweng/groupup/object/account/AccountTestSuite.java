package ch.epfl.sweng.groupup.object.account;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

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
                new LocalDateTime().plusDays(1), new ArrayList<Member>())));
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
    }
    @Test(expected = IllegalArgumentException.class)
    public void withCurrentEventNotOKWithPastEvent(){
        shared.withCurrentEvent(Optional.from(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), new ArrayList<Member>())));
    }

    @Test
    public void addPastEventOK(){
        shared.addPastEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), new ArrayList<Member>()));
        assertEquals(shared.getPastEvents().get(0).getEventStatus(),EventStatus.PAST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPastEventNotOKWithCurrentEvent() {
        shared.addPastEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().plusDays(1), new ArrayList<Member>()));
    }
    @Test
    public void addFutureEventOK(){
        shared.addFutureEvent(new Event("Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), new ArrayList<Member>()));
        assertEquals(shared.getFutureEvents().get(0).getEventStatus(),EventStatus.FUTURE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureEventNotOKWithCurrentEvent() {
        shared.addFutureEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().plusDays(1), new ArrayList<Member>()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureEventNotOKWithPastEvent() {
        shared.addFutureEvent(new Event("Test", new LocalDateTime().minusDays(2),
                new LocalDateTime().minusDays(1), new ArrayList<Member>()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPastEventNotOKWithFutureEvent() {
        shared.addPastEvent(new Event("Test", new LocalDateTime().plusDays(1),
                new LocalDateTime().plusDays(2), new ArrayList<Member>()));
    }
}
