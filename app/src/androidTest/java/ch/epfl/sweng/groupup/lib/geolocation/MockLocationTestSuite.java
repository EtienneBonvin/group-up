package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.database.Database;

@RunWith(AndroidJUnit4.class)
public class MockLocationTestSuite {
    @Rule
    public final ActivityTestRule<ToolbarActivity> mActivityRule =
            new ActivityTestRule<>(ToolbarActivity.class);

    @Test
    public void handlesAllStatusChanges() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.onStatusChanged(null,
                                       LocationProvider.OUT_OF_SERVICE,
                                       null);
        myTestLocation.onStatusChanged(null,
                                       LocationProvider.TEMPORARILY_UNAVAILABLE,
                                       null);
        myTestLocation.onStatusChanged(null,
                                       LocationProvider.AVAILABLE,
                                       null);
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
    public void canPauseLocationUpdates() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.pauseLocationUpdates();
    }

    @Test
    public void canHandleOnLocationChanged() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        Database.setUpDatabase();
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

        myTestLocation.onLocationChanged(new Location(LocationManager.GPS_PROVIDER));
    }
}
