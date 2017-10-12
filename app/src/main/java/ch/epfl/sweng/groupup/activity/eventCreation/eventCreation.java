package ch.epfl.sweng.groupup.activity.eventCreation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.GregorianCalendar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.groupup.R;

/**
 * EventCreation class
 * Offers the possibility to the user to create a new event.
 * Is linked to the layout event_creation.xml
 */
public class eventCreation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{

    private Button start_date, end_date, start_time, end_time;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar cal;
    private boolean set_start_date, set_end_date, set_start_time, set_end_time;
    private int numberOfMembers;
    private HashMap<View.OnClickListener, View> viewsWithOCL;

    /**
     * Initialization of all the variables of the class and of the OnClickListeners
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);

        cal = Calendar.getInstance();

        set_start_date = false;
        set_end_date = false;
        set_start_time = false;
        set_end_time = false;

        numberOfMembers = 0;

        viewsWithOCL = new HashMap<>();

        start_date = (Button)findViewById(R.id.button_start_date);
        start_date.setText(date_format(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH),
                cal.get(Calendar.YEAR)));

        start_time = (Button)findViewById(R.id.button_start_time);
        start_time.setText(time_format(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));

        end_date = (Button)findViewById(R.id.button_end_date);
        end_date.setText(date_format(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH),
                cal.get(Calendar.YEAR)));

        end_time = (Button)findViewById(R.id.button_end_time);
        end_time.setText(time_format(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));

        datePickerDialog = new DatePickerDialog(
                this, eventCreation.this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(
                this, eventCreation.this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);

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
                        addNewMember();
                    }
                });
    }

    /**
     * Adds a line in the member list on the UI with the email address specified by the user
     * with the possibility to remove it by linking the OnClickListener to the instance of
     * LinearLayout created.
     */
    private void addNewMember() {
        numberOfMembers++;

        LinearLayout newMember = new LinearLayout(this);
        newMember.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        EditText memberMail = (EditText) findViewById(R.id.edit_text_add_member);

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f));
        textView.setText(memberMail.getText());
        textView.setTextColor(Color.WHITE);
        memberMail.setText("");


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
            }
        };

        minus.setOnClickListener(ocl);
        viewsWithOCL.put(ocl, newMember);
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
            start_date.setText(date_format(dayOfMonth, month, year));
            set_start_date = false;
        }else if(set_end_date){
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
            start_time.setText(time_format(hourOfDay, minute));
            set_start_time = false;
        }else if(set_end_time){
            end_time.setText(time_format(hourOfDay, minute));
            set_end_time = false;
        }
    }

    /**
     * Format a date into a DD/MM/YY string.
     * @param day
     * @param month
     * @param year
     * @return a DD/MM/YY string
     */
    private String date_format(int day, int month, int year){
        return String.format("%02d", day)+"/"+
                String.format("%02d", month+1)+"/"+
                String.format("%02d", (year%100));
    }

    /**
     * Format a time into a HH:MM string.
     * @param hour
     * @param minutes
     * @return a HH:MM string
     */
    private String time_format(int hour, int minutes){
        return String.format("%02d", hour)+":"+String.format("%02d", minutes);
    }
}
