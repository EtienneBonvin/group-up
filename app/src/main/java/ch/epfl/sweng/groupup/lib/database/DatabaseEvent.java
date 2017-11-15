package ch.epfl.sweng.groupup.lib.database;

import java.util.Collections;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
final class DatabaseEvent {

    /**
     * Class to represent the event object that will be stored in the database.
     */

    public String name = Database.EMPTY_FIELD;
    public String description = Database.EMPTY_FIELD;
    public String datetime_start = Database.EMPTY_FIELD;
    public String datetime_end = Database.EMPTY_FIELD;
    public String uuid = Database.EMPTY_FIELD;
    public Map<String, DatabaseUser> members = new HashMap<>();

    public DatabaseEvent() {
    }

    DatabaseEvent(String name, String description, String datetime_start, String
            datetime_end, String uuid, HashMap<String, DatabaseUser> members) {
        this.name = name;
        this.description = description;
        this.datetime_start = datetime_start;
        this.datetime_end = datetime_end;
        this.uuid = uuid;
        this.members = Collections.unmodifiableMap(new HashMap<>(members));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDatetime_start() {
        return datetime_start;
    }

    public String getDatetime_end() {
        return datetime_end;
    }

    public String getUuid() {
        return uuid;
    }

    public Map<String, DatabaseUser> getMembers() {
        return members;
    }
}
