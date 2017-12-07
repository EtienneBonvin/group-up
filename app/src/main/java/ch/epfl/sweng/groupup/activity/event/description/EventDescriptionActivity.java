package ch.epfl.sweng.groupup.activity.event.description;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.files.FileManager;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.AndroidHelper;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.account.User;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;

/**
 * EventDescriptionActivity.
 * This activity gathers the description of an event, its map and its file management.
 */
public class EventDescriptionActivity extends ToolbarActivity implements OnMapReadyCallback {

    private static boolean swipeBarTouched;

    private FileManager fileManager;

    private GoogleMap mMap;
    private Event currentEvent;
    private Map<Marker, String> mPoiMarkers;

    // Switch view attributes
    private float x1,x2;
    private static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        super.initializeToolbarActivity();

        swipeBarTouched = false;

        x1 = -1;

        new EventDescription(this);
        fileManager = new FileManager(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        User.observer = this;
        mPoiMarkers = new HashMap<>();

        // View Switcher
        findViewById(R.id.swipe_bar)
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        swipeBarTouched = true;

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
     * Remove the user from the Event
     * TODO change the place of this method, it doesn't make much sense to have it here
     */
    public static void removeEvent(Event eventToRemove) {
        List<Member> futureMembers = new ArrayList<>(eventToRemove.getEventMembers());
        futureMembers.remove(Account.shared.toMember());
        eventToRemove = eventToRemove.withEventMembers(futureMembers);
        Account.shared.addOrUpdateEvent(eventToRemove);
        Database.update();
        List<Event> futureEventList = new ArrayList<>(Account.shared.getEvents());
        Account.shared.withFutureEvents(new ArrayList<Event>()).withPastEvents(new ArrayList<Event>
                ());
        Log.d("FUTUREEVENTBEFORE", futureEventList.toString());
        futureEventList.remove(eventToRemove);
        Log.d("FUTUREEVENTAFETERREMOVE", futureEventList.toString());
        for (Event fe : futureEventList) {
            Account.shared.addOrUpdateEvent(fe);
        }
        Database.update();
    }

    public void onStop() {
        super.onStop();
        fileManager.close();
    }

    /*
     * Override onDestroy method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    public void onDestroy() {
        super.onDestroy();
        fileManager.close();
    }

    /**
     * Override of onActivityResult method.
     * Define the behavior when the user finished selecting the picture he wants to add or taking
     * a picture.
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

    /**
     * Defines the behavior of the activity when the Google map is ready.
     * @param googleMap the Google map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (isMapMockWanted()) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }

        mMap.setOnMapLoadedCallback(getOnMapLoadedCallback(this));
        mMap.setOnMapLongClickListener(getMapLongClickListener());
        mMap.setOnMarkerDragListener(getMarkerDragListener());

        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission
                        .ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);
        }

        Intent intent = getIntent();
        int eventIndex = intent.getIntExtra(getString(R.string.event_listing_extraIndex), -1);
        if (eventIndex != -1) {
            currentEvent = Account.shared.getEvents().get(eventIndex);
            updateEventIfNeeded(currentEvent);
        } else {
            throw new Error("no event was passed down to the map activity");
        }

        super.provideGeoLocation();
    }

    public void requestLocation() {
        super.provideGeoLocation();
    }

    public void updateEventIfNeeded(Event event) {
        if (mMap != null && event.getUUID().equals(currentEvent.getUUID())) {
            currentEvent = event;

            mMap.clear();
            updateMemberMarkers();
            updatePoiMarkers();
        }
    }

    private void updateMemberMarkers() {
        for (Member memberToDisplay : currentEvent.getEventMembers()) {
            Optional<Location> location = memberToDisplay.getLocation();

            if (!location.isEmpty() && !memberToDisplay.getUUID().isEmpty()) {
                LatLng pos = new LatLng(location.get().getLatitude(), location.get().getLongitude());
                String uuid = memberToDisplay.getUUID().get();
                String displayName = memberToDisplay.getDisplayName().getOrElse("NO_NAME");

                if (!Account.shared.getUUID().isEmpty() && !uuid.equals(Account.shared.getUUID().get())) {
                    mMap.addMarker(new MarkerOptions().position(pos)
                            .title(displayName)
                            .icon(BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_ORANGE)));
                }
            }
        }
    }


    private void updatePoiMarkers() {
        for (PointOfInterest poi : currentEvent.getPointsOfInterest()) {
            LatLng latLng = new LatLng(poi.getLocation().getLatitude(), poi.getLocation().getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(poi.getName())
                    .snippet(poi.getDescription())
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN)));
            marker.setVisible(true);

            mPoiMarkers.put(marker, poi.getUuid());
        }
    }


    private GoogleMap.OnMapLoadedCallback getOnMapLoadedCallback(final Context context) {
        return new OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (swipeBarTouched) {
                    AndroidHelper.showToast(context, getString(R.string.map_activity_poi_instruction), Toast.LENGTH_LONG);
                    mMap.setOnMapLoadedCallback(null);
                }
            }
        };
    }


    private GoogleMap.OnMapLongClickListener getMapLongClickListener() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                Context context = EventDescriptionActivity.this;

                // Dialog Builder
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AboutDialog));
                builder.setTitle(R.string.poi_dialog_title);

                // Container + Child Views to enable input from the user.
                LinearLayout container = new LinearLayout(context);
                container.setOrientation(LinearLayout.VERTICAL);

                final EditText titleEditText = new EditText(context);
                titleEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                titleEditText.setHint(R.string.poi_title_hint);
                container.addView(titleEditText);

                final EditText descriptionEditText = new EditText(context);
                descriptionEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                descriptionEditText.setHint(R.string.poi_description_hint);
                container.addView(descriptionEditText);

                builder.setView(container);

                builder.setPositiveButton(R.string.poi_create_add,
                        getCreatePositiveListener(latLng, titleEditText, descriptionEditText));
                builder.setNegativeButton(R.string.poi_create_cancel, getNegativeListener());

                builder.create().show();
            }
        };
    }


    private DialogInterface.OnClickListener getCreatePositiveListener(final LatLng latLng,
                                                                      final EditText titleEditText,
                                                                      final EditText descriptionEditText) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);

                Account.shared.addOrUpdateEvent(currentEvent.withPointOfInterest(new PointOfInterest(title, description, location)));
                Database.update();

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        };
    }


    private DialogInterface.OnClickListener getNegativeListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
    }


    private GoogleMap.OnMarkerDragListener getMarkerDragListener() {
        return new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Context context = EventDescriptionActivity.this;

                // Dialog Builder
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AboutDialog));
                builder.setTitle(R.string.poi_remove_title);

                builder.setPositiveButton(R.string.poi_remove_positive, getRemovePositiveListener(marker));
                builder.setNegativeButton(R.string.poi_remove_negative, getNegativeListener());

                builder.create().show();
            }


            @Override
            public void onMarkerDrag(Marker marker) {
                // Ignore
            }


            @Override
            public void onMarkerDragEnd(Marker marker) {
                /*
                This needs to be done because of a "bug" of the Google Maps, when you start dragging a marker it gets
                 automatically deviated a little bit.
                 */
                marker.remove();
                updatePoiMarkers();
            }
        };
    }


    private DialogInterface.OnClickListener getRemovePositiveListener(final Marker marker) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String uuidToDelete = mPoiMarkers.get(marker);
                if (uuidToDelete == null) {
                    return;
                }

                Set<PointOfInterest> newPointsOfInterest = new HashSet<>();
                for (PointOfInterest poi : currentEvent.getPointsOfInterest()) {
                    if (!poi.getUuid().equals(uuidToDelete)) {
                        newPointsOfInterest.add(poi);
                    }
                }

                Account.shared.addOrUpdateEvent(currentEvent.withPointsOfInterest(newPointsOfInterest));
                marker.remove();

                Database.update();
            }
        };
    }
}
