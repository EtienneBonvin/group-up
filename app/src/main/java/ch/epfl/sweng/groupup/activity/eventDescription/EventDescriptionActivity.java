package ch.epfl.sweng.groupup.activity.eventDescription;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

/**
 * Created by alix on 10/26/17.
 */

public class EventDescriptionActivity extends ToolbarActivity {
    LinearLayout linear;
    TextView displayEventName;
    TextView displayEventStartDate;
    TextView displayEventEndDate;
    TextView displayEventMembers;
    TextView displayEventDescription;
    Event eventToDisplay;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        super.initializeToolbar();
        initializeField();

        //To test before while I don't have an Intent, destined to be deleted of curse
        eventToDisplay = new Event("Name", new LocalDateTime(), new LocalDateTime().plusDays(1),
                "My amazing description", new ArrayList<>(Arrays.asList(new Member("1","displayed","","",""),
                new Member("2","YOLO","","",""),new Member("3","LOOOOOOOOOOOOOOL","","",""))));

        printEvent();

        findViewById(R.id.remove_event_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeEvent();
                    }
                });
    }

    /**
     * Remove the user from the Event
     */
    private void removeEvent() {
        List<Member> futureMember = eventToDisplay.getEventMembers();
        futureMember.remove(Account.shared.toMember());

        Account.shared.addOrUpdateEvent(eventToDisplay.withEventMembers(futureMember));
    }

    public void initializeField(){
        displayEventName= findViewById(R.id.event_description_name);
        displayEventStartDate= findViewById(R.id.event_description_start_date);
        displayEventEndDate=findViewById(R.id.event_description_end_date);
        displayEventDescription=findViewById(R.id.event_description_description);
        displayEventMembers = findViewById(R.id.event_description_tv_members);
    }

    public void printEvent() {
       /* displayEventName.setText(eventToDisplay.getEventName());
        displayEventStartDate.setText(eventToDisplay.getStartTime().toString(null, Locale.FRANCE));
        displayEventEndDate.setText(eventToDisplay.getEndTime().toString(null, Locale.FRANCE));
        displayEventDescription.setText(eventToDisplay.getDescription());*/

        for (Member member : eventToDisplay.getEventMembers()) {
            TextView memberName = new TextView(this);
            memberName.setText(member.getDisplayName().getOrElse("NO_MAME"));
            linear = findViewById(R.id.linear_scroll_members);
            linear.addView(memberName);
        }
    }
}
