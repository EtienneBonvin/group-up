package ch.epfl.sweng.groupup.lib.pickers;

import org.joda.time.LocalDateTime;

import java.util.Locale;

/**
 * Visitor for the DecoratedPickers that will be used to format the displayed texts on the buttons
 * linked to them.
 */
public class DecoratedPickersVisitor {

    private StringBuilder formatted;

    /**
     * Create a DecoratedPickersVisitor with an empty content.
     */
    public DecoratedPickersVisitor(){
        formatted = new StringBuilder();
    }

    /**
     * Visit the DecoratedDatePicker given, generating a String in a date format.
     * @param datePicker the date picker to visit.
     */
    public void visit(DecoratedDatePicker datePicker){
        LocalDateTime time = datePicker.getDate();
        formatted.append(date_format(
                time.getDayOfMonth(),
                time.getMonthOfYear(),
                time.getYear()));
    }

    /**
     * Visit the DecoratedTimePicker given, generating a String in a time format.
     * @param timePicker the time picker to visit.
     */
    public void visit(DecoratedTimePicker timePicker){
        LocalDateTime time = timePicker.getTime();
        formatted.append(time_format(
                time.getHourOfDay(),
                time.getMinuteOfHour()));
    }

    /**
     * Give the formatted String generated during a visit. The visitor then empty its content.s
     * @return a string in the expected format.
     */
    public String getFormatted(){
        String result = formatted.toString();
        formatted = new StringBuilder();
        return result;
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
}
