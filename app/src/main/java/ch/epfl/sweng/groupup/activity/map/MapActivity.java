package ch.epfl.sweng.groupup.activity.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.User;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;

public class MapActivity extends ToolbarActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Event currentEvent;
    private Marker mDefault;
    private Map<String, Marker> mMemberMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        super.initializeToolbarActivity();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        int
                eventIndex =
                intent.getIntExtra(getString(R.string.event_listing_extraIndex),
                                   -1);
        if (eventIndex != -1) {
            currentEvent = Account.shared.getEvents().get(eventIndex);
        } else {
            throw new Error("no event was passed down the the map " +
                            "activity");
        }

        User.observer = this;
        mMemberMarkers = new HashMap<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (!(ActivityCompat.checkSelfPermission(this,
                                                 Manifest.permission.ACCESS_FINE_LOCATION) !=
              PackageManager.PERMISSION_GRANTED &&
              ActivityCompat.checkSelfPermission(this,
                                                 Manifest.permission.ACCESS_COARSE_LOCATION) !=
              PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(false);
            super.initializeToolbarActivity();
        }

        if (!Account.shared.getLocation().isEmpty()) {
            updateDefaultMarker(Account.shared.getLocation().get());

        }
    }

    public void updateDefaultMarker(Location location) {
        LatLng
                pos =
                new LatLng(location.getLatitude(), location.getLongitude());
        if (mDefault == null) {
            mDefault =
                    mMap.addMarker(new MarkerOptions().position(pos)
                                           .title("You"));
        } else {
            mDefault.setPosition(pos);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }

    public void updateMemberMarkers(String UUID,
                                    String displayName,
                                    Location location) {
        LatLng
                pos =
                new LatLng(location.getLatitude(), location.getLongitude());
        if (!Account.shared.getUUID().isEmpty() &&
            !UUID.equals(Account.shared.getUUID().get())) {
            if (!mMemberMarkers.containsKey(UUID)) {
                mMemberMarkers.put(UUID,
                                   mMap.addMarker(new MarkerOptions().position(
                                           pos)
                                                          .title(displayName)
                                                          .icon(BitmapDescriptorFactory
                                                                        .defaultMarker(
                                                                                BitmapDescriptorFactory.HUE_ORANGE))));
            } else {
                mMemberMarkers.get(UUID).setPosition(pos);
            }
        }

        mMap.setOnMapLongClickListener(getMapLongClickListener());
        mMap.setOnMarkerDragListener(getMarkerDragListener());

        for (PointOfInterest poi : currentEvent.getPointsOfInterest()) {
            LatLng latLng = new LatLng(poi.getLocation().getLatitude(),
                                       poi.getLocation().getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng)
                                   .title(poi.getName())
                                   .snippet(poi.getDescription())
                                   .draggable(true)
                                   .icon(BitmapDescriptorFactory
                                                 .defaultMarker(
                                                         BitmapDescriptorFactory.HUE_GREEN)));
        }
    }

    private GoogleMap.OnMapLongClickListener getMapLongClickListener() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                Context context = MapActivity.this;

                // Dialog Builder
                final AlertDialog.Builder
                        builder =
                        new AlertDialog.Builder(context);
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

                builder.setPositiveButton("Add",
                                          getCreatePositiveListener(latLng,
                                                                    titleEditText,
                                                                    descriptionEditText));

                builder.setNegativeButton("Cancel",
                                          getNegativeListener());

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

                Location location = new
                        Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .snippet(description)
                        .icon(BitmapDescriptorFactory
                                      .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(markerOptions);

                PointOfInterest poi = new PointOfInterest(title,
                                                          description,
                                                          location);

                Event newEvent = currentEvent.withPointOfInterest(poi);

                Account.shared.addOrUpdateEvent(newEvent);
                currentEvent = newEvent;
                Database.update();
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
                Context context = MapActivity.this;

                // Dialog Builder
                final AlertDialog.Builder
                        builder =
                        new AlertDialog.Builder(context);
                builder.setTitle(R.string.poi_remove_title);

                builder.setPositiveButton(R.string.poi_remove_positive,
                                          getRemovePositiveListener(marker));

                builder.setNegativeButton(R.string.poi_remove_negative,
                                          getNegativeListener());

                builder.create().show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // Ignore
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // Ignore
            }
        };
    }

    private DialogInterface.OnClickListener getRemovePositiveListener(final Marker marker) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Location markersLocation =
                        new Location(LocationManager.GPS_PROVIDER);
                markersLocation.setLatitude(marker.getPosition().latitude);
                markersLocation.setLongitude(marker.getPosition().longitude);

                String uuidToDelete = "";

                float minDistance = Float.MAX_VALUE;
                for (PointOfInterest poi : currentEvent.getPointsOfInterest()) {
                    Location location = poi.getLocation();

                    if (location.distanceTo(markersLocation) < minDistance) {
                        uuidToDelete = poi.getUuid();
                    }
                }

                Set<PointOfInterest> newPointsOfInterest = new HashSet<>();
                for (PointOfInterest poi : currentEvent.getPointsOfInterest()) {
                    if (!poi.getUuid().equals(uuidToDelete)) {
                        newPointsOfInterest.add(poi);
                    }
                }

                Event
                        newEvent =
                        currentEvent.withPointsOfInterest(newPointsOfInterest);

                Account.shared.addOrUpdateEvent(newEvent);
                currentEvent = newEvent;
                marker.setVisible(false);

                Database.update();
            }
        };
    }

}
