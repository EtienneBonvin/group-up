package ch.epfl.sweng.groupup;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

import static junit.framework.Assert.*;

public class EventsShould {
    private Event event;
    private int eventID = 1234;
    private Member member = new Member("Even monkeys can fly", "Tester", "Test","test@test.test");
    @Before
    public void init() {
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", new LocalDateTime(), new LocalDateTime(), eventMembers, eventID);
    }

    @Test
    public void haveName() {
        assertEquals(event.getEventName(), "Name");
    }

    @Test
    public void haveStartTime() {
        LocalDateTime start = new LocalDateTime().now();
        assertEquals(event.withStartTime(start).getStartTime(), start);
    }

    @Test
    public void haveEndTime() {
        LocalDateTime end = new LocalDateTime().now();
        assertEquals(event.withEndTime(end).getEndTime(), end);
    }

    @Test
    public void haveMembers() {
        List<Member> eventMembers = new ArrayList<>();
        eventMembers.add(member);
        assertEquals(event.withEventMembers(eventMembers).getEventMembers(), eventMembers);
    }

    @Test
    public void haveStatusCurrent() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, eventMembers, eventID);
        assertEquals(event.getEventStatus(), EventStatus.CURRENT);
    }

    @Test
    public void haveStatusFuture() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, eventMembers, eventID);
        assertEquals(event.getEventStatus(), EventStatus.FUTURE);
    }

    @Test
    public void haveStatusPast() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, eventMembers, eventID);
        assertEquals(event.getEventStatus(), EventStatus.PAST);
    }

    @Test
    public void haveID() {
        int ID = eventID;
        assertEquals(event.getEventID(), ID);
    }


    @Test
    public void addMembers() {
        // Event must be future
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, eventMembers, eventID);
        eventMembers.add(member);

        List<Member> updatedMember;
        updatedMember = event.addMember(member).getEventMembers();

        assertEquals(updatedMember, eventMembers);
    }

    @Test
    public void notDoubleAddMembers() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, eventMembers, eventID);
        eventMembers.add(member);

        List<Member> updatedMember;
        updatedMember = event.addMember(member).addMember(member).getEventMembers();

        assertEquals(updatedMember, eventMembers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void preventAddingMembersToCurrentEvents() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, eventMembers, eventID);
        event.addMember(member);

    }

    @Test(expected = IllegalArgumentException.class)
    public void preventAddingMembersToPastEvents() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, eventMembers, eventID);
        event.addMember(member);
    }

}