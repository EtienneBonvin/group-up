package ch.epfl.sweng.groupup.object.account;

import ch.epfl.sweng.groupup.lib.Optional;

/**
 * The user abstract class is extended by the Account and Member classes
 * to give them access to common first name, last name and email.
 */
public abstract class User {
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

    @Override
    public String toString() {
        return "Account{" +
                "UUID='" + UUID + '\'' +
                "displayName='" + displayName + '\'' +
                "givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        if (!UUID.equals(user.UUID)) return false;
        if (!displayName.equals(user.displayName)) return false;
        if (!givenName.equals(user.givenName)) return false;
        if (!familyName.equals(user.familyName)) return false;
        if (!email.equals(user.email)) return false;
        return true;
    }
}