package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.Location;
import android.os.Bundle;

public final class MockLocation implements GeoLocationInterface {
    @Override
    public void onLocationChanged(Location location) {
        // TODO
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // TODO
    }

    @Override
    public void onProviderEnabled(String s) {
        // TODO
    }

    @Override
    public void onProviderDisabled(String s) {
        // TODO
    }

    @Override
    public void requestLocationUpdates() {
        // TODO
    }

    @Override
    public void pauseLocationUpdates() {
        // TODO
    }
}
