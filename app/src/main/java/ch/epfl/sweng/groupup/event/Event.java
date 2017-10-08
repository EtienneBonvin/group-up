package ch.epfl.sweng.groupup.event;


import java.util.Date;
import java.util.List;

import ch.epfl.sweng.groupup.object.account.Account;

public class Event {

    private String eventName;
    private Date startTime;
    private Date endTime;
    private List<Account> eventMembers;
    private EventStatus eventStatus;

    public Event(String eventName, Date startTime, Date endTime, List<Account> eventMembers, EventStatus eventStatus) {
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
}
