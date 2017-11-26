package ch.epfl.sweng.groupup.activity.event.description;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.files.FileManagementActivity;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.account.User;
import ch.epfl.sweng.groupup.object.event.Event;

public class EventDescriptionActivity extends ToolbarActivity implements OnMapReadyCallback {

    // Event description attributes
    private EditText displayEventName;
    private TextView displayEventStartDate;
    private TextView displayEventEndDate;
    private EditText displayEventDescription;
    private Event eventToDisplay;

    // Map attributes
    private GoogleMap mMap;
    private Marker mDefault;
    private Map<String, Marker> mMemberMarkers;

    // Switch view attributes
    private float x1,x2;
    private static final int MIN_DISTANCE = 150;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize event description fields
        final int maxName = 50;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        super.initializeToolbarActivity();
        Intent i = getIntent();
        final int eventIndex = i.getIntExtra("eventIndex", -1);
        if (eventIndex > -1) {
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

                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                EventDescriptionActivity.this).create();
                        alertDialog.setTitle(R.string.alert_dialog_title_delete_event);
                        alertDialog.setMessage(Html.fromHtml("<font color='#000000'>Would you " +
                                "like to leave and delete this event?</font>"));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continue",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(EventDescriptionActivity.this,
                                                EventListingActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        removeEvent(eventToDisplay);
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
                        if (name.length()>maxName){
                            displayEventName.setError(getString(R.string.event_creation_toast_event_name_too_long));
                        } else if (name.length() == 0){
                            displayEventName.setError(getString(R.string.event_creation_toast_non_empty_event_name));
                        } else {
                            Account.shared.addOrUpdateEvent(eventToDisplay.withEventName(name).
                                    withDescription(description));
                            Database.update();
                            eventToDisplay = Account.shared.getEvents().get(eventIndex);

                            Intent i = new Intent(EventDescriptionActivity.this,
                                    EventListingActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    }
                });

        // Do we need to store the modifications ?
        findViewById(R.id.upload_file)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),
                                FileManagementActivity.class);
                        intent.putExtra("EventIndex", eventIndex);
                        startActivity(intent);
                    }
                });

        // Initialize map
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        User.observer = this;
        mMemberMarkers = new HashMap<String, Marker>();

        // View Switcher
        findViewById(R.id.go_to_map)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewFlipper) findViewById( R.id.view_flipper ))
                                .showNext();
                    }
                });

        findViewById(R.id.go_to_description)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewFlipper) findViewById( R.id.view_flipper ))
                                .showNext();
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
        displayEventName = findViewById(R.id.event_description_name);
        displayEventStartDate = findViewById(R.id.event_description_start_date);
        displayEventEndDate = findViewById(R.id.event_description_end_date);
        displayEventDescription = findViewById(R.id.event_description_description);
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
            TextView memberName = new TextView(this);
            memberName.setText(member.getDisplayName().getOrElse("NO_NAME"));
            LinearLayout linear = findViewById(R.id.event_description_linear_scroll_members);
            linear.addView(memberName);
        }
    }

    // Map methods

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(false);
            super.initializeToolbarActivity();
        }

        if(!Account.shared.getLocation().isEmpty()){
            updateDefaultMarker(Account.shared.getLocation().get());

        }
    }

    public void updateDefaultMarker(Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        if (mDefault == null) {
            mDefault = mMap.addMarker(new MarkerOptions().position(pos).title("You"));
        } else {
            mDefault.setPosition(pos);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }

    public void updateMemberMarkers(String UUID, String displayName, Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        if(!Account.shared.getUUID().isEmpty() && !UUID.equals(Account.shared.getUUID().get())) {
            if (!mMemberMarkers.containsKey(UUID)) {
                mMemberMarkers.put(UUID, mMap.addMarker(new MarkerOptions().position(pos).title(displayName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));
            } else {
                mMemberMarkers.get(UUID).setPosition(pos);
            }
        }
    }
}