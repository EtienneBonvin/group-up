package ch.epfl.sweng.groupup.object.account;

public final class UserAccount extends Account {
    public UserAccount(String firstName,
                       String lastName,
                       String email) {
        super(firstName, lastName, email, AccountType.USER);
    }
}
