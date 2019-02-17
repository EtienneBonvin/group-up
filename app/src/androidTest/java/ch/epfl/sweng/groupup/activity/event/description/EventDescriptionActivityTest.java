package ch.epfl.sweng.groupup.activity.event.description;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import org.junit.*;
import org.junit.runner.*;


@RunWith(AndroidJUnit4.class)
public class EventDescriptionActivityTest {

    @Rule
    public final ActivityTestRule<EventCreationActivity> mActivityRule = new ActivityTestRule<>(EventCreationActivity.class);


    @Test
    public void CreateAndDisplayAlertOnDeleteEvent() {
        Database.setUp();
        String name = "My beautiful event";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.toolbar_image_right)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());

        onView(withId(R.id.remove_event_button)).perform(click());

        onView(withText(R.string.alert_dialog_title_delete_event)).check(matches(isDisplayed()));
        onView(withText("Continue")).perform(click());
        Account.shared.clear();
    }


    @Test
    public void createDisplayAndChangeAnEvent() {
        Database.setUp();
        String name = "My event";
        String endName = " is changed";
        String description = "has an awesome description";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.toolbar_image_right)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());
        onView(withId(R.id.event_description_tv_description)).check(matches(withText(R.string.event_description_tv_description)));

        onView(withId(R.id.event_description_name)).check(matches(withText(name)));

        onView(withId(R.id.event_description_name)).perform(replaceText(name + endName));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.event_description_description)).perform(typeText(description));
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withId(R.id.linear_layout_event_list)).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.event_description_name)).check(matches(withText(name + endName)));
        onView(withId(R.id.event_description_description)).check(matches(withText(description)));
        Account.shared.clear();
    }


    @Test
    public void nameTooLong() {
        Database.setUp();
        String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String impossibleName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.toolbar_image_right)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());
        onView(withId(R.id.event_description_name)).check(matches(withText(name)));

        onView(withId(R.id.event_description_name)).perform(typeText(impossibleName));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withId(R.id.event_description_name)).check(matches(hasErrorText(getTargetContext().getString(R.string.event_creation_toast_event_name_too_long))));
        Account.shared.clear();
    }


    @Test
    public void nameTooShort() {
        Database.setUp();
        String name = "U so pretty";
        String impossibleName = "";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.toolbar_image_right)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());
        onView(withId(R.id.event_description_name)).check(matches(withText(name)));

        onView(withId(R.id.event_description_name)).perform(replaceText(impossibleName));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withId(R.id.event_description_name)).check(matches(hasErrorText(getTargetContext().getString(R.string.event_creation_toast_non_empty_event_name))));
        Account.shared.clear();
    }
}
