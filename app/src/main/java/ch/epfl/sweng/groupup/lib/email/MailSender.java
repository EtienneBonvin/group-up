package ch.epfl.sweng.groupup.lib.email;

import android.os.AsyncTask;
import android.util.Log;

public class MailSender extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            GMailSender sender = new GMailSender("swenggroupup@gmail.com", "swengswengsweng");
            sender.sendMail(
                    "Hello ✌️",
                    "That app rocks!",
                    "GroupUp! <swenggroupup@gmail.com>",
                    "cedric.maire@epfl.ch");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        return null;
    }
}
