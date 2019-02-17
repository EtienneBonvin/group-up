package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.LocationListener;


public interface GeoLocationInterface extends LocationListener {

    void pauseLocationUpdates();

    void requestLocationUpdates();
}
