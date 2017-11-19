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
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;

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
        onView(withParent(withId(R.id.linear_layout_event_list)))
                .perform(longClick());
        onView(withId(R.id.upload_file))
                .perform(click());
    }

    @After
    public void clearDatabase(){
        Account.shared.clear();
    }

    @Test
    public void backAndForthWithoutException(){
        Espresso.pressBack();
    }

    public void createEvent(){
        onView(withId(R.id.ui_edit_event_name))
                .perform(typeText(EVENT_NAME));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button))
                .perform(click());
    }
}
