package ch.epfl.sweng.groupup.lib.login;
/*
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginTestSuite extends LoginActivityInterface {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void test() throws Exception {
        mActivityRule.getActivity().setAuthService(new MockAuth(this, true, true));
        onView(withId(R.id.sign_in_button_google)).perform(click());
    }

    @Override
    public void onFail() {
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("###", "FAIL");
            }
        });
    }

    @Override
    public void onSuccess() {
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("###", "SUCCESS");
            }
        });
    }
}
*/
