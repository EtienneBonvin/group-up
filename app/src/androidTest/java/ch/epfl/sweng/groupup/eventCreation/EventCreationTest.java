package ch.epfl.sweng.groupup.eventCreation;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.joda.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventCreation.EventCreation;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class EventCreationTest {

    @Rule
    // third parameter is set to true which means the activity is started automatically
    public ActivityTestRule<EventCreation> mActivityRule =
            new ActivityTestRule<>(EventCreation.class, false, true);


    /**
     * Tests if an event is well generated when the user enters specified inputs.
     * Important ! The test could fail because of the start and end date and time.
     */
    @Test
    public void testEventWellGenerated() {

        Database.setUpDatabase();

        Member emptyMember = new Member(Optional.<String>empty(), Optional.<String>empty(), Optional.<String>empty(),
                Optional.<String>empty(), Optional.<String>empty());
        List<Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(emptyMember.withUUID("0"));
        expectedMembers.add(emptyMember.withUUID("1"));
        expectedMembers.add(emptyMember.withUUID("2"));
        expectedMembers.add(emptyMember.withUUID("3"));
        expectedMembers.add(emptyMember.withUUID("4"));
        expectedMembers.add(emptyMember.withUUID("5"));
        expectedMembers.add(emptyMember.withUUID(Account.shared.getUUID().getOrElse("Default UUID")));

        addEventName("My event");

        onView(withId(R.id.button_start_date)).perform(pressBack());

        LocalDateTime start = LocalDateTime.now().plusHours(1).plusMinutes(5);
        LocalDateTime end = LocalDateTime.now().plusHours(1).plusMinutes(6);

        onView(withId(R.id.button_start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(start.getYear(), start.getMonthOfYear(), start.getDayOfYear()));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_end_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(end.getYear(), end.getMonthOfYear(), end.getDayOfYear()));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(start.getHourOfDay(), start.getMinuteOfHour()));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_end_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(end.getHourOfDay(), end.getMinuteOfHour()));
        onView(withId(android.R.id.button1)).perform(click());

        addMembers();
        onView(withId(R.id.save_button)).perform(click());
        Event expected = new Event("My event", start, end, "", expectedMembers);

        Event found = findEvent();

        assert(found.equals(expected));
    }

    @Test
    public void noEventCreatedOnEmptyEventName(){

        addEventName("");
        onView(withId(R.id.save_button)).perform(click());
        assert(findEvent() == null);
    }

    @Test
    public void dateWellComparedYear(){
        addEventName("My event");
        onView(withId(R.id.button_end_date)).perform(pressBack());
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2099, 5, 5, 5, 5);
        onView(withId(R.id.save_button)).perform(click());

        assert(findEvent() == null);
    }

    @Test
    public void dateWellComparedMonth(){
        addEventName("My event");
        onView(withId(R.id.button_end_date)).perform(pressBack());
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2100, 4, 5, 5, 5);
        onView(withId(R.id.save_button)).perform(click());
        assert(findEvent() == null);
    }

    @Test
    public void dateWellComparedDay(){
        addEventName("My event");
        onView(withId(R.id.button_end_date)).perform(pressBack());
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2100, 5, 4, 5, 5);
        onView(withId(R.id.save_button)).perform(click());
        assert(findEvent() == null);
    }

    @Test
    public void dateWellComparedHour(){
        addEventName("My event");
        onView(withId(R.id.button_end_date)).perform(pressBack());
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2100, 5, 5, 4, 5);
        onView(withId(R.id.save_button)).perform(click());
        assert(findEvent() == null);
    }

    @Test
    public void dateWellComparedMinute(){
        addEventName("My event");
        onView(withId(R.id.button_end_date)).perform(pressBack());
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2100, 5, 5, 5, 4);
        onView(withId(R.id.save_button)).perform(click());
        assert(findEvent() == null);
    }

    @Test
    public void atLeastOneMinuteBetweenStartAndEndDate(){
        addEventName("My event");
        onView(withId(R.id.button_end_date)).perform(pressBack());
        setStartDate(2100, 5, 5, 5, 5);
        setEndDate(2100, 5, 5, 5, 5);
        onView(withId(R.id.save_button)).perform(click());
        assert(findEvent() == null);
    }

    @Test
    public void noEventCreationOnPastStartDate(){
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        int month = now.getMonthOfYear();
        int year = now.getYear() - 1;

        addEventName("My event");
        onView(withId(R.id.button_start_date)).perform(pressBack());
        onView(withId(R.id.button_start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.save_button)).perform(click());
        assert(findEvent() == null);
    }

    @Test
    public void noEventCreationOnEndDateBeforeStartDate(){
        addEventName("My event");
        onView(withId(R.id.button_end_date)).perform(pressBack());
        setStartDate(5555, 5, 5, 5, 5);
        setEndDate(5554, 5, 5, 5, 5);
        assert(findEvent() == null);
    }

    private void setStartDate(int year, int month, int day, int hour, int minute){
        onView(withId(R.id.button_start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void setEndDate(int year, int month, int day, int hour, int minute){
        onView(withId(R.id.button_end_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_end_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private Event findEvent(){
        List<Event> accountEvents = Account.shared.getFutureEvents();
        Event found = null;
        for(Event e : accountEvents){
            if(e.getEventName().equals("My event")){
                found = e;
                break;
            }
        }
        return found;
    }

    private void addMembers(){
        onView(withId(R.id.edit_text_add_member)).perform(typeText("0"));
        onView(withId(R.id.image_view_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("1"));
        onView(withId(R.id.image_view_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("2"));
        onView(withId(R.id.image_view_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("3"));
        onView(withId(R.id.image_view_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("4"));
        onView(withId(R.id.image_view_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("5"));
        onView(withId(R.id.image_view_add_member)).perform(click());
    }

    private void addEventName(String name){
        onView(withId(R.id.ui_edit_event_name)).perform(typeText(name));
    }
}

