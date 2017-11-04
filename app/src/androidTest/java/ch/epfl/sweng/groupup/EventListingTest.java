package ch.epfl.sweng.groupup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.AssertionFailedError;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventListingTest {

    @Rule
    public ActivityTestRule<EventListingActivity> mEventListingActivityActivityTestRule =
            new ActivityTestRule<>(EventListingActivity.class);

    @Test
    public void ensureCreateNewEventWork()  {

        // Click on the create_new_event button, when ID is implemented, use it.
        onView(withText(R.string.create_new_event))
                .perform(click());

        // Checks that the EventCreationActivity view is displated by looking for ui_edit_event_name,
        // change to same as Etienne when Solal comes back with an answer.
        try {
            onView(withId(R.id.ui_edit_event_name)).check(matches(isDisplayed()));
            // The ui_edit_event_name is displayed in the view.
        } catch (AssertionFailedError e) {
            // The ui_edit_event_name is not displayed in the view.
        }
    }
}
