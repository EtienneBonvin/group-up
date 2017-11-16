package ch.epfl.sweng.groupup.object.event;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

public class EventsShould {
    private Event event;

    private Member member = new Member("UUID", "Even monkeys can fly",
                                       "Tester", "Test","test@test.test", null);

    @Before
    public void init() {
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", new LocalDateTime(), new LocalDateTime(), "My amazing description", eventMembers, false);
    }

    @Test
    public void haveName() {
        assertEquals(event.getEventName(), "Name");
    }

    @Test
    public void setEventByName(){
        String testName = "Test Name";
        Event newEvent = event.withEventName(testName);
        assertEquals(newEvent.getEventName(),testName);

    }

    @Test
    public void setEventByDescription(){
        String testDescription = "Test Description";
        Event newEvent = event.withDescription(testDescription);
        assertEquals(newEvent.getDescription(),testDescription);

    }

    @Test
    public void printProperShortOutput(){
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        String eventName = event.getEventName();
        EventStatus eventStatus = EventStatus.CURRENT;
        String ID = event.getUUID();
        String expectedOutput = "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventStatus='" + eventStatus +
                ", eventID= " + ID +
                '}';

        event = event.withStartTime(start);
        event = event.withEndTime(end);

        assertEquals(event.toStringShort(),expectedOutput);
    }

    @Test
    public void printProperOutput(){
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        String eventName = event.getEventName();
        EventStatus eventStatus = EventStatus.CURRENT;
        String ID = event.getUUID();
        String eventMembers = event.getEventMembers().toString();
        String expectedOutput = "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventMember='" + eventMembers + '\'' +
                ", startDate='" + start + '\'' +
                ", endDate=" + end + '\'' +
                ", eventStatus=" + eventStatus + '\'' +
                ", eventID= " + ID +
                '}';
        event = event.withStartTime(start);
        event = event.withEndTime(end);
        assertEquals(event.toString(),expectedOutput);
    }

    @Test
    public void haveStartTime() {
        LocalDateTime start = LocalDateTime.now();
        assertEquals(event.withStartTime(start).getStartTime(), start);
    }

    @Test
    public void haveEndTime() {
        LocalDateTime end = LocalDateTime.now();
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers,false);
        assertEquals(event.getEventStatus(), EventStatus.CURRENT);
    }

    @Test
    public void haveStatusFuture() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers,false);
        assertEquals(event.getEventStatus(), EventStatus.FUTURE);
    }

    @Test
    public void haveStatusPast() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers,false);
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers,false);
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers,false);
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers,false);
        event.addMember(member);

    }

    @Test(expected = IllegalArgumentException.class)
    public void preventAddingMembersToPastEvents() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers,false);
        event.addMember(member);
    }

    @Test
    public void allowToRemoveCurrentUserFromMemberList(){
        Account.shared.withUUID("UUID").withGivenName("Xavier").withFamilyName("Pantet").
                withDisplayName(null).withEmail("xavier.pantet@pindex.ch");
        List<Member> eventMembers = new ArrayList<>(Arrays.asList(new Member("UUID", null,
                "Xavier", "Pantet", "xavier.pantet@pindex.ch", null), new Member("UUID2", null,
                "Cedric", "Maire", "cedmaire@gmail.com", null)));
        Event e = new Event("Name", null, null, null, eventMembers,false);
        Event withoutMe = e.withoutCurrentUser();
        assertEquals(withoutMe.getEventMembers().size(), 1);
        assertEquals(withoutMe.getEventMembers().get(0), new Member("UUID2", null, "Cedric",
                "Maire", "cedmaire@gmail.com", null));
    }

    @Test
    public void equalsEventsAreEquals(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        List<Member> members =  new ArrayList<>(Arrays.asList(new Member("1","Javier","Pavier",
                "Xantet","yolo@yolo.com", null), new Member("2","asdf","Médric","Caire",
                "yolo1@yolo.yolo", null)));
        Event e = new Event("1","inm", start, end,"Du travail, toujours du travail", members,false);
        Event f = new Event("1","inm", start, end,"Du travail, toujours du travail", members,false);
        assertEquals(e,f);
    }
    @Test
    public void differentEventsAreDifferent(){
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        List<Member> members =  new ArrayList<>(Arrays.asList(new Member("1","Javier","Pavier","Xantet","yolo@yolo.com", null), new Member("2","asdf","Médric","Caire","yolo1@yolo.yolo", null)));
        Event e = new Event("2","inm", start, end,"Du travail, toujours du travail", members,false);
        Event f = new Event("1","inm", start, end,"Du travail, toujours du travail", members,false);
        assertNotEquals(e,f);
    }
}
