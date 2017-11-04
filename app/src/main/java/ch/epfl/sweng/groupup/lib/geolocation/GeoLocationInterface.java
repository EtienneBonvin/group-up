package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.Location;
import android.location.LocationListener;

interface GeoLocationInterface extends LocationListener {
    Location getGeoLocation();
}
