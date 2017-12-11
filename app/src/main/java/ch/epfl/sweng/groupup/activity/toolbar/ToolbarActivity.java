package ch.epfl.sweng.groupup.activity.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class ToolbarActivity extends AppCompatActivity {

    private static GeoLocationInterface geoLocation;
    //Event with invitation need to be stored outside the listing activity
    protected static Set<Event> eventsToDisplay = new HashSet<>();
    private static boolean mockMap = false;

    protected static final String MEMBERS_ADDING = "MembersAdding";
    protected static final String EVENT_CREATION = "EventCreation";
    protected static final String EVENT_DESCRIPTION = "EventDescription";
    protected static final String USER_PROFILE = "UserProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
        initializeToolbarActivity("");
    }


    protected void initializeToolbarActivity(String activity) {
        provideGeoLocation();
        initializeToolbar(activity);
    }


    public void provideGeoLocation() {
        if (geoLocation != null) {
            geoLocation.pauseLocationUpdates();
        }

        geoLocation = new GeoLocation(this, this);
        geoLocation.requestLocationUpdates();
    }


    private void initializeToolbar(String activity) {
        TextView title = findViewById(R.id.toolbar_title);
        ImageView rightImage = findViewById(R.id.toolbar_image_right);
        ImageView secondRightImage = findViewById(R.id.toolbar_image_second_from_right);

        switch (activity){
            case EVENT_CREATION:
                title.setText(R.string.toolbar_title_create_event);
                rightImage.setImageResource(R.drawable.ic_check);
                break;
            case EVENT_DESCRIPTION:
                rightImage.setImageResource(R.drawable.ic_check);
                secondRightImage.setImageResource(R.drawable.ic_user);
                findViewById(R.id.toolbar_image_second_from_right).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUpListener(UserInformationActivity.class);
                    }
                });
                break;
            case MEMBERS_ADDING:
                title.setText(R.string.toolbar_title_add_members);
                rightImage.setImageResource(R.drawable.ic_check);
                break;
            case USER_PROFILE:
                title.setText(R.string.toolbar_title_user_profile);
            default:
                rightImage.setImageResource(R.drawable.ic_user);
                findViewById(R.id.toolbar_image_right).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUpListener(UserInformationActivity.class);
                    }
                });
                break;
        }

        // home button
        findViewById(R.id.toolbar_image_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(EventListingActivity.class);
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
