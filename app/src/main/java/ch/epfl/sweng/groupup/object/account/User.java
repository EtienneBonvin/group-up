package ch.epfl.sweng.groupup.object.account;

import android.location.Location;

import ch.epfl.sweng.groupup.lib.Optional;

/**
 * The user abstract class is extended by the Account and Member classes
 * to give them access to common first name, last name and email.
 */
public abstract class User {
    final Optional<String> displayName;
    final Optional<String> givenName;
    final Optional<String> familyName;
    protected final Optional<String> email;
    protected final Optional<String> UUID;
    final Optional<Location> location;

    protected User(String displayName,
                   String firstName,
                   String familyName,
                   String email,
                   String UUID,
                   Location location) {
        this.displayName = Optional.from(displayName);
        this.givenName = Optional.from(firstName);
        this.familyName = Optional.from(familyName);
        this.email = Optional.from(email);
        this.UUID = Optional.from(UUID);
        this.location = Optional.from(new Location(location));
    }

    protected User(Optional<String> displayName,
                   Optional<String> firstName,
                   Optional<String> familyName,
                   Optional<String> email,
                   Optional<String> UUID,
                   Optional<Location> location) {
        this.displayName = displayName;
        this.givenName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.UUID = UUID;

        if (location.isEmpty()) {
            this.location = Optional.empty();
        } else {
            this.location = Optional.from(new Location(location.get()));
        }
    }

    /**
     * Returns user's display name
     *
     * @return user's display name
     */
    public Optional<String> getDisplayName() {
        return displayName;
    }

    /**
     * Returns user's given name
     *
     * @return user's given name
     */
    public Optional<String> getGivenName() {
        return givenName;
    }

    /**
     * Returns user's last name
     *
     * @return user's last name
     */
    public Optional<String> getFamilyName() {
        return familyName;
    }

    /**
     * Returns user's email
     *
     * @return user's email
     */
    public Optional<String> getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
               "displayName=" + displayName +
               ",  givenName=" + givenName +
               ", familyName=" + familyName +
               ", email=" + email +
               '}';
    }

    /**
     * Returns user's UUID
     *
     * @return user's UUID
     */
    public Optional<String> getUUID() {
        return UUID;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        if (!UUID.equals(user.UUID)) {
            return false;
        }
        if (!displayName.equals(user.displayName)) {
            return false;
        }
        if (!givenName.equals(user.givenName)) {
            return false;
        }
        if (!familyName.equals(user.familyName)) {
            return false;
        }
        if (!email.equals(user.email)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the location of the user.
     *
     * @return - location of the user
     */
    public Optional<Location> getLocation() {
        if (location.isEmpty()) {
            return Optional.empty();
        } else {
            Location locationCopy = new Location(location.get());

            return Optional.from(locationCopy);
        }
    }
}
