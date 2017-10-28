package ch.epfl.sweng.groupup.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventDescription.EventDescriptionActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by alix on 10/28/17.
 */

@RunWith(AndroidJUnit4.class)
public class EventDescriptionTest {
    @Rule
    public final ActivityTestRule<EventDescriptionActivity> mActivityRule =
            new ActivityTestRule<>(EventDescriptionActivity.class);

    @Test
    public void displayDefaultAccountFields() throws Exception {
        onView(withId(R.id.event_description_tv_name))
                .check(matches(withText(R.string.event_description_tv_name)));

        onView(withId(R.id.event_description_name))
                .check(matches(withText(R.string.no_event_name)));

        onView(withId(R.id.event_description_tv_start_date))
                .check(matches(withText(R.string.event_description_tv_start_date)));

        onView(withId(R.id.event_description_start_date))
                .check(matches(withText(R.string.no_event_start_date)));

        onView(withId(R.id.event_description_tv_end_date))
                .check(matches(withText(R.string.event_description_tv_end_date)));

        onView(withId(R.id.event_description_end_date))
                .check(matches(withText(R.string.no_event_end_date)));

        onView(withId(R.id.event_description_tv_description))
                .check(matches(withText(R.string.event_description_tv_description)));
    }
}
