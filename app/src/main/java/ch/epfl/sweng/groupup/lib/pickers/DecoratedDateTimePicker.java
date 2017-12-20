package ch.epfl.sweng.groupup.lib.pickers;

import android.widget.Button;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import org.joda.time.LocalDateTime;


/**
 * DecoratedDateTimePicker.
 * Combine a DecoratedDatePicker and a DecoratedTimePicker, each linked to a trigger.
 */
public class DecoratedDateTimePicker {

    private final DecoratedDatePicker datePicker;
    private final DecoratedTimePicker timePicker;


    /**
     * Create a DecoratedDateTimePicker linked an activity and to two triggers : one for the time setting and one
     * for the date setting.
     *
     * @param activity    the context in which the dialogs will be shown.
     * @param dateTrigger the button which is the trigger for the date set.
     * @param timeTrigger the button which is the trigger for the time set.
     * @param time        the initial DateTime of the pickers.
     */
    public DecoratedDateTimePicker(EventCreationActivity activity, Button dateTrigger,
                                   Button timeTrigger, LocalDateTime time) {
        datePicker = new DecoratedDatePicker(activity, dateTrigger, time);
        timePicker = new DecoratedTimePicker(activity, timeTrigger, time);
    }


    /**
     * Getter for the full DateTime recovered by the pickers.
     *
     * @return the DateTime set on the pickers.
     */
    public LocalDateTime getDateTime() {
        LocalDateTime date = datePicker.getDate();
        LocalDateTime time = timePicker.getTime();
        return new LocalDateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                                 time.getHourOfDay(), time.getMinuteOfHour());
    }
}
