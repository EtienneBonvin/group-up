package ch.epfl.sweng.groupup;

import org.junit.Test;

import java.util.Date;

import static ch.epfl.sweng.groupup.lib.Optional.from;
import static org.junit.Assert.*;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

import static ch.epfl.sweng.groupup.object.account.Account.shared;

/**
 * Created by alix on 10/8/17.
 */

public class AccountTestSuite {

    @Test
    public void withFirstNameOK(){
        shared.withFirstName("Tester");
        assertEquals(shared.getFirstName(),"Tester");
    }

    @Test
    public void withLastNameOK(){
        shared.withLastName("Test");
        assertEquals(shared.getLastName(),"Test");
    }

    @Test
    public void withEmailOK(){
        shared.withEmail("test@testedtest.com");
        assertEquals(shared.getEmail(),"test@testedtest.com");
    }

    @Test(expected=IllegalArgumentException.class)
    public void withEmailNotOK(){
        shared.withEmail("13415.com");
    }

    @Test
    public void withCurrentEventOK(){
        shared.withCurrentEvent(Optional.from(new Event("Test", new Date(), new Date(), null, EventStatus.CURRENT)));
        assertEquals(shared.getCurrentEvent().get().getEventStatus(),EventStatus.CURRENT);
    }
    @Test(expected = IllegalArgumentException.class)
    public void withCurrentEventNotOKWithPastEvent(){
        shared.withCurrentEvent(Optional.from(new Event("Test", new Date(), new Date(), null, EventStatus.PAST)));
    }

    @Test
    public void addPastEventOK(){
        shared.addPastEvent(new Event("test", new Date(), new Date(), null, EventStatus.PAST));
        assertEquals(shared.getPastEvents().get(0).getEventStatus(),EventStatus.PAST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPastEventNotOKWithCurrentEvent() {
        shared.addPastEvent(new Event("Test", new Date(), new Date(), null, EventStatus.CURRENT));
    }
    @Test
    public void addFutureEventOK(){
        shared.addFutureEvent(new Event("test", new Date(), new Date(), null, EventStatus.FUTURE));
        assertEquals(shared.getFutureEvents().get(0).getEventStatus(),EventStatus.FUTURE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureEventNotOKWithCurrentEvent() {
        shared.addFutureEvent(new Event("Test", new Date(), new Date(), null, EventStatus.CURRENT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureEventNotOKWithPastEvent() {
        shared.addFutureEvent(new Event("Test", new Date(), new Date(), null, EventStatus.PAST));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPastEventNotOKWithFutureEvent() {
        shared.addPastEvent(new Event("Test", new Date(), new Date(), null, EventStatus.FUTURE));
    }
}
