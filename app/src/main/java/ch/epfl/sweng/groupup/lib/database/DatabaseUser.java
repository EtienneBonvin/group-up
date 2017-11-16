package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import ch.epfl.sweng.groupup.lib.Optional;

import static ch.epfl.sweng.groupup.lib.database.Database.EMPTY_FIELD;

@IgnoreExtraProperties
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

    DatabaseUser(Optional<String> given_name,
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

    public String getGiven_name() {
        return given_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getEmail() {
        return email;
    }

    public String getUuid() {
        return uuid;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getProvider() {
        return provider;
    }

    @Exclude
    Optional<String> getOptGivenName() {
        if (given_name.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(given_name);
        }
    }

    @Exclude
    Optional<String> getOptFamilyName() {
        if (family_name.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(family_name);
        }
    }

    @Exclude
    Optional<String> getOptDisplayName() {
        if (display_name.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(display_name);
        }
    }

    @Exclude
    Optional<String> getOptEmail() {
        if (email.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(email);
        }
    }

    @Exclude
    Optional<String> getOptUUID() {
        if (uuid.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(uuid);
        }
    }

    @Exclude
    Optional<Location> getOptLocation() {
        switch (provider) {
            case LocationManager.GPS_PROVIDER:
                return createOptLocation();
            case LocationManager.NETWORK_PROVIDER:
                return createOptLocation();
            case LocationManager.PASSIVE_PROVIDER:
                return createOptLocation();
            default:
                return Optional.empty();
        }
    }

    @Exclude
    private Optional<Location> createOptLocation() {
        Location location = new Location(provider);

        try {
            location.setLatitude(Double.parseDouble(latitude));
            location.setLongitude(Double.parseDouble(longitude));
        } catch (NumberFormatException exception) {
            Log.e("DATABASE_USER_LOCATION:", exception.getMessage());
            return Optional.empty();
        }

        return Optional.from(location);
    }
}
