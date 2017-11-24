package ch.epfl.sweng.groupup.lib.database;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.activity.main.MainActivity;

@RunWith(AndroidJUnit4.class)
public class DatabasePointOfInterestShould {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void overrideDefaultConstructorAndHaveDefaultValuesAssigned() throws Exception {
        DatabasePointOfInterest databasePointOfInterest =
                new DatabasePointOfInterest();

        Assert.assertEquals(Database.EMPTY_FIELD, databasePointOfInterest.name);
        Assert.assertEquals(Database.EMPTY_FIELD,
                            databasePointOfInterest.description);
        Assert.assertEquals(Database.EMPTY_FIELD,
                            databasePointOfInterest.latitude);
        Assert.assertEquals(Database.EMPTY_FIELD,
                            databasePointOfInterest.longitude);
        Assert.assertEquals(Database.EMPTY_FIELD,
                            databasePointOfInterest.provider);
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

        DatabasePointOfInterest databasePointOfInterest =
                new DatabasePointOfInterest(uuid, name, description, location);

        Assert.assertEquals(uuid, databasePointOfInterest.getUuid());
        Assert.assertEquals(name, databasePointOfInterest.getName());
        Assert.assertEquals(description,
                            databasePointOfInterest.getDescription());
        Assert.assertEquals("" + latitude,
                            databasePointOfInterest.getLatitude());
        Assert.assertEquals("" + longitude,
                            databasePointOfInterest.getLongitude());
        Assert.assertEquals(LocationManager.GPS_PROVIDER,
                            databasePointOfInterest
                                    .getProvider());
        Assert.assertEquals(location.getLatitude(), databasePointOfInterest
                .getLocation().getLatitude(), 0.1);
        Assert.assertEquals(location.getLongitude(), databasePointOfInterest
                .getLocation().getLongitude(), 0.1);
    }
}
