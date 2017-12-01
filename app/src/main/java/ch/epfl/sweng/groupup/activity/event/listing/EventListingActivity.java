package ch.epfl.sweng.groupup.activity.event.listing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.activity.event.description.EventDescription;
import ch.epfl.sweng.groupup.activity.event.description.EventDescriptionActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.Watcher;
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

public class EventListingActivity extends ToolbarActivity implements Watcher {

    private LinearLayout linearLayout;
    private int heightInSp;
    private boolean dialogShown;


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

        Account.shared.addWatcher(this);
        initializeVariables();
        updateEvents();
        initializeCreateEvent();
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
    private void updateEvents() {
        linearLayout.removeAllViews();
        List<Event> events = Account.shared.getEvents();

        int index = 0;
        for (Event e : events) {
            if (e.getInvitation()) {
                eventsToDisplay.add(e);
            }
        }

        askForInvitation();

        for (Event e : events) {
            Button eventButton = new Button(this);
            eventButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.
                    MATCH_PARENT, heightInSp));
            eventButton.setText(String.format(Locale.getDefault(), "%s | %d/%d - %d/%d", e.getEventName(),
                    e.getStartTime().getDayOfMonth(), e.getStartTime().getMonthOfYear(),
                    e.getEndTime().getDayOfMonth(), e.getEndTime().getMonthOfYear()));
            eventButton.setBackgroundColor(getResources().getColor(R.color.primaryLightColor));
            eventButton.setCompoundDrawablePadding(2);

            final int i = index;
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventListingActivity.this, EventDescriptionActivity.class);
                    intent.putExtra(getString(R.string.event_listing_extraIndex), i);

                    startActivity(intent);
                }
            });
            index++;
            linearLayout.addView(eventButton);
        }
    }

    /**
     * Initialization of the create event button in the linear layout and
     * of the OnClickListener
     */
    private void initializeCreateEvent() {
        FloatingActionButton createEventButton = this.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventListingActivity.this, EventCreationActivity.class);
                startActivity(i);
            }
        });
    }


    /**
     * Create a dialog to invite the user to the event
     */
    private void askForInvitation() {
        for (final Event eventToDisplay : eventsToDisplay) {
            List<Event> eventsToRemove = new ArrayList<>();
            if (!dialogShown) {
                dialogShown = true;

                String message = "";
                String names = "";

                for(Event e : Account.shared.getFutureEvents()){
                    if (e.overlap(Optional.from(eventToDisplay)) && !e.getUUID().equals(eventToDisplay.getUUID())){
                        eventsToRemove.add(e);
                        names += e.getEventName()+" ";
                    }
                }

                if (eventToDisplay.overlap(Account.shared.getCurrentEvent())){
                    eventsToRemove.add(Account.shared.getCurrentEvent().get());
                }

                final List<Event> eventsToRemoveFinal = Collections.unmodifiableList(eventsToRemove);

                String members = getString(R.string.event_invitation_dialog_members);
                for (Member member : eventToDisplay.getEventMembers()) {
                    members += member.getDisplayName().getOrElse(getString(R.string.event_invitation_dialog_unknown)) + "\n";
                }

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(new ContextThemeWrapper(EventListingActivity.this, R.style.AboutDialog));
                alertDialogBuilder.setTitle(R.string.event_invitation_title);

                if(!eventsToRemove.isEmpty()){
                    message = getString(R.string.overlapping_events) + names + "\n";
                }
                message= message + getString(R.string.event_invitation_dialog_name) + eventToDisplay.getEventName() + "\n"
                        + getString(R.string.event_invitation_dialog_start) + eventToDisplay.getStartTimeToString() + "\n"
                        + getString(R.string.event_invitation_dialog_end) + eventToDisplay.getEndTimeToString() + "\n"
                        + getString(R.string.event_invitation_dialog_description) + eventToDisplay.getDescription() + "\n" + members;
                alertDialogBuilder.setMessage(message);

                alertDialogBuilder
                        .setPositiveButton(R.string.event_invitation_dialog_accept,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            int i) {
                                        Account.shared.addOrUpdateEvent(eventToDisplay.withInvitation(
                                               !eventToDisplay.getInvitation()));
                                        eventsToDisplay.remove(eventToDisplay);
                                        for (Event e : eventsToRemoveFinal){
                                            EventDescriptionActivity.removeEvent(e);
                                        }
                                        dialogShown = false;
                                        dialogInterface.dismiss();
                                        recreate();
                                    }
                                });

                alertDialogBuilder
                        .setNegativeButton(R.string.event_invitation_dialog_decline,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            int i) {
                                        EventDescription.removeEvent(eventToDisplay);
                                        eventsToDisplay.remove(eventToDisplay);
                                        dialogShown = false;
                                        dialogInterface.dismiss();
                                        recreate();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    @Override
    public void notifyWatcher() {
        updateEvents();
    }

    @Override
    public void onStop(){
        super.onStop();
        Account.shared.removeWatcher(this);
    }
}