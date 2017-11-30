package ch.epfl.sweng.groupup.activity.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.info.UserInformationActivity;
import ch.epfl.sweng.groupup.activity.settings.SettingsActivity;
import ch.epfl.sweng.groupup.lib.geolocation.GeoLocation;
import ch.epfl.sweng.groupup.lib.geolocation.GeoLocationInterface;
import ch.epfl.sweng.groupup.lib.geolocation.MockLocation;
import ch.epfl.sweng.groupup.object.event.Event;


public class ToolbarActivity extends AppCompatActivity {

    private static GeoLocationInterface geoLocation;
    protected static Set<Event> eventsToDisplay = new HashSet<>();
    private static boolean mockMap = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
        initializeToolbarActivity();
    }


    protected void initializeToolbarActivity() {
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


    private void initializeToolbar() {
        findViewById(R.id.icon_access_group_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(EventListingActivity.class);
            }
        });

        findViewById(R.id.icon_access_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(SettingsActivity.class);
            }
        });

        findViewById(R.id.icon_access_user_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(UserInformationActivity.class);
            }
        });
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
    private void setUpListener(Class intentClass) {
        Intent intent = new Intent(getApplicationContext(), intentClass);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
