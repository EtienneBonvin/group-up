package ch.epfl.sweng.groupup;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import org.joda.time.LocalDateTime;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

import static junit.framework.Assert.*;

public class EventsShould {
    private Event event;
    private int eventID = 1234;
    private Member member = new Member("UUID", "Even monkeys can fly", "Tester", "Test","test@test.test");
    @Before
    public void init() {
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", new LocalDateTime(), new LocalDateTime(), "My amazing description", eventMembers);
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
    public void haveDescription() {
        assertEquals(event.getDescription(), "My amazing description");
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers);
        assertEquals(event.getEventStatus(), EventStatus.CURRENT);
    }

    @Test
    public void haveStatusFuture() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers);
        assertEquals(event.getEventStatus(), EventStatus.FUTURE);
    }

    @Test
    public void haveStatusPast() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers);
        assertEquals(event.getEventStatus(), EventStatus.PAST);
    }

    @Test
    public void haveID() {
        System.out.print(event.getUUID());
        assertNotNull(event.getUUID());
    }


    @Test
    public void addMembers() {
        // Event must be future
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers);
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers);
        eventMembers.add(member);

        List<Member> updatedMember;
        updatedMember = event.addMember(member).addMember(member).getEventMembers();

        assertEquals(updatedMember, eventMembers);
    }

   /* //Test that print an event to the console so that we can visually see if an event is correctly
   //printed
   @Test
    public void testToString(){
        Member test = new Member("Test", "Tested", "Tester", "test@test.test");
        Member test1 = new Member("Test1", "Tested1", "Tester1", "test1@test.test");
        List<Member> members= new ArrayList<>();
        members.add(test);
        members.add(test1);
        Event e =new Event("Name", LocalDateTime.now(),LocalDateTime.now().plusDays(1),members,1);
        System.out.println(e.toString());
    }*/

    @Test(expected = IllegalArgumentException.class)
    public void preventAddingMembersToCurrentEvents() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers);
        event.addMember(member);

    }

    @Test(expected = IllegalArgumentException.class)
    public void preventAddingMembersToPastEvents() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers);
        event.addMember(member);
    }

    @Test
    public void allowToRemoveCurrentUserFromMemberList(){
        Account.shared.withUUID("UUID").withGivenName("Xavier").withFamilyName("Pantet").withDisplayName(null).withEmail("xavier.pantet@pindex.ch");
        List<Member> eventMembers = new ArrayList<Member>(Arrays.asList(new Member("UUID", null, "Xavier", "Pantet", "xavier.pantet@pindex.ch"), new Member("UUID2", null, "Cedric", "Maire", "cedmaire@gmail.com")));
        Event e = new Event("Name", null, null, null, eventMembers);
        Event withoutMe = e.withoutCurrentUser();
        assertEquals(withoutMe.getEventMembers().size(), 1);
        assertEquals(withoutMe.getEventMembers().get(0), new Member("UUID2", null, "Cedric", "Maire", "cedmaire@gmail.com"));
    }

}
