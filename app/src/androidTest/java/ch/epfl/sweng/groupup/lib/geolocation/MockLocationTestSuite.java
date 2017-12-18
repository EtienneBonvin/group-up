package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class MockLocationTestSuite {

    @Rule
    public final ActivityTestRule<EventListingActivity> mActivityRule = new ActivityTestRule<>(EventListingActivity.class);


    @Test
    public void handlesAllStatusChanges() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.onStatusChanged(null, LocationProvider.OUT_OF_SERVICE, null);
        myTestLocation.onStatusChanged(null, LocationProvider.TEMPORARILY_UNAVAILABLE, null);
        myTestLocation.onStatusChanged(null, LocationProvider.AVAILABLE, null);
    }


    @Test
    public void handlesOnProviderEnabled() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.onProviderEnabled("MY_PROVIDER");
    }


    @Test
    public void handlesOnProviderDisabled() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.onProviderDisabled("MY_PROVIDER");
    }


    @Test
    public void canRequestLocationUpdates() throws Throwable {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.requestLocationUpdates();
        MockLocation.cancel();
    }


    @Test
    public void canPauseLocationUpdates() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.pauseLocationUpdates();
    }


    @Test
    public void canHandleOnLocationChanged() throws Exception {
        Account.shared.clear();
        final MockLocation myTestLocation = new MockLocation();

        Database.setUp();
        Database.setUpEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Ignore
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Ignore
            }
        });

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(3.1415);
                location.setLongitude(4.1415);

                myTestLocation.onLocationChanged(location);
                assertEquals(location.getLatitude(), Account.shared.getLocation().get().getLatitude(), 0.01);
                assertEquals(location.getLongitude(), Account.shared.getLocation().get().getLongitude(), 0.01);

                myTestLocation.onLocationChanged(null);
                assertEquals(location.getLatitude(), Account.shared.getLocation().get().getLatitude(), 0.01);
                assertEquals(location.getLongitude(), Account.shared.getLocation().get().getLongitude(), 0.01);
            }
        });

        Account.shared.clear();
    }
}
