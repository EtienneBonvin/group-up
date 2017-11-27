package ch.epfl.sweng.groupup.activity.event.description;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.files.FileManager;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.User;
import ch.epfl.sweng.groupup.object.event.Event;

public class EventDescriptionActivity extends ToolbarActivity implements OnMapReadyCallback {

    private EventDescription eventDescription;
    private FileManager fileManager;

    private GoogleMap mMap;
    private Marker mDefault;
    private Map<String, Marker> mMemberMarkers;

    // Switch view attributes
    private float x1,x2;
    private static final int MIN_DISTANCE = 150;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        super.initializeToolbarActivity();

        x1 = -1;

        Event event = null;

        Intent i = getIntent();
        final int eventIndex = i.getIntExtra(getString(R.string.event_listing_extraIndex), -1);
        if (eventIndex > -1) {
            //!!!Order the events !!!
            event = Account.shared.getEvents().get(eventIndex);
        }

        eventDescription = new EventDescription(this, event);
        fileManager = new FileManager(this, event);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        User.observer = this;
        mMemberMarkers = new HashMap<>();

        // View Switcher
        findViewById(R.id.swipe_bar)
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                if(x1 == -1)
                                    x1 = event.getX();
                                break;

                            case MotionEvent.ACTION_UP:
                                if(x1 != -1) {
                                    x2 = event.getX();
                                    if (Math.abs(x2 - x1) > MIN_DISTANCE) {
                                        if(x2 > x1) {
                                            ((ViewFlipper) findViewById(R.id.view_flipper))
                                                    .showNext();
                                        }else{
                                            ((ViewFlipper) findViewById(R.id.view_flipper))
                                                    .showPrevious();
                                        }
                                    }else{
                                        //Handle click for further uses.
                                        findViewById(R.id.swipe_bar)
                                                .performClick();
                                    }
                                    x1 = -1;
                                }
                                break;
                        }
                        return true;
                    }
                });
    }

    /**
     * Override onPause method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    protected void onPause() {
        super.onPause();
        fileManager.close();
    }

    /**
     * Override onStop method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    public void onStop() {
        super.onStop();
        fileManager.close();
    }

    /**
     * Override onDestroy method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        fileManager.close();
    }

    /**
     * Override of onActivityResult method.
     * Define the behavior when the user finished selecting the picture he wants to add.
     *
     * @param requestCode unused.
     * @param resultCode  indicate if the operation succeeded.
     * @param data        the data returned by the previous activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fileManager.onActivityResult(requestCode, resultCode, data);
    }

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
        if(mMap == null){
            return;
        }
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        if (mDefault == null) {
            mDefault = mMap.addMarker(new MarkerOptions().position(pos).title("You"));
        } else {
            mDefault.setPosition(pos);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }

    public void updateMemberMarkers(String UUID, String displayName, Location location) {
        if(mMap == null){
            return;
        }
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