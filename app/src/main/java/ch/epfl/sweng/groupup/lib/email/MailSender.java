package ch.epfl.sweng.groupup.lib.email;

import android.os.AsyncTask;
import android.util.Log;

public class MailSender extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... receiverEmailAddress) {
        try {
            for (String recMail : receiverEmailAddress) {
                GMailSender sender = new GMailSender("swenggroupup@gmail.com", "swengswengsweng");
                sender.sendMail(
                        "Hello ✌️",
                        "That app rocks!",
                        "GroupUp! <swenggroupup@gmail.com>",
                        recMail);
            }
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        return null;
    }
}
