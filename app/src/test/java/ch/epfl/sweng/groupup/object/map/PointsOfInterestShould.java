package ch.epfl.sweng.groupup.object.map;

import static org.junit.Assert.*;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import ch.epfl.sweng.groupup.lib.database.DatabasePointOfInterest;
import ch.epfl.sweng.groupup.object.TestHelper;
import java.util.ArrayList;
import org.junit.*;


public class PointsOfInterestShould {

    private final String DEFAULT_DESCRIPTION = "Description";
    private final String DEFAULT_NAME = "Name";
    private final String DEFAULT_UUID = "suchComplexVeryWow";
    private Location defaultLocation;
    private PointOfInterest defaultPoint;


    @Test
    public void correctlyComputeHashCode() {
        int expectedHashCode = defaultPoint.getName()
                                           .hashCode();
        expectedHashCode = 31 * expectedHashCode + defaultPoint.getDescription()
                                                               .hashCode();
        expectedHashCode = 31 * expectedHashCode + defaultPoint.getUuid()
                                                               .hashCode();

        int realHashCode = defaultPoint.hashCode();

        assertEquals(expectedHashCode, realHashCode);
    }


    @Test
    public void correctlyConvertToDatabasePointOfInterest() {
        DatabasePointOfInterest databasePointOfInterest = defaultPoint.toDatabasePointOfInterest();

        assertEquals(defaultPoint.getDescription(), databasePointOfInterest.getDescription());
        assertEquals(defaultPoint.getName(), databasePointOfInterest.getName());
        assertEquals(defaultPoint.getUuid(), databasePointOfInterest.getUuid());
        assertEquals("" + defaultPoint.getLocation()
                                      .getLatitude(), databasePointOfInterest.getLatitude());
        assertEquals("" + defaultPoint.getLocation()
                                      .getLongitude(), databasePointOfInterest.getLongitude());
    }


    @Test
    public void correctlyPrintToString() {
        String expectedString = "PointOfInterest{" +
                                "name='" +
                                defaultPoint.getName() +
                                '\'' +
                                ", description='" +
                                defaultPoint.getDescription() +
                                '\'' +
                                ", location=" +
                                defaultPoint.getLocation() +
                                ", uuid='" +
                                defaultPoint.getUuid() +
                                '\'' +
                                '}';

        String realString = defaultPoint.toString();

        assertEquals(expectedString, realString);
    }


    @SuppressWarnings("all")
    @Test
    public void findEqualAndNotEqualPointOfInterest() {
        PointOfInterest poi01 = defaultPoint.withName("Name01");
        PointOfInterest poi02 = defaultPoint.withDescription("Desc01");
        PointOfInterest poi03 = defaultPoint.withUuid("NewUUID04");

        PointOfInterest samePoI = defaultPoint.withName("SameName")
                                              .withName(DEFAULT_NAME);

        // HashCode should be equal when the objects are equal.
        assertTrue(!poi01.equals(defaultPoint));
        assertTrue(poi01.hashCode() != defaultPoint.hashCode());
        assertTrue(!poi02.equals(defaultPoint));
        assertTrue(poi02.hashCode() != defaultPoint.hashCode());
        assertTrue(!poi03.equals(defaultPoint));
        assertTrue(poi03.hashCode() != defaultPoint.hashCode());
        assertTrue(!defaultPoint.equals(null));
        assertTrue(samePoI.equals(defaultPoint));
        assertTrue(samePoI.hashCode() == defaultPoint.hashCode());
        assertTrue(defaultPoint.equals(defaultPoint));
        assertTrue(defaultPoint.hashCode() == defaultPoint.hashCode());
        assertNotEquals(defaultPoint, new ArrayList<>());
    }


    @SuppressLint("Assert")
    @Test
    public void haveCorrectGetters() {
        assertEquals(defaultPoint.getName(), DEFAULT_NAME);
        assertEquals(defaultPoint.getDescription(), DEFAULT_DESCRIPTION);
        assert (TestHelper.reasonablyEqual(defaultPoint.getLocation()
                                                       .getLatitude(), defaultLocation.getLatitude()));
        assert (TestHelper.reasonablyEqual(defaultPoint.getLocation()
                                                       .getLongitude(), defaultLocation.getLongitude()));
        assertEquals(DEFAULT_UUID, defaultPoint.getUuid());
    }


    @SuppressLint("Assert")
    @Test
    public void haveCorrectSetters() {
        Location testLocation = new Location(defaultLocation);
        testLocation.setLatitude(4.32);
        testLocation.setLongitude(7.23);

        PointOfInterest p = defaultPoint.withName("Name2");
        PointOfInterest p2 = defaultPoint.withDescription("Description2");
        PointOfInterest p3 = defaultPoint.withLocation(testLocation);
        PointOfInterest p4 = defaultPoint.withUuid("veryComplexSuchWow");

        assertEquals(p.getName(), "Name2");
        assertEquals(p2.getDescription(), "Description2");
        assert (TestHelper.reasonablyEqual(p3.getLocation()
                                             .getLatitude(), testLocation.getLatitude()));
        assert (TestHelper.reasonablyEqual(p3.getLocation()
                                             .getLongitude(), testLocation.getLongitude()));
        assertEquals(p4.getUuid(), "veryComplexSuchWow");
    }


    @Before
    public void init() {
        defaultLocation = new Location(LocationManager.GPS_PROVIDER);
        defaultLocation.setLatitude(47.3);
        defaultLocation.setLongitude(2.23);

        defaultPoint = new PointOfInterest(DEFAULT_NAME, DEFAULT_DESCRIPTION, defaultLocation);

        defaultPoint = defaultPoint.withUuid(DEFAULT_UUID);

        defaultPoint = new PointOfInterest(defaultPoint.getUuid(),
                                           defaultPoint.getName(),
                                           defaultPoint.getDescription(),
                                           defaultPoint.getLocation());
    }


    @Test(expected = NullPointerException.class)
    public void notBeCreatableWhenNameIsNull() {
        new PointOfInterest(null, DEFAULT_DESCRIPTION, defaultLocation);
    }


    @Test(expected = NullPointerException.class)
    public void notBeCreatableWhenNameIsNull2() {
        new PointOfInterest(DEFAULT_UUID, null, DEFAULT_DESCRIPTION, defaultLocation);
    }


    @Test(expected = NullPointerException.class)
    public void notBeCreatedWhenDescriptionIsNull() {
        new PointOfInterest(DEFAULT_NAME, null, defaultLocation);
    }


    @Test(expected = NullPointerException.class)
    public void notBeCreatedWhenDescriptionIsNull2() {
        new PointOfInterest(DEFAULT_UUID, DEFAULT_NAME, null, defaultLocation);
    }


    @Test(expected = NullPointerException.class)
    public void notBeCreatedWhenLocationIsNull() {
        new PointOfInterest(DEFAULT_NAME, DEFAULT_DESCRIPTION, null);
    }


    @Test(expected = NullPointerException.class)
    public void notBeCreatedWhenLocationIsNull2() {
        new PointOfInterest(DEFAULT_UUID, DEFAULT_NAME, DEFAULT_DESCRIPTION, null);
    }


    @Test(expected = NullPointerException.class)
    public void notBeCreatedWhenUuidIsNull() {
        new PointOfInterest(null, DEFAULT_NAME, DEFAULT_DESCRIPTION, defaultLocation);
    }
}
