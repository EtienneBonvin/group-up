package ch.epfl.sweng.groupup.lib.database;

public final class DatabaseUser {

    /**
     * Class to represent the user object that will be stored in the database.
     */

    public String given_name = Database.EMPTY_FIELD;
    public String family_name = Database.EMPTY_FIELD;
    public String display_name = Database.EMPTY_FIELD;
    public String email = Database.EMPTY_FIELD;
    public String uuid = Database.EMPTY_FIELD;

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
