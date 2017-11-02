package ch.epfl.sweng.groupup.activity;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.BuildConfig;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.joda.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventCreation.EventCreation;
import ch.epfl.sweng.groupup.activity.eventDescription.EventDescriptionActivity;
import ch.epfl.sweng.groupup.eventCreation.EventCreationTest;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by alix on 10/28/17.
 */

@RunWith(AndroidJUnit4.class)
public class EventDescriptionTest {
    private String name = "My beautiful event";

    @Rule
    public final ActivityTestRule<EventCreation> mActivityRule =
            new ActivityTestRule<>(EventCreation.class);


    @Test
    public void createDisplayAndChangeAnEvent() {
        Database.setUpDatabase();
        String name = "My beautiful event";
        String endName = " is changed";
        String description = "has an awesome description";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_button)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());
        onView(withId(R.id.event_description_tv_name))
                .check(matches(withText(R.string.event_description_tv_name)));
        onView(withId(R.id.event_description_name))
                .check(matches(withText(name)));
        onView(withId(R.id.event_description_name)).perform(typeText(endName));
        onView(withId(R.id.event_description_description)).perform(typeText(description));
        onView(withId(R.id.save)).perform(click());
        onView(withId(R.id.event_description_name)).check(matches(withText(name + endName)));
        onView(withId(R.id.event_description_description)).check(matches(withText(description)));
        Account.shared.clear();
    }

    @Test
    public void CreateAndRemoveAnEvent() {

        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_button)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());

        onView(withId(R.id.remove_event_button)).perform(click());

        if (BuildConfig.DEBUG && !(Account.shared.getEvents().isEmpty())){
            throw new AssertionError();
        }
        Account.shared.clear();
    }
}
