package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.LocationProvider;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;

@RunWith(AndroidJUnit4.class)
public class GeoLocationTestSuite {
    @Rule
    public final ActivityTestRule<ToolbarActivity> mActivityRule =
            new ActivityTestRule<>(ToolbarActivity.class);

    @Test
    public void handlesAllStatusChanges() throws Exception {
        GeoLocation myTestLocation =
                new GeoLocation(mActivityRule.getActivity(),
                                mActivityRule.getActivity()
                                        .getApplicationContext());

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
        GeoLocation myTestLocation =
                new GeoLocation(mActivityRule.getActivity(),
                                mActivityRule.getActivity()
                                        .getApplicationContext());

        myTestLocation.onProviderEnabled("MY_PROVIDER");
    }

    @Test
    public void handlesOnProviderDisabled() throws Exception {
        GeoLocation myTestLocation =
                new GeoLocation(mActivityRule.getActivity(),
                                mActivityRule.getActivity()
                                        .getApplicationContext());

        myTestLocation.onProviderDisabled("MY_PROVIDER");
    }

    @Test
    public void canRequestLocationUpdates() throws Exception {
        GeoLocation myTestLocation =
                new GeoLocation(mActivityRule.getActivity(),
                                mActivityRule.getActivity()
                                        .getApplicationContext());

        myTestLocation.requestLocationUpdates();
    }

    @Test
    public void canPauseLocationUpdates() throws Exception {
        GeoLocation myTestLocation =
                new GeoLocation(mActivityRule.getActivity(),
                                mActivityRule.getActivity()
                                        .getApplicationContext());

        myTestLocation.pauseLocationUpdates();
    }
}
