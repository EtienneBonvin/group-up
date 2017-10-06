package ch.epfl.sweng.groupup.object.event;


import java.util.Date;
import java.util.List;

import ch.epfl.sweng.groupup.object.account.Account;

public class Event {

    private String eventName;
    private Date startTime;
    private Date endTime;
    private List<Account> eventMembers;
    private EventStatus eventStatus;

    public Event(String eventName, Date startTime, Date endTime,
                 List<Account> eventMembers, EventStatus eventStatus) {
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventMembers = eventMembers;
        this.eventStatus = eventStatus;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Account> getEventMembers() {
        return eventMembers;
    }

    public void setEventMembers(List<Account> eventMembers) {
        this.eventMembers = eventMembers;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        if (this.eventStatus.equals(EventStatus.FUTURE) && eventStatus.equals(EventStatus.CURRENT) ||
                this.eventStatus.equals(EventStatus.CURRENT) && eventStatus.equals(EventStatus.PAST)) {
            this.eventStatus = eventStatus;
        } else {
            throw new IllegalArgumentException("Status transitions must be consistant with time");
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!eventName.equals(event.eventName)) {return false;}
        if (!startTime.equals(event.startTime)) {return false;}
        if (!endTime.equals(event.endTime)) {return false;}
        if (!eventMembers.equals(event.eventMembers)) {return false;}
        return eventStatus == event.eventStatus;

    }

}
