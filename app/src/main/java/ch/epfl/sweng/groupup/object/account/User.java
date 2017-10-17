package ch.epfl.sweng.groupup.object.account;

import ch.epfl.sweng.groupup.lib.Optional;

/**
 * The user abstract class is extended by the Account and Member classes
 * to give them access to common first name, last name and email.
 */
abstract class User {
    protected final Optional<String> displayName;
    protected final Optional<String> givenName;
    protected final Optional<String> familyName;
    protected final Optional<String> email;
    protected final Optional<String> UUID;

    protected User(String displayName, String firstName, String familyName, String email, String UUID){
        this.displayName = Optional.from(displayName);
        this.givenName = Optional.from(firstName);
        this.familyName = Optional.from(familyName);
        this.email = Optional.from(email);
        this.UUID = Optional.from(UUID);
    }

    protected User(Optional<String> displayName, Optional<String> firstName, Optional<String> familyName, Optional<String> email, Optional<String> UUID){
        this.displayName = displayName;
        this.givenName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.UUID = UUID;
    }

    /**
     * Returns user's display name
     * @return user's display name
     */
    public Optional<String> getDisplayName(){
        return displayName;
    }

    /**
     * Returns user's given name
     * @return user's given name
     */
    public Optional<String> getGivenName(){
        return givenName;
    }

    /**
     * Returns user's last name
     * @return user's last name
     */
    public Optional<String> getFamilyName(){
        return familyName;
    }

    /**
     * Returns user's email
     * @return user's email
     */
    public Optional<String> getEmail(){
        return email;
    }

    /**
     * Returns user's UUID
     * @return user's UUID
     */
    public Optional<String> getUUID() {return UUID; }
}
