package ch.epfl.sweng.groupup.lib.geolocation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.lib.Helper;

public final class GeoLocation implements GeoLocationInterface {

    private static final long MIN_UPDATE_TIME_INTERVAL = 1000;
    private static final float MIN_UPDATE_DISTANCE_INTERVAL = 2;

    private final Activity activity;
    private final Context context;

    private final LocationManager locationManager;
    private final String provider;

    public GeoLocation(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;

        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(false);

        provider = locationManager.getBestProvider(criteria, false);

        if (!locationManager.isProviderEnabled(provider)) {
            askToEnableProvider();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO
        if (location != null) {
            Log.e("###", "" + location.getLatitude());
            Log.e("###", "" + location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        if (provider.equals(s)) {
            switch (i) {
                case LocationProvider.OUT_OF_SERVICE:
                    pauseLocationUpdates();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    pauseLocationUpdates();
                    break;
                case LocationProvider.AVAILABLE:
                    requestLocationUpdates();
                    break;
                default:
                    // If this is the case then we simply ignore the event.
                    break;
            }
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        Helper.showToast(context,
                         "Provider \"" + s + "\" enabled.",
                         Toast.LENGTH_SHORT);
    }

    @Override
    public void onProviderDisabled(String s) {
        Helper.showToast(context,
                         "Provider \"" + s + "\" disabled.",
                         Toast.LENGTH_SHORT);
    }

    /**
     * Method to be called in the onCreate()/onResume() method of the activity
     * to start listening for location updates.
     */
    public void requestLocationUpdates() {
        if ((ActivityCompat
                     .checkSelfPermission(context,
                                          Manifest.permission.ACCESS_FINE_LOCATION) !=
             PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat
                     .checkSelfPermission(context,
                                          Manifest.permission.ACCESS_COARSE_LOCATION) !=
             PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        locationManager.requestLocationUpdates(provider,
                                               MIN_UPDATE_TIME_INTERVAL,
                                               MIN_UPDATE_DISTANCE_INTERVAL,
                                               this);
    }

    /**
     * Method to be called in onPause() method in the activity to stop
     * receiving location updates.
     */
    public void pauseLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    /**
     * Method used to ask the user to enable the GPS function if it wasn't
     * already enabled.
     */
    private void askToEnableProvider() {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(activity);

        alertDialogBuilder.setMessage(R.string.alert_dialog_ask_enable_provider_message)
                .setTitle(R.string.alert_dialog_ask_enable_provider_title);

        alertDialogBuilder
                .setPositiveButton(R.string.alert_dialog_yes,
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(
                                               DialogInterface dialogInterface,
                                               int i) {
                                           dialogInterface.dismiss();
                                           Intent intent =
                                                   new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                           context.startActivity(intent);
                                       }
                                   });
        alertDialogBuilder
                .setNegativeButton(R.string.alert_dialog_no,
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(
                                               DialogInterface dialogInterface,
                                               int i) {
                                           dialogInterface.dismiss();
                                       }
                                   });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
