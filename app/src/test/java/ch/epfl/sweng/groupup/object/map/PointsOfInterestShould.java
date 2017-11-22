package ch.epfl.sweng.groupup.object.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import org.junit.Before;
import org.junit.Test;

import static ch.epfl.sweng.groupup.TestHelper.reasonablyEqual;
import static org.junit.Assert.*;

public class PointsOfInterestShould {
    private final String DEFAULT_NAME = "Name";
    private final String DEFAULT_DESCRIPTION = "Description";
    private Location defaultLocation;
    private PointOfInterest defaultPoint;

    @Before
    public void init(){
        defaultLocation = new Location(LocationManager.GPS_PROVIDER);
        defaultLocation.setLatitude(47.3);
        defaultLocation.setLongitude(2.23);
        defaultPoint = new PointOfInterest(DEFAULT_NAME, DEFAULT_DESCRIPTION, defaultLocation);
    }

    @Test(expected = NullPointerException.class)
    public void notBeCreatableWhenNameIsNull(){
        new PointOfInterest(null, DEFAULT_DESCRIPTION, defaultLocation);
    }

    @Test(expected = NullPointerException.class)
    public void notBeCreatedWhenDescriptionIsNull(){
        new PointOfInterest(DEFAULT_NAME, null, defaultLocation);
    }

    @Test(expected = NullPointerException.class)
    public void notBeCreatedWhenLocationIsNull(){
        new PointOfInterest(DEFAULT_NAME, DEFAULT_DESCRIPTION, null);
    }

    @SuppressLint("Assert")
    @Test
    public void haveCorrectGetters(){
        assertEquals(defaultPoint.getName(), DEFAULT_NAME);
        assertEquals(defaultPoint.getDescription(), DEFAULT_DESCRIPTION);
        assert(reasonablyEqual(defaultPoint.getLocation().getLatitude(), defaultLocation.getLatitude()));
        assert(reasonablyEqual(defaultPoint.getLocation().getLongitude(), defaultLocation.getLongitude()));

    }

    @SuppressLint("Assert")
    @Test
    public void haveCorrectSetters(){
        Location testLocation = new Location(defaultLocation);
        testLocation.setLatitude(4.32);
        testLocation.setLongitude(7.23);

        PointOfInterest p = defaultPoint.withName("Name2");
        PointOfInterest p2 = defaultPoint.withDescription("Description2");
        PointOfInterest p3 = defaultPoint.withLocation(testLocation);

        assertEquals(p.getName(), "Name2");
        assertEquals(p2.getDescription(), "Description2");
        assert(reasonablyEqual(p3.getLocation().getLatitude(), testLocation.getLatitude()));
        assert(reasonablyEqual(p3.getLocation().getLongitude(), testLocation.getLongitude()));
    }
}