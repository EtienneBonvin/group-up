package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.LocationListener;

interface GeoLocationInterface extends LocationListener {
    void requestLocationUpdates();

    void pauseLocationUpdates();
}
