package ch.epfl.sweng.groupup.activity.event.creation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Helper;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.lib.email.GMailService;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;


/**
 * EventCreationActivity class
 * Offers the possibility to the user to create a new event.
 * Is linked to the layout event_creation.xml
 */
public class EventCreationActivity extends ToolbarActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, Serializable{

    private transient DatePickerDialog datePickerDialog;
    private transient TimePickerDialog timePickerDialog;
    private transient boolean set_start_date, set_end_date, set_start_time, set_end_time;

    private EventBuilder builder;

    /**
     * Initialization of all the variables of the class and of the OnClickListeners
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);
        super.initializeToolbarActivity();

        initFields();

        initListeners();

    }

    /**
     * Initialize all fields that will be used in the UI to default values.
     */
    private void initFields(){

        try {
            builder = (EventBuilder)getIntent().getSerializableExtra("Builder");
        }catch(Exception e){
            builder = new EventBuilder();
        }
        if(builder == null){
            builder = new EventBuilder();
        }

        set_start_date = false;
        set_end_date = false;
        set_start_time = false;
        set_end_time = false;

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

        ((TextView)findViewById(R.id.number_of_members))
                .setText(String.format(Locale.getDefault(),
                        "%s %d",
                        getString(R.string.event_creation_tv_number_of_members),
                        builder.getMembers().size()));

        ((EditText)findViewById(R.id.ui_edit_event_name))
                .setText(builder.getEventName());

        ((EditText)findViewById(R.id.edit_text_description))
                .setText(builder.getDescription());

        datePickerDialog = new DatePickerDialog(
                this, EventCreationActivity.this,
                builder.getStartDate().getYear(),
                builder.getStartDate().getMonthOfYear() - 1,
                builder.getStartDate().getDayOfMonth());

        timePickerDialog = new TimePickerDialog(
                this, EventCreationActivity.this,
                builder.getStartDate().getHourOfDay(),
                builder.getStartDate().getMinuteOfHour(), true);
    }

    /**
     * Initialize the OnClickListeners of the layout.
     */
    private void initListeners(){

        super.initializeToolbarActivity();

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

        findViewById(R.id.save_new_event_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEvent();
                    }
                });

        findViewById(R.id.button_add_members)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.setEventName(
                                ((EditText)findViewById(R.id.ui_edit_event_name))
                                .getText().toString());
                        builder.setDescription(
                                ((EditText)findViewById(R.id.edit_text_description))
                                .getText().toString());
                        Intent intent = new Intent(getApplicationContext(), MembersAddingActivity.class);
                        intent.putExtra("Builder", builder);
                        startActivity(intent);
                    }
                });
    }

    /**
     * Overrides the method onDateSet of the interface DatePickerDialog.OnDateSetListener,
     * changes the text of the button on the UI accordingly to the data entered by the user
     * on the DatePickerDialog.
     * @param view unused
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
     * @param view unused
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


        if(builder.getStartDate().isBefore(LocalDateTime.now())){
            Helper.showToast(getApplicationContext(),
                    getString(R.string.event_creation_toast_event_start_before_now),
                    Toast.LENGTH_SHORT);
            return;
        }

        if(builder.getEndDate().isBefore(builder.getStartDate())){
            Helper.showToast(getApplicationContext(),
                    getString(R.string.event_creation_toast_event_end_before_begin),
                    Toast.LENGTH_SHORT);
            return;
        }

        if(builder.getStartDate().isEqual(builder.getEndDate())){
            Helper.showToast(getApplicationContext(),
                    getString(R.string.event_creation_toast_event_last_1_minute),
                    Toast.LENGTH_SHORT);
            return;
        }

        for (Event e : Account.shared.getEvents()) {
            if (!((builder.getStartDate().compareTo(e.getStartTime()) <= 0
                    && builder.getEndDate().compareTo(e.getStartTime()) <= 0)
                    || (e.getEndTime().compareTo(builder.getStartDate()) <= 0))) {
                Helper.showToast(getApplicationContext(), getString(R.string.toast_overlapping_event),
                        Toast.LENGTH_LONG);
            return;
            }
        }
        builder.setEventName(((EditText)findViewById(R.id.ui_edit_event_name)).getText().toString());

        builder.setDescription(((EditText)findViewById(R.id.edit_text_description)).getText().toString());

        Account.shared.addOrUpdateEvent(builder.build());
        Database.update();

        Intent intent = new Intent(this, EventListingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
    protected class EventBuilder implements Serializable{

        private String eventName = "";
        private String description = "";
        private LocalDateTime startDate = LocalDateTime.now().plusMinutes(5)
                .withMillisOfSecond(0).withSecondOfMinute(0);
        private LocalDateTime endDate = LocalDateTime.now().plusMinutes(6)
                .withMillisOfSecond(0).withSecondOfMinute(0);
        private HashSet<String> members = new HashSet<>();

        private EventBuilder(){}

        /**
         * Setter for the name of the event.
         * @param eventName the name of the event.
         */
        private void setEventName(String eventName){
            this.eventName = eventName;
        }

        /**
         * Getter for the name of the event.
         * @return the name of the event.
         */
        private String getEventName(){
            return eventName;
        }

        /**
         * Setter for the description of the event.
         * @param description the description of the event.
         */
        private void setDescription(String description){
            this.description = description;
        }

        /**
         * Getter for the description of the event.
         * @return the description of the event.
         */
        private String getDescription(){
            return description;
        }

        /**
         * Setter for the start date of the event.
         * @param year the year.
         * @param monthOfYear the month of the year.
         * @param dayOfMonth the day of the month.
         */
        private void setStartDate(int year, int monthOfYear, int dayOfMonth){
            startDate = startDate.withYear(year)
                    .withMonthOfYear(monthOfYear)
                    .withDayOfMonth(dayOfMonth);
        }

        /**
         * Setter for the end date of the event.
         * @param year the year.
         * @param monthOfYear the month of the year.
         * @param dayOfMonth the day of the month.
         */
        private void setEndDate(int year, int monthOfYear, int dayOfMonth){
            endDate = endDate.withYear(year)
                    .withMonthOfYear(monthOfYear)
                    .withDayOfMonth(dayOfMonth);
        }

        /**
         * Setter for the start time of the event.
         * @param hoursOfDay the hour of the day.
         * @param minutesOfHour the minute of the hour.
         */
        private void setStartTime(int hoursOfDay, int minutesOfHour){
            startDate = startDate.withHourOfDay(hoursOfDay)
                    .withMinuteOfHour(minutesOfHour);
        }

        /**
         * Setter for the end time of the event.
         * @param hoursOfDay the hour of the day.
         * @param minutesOfHour the minute of the hour.
         */
        private void setEndTime(int hoursOfDay, int minutesOfHour){
            endDate = endDate.withHourOfDay(hoursOfDay)
                    .withMinuteOfHour(minutesOfHour);
        }

        /**
         * Getter for the start time of the event under the form of a LocaleDateTime.
         * Both the start date and the start time are in it.
         * @return a LocaleDateTime for the start date and time.
         */
        private LocalDateTime getStartDate(){
            return new LocalDateTime(startDate);
        }

        /**
         * Getter for the end time of the event under the form of a LocaleDateTime.
         * Both the end date and the end time are in it.
         * @return a LocaleDateTime for the end date and time.
         */
        private LocalDateTime getEndDate(){
            return new LocalDateTime(endDate);
        }

        /**
         * Add a new member to the event.
         * If the member already belongs to the event he won't be added a second time.
         * The member can be given under the form of its UID or its email address.
         * @param newMember the new member to add.
         */
        private void addMember(String newMember){
            members.add(newMember);
        }

        /**
         * Remove all the members added until now to the event.
         */
        private void cleanMembers(){
            members = new HashSet<>();
        }

        /**
         * Set the list of the members according to the set of String given as argument.
         * The Strings can be either the UUID either the email address of the member.
         * @param members the set of the representative strings of the members
         */
        @SuppressWarnings("WeakerAccess")
        protected void setMembersTo(Collection<String> members){
            cleanMembers();
            for(String s : members){
                addMember(s);
            }
        }

        /**
         * Returns a List of all the members added until now to the event.
         * @return a List containing all the event members.
         */
        protected List<String> getMembers(){
            List<String> membersList = new ArrayList<>();
            membersList.addAll(members);
            return membersList;
        }

        /**
         * Builds an event containing all the properties set until now.
         * Note : the user who is creating the event is automatically added to the list of the members.
         * @return an event containing all the properties set until now.
         */
        private Event build(){

            GMailService gms = new GMailService();

            members.add(Account.shared.getUUID().getOrElse("Default UUID"));
            List<Member> finalMembers = new ArrayList<>();
            Member emptyMember = new Member(Optional.<String>empty(), Optional.<String>empty(),
                    Optional.<String>empty(), Optional.<String>empty(),
                                            Optional.<String>empty(), Optional.<Location>empty());

            int nb_unknown = 0;

            List<String> mailsToSend = new ArrayList<>();

            for(String s : members){
                if(emailCheck(s)){
                    finalMembers.add(emptyMember
                            .withUUID(Member.UNKNOWN_USER + (++nb_unknown))
                            .withEmail(s));
                    mailsToSend.add(s);
                }else{
                    finalMembers.add(emptyMember.withUUID(s));
                }
            }

            gms.sendInvitationEmail(mailsToSend);

            return new Event(eventName, startDate, endDate, description, finalMembers,false);
            }
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
