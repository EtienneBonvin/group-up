package ch.epfl.sweng.groupup.lib.geolocation;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;

public final class MockLocation implements GeoLocationInterface {

    private final static int REPEAT_DELAY = 0;
    private final static int REPEAT_INTERVAL = 5000;
    private final static List<Location> locations = getCoordinates();

    private final static Timer locationMocker = new Timer();

    private int coordinateIndex = 0;

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Account.shared.withLocation(location);
            Database.update();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // Ignored
    }

    @Override
    public void onProviderEnabled(String s) {
        // Ignored
    }

    @Override
    public void onProviderDisabled(String s) {
        // Ignored
    }

    @Override
    public void requestLocationUpdates() {
        locationMocker.schedule(new TimerTask() {
            @Override
            public void run() {
                if (coordinateIndex > locations.size() - 1) {
                    coordinateIndex = 0;
                }

                onLocationChanged(locations.get(coordinateIndex));
                coordinateIndex += 1;
            }
        }, REPEAT_DELAY, REPEAT_INTERVAL);
    }

    @Override
    public void pauseLocationUpdates() {
        locationMocker.cancel();
    }

    /**
     * Generates a coordinates path to ease mocking of the location. The path
     * is a guy arriving at EPFL from the metro and going to INM.
     *
     * @return - a list of coordinates
     */
    private static List<Location> getCoordinates() {
        List<Location> coordinates = new ArrayList<>();

        coordinates.add(getLocationFromPair(new Pair<>(46.522023, 6.564925)));
        coordinates.add(getLocationFromPair(new Pair<>(46.521886, 6.564951)));
        coordinates.add(getLocationFromPair(new Pair<>(46.521694, 6.564951)));
        coordinates.add(getLocationFromPair(new Pair<>(46.521617, 6.564957)));
        coordinates.add(getLocationFromPair(new Pair<>(46.521466, 6.564962)));
        coordinates.add(getLocationFromPair(new Pair<>(46.521292, 6.564978)));
        coordinates.add(getLocationFromPair(new Pair<>(46.521049, 6.564973)));
        coordinates.add(getLocationFromPair(new Pair<>(46.520927, 6.565000)));
        coordinates.add(getLocationFromPair(new Pair<>(46.520764, 6.564984)));
        coordinates.add(getLocationFromPair(new Pair<>(46.520635, 6.565000)));
        coordinates.add(getLocationFromPair(new Pair<>(46.520410, 6.565005)));
        coordinates.add(getLocationFromPair(new Pair<>(46.520155, 6.565032)));
        coordinates.add(getLocationFromPair(new Pair<>(46.519974, 6.564823)));
        coordinates.add(getLocationFromPair(new Pair<>(46.519919, 6.564775)));
        coordinates.add(getLocationFromPair(new Pair<>(46.519775, 6.564555)));
        coordinates.add(getLocationFromPair(new Pair<>(46.519572, 6.564281)));
        coordinates.add(getLocationFromPair(new Pair<>(46.519417, 6.564083)));
        coordinates.add(getLocationFromPair(new Pair<>(46.519280, 6.563868)));
        coordinates.add(getLocationFromPair(new Pair<>(46.519177, 6.563708)));
        coordinates.add(getLocationFromPair(new Pair<>(46.519026, 6.563482)));

        return coordinates;
    }

    /**
     * Eases the process of getting a location object from raw coordinates.
     *
     * @param coordinatesPair - a pair containing the latitude and longitude
     * @return - a location object representing the coordinates given as
     * parameter
     */
    private static Location getLocationFromPair(Pair<Double, Double>
                                                        coordinatesPair) {
        Location location = new Location(LocationManager.GPS_PROVIDER);

        location.setLatitude(coordinatesPair.first);
        location.setLongitude(coordinatesPair.second);

        return location;
    }
}
