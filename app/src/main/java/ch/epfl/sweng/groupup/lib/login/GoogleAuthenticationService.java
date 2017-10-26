package ch.epfl.sweng.groupup.lib.login;

import android.content.Intent;

public interface GoogleAuthenticationService {
    enum Status {
        DISCONNECTED, CONNECTING, CONNECTED
    }

    void signIn();

    void signOut();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
