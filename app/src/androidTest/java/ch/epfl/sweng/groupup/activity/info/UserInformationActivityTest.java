package ch.epfl.sweng.groupup.activity.info;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.object.account.Account;

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

    @Before
    public void before() {
        Account.shared.clear();
    }

    @Test
    public void displayRightAccountFields() throws Exception {
        Account a = Account.shared.clear();
        Thread.sleep(5000);

        onView(withId(R.id.text_view_display_name_info))
                .check(matches(withText(a.getDisplayName()
                                                .getOrElse(mActivityRule
                                                                   .getActivity()
                                                                   .getString(R.string.text_view_display_name_info)).toString())));
        onView(withId(R.id.text_view_display_name_text))
                .check(matches(withText(a.getDisplayName()
                                                .getOrElse(mActivityRule
                                                                   .getActivity()
                                                                   .getString(R.string.text_view_display_name_text)).toString())));
        onView(withId(R.id.text_view_family_name_text))
                .check(matches(withText(a.getDisplayName()
                                                .getOrElse(mActivityRule
                                                                   .getActivity()
                                                                   .getString(R.string.text_view_family_name_text)).toString())));
        onView(withId(R.id.text_view_family_name_info))
                .check(matches(withText(a.getDisplayName()
                                                .getOrElse(mActivityRule
                                                                   .getActivity()
                                                                   .getString(R.string.text_view_family_name_info)).toString())));
        onView(withId(R.id.text_view_given_name_text))
                .check(matches(withText(a.getDisplayName()
                                                .getOrElse(mActivityRule
                                                                   .getActivity()
                                                                   .getString(R.string.text_view_given_name_text)).toString())));
        onView(withId(R.id.text_view_given_name_info))
                .check(matches(withText(a.getDisplayName()
                                                .getOrElse(mActivityRule
                                                                   .getActivity()
                                                                   .getString(R.string.text_view_given_name_info)).toString())));
        onView(withId(R.id.text_view_email_text))
                .check(matches(withText(a.getDisplayName()
                                                .getOrElse(mActivityRule
                                                                   .getActivity()
                                                                   .getString(R.string.text_view_email_text)).toString())));
        onView(withId(R.id.text_view_email_info))
                .check(matches(withText(a.getDisplayName()
                                                .getOrElse(mActivityRule
                                                                   .getActivity()
                                                                   .getString(R.string.text_view_email_info)).toString())));
    }

    /* IMPLEMENTATION CHANGED, THIS IS NOW USELESS
    @Test
    public void displayErrorToastOnQrButtonClick() throws Exception {
        Account.shared.clear();
        onView(withId(R.id.buttonDisplayQR)).perform(click());
        onView(withText(R.string.toast_unable_to_generate_qr))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                     .getWindow()
                                                     .getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.buttonDisplayQR)).perform(click());
        Account.shared.clear();
    }*/

    /* IMPLEMENTATION CHANGED, THIS IS NOW USELESS
    @Test
    public void displayErrorToastOnSignOutButtonClick() throws Exception {
        onView(withId(R.id.button_sign_out)).perform(click());
        onView(withText(R.string.toast_unable_to_sign_out))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                     .getWindow()
                                                     .getDecorView()))))
                .check(matches(isDisplayed()));
    }
    */
}
