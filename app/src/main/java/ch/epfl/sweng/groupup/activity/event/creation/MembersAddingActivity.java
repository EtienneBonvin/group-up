package ch.epfl.sweng.groupup.activity.event.creation;

import static android.Manifest.permission.CAMERA;
import static ch.epfl.sweng.groupup.lib.AndroidHelper.emailCheck;
import static ch.epfl.sweng.groupup.lib.AndroidHelper.showToast;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.lib.AndroidHelper;
import ch.epfl.sweng.groupup.object.account.Account;
import java.util.Collection;
import java.util.HashMap;
import java.util.MissingResourceException;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * An activity dedicated to adding members to an event, during the event creation process
 * Is linked to members_adding.xml
 */
public class MembersAddingActivity extends EventCreationActivity implements ZXingScannerView.ResultHandler {

    private EventBuilder builder;
    private transient MemberLabelFactory factory;
    private transient ZXingScannerView mScannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members_adding);
        initFields();
        initListeners();
    }


    /**
     * Initialize the fields of the activity.
     */
    private void initFields() {

        builder = (EventBuilder) getIntent().getSerializableExtra(EventCreationActivity.EXTRA_MESSAGE);
        if (builder == null) {
            throw new MissingResourceException("The builder has not been given in the Extra.",
                                               EventBuilder.class.toString(),
                                               "Builder");
        }

        factory = new MemberLabelFactory(this);

        for (MemberRepresentation member : builder.getMembers()) {
            factory.addNewMember(member);
        }
    }


    /**
     * Initialize the listeners and fields of the layout.
     */
    private void initListeners() {
        findViewById(R.id.image_view_add_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText memberEmail = findViewById(R.id.edit_text_add_member);
                if (!emailCheck(memberEmail.getText()
                                           .toString())) {
                    memberEmail.setError(getString(R.string.members_adding_error_toast_invalid_email));
                } else if (memberEmail.getText()
                                      .toString()
                                      .equals(Account.shared.getEmail()
                                                            .getOrElse("Default Email"))) {
                    memberEmail.setError(getString(R.string.event_creation_error_cant_add_yourself));
                } else {
                    MemberRepresentation newRep = new MemberRepresentation(memberEmail.getText()
                                                                                      .toString());
                    factory.addNewMember(newRep);
                    memberEmail.setText("");
                }
            }
        });

        findViewById(R.id.buttonScanQR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QrScanner(v);
            }
        });

        findViewById(R.id.toolbar_image_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToEventCreation();
            }
        });
    }


    /**
     * Overrides the behavior of the app when the QR Scanner is called. Current state of layout
     * saved.
     *
     * @param view unused
     */
    public void QrScanner(View view) {
        if (checkPermission()) {
            mScannerView = new ZXingScannerView(this);
            saveState();
            setContentView(mScannerView);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        } else {
            AndroidHelper.showToast(this, "Please grant access to the camera", Toast.LENGTH_SHORT);
        }
    }


    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA)
                == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Saves the already added members
     */
    private void saveState() {
        builder.setMembersTo(factory.getMembers());
    }


    /**
     * Save the members added in the builder and returns to the event creation activity.
     */
    private void returnToEventCreation() {
        builder.setMembersTo(factory.getMembers());

        Intent intent = new Intent(this, EventCreationActivity.class);
        intent.putExtra(EventCreationActivity.EXTRA_MESSAGE, builder);
        startActivity(intent);
    }


    /**
     * Describes the behavior of the app when the QR Scanner get its results.
     *
     * @param rawResult the result of the QR scanner
     */
    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        // Close camera and return to activity after successful scan
        mScannerView.stopCamera();
        setContentView(R.layout.members_adding);
        initListeners();
        restoreState();

        // contains UUID and displayName separated by ","
        String[] decoded = rawResult.getText()
                                    .split(",");
        if (decoded.length != 2 || decoded[0].length() == 0) {
            throw new IllegalArgumentException("Decoded information not proper.");
        }
        if (decoded[0].equals(Account.shared.getUUID()
                                            .getOrElse("Default UUID"))) {
            showToast(this, getString(R.string.event_creation_error_cant_add_yourself), Toast.LENGTH_SHORT);
        } else {
            MemberRepresentation newRep = new MemberRepresentation(decoded[0], decoded[1]);
            factory.addNewMember(newRep);
        }
    }


    /**
     * Restores the already added members
     */
    private void restoreState() {
        for (MemberRepresentation member : builder.getMembers()) {
            factory.addNewMember(member);
        }
    }


    /**
     * Describe the behavior of the app when the back button
     */
    @Override
    public void onBackPressed() {
        if (mScannerView != null) {
            mScannerView.stopCamera();
            mScannerView = null;
            setContentView(R.layout.members_adding);
            initListeners();
            restoreState();
            initializeToolbar();
        } else {
            returnToEventCreation();
        }
    }


    /**
     * Initializes the toolbar for this specific activity
     */
    @Override
    public void initializeToolbar() {
        super.initializeToolbar();
        TextView title = findViewById(R.id.toolbar_title);
        ImageView rightImage = findViewById(R.id.toolbar_image_right);

        title.setText(R.string.toolbar_title_add_members);
        rightImage.setImageResource(R.drawable.ic_check);

        // Home button
        findViewById(R.id.toolbar_image_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(EventListingActivity.class);
            }
        });
    }


    /**
     * MemberLabelFactory class.
     * Variation of the factory design pattern. This class generates the layout labels to display the
     * members and allow to add them to the layout.
     */
    private class MemberLabelFactory {

        private transient MembersAddingActivity activity;
        private transient LinearLayout baseLayout;
        private transient TextView baseTextView;
        private transient ImageView delImage;
        private transient HashMap<View.OnClickListener, MemberRepresentation> uIdsWithOCL;
        private transient HashMap<View.OnClickListener, View> viewsWithOCL;


        /**
         * Create a MemberTagFactory to display member labels on the given MembersAddingActivity.
         *
         * @param activity the activity where to display the members tags.
         */
        private MemberLabelFactory(MembersAddingActivity activity) {
            this.activity = activity;
            viewsWithOCL = new HashMap<>();
            uIdsWithOCL = new HashMap<>();
            generateLayout();
        }


        /**
         * Generate an empty label linked to the activity.
         */
        private void generateLayout() {
            baseLayout = new LinearLayout(activity);
            baseLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                     LinearLayout.LayoutParams.WRAP_CONTENT));

            baseTextView = new TextView(activity);
            baseTextView.setTextSize(20);
            baseTextView.setLayoutParams(new LinearLayout.LayoutParams(0,
                                                                       LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                       0.9f));

            baseTextView.setTextColor(getResources().getColor(R.color.middle_grey));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                                                                             LinearLayout.LayoutParams.MATCH_PARENT,
                                                                             0.1f);
            params.setMargins(2, 2, 2, 2);
            delImage = new ImageView(activity);
            delImage.setImageResource(R.drawable.ic_minus_box);
            delImage.setLayoutParams(params);
            delImage.setBackgroundColor(getResources().getColor(R.color.transparent));

            baseLayout.addView(baseTextView);
            baseLayout.addView(delImage);
        }


        /**
         * Add a new member label to the activity filled with the given member infos.
         *
         * @param memberInfo the MemberRepresentation representing the informations of the member.
         */
        private void addNewMember(MemberRepresentation memberInfo) {

            generateLayout();

            baseTextView.setText(memberInfo.toString());
            ((LinearLayout) findViewById(R.id.members_list)).addView(baseLayout);

            View.OnClickListener ocl = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayout) findViewById(R.id.members_list)).removeView(viewsWithOCL.get(this));
                    uIdsWithOCL.remove(this);
                }
            };

            delImage.setOnClickListener(ocl);
            viewsWithOCL.put(ocl, baseLayout);
            uIdsWithOCL.put(ocl, memberInfo);
        }


        /**
         * Give a collection of the members that have been added so far.
         *
         * @return a collection of MemberRepresentations of the members added so far.
         */
        private Collection<MemberRepresentation> getMembers() {
            return uIdsWithOCL.values();
        }
    }
}
