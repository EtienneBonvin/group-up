package ch.epfl.sweng.groupup.lib.login;

import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;

public abstract class LoginActivityInterface extends ToolbarActivity {
    abstract public void onFail();

    abstract public void onSuccess();
}
