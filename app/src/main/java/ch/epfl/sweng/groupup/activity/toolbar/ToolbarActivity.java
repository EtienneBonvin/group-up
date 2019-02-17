package ch.epfl.sweng.groupup.activity.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.lib.geolocation.GeoLocation;
import ch.epfl.sweng.groupup.lib.geolocation.GeoLocationInterface;
import ch.epfl.sweng.groupup.lib.geolocation.MockLocation;
import ch.epfl.sweng.groupup.object.event.Event;
import java.util.HashSet;
import java.util.Set;


public abstract class ToolbarActivity extends AppCompatActivity {

    //Event with invitation need to be stored outside the listing activity
    protected static Set<Event> eventsToDisplay = new HashSet<>();
    private static GeoLocationInterface geoLocation;
    private static boolean mockMap = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
        provideGeoLocation();
    }


    public void provideGeoLocation() {
        if (geoLocation != null) {
            geoLocation.pauseLocationUpdates();
        }

        geoLocation = new GeoLocation(this, this);
        geoLocation.requestLocationUpdates();
    }


    @Override
    protected void onStart() {
        super.onStart();
        initializeToolbar();
    }


    /**
     * Initializes the toolbar depending on what activity is running
     */
    public abstract void initializeToolbar();


    /**
     * Return true when the map should be mocked.
     *
     * @return - true if the map should be mocked
     */
    public boolean isMapMockWanted() {
        return mockMap;
    }


    /**
     * Permits the tester to mock the location correctly when performing some
     * tests.
     */
    public void mockLocation() {
        geoLocation = new MockLocation();
        geoLocation.requestLocationUpdates();
    }


    /**
     * Permits the tester to mock the map when performing some tests.
     */
    public void mockMap() {
        mockMap = true;
    }


    /**
     * Sets up a listener to the given class
     *
     * @param intentClass the class of the activity to be started
     */
    protected void setUpListener(Class intentClass) {
        Intent intent = new Intent(getApplicationContext(), intentClass);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
