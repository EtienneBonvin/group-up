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

        MailSender task = new MailSender();
        task.execute();
    }
}