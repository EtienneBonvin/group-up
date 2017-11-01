package ch.epfl.sweng.groupup.activity.eventDescription;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventListing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

/**
 * Created by alix on 10/26/17.
 */

public class EventDescriptionActivity extends ToolbarActivity {
    LinearLayout linear;
    EditText displayEventName;
    TextView displayEventStartDate;
    TextView displayEventEndDate;
    TextView displayEventMembers;
    EditText displayEventDescription;
    private Event eventToDisplay;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        super.initializeToolbar();
        Intent i= getIntent();
        final int eventIndex = i.getIntExtra("eventIndex", -1);
        if (eventIndex >-1) {
            //!!!Order the events !!!
            eventToDisplay = Account.shared.getEvents().get(eventIndex);
        }
        initializeField();
        printEvent();

        //Remove and go to the event creation
        findViewById(R.id.remove_event_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(EventDescriptionActivity.this, EventListingActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        removeEvent();
                    }
                });
        //Change name
        findViewById(R.id.modifyName)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name= displayEventName.getText().toString();
                        displayEventName.setText(name);
                        Account.shared.addOrUpdateEvent(eventToDisplay.withEventName(name));
                        Database.update();
                        eventToDisplay=Account.shared.getEvents().get(eventIndex);
                    }
                });
        //Change description
        findViewById(R.id.modifyDescription)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String description=displayEventDescription.getText().toString();
                        displayEventDescription.setText(description);
                        Account.shared.addOrUpdateEvent(eventToDisplay.withDescription(description));
                        Database.update();
                        eventToDisplay=Account.shared.getEvents().get(eventIndex);
                    }
                });
    }

    /**
     * Remove the user from the Event
     */
    private void removeEvent() {
        List<Member> futureMembers = new ArrayList<>(eventToDisplay.getEventMembers());
        futureMembers.remove(Account.shared.toMember());
        eventToDisplay=eventToDisplay.withEventMembers(futureMembers);
        Account.shared.addOrUpdateEvent(eventToDisplay);
        Database.update();
        Account.shared.getEvents().remove(eventToDisplay);
        Database.update();

    }

    /**
     * Intilize the different TextView and EditText
     */
    public void initializeField(){
        displayEventName= findViewById(R.id.event_description_name);
        displayEventStartDate= findViewById(R.id.event_description_start_date);
        displayEventEndDate=findViewById(R.id.event_description_end_date);
        displayEventDescription=findViewById(R.id.event_description_description);
        displayEventMembers = findViewById(R.id.event_description_tv_members);
    }

    /**
     * Print the information about the event, if there is no event prints default string in fields.
     */
    public void printEvent() {
        if (eventToDisplay!=null) {
            displayEventName.setText(eventToDisplay.getEventName());
            displayEventStartDate.setText(eventToDisplay.getStartTime().toString(null, Locale.FRANCE));
            displayEventEndDate.setText(eventToDisplay.getEndTime().toString(null, Locale.FRANCE));
            displayEventDescription.setText(eventToDisplay.getDescription());

            for (Member member : eventToDisplay.getEventMembers()) {
                TextView memberName = new TextView(this);
                memberName.setText(member.getDisplayName().getOrElse("NO_NAME"));
                linear = findViewById(R.id.linear_scroll_members);
                linear.addView(memberName);
            }
        }
    }
}
