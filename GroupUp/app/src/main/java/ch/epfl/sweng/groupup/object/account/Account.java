package ch.epfl.sweng.groupup.object.account;

public class Account {

    private static final String NO_FIRST_NAME = "NO_FIRST_NAME";
    private static final String NO_LAST_NAME = "NO_LAST_NAME";
    private static final String NO_EMAIL = "NO_EMAIL";

    public static Account shared = new Account(NO_FIRST_NAME, NO_LAST_NAME, NO_EMAIL);

    private final String firstName;
    private final String lastName;
    private final String email;

    private Account(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Account withFirstName(String firstName) {
        shared = new Account(firstName, shared.getLastName(), shared.getEmail());
        return shared;
    }

    public Account withLastName(String lastName) {
        shared = new Account(shared.getFirstName(), lastName, shared.getEmail());
        return shared;
    }

    public Account withEmail(String email) {
        shared = new Account(shared.getFirstName(), shared.getLastName(), email);
        return shared;
    }

    public Account clear() {
        shared = new Account(NO_FIRST_NAME, NO_LAST_NAME, NO_EMAIL);
        return shared;
    }
}