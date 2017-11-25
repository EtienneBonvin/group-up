package ch.epfl.sweng.groupup.object.account;

import android.location.Location;

import ch.epfl.sweng.groupup.lib.Optional;

/**
 * This class represents members of a group (which are User objects)
 */
public final class Member extends User {

    public static final String UNKNOWN_USER_ = "UNKNOWN_USER_";

    public Member(String UUID,
                  String displayName,
                  String givenName,
                  String familyName,
                  String email,
                  Location location) {
        super(displayName, givenName, familyName, email, UUID, location);
        if(User.observer != null) {
            User.observer.updateMemberMarkers(UUID, displayName, location);
        }
    }

    public Member(Optional<String> UUID,
                  Optional<String> displayName,
                  Optional<String> givenName,
                  Optional<String> familyName,
                  Optional<String> email,
                  Optional<Location> location) {
        super(displayName, givenName, familyName, email, UUID, location);
        if(User.observer != null && !UUID.isEmpty() && !displayName.isEmpty() && !location.isEmpty()){
            User.observer.updateMemberMarkers(UUID.get(), displayName.get(), location.get());
        }
    }

    /**
     * Returns a new member with the given UUID
     *
     * @param UUID the new UUID of the member
     * @return a new member with the given UUID
     */
    public Member withUUID(String UUID) {
        return new Member(Optional.from(UUID),
                          displayName,
                          givenName,
                          familyName,
                          email,
                          location);
    }

    /**
     * Returns a new member with the given display name
     *
     * @param newDisplayName the new display name of the member
     * @return a new member with the given display name
     */
    public Member withDisplayName(String newDisplayName) {
        return new Member(UUID,
                          Optional.from(newDisplayName),
                          givenName,
                          familyName,
                          email,
                          location);
    }

    /**
     * Returns a new member with the given first name
     *
     * @param newFirstName the new first name of the member
     * @return a new member with the given first name
     */
    public Member withFirstName(String newFirstName) {
        return new Member(UUID,
                          displayName,
                          Optional.from(newFirstName),
                          familyName,
                          email,
                          location);
    }

    /**
     * Returns a new member with the given last name
     *
     * @param newLastName the new last name of the member
     * @return a new member with the given last name
     */
    public Member withLastName(String newLastName) {
        return new Member(UUID,
                          displayName,
                          givenName,
                          Optional.from(newLastName),
                          email,
                          location);
    }

    /**
     * Returns a new member with the given email
     *
     * @param newEmail the new email of the member
     * @return a new member with the given email
     */
    public Member withEmail(String newEmail) {
        return new Member(UUID,
                          displayName,
                          givenName,
                          familyName,
                          Optional.from(newEmail),
                          location);
    }

    /**
     * Returns a new member with the given location.
     *
     * @param location - the new location of the member
     * @return - a new member with the given location
     */
    public Member withLocation(Location location) {
        Member updatedMember = new Member(UUID,
                displayName,
                givenName,
                familyName,
                email,
                Optional.from(location));

        return updatedMember;
    }
}
