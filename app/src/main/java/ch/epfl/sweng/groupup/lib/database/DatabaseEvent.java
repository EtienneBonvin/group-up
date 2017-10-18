package ch.epfl.sweng.groupup.lib.database;

import java.util.ArrayList;
import java.util.List;

public final class DatabaseEvent {
    public String name;
    public String description;
    public String datetime_start;
    public String datetime_end;
    public String uuid;
    public List<DatabaseUser> members;

    public DatabaseEvent() {
    }

    public DatabaseEvent(String name, String description, String datetime_start, String
            datetime_end, String uuid, List<DatabaseUser> members) {
        this.name = name;
        this.description = description;
        this.datetime_start = datetime_start;
        this.datetime_end = datetime_end;
        this.uuid = uuid;
        this.members = new ArrayList<>(members);
    }
}
