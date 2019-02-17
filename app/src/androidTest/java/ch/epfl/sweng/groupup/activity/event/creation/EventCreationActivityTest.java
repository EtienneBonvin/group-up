package ch.epfl.sweng.groupup.activity.event.creation;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.*;

import android.location.Location;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.TimePicker;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.hamcrest.*;
import org.joda.time.LocalDateTime;
import org.junit.*;
import org.junit.rules.*;


public class EventCreationActivityTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Rule
    // third parameter is set to true which means the activity is started automatically
    public ActivityTestRule<EventCreationActivity> mActivityRule = new ActivityTestRule<>(EventCreationActivity.class,
                                                                                          true,
                                                                                          true);
    private final String EVENT_DESCRIPTION = "My description";
    private final String EVENT_MEMBER = "swenggroupup@gmail.com";
    private final String EVENT_NAME = "My event";


    @Test
    public void atLeastOneMinuteBetweenStartAndEndDate() {
        addEventName(EVENT_NAME);
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2100, 5, 5, 5, 5);
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withText(R.string.event_creation_toast_event_last_1_minute)).inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                                                                                     .getWindow()
                                                                                                                     .getDecorView()))))
                                                                           .check(matches(isDisplayed()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            //The tests are stopped, nothing to do.
        }
    }


    private void addEventName(String name) {
        onView(withId(R.id.ui_edit_event_name)).perform(clearText());
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();
    }


    /**
     * Helper functions
     */

    private void setStartDate(int year, int month, int day, int hour, int minute) {
        onView(withId(R.id.button_start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year,
                                                                                                          month,
                                                                                                          day));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hour,
                                                                                                          minute));
        onView(withId(android.R.id.button1)).perform(click());
    }


    private void setEndDate(int year, int month, int day, int hour, int minute) {
        onView(withId(R.id.button_end_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year,
                                                                                                          month,
                                                                                                          day));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_end_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hour,
                                                                                                          minute));
        onView(withId(android.R.id.button1)).perform(click());
    }


    /**
     * Tests if members which previously were added are correctly displayed if you enter members adding view again
     * covers the restoreState code
     */
    @Test
    public void correctlyDisplayAlreadyAddedMembers() {
        addMember(EVENT_MEMBER);

        // Check that already added members get displayed correctly
        onView(withId(R.id.button_add_members)).perform(click());
        onView(withId(R.id.members_list)).check(matches(withChild(withChild(withText((EVENT_MEMBER))))));
    }


    private void addMember(String input) {
        onView(withId(R.id.button_add_members)).perform(click());
        // Let time to the view to get loaded
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.edit_text_add_member)).perform(typeText(input));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.image_view_add_member)).perform(click());
        onView(withId(R.id.toolbar_image_right)).perform(click());
    }


    @Test
    public void dateWellComparedYear1() {
        addEventName(EVENT_NAME);
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2099, 5, 5, 5, 5);
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withText(R.string.event_creation_toast_event_end_before_begin)).inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                                                                                        .getWindow()
                                                                                                                        .getDecorView()))))
                                                                              .check(matches(isDisplayed()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            //The tests are stopped, nothing to do.
        }
    }


    @Test
    public void noCreationOnStartDateBeforeEndDate() {
        addEventName(EVENT_NAME);
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2100, 4, 5, 5, 5);
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withText(R.string.event_creation_toast_event_end_before_begin)).inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                                                                                        .getWindow()
                                                                                                                        .getDecorView()))))
                                                                              .check(matches(isDisplayed()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            //The tests are stopped, nothing to do.
        }
    }


    @Test
    public void noEventCreatedOnEmptyEventName() {

        addEventName("");
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withId(R.id.ui_edit_event_name)).check(matches(hasErrorText(getTargetContext().getString(R.string.event_creation_toast_non_empty_event_name))));

        //Remove the error text for further tests
        addEventName(EVENT_NAME);
        onView(withId(R.id.toolbar_image_right)).perform(click());
        Account.shared.clear();
    }


    @Test
    public void noEventCreatedOnTooLongName() {
        addEventName("This event name should be way too long for the event creator to accept it"
                     + "I should not be able to tell my life in the event name");
        setStartDate(2100, 5, 5, 4, 5);
        setEndDate(2100, 5, 5, 5, 5);
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withId(R.id.ui_edit_event_name)).check(matches(hasErrorText(getTargetContext().getString(R.string.event_creation_toast_event_name_too_long))));

        //Remove the error text for further tests
        addEventName(EVENT_NAME);
        onView(withId(R.id.toolbar_image_right)).perform(click());
        Account.shared.clear();
    }


    @Test
    public void noEventCreationOnPastStartDate() {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        int month = now.getMonthOfYear();
        int year = now.getYear() - 1;

        addEventName(EVENT_NAME);
        onView(withId(R.id.button_start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year,
                                                                                                          month,
                                                                                                          day));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.toolbar_image_right)).perform(click());
        onView(withText(R.string.event_creation_toast_event_start_before_now)).inRoot(withDecorView(not(is(mActivityRule.getActivity()
                                                                                                                        .getWindow()
                                                                                                                        .getDecorView()))))
                                                                              .check(matches(isDisplayed()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            //The tests are stopped, nothing to do.
        }
    }


    /**
     * Check if an error message appears if the user enters a string which is not an email address
     *
     * @throws InterruptedException
     */
    @Test
    public void onlyAllowEmailsAsInput() throws InterruptedException {
        addEventName(EVENT_NAME);
        onView(withId(R.id.button_add_members)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.edit_text_add_member)).perform(typeText("Not valid email address"));
        onView(withId(R.id.image_view_add_member)).perform(click());

        onView(withId(R.id.edit_text_add_member)).check(matches(hasErrorText(getTargetContext().getString(R.string.members_adding_error_toast_invalid_email))));
    }


    /**
     * Test QR Scanner
     * Does not work for Jenkins because he does not have a camera
     */
    /*
    @Test
    public void stateRestoredAfterCameraOpened(){
        String eventName = "testEventName";
        // Enter event details
        addEventName(eventName);
        Espresso.closeSoftKeyboard();
        addMember();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button_add_members)).perform(click());
        Espresso.closeSoftKeyboard();
        // Click scan button
        onView(withId(R.id.buttonScanQR)).perform(click());
        // Click back
        Espresso.pressBack();
        onView(withId(R.id.toolbar_image_right)).perform(click());
        // Check event details
        onView(withId(R.id.ui_edit_event_name)).check(matches(withText(eventName)));
        Account.shared.clear();
    }*/
    @Before
    public void setup() {
        Database.setUp();
    }


    /**
     * Tests if an event is well generated when the user enters specified inputs.
     * Important ! The test could fail because of the start and end date and time.
     */
    @Test
    public void testEventWellGenerated() {

        Member emptyMember = new Member(Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<String>empty(),
                                        Optional.<Location>empty());
        List<Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(emptyMember.withUUID(Member.UNKNOWN_USER_ + "1")
                                       .withEmail("swenggroupup@gmail.com"));

        expectedMembers.add(emptyMember.withUUID(Account.shared.getUUID()
                                                               .getOrElse("Default UUID")));

        addEventName(EVENT_NAME);
        addDescription(EVENT_DESCRIPTION);

        LocalDateTime start = new LocalDateTime(2099, 1, 6, 9, 0, 0, 0);
        LocalDateTime end = start.plusDays(4);

        onView(withId(R.id.button_start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(start.getYear(),
                                                                                                          start.getMonthOfYear(),
                                                                                                          start.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_end_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(end.getYear(),
                                                                                                          end.getMonthOfYear(),
                                                                                                          end.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(start.getHourOfDay(),
                                                                                                          start.getMinuteOfHour()));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_end_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(end.getHourOfDay(),
                                                                                                          end.getMinuteOfHour()));
        onView(withId(android.R.id.button1)).perform(click());

        addMember(EVENT_MEMBER);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            //The tests are stopped, nothing to do.
        }

        onView(withId(R.id.toolbar_image_right)).perform(click());

        Event found = findEvent(EVENT_NAME);
        Event expected = new Event(found.getUUID(),
                                   EVENT_NAME,
                                   start,
                                   end,
                                   EVENT_DESCRIPTION,
                                   expectedMembers,
                                   new HashSet<PointOfInterest>(),
                                   false);

        if (!(found.equals(expected))) {
            throw new AssertionError("Expected : " + expected + ".\nFound : " + found);
        }
        Account.shared.clear();
    }


    private void addDescription(String description) {
        onView(withId(R.id.edit_text_description)).perform(clearText());
        onView(withId(R.id.edit_text_description)).perform(typeText(description));
        Espresso.closeSoftKeyboard();
    }


    private Event findEvent(String eventName) {
        List<Event> accountEvents = Account.shared.getFutureEvents();
        Event found = null;
        for (Event e : accountEvents) {
            if (e.getEventName()
                 .equals(eventName)) {
                found = e;
                break;
            }
        }
        return found;
    }
}

