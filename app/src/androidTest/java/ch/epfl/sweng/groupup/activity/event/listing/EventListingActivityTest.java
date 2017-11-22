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

    /*@Before
    public void setup(){
        Database.setUpDatabase();
    }*/

    @Before
    public void setup() {
        Database.setUpDatabase();

        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        List<Member> eventMembers = new ArrayList<>();
        event = new Event("Name", startDate, endDate, "Description", eventMembers, false);
        Account.shared.addOrUpdateEvent(event);

    }

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

    @Test
    public void ensureMapShownOnShortClick()  {
        // Create an event, is it necessary to perform all
        // the steps or does it exist a predefined setup with a function?

        // Short click on the newly created event

        // Check that the map is shown

        // In order to test the other else clause,
        // update the position and show the map again
        // and see that the position is the same as changed to

        Log.d("myTag", event.getUUID());

        onView(ViewMatchers.withTagValue(is((Object) event.getUUID())))
                .perform(click());

        /*onView(withText(String.format(Locale.getDefault(), "%s | %d/%d - %d/%d", event.getEventName(),
                event.getStartTime().getDayOfMonth(), event.getStartTime().getMonthOfYear(),
                event.getEndTime().getDayOfMonth(), event.getEndTime().getMonthOfYear()))).perform(click());*/

        /*onView(withContentDescription("Google Map")).perform(click());*/
    }

}
