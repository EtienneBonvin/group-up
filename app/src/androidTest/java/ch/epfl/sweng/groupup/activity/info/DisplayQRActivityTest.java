package ch.epfl.sweng.groupup.activity.info;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DisplayQRActivityTest {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void qrButtonIsClickable() throws Exception {
        mActivityRule.getActivity().mock(true, true);
        login();
        clickQRButton();
    }

    private void login() {
        onView(withId(R.id.sign_in_button_google)).perform(click());
    }

    private void clickQRButton() {
        onView(withId(R.id.icon_access_user_profile)).perform(click());
        onView(withId(R.id.buttonDisplayQR)).perform(click());
    }
}
