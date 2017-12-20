package ch.epfl.sweng.groupup.lib;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import android.os.Build;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import org.junit.*;
import org.junit.runner.*;


@RunWith(AndroidJUnit4.class)
public class AndroidHelperShould {

    @Rule
    public final ActivityTestRule<EventListingActivity> mActivityRule = new ActivityTestRule<>(
            EventListingActivity.class);


    @Test
    public void beAbleToTestIfEmulator() throws Exception {
        boolean isEmulator = Build.FINGERPRINT.startsWith("generic") ||
                             Build.FINGERPRINT.startsWith("unknown") ||
                             Build.MODEL.contains("google_sdk") ||
                             Build.MODEL.contains("Emulator") ||
                             Build.MODEL.contains("Android SDK built for x86") ||
                             Build.MANUFACTURER.contains("Genymotion") ||
                             (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                             "google_sdk".equals(Build.PRODUCT);

        assertTrue(isEmulator == AndroidHelper.isEmulator());
    }


    @Test
    public void correctlyDisplayAlert() throws Exception {
        final String alertTitle = "Alert Test";
        final String alertMessage = "Please test this alert!";
        final String alertButtonText = "Test Me!";

        mActivityRule.getActivity()
                     .runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             AndroidHelper.showAlert(mActivityRule.getActivity()
                                                                  .getWindow()
                                                                  .getContext(),
                                                     alertTitle,
                                                     alertMessage,
                                                     alertButtonText);
                         }
                     });
        onView(withText(alertTitle)).inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                                              .getWindow()
                                                                              .getDecorView()))))
                                    .perform(click());
    }


    @Test
    public void correctlyDisplayToast() throws Exception {
        final String toastString = "Please test this toast!";

        mActivityRule.getActivity()
                     .runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             AndroidHelper.showToast(mActivityRule.getActivity()
                                                                  .getApplicationContext(),
                                                     toastString,
                                                     Toast.LENGTH_SHORT);
                         }
                     });
        onView(withText(toastString)).inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                                               .getWindow()
                                                                               .getDecorView()))))
                                     .check(matches(
                                             isDisplayed()));
    }


    @Test
    public void instanciate() {
        new AndroidHelper();
    }
}
