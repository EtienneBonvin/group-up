package ch.epfl.sweng.groupup.activity.event.description;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.files.FileManagementActivity;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

public class EventDescription {

    private final EventDescriptionActivity activity;

    // Event description attributes
    private EditText displayEventName;
    private TextView displayEventStartDate;
    private TextView displayEventEndDate;
    private EditText displayEventDescription;
    private Event eventToDisplay;

    public EventDescription(final EventDescriptionActivity activity){

        this.activity = activity;

        // Initialize event description fields
        final int maxName = 50;
        Intent i = activity.getIntent();
        final int eventIndex = i.getIntExtra("eventIndex", -1);
        if (eventIndex > -1) {
            //!!!Order the events !!!
            eventToDisplay = Account.shared.getEvents().get(eventIndex);
        }
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        initializeField();
        printEvent();

        //Remove and go to the event listing
        activity.findViewById(R.id.remove_event_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                activity.getApplicationContext()).create();
                        alertDialog.setTitle(R.string.alert_dialog_title_delete_event);
                        alertDialog.setMessage(Html.fromHtml("<font color='#000000'>Would you " +
                                "like to leave and delete this event?</font>"));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continue",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(activity.getApplicationContext(),
                                                EventListingActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        removeEvent(eventToDisplay);
                                        activity.startActivity(i);
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
        activity.findViewById(R.id.save_event_modification_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name= displayEventName.getText().toString();
                        String description = displayEventDescription.getText().toString();
                        if (name.length()>maxName){
                            displayEventName.setError(
                                    activity.getString(R.string.event_creation_toast_event_name_too_long));
                        } else if (name.length() == 0){
                            displayEventName.setError(
                                    activity.getString(R.string.event_creation_toast_non_empty_event_name));
                        } else {
                            Account.shared.addOrUpdateEvent(eventToDisplay.withEventName(name).
                                    withDescription(description));
                            Database.update();
                            eventToDisplay = Account.shared.getEvents().get(eventIndex);

                            Intent i = new Intent(activity.getApplicationContext(),
                                    EventListingActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(i);
                        }
                    }
                });

        // Do we need to store the modifications ?
        activity.findViewById(R.id.upload_file)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity.getApplicationContext(),
                                FileManagementActivity.class);
                        intent.putExtra("EventIndex", eventIndex);
                        activity.startActivity(intent);
                    }
                });
    }

    // Event description methods.

    /**
     * Remove the user from the Event
     */
    public static void removeEvent(Event eventToRemove) {
        List<Member> futureMembers = new ArrayList<>(eventToRemove.getEventMembers());
        futureMembers.remove(Account.shared.toMember());
        eventToRemove = eventToRemove.withEventMembers(futureMembers);
        Account.shared.addOrUpdateEvent(eventToRemove);
        Database.update();
        List<Event> futureEventList = new ArrayList<>(Account.shared.getEvents());
        Account.shared.withFutureEvents(new ArrayList<Event>()).withPastEvents(new ArrayList<Event>
                ()).withCurrentEvent(Optional.<Event>empty());
        futureEventList.remove(eventToRemove);
        for (Event fe : futureEventList) {
            Account.shared.addOrUpdateEvent(fe);
        }

        Database.update();
    }

    /**
     * Intilize the different TextView and EditText
     */
    private void initializeField() {
        displayEventName = activity.findViewById(R.id.event_description_name);
        displayEventStartDate = activity.findViewById(R.id.event_description_start_date);
        displayEventEndDate = activity.findViewById(R.id.event_description_end_date);
        displayEventDescription = activity.findViewById(R.id.event_description_description);
    }

    /**
     * Print the information about the event, if there is no event prints default string in fields.
     */
    private void printEvent() {
        displayEventName.setText(eventToDisplay.getEventName());
        displayEventStartDate.setText(eventToDisplay.getStartTimeToString());
        displayEventEndDate.setText(eventToDisplay.getEndTimeToString());

        displayEventDescription.setText(eventToDisplay.getDescription());

        for (Member member : eventToDisplay.getEventMembers()) {
            TextView memberName = new TextView(activity);
            memberName.setText(member.getDisplayName().getOrElse("NO_NAME"));
            LinearLayout linear = activity.findViewById(R.id.event_description_linear_scroll_members);
            linear.addView(memberName);
        }
    }

}
