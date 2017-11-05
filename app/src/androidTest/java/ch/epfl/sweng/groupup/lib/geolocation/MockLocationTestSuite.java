package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.LocationProvider;

import org.junit.Test;

public class MockLocationTestSuite {
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

    /* TO FIX
    @Test
    public void canRequestLocationUpdates() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.requestLocationUpdates();
    }
    */

    @Test
    public void canPauseLocationUpdates() throws Exception {
        MockLocation myTestLocation = new MockLocation();

        myTestLocation.pauseLocationUpdates();
    }
}
