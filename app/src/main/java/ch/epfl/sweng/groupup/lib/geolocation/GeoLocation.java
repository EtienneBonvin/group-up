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
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.lib.AndroidHelper;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;


public final class GeoLocation implements GeoLocationInterface {

    private static final long MIN_UPDATE_TIME_INTERVAL = 5000;
    private static final float MIN_UPDATE_DISTANCE_INTERVAL = 5;
    private static final String ASK_PERMISSION = "ASK_PERMISSION";
    private static final String ASK_ENABLE_GPS = "ASK_ENABLE_GPS";
    private static boolean alreadyAskedPermission = false;
    private static boolean alreadyAskedEnableGps = false;
    private static boolean dialogIsShown = false;
    private static final String INTENT_SCHEME = "package";
    private final Activity activity;
    private final Context context;
    private final LocationManager locationManager;
    private final String provider;


    public GeoLocation(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (AndroidHelper.isEmulator()) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            provider = locationManager.getBestProvider(getCriteria(), false);

            if (!dialogIsShown) {
                if (provider == null && !alreadyAskedPermission) {
                    askToEnableProvider(ASK_PERMISSION);
                } else if (provider != null && !locationManager.isProviderEnabled(provider) && !alreadyAskedEnableGps) {
                    askToEnableProvider(ASK_ENABLE_GPS);
                }
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Account.shared.withLocation(location);
            Database.update();
        }
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        switch (i) {
            case LocationProvider.OUT_OF_SERVICE:
                pauseLocationUpdates();
                AndroidHelper.showToast(context, "Provider \"" + s + "\" out of service.", Toast.LENGTH_SHORT);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                pauseLocationUpdates();
                AndroidHelper.showToast(context, "Provider \"" + s + "\" unavailable.", Toast.LENGTH_SHORT);
                break;
            case LocationProvider.AVAILABLE:
                requestLocationUpdates();
                AndroidHelper.showToast(context, "Provider \"" + s + "\" available.", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }


    @Override
    public void onProviderEnabled(String s) {
        requestLocationUpdates();
        AndroidHelper.showToast(context, "Provider \"" + s + "\" enabled.", Toast.LENGTH_SHORT);
    }


    @Override
    public void onProviderDisabled(String s) {
        pauseLocationUpdates();
        AndroidHelper.showToast(context, "Provider \"" + s + "\" disabled.", Toast.LENGTH_SHORT);
    }


    /**
     * Eases the creation of the criteria we want for the localisation.
     *
     * @return - the criteria to use for choosing the right localisation
     * provider
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_LOW);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_LOW);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_LOW);
        criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);

        return criteria;
    }


    /**
     * Method to be called in the onCreate()/onResume() method of the activity
     * to start listening for location updates.
     */
    public void requestLocationUpdates() {
        if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
             PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(context,
                                                                                       Manifest.permission
                                                                                               .ACCESS_COARSE_LOCATION) !=
                                                    PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        locationManager.requestLocationUpdates(provider, MIN_UPDATE_TIME_INTERVAL, MIN_UPDATE_DISTANCE_INTERVAL, this);
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
    private void askToEnableProvider(final String whatToAsk) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                                                                                                 R.style.AboutDialog));

        alertDialogBuilder.setMessage(R.string.alert_dialog_ask_enable_provider_message)
                          .setTitle(R.string.alert_dialog_ask_enable_provider_title);

        alertDialogBuilder.setPositiveButton(R.string.alert_dialog_yes, getPositiveOnClick(whatToAsk));
        alertDialogBuilder.setNegativeButton(R.string.alert_dialog_no, getNegativeOnClick(whatToAsk));

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        dialogIsShown = true;
    }


    /**
     * Returns the on click listener for the positive button of the dialog.
     *
     * @param whatToAsk - what we need to ask to the user
     *
     * @return - callback for the positive button
     */
    private DialogInterface.OnClickListener getPositiveOnClick(final String whatToAsk) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogIsShown = false;
                dialogInterface.dismiss();

                switch (whatToAsk) {
                    case ASK_PERMISSION: {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts(INTENT_SCHEME, activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                        break;
                    }
                    case ASK_ENABLE_GPS: {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
    }


    /**
     * Returns the on click listener for the negative button on the dialog.
     *
     * @return - callback for the negative button
     */
    private DialogInterface.OnClickListener getNegativeOnClick(final String whatToAsk) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogIsShown = false;

                if (whatToAsk.equals(ASK_PERMISSION)) {
                    alreadyAskedPermission = true;
                } else if (whatToAsk.equals(ASK_ENABLE_GPS)) {
                    alreadyAskedEnableGps = true;
                }

                dialogInterface.dismiss();
            }
        };
    }
}
