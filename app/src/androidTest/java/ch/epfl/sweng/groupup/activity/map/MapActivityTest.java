package ch.epfl.sweng.groupup.activity.map;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.BuildConfig;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.lib.database.Database;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

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
        String name="maptest1";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());

        onView(withContentDescription("Google Map")).perform(click());
    }

    @Test
    public void testAddMarker()  {
        String name = "maptest2";
        String nameMarker = "markertest2";
        String markeDescription = "markerdescription2";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());

        onView(withContentDescription("Google Map")).perform(longClick());

        onView(withHint(R.string.poi_title_hint)).perform(typeText(nameMarker));
        onView(withHint(R.string.poi_description_hint)).perform(typeText(markeDescription));

        onView(withText(R.string.poi_create_add)).perform(click());

        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains(nameMarker));
        try {
            mMarker1.click();
        } catch (UiObjectNotFoundException e) {
            assertEquals(0,1);
        }

        /*onView(withText(nameMarker));
        onView(withText(markeDescription));*/
    }

    @Test
    public void testNotAddMarker()  {
        String name = "maptest3";
        String nameMarker = "markertest3";
        String markeDescription = "markerdescription3";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());

        onView(withContentDescription("Google Map")).perform(longClick());

        onView(withHint(R.string.poi_title_hint)).perform(typeText(nameMarker));
        onView(withHint(R.string.poi_description_hint)).perform(typeText(markeDescription));

        onView(withText(R.string.poi_create_cancel)).perform(click());

        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains(nameMarker));
        try {
            mMarker1.click();
        } catch (UiObjectNotFoundException e) {
            assertEquals(1,1);
            return;
        }
        assertEquals(0,1);
    }

    @Test
    public void testAddAndRemoveMarker()  {
        String name = "maptest4";
        String nameMarker = "markertest4";
        String markeDescription = "markerdescription4";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());

        onView(withContentDescription("Google Map")).perform(longClick());

        onView(withHint(R.string.poi_title_hint)).perform(typeText(nameMarker));
        onView(withHint(R.string.poi_description_hint)).perform(typeText(markeDescription));

        onView(withText(R.string.poi_create_add)).perform(click());

        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains(nameMarker));
        try {
            mMarker1.dragTo(mMarker1,1);
            onView(withText(R.string.poi_remove_positive)).perform(click());
            mMarker1.click();
        } catch (UiObjectNotFoundException e) {
            assertEquals(1,1);
            return;
        }
        assertEquals(0,1);
    }

    @Test
    public void testAddAndNotRemoveMarker()  {
        String name = "maptest5";
        String nameMarker = "markertest5";
        String markeDescription = "markerdescription5";
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button)).perform(click());

        onView(withId(R.id.linear_layout_event_list)).perform(click());

        onView(withContentDescription("Google Map")).perform(longClick());

        onView(withHint(R.string.poi_title_hint)).perform(typeText(nameMarker));
        onView(withHint(R.string.poi_description_hint)).perform(typeText(markeDescription));

        onView(withText(R.string.poi_create_add)).perform(click());

        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains(nameMarker));
        try {
            mMarker1.dragTo(mMarker1,1);
            onView(withText(R.string.poi_remove_negative)).perform(click());
            mMarker1.click();
        } catch (UiObjectNotFoundException e) {
            assertEquals(0,1);
        }
        assertEquals(1,1);
    }
}
