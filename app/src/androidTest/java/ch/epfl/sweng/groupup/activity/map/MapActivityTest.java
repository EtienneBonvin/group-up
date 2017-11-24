package ch.epfl.sweng.groupup.activity.map;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.BuildConfig;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.lib.database.Database;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MapActivityTest {

    @Rule
    public ActivityTestRule<EventCreationActivity> mActivityRule = new ActivityTestRule<>(
            EventCreationActivity.class);

    @Before
    public void setup(){
        Database.setUpDatabase();
    }

    @Test
    public void ensureMapShownOnShortClick()  {
        String name="maptest";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());

        onView(withContentDescription("Google Map")).perform(click());
    }

}
