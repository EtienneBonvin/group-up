package ch.epfl.sweng.groupup.activity.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

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
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.User;

public class MapActivity extends ToolbarActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mDefault;
    private Map<String, Marker> mMemberMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        super.initializeToolbarActivity();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //User.observer = this;
        mMemberMarkers = new HashMap<String, Marker>();
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
