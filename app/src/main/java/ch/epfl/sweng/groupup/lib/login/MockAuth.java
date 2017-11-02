package ch.epfl.sweng.groupup.lib.login;

import android.content.Intent;

import ch.epfl.sweng.groupup.object.account.Account;

public final class MockAuth implements GoogleAuthenticationService {

    private final LoginActivityInterface activityInterface;
    private final boolean signInSuccess;
    private final boolean signOutSuccess;

    public MockAuth(LoginActivityInterface activityInterface, boolean signInSuccess, boolean
            signOutSuccess) {
        this.activityInterface = activityInterface;
        this.signInSuccess = signInSuccess;
        this.signOutSuccess = signOutSuccess;
    }

    @Override
    public void signIn() {
        if (signInSuccess) {
            Account.shared
                    .withEmail("xavier@maire.com")
                    .withDisplayName("Cedric")
                    .withFamilyName("Jeannerot")
                    .withGivenName("Selma")
                    .withUUID("EtienneID");

            activityInterface.onSuccess();
        } else {
            activityInterface.onFail();
        }
    }

    @Override
    public void signOut() {
        if (signOutSuccess) {
            Account.shared.clear();
            activityInterface.onSuccess();
        } else {
            activityInterface.onFail();
        }
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        // UNUSED
    }
}
