package ch.epfl.sweng.groupup.activity.event.File;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.lib.database.Database;

@RunWith(AndroidJUnit4.class)
public class MediaSharingTests {

    private final String EVENT_NAME = "My event";

    @Rule
    public final ActivityTestRule<EventCreationActivity> mActivityRule =
            new ActivityTestRule<>(EventCreationActivity.class);

    @Before
    public void goToFileManagement(){
        Database.setUpDatabase();
        createEvent();
        /*onView(withText(EVENT_NAME))
                .perform(click());
        onView(withId(R.id.upload_file))
                .perform(click());*/
    }

    @Test
    public void backAndForthWithoutException(){
        /*onView(withId(R.id.add_files))
                .perform(click());

        Espresso.pressBack();*/
    }

    public void createEvent(){
        onView(withId(R.id.ui_edit_event_name))
                .perform(typeText(EVENT_NAME));

        onView(withId(R.id.save_new_event_button))
                .perform(click());
    }
}
