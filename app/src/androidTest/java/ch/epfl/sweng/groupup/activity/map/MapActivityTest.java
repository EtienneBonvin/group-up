package ch.epfl.sweng.groupup.activity.map;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.Set;
import org.junit.*;


public class MapActivityTest {

    @Rule
    public ActivityTestRule<EventCreationActivity> mActivityRule = new ActivityTestRule<>(EventCreationActivity.class);
    private final String EVENT_NAME = "Map test";
    private final String MARKER_DESCRIPTION = "Marker description";
    private final String MARKER_NAME = "Marker test";


    @Test
    public void ensureMapShownOnSwipe() {
        createEvent();

        onView(withId(R.id.map)).perform(click());

        deleteEvent();
    }


    private void createEvent() {
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(EVENT_NAME));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withId(R.id.linear_layout_event_list)).perform(click());
        onView(withId(R.id.tap_view_map)).perform(click());
    }


    private void deleteEvent() {
        onView(withId(R.id.tap_view_details)).perform(click());
        onView(withId(R.id.remove_event_button)).perform(click());
        onView(withText("Continue")).perform(click());
    }


    @After
    public void finish() {
        Account.shared.clear();
    }


    @Before
    public void setup() {
        Account.shared.clear();
        Database.setUp();
        Database.setUpEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }
        });
        mActivityRule.getActivity()
                     .mockMap();
    }


    @Ignore
    public void testAddAndNotRemoveMarker() {
        createEvent();

        UiObject mMarker = addMarker(R.string.poi_create_add);

        try {
            mMarker.dragTo(mMarker, 1);
            onView(withText(R.string.poi_action_cancel)).perform(click());
            mMarker.click();
        } catch (UiObjectNotFoundException e) {
        }

        assertMarkersNotEmpty();
    }


    private UiObject addMarker(int buttonToPush) {
        onView(withId(R.id.map)).perform(longClick());

        onView(withHint(R.string.poi_title_hint)).perform(typeText(MARKER_NAME));
        onView(withHint(R.string.poi_description_hint)).perform(typeText(MARKER_DESCRIPTION));

        onView(withText(buttonToPush)).perform(click());

        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());

        return uiDevice.findObject(new UiSelector().descriptionContains(MARKER_DESCRIPTION));
    }


    private void assertMarkersNotEmpty() {
        Set<PointOfInterest> allMarkers = Account.shared.getEvents()
                                                        .get(0)
                                                        .getPointsOfInterest();
        assertFalse(allMarkers.isEmpty());

        deleteEvent();
    }


    @Ignore
    public void testAddAndRemoveMarker() {
        createEvent();

        UiObject mMarker = addMarker(R.string.poi_create_add);

        try {
            mMarker.dragTo(mMarker, 1);
            onView(withText(R.string.poi_action_remove)).perform(click());
            mMarker.click();
        } catch (UiObjectNotFoundException e) {
        }

        assertMarkersEmpty();
    }


    private void assertMarkersEmpty() {
        Set<PointOfInterest> allMarkers = Account.shared.getEvents()
                                                        .get(0)
                                                        .getPointsOfInterest();
        assertTrue(allMarkers.isEmpty());

        deleteEvent();
    }


    @Test
    public void testAddMarker() {
        createEvent();

        UiObject mMarker = addMarker(R.string.poi_create_add);

        assertMarkersNotEmpty();
    }


    @Test
    public void testNotAddMarker() {
        createEvent();

        UiObject mMarker = addMarker(R.string.poi_create_cancel);

        assertMarkersEmpty();
    }
}
