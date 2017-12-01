package ch.epfl.sweng.groupup.activity.event.listing;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.BuildConfig;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.AssertionFailedError;

import org.joda.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventListingActivityTest {

    private Event event;

    @Rule
    public ActivityTestRule<EventListingActivity> mEventListingActivityActivityTestRule =
            new ActivityTestRule<>(EventListingActivity.class);

    @Test
    public void ensureCreateNewEventWork()  {

        // Click on the create_new_event button, when ID is implemented, use it.
        onView(ViewMatchers.withId(R.id.createEventButton))
                .perform(click());

        // Checks that the EventCreationActivity view is displayed by looking for ui_edit_event_name,
        // change to same as Etienne when Solal comes back with an answer.
        try {
            onView(withId(R.id.ui_edit_event_name)).check(matches(isDisplayed()));
            // The ui_edit_event_name is displayed in the view.
        } catch (AssertionFailedError e) {
            // The ui_edit_event_name is not displayed in the view.
        }
    }

    @Test
    public void invitationIsAccepted(){
        Database.setUpDatabase();
        onView(ViewMatchers.withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.ui_edit_event_name)).perform(typeText("CurrentEvent"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_new_event_button)).perform(click());
        //Generate directly a new event with an invitation
        Event eventInvitation= new Event("event invitation", LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),"This is the event to test the invitation process",
                new ArrayList<>(Collections.singletonList(Account.shared.toMember())),true);
        Account.shared.addOrUpdateEvent(eventInvitation);

        //onView(withId(R.id.icon_access_group_list)).perform(click());
        onView(withText("Accept")).perform(click());
        if(BuildConfig.DEBUG&& !(Account.shared.getEvents().size()==2)){
            throw new AssertionError();
        }
        Account.shared.clear();
    }
    @Test
    public void invitationIsDeclinedAndDeleted(){
        Database.setUpDatabase();
        onView(ViewMatchers.withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.ui_edit_event_name)).perform(typeText("CurrentEvent"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_new_event_button)).perform(click());

        Event eventInvitation= new Event("event invitation", LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),"This is the event to test the invitation process",
                new ArrayList<>(Collections.singletonList(Account.shared.toMember())),true);
        Account.shared.addOrUpdateEvent(eventInvitation);

        onView(withId(R.id.icon_access_group_list)).perform(click());
        onView(withText("Decline")).perform(click());
        if(BuildConfig.DEBUG&& !(Account.shared.getEvents().size()==1)){
            throw new AssertionError();
        }
        Account.shared.clear();
    }

    @Test
    public void invitationWithConflictOnAccept(){
        Database.setUpDatabase();
        String newEventName= "New Event";
        onView(ViewMatchers.withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.ui_edit_event_name)).perform(typeText("EventBefore"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_new_event_button)).perform(click());
        Event previousEvent=Account.shared.getEvents().get(0);
        //Generate directly the event with a conflict with the current event,
        // can't create the event from the event creation as this will be blocked
        Event eventInvitation= new Event(newEventName , LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),"This is the event to test the invitation process",
                new ArrayList<>(Collections.singletonList(Account.shared.toMember())),true);
        Account.shared.addOrUpdateEvent(eventInvitation);

        onView(withId(R.id.icon_access_group_list)).perform(click());
        onView(withText("Accept")).perform(click());

        if(BuildConfig.DEBUG && !(Account.shared.getEvents().contains(eventInvitation) && !(Account.shared.getEvents().contains(previousEvent)))){
            throw new AssertionError();
        }
        onView(withId(R.id.icon_access_group_list)).perform(click());

        Account.shared.clear();
    }
}

