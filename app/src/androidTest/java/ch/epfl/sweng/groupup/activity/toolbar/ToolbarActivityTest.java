package ch.epfl.sweng.groupup.activity.toolbar;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.info.UserInformationActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ToolbarActivityTest {

    @Rule
    // third parameter is set to true which means the activity is started automatically
    public ActivityTestRule<ToolbarActivity> mActivityRule =
            new ActivityTestRule<>(ToolbarActivity.class, false, false);

    @Before
    public void setup(){
        Intents.init();
    }

    @After
    public void finish(){
        Intents.release();
    }

    @Test
    public void UserProfileOpenedOnIconClick(){
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.toolbar_image_right)).perform(click());
        intended(hasComponent(UserInformationActivity.class.getName()));

    }

    @Test
    public void GroupListOpenedOnIconClick(){
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.toolbar_image_left)).perform(click());
        intended(hasComponent(EventListingActivity.class.getName()));

    }
}
