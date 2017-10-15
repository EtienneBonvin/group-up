package ch.epfl.sweng.groupup.object.account;

import ch.epfl.sweng.groupup.lib.Optional;
/**
 * This class represents members of a group (which are User objects)
 */
public final class Member extends User {

    public Member(String displayName, String givenName, String familyName, String email){
        super(displayName, givenName, familyName, email);
    }

    public Member(Optional<String> displayName, Optional<String> givenName, Optional<String> familyName, Optional<String> email){
        super(displayName, givenName, familyName, email);
    }

    /**
     * Returns a new member with the given display name
     * @param newDisplayName the new display name of the member
     * @return a new member with the given display name
     */
    public Member withDisplayName(String newDisplayName){
        return new Member(Optional.from(newDisplayName), givenName, familyName, email);
    }

    /**
     * Returns a new member with the given first name
     * @param newFirstName the new first name of the member
     * @return a new member with the given first name
     */
    public Member withFirstName(String newFirstName){
        return new Member(displayName, Optional.from(newFirstName), familyName, email);
    }

    /**
     * Returns a new member with the given last name
     * @param newLastName the new last name of the membre
     * @return a new member with the given last name
     */
    public Member withLastName(String newLastName){
        return new Member(displayName, givenName, Optional.from(newLastName), email);
    }

    /**
     * Returns a new member with the given email
     * @param newEmail the new email of the member
     * @return a new member with the given email
     */
    public Member withEmail(String newEmail) {
        return new Member(displayName, givenName, familyName, Optional.from(newEmail));
    }
}
