package ch.epfl.sweng.groupup.object.account;

/**
 * The user abstract class is extended by the Account and Member classes
 * to give them access to common first name, last name and email.
 */
abstract class User {
    protected final String firstName;
    protected final String lastName;
    protected final String email;

    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Returns user's first name
     * @return user's first name
     */
    public String getFirstName(){
        return firstName;
    }

    /**
     * Returns user's last name
     * @return user's last name
     */
    public String getLastName(){
        return lastName;
    }

    /**
     * Returns user's email
     * @return user's email
     */
    public String getEmail(){
        return email;
    }
}
