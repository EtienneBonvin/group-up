package ch.epfl.sweng.groupup.activity.event.listing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.activity.event.description.EventDescriptionActivity;
import ch.epfl.sweng.groupup.activity.map.MapActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
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
    private Timer autoUpdate;

    /**
     * Initialization of the private variables of the class and
     * of the future events, create event and past events
     * buttons in the linear layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_listing);
        super.initializeToolbarActivity();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        setContentView(R.layout.activity_event_listing);
                        initializeToolbarActivity();

                        initView();
                    }
                });
            }
        }, 0, 5000); // updates each 5 secs
    }


    @Override
    public void onPause() {
        autoUpdate.cancel();
        super.onPause();
    }

    private void initView() {
        initializeVariables();
        initializeEvents(Account.shared.getFutureEvents(), false);
        initializeCreateEvent();
        List<Event> belowCreateButton = Account.shared.getPastEvents();
        if (!Account.shared.getCurrentEvent().isEmpty()) {
            belowCreateButton.add(Account.shared.getCurrentEvent().get());
        }
        Collections.sort(belowCreateButton, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        initializeEvents(belowCreateButton, true);
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
    private void initializeEvents(List<Event> events, boolean needAnOffset) {
        int offset= needAnOffset ? Account.shared.getFutureEvents().size() : 0;
        for(Event e : events){
            if (e.getInvitation()){
                askForInvitation(e);
            }
            Button eventButton =new Button(this);
            eventButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.
                    MATCH_PARENT, heightInSp));
            eventButton.setText(String.format(Locale.FRANCE, "%s | %d/%d - %d/%d", e.getEventName(),
                    e.getStartTime().getDayOfMonth(), e.getStartTime().getMonthOfYear(),
                    e.getEndTime().getDayOfMonth(), e.getEndTime().getMonthOfYear()));
            final int indexToPass=offset;
            eventButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(EventListingActivity.this, EventDescriptionActivity.class);
                    intent.putExtra("eventIndex", indexToPass);
                    startActivity(intent);
                    return true;
                }
            });
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventListingActivity.this, MapActivity.class);
                    intent.putExtra("eventIndex", indexToPass);
                    startActivity(intent);
                }
            });
            offset++;
            linearLayout.addView(eventButton);
        }
        }

    /**
     * Initialization of the create event button in the linear layout and
     * of the OnClickListener
     */
    private void initializeCreateEvent() {
        Button createEventButton = new Button(this);
        createEventButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.
                MATCH_PARENT, heightInSp));
        createEventButton.setText(R.string.create_new_event);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventListingActivity.this, EventCreationActivity.class);
                startActivity(i);
            }
        });
        //createEventButton.setId(View.generateViewId()); // Assign the ID of the event
        linearLayout.addView(createEventButton);
    }


    /**
     * Create a dialog to invite the user to the event
     * @param eventToDisplay
     */
    private void askForInvitation(final Event eventToDisplay) {
        onPause();
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this);
        String members=getString(R.string.event_invitation_dialog_members);
        for(Member member : eventToDisplay.getEventMembers()){
            members+=member.getDisplayName().getOrElse(getString(R.string.event_invitation_dialog_unknow))+"\n";
        }

        alertDialogBuilder.setTitle(R.string.event_invitation_title);
        alertDialogBuilder.setMessage(getString(R.string.event_invitation_dialog_name) + eventToDisplay.getEventName()+"\n"
        +getString(R.string.event_invitation_dialog_start) + eventToDisplay.getStartTime().toString()+"\n"
        +getString(R.string.event_invitation_dialog_end) + eventToDisplay.getEndTime().toString()+"\n"
        +getString(R.string.event_invitation_dialog_description) + eventToDisplay.getDescription()+"\n"+members);
        alertDialogBuilder
                .setPositiveButton(R.string.event_invitation_dialog_accept,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface,
                                    int i) {
                                Account.shared.addOrUpdateEvent(eventToDisplay.withInvitation(!
                                        eventToDisplay.getInvitation()));
                                Database.update();
                                onResume();
                                dialogInterface.dismiss();
                            }
                        });
        alertDialogBuilder
                .setNegativeButton(R.string.event_invitation_dialog_decline,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface,
                                    int i) {
                                EventDescriptionActivity.removeEvent(eventToDisplay);
                                onResume();
                                dialogInterface.dismiss();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
