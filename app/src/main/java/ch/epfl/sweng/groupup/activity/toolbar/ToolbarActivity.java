package ch.epfl.sweng.groupup.activity.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.info.UserInformationActivity;
import ch.epfl.sweng.groupup.activity.settings.SettingsActivity;
import ch.epfl.sweng.groupup.lib.geolocation.GeoLocation;
import ch.epfl.sweng.groupup.lib.geolocation.GeoLocationInterface;
import ch.epfl.sweng.groupup.lib.geolocation.MockLocation;

public class ToolbarActivity extends AppCompatActivity {

    private static GeoLocationInterface geoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
        initializeToolbarActivity();
    }

    protected void initializeToolbarActivity() {
        initializeGeoLocation();
        initializeToolbar();
    }

    private void initializeGeoLocation() {
        geoLocation = new GeoLocation(this, this);
        geoLocation.requestLocationUpdates();
    }

    private void initializeToolbar() {
        findViewById(R.id.icon_access_group_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =
                                new Intent(getApplicationContext(),
                                           EventListingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        findViewById(R.id.icon_access_settings)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =
                                new Intent(getApplicationContext(),
                                           SettingsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        findViewById(R.id.icon_access_user_profile)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =
                                new Intent(getApplicationContext(),
                                           UserInformationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
    }

    /**
     * Permits the tester to mock the location correctly when performing some
     * tests.
     */
    public void mock() {
        geoLocation = new MockLocation();
        geoLocation.requestLocationUpdates();
    }
}
