package ch.epfl.sweng.groupup.object.event;

import static ch.epfl.sweng.groupup.lib.database.Database.EMPTY_FIELD;

import android.net.Uri;
import ch.epfl.sweng.groupup.lib.CompressedBitmap;
import ch.epfl.sweng.groupup.lib.Watchee;
import ch.epfl.sweng.groupup.lib.Watcher;
import ch.epfl.sweng.groupup.lib.database.DatabaseEvent;
import ch.epfl.sweng.groupup.lib.database.DatabasePointOfInterest;
import ch.epfl.sweng.groupup.lib.database.DatabaseUser;
import ch.epfl.sweng.groupup.lib.fileStorage.FirebaseFileProxy;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.joda.time.LocalDateTime;


@SuppressWarnings("SimplifiableIfStatement")
public final class Event implements Serializable, Watcher, Watchee {

    private final String UUID;
    private final String description;
    private final LocalDateTime endTime;
    private List<CompressedBitmap> eventImages;
    private final List<Member> eventMembers;
    private final String eventName;
    private List<Uri> eventVideos; //is it a good idea to have a File ?
    //The invitation is designed only for the user linked to the Account. This state is set to true
    //in the database on reception and not in the creation of an event
    private final boolean invitation;
    private final Set<PointOfInterest> pointsOfInterest;
    private FirebaseFileProxy proxy;
    private final LocalDateTime startTime;
    private Set<Watcher> watchers;


    public Event(String eventName, LocalDateTime startTime, LocalDateTime endTime, String
            description, List<Member> eventMembers, boolean invitation) {
        this.UUID = java.util.UUID.randomUUID()
                                  .toString();
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.eventMembers = Collections.unmodifiableList(new ArrayList<>(eventMembers));
        eventImages = new ArrayList<>();
        eventVideos = new ArrayList<>();
        watchers = new HashSet<>();
        this.invitation = invitation;
        this.pointsOfInterest = new HashSet<>();
    }


    public Event(String uuid, String eventName, LocalDateTime startTime, LocalDateTime endTime, String
            description, List<Member> eventMembers, Set<PointOfInterest> pointsOfInterest,
                 boolean invitation) {
        this.UUID = uuid;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.eventMembers = Collections.unmodifiableList(new ArrayList<>(eventMembers));
        this.invitation = invitation;
        eventImages = new ArrayList<>();
        eventVideos = new ArrayList<>();
        watchers = new HashSet<>();
        this.pointsOfInterest = Collections.unmodifiableSet(
                new HashSet<>(pointsOfInterest));
    }


    /**
     * Adds an member to the list of event members
     *
     * @param member to add
     *
     * @return the modified event
     */
    public Event addMember(Member member) {
        if (this.getEventStatus()
                .equals(EventStatus.FUTURE)) {
            if (this.getEventMembers()
                    .contains(member)) {
                return this;
            } else {
                List<Member> eventMembers = new ArrayList<>(this.getEventMembers());
                eventMembers.add(member);
                return this.withEventMembers(eventMembers);
            }
        } else {
            throw new IllegalArgumentException("Event is not " + EventStatus.FUTURE.toString());
        }
    }


    /**
     * Getter for the list of members
     *
     * @return List<Account> event member
     */
    public List<Member> getEventMembers() {
        return Collections.unmodifiableList(new ArrayList<>(eventMembers));
    }


    /**
     * Change the list of members of an event
     *
     * @param eventMembers list of members
     *
     * @return the modified event
     */
    public Event withEventMembers(List<Member> eventMembers) {
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers, pointsOfInterest, invitation);
    }


    /**
     * Computes current status of the event using start and end time
     *
     * @return EventStatus current status
     */
    public EventStatus getEventStatus() {
        LocalDateTime now = LocalDateTime.now();

        if (this.getStartTime()
                .isAfter(now)) {
            return EventStatus.FUTURE;
        } else if (this.getEndTime()
                       .isBefore(now)) {
            return EventStatus.PAST;
        } else {
            return EventStatus.CURRENT;
        }
    }


    /**
     * Getter for the end date and time
     *
     * @return LocalDateTime ending time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }


    /**
     * Getter for the starting date and time
     *
     * @return LocalDateTime starting time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }


    /**
     * Upload a picture to the event and download it on the database.
     *
     * @param uuid   the id of the uploader.
     * @param bitmap the Bitmap to upload
     */
    public void addPicture(String uuid, CompressedBitmap bitmap) {
        verifyProxyInstantiated();
        eventImages.add(bitmap);
        proxy.uploadFile(uuid, bitmap);
    }


    /**
     * Verifies that the proxy is well instantiated. If it isn't the case, reinitialize it.
     */
    private void verifyProxyInstantiated() {
        if (proxy == null) {
            initializeProxy();
        }
    }


    /**
     * Initialize the proxy for the media sharing.
     */
    private void initializeProxy() {
        proxy = new FirebaseFileProxy(this);
        proxy.addWatcher(this);
    }


    public void addVideo(String uuid, Uri uri) {
        verifyProxyInstantiated();
        eventVideos.add(uri);
        proxy.uploadFile(uuid, uri);
    }


    /**
     * Override the addWatcher method of Watchee class.
     *
     * @param newWatcher the watcher to be added.
     */
    @Override
    public void addWatcher(Watcher newWatcher) {
        verifyProxyInstantiated();
        watchers.add(newWatcher);
        proxy.addWatcher(this);
    }


    /**
     * Override the equals method.
     *
     * @param o the object to be compared with.
     *
     * @return true if the object o and this are equals, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event event = (Event) o;

        return eventName.equals(event.eventName) && this.getEventStatus()
                                                        .equals(event.getEventStatus())
               && startTime.equals(event.startTime) && endTime.equals(event.endTime) &&
               (UUID.equals(event.UUID)) && eventMembers.containsAll(event.getEventMembers()) && event
                       .getEventMembers()
                       .containsAll(eventMembers);
    }


    public String getEndTimeToString() {
        LocalDateTime date = getEndTime();
        return String.format(Locale.getDefault(), "%02d/%02d/%d %d:%02d", date.getDayOfMonth(),
                             date.getMonthOfYear(), date.getYear(), date.getHourOfDay(), date.getMinuteOfHour());
    }


    public List<Uri> getEventVideos() {
        verifyProxyInstantiated();
        return new ArrayList<>(eventVideos);
    }


    /**
     * get if this event need to display an invitation to the user
     *
     * @return boolean invitation
     */
    public boolean getInvitation() {
        return invitation;
    }


    /**
     * Returns the pictures of the event.
     * The pictures list synchronizes itself with the database when the
     * method is called.
     *
     * @return the list of Bitmap of the pictures of the event.
     */
    public List<CompressedBitmap> getPictures() {
        verifyProxyInstantiated();
        return new ArrayList<>(eventImages);
    }


    public String getStartTimeToString() {
        LocalDateTime date = getStartTime();
        return String.format(Locale.getDefault(), "%02d/%02d/%d %d:%02d", date.getDayOfMonth(),
                             date.getMonthOfYear(), date.getYear(), date.getHourOfDay(), date.getMinuteOfHour());
    }


    /**
     * Return an hash comparing only what needed to differentiate two invitation.
     *
     * @return
     */
    public int hashCode() {
        int result = UUID.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        return result;
    }


    /**
     * Override the notifyWatcher method of the Watcher class.
     */
    @Override
    public void notifyWatcher() {
        verifyProxyInstantiated();
        List<CompressedBitmap> proxyImages = proxy.getImagesFromDatabase();
        List<Uri> proxyVideos = new ArrayList<>();
        for (File f : proxy.getVideosFromDatabase()) {
            proxyVideos.add(Uri.fromFile(f));
        }
        if (proxyImages.size() > eventImages.size()) {
            eventImages = proxyImages;
        }
        if (proxyVideos.size() > eventVideos.size()) {
            eventVideos = proxyVideos;
        }
        notifyAllWatchers();
    }


    /**
     * Override notifyAllWatchers method of Watchee class.
     */
    @Override
    public void notifyAllWatchers() {
        for (Watcher w : watchers) {
            w.notifyWatcher();
        }
    }


    /**
     * Remove of the file storage all the files of a particular member.
     */
    public void removeFiles() {
        verifyProxyInstantiated();
        proxy.removeFiles();
    }


    /**
     * Override the removeWatcher method of Watchee class.
     *
     * @param watcher the watcher to unregister.
     */
    @Override
    public void removeWatcher(Watcher watcher) {
        if (watchers.contains(watcher)) {
            watchers.remove(watcher);
        }
        if (watchers.isEmpty()) {
            verifyProxyInstantiated();
            proxy.removeWatcher(this);
            proxy.kill();
            proxy = null;
        }
    }


    /**
     * Converts a regular event into its database representation.
     *
     * @return - the database event that can be stored easily
     */
    public DatabaseEvent toDatabaseEvent() {
        HashMap<String, DatabaseUser> uuidToUserMap = new HashMap<>();
        for (Member memberToStore : getEventMembers()) {
            if (!memberToStore.getUUID()
                              .isEmpty()) {
                DatabaseUser databaseUser = memberToStore.toDatabaseUser();

                if (memberToStore.getUUID()
                                 .get()
                                 .equals(Account.shared.getUUID()
                                                       .getOrElse(EMPTY_FIELD))) {
                    databaseUser = Account.shared.toMember()
                                                 .toDatabaseUser();
                }

                if (!isCurrent()) {
                    databaseUser.clearLocation();
                }

                uuidToUserMap.put(databaseUser.getUuid(), databaseUser);
            }
        }

        HashMap<String, DatabasePointOfInterest> uuidToPoIMap = new HashMap<>();
        for (PointOfInterest poiToStore : getPointsOfInterest()) {
            uuidToPoIMap.put(poiToStore.getUuid(), poiToStore.toDatabasePointOfInterest());
        }

        return new DatabaseEvent(getEventName(),
                                 getDescription(),
                                 getStartTime().toString(),
                                 getEndTime().toString(),
                                 getUUID(),
                                 uuidToUserMap,
                                 uuidToPoIMap);
    }


    /**
     * Getter for the event name
     *
     * @return String event name
     */
    public String getEventName() {
        return eventName;
    }


    /**
     * Getter for the set of points of interest.
     *
     * @return the set of points of interest
     */
    public Set<PointOfInterest> getPointsOfInterest() {
        return Collections.unmodifiableSet(new HashSet<>(pointsOfInterest));
    }


    /**
     * Getter for the event ID
     *
     * @return String unique ID of event
     */
    public String getUUID() {
        return UUID;
    }


    /**
     * Getter for the event description
     *
     * @return String description of event
     */
    public String getDescription() { return description; }


    /**
     * Returns true if and only if the status of the event is current.
     *
     * @return -  true if the status of the event is current
     */
    public boolean isCurrent() {
        return getEventStatus() == EventStatus.CURRENT;
    }


    /**
     * Override the toString method.
     *
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
               ", invitation= " + invitation +
               '}';
    }


    /**
     * Shorter version of toString()
     *
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
     * Change the description of an event
     *
     * @param description String containing event description
     *
     * @return the modified event
     */
    public Event withDescription(String description) {
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers, pointsOfInterest, invitation);
    }


    /**
     * Change the ending time of an event
     *
     * @param endTime LocalDateTime containing ending time of event
     *
     * @return the modified event
     */
    public Event withEndTime(LocalDateTime endTime) {
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers, pointsOfInterest, invitation);
    }


    /**
     * Change the name of an event
     *
     * @param eventName String containing event name
     *
     * @return the modified event
     */
    public Event withEventName(String eventName) {
        return new Event(UUID, eventName, startTime, endTime, description,
                         eventMembers, pointsOfInterest, invitation);
    }


    /**
     * Change the status of the invitation
     * * @param invitation
     *
     * @return
     */
    public Event withInvitation(boolean invitation) {
        return new Event(UUID, eventName, startTime, endTime, description,
                         eventMembers, pointsOfInterest, invitation);
    }


    /**
     * Adds the point of interest to an event
     *
     * @param pointOfInterest the point of interest to add
     *
     * @return the modified event
     */
    public Event withPointOfInterest(PointOfInterest pointOfInterest) {
        Set<PointOfInterest> newPointsOfInterest = new HashSet<>();

        newPointsOfInterest.addAll(pointsOfInterest);
        newPointsOfInterest.add(pointOfInterest);

        return withPointsOfInterest(newPointsOfInterest);
    }


    /**
     * Change the set of points of interest of an event
     *
     * @param pointsOfInterest set of points of interest
     *
     * @return the modified event
     */
    public Event withPointsOfInterest(Set<PointOfInterest> pointsOfInterest) {
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers, pointsOfInterest, invitation);
    }


    /**
     * Change the start time of an event
     *
     * @param startTime LocalDateTime containing starting time of event
     *
     * @return the modified event
     */
    public Event withStartTime(LocalDateTime startTime) {
        return new Event(UUID, eventName, startTime, endTime, description, eventMembers, pointsOfInterest, invitation);
    }


    /**
     * Removes the current user from the member list of the event
     *
     * @return the modified event
     */
    public Event withoutCurrentUser() {
        List<Member> newMemberList = new ArrayList<>();
        for (Member m : eventMembers) {
            if (!m.equals(Account.shared.toMember())) {
                newMemberList.add(m);
            }
        }
        return withEventMembers(newMemberList);
    }
}
