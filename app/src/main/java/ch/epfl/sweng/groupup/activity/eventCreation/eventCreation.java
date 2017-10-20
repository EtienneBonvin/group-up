package ch.epfl.sweng.groupup.activity.eventCreation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventListing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.settings.Settings;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * EventCreation class
 * Offers the possibility to the user to create a new event.
 * Is linked to the layout event_creation.xml
 */
public class EventCreation extends AppCompatActivity implements ZXingScannerView.ResultHandler, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{

    private final int INPUT_MAX_LENGTH = 50;

    private Event finalEvent;
    private Button start_date, end_date, start_time, end_time;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private boolean set_start_date, set_end_date, set_start_time, set_end_time;
    private int numberOfMembers;
    private HashMap<View.OnClickListener, View> viewsWithOCL;
    private HashMap<View.OnClickListener, String> emailWithOCL;
    private LocalDateTime date_start, date_end;
    private ZXingScannerView mScannerView;
    private String qrString;

    /**
     * Initialization of all the variables of the class and of the OnClickListeners
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);

        date_start = LocalDateTime.now();
        date_end = LocalDateTime.now();

        set_start_date = false;
        set_end_date = false;
        set_start_time = false;
        set_end_time = false;

        numberOfMembers = 0;

        viewsWithOCL = new HashMap<>();
        emailWithOCL = new HashMap<>();

        start_date = (Button)findViewById(R.id.button_start_date);
        start_date.setText(date_format(date_start.getDayOfMonth(), date_start.getMonthOfYear(),
                date_start.getYear()));

        start_time = (Button)findViewById(R.id.button_start_time);
        start_time.setText(time_format(date_start.getHourOfDay(), date_start.getMinuteOfHour()));

        end_date = (Button)findViewById(R.id.button_end_date);
        end_date.setText(date_format(date_end.getDayOfMonth(), date_end.getMonthOfYear(),
                date_end.getYear()));

        end_time = (Button)findViewById(R.id.button_end_time);
        end_time.setText(time_format(date_end.getHourOfDay(), date_end.getMinuteOfHour()));

        datePickerDialog = new DatePickerDialog(
                this, EventCreation.this, date_start.getYear(), date_start.getMonthOfYear(),
                date_start.getDayOfMonth());

        timePickerDialog = new TimePickerDialog(
                this, EventCreation.this, date_start.getHourOfDay(), date_start.getMinuteOfHour(), true);

        initListeners();

    }

    private void initListeners(){
        findViewById(R.id.icon_access_group_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), EventListingActivity.class);
                        startActivity(intent);
                    }
                });

        findViewById(R.id.icon_access_settings)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Settings.class);
                        startActivity(intent);
                    }
                });

        findViewById(R.id.button_start_date)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_start_date = true;
                        datePickerDialog.show();
                    }
                });

        findViewById(R.id.button_end_date)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_end_date = true;
                        datePickerDialog.show();
                    }
                });

        findViewById(R.id.button_start_time)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_start_time = true;
                        timePickerDialog.show();
                    }
                });
        findViewById(R.id.button_end_time)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_end_time = true;
                        timePickerDialog.show();
                    }
                });

        findViewById(R.id.image_view_add_member)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText memberMail = (EditText)findViewById(R.id.edit_text_add_member);
                        addNewMember(memberMail.getText().toString());
                        memberMail.setText("");
                    }
                });
        findViewById(R.id.buttonScanQR)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QrScanner(v);
                    }
                });

        findViewById(R.id.save_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEvent();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (mScannerView != null) {
            mScannerView.stopCamera();
            setContentView(R.layout.event_creation);
        }
    }

    public void QrScanner(View view){
        // TODO: 18.10.2017 Check if user granted camera access to app
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        // Do something with the result here
        qrString = rawResult.toString();

        // Close camera and return to activity after successful scan
        mScannerView.stopCamera();
        setContentView(R.layout.event_creation);
        initListeners();
        addNewMember(rawResult.getText());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        // TODO

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // TODO
    }


        /**
         * Adds a line in the member list on the UI with the email address specified by the user
         * with the possibility to remove it by linking the OnClickListener to the instance of
         * LinearLayout created.
         * @param memberMail
         */
    private void addNewMember(String memberMail) {
        numberOfMembers++;

        LinearLayout newMember = new LinearLayout(this);
        newMember.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f));
        textView.setText(memberMail);
        textView.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                0.1f);
        params.setMargins(2, 2, 2, 2);
        ImageView minus = new ImageView(this);
        minus.setImageResource(R.drawable.minussign);
        minus.setLayoutParams(params);
        minus.setId(numberOfMembers);
        minus.setBackgroundColor(Color.BLACK);

        newMember.addView(textView);
        newMember.addView(minus);

        ((LinearLayout) findViewById(R.id.members_list))
                .addView(newMember);

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.members_list))
                        .removeView(
                                viewsWithOCL.get(this)
                        );
                emailWithOCL.remove(this);
            }
        };

        minus.setOnClickListener(ocl);
        viewsWithOCL.put(ocl, newMember);
        emailWithOCL.put(ocl, memberMail);
    }

    /**
     * Overrides the method onDateSet of the interface DatePickerDialog.OnDateSetListener,
     * changes the text of the button on the UI accordingly to the data entered by the user
     * on the DatePickerDialog.
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(set_start_date) {
            date_start = new LocalDateTime(year, month, dayOfMonth,
                    date_start.getHourOfDay(), date_start.getMinuteOfHour());
            start_date.setText(date_format(dayOfMonth, month, year));
            set_start_date = false;
        }else if(set_end_date){
            date_end = new LocalDateTime(year, month, dayOfMonth,
                    date_end.getHourOfDay(), date_end.getMinuteOfHour());
            end_date.setText(date_format(dayOfMonth, month, year));
            set_end_date = false;
        }
    }

    /**
     * Overrides the method onTimeSet of the interface TimePickerDialog.OnTimeSetListener,
     * changes the text of the button on the UI accordingly to the data entered by the user
     * on the TimePickerDialog.
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(set_start_time) {
            date_start = new LocalDateTime(date_start.getYear(), date_start.getMonthOfYear(),
                    date_start.getDayOfMonth(), hourOfDay, minute);
            start_time.setText(time_format(hourOfDay, minute));
            set_start_time = false;
        }else if(set_end_time){
            date_end = new LocalDateTime(date_end.getYear(), date_end.getMonthOfYear(),
                    date_end.getDayOfMonth(), hourOfDay, minute);
            end_time.setText(time_format(hourOfDay, minute));
            set_end_time = false;
        }
    }

    /**
     * Method called when the 'Save event' button is clicked.
     * It registers the informations entered by the user, verify them and bring him to the
     * group list Activity.
     */
    private void saveEvent(){
        EditText eventName = ((EditText)findViewById(R.id.ui_edit_event_name));
        if(eventName.getText().toString().length() == 0){
            eventName.setError("Give a name to your event !");
            return;
        }else if(eventName.getText().toString().length() > INPUT_MAX_LENGTH){
            eventName.setError("The name of the event is too long.");
            return;
        }
        eventName.setError(null);

        if(compare_date(LocalDateTime.now(), date_start) < 0){
            Toast.makeText(this.getBaseContext(), "Are you planning to go back to the past ?",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(compare_date(date_start, date_end) < 0){
            Toast.makeText(this.getBaseContext(), "Your event ends before it begins.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(compare_date(date_start, date_end) == 0){
            Toast.makeText(this.getBaseContext(), "Your event should last for at least 1 minute.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Set<String> distinctEmails = new HashSet<>();
        for(View.OnClickListener ocl : emailWithOCL.keySet()){
            distinctEmails.add(emailWithOCL.get(ocl));
        }

        /*
         * This method does not guarantee that the email are all well written and valid, it just prevents
         * basic user error (this bit of code can be updated depending our needs).
          */
        /*for(String mail : distinctEmails){
            if(!emailCheck(mail)) {
                Toast.makeText(this.getBaseContext(), "One or more email address does not have the " +
                                "good format.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }*/

        List<Member> members = new ArrayList<>();
        Member emptyMember = new Member(Optional.<String>empty(), Optional.<String>empty(),
                Optional.<String>empty(), Optional.<String>empty());

        for(String email : distinctEmails){
            members.add(emptyMember.withEmail(email));
        }

        Event event = new Event(eventName.getText().toString(), date_start, date_end, members, 0);

        Account.shared.addFutureEvent(event);

        /*
         * Decomment this piece of code when the Activity showing the group list is implemented
         * Modify the 'GroupList.class' to the needed class.
         */
        Intent intent = new Intent(this, EventListingActivity.class);
        startActivity(intent);
    }

    /**
     * Private method to compare two LocalDateTime to the minute level.
     * @param start
     * @param end
     * @return 1 if start is before end of at least 1 minute, 0 if start and end are the same
     * to the minute level, -1 otherwise.
     */
    private int compare_date(LocalDateTime start, LocalDateTime end){
        if(start.getYear() > end.getYear()) return -1;
        if(start.getYear() < end.getYear()) return 1;
        if(start.getMonthOfYear() > end.getMonthOfYear()) return -1;
        if(start.getMonthOfYear() < end.getMonthOfYear()) return 1;
        if(start.getDayOfMonth() > end.getDayOfMonth()) return -1;
        if(start.getDayOfMonth() < end.getDayOfMonth()) return 1;
        if(start.getHourOfDay() > end.getHourOfDay()) return -1;
        if(start.getHourOfDay() < end.getHourOfDay()) return 1;
        if(start.getMinuteOfHour() > end.getMinuteOfHour()) return -1;
        if(start.getMinuteOfHour() < end.getMinuteOfHour()) return 1;
        return 0;
    }

    /**
     * Format a date into a DD/MM/YY string.
     * @param day
     * @param month
     * @param year
     * @return a DD/MM/YY string
     */
    private String date_format(int day, int month, int year){
        return String.format(Locale.getDefault(), "%02d", day)+"/"+
                String.format(Locale.getDefault(), "%02d", month+1)+"/"+
                String.format(Locale.getDefault(), "%02d", (year%100));
    }

    /**
     * Format a time into a HH:MM string.
     * @param hour
     * @param minutes
     * @return a HH:MM string
     */
    private String time_format(int hour, int minutes){
        return String.format(Locale.getDefault(), "%02d", hour)+":"+
                String.format(Locale.getDefault(), "%02d", minutes);
    }

    /**
     * Check that the passed email is an "acceptable" form (not the icann official definition)
     * @param email
     * @return true of email ok else false
     */
    private boolean emailCheck(String email){
        Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,13}\\b",Pattern.CASE_INSENSITIVE);
        Matcher m=p.matcher(email);
        return m.matches();
    }
}
