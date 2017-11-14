package ch.epfl.sweng.groupup.object.event;

import android.graphics.Bitmap;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.groupup.lib.Watchee;
import ch.epfl.sweng.groupup.lib.Watcher;
import ch.epfl.sweng.groupup.lib.fileStorage.FirebaseFileProxy;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;

@SuppressWarnings("SimplifiableIfStatement")
public final class Event implements Serializable, Watcher, Watchee{

    private final String UUID;
    private final String eventName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String description;
    private final List<Member> eventMembers;
    private List<Bitmap> eventImages;
    private FirebaseFileProxy proxy;
    private Set<Watcher> watchers;

    public Event(String eventName, LocalDateTime startTime, LocalDateTime endTime, String description, List<Member> eventMembers) {
        this.UUID = java.util.UUID.randomUUID().toString();
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.eventMembers = Collections.unmodifiableList(new ArrayList<>(eventMembers));
        eventImages = new ArrayList<>();
        watchers = new HashSet<>();
    }

    public Event(String uuid, String eventName, LocalDateTime startTime, LocalDateTime endTime, String
            description, List<Member> eventMembers) {
        this.UUID = uuid;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.eventMembers = Collections.unmodifiableList(new ArrayList<>(eventMembers));
        eventImages = new ArrayList<>();
        watchers = new HashSet<>();
    }

    /**
     * Initialize the proxy for the media sharing.
     */
    private void initializeProxy(){
        proxy = new FirebaseFileProxy(this);
        proxy.addWatcher(this);
    }

    /**
     * Returns the pictures of the event.
     * The pictures list synchronizes itself with the database when the
     * method is called.
     * @return the list of Bitmap of the pictures of the event.
     */
    public List<Bitmap> getPictures(){
        verifyProxyInstantiated();
        return new ArrayList<>(eventImages);
    }

    /**
     * Upload a picture to the event and download it on the database.
     * @param uuid the id of the uploader.
     * @param bitmap the Bitmap to upload
     */
    public void addPicture(String uuid, Bitmap bitmap){
        verifyProxyInstantiated();
        proxy.uploadFile(uuid, bitmap);
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

    /**
     * Override the equals method.
     * @param o the object to be compared with.
     * @return true if the object o and this are equals, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!eventName.equals(event.eventName)) return false;
        if (!this.getEventStatus().equals(event.getEventStatus())) return false;
        if (!startTime.equals(event.startTime)) return false;
        if (!endTime.equals(event.endTime)) return false;
        if (!(UUID.equals(event.UUID))) return false;
        return eventMembers.equals(event.eventMembers);
    }

    /**
     * Override the toString method.
     * @return a String representing the object.
     */
    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventMember='" + eventMembers + '\'' +
                ", startDate='" + startTime + '\'' +
                ", endDate=" + endTime + '\'' +
                ", eventStatus=" + getEventStatus() + '\'' +
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

    /**
     * Override notifyAllWatchers method of Watchee class.
     */
    @Override
    public void notifyAllWatchers() {
        for(Watcher w : watchers){
            w.notifyWatcher();
        }
    }

    /**
     * Verifies that the proxy is well instantiated. If it isn't the case, reinitialize it.
     */
    private void verifyProxyInstantiated(){
        if(proxy == null){
            initializeProxy();
        }
    }

    /**
     * Override the addWatcher method of Watchee class.
     * @param newWatcher the watcher to be added.
     */
    @Override
    public void addWatcher(Watcher newWatcher) {
        watchers.add(newWatcher);
        verifyProxyInstantiated();
        proxy.addWatcher(this);
    }

    /**
     * Override the removeWatcher method of Watchee class.
     * @param watcher the watcher to unregister.
     */
    @Override
    public void removeWatcher(Watcher watcher) {
        if(watchers.contains(watcher))
            watchers.remove(watcher);
        if(watchers.isEmpty()){
            verifyProxyInstantiated();
            proxy.removeWatcher(this);
        }
    }

    /**
     * Override the notifyWatcher method of the Watcher class.
     */
    @Override
    public void notifyWatcher() {
        verifyProxyInstantiated();
        eventImages = proxy.getFromDatabase();
        notifyAllWatchers();
    }
}
