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
    public String givenName = EMPTY_FIELD;
    public String familyName = EMPTY_FIELD;
    public String displayName = EMPTY_FIELD;
    public String email = EMPTY_FIELD;
    public String uuid = EMPTY_FIELD;
    public String latitude = EMPTY_FIELD;
    public String longitude = EMPTY_FIELD;
    public String provider = EMPTY_FIELD;


    public DatabaseUser() {
    }


    DatabaseUser(Optional<String> givenName,
                 Optional<String> familyName,
                 Optional<String> displayName,
                 Optional<String> email,
                 Optional<String> uuid,
                 Optional<Location> location) {
        this.givenName = givenName.getOrElse(EMPTY_FIELD);
        this.familyName = familyName.getOrElse(EMPTY_FIELD);
        this.displayName = displayName.getOrElse(EMPTY_FIELD);
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


    public String getGivenName() {
        return givenName;
    }


    public String getFamilyName() {
        return familyName;
    }


    public String getDisplayName() {
        return displayName;
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
    void clearLocation() {
        latitude = EMPTY_FIELD;
        longitude = EMPTY_FIELD;
        provider = EMPTY_FIELD;
    }


    @Exclude
    Optional<String> getOptGivenName() {
        if (givenName.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(givenName);
        }
    }


    @Exclude
    Optional<String> getOptFamilyName() {
        if (familyName.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(familyName);
        }
    }


    @Exclude
    Optional<String> getOptDisplayName() {
        if (displayName.equals(EMPTY_FIELD)) {
            return Optional.empty();
        } else {
            return Optional.from(displayName);
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
    Optional<String> getOptUuid() {
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
