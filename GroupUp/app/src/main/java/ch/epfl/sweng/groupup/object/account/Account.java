package ch.epfl.sweng.groupup.object.account;

public abstract class Account {

    public static final String NO_FIRST_NAME = "NO_FIRST_NAME";
    public static final String NO_LAST_NAME = "NO_LAST_NAME";
    public static final String NO_EMAIL = "NO_EMAIL";

    private String firstName;
    private String lastName;
    private String email;
    private AccountType accountType;

    Account(String firstName, String lastName, String email, AccountType accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountType = accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
