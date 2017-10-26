package ch.epfl.sweng.groupup.object.event;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;

public final class Event {

    private final String UUID;
    private final String eventName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String description;
    private final List<Member> eventMembers;

    public Event(String eventName, LocalDateTime startTime, LocalDateTime endTime, String description, List<Member> eventMembers) {
        this.UUID = java.util.UUID.randomUUID().toString();
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.eventMembers = Collections.unmodifiableList(new ArrayList<>(eventMembers));
    }

    public Event(String uuid, String eventName, LocalDateTime startTime, LocalDateTime endTime, String
            description, List<Member> eventMembers) {
        this.UUID = uuid;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.eventMembers = Collections.unmodifiableList(new ArrayList<>(eventMembers));
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
    public List<Member> getEventMembers() {
        return Collections.unmodifiableList(new ArrayList<>(eventMembers));
    }

    /**
     * Getter for the event ID
     * @return String unique ID of event
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * Getter for the event description
     * @return String description of event
     */
    public String getDescription() { return description; }

    /**
     * Change the name of an event
     * @param eventName String containing event name
     * @return the modified event
     */
    public Event withEventName(String eventName){
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers);
    }

    /**
     * Change the start time of an event
     * @param startTime LocalDateTime containing starting time of event
     * @return the modified event
     */
    public Event withStartTime(LocalDateTime startTime){
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers);
    }

    /**
     * Change the ending time of an event
     * @param endTime LocalDateTime containing ending time of event
     * @return the modified event
     */
    public Event withEndTime(LocalDateTime endTime){
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers);
    }

    /**
     * Change the description of an event
     * @param description String containing event description
     * @return the modified event
     */
    public Event withDescription(String description) {
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers);
    }

    /**
     * Change the list of members of an event
     * @param eventMembers list of members
     * @return the modified event
     */
    public Event withEventMembers(List<Member> eventMembers){
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers);
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
     * Adds an member to the list of event members
     * @param member to add
     * @return the modified event
     */
    public Event addMember(Member member){
        if (this.getEventStatus().equals(EventStatus.FUTURE)){
            if (this.getEventMembers().contains(member)){
                return this;
            } else {
                List<Member>eventMembers = new ArrayList<>(this.getEventMembers());
                eventMembers.add(member);
                return this.withEventMembers(eventMembers);
            }
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.FUTURE.toString());
    }

    /**
     * Removes the current user from the member list of the event
     * @return the modified event
     */
    public Event withoutCurrentUser(){
        List<Member> newMemberList = new ArrayList<>();
        for(Member m: eventMembers){
            if(!m.equals(Account.shared.toMember())){
                newMemberList.add(m);
            }
        }
        return withEventMembers(newMemberList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return  eventName.equals(event.eventName) &&
                this.getEventStatus().equals(event.getEventStatus()) &&
                startTime.equals(event.startTime) &&
                endTime.equals(event.endTime) &&
                UUID.equals(event.UUID) &&
                eventMembers.equals(event.eventMembers);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventMember='" + eventMembers + '\'' +
                ", startDate='" + startTime + '\'' +
                ", endDate=" + endTime + '\'' +
                ", evenStatus=" + getEventStatus() + '\'' +
                ", eventID= " + UUID +
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
                ", eventID= " + UUID +
                '}';
    }
}
