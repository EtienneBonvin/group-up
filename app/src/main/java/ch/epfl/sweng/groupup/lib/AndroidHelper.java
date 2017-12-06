package ch.epfl.sweng.groupup.lib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.R;

/**
 * Class containing some useful methods to help the programmer out.
 */

public final class AndroidHelper {

    /**
     * Check that the passed email is an "acceptable" form (not the icann official definition)
     * @param email the email to check
     * @return true if email ok else false
     */
    public static boolean emailCheck(String email){
        Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,13}\\b",Pattern.CASE_INSENSITIVE);
        Matcher m=p.matcher(email);
        return m.matches();
    }

    private static Toast lastShowedToast = null;

    /**
     * Private constructor, we don't want to instantiate this class.
     */
    private AndroidHelper() {
        // Not instantiable.
    }

    /**
     * Method to help displaying an alert personalized with the given parameters. This alert is
     * only used to inform the user of an event, it simply gets dismissed when clicked on the
     * button. It return the shown alert for further modifications.
     *
     * @param context    - current context of the activity
     * @param title      - alert title
     * @param message    - alert message
     * @param buttonText - button text
     * @return - the alert dialog that is shown
     */
    public static AlertDialog showAlert(Context context,
                                        String title,
                                        String message,
                                        String
                                                buttonText) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AboutDialog)).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                              buttonText,
                              new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog,
                                                      int which) {
                                      dialog.dismiss();
                                  }
                              });

        alertDialog.show();

        return alertDialog;
    }

    /**
     * Method to help displaying toasts on the screen. Pass in the required parameters and it
     * will show the toast to inform the user. It return the shown toast for further modifications.
     *
     * @param context  - current context for the toast
     * @param text     - the text to display
     * @param duration - the duration
     * @return - the toast that is shown
     */
    public static Toast showToast(Context context, String text, int duration) {
        Toast newToast = Toast.makeText(context, text, duration);

        if (lastShowedToast != null) {
            lastShowedToast.cancel();
        }
        lastShowedToast = newToast;

        newToast.show();

        return newToast;
    }

    /**
     * Returns whether we are running on an emulator or not.
     * Source: https://stackoverflow.com/questions/2799097/how-can-i-detect
     * -when-an-android-application-is-running-in-the-emulator
     *
     * @return - true if we are on an emulator
     */
    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic") ||
               Build.FINGERPRINT.startsWith("unknown") ||
               Build.MODEL.contains("google_sdk") ||
               Build.MODEL.contains("Emulator") ||
               Build.MODEL.contains("Android SDK built for x86") ||
               Build.MANUFACTURER.contains("Genymotion") ||
               (Build.BRAND.startsWith("generic") &&
                Build.DEVICE.startsWith("generic")) ||
               "google_sdk".equals(Build.PRODUCT);
    }
}
