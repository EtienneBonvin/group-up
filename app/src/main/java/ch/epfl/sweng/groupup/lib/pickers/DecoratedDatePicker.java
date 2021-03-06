package ch.epfl.sweng.groupup.lib.pickers;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import org.joda.time.LocalDateTime;


/**
 * DecoratedDatePicker class.
 * Decorates the date pickers, associating them directly to an activity and a button on which they
 * will be triggered.
 */
public class DecoratedDatePicker implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog datePickerDialog;
    private LocalDateTime setDate;
    private final Button trigger;
    private final DecoratedPickersVisitor visitor;


    /**
     * Create a DecoratedDatePicker with the activity to be linked to, the button to be triggered on
     * and the initial date.
     *
     * @param activity the activity where to display the dialog.
     * @param trigger  the button to be triggered on.
     * @param date     the initial date of the picker.
     */
    public DecoratedDatePicker(EventCreationActivity activity, Button trigger, LocalDateTime date) {
        this.trigger = trigger;
        setDate = date;
        visitor = new DecoratedPickersVisitor();
        accept(visitor);
        trigger.setText(visitor.getFormatted());

        datePickerDialog = new DatePickerDialog(activity,
                                                R.style.Picker,
                                                DecoratedDatePicker.this,
                                                date.getYear(),
                                                date.getMonthOfYear() - 1,
                                                date.getDayOfMonth());
        datePickerDialog.getWindow()
                        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }


    /**
     * Accept a DecoratedPickersVisitor that will generate the string to display on the trigger.
     *
     * @param visitor the visitor to accept.
     */
    public void accept(DecoratedPickersVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * Getter for the actual stored date.
     *
     * @return the actual stored date.
     */
    public LocalDateTime getDate() {
        return setDate;
    }


    /**
     * On date set, we change the actual stored date and update the text on the trigger.
     *
     * @param view       unused.
     * @param year       the year set.
     * @param month      the month of the year set.
     * @param dayOfMonth the day of the month set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        setDate = setDate.withYear(year)
                         .withMonthOfYear(month + 1)
                         .withDayOfMonth(dayOfMonth);

        accept(visitor);
        trigger.setText(visitor.getFormatted());
    }
}
