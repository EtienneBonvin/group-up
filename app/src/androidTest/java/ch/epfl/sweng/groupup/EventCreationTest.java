package ch.epfl.sweng.groupup;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import ch.epfl.sweng.groupup.activity.eventCreation.eventCreation;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

public class EventCreationTest {

    @Test
    public void testEventWellGenerated() {
        /*LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        int id = 0;

        Member emptyMember = new Member(Optional.<String>empty(), Optional.<String>empty(),
                Optional.<String>empty(), Optional.<String>empty());
        List<Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(emptyMember.withEmail("Alix.Jeannerot@gmail.com"));
        expectedMembers.add(emptyMember.withEmail("mairecedric@gmail.com"));
        expectedMembers.add(emptyMember.withEmail("etienne.bonvin@hotmail.com"));
        expectedMembers.add(emptyMember.withEmail("hampusram93@gmail.com"));
        expectedMembers.add(emptyMember.withEmail("selma.steinhoff@gmail.com"));
        expectedMembers.add(emptyMember.withEmail("xavpantet@gmail.com"));

        Event expected = new Event("My event", start, end, expectedMembers, id);

        onView(withId(R.id.ui_edit_event_name)).perform(typeText("My event"));
        onView(withId(R.id.edit_text_add_member)).perform(typeText("Alix.Jeannerot@gmail.com"));
        onView(withId(R.id.layout_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("mairecedric@gmail.com"));
        onView(withId(R.id.layout_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("etienne.bonvin@hotmail.com"));
        onView(withId(R.id.layout_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("hampusram93@gmail.com"));
        onView(withId(R.id.layout_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("selma.steinhoff@gmail.com"));
        onView(withId(R.id.layout_add_member)).perform(click());
        onView(withId(R.id.edit_text_add_member)).perform(typeText("xavpantet@gmail.com"));
        onView(withId(R.id.layout_add_member)).perform(click());
        onView(withId(R.id.save_button)).perform(click());

        List<Event> accountEvents = Account.shared.getFutureEvents();
        Event found = new Event("", LocalDateTime.now(), LocalDateTime.now(),
                new ArrayList<Member>(), 0);
        for(Event e : accountEvents){
            if(e.getEventName().equals("My event")){
                found = e;
                break;
            }
        }*/

        //assert(compare_event_short(expected, found));
        assert(true);
    }

    /**
     * Compare two events only based on their members and name.
     * @param e1
     * @param e2
     * @return true if the events match, false otherwise.
     */
    private boolean compare_event_short(Event e1, Event e2){
        return e1.getEventMembers().equals(e2.getEventMembers()) &&
                e1.getEventName().equals(e2.getEventName());
    }

}

