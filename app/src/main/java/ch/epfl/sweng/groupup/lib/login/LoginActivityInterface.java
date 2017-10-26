package ch.epfl.sweng.groupup.lib.login;

import android.support.v7.app.AppCompatActivity;

public abstract class LoginActivityInterface extends AppCompatActivity {
    abstract public void onFail();

    abstract public void onSuccess();
}
