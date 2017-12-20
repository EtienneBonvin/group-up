package ch.epfl.sweng.groupup.activity.info;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.object.account.Account;
import org.junit.*;
import org.junit.runner.*;


@RunWith(AndroidJUnit4.class)
public class UserInformationActivityTest {

    @Rule
    public final ActivityTestRule<UserInformationActivity> mActivityRule =
            new ActivityTestRule<>(UserInformationActivity.class);


    @Test
    public void displayErrorToast() throws Exception {
        Account.shared.clear();
        onView(withText(R.string.toast_unable_to_generate_qr))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                          .getWindow()
                                                          .getDecorView()))))
                .check(matches(isDisplayed()));
    }


    @Test
    public void displayRightAccountFields() throws Exception {
        Account a = Account.shared;

        // compare titles
        onView(withId(R.id.text_view_display_name_info))
                .check(matches(withText(R.string.text_view_display_name_info)));

        onView(withId(R.id.text_view_family_name_info))
                .check(matches(withText(R.string.text_view_family_name_info)));

        onView(withId(R.id.text_view_given_name_info))
                .check(matches(withText(R.string.text_view_given_name_info)));

        onView(withId(R.id.text_view_email_info))
                .check(matches(withText(R.string.text_view_email_info)));

        onView(withId(R.id.text_view_display_name_text))
                .check(matches(withText(a.getDisplayName()
                                         .getOrElse(mActivityRule
                                                            .getActivity()
                                                            .getString(R.string.text_view_display_name_text)))));

        onView(withId(R.id.text_view_family_name_text))
                .check(matches(withText(a.getFamilyName()
                                         .getOrElse(mActivityRule
                                                            .getActivity()
                                                            .getString(R.string.text_view_family_name_text)))));

        onView(withId(R.id.text_view_given_name_text))
                .check(matches(withText(a.getGivenName()
                                         .getOrElse(mActivityRule
                                                            .getActivity()
                                                            .getString(R.string.text_view_given_name_text)))));

        onView(withId(R.id.text_view_email_text))
                .check(matches(withText(a.getEmail()
                                         .getOrElse(mActivityRule
                                                            .getActivity()
                                                            .getString(R.string.text_view_email_text)))));
    }


    @Test
    public void shouldBeAbleToReturnActivity() throws Exception {
        mActivityRule.getActivity()
                     .getActivity();
    }
}
