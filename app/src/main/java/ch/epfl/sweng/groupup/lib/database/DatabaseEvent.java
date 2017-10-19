package ch.epfl.sweng.groupup.lib.database;

import java.util.HashMap;

public final class DatabaseEvent {
    public String name = "";
    public String description = "";
    public String datetime_start = "";
    public String datetime_end = "";
    public String uuid = "";
    public HashMap<String, DatabaseUser> members = new HashMap<String, DatabaseUser>();

    public DatabaseEvent() {
    }

    public DatabaseEvent(String name, String description, String datetime_start, String
            datetime_end, String uuid, HashMap<String, DatabaseUser> members) {
        this.name = name;
        this.description = description;
        this.datetime_start = datetime_start;
        this.datetime_end = datetime_end;
        this.uuid = uuid;
        this.members = new HashMap<>(members);
    }
}
