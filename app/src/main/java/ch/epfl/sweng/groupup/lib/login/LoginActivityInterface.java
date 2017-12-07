package ch.epfl.sweng.groupup.lib.login;

import android.app.Activity;
import android.content.Intent;


public interface LoginActivityInterface {
    void onFail();

    void onSuccess();

    void startActivityForResult(Intent intent, int requestCode);

    Activity getActivity();
}
