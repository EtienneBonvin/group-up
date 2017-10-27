package ch.epfl.sweng.groupup.activity.login;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginTestSuite {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void newLoginSuccessLogoutSuccess() throws Exception {
        mActivityRule.getActivity().mock(true, true);
        loginThenLogout();
    }

    @Test
    public void newLoginSuccessLogoutFail() throws Exception {
        mActivityRule.getActivity().mock(true, true);
        loginThenLogout();
    }

    @Test
    public void newLoginFail() throws Exception {
        mActivityRule.getActivity().mock(true, true);
        onView(withId(R.id.sign_in_button_google)).perform(click());
    }

    private void loginThenLogout(){
        onView(withId(R.id.sign_in_button_google)).perform(click());
        onView(withId(R.id.icon_access_user_profile)).perform(click());
        onView(withId(R.id.button_sign_out)).perform(click());
    }
}
