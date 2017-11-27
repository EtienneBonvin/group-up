package ch.epfl.sweng.groupup.activity.map;

import android.app.Activity;
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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
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

    private final String EVENT_NAME = "Map test";
    private final String MARKER_NAME = "Marker test";
    private final String MARKER_DESCRIPTION = "Marker description";

    @Rule
    public ActivityTestRule<EventCreationActivity> mActivityRule = new ActivityTestRule<>(
            EventCreationActivity.class);

    @Before
    public void setup(){
        Database.setUpDatabase();
    }

    @Test
    public void ensureMapShownOnShortClick()  {
        createEvent();

        onView(withContentDescription("Google Map")).perform(click());

        deleteEvent();
    }

    @Test
    public void testAddMarker() {
        createEvent();

        UiObject mMarker = addMarker(R.string.poi_create_add);

        try {
            mMarker.click();
        } catch (UiObjectNotFoundException e) {
            deleteEvent();
            assertEquals(0,1);
        }
        deleteEvent();
    }

    @Test
    public void testNotAddMarker() {
        createEvent();

        UiObject mMarker = addMarker(R.string.poi_create_cancel);

        try {
            mMarker.click();
        } catch (UiObjectNotFoundException e) {
            deleteEvent();
            return;
        }
        deleteEvent();
        assertEquals(0,1);
    }

    @Test
    public void testAddAndRemoveMarker() {
        createEvent();

        UiObject mMarker = addMarker(R.string.poi_create_add);

        try {
            mMarker.dragTo(mMarker,1);
            onView(withText(R.string.poi_remove_positive)).perform(click());
            mMarker.click();
        } catch (UiObjectNotFoundException e) {
            deleteEvent();
            return;
        }
        deleteEvent();
        assertEquals(0,1);
    }

    @Test
    public void testAddAndNotRemoveMarker() {
        createEvent();

        UiObject mMarker = addMarker(R.string.poi_create_add);

        try {
            mMarker.dragTo(mMarker,1);
            onView(withText(R.string.poi_remove_negative)).perform(click());
            mMarker.click();
        } catch (UiObjectNotFoundException e) {
            deleteEvent();
            assertEquals(0,1);
        }
        deleteEvent();
    }

    private void createEvent() {
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(EVENT_NAME));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_new_event_button)).perform(click());
        onView(withId(R.id.linear_layout_event_list)).perform(click());
    }

    private void deleteEvent() {
        pressBack();
        onView(withId(R.id.linear_layout_event_list)).perform(longClick());
        onView(withId(R.id.remove_event_button)).perform(click());
        onView(withText("Continue")).perform(click());
    }

    private UiObject addMarker(int buttonToPush) {
        onView(withContentDescription("Google Map")).perform(longClick());

        onView(withHint(R.string.poi_title_hint)).perform(typeText(MARKER_NAME));
        onView(withHint(R.string.poi_description_hint)).perform(typeText(MARKER_DESCRIPTION));

        onView(withText(buttonToPush)).perform(click());

        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());

        return uiDevice.findObject(new UiSelector().descriptionContains(MARKER_NAME));
    }
}
