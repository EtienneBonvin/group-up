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
    @Rule
    public final ActivityTestRule<EventDescriptionActivity> mActivityRule =
            new ActivityTestRule<>(EventDescriptionActivity.class);

    Event e = new Event("Name", new LocalDateTime(), new LocalDateTime().plusDays(1),
                "My amazing description", new ArrayList<>(Arrays.asList(new Member("1","displayed","","",""),
                new Member("2","YOLO","","",""),new Member("3","LOOOOOOOOOOOOOOL","","",""))));


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

    @Test
    public void displayCorrectEvent(){
        onView(withId(R.id.event_description_tv_name))
                .check(matches(withText(R.string.event_description_tv_name)));

        onView(withId(R.id.event_description_name))
                .check(matches(withText(e.getEventName())));

        onView(withId(R.id.event_description_tv_start_date))
                .check(matches(withText(R.string.event_description_tv_start_date)));

        onView(withId(R.id.event_description_start_date))
                .check(matches(withText(e.getStartTime().toString(null, Locale.FRANCE))));

        onView(withId(R.id.event_description_tv_end_date))
                .check(matches(withText(R.string.event_description_tv_end_date)));

        onView(withId(R.id.event_description_end_date))
                .check(matches(withText(e.getEndTime().toString(null, Locale.FRANCE))));

        onView(withId(R.id.event_description_tv_description))
                .check(matches(withText(R.string.event_description_tv_description)));

        onView(withId(R.id.event_description_description))
                .check(matches(withText(e.getDescription())));
    }

    @Test
    public void nameIsWellChanged(){
        onView(withId(R.id.modifyName)).perform(click());
        onView(withId(R.id.event_description_name)).perform(typeText("Test"))
                .check(matches(withText("Test")));
    }

    @Test
    public void eventIsRemoved(){
        onView(withId(R.id.remove_event_button)).perform(click());
        if (BuildConfig.DEBUG && !Account.shared.getEvents().contains(e)){
            throw new AssertionError();
        }
    }
}
