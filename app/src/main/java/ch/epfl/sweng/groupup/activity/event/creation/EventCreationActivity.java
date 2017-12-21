package ch.epfl.sweng.groupup.activity.event.creation;

import static ch.epfl.sweng.groupup.lib.AndroidHelper.emailCheck;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.AndroidHelper;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.lib.email.GMailService;
import ch.epfl.sweng.groupup.lib.pickers.DecoratedDateTimePicker;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import org.joda.time.LocalDateTime;


/**
 * EventCreationActivity class
 * Offers the possibility to the user to create a new event.
 * Is linked to the layout event_creation.xml
 */
public class EventCreationActivity extends ToolbarActivity implements Serializable {

    /**
     * Represents a user during the event creation process
     */
    protected class MemberRepresentation implements Serializable {

        private String UUID;
        private String displayName;
        private String email;


        protected MemberRepresentation(String UUID, String displayName) {
            this.UUID = UUID;
            this.displayName = displayName;
            this.email = "";
        }


        MemberRepresentation(String email) {
            this.email = email;
            this.displayName = "";
            this.UUID = "";
        }


        /**
         * If member constructed with email, return the member email
         * If member constructed with UUID and display name, return display name
         *
         * @return email or display name of member
         */
        @Override
        public String toString() {
            return email.length() == 0 ? displayName : email;
        }


        /**
         * Create Member out of the MemberRepresentation
         *
         * @return member with name, UUID and email of MemberRepresentation
         */
        protected Member toMember() {
            Member emptyMember = new Member(Optional.<String>empty(), Optional.<String>empty(),
                                            Optional.<String>empty(), Optional.<String>empty(),
                                            Optional.<String>empty(), Optional.<Location>empty());

            return email.length() == 0 ? emptyMember.withDisplayName(displayName)
                                                    .withUUID(UUID) : emptyMember.withEmail(email);
        }


        /**
         * Getter for the member display name.
         *
         * @return the member display name.
         */
        private String getDisplayName() {
            return displayName;
        }


        /**
         * Getter for the member email address.
         *
         * @return the member email address.
         */
        private String getEmail() {
            return email;
        }


        /**
         * Getter for the member UUID.
         *
         * @return the member UUDI.
         */
        private String getUUID() {
            return UUID;
        }
    }


    /**
     * Event builder. Follows the Design Pattern of a builder.
     */
    protected class EventBuilder implements Serializable {

        private String description = "";
        private LocalDateTime endDate = LocalDateTime.now()
                                                     .plusMinutes(6)
                                                     .
                                                             withMillisOfSecond(0)
                                                     .
                                                             withSecondOfMinute(0);
        private String eventName = "";
        private HashSet<MemberRepresentation> members = new HashSet<>();
        private LocalDateTime startDate = LocalDateTime.now()
                                                       .plusMinutes(5)
                                                       .
                                                               withMillisOfSecond(0)
                                                       .
                                                               withSecondOfMinute(0);


        private EventBuilder() {}


        /**
         * Returns a List of all the members added until now to the event.
         *
         * @return a List containing all the event members.
         */
        protected List<MemberRepresentation> getMembers() {
            List<MemberRepresentation> membersList = new ArrayList<>();
            membersList.addAll(members);
            return membersList;
        }


        /**
         * Set the list of the members according to the set of String given as argument.
         * The Strings can be either the UUID either the email address of the member.
         *
         * @param members the set of the representative strings of the members
         */
        @SuppressWarnings("WeakerAccess")
        protected void setMembersTo(Collection<MemberRepresentation> members) {
            cleanMembers();
            for (MemberRepresentation s : members) {
                addMember(s);
            }
        }


        /**
         * Add a new member to the event.
         * If the member already belongs to the event he won't be added a second time.
         * The member can be given under the form of its UID or its email address.
         *
         * @param newMember the new member to add.
         */
        private void addMember(MemberRepresentation newMember) {
            members.add(newMember);
        }


        /**
         * Remove all the members added until now to the event.
         */
        private void cleanMembers() {
            members = new HashSet<>();
        }


        /**
         * Builds an event containing all the properties set until now.
         * Note : the user who is creating the event is automatically added to the list of the members.
         *
         * @return an event containing all the properties set until now.
         */
        private Event build(Context context) {

            GMailService gms = new GMailService(context);

            MemberRepresentation newMember = new MemberRepresentation(
                    Account.shared.getUUID()
                                  .getOrElse("Default UUID"),
                    Account.shared.getDisplayName()
                                  .getOrElse("Default Name"));
            members.add(newMember);
            List<Member> finalMembers = new ArrayList<>();
            Member emptyMember = new Member(Optional.<String>empty(), Optional.<String>empty(),
                                            Optional.<String>empty(), Optional.<String>empty(),
                                            Optional.<String>empty(), Optional.<Location>empty());

            int nb_unknown = 0;

            List<String> mailsToSend = new ArrayList<>();

            for (MemberRepresentation s : members) {
                if (emailCheck(s.toString()) && s.getEmail()
                                                 .length() != 0) {
                    finalMembers.add(emptyMember.
                                                        withUUID(Member.UNKNOWN_USER_ + (++nb_unknown))
                                                .
                                                        withEmail(s.toString()));
                    mailsToSend.add(s.getEmail());
                } else {
                    finalMembers.add(emptyMember.
                                                        withUUID(s.getUUID()));
                }
            }

            gms.sendInvitationEmail(mailsToSend);

            return new Event(eventName, startDate, endDate, description, finalMembers, false);
        }


        /**
         * Getter for the description of the event.
         *
         * @return the description of the event.
         */
        private String getDescription() {
            return description;
        }


        /**
         * Setter for the description of the event.
         *
         * @param description the description of the event.
         */
        private void setDescription(String description) {
            this.description = description;
        }


        /**
         * Getter for the end time of the event under the form of a LocaleDateTime.
         * Both the end date and the end time are in it.
         *
         * @return a LocaleDateTime for the end date and time.
         */
        private LocalDateTime getEndDate() {
            return new LocalDateTime(endDate);
        }


        /**
         * Getter for the name of the event.
         *
         * @return the name of the event.
         */
        private String getEventName() {
            return eventName;
        }


        /**
         * Setter for the name of the event.
         *
         * @param eventName the name of the event.
         */
        private void setEventName(String eventName) {
            this.eventName = eventName;
        }


        /**
         * Getter for the start time of the event under the form of a LocaleDateTime.
         * Both the start date and the start time are in it.
         *
         * @return a LocaleDateTime for the start date and time.
         */
        private LocalDateTime getStartDate() {
            return new LocalDateTime(startDate);
        }


        /**
         * Setter for the end date of the event.
         *
         * @param dateTime the new end date time to set.
         */
        private void setEndDateTime(LocalDateTime dateTime) {
            endDate = endDate.withYear(dateTime.getYear())
                             .
                                     withMonthOfYear(dateTime.getMonthOfYear())
                             .
                                     withDayOfMonth(dateTime.getDayOfMonth())
                             .
                                     withHourOfDay(dateTime.getHourOfDay())
                             .
                                     withMinuteOfHour(dateTime.getMinuteOfHour());
        }


        /**
         * Setter for the start date of the event.
         *
         * @param dateTime the new start date time to set.
         */
        private void setStartDateTime(LocalDateTime dateTime) {
            startDate = startDate.withYear(dateTime.getYear())
                                 .
                                         withMonthOfYear(dateTime.getMonthOfYear())
                                 .
                                         withDayOfMonth(dateTime.getDayOfMonth())
                                 .
                                         withHourOfDay(dateTime.getHourOfDay())
                                 .
                                         withMinuteOfHour(dateTime.getMinuteOfHour());
        }
    }


    public static final int INPUT_MAX_LENGTH = 30;
    public static final String EXTRA_MESSAGE = "Builder";
    private EventBuilder builder;
    private transient DecoratedDateTimePicker startDateTime, endDateTime;


    /**
     * Initialization of all the variables of the class and of the OnClickListeners
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);
        initFields();
        initListeners();
    }


    /**
     * Initializes all fields that will be used in the UI to default values.
     */
    private void initFields() {

        LocalDateTime regStart;
        LocalDateTime regEnd;

        try {
            builder = (EventBuilder) getIntent().getSerializableExtra(EXTRA_MESSAGE);
            regStart = builder.getStartDate();
            regEnd = builder.getEndDate();
        } catch (Exception e) {
            builder = new EventBuilder();
            regStart = LocalDateTime.now()
                                    .plusMinutes(5);
            regEnd = LocalDateTime.now()
                                  .plusMinutes(6);
        }

        if (builder == null) {
            builder = new EventBuilder();
            regStart = LocalDateTime.now()
                                    .plusMinutes(5);
            regEnd = LocalDateTime.now()
                                  .plusMinutes(6);
        }

        startDateTime = new DecoratedDateTimePicker(this,
                                                    (Button) findViewById(R.id.button_start_date),
                                                    (Button) findViewById(R.id.button_start_time),
                                                    regStart);

        endDateTime = new DecoratedDateTimePicker(this,
                                                  (Button) findViewById(R.id.button_end_date),
                                                  (Button) findViewById(R.id.button_end_time),
                                                  regEnd);

        ((TextView) findViewById(R.id.number_of_members)).
                                                                 setText(String.format(Locale.getDefault(),
                                                                                       "%s %d",
                                                                                       getString(
                                                                                               R.string.event_creation_tv_number_of_members),
                                                                                       builder.getMembers()
                                                                                              .size()));

        ((EditText) findViewById(R.id.ui_edit_event_name)).
                                                                  setText(builder.getEventName());

        ((EditText) findViewById(R.id.edit_text_description)).
                                                                     setText(builder.getDescription());
    }


    /**
     * Initialize the OnClickListeners of the layout.
     */
    private void initListeners() {

        findViewById(R.id.toolbar_image_right).
                                                      setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              saveEvent();
                                                          }
                                                      });

        findViewById(R.id.button_add_members).
                                                     setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             builder.setEventName(
                                                                     ((EditText) findViewById(R.id.ui_edit_event_name))
                                                                             .getText()
                                                                             .toString());
                                                             builder.setDescription(
                                                                     ((EditText) findViewById(
                                                                             R.id.edit_text_description))
                                                                             .getText()
                                                                             .toString());
                                                             builder.setStartDateTime(startDateTime.getDateTime());
                                                             builder.setEndDateTime(endDateTime.getDateTime());
                                                             Intent intent = new Intent(getApplicationContext(),
                                                                                        MembersAddingActivity.class);
                                                             intent.putExtra(EXTRA_MESSAGE, builder);
                                                             startActivity(intent);
                                                         }
                                                     });
    }


    /**
     * Method called when the 'Save event' button is clicked.
     * It registers the informations entered by the user, verify them and bring him to the
     * group list Activity.
     */
    private void saveEvent() {

        EditText eventName = findViewById(R.id.ui_edit_event_name);
        if (eventName.getText()
                     .toString()
                     .length() == 0) {
            eventName.setError(getString(R.string.event_creation_toast_non_empty_event_name));
            return;
        } else if (eventName.getText()
                            .toString()
                            .length() > INPUT_MAX_LENGTH) {
            eventName.setError(getString(R.string.event_creation_toast_event_name_too_long));
            return;
        }
        eventName.setError(null);

        builder.setStartDateTime(startDateTime.getDateTime());
        builder.setEndDateTime(endDateTime.getDateTime());

        if (builder.getStartDate()
                   .isBefore(LocalDateTime.now())) {
            AndroidHelper.showToast(getApplicationContext(),
                                    getString(R.string.event_creation_toast_event_start_before_now),
                                    Toast.LENGTH_SHORT);
            return;
        }

        if (builder.getEndDate()
                   .isBefore(builder.getStartDate())) {
            AndroidHelper.showToast(getApplicationContext(),
                                    getString(R.string.event_creation_toast_event_end_before_begin),
                                    Toast.LENGTH_SHORT);
            return;
        }

        if (builder.getStartDate()
                   .isEqual(builder.getEndDate())) {
            AndroidHelper.showToast(getApplicationContext(),
                                    getString(R.string.event_creation_toast_event_last_1_minute),
                                    Toast.LENGTH_SHORT);
            return;
        }

        builder.setEventName(((EditText) findViewById(R.id.ui_edit_event_name)).getText()
                                                                               .toString());

        builder.setDescription(((EditText) findViewById(R.id.edit_text_description)).getText()
                                                                                    .toString());

        Account.shared.addOrUpdateEvent(builder.build(getApplicationContext()));
        Database.update();

        Intent intent = new Intent(this, EventListingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * Initializes the behaviour of the toolbar for this specific activity
     */
    @Override
    public void initializeToolbar() {
        TextView title = findViewById(R.id.toolbar_title);
        ImageView rightImage = findViewById(R.id.toolbar_image_right);

        title.setText(R.string.toolbar_title_create_event);

        // User icon
        rightImage.setImageResource(R.drawable.ic_check);

        // Home button
        findViewById(R.id.toolbar_image_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(EventListingActivity.class);
            }
        });
    }
}
