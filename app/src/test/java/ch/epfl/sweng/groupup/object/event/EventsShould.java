package ch.epfl.sweng.groupup.object.event;

import android.location.Location;
import android.location.LocationManager;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

public class EventsShould {

    private Member member = new Member("UUID", "Even monkeys can fly",
            "Tester", "Test","test@test.test", null);
    private Member member2=new Member("UUID2", "Even monkeys can fly2",
            "Tester2", "Test2","test@test.test", null);
    private Event future = new Event("Test", new LocalDateTime().plusSeconds(2),
            new LocalDateTime().plusDays(2), "", Collections.singletonList(member),false);
    private Event current = new Event("Test1234", new LocalDateTime().minusDays(1),
            new LocalDateTime().plusSeconds(2), "Desc",Collections.singletonList(member) ,false);
    private Event past = new Event("Test", new LocalDateTime().minusDays(2),
            new LocalDateTime().minusDays(1), "", Collections.singletonList(member),false);

    @Before
    public void init() {
        Event e=current;
    }

    @Test
    public void haveName() {
        assertEquals(current.getEventName(), "Test1234");
    }

    @Test
    public void setEventByName(){
        String testName = "Test Name";
        Event newEvent = current.withEventName(testName);
        assertEquals(newEvent.getEventName(),testName);
    }

    @Test
    public void setEventByDescription(){
        String testDescription = "Test Description";
        Event newEvent = current.withDescription(testDescription);
        assertEquals(newEvent.getDescription(),testDescription);
    }

    @Test
    public void setEventByPointsOfInterest(){
        Set<PointOfInterest> pointsOfInterest = new HashSet<>();
        PointOfInterest defaultPointOfInterest =
                new PointOfInterest("Name", "Description", new Location(
                LocationManager.GPS_PROVIDER));

        pointsOfInterest.add(defaultPointOfInterest.withName("newName01"));
        pointsOfInterest.add(defaultPointOfInterest.withName("newName02"));
        pointsOfInterest.add(defaultPointOfInterest.withName("newName03"));

        Event newEvent = current.withPointsOfInterest(pointsOfInterest);
        newEvent = newEvent.withPointOfInterest(defaultPointOfInterest.withName("newName04"));

        pointsOfInterest.add(defaultPointOfInterest.withName("newName04"));
        assertEquals(newEvent.getPointsOfInterest(), pointsOfInterest);
    }

    @Test
    public void printProperShortOutput(){
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        String eventName = current.getEventName();
        EventStatus eventStatus = EventStatus.CURRENT;
        String ID = current.getUUID();
        String expectedOutput = "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventStatus='" + eventStatus +
                ", eventID= " + ID +
                '}';

        current = current.withStartTime(start);
        current = current.withEndTime(end);

        assertEquals(current.toStringShort(),expectedOutput);
    }

    @Test
    public void printProperOutput(){
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        String eventName = current.getEventName();
        EventStatus eventStatus = EventStatus.CURRENT;
        String ID = current.getUUID();
        String eventMembers = current.getEventMembers().toString();
        String expectedOutput = "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventMember='" + eventMembers + '\'' +
                ", startDate='" + start + '\'' +
                ", endDate=" + end + '\'' +
                ", eventStatus=" + eventStatus + '\'' +
                ", eventID= " + ID +
                ", invitation= " + ""+current.getInvitation()+
                '}';
        current = current.withStartTime(start);
        current = current.withEndTime(end);
        current = current.withInvitation(false);
        assertEquals(current.toString(),expectedOutput);
    }

    @Test
    public void haveStartTime() {
        LocalDateTime start = LocalDateTime.now();
        assertEquals(current.withStartTime(start).getStartTime(), start);
    }

    @Test
    public void haveEndTime() {
        LocalDateTime end = LocalDateTime.now();
        assertEquals(current.withEndTime(end).getEndTime(), end);
    }

    @Test
    public void haveDescription() {
        assertEquals(current.getDescription(), "Desc");
    }

    @Test
    public void haveMembers() {
        List<Member> eventMembers = new ArrayList<>();
        eventMembers.add(member);
        assertEquals(current.withEventMembers(eventMembers).getEventMembers(), eventMembers);
    }

    @Test
    public void haveStatusCurrent() {
        assertEquals(current.getEventStatus(), EventStatus.CURRENT);
    }

    @Test
    public void haveStatusFuture() {
        assertEquals(future.getEventStatus(), EventStatus.FUTURE);
    }

    @Test
    public void haveStatusPast() {
        assertEquals(past.getEventStatus(), EventStatus.PAST);
    }

    @Test
    public void haveID() {
        System.out.print(current.getUUID());
        assertNotNull(current.getUUID());
    }

    @Test
    public void addMembers() {
        // Event must be future
        future.addMember(member2);
        List<Member> updatedMember=future.addMember(member).getEventMembers();
        assertEquals(updatedMember, future.getEventMembers());
    }

    @Test
    public void notDoubleAddMembers() {
        future.addMember(member);
        assertEquals(future.addMember(member).getEventMembers().size(),1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void preventAddingMembersToCurrentEvents() {
        current.addMember(member);

    }

    @Test(expected = IllegalArgumentException.class)
    public void preventAddingMembersToPastEvents() {
        past.addMember(member);
    }

    @Test
    public void allowToRemoveCurrentUserFromMemberList(){
        Account.shared.withUUID("UUID").withGivenName("Tester").withFamilyName("Test").
                withDisplayName("Even monkeys can fly").withEmail("test@test.test");
        List<Member> eventMembers = new ArrayList<>(Arrays.asList(member, member2));
        Event e = new Event("Name", null, null, null, eventMembers,false);
        Event withoutMe = e.withoutCurrentUser();
        assertEquals(withoutMe.getEventMembers().size(), 1);
        assertEquals(withoutMe.getEventMembers().get(0), member2);
    }

    @Test
    public void equalsEventsAreEquals(){
        assertEquals(current,current);
    }
  @Test
    public void differentEventsAreDifferent(){
        assertNotEquals(past,future);
    }
    @Test
    public void getCorrectDateString(){
        LocalDateTime now= LocalDateTime.now();
        Event e= new Event("1",now,now,"", new ArrayList<>(Collections.singletonList(member)),false);
        assertEquals(e.getStartTimeToString(), String.format(Locale.getDefault(),"%02d/%02d/%d %d:%02d", now.getDayOfMonth(),
                now.getMonthOfYear(),now.getYear(), now.getHourOfDay(), now.getMinuteOfHour()));
        assertEquals(e.getEndTimeToString(),String.format(Locale.getDefault(),"%02d/%02d/%d %d:%02d", now.getDayOfMonth(),
                now.getMonthOfYear(),now.getYear(), now.getHourOfDay(), now.getMinuteOfHour()));
    }

}
