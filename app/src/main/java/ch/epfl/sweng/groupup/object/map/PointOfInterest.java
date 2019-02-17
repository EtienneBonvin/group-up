package ch.epfl.sweng.groupup.object.map;

import static java.util.UUID.randomUUID;

import android.location.Location;
import ch.epfl.sweng.groupup.lib.database.DatabasePointOfInterest;


/**
 * This class represents a point of interest on a map
 */
public final class PointOfInterest {

    private final String description;
    private final Location location;
    private final String name;
    private final String uuid;


    public PointOfInterest(String name, String description, Location location) {
        if (name == null || description == null || location == null) {
            throw new NullPointerException();
        }

        this.name = name;
        this.description = description;
        this.location = new Location(location);
        this.uuid = randomUUID().toString();
    }


    public PointOfInterest(String uuid, String name, String description, Location location) {
        if (uuid == null || name == null || description == null || location == null) {
            throw new NullPointerException();
        }

        this.name = name;
        this.description = description;
        this.location = new Location(location);
        this.uuid = uuid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PointOfInterest that = (PointOfInterest) o;

        return name.equals(that.name) && description.equals(that.description) && uuid.equals(that.uuid);
    }


    @Override
    public int hashCode() {
        int result = name.hashCode();

        result = 31 * result + description.hashCode();
        result = 31 * result + uuid.hashCode();

        return result;
    }


    /**
     * Converts a point of interest into its database representation.
     *
     * @return - the database point of interest
     */
    public DatabasePointOfInterest toDatabasePointOfInterest() {
        return new DatabasePointOfInterest(getUuid(), getName(), getDescription(), getLocation());
    }


    /**
     * @return The uuid of the point
     */
    public String getUuid() {
        return uuid;
    }


    /**
     * @return The name of the point
     */
    public String getName() {
        return name;
    }


    /**
     * @return The description of the point
     */
    public String getDescription() {
        return description;
    }


    /**
     * @return The location of the point
     */
    public Location getLocation() {
        return new Location(location);
    }


    @Override
    public String toString() {
        return "PointOfInterest{"
               + "name='"
               + name
               + '\''
               + ", description='"
               + description
               + '\''
               + ", location="
               + location
               + ", uuid='"
               + uuid
               + '\''
               + '}';
    }


    /**
     * Creates a new PointOfInterest with the given description
     *
     * @param description the new description
     *
     * @return a new point with the new description
     */
    public PointOfInterest withDescription(String description) {
        return new PointOfInterest(uuid, name, description, location);
    }


    /**
     * Creates a new PointOfInterest with the given location
     *
     * @param location the new location
     *
     * @return a new point with the new location
     */
    public PointOfInterest withLocation(Location location) {
        return new PointOfInterest(uuid, name, description, location);
    }


    /**
     * Creates a new PointOfInterest with the given name
     *
     * @param name the new name
     *
     * @return a new point with the new name
     */
    public PointOfInterest withName(String name) {
        return new PointOfInterest(uuid, name, description, location);
    }


    /**
     * Creates a new PointOfInterest with the given uuid
     *
     * @param uuid the new uuid
     *
     * @return a new point with the new location
     */
    public PointOfInterest withUuid(String uuid) {
        return new PointOfInterest(uuid, name, description, location);
    }
}
