package ch.epfl.sweng.groupup.lib.database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
final class DatabaseEvent {

    /**
     * Class to represent the event object that will be stored in the database.
     */

    public String name = Database.EMPTY_FIELD;
    public String description = Database.EMPTY_FIELD;
    public String datetimeStart = Database.EMPTY_FIELD;
    public String datetimeEnd = Database.EMPTY_FIELD;
    public String uuid = Database.EMPTY_FIELD;
    public Map<String, DatabaseUser> members = new HashMap<>();
    public Map<String, DatabasePointOfInterest>
            pointsOfInterest = new HashMap<>();

    public DatabaseEvent() {
    }

    DatabaseEvent(String name, String description, String datetimeStart, String
            datetimeEnd, String uuid, HashMap<String, DatabaseUser> members,
                  HashMap<String, DatabasePointOfInterest> pointsOfInterest) {
        this.name = name;
        this.description = description;
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
        this.uuid = uuid;
        this.members = Collections.unmodifiableMap(new HashMap<>(members));
        this.pointsOfInterest =
                Collections.unmodifiableMap(new HashMap<>(pointsOfInterest));
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
}
