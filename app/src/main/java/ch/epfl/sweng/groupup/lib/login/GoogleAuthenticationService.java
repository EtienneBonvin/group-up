package ch.epfl.sweng.groupup.lib.login;

import android.content.Intent;


public interface GoogleAuthenticationService {

    void onActivityResult(int requestCode, Intent data);

    void signIn();

    void signOut();

    enum Status {
        DISCONNECTED, CONNECTING, CONNECTED
    }
}
