package ch.epfl.sweng.groupup.activity.eventListing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventCreation.eventCreation;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;

public class EventListingActivity extends AppCompatActivity {

    private String[] futureEventsEx = {"Future Event 1", "Future Event 2", "Future Event 3"};
    private String[] pastEventsEx = {"Past Event 1", "Past Event 2", "Past Event 3"};
    private LinearLayout linearLayout;
    private int heightInSp;

    private List<Event> futureEvents;
    private List<Event> pastEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_listing);

        initializeVariables();
        /*initializeEvents(futureEventsEx);
        initializeCreateEvent();
        initializeEvents(pastEventsEx);*/
        initializeEvents(getEventNames(futureEvents));
        initializeCreateEvent();
        initializeEvents(getEventNames(pastEvents));
    }

    private void initializeVariables() {
        linearLayout = (LinearLayout)findViewById(R.id.linear_layout_event_list);
        heightInSp = Math.round(100 * getResources().getDisplayMetrics().scaledDensity);
        // Fixed height, best would be to create a dynamical height so it works for all screens

        futureEvents = Account.shared.getFutureEvents();
        pastEvents = Account.shared.getPastEvents();
    }

    private void initializeEvents(String[] events) {
        for(int i=0; i<events.length; i++){
            Button eventButton = new Button(this);
            eventButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    heightInSp));
            eventButton.setText(events[i]);
            //eventButton.setId(View.generateViewId()); // Assign the ID of the event
            linearLayout.addView(eventButton);
        }
    }

    private void initializeCreateEvent() {
        Button creatEventButton = new Button(this);
        creatEventButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                heightInSp));
        creatEventButton.setText(R.string.create_new_event);
        creatEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventListingActivity.this, eventCreation.class);
                startActivity(i);
            }
        });
        //creatEventButton.setId(View.generateViewId()); // Assign the ID of the event
        linearLayout.addView(creatEventButton);
    }

    private String[] getEventNames(List<Event> events) {
        String[] eventNames = new String[events.size()];
        for (int i=0; i < events.size(); i++) {
            eventNames[i] = events.get(i).getEventName();
        }
        return eventNames;
    }
}
