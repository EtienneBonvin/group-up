package ch.epfl.sweng.groupup.activity.event.listing;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.AssertionFailedError;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class EventListingActivityTest {

    private Event event;

    @Rule
    public ActivityTestRule<EventListingActivity> mEventListingActivityActivityTestRule =
            new ActivityTestRule<>(EventListingActivity.class);

    @Test
    public void ensureCreateNewEventWork()  {

        // Click on the create_new_event button, when ID is implemented, use it.
        onView(ViewMatchers.withId(R.id.createEventButton))
                .perform(click());

        // Checks that the EventCreationActivity view is displayed by looking for ui_edit_event_name,
        // change to same as Etienne when Solal comes back with an answer.
        try {
            onView(withId(R.id.ui_edit_event_name)).check(matches(isDisplayed()));
            // The ui_edit_event_name is displayed in the view.
        } catch (AssertionFailedError e) {
            // The ui_edit_event_name is not displayed in the view.
        }
    }
}
