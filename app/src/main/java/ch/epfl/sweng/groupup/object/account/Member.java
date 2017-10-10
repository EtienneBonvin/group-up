package ch.epfl.sweng.groupup.object.account;

/**
 * This class represents members of a group (which are User objects)
 */
public final class Member extends User {

    public Member(String firstName, String lastName, String email){
        super(firstName, lastName, email);
    }

    /**
     * Returns a new member with the given first name
     * @param newFirstName the new first name of the member
     * @return a new member with the given first name
     */
    public Member withFirstName(String newFirstName){
        return new Member(newFirstName, lastName, email);
    }

    /**
     * Returns a new member with the given last name
     * @param newLastName the new last name of the membre
     * @return a new member with the given last name
     */
    public Member withLastName(String newLastName){
        return new Member(firstName, newLastName, email);
    }

    /**
     * Returns a new member with the given email
     * @param newEmail the new email of the member
     * @return a new member with the given email
     */
    public Member withEmail(String newEmail){
        return new Member(firstName, lastName, newEmail);
    }
}
