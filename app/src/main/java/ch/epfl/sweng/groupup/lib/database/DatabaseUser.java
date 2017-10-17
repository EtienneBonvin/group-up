package ch.epfl.sweng.groupup.lib.database;

public final class DatabaseUser {
    public String given_name;
    public String family_name;
    public String display_name;
    public String email;
    public String uuid;

    public DatabaseUser() {
    }

    public DatabaseUser(String given_name, String family_name, String display_name, String
            email, String uuid) {
        this.given_name = given_name;
        this.family_name = family_name;
        this.display_name = display_name;
        this.email = email;
        this.uuid = uuid;
    }
}
