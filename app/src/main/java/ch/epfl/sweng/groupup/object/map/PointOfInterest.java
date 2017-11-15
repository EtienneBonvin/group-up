package ch.epfl.sweng.groupup.object.map;

import android.location.Location;

/**
 * This class represents a point of interest on a map
 */
public final class PointOfInterest {
    private final String name;
    private final String description;
    private final Location location;

    public PointOfInterest(String name, String description, Location location){
        if(name == null || description == null || location == null){
            throw new NullPointerException();
        }

        this.name = name;
        this.description = description;
        this.location = new Location(location);
    }

    /**
     * @return The name of the point
     */
    public String getName(){ return name; }

    /**
     * @return The description of the point
     */
    public String getDescription(){ return description; }

    /**
     * @return The location of the point
     */
    public Location getLocation(){ return new Location(location); }

    /**
     * Creates a new PointOfInterest with the given name
     * @param name the new name
     * @return a new point with the new name
     */
    public PointOfInterest withName(String name){ return new PointOfInterest(name, description, location); }

    /**
     * Creates a new PointOfInterest with the given description
     * @param description the new description
     * @return a new point with the new description
     */
    public PointOfInterest withDescription(String description){ return new PointOfInterest(name, description, location); }

    /**
     * Creates a new PointOfInterest with the given location
     * @param location the new location
     * @return a new point with the new location
     */
    public PointOfInterest withLocation(Location location){ return new PointOfInterest(name, description, location); }
}
