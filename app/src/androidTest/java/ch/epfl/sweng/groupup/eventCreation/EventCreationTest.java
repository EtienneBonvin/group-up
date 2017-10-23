package ch.epfl.sweng.groupup.eventCreation;

import android.support.test.rule.ActivityTestRule;

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
import static android.support.test.espresso.action.ViewActions.typeText;
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

        onView(withId(R.id.ui_edit_event_name)).perform(typeText("My event"));
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
        onView(withId(R.id.save_button)).perform(click());

        List<Event> accountEvents = Account.shared.getFutureEvents();
        Event found = new Event("", LocalDateTime.now(), LocalDateTime.now(), "",
                new ArrayList<Member>());

        LocalDateTime start = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end = LocalDateTime.now().plusMinutes(6);
        Event expected = new Event("My event", start, end, "", expectedMembers);

        for(Event e : accountEvents){
            if(e.getEventName().equals("My event")){
                found = e;
                break;
            }
        }

        assert(found.equals(expected));
    }
}

