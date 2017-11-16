package ch.epfl.sweng.groupup.activity.event.description;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

/**
 * Created by alix on 10/26/17.
 */

public class EventDescriptionActivity extends ToolbarActivity {
    private EditText displayEventName;
    private TextView displayEventStartDate;
    private TextView displayEventEndDate;
    private EditText displayEventDescription;
    private Event eventToDisplay;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        int maxName= 50;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        super.initializeToolbarActivity();
        Intent i= getIntent();
        final int eventIndex = i.getIntExtra("eventIndex", -1);
        if (eventIndex >-1) {
            //!!!Order the events !!!
            eventToDisplay = Account.shared.getEvents().get(eventIndex);
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        initializeField();
        printEvent();

        //Remove and go to the event listing
        findViewById(R.id.remove_event_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog alertDialog = new AlertDialog.Builder(EventDescriptionActivity.this).create();
                        alertDialog.setTitle("Deleting Event");
                        alertDialog.setMessage(Html.fromHtml("<font color='#000000'>Would you like to leave and delete this event?</font>"));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continue",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(EventDescriptionActivity.this, EventListingActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        removeEvent();
                                        startActivity(i);
                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
        //Save changes and go to event listing
        findViewById(R.id.save_event_modification_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            String name= displayEventName.getText().toString();
                            String description = displayEventDescription.getText().toString();
                            if (name.length()>50){
                                displayEventName.setError(getString(R.string.event_creation_toast_event_name_too_long));
                            }
                            else if (name.length()==0){
                                displayEventName.setError(getString(R.string.event_creation_toast_non_empty_event_name));
                            }
                            Account.shared.addOrUpdateEvent(eventToDisplay.withEventName(name).withDescription(description));
                            Database.update();
                            eventToDisplay=Account.shared.getEvents().get(eventIndex);

                        Intent i = new Intent(EventDescriptionActivity.this, EventListingActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

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
        List<Event> futureEventList=new ArrayList<>(Account.shared.getEvents());
        Account.shared.withFutureEvents(new ArrayList<Event>()).withPastEvents(new ArrayList<Event>()).withCurrentEvent(Optional.<Event>empty());
        futureEventList.remove(eventToDisplay);
        for (Event fe:futureEventList){
            Account.shared.addOrUpdateEvent(fe);
        }

        Database.update();
    }

    /**
     * Intilize the different TextView and EditText
     */
    private void initializeField(){
        displayEventName= findViewById(R.id.event_description_name);
        displayEventStartDate= findViewById(R.id.event_description_start_date);
        displayEventEndDate=findViewById(R.id.event_description_end_date);
        displayEventDescription=findViewById(R.id.event_description_description);
    }

    /**
     * Print the information about the event, if there is no event prints default string in fields.
     */
    private void printEvent() {
        if (eventToDisplay!=null) {
            displayEventName.setText(eventToDisplay.getEventName());
            displayEventStartDate.setText(eventToDisplay.getStartTime().toString(null, Locale.FRANCE));
            displayEventEndDate.setText(eventToDisplay.getEndTime().toString(null, Locale.FRANCE));
            displayEventDescription.setText(eventToDisplay.getDescription());

            for (Member member : eventToDisplay.getEventMembers()) {
                TextView memberName = new TextView(this);
                memberName.setText(member.getDisplayName().getOrElse("NO_NAME"));
                LinearLayout linear = findViewById(R.id.event_description_linear_scroll_members);
                linear.addView(memberName);
            }
        }
    }
}
