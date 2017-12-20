package ch.epfl.sweng.groupup.lib.pickers;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import org.joda.time.LocalDateTime;


/**
 * DecoratedTimePicker class.
 * Decorates the time pickers, associating them directly to an activity and a button on which they
 * will be triggered.
 */
public class DecoratedTimePicker implements TimePickerDialog.OnTimeSetListener {

    private LocalDateTime setTime;
    private TimePickerDialog timePickerDialog;
    private final Button trigger;
    private final DecoratedPickersVisitor visitor;


    /**
     * Create a DecoratedTimePicker associated to an activity and a trigger with a given initial time.
     *
     * @param activity the activity to be linked to.
     * @param trigger  the button to be triggered on.
     * @param time     the initial time of the dialog.
     */
    public DecoratedTimePicker(EventCreationActivity activity, Button trigger, LocalDateTime time) {
        this.trigger = trigger;
        this.visitor = new DecoratedPickersVisitor();
        this.setTime = time;
        accept(visitor);
        trigger.setText(visitor.getFormatted());

        timePickerDialog = new TimePickerDialog(
                activity,
                R.style.Picker,
                DecoratedTimePicker.this,
                time.getHourOfDay(),
                time.getMinuteOfHour(),
                true);
        timePickerDialog.getWindow()
                        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
    }


    /**
     * Accept a DecoratedPickersDialog that will format the time to display on the trigger.
     *
     * @param visitor the visitor to accept.
     */
    public void accept(DecoratedPickersVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * Getter for the actual registered time.
     *
     * @return the actual registered time.
     */
    public LocalDateTime getTime() {
        return setTime;
    }


    /**
     * On time set change the registered time and display the new time on the trigger.
     *
     * @param view      unused.
     * @param hourOfDay the hours of the day to set.
     * @param minute    the minutes of the hour to set.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setTime = setTime
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(minute);

        accept(visitor);
        trigger.setText(visitor.getFormatted());
    }
}
