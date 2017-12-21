package ch.epfl.sweng.groupup.object.account;

import android.location.Location;
import ch.epfl.sweng.groupup.activity.event.description.EventDescriptionActivity;
import ch.epfl.sweng.groupup.lib.Optional;


/**
 * The user abstract class is extended by the Account and Member classes
 * to give them access to common first name, last name and email.
 */
public abstract class User {

    public static EventDescriptionActivity observer = null;
    protected final Optional<String> UUID;
    protected final Optional<String> email;
    final Optional<String> displayName;
    final Optional<String> familyName;
    final Optional<String> givenName;
    final Optional<Location> location;


    protected User(String displayName, String firstName, String familyName, String email, String UUID,
                   Location location) {
        this.displayName = Optional.from(displayName);
        this.givenName = Optional.from(firstName);
        this.familyName = Optional.from(familyName);
        this.email = Optional.from(email);
        this.UUID = Optional.from(UUID);

        if (location == null) {
            this.location = Optional.empty();
        } else {
            this.location = Optional.from(new Location(location));
        }
    }


    protected User(Optional<String> displayName, Optional<String> firstName, Optional<String> familyName,
                   Optional<String> email, Optional<String> UUID, Optional<Location> location) {
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
        return UUID.equals(user.UUID)
               && displayName.equals(user.displayName)
               && givenName.equals(user.givenName)
               && familyName.equals(user.familyName)
               && email.equals(user.email);
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
     * Returns user's email
     *
     * @return user's email
     */
    public Optional<String> getEmail() {
        return email;
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
     * Returns user's given name
     *
     * @return user's given name
     */
    public Optional<String> getGivenName() {
        return givenName;
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


    /**
     * Returns user's UUID
     *
     * @return user's UUID
     */
    public Optional<String> getUUID() {
        return UUID;
    }


    @Override
    public String toString() {
        return "User{"
               + "displayName="
               + displayName
               + ",  givenName="
               + givenName
               + ", familyName="
               + familyName
               + ", email="
               + email
               + ", location="
               + location.toString()
               + '}';
    }
}
