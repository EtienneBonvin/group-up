package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import ch.epfl.sweng.groupup.object.map.PointOfInterest;

import static ch.epfl.sweng.groupup.lib.database.Database.EMPTY_FIELD;


@IgnoreExtraProperties
public final class DatabasePointOfInterest {

    /**
     * Class to represent a point of interest that will be stored in the
     * database.
     */
    public String uuid = EMPTY_FIELD;
    public String name = EMPTY_FIELD;
    public String description = EMPTY_FIELD;
    public String latitude = EMPTY_FIELD;
    public String longitude = EMPTY_FIELD;
    public String provider = EMPTY_FIELD;


    public DatabasePointOfInterest() {
    }


    public DatabasePointOfInterest(String uuid, String name, String description, Location location) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.latitude = "" + location.getLatitude();
        this.longitude = "" + location.getLongitude();
        this.provider = LocationManager.GPS_PROVIDER;
    }


    public String getUuid() {
        return uuid;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
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
    Location getLocation() {
        Location location = new Location(provider);

        try {
            location.setLatitude(Double.parseDouble(latitude));
            location.setLongitude(Double.parseDouble(longitude));
        } catch (NumberFormatException exception) {
            Log.e("DATABASE_POI_LOCATION:", exception.getMessage());
            location.setLatitude(0.0);
            location.setLongitude(0.0);
        }

        return location;
    }


    @Exclude
    public PointOfInterest toPointOfInterest() {
        return new PointOfInterest(getUuid(), getName(), getDescription(), getLocation());
    }


    @Exclude
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DatabasePointOfInterest that = (DatabasePointOfInterest) o;

        return uuid.equals(that.uuid) &&
               name.equals(that.name) &&
               description.equals(that.description) &&
               latitude.equals(that.latitude) &&
               longitude.equals(that.longitude);
    }
}
