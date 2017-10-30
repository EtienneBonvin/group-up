package ch.epfl.sweng.groupup.activity.email;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ch.epfl.sweng.groupup.R;

public class EmailActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //final Button send = (Button) this.findViewById(R.id.send);
        //send.setOnClickListener(new View.OnClickListener() {

            //public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    GMailSender sender = new GMailSender("swenggroupup@gmail.com", "swengswengsweng");
                    sender.sendMail("This is Subject",
                            "This is Body",
                            "xavpantet@gmail.com",
                            "xavpantet@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }

            //}
        //});

    }
}
