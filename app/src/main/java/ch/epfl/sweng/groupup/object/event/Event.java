package ch.epfl.sweng.groupup.object.event;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.groupup.object.account.Account;

public final class Event {

    private final String eventName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<Account> eventMembers;
    private final int eventID;

    public Event(String eventName, LocalDateTime startTime, LocalDateTime endTime, List<Account> eventMembers, int eventID) {
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;

        this.eventMembers = Collections.unmodifiableList(new ArrayList<>(eventMembers));
        this.eventID = eventID;
    }

    /**
     * Getter for the event name
     * @return String event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Getter for the starting date and time
     * @return LocalDateTime starting time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Getter for the end date and time
     * @return LocalDateTime ending time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Getter for the list of members
     * @return List<Account> event member
     */
    public List<Account> getEventMembers() {
        return Collections.unmodifiableList(new ArrayList<Account>(eventMembers));
    }

    /**
     * Getter for the event ID
     * @return int unique ID of event
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Change the name of an event
     * @param eventName
     * @return the modified event
     */
    public Event withEventName(String eventName){
        return new Event(eventName, startTime, endTime, eventMembers, eventID);
    }

    /**
     * Change the start time of an event
     * @param startTime
     * @return the modified event
     */
    public Event withStartTime(LocalDateTime startTime){
        return new Event(eventName, startTime, endTime, eventMembers, eventID);
    }

    /**
     * Change the ending time of an event
     * @param endTime
     * @return the modified event
     */
    public Event withEndTime(LocalDateTime endTime){
        return new Event(eventName, startTime, endTime, eventMembers, eventID);
    }

    /**
     * Change the list of members of an event
     * @param eventMembers
     * @return the modified event
     */
    public Event withEventMembers(List<Account> eventMembers){
        return new Event(eventName, startTime, endTime, eventMembers, eventID);
    }

    /**
     * Computes current status of the event using start and end time
     * @return EventStatus current status
     */
    public EventStatus getEventStatus() {
        LocalDateTime now = LocalDateTime.now();

        if (this.getStartTime().isAfter(now)) return EventStatus.FUTURE;
        else if (this.getEndTime().isBefore(now)) return EventStatus.PAST;
        else return EventStatus.CURRENT;
    }

    /**
     * Adds an account to the list of event members
     * @param account to add
     * @return the modified event
     */
    public Event addMember(Account account){
        if (this.getEventStatus().equals(EventStatus.FUTURE)){
            if (this.getEventMembers().contains(account)){
                return this;
            } else {
                List<Account>eventMembers = new ArrayList<>(this.getEventMembers());
                eventMembers.add(account);
                return this.withEventMembers(eventMembers);
            }
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.FUTURE.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!eventName.equals(event.eventName)) return false;
        if (!this.getEventStatus().equals(event.getEventStatus())) return false;
        if (!startTime.equals(event.startTime)) return false;
        if (!endTime.equals(event.endTime)) return false;
        if (!(eventID==event.eventID)) return false;
        return eventMembers.equals(event.eventMembers);
    }

    @Override
    public String toString() {
        return "Event{" +
                "evenName='" + eventName + '\'' +
                ", eventMember='" + eventMembers + '\'' +
                ", startDate='" + startTime + '\'' +
                ", endDate=" + endTime + '\'' +
                ", evenStatus=" + getEventStatus() + '\'' +
                ", eventID= " + eventID +
                '}';
    }

    /**
     *Shorter version of toString()
     * @return string containing only event name and status
     */
    public String toStringShort() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventStatus='" + getEventStatus() +
                ", eventID= " + eventID +
                '}';
    }
}