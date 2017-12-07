package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.object.map.PointOfInterest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(AndroidJUnit4.class)
public class DatabasePointOfInterestShould {

    @Rule
    public final ActivityTestRule<ToolbarActivity> mActivityRule = new ActivityTestRule<>(ToolbarActivity.class);


    @Test
    public void overrideDefaultConstructorAndHaveDefaultValuesAssigned() throws Exception {
        DatabasePointOfInterest databasePointOfInterest = new DatabasePointOfInterest();

        assertEquals(Database.EMPTY_FIELD, databasePointOfInterest.name);
        assertEquals(Database.EMPTY_FIELD, databasePointOfInterest.description);
        assertEquals(Database.EMPTY_FIELD, databasePointOfInterest.latitude);
        assertEquals(Database.EMPTY_FIELD, databasePointOfInterest.longitude);
        assertEquals(Database.EMPTY_FIELD, databasePointOfInterest.provider);
    }


    @Test
    public void correctlyCreatePointOfInterest() throws Exception {
        String uuid = "uuidVeryComplex";
        String name = "My PoI";
        String description = "This is a PoI.";
        Location location = new Location(LocationManager.GPS_PROVIDER);
        double latitude = 32.7;
        double longitude = 17.87;
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        DatabasePointOfInterest databasePointOfInterest = new DatabasePointOfInterest(uuid,
                                                                                      name,
                                                                                      description,
                                                                                      location);

        assertEquals(uuid, databasePointOfInterest.getUuid());
        assertEquals(name, databasePointOfInterest.getName());
        assertEquals(description, databasePointOfInterest.getDescription());
        assertEquals("" + latitude, databasePointOfInterest.getLatitude());
        assertEquals("" + longitude, databasePointOfInterest.getLongitude());
        assertEquals(LocationManager.GPS_PROVIDER, databasePointOfInterest.getProvider());
        assertEquals(location.getLatitude(), databasePointOfInterest.getLocation().getLatitude(), 0.1);
        assertEquals(location.getLongitude(), databasePointOfInterest.getLocation().getLongitude(), 0.1);
    }


    @Test
    public void onlyBeEqualIfReallyEqual() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        double latitude = 32.7;
        double longitude = 17.87;
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        DatabasePointOfInterest poi01 = new DatabasePointOfInterest("STUFF", "STUFF", "STUFF", location);
        DatabasePointOfInterest poi02 = new DatabasePointOfInterest("STUFF", "STUFF", "STUFF", location);

        assertEquals(poi01, poi02);
        assertNotEquals(poi01, null);

        poi02.uuid = "OTHER_STUFF";
        assertNotEquals(poi01, poi02);
        poi02.uuid = "STUFF";

        poi02.name = "OTHER_STUFF";
        assertNotEquals(poi01, poi02);
        poi02.name = "STUFF";

        poi02.description = "OTHER_STUFF";
        assertNotEquals(poi01, poi02);
        poi02.description = "STUFF";

        poi02.latitude = "OTHER_STUFF";
        assertNotEquals(poi01, poi02);
        poi02.latitude = "" + latitude;

        poi02.longitude = "OTHER_STUFF";
        assertNotEquals(poi01, poi02);
        poi02.longitude = "" + longitude;
    }


    @Test
    public void correctlyConvertToPointOfInterest() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        double latitude = 32.7;
        double longitude = 17.87;
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        DatabasePointOfInterest databasePointOfInterest = new DatabasePointOfInterest("STUFF01",
                                                                                      "STUFF02",
                                                                                      "STUFF03",
                                                                                      location);
        PointOfInterest pointOfInterest = databasePointOfInterest.toPointOfInterest();

        assertEquals(databasePointOfInterest.getUuid(), pointOfInterest.getUuid());
        assertEquals(databasePointOfInterest.getName(), pointOfInterest.getName());
        assertEquals(databasePointOfInterest.getDescription(), pointOfInterest.getDescription());
        assertEquals(location.getLatitude(), pointOfInterest.getLocation().getLatitude(), 0.1);
        assertEquals(location.getLongitude(), pointOfInterest.getLocation().getLongitude(), 0.1);
    }
}
