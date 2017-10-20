package ch.epfl.sweng.groupup.object.account;

import ch.epfl.sweng.groupup.lib.Optional;
/**
 * This class represents members of a group (which are User objects)
 */
public final class Member extends User {

    public Member(String UUID, String displayName, String givenName, String familyName, String email){
        super(displayName, givenName, familyName, email, UUID);
    }

    public Member(Optional<String> UUID, Optional<String> displayName, Optional<String> givenName, Optional<String> familyName, Optional<String> email){
        super(displayName, givenName, familyName, email, UUID);
    }

    /**
     * Returns a new member with the given UUID
     * @param UUID the new UUID of the member
     * @return a new member with the given UUID
     */
    public Member withUUID(String UUID) {
        return new Member(Optional.<String>from(UUID), displayName, givenName, familyName, email);
    }

    /**
     * Returns a new member with the given display name
     * @param newDisplayName the new display name of the member
     * @return a new member with the given display name
     */
    public Member withDisplayName(String newDisplayName){
        return new Member(UUID, Optional.from(newDisplayName), givenName, familyName, email);
    }

    /**
     * Returns a new member with the given first name
     * @param newFirstName the new first name of the member
     * @return a new member with the given first name
     */
    public Member withFirstName(String newFirstName){
        return new Member(UUID, displayName, Optional.from(newFirstName), familyName, email);
    }

    /**
     * Returns a new member with the given last name
     * @param newLastName the new last name of the membre
     * @return a new member with the given last name
     */
    public Member withLastName(String newLastName){
        return new Member(UUID, displayName, givenName, Optional.from(newLastName), email);
    }

    /**
     * Returns a new member with the given email
     * @param newEmail the new email of the member
     * @return a new member with the given email
     */
    public Member withEmail(String newEmail){
        return new Member(UUID, displayName, givenName, familyName, Optional.from(newEmail));
    }
}
