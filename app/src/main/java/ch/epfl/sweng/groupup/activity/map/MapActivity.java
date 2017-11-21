package ch.epfl.sweng.groupup.activity.map;

import android.location.Location;
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
    private Map<String,Marker> mMemberMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        super.initializeToolbarActivity();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        User.observer = this;
        mMemberMarkers = new HashMap<String,Marker>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

    public void updateMemberMarkers(User user, Location location) {
        //LatLng pos = new LatLng(location.getLatitude() - 5, location.getLongitude() - 5);
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

        if (!mMemberMarkers.containsKey(user.getUUID().get())) {
            mMemberMarkers.put(user.getUUID().get(), mMap.addMarker(new MarkerOptions().position(pos).title(user.getDisplayName().get()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));
        } else {
            mMemberMarkers.get(user.getUUID().get()).setPosition(pos);
        }
    }
}
