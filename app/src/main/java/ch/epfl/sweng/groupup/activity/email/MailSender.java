package ch.epfl.sweng.groupup.activity.email;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

public class MailSender extends AsyncTask<URL, Integer, Long> {
    protected Long doInBackground(URL... urls) {
        try {
            GMailSender sender = new GMailSender("swenggroupup@gmail.com", "swengswengsweng");
            sender.sendMail("Hello ✌️",
                    "That app rocks!",
                    "swenggroupup@gmail.com",
                    "cedric.maire@epfl.ch");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        return new Long(0);
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(Long result) {
    }
}
