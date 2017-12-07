package ch.epfl.sweng.groupup.object.event;

import android.location.Location;
import android.location.LocationManager;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ch.epfl.sweng.groupup.lib.database.DatabaseEvent;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;


public class EventsShould {

    private Event event;
    private Member member = new Member("UUID", "Even monkeys can fly", "Tester", "Test", "test@test.test", null);


    @Before
    public void init() {
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name",
                          new LocalDateTime(),
                          new LocalDateTime(),
                          "My amazing description",
                          eventMembers,
                          false);
    }


    @Test
    public void haveName() {
        assertEquals(event.getEventName(), "Name");
    }


    @Test
    public void setEventByName() {
        String testName = "Test Name";
        Event newEvent = event.withEventName(testName);
        assertEquals(newEvent.getEventName(), testName);
    }


    @Test
    public void setEventByDescription() {
        String testDescription = "Test Description";
        Event newEvent = event.withDescription(testDescription);
        assertEquals(newEvent.getDescription(), testDescription);
    }


    @Test
    public void setEventByPointsOfInterest() {
        Set<PointOfInterest> pointsOfInterest = new HashSet<>();
        PointOfInterest defaultPointOfInterest = new PointOfInterest("Name",
                                                                     "Description",
                                                                     new Location(LocationManager.GPS_PROVIDER));

        pointsOfInterest.add(defaultPointOfInterest.withName("newName01"));
        pointsOfInterest.add(defaultPointOfInterest.withName("newName02"));
        pointsOfInterest.add(defaultPointOfInterest.withName("newName03"));

        Event newEvent = event.withPointsOfInterest(pointsOfInterest);
        newEvent = newEvent.withPointOfInterest(defaultPointOfInterest.withName("newName04"));

        pointsOfInterest.add(defaultPointOfInterest.withName("newName04"));
        assertEquals(newEvent.getPointsOfInterest(), pointsOfInterest);
    }


    @Test
    public void printProperShortOutput() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        String eventName = event.getEventName();
        EventStatus eventStatus = EventStatus.CURRENT;
        String ID = event.getUUID();
        String expectedOutput = "Event{" +
                                "eventName='" +
                                eventName +
                                '\'' +
                                ", eventStatus='" +
                                eventStatus +
                                ", eventID= " +
                                ID +
                                '}';

        event = event.withStartTime(start);
        event = event.withEndTime(end);

        assertEquals(event.toStringShort(), expectedOutput);
    }


    @Test
    public void printProperOutput() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        String eventName = event.getEventName();
        EventStatus eventStatus = EventStatus.CURRENT;
        String ID = event.getUUID();
        String eventMembers = event.getEventMembers().toString();
        String expectedOutput = "Event{" +
                                "eventName='" +
                                eventName +
                                '\'' +
                                ", eventMember='" +
                                eventMembers +
                                '\'' +
                                ", startDate='" +
                                start +
                                '\'' +
                                ", endDate=" +
                                end +
                                '\'' +
                                ", eventStatus=" +
                                eventStatus +
                                '\'' +
                                ", eventID= " +
                                ID +
                                ", invitation= " +
                                "" +
                                event.getInvitation() +
                                '}';
        event = event.withStartTime(start);
        event = event.withEndTime(end);
        event = event.withInvitation(false);
        assertEquals(event.toString(), expectedOutput);
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers, false);
        assertEquals(event.getEventStatus(), EventStatus.CURRENT);
    }


    @Test
    public void haveStatusFuture() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers, false);
        assertEquals(event.getEventStatus(), EventStatus.FUTURE);
    }


    @Test
    public void haveStatusPast() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers, false);
        assertEquals(event.getEventStatus(), EventStatus.PAST);
    }


    @Test
    public void beCurrentOnlyIfTheyReallyAreCurrent() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now();
        List<Member> eventMembers = new ArrayList<>();

        Event futureEvent = new Event("Name",
                                      startDate.plusDays(2),
                                      endDate.plusDays(3),
                                      "Description",
                                      eventMembers,
                                      false);
        Event currentEvent = new Event("Name",
                                       startDate.minusDays(1),
                                       endDate.plusDays(1),
                                       "Description",
                                       eventMembers,
                                       false);
        Event pastEvent = new Event("Name",
                                    startDate.minusDays(3),
                                    endDate.minusDays(1),
                                    "Description",
                                    eventMembers,
                                    false);

        assertFalse(futureEvent.isCurrent());
        assertTrue(currentEvent.isCurrent());
        assertFalse(pastEvent.isCurrent());
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers, false);
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers, false);
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
        event = new Event("Name", startDate, endDate, "Description", eventMembers, false);
        event.addMember(member);
    }


    @Test(expected = IllegalArgumentException.class)
    public void preventAddingMembersToPastEvents() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers, false);
        event.addMember(member);
    }


    @Test
    public void allowToRemoveCurrentUserFromMemberList() {
        Account.shared.withUUID("UUID")
                      .withGivenName("Xavier")
                      .withFamilyName("Pantet")
                      .
                              withDisplayName(null)
                      .withEmail("xavier.pantet@pindex.ch");
        List<Member> eventMembers = new ArrayList<>(Arrays.asList(new Member("UUID",
                                                                             null,
                                                                             "Xavier",
                                                                             "Pantet",
                                                                             "xavier.pantet@pindex.ch",
                                                                             null),
                                                                  new Member("UUID2",
                                                                             null,
                                                                             "Cedric",
                                                                             "Maire",
                                                                             "cedmaire@gmail.com",
                                                                             null)));
        Event e = new Event("Name", null, null, null, eventMembers, false);
        Event withoutMe = e.withoutCurrentUser();
        assertEquals(withoutMe.getEventMembers().size(), 1);
        assertEquals(withoutMe.getEventMembers().get(0),
                     new Member("UUID2", null, "Cedric", "Maire", "cedmaire@gmail.com", null));
    }


    @Test
    public void equalsEventsAreEquals() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        List<Member> members = new ArrayList<>(Arrays.asList(new Member("1",
                                                                        "Javier",
                                                                        "Pavier",
                                                                        "Xantet",
                                                                        "yolo@yolo.com",
                                                                        null),
                                                             new Member("2",
                                                                        "asdf",
                                                                        "Médric",
                                                                        "Caire",
                                                                        "yolo1@yolo.yolo",
                                                                        null)));
        Event e = new Event("1",
                            "inm",
                            start,
                            end,
                            "Du travail, toujours du " + "travail",
                            members,
                            new HashSet<PointOfInterest>(),
                            false);
        Event f = new Event("1",
                            "inm",
                            start,
                            end,
                            "Du travail, toujours du travail",
                            members,
                            new HashSet<PointOfInterest>(),
                            false);
        assertEquals(e, f);
    }


    @Test
    public void getCorrectDateString() {
        LocalDateTime now = LocalDateTime.now();
        Event e = new Event("1", now, now, "", new ArrayList<>(Collections.singletonList(member)), false);
        assertEquals(e.getStartTimeToString(),
                     (String.format(Locale.getDefault(),
                                    "%d/%d/%d",
                                    now.getDayOfMonth(),
                                    now.getMonthOfYear(),
                                    now.getYear())));
        assertEquals(e.getEndTimeToString(),
                     (String.format(Locale.getDefault(),
                                    "%d/%d/%d",
                                    now.getDayOfMonth(),
                                    now.getMonthOfYear(),
                                    now.getYear())));
    }


    @Test
    public void correctlyConvertToDatabaseEvent() {
        Account.shared.withUUID("AccountUUID");
        Member randomMember = new Member("UUID", "DispName", "GiveName", "FamName", "Email", null);
        PointOfInterest poi = new PointOfInterest("UUDID", "Name", "Desc", new Location(LocationManager.GPS_PROVIDER));

        LocalDateTime now = LocalDateTime.now();
        List<Member> members = new ArrayList<>();
        members.add(Account.shared.toMember());
        members.add(randomMember);

        Event event = new Event("My Event", now.minusDays(1), now.plusDays(2), "My Description", members, false);
        event = event.withPointOfInterest(poi);
        DatabaseEvent databaseEvent = event.toDatabaseEvent();

        assertEquals(event.getEventName(), databaseEvent.getName());
        assertEquals(event.getDescription(), databaseEvent.getDescription());
        assertEquals(event.getStartTime().toString(), databaseEvent.getDatetimeStart());
        assertEquals(event.getEndTime().toString(), databaseEvent.getDatetimeEnd());
        assertEquals(event.getUUID(), databaseEvent.getUuid());

        assertEquals(databaseEvent.getMembers().keySet().size(), 2);
        assertEquals(databaseEvent.getMembers().get(Account.shared.getUUID().get()),
                     Account.shared.toMember().toDatabaseUser());
        assertEquals(databaseEvent.getMembers().get(randomMember.getUUID().get()), randomMember.toDatabaseUser());

        assertEquals(databaseEvent.getPointsOfInterest().keySet().size(), 1);
        assertEquals(databaseEvent.getPointsOfInterest().get(poi.getUuid()), poi.toDatabasePointOfInterest());

        Account.shared.clear();
    }


    @Test
    public void EventsWithDifferentStatusAreDifferent() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        Event e = event.withStartTime(start).withEndTime(end); // future event
        Event f = event.withStartTime(start.minusDays(4)).withEndTime(end.minusDays(3)); // past event

        assertNotEquals(e, f);
    }


    @Test
    public void EventsWithDifferentNameAreDifferent() {
        Event e = event.withEventName("Event One");
        Event f = event.withEventName("Event Two");

        assertNotEquals(e, f);
    }


    @Test
    public void EventsWithDifferentStartAreDifferent() {
        LocalDateTime start = LocalDateTime.now();

        // Important to keep the event status the same
        Event e = event.withStartTime(start.plusDays(1));
        Event f = event.withStartTime(start.plusDays(2));

        assertNotEquals(e, f);
    }


    @Test
    public void EventsWithDifferentEndAreDifferent() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        // Important to keep the event status the same
        Event e = event.withStartTime(start).withEndTime(end.plusDays(1));
        Event f = event.withStartTime(start).withEndTime(end.plusDays(2));

        assertNotEquals(e, f);
    }


    @Test
    public void EventsWithDifferentMembersAreDifferent() {
        Member member1 = new Member("UUID1", "Even monkeys can fly", "Tester", "Test", "test@test.test", null);
        Member member2 = new Member("UUID2", "Even monkeys can fly", "Tester", "Test", "test@test.test", null);

        List<Member> eventMembers = new ArrayList<>();
        eventMembers.add(member1);
        Event e = event.withEventMembers(eventMembers);
        eventMembers.add(member2);
        Event f = event.withEventMembers(eventMembers);

        assertNotEquals(e, f);
    }
}
