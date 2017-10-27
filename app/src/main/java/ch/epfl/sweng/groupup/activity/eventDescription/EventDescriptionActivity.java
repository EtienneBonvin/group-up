package ch.epfl.sweng.groupup.activity.eventDescription;

import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ch.epfl.sweng.groupup.R;

/**
 * Created by alix on 10/26/17.
 */

public class EventDescriptionActivity extends AppCompatActivity{

    TextView displayEventName;
    TextView displayEventStartDate;
    TextView displayEventEndDate;
    TextView displayEventMembers;
    TextView displayEventDescription;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);

        initializeField();

    }

    public void initializeField(){
        displayEventName= findViewById(R.id.event_description_tv_name);
        displayEventStartDate= findViewById(R.id.event_description_tv_start_date);
        displayEventEndDate=findViewById(R.id.event_description_tv_start_date);
        displayEventDescription=findViewById(R.id.event_description_tv_description);
        displayEventMembers = findViewById(R.id.event_description_tv_members);
    }
}
