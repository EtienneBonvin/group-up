package ch.epfl.sweng.groupup.activity.event.eventInvitation;

import android.app.Activity;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

import ch.epfl.sweng.groupup.R;

/**
 * Created by alix on 11/13/17.
 */

public class EventInvitationActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_invitation);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height= (int) (dm.heightPixels*0.8);
        int width= (int) (dm.widthPixels*0.8);

        getWindow().setLayout(width,height);
    }


}
