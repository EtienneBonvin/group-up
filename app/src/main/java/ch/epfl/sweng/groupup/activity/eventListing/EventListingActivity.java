package ch.epfl.sweng.groupup.activity.eventListing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.joda.time.LocalDateTime;


import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventCreation.EventCreation;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;

/**
 * EventListing class
 * Lists the future and past events and in between a create
 * event button for the user to create a new event.
 * It is linked to the layout activity_event_listing.xml
 */

public class EventListingActivity extends ToolbarActivity {

    private LinearLayout linearLayout;
    private int heightInSp;

    /**
     * Initialization of the private variables of the class and
     * of the future events, create event and past events
     * buttons in the linear layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_listing);
        super.initializeToolbar();

        initializeVariables();
        initializeEvents(Account.shared.getFutureEvents());
        initializeCreateEvent();
        initializeEvents(Account.shared.getPastEvents());
    }

    /**
     * Initialization of the private variables of the class
     */
    private void initializeVariables() {
        linearLayout = findViewById(R.id.linear_layout_event_list);
        heightInSp = Math.round(100 * getResources().getDisplayMetrics().scaledDensity);
        // Fixed height, best would be to create a dynamical height so it works for all screens
    }

    /**
     * Initialization of the event buttons in the linear layout with the
     * name and start to event dates stated
     */
    private void initializeEvents(List<Event> events) {
        String[] eventNames = getEventNames(events);
        LocalDateTime[] eventStartTimes = getEventStartTimes(events);
        LocalDateTime[] eventEndTimes = getEventEndTimes(events);

        for(int i=0; i<events.size(); i++){
            Button eventButton = new Button(this);
            eventButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    heightInSp));
            /*eventButton.setText(eventNames[i] + " | " + Integer.toString(eventStartTimes[i].
                    getDayOfMonth()) + "/" + Integer.toString(eventStartTimes[i].getMonthOfYear()) +
                    " - " + Integer.toString(eventEndTimes[i].getDayOfMonth()) + "/" +
                    Integer.toString(eventEndTimes[i].getDayOfMonth()));*/
            eventButton.setText(String.format(Locale.FRANCE, "%s | %d/%d - %d/%d",eventNames[i],
                    eventStartTimes[i].getDayOfMonth(),eventStartTimes[i].getMonthOfYear(),
                    eventEndTimes[i].getDayOfMonth(), eventEndTimes[i].getDayOfMonth()));
            //eventButton.setId(View.generateViewId()); // Assign the ID of the event
            linearLayout.addView(eventButton);
        }
    }

    /**
     * Initialization of the create event button in the linear layout and
     * of the OnClickListener
     */
    private void initializeCreateEvent() {
        Button createEventButton = new Button(this);
        createEventButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                heightInSp));
        createEventButton.setText(R.string.create_new_event);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventListingActivity.this, EventCreation.class);
                startActivity(i);
            }
        });
        //createEventButton.setId(View.generateViewId()); // Assign the ID of the event
        linearLayout.addView(createEventButton);
    }

    /**
     * Getter for the event names of a list of events.
     * @param events a list of events
     * @return       a list of the event names strings
     */
    private String[] getEventNames(List<Event> events) {
        String[] eventNames = new String[events.size()];
        for (int i=0; i < events.size(); i++) {
            eventNames[i] = events.get(i).getEventName();
        }
        return eventNames;
    }

    /**
     * Getter for the start times of a list of events.
     * @param events a list of events
     * @return       a LocalDateTime list of the start times
     */
    private LocalDateTime[] getEventStartTimes(List<Event> events) {
        LocalDateTime[] eventStartTimes = new LocalDateTime[events.size()];
        for (int i=0; i < events.size(); i++) {
            eventStartTimes[i] = events.get(i).getStartTime();
        }
        return eventStartTimes;
    }

    /**
     * Getter for the start times of a list of events.
     * @param events a list of events
     * @return       a LocalDateTime list of the end times
     */
    private LocalDateTime[] getEventEndTimes(List<Event> events) {
        LocalDateTime[] eventEndTimes = new LocalDateTime[events.size()];
        for (int i=0; i < events.size(); i++) {
            eventEndTimes[i] = events.get(i).getEndTime();
        }
        return eventEndTimes;
    }
}
