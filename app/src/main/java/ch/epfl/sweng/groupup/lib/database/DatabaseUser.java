package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import ch.epfl.sweng.groupup.lib.Optional;

import static ch.epfl.sweng.groupup.lib.database.Database.EMPTY_FIELD;

final class DatabaseUser {

    /**
     * Class to represent the user object that will be stored in the database.
     */

    public String given_name = EMPTY_FIELD;
    public String family_name = EMPTY_FIELD;
    public String display_name = EMPTY_FIELD;
    public String email = EMPTY_FIELD;
    public String uuid = EMPTY_FIELD;
    public String latitude = EMPTY_FIELD;
    public String longitude = EMPTY_FIELD;
    public String provider = EMPTY_FIELD;

    public DatabaseUser() {
    }

    public DatabaseUser(Optional<String> given_name,
                        Optional<String> family_name,
                        Optional<String> display_name,
                        Optional<String> email,
                        Optional<String> uuid,
                        Optional<Location> location) {
        this.given_name = given_name.getOrElse(EMPTY_FIELD);
        this.family_name = family_name.getOrElse(EMPTY_FIELD);
        this.display_name = display_name.getOrElse(EMPTY_FIELD);
        this.email = email.getOrElse(EMPTY_FIELD);
        this.uuid = uuid.getOrElse(EMPTY_FIELD);

        if (!location.isEmpty()) {
            latitude = location.get().getLatitude() + "";
            longitude = location.get().getLongitude() + "";
            provider = location.get().getProvider();
        } else {
            latitude = EMPTY_FIELD;
            longitude = EMPTY_FIELD;
            provider = EMPTY_FIELD;
        }
    }

    public Optional<String> getGivenName() {
        if (given_name.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(given_name);
        }
    }

    public Optional<String> getFamilyName() {
        if (family_name.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(family_name);
        }
    }

    public Optional<String> getDisplayName() {
        if (display_name.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(display_name);
        }
    }

    public Optional<String> getEmail() {
        if (email.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(email);
        }
    }

    public Optional<String> getUUID() {
        if (uuid.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(uuid);
        }
    }

    public Optional<Location> getLocation() {
        switch (provider) {
            case LocationManager.GPS_PROVIDER:
                return createLocation();
            case LocationManager.NETWORK_PROVIDER:
                return createLocation();
            case LocationManager.PASSIVE_PROVIDER:
                return createLocation();
            default:
                return Optional.empty();
        }
    }

    private Optional<Location> createLocation() {
        Location location = new Location(provider);

        try {
            location.setLatitude(Double.parseDouble(latitude));
            location.setLongitude(Double.parseDouble(longitude));
        } catch (NumberFormatException exception) {
            Log.e("DATABASE_USER_LOCATION", exception.getMessage());
            return Optional.empty();
        }

        return Optional.from(location);
    }
}
