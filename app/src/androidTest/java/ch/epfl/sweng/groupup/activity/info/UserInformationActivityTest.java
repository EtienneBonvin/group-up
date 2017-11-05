package ch.epfl.sweng.groupup.activity.info;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.info.UserInformationActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class UserInformationActivityTest {
    @Rule
    public final ActivityTestRule<UserInformationActivity> mActivityRule =
            new ActivityTestRule<>(UserInformationActivity.class);

    @Test
    public void displayDefaultAccountFields() throws Exception {
        onView(withId(R.id.text_view_display_name_text))
                .check(matches(withText(R.string.text_view_display_name_text)));
        onView(withId(R.id.text_view_display_name_info))
                .check(matches(withText(R.string.text_view_display_name_info)));
        onView(withId(R.id.text_view_family_name_text))
                .check(matches(withText(R.string.text_view_family_name_text)));
        onView(withId(R.id.text_view_family_name_info))
                .check(matches(withText(R.string.text_view_family_name_info)));
        onView(withId(R.id.text_view_given_name_text))
                .check(matches(withText(R.string.text_view_given_name_text)));
        onView(withId(R.id.text_view_given_name_info))
                .check(matches(withText(R.string.text_view_given_name_info)));
        onView(withId(R.id.text_view_email_text))
                .check(matches(withText(R.string.text_view_email_text)));
        onView(withId(R.id.text_view_email_info))
                .check(matches(withText(R.string.text_view_email_info)));
    }

    @Test
    public void displayErrorToastOnQrButtonClick() throws Exception {
        onView(withId(R.id.buttonDisplayQR)).perform(click());
        onView(withText(R.string.toast_unable_to_generate_qr))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                     .getWindow()
                                                     .getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.buttonDisplayQR)).perform(click());
    }

    @Test
    public void displayErrorToastOnSignOutButtonClick() throws Exception {
        onView(withId(R.id.button_sign_out)).perform(click());
        onView(withText(R.string.toast_unable_to_sign_out))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                     .getWindow()
                                                     .getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
