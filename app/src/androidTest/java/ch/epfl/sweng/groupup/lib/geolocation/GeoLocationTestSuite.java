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

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class GeoLocationTestSuite {

    @Rule
    public final ActivityTestRule<EventListingActivity>
            mActivityRule
            = new ActivityTestRule<>(EventListingActivity.class);


    @Test
    public void handlesAllStatusChanges() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.onStatusChanged(null, LocationProvider.OUT_OF_SERVICE, null);
                myTestLocation.onStatusChanged(null, LocationProvider.TEMPORARILY_UNAVAILABLE, null);
                myTestLocation.onStatusChanged(null, LocationProvider.AVAILABLE, null);
                myTestLocation.onStatusChanged(null, -1, null);
            }
        });
    }


    @Test
    public void handlesOnProviderEnabled() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.onProviderEnabled("MY_PROVIDER");
            }
        });
    }


    @Test
    public void handlesOnProviderDisabled() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.onProviderDisabled("MY_PROVIDER");
            }
        });
    }


    @Test
    public void canRequestLocationUpdates() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.requestLocationUpdates();
                myTestLocation.pauseLocationUpdates();
            }
        });
    }


    @Test
    public void canPauseLocationUpdates() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.pauseLocationUpdates();
            }
        });
    }


    @Test
    public void onLocationChangesCorrectlyAssignsLocationToAccount() throws Throwable {
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

        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Account.shared.clear();
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(3.1415);
                location.setLongitude(4.1415);

                myTestLocation.onLocationChanged(location);
                assertEquals(location.getLatitude(), Account.shared.getLocation().get().getLatitude(), 0.01);
                assertEquals(location.getLongitude(), Account.shared.getLocation().get().getLongitude(), 0.01);

                myTestLocation.onLocationChanged(null);
                assertEquals(location.getLatitude(), Account.shared.getLocation().get().getLatitude(), 0.01);
                assertEquals(location.getLongitude(), Account.shared.getLocation().get().getLongitude(), 0.01);

                Account.shared.clear();
            }
        });
    }


    @Test
    public void canCorrectlyAskForGpsPermissionsAndRefuse() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.askToEnableProvider(GeoLocation.ASK_ENABLE_GPS);
            }
        });

        onView(withText(R.string.alert_dialog_no)).perform(click());
    }


    @Test
    public void canCorrectlyAskForPermissionsAndRefuse() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.askToEnableProvider(GeoLocation.ASK_PERMISSION);
            }
        });

        onView(withText(R.string.alert_dialog_no)).perform(click());
    }


    @Test
    public void canCorrectlyAskForGpsPermissionsAndAccept() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.askToEnableProvider(GeoLocation.ASK_ENABLE_GPS);
            }
        });

        onView(withText(R.string.alert_dialog_yes)).perform(click());
    }


    @Test
    public void canCorrectlyAskForPermissionsAndAccept() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.askToEnableProvider(GeoLocation.ASK_PERMISSION);
            }
        });

        onView(withText(R.string.alert_dialog_yes)).perform(click());
    }


    @Test
    public void canCorrectlyAskForAnythingAndAccept() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.askToEnableProvider("ANYTHING");
            }
        });

        onView(withText(R.string.alert_dialog_yes)).perform(click());
    }


    @Test
    public void canCorrectlyAskForAnythingAndRefuse() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GeoLocation myTestLocation = new GeoLocation(mActivityRule.getActivity(),
                                                             mActivityRule.getActivity().getApplicationContext());

                myTestLocation.askToEnableProvider("ANYTHING");
            }
        });

        onView(withText(R.string.alert_dialog_no)).perform(click());
    }
}
