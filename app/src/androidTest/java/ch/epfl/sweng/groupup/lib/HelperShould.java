package ch.epfl.sweng.groupup.lib;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.activity.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class HelperShould {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void correctlyDisplayAlert() throws Exception {
        final String alertTitle = "Alert Test";
        final String alertMessage = "Please test this alert!";
        final String alertButtonText = "Test Me!";

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Helper.showAlert(mActivityRule.getActivity().getWindow().getContext(),
                                 alertTitle,
                                 alertMessage,
                                 alertButtonText);
            }
        });
        onView(withText(alertTitle))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                     .getWindow()
                                                     .getDecorView()))))
                .perform(click());
    }

    @Test
    public void correctlyDisplayToast() throws Exception {
        final String toastString = "Please test this toast!";

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Helper.showToast(mActivityRule.getActivity().getApplicationContext(),
                                 toastString,
                                 Toast.LENGTH_SHORT);
            }
        });
        onView(withText(toastString))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                     .getWindow()
                                                     .getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
