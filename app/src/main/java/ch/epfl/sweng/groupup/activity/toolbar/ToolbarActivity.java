package ch.epfl.sweng.groupup.activity.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.info.UserInformationActivity;
import ch.epfl.sweng.groupup.lib.geolocation.GeoLocation;
import ch.epfl.sweng.groupup.lib.geolocation.GeoLocationInterface;
import ch.epfl.sweng.groupup.lib.geolocation.MockLocation;
import ch.epfl.sweng.groupup.object.event.Event;


public abstract class ToolbarActivity extends AppCompatActivity {

    private static GeoLocationInterface geoLocation;
    //Event with invitation need to be stored outside the listing activity
    protected static Set<Event> eventsToDisplay = new HashSet<>();
    private static boolean mockMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
        provideGeoLocation();
        initializeToolbar();
    }


    public void provideGeoLocation() {
        if (geoLocation != null) {
            geoLocation.pauseLocationUpdates();
        }

        geoLocation = new GeoLocation(this, this);
        geoLocation.requestLocationUpdates();
    }


    public abstract void initializeToolbar();


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
     * Return true when the map should be mocked.
     *
     * @return - true if the map should be mocked
     */
    public boolean isMapMockWanted() {
        return mockMap;
    }


    /**
     * Sets up a listener to the given class
     *
     * @param intentClass the class of the activity to be started
     */
    protected void setUpListener(Class intentClass) {
        Intent intent = new Intent(getApplicationContext(), intentClass);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
