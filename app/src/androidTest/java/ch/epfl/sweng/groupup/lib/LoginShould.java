package ch.epfl.sweng.groupup.lib;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.activity.login.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class LoginShould {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void foo() throws Exception {
        // TODO: implement
    }
}
