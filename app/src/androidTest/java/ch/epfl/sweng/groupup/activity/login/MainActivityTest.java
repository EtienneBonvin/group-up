package ch.epfl.sweng.groupup.activity.login;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.*;
import org.junit.runner.*;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void newLoginFail() throws Exception {
        mActivityRule.getActivity()
                     .mock(false, true);
        onView(withId(R.id.sign_in_button_google)).perform(click());
    }


    @Test
    public void newLoginSuccessLogoutFail() throws Exception {
        mActivityRule.getActivity()
                     .mock(true, false);
        loginThenLogout();
    }


    private void loginThenLogout() {
        onView(withId(R.id.sign_in_button_google)).perform(click());
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withId(R.id.button_sign_out)).perform(click());
    }


    @Test
    public void newLoginSuccessLogoutSuccess() throws Exception {
        mActivityRule.getActivity()
                     .mock(true, true);
        loginThenLogout();
    }


    @Test
    public void testPressBackBeforeSignInReturnLoginActivity() {
        mActivityRule.getActivity()
                     .mock(false, true);
        if (FirebaseAuth.getInstance()
                        .getCurrentUser() == null) {
            onView(withId(R.id.sign_in_button_google)).check(matches(isDisplayed()));
            onView(withId(R.id.sign_in_button_google)).perform(click());
            pressBack();
            onView(withId(R.id.sign_in_button_google)).check(matches(isDisplayed()));
        }
    }
}
