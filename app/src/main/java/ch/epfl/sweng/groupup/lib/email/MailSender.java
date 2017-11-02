package ch.epfl.sweng.groupup.lib.email;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class MailSender extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... receiverEmailAddress) {
        try {
            for (String recMail : receiverEmailAddress) {
                GMailSender sender = new GMailSender();
                sender.sendMail(
                        recMail);
            }
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        return null;
    }
}
