package ch.epfl.sweng.groupup.lib.database;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;


@IgnoreExtraProperties
public final class DatabaseEvent {

    /**
     * Class to represent the event object that will be stored in the database.
     */
    public String name = Database.EMPTY_FIELD;
    public String description = Database.EMPTY_FIELD;
    public String datetimeStart = Database.EMPTY_FIELD;
    public String datetimeEnd = Database.EMPTY_FIELD;
    public String uuid = Database.EMPTY_FIELD;
    public Map<String, DatabaseUser> members = new HashMap<>();
    public Map<String, DatabasePointOfInterest> pointsOfInterest = new HashMap<>();


    public DatabaseEvent() {
    }


    public DatabaseEvent(String name,
                         String description,
                         String datetimeStart,
                         String datetimeEnd,
                         String uuid,
                         HashMap<String, DatabaseUser> members,
                         HashMap<String, DatabasePointOfInterest> pointsOfInterest) {
        this.name = name;
        this.description = description;
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
        this.uuid = uuid;
        this.members = Collections.unmodifiableMap(new HashMap<>(members));
        this.pointsOfInterest = Collections.unmodifiableMap(new HashMap<>(pointsOfInterest));
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public String getDatetimeStart() {
        return datetimeStart;
    }


    public String getDatetimeEnd() {
        return datetimeEnd;
    }


    public String getUuid() {
        return uuid;
    }


    public Map<String, DatabaseUser> getMembers() {
        return new HashMap<>(members);
    }


    public Map<String, DatabasePointOfInterest> getPointsOfInterest() {
        return new HashMap<>(pointsOfInterest);
    }


    @Exclude
    public Event toEvent() {
        // TODO: test

        /*
        This variable defines if we need to update ourselves in the database and fill in
        our information.
        */
        boolean needToUpdateMyself = false;

        // We transform every DatabaseUser to a Member.
        List<Member> members = new ArrayList<>();
        for (DatabaseUser databaseUser : getMembers().values()) {
            Member memberToAdd = databaseUser.toMember();

            // We check if the member we are about to add is Account.shared.
            if (databaseUser.isAccount()) {
                Member mySelfAsMember = Account.shared.toMember();

                // If we have to update our selves we update the member.
                if (!memberToAdd.equals(mySelfAsMember)) {
                    memberToAdd = mySelfAsMember;
                    needToUpdateMyself = true;
                }
            }

            // We add the member.
            members.add(memberToAdd);
        }

        // We transform every DatabasePointOfInterest to a PointOfInterest
        Set<PointOfInterest> pointsOfInterest = new HashSet<>();
        for (DatabasePointOfInterest databasePointOfInterest : getPointsOfInterest().values()) {
            pointsOfInterest.add(databasePointOfInterest.toPointOfInterest());
        }

        return new Event(getUuid(),
                         getName(),
                         LocalDateTime.parse(getDatetimeStart()),
                         LocalDateTime.parse(getDatetimeEnd()),
                         getDescription(),
                         members,
                         pointsOfInterest,
                         needToUpdateMyself);
    }


    /**
     * Checks if we belong to the event either as member of unknown user.
     *
     * @return - true if and only if we belong to the event
     */
    @Exclude
    public boolean containedAsMember() {
        // TODO: test

        Set<String> uuidsOfMembers = getMembers().keySet();

        return uuidsOfMembers.contains(Account.shared.getUUID().get()) || containedAsUnknownUser();
    }


    /**
     * Checks if we are contained as an unknown user (only added by email since we didn't had a
     * UUID yet).
     *
     * @return - true if we are contained through our email
     */
    @Exclude
    private boolean containedAsUnknownUser() {
        // TODO: test

        Collection<DatabaseUser> users = getMembers().values();
        Set<String> unknownUsers = new HashSet<>();

        for (DatabaseUser user : users) {
            unknownUsers.add(user.getEmail());
        }

        return unknownUsers.contains(Account.shared.getEmail().get());
    }
}
