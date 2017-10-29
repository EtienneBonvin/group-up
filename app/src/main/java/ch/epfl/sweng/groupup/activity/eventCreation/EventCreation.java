package ch.epfl.sweng.groupup.activity.eventCreation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventListing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * EventCreation class
 * Offers the possibility to the user to create a new event.
 * Is linked to the layout event_creation.xml
 */
public class EventCreation extends ToolbarActivity implements ZXingScannerView.ResultHandler, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private boolean set_start_date, set_end_date, set_start_time, set_end_time;
    private int numberOfMembers;
    private HashMap<View.OnClickListener, View> viewsWithOCL;
    private HashMap<View.OnClickListener, String> uIdsWithOCL;
    private ZXingScannerView mScannerView;

    private EventBuilder builder;

    /**
     * Initialization of all the variables of the class and of the OnClickListeners
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);
        super.initializeToolbar();

        initFields();

        initListeners();

    }

    /**
     * Initialize all fields that will be used in the UI to default values.
     */
    private void initFields(){

        builder = new EventBuilder();

        set_start_date = false;
        set_end_date = false;
        set_start_time = false;
        set_end_time = false;

        numberOfMembers = 0;

        viewsWithOCL = new HashMap<>();
        uIdsWithOCL = new HashMap<>();

        ((Button)findViewById(R.id.button_start_date))
                .setText(date_format(
                        builder.getStartDate().getDayOfMonth(),
                        builder.getStartDate().getMonthOfYear(),
                        builder.getStartDate().getYear()));

        ((Button)findViewById(R.id.button_start_time))
                .setText(time_format(
                        builder.getStartDate().getHourOfDay(),
                        builder.getStartDate().getMinuteOfHour()));

        ((Button)findViewById(R.id.button_end_date))
                .setText(date_format(
                        builder.getEndDate().getDayOfMonth(),
                        builder.getEndDate().getMonthOfYear(),
                        builder.getEndDate().getYear()));

        ((Button)findViewById(R.id.button_end_time))
                .setText(time_format(
                        builder.getEndDate().getHourOfDay(),
                        builder.getEndDate().getMinuteOfHour()));

        datePickerDialog = new DatePickerDialog(
                this, EventCreation.this,
                builder.getStartDate().getYear(),
                builder.getStartDate().getMonthOfYear() - 1,
                builder.getStartDate().getDayOfMonth());

        timePickerDialog = new TimePickerDialog(
                this, EventCreation.this,
                builder.getStartDate().getHourOfDay(),
                builder.getStartDate().getMinuteOfHour(), true);
    }

    /**
     * Initialize the OnClickListeners of the layout.
     */
    private void initListeners(){

        super.initializeToolbar();

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
                        EditText memberUId = findViewById(R.id.edit_text_add_member);
                        addNewMember(memberUId.getText().toString());
                        memberUId.setText("");
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

    /**
     * Describe the behavior of the app when the back button is pressed while using the QR scanner
     */
    @Override
    public void onBackPressed() {
        if (mScannerView != null) {
            mScannerView.stopCamera();
            setContentView(R.layout.event_creation);
        }
        initListeners();
        restoreState();
    }

    /**
     * Overrides the behavior of the app when the QR Scanner is called. Current state of layout
     * saved.
     * @param view
     */
    public void QrScanner(View view){
        // TODO: 18.10.2017 Check if user granted camera access to app
        mScannerView = new ZXingScannerView(this);
        saveState();
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    /**
     * Saves the current state of the layout :
     *      - Event name
     *      - Already added members
     */
    private void saveState(){
        builder.setEventName(((EditText)findViewById(R.id.ui_edit_event_name)).getText().toString());
        // Change this line when the description will be available
        builder.setDescription("");
        builder.setMembersTo(uIdsWithOCL.values());
    }

    /**
     * Restores the current state of the layout :
     *      - Event name
     *      - Already added members
     *      - State date and time
     *      - End date and time
     */
    private void restoreState(){
        ((EditText)findViewById(R.id.ui_edit_event_name)).setText(builder.getEventName());

        for(Member member : builder.members){
            if(!member.getUUID().isEmpty()) {
                addNewMember(member.getUUID().get());
            }else if(!member.getEmail().isEmpty()){
                addNewMember(member.getEmail().get());
            }
        }

        ((Button)findViewById(R.id.button_start_date))
                .setText(date_format(
                        builder.getStartDate().getDayOfMonth(),
                        builder.getStartDate().getMonthOfYear(),
                        builder.getStartDate().getYear()));
        ((Button)findViewById(R.id.button_end_date))
                .setText(date_format(
                        builder.getEndDate().getDayOfMonth(),
                        builder.getEndDate().getMonthOfYear(),
                        builder.getEndDate().getYear()));
        ((Button)findViewById(R.id.button_start_time))
                .setText(time_format(
                        builder.getStartDate().getHourOfDay(),
                        builder.getStartDate().getMinuteOfHour()));
        ((Button)findViewById(R.id.button_end_time))
                .setText(time_format(
                        builder.getEndDate().getHourOfDay(),
                        builder.getEndDate().getMinuteOfHour()));
    }

    /**
     * Describes the behavior of the app when the QR Scanner get its results.
     * @param rawResult
     */
    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        // Do something with the result here
        String qrString = rawResult.toString();

        // Close camera and return to activity after successful scan
        mScannerView.stopCamera();
        setContentView(R.layout.event_creation);
        initListeners();
        restoreState();
        addNewMember(rawResult.getText());
    }


    /**
     * Adds a line in the member list on the UI with the user ID address specified by the user
     * @param memberUId
     */
    private void addNewMember(String memberUId) {
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
        textView.setText(memberUId);
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
                uIdsWithOCL.remove(this);
            }
        };

        minus.setOnClickListener(ocl);
        viewsWithOCL.put(ocl, newMember);
        uIdsWithOCL.put(ocl, memberUId);
    }

    /**
     * Overrides the method onDateSet of the interface DatePickerDialog.OnDateSetListener,
     * changes the text of the button on the UI accordingly to the data entered by the user
     * on the DatePickerDialog.
     * @param view
     * @param year int containing year
     * @param month int containing month
     * @param dayOfMonth int containing date
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(set_start_date) {
            builder.setStartDate(year, month + 1, dayOfMonth);
            ((Button)findViewById(R.id.button_start_date))
                    .setText(date_format(dayOfMonth, month + 1, year));
            set_start_date = false;
        }else if(set_end_date){
            builder.setEndDate(year, month + 1, dayOfMonth);
            ((Button)findViewById(R.id.button_end_date))
                    .setText(date_format(dayOfMonth, month + 1, year));
            set_end_date = false;
        }
    }

    /**
     * Overrides the method onTimeSet of the interface TimePickerDialog.OnTimeSetListener,
     * changes the text of the button on the UI accordingly to the data entered by the user
     * on the TimePickerDialog.
     * @param view
     * @param hourOfDay int containing hour
     * @param minute int containing minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(set_start_time) {
            builder.setStartTime(hourOfDay, minute);
            ((Button)findViewById(R.id.button_start_time))
                    .setText(time_format(hourOfDay, minute));
            set_start_time = false;
        }else if(set_end_time){
            builder.setEndTime(hourOfDay, minute);
            ((Button)findViewById(R.id.button_end_time))
                    .setText(time_format(hourOfDay, minute));
            set_end_time = false;
        }
    }

    /**
     * Method called when the 'Save event' button is clicked.
     * It registers the informations entered by the user, verify them and bring him to the
     * group list Activity.
     */
    private void saveEvent(){

        int INPUT_MAX_LENGTH = 50;

        EditText eventName = findViewById(R.id.ui_edit_event_name);
        if(eventName.getText().toString().length() == 0){
            eventName.setError(getString(R.string.event_creation_toast_non_empty_event_name));
            return;
        }else if(eventName.getText().toString().length() > INPUT_MAX_LENGTH){
            eventName.setError(getString(R.string.event_creation_toast_event_name_too_long));
            return;
        }
        eventName.setError(null);

        if(compare_date(LocalDateTime.now(), builder.getStartDate()) < 0){
            Toast.makeText(getApplicationContext(), getString(R.string.event_creation_toast_event_start_before_now),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(compare_date(builder.getStartDate(), builder.getEndDate()) < 0){
            Toast.makeText(getApplicationContext(), getString(R.string.event_creation_toast_event_end_before_begin),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(compare_date(builder.getStartDate(), builder.getEndDate()) == 0){
            Toast.makeText(getApplicationContext(), getString(R.string.event_craeation_toast_event_last_1_minute),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        builder.setEventName(((EditText)findViewById(R.id.ui_edit_event_name)).getText().toString());
        //Change this line when description will be available
        builder.setDescription("");
        builder.setMembersTo(uIdsWithOCL.values());

        Account.shared.addOrUpdateEvent(builder.build());
        Database.update();

        Intent intent = new Intent(this, EventListingActivity.class);
        startActivity(intent);
    }

    /**
     * Private method to compare two LocalDateTime to the minute level.
     * @param start LocalDateTime containing starting time
     * @param end LocalDateTime containing ending time
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
     * @param day int containing date
     * @param month int containing month
     * @param year int containing year
     * @return a DD/MM/YY string
     */
    private String date_format(int day, int month, int year){
        return String.format(Locale.getDefault(), "%02d", day)+"/"+
                String.format(Locale.getDefault(), "%02d", month)+"/"+
                String.format(Locale.getDefault(), "%02d", (year%100));
    }

    /**
     * Format a time into a HH:MM string.
     * @param hour int containing hour
     * @param minutes int containing minute
     * @return a HH:MM string
     */
    private String time_format(int hour, int minutes){
        return String.format(Locale.getDefault(), "%02d", hour)+":"+
                String.format(Locale.getDefault(), "%02d", minutes);
    }

    /**
     * Event builder. Follows the Design Pattern of a builder.
     */
    protected class EventBuilder{

        private String eventName = "";
        private String description = "";
        private LocalDateTime startDate = LocalDateTime.now().plusMinutes(5);
        private LocalDateTime endDate = LocalDateTime.now().plusMinutes(6);
        private Set<Member> members = new HashSet<>();

        protected EventBuilder(){}

        /**
         * Setter for the name of the event.
         * @param eventName the name of the event.
         */
        protected void setEventName(String eventName){
            this.eventName = eventName;
        }

        /**
         * Getter for the name of the event.
         * @return the name of the event.
         */
        protected String getEventName(){
            return eventName;
        }

        /**
         * Setter for the description of the event.
         * @param description the description of the event.
         */
        protected void setDescription(String description){
            this.description = description;
        }

        /**
         * Getter for the descritpion of the event.
         * @return the description of the event.
         */
        protected String getDescription(){
            return description;
        }

        /**
         * Setter for the start date of the event.
         * @param year the year.
         * @param monthOfYear the month of the year.
         * @param dayOfMonth the day of the month.
         */
        protected void setStartDate(int year, int monthOfYear, int dayOfMonth){
            startDate = startDate.withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth(dayOfMonth);
        }

        /**
         * Setter for the end date of the event.
         * @param year the year.
         * @param monthOfYear the month of the year.
         * @param dayOfMonth the day of the month.
         */
        protected void setEndDate(int year, int monthOfYear, int dayOfMonth){
            endDate = endDate.withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth(dayOfMonth);
        }

        /**
         * Setter for the start time of the event.
         * @param hoursOfDay the hour of the day.
         * @param minutesOfHour the minute of the hour.
         */
        protected void setStartTime(int hoursOfDay, int minutesOfHour){
            startDate = startDate.withHourOfDay(hoursOfDay).withMinuteOfHour(minutesOfHour);
        }

        /**
         * Setter for the end time of the event.
         * @param hoursOfDay the hour of the day.
         * @param minutesOfHour the minute of the hour.
         */
        protected void setEndTime(int hoursOfDay, int minutesOfHour){
            endDate = endDate.withHourOfDay(hoursOfDay).withMinuteOfHour(minutesOfHour);
        }

        /**
         * Getter for the start time of the event under the form of a LocaleDateTime.
         * Both the start date and the start time are in it.
         * @return a LocaleDateTime for the start date and time.
         */
        protected LocalDateTime getStartDate(){
            return new LocalDateTime(startDate);
        }

        /**
         * Getter for the end time of the event under the form of a LocaleDateTime.
         * Both the end date and the end time are in it.
         * @return a LocaleDateTime for the end date and time.
         */
        protected LocalDateTime getEndDate(){
            return new LocalDateTime(endDate);
        }

        /**
         * Add a new member to the event.
         * If the member already belongs to the event he won't be added a second time.
         * @param newMember the new member to add.
         */
        protected void addMember(Member newMember){
            members.add(newMember);
        }

        /**
         * Remove a member from the event.
         * If the member isn't already in the event, nothing will happen.
         * @param member the member to remove.
         */
        protected void removeMember(Member member){
            if(members.contains(member)){
                members.remove(member);
            }
        }

        /**
         * Remove all the members added until now to the event.
         */
        protected void cleanMembers(){
            members = new HashSet<>();
        }

        /**
         * Set the list of the members according to the set of String given as argument.
         * The Strings can be either the UUID either the email address of the member.
         * @param members the set of the representative strings of the members
         */
        protected void setMembersTo(Collection<String> members){
            cleanMembers();
            Member emptyMember = new Member(Optional.<String>empty(), Optional.<String>empty(),
                    Optional.<String>empty(), Optional.<String>empty(), Optional.<String>empty());
            for(String s : members){
                if(emailCheck(s)){
                    addMember(emptyMember.withEmail(s));
                }else{
                    addMember(emptyMember.withUUID(s));
                }
            }
        }

        /**
         * Returns a List of all the members added until now to the event.
         * @return a List containing all the event members.
         */
        protected List<Member> getMembers(){
            List<Member> membersList = new ArrayList<>();
            membersList.addAll(members);
            return membersList;
        }

        /**
         * Builds an event containing all the properties set until now.
         * Note : the user who is creating the event is automatically added to the list of the members.
         * @return an event containing all the properties set until now.
         */
        protected Event build(){
            List<Member> members = getMembers();
            members.add(new Member(Optional.<String>empty(),
                    Optional.<String>empty(), Optional.<String>empty(),
                    Optional.<String>empty(), Optional.<String>empty())
                    .withUUID(Account.shared.getUUID().getOrElse("Default UUID")));
            return new Event(eventName, startDate, endDate, description, getMembers());
        }

        /**
         * Check that the passed email is an "acceptable" form (not the icann official definition)
         * @param email the email to check
         * @return true if email ok else false
         */
        private boolean emailCheck(String email){
            Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,13}\\b",Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(email);
            return m.matches();
        }
    }
}
