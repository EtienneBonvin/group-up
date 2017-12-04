package ch.epfl.sweng.groupup.activity.event.creation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.MissingResourceException;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.object.account.Account;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static ch.epfl.sweng.groupup.lib.AndroidHelper.emailCheck;
import static ch.epfl.sweng.groupup.lib.AndroidHelper.showToast;

public class MembersAddingActivity extends EventCreationActivity implements ZXingScannerView.ResultHandler {

    private transient HashMap<View.OnClickListener, View> viewsWithOCL;
    private transient HashMap<View.OnClickListener, MemberRepresentation> uIdsWithOCL;
    private transient ZXingScannerView mScannerView;

    private EventBuilder builder;

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
    private void initFields(){
        viewsWithOCL = new HashMap<>();
        uIdsWithOCL = new HashMap<>();

        builder = (EventBuilder)getIntent().getSerializableExtra(EventCreationActivity.EXTRA_MESSAGE);
        if(builder == null){
            throw new MissingResourceException(
                    "The builder has not been given in the Extra.",
                    EventBuilder.class.toString(),
                    "Builder");
        }

        for(MemberRepresentation member : builder.getMembers()){
            addNewMember(member);
        }
    }

    /**
     * Initialize the listeners and fields of the layout.
     */
    private void initListeners(){
        findViewById(R.id.image_view_add_member)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText memberEmail = findViewById(R.id.edit_text_add_member);
                        if (!emailCheck(memberEmail.getText().toString())){
                            memberEmail.setError(getString(R.string.members_adding_invalid_email));
                        } else if(memberEmail.getText().toString() == Account.shared.getEmail().getOrElse("Default Email")){
                            memberEmail.setError(getString(R.string.event_cration_error_cant_add_yourself));
                        } else{
                            MemberRepresentation newRep = new MemberRepresentation(memberEmail.getText().toString());
                            addNewMember(newRep);
                            memberEmail.setText("");
                        }
                    }
                });

        findViewById(R.id.buttonScanQR)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QrScanner(v);
                    }
                });

        findViewById(R.id.save_added_members_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        returnToEventCreation();
                    }
                });

        super.initializeToolbarActivity();
    }

    /**
     * Saves the already added members
     */
    private void saveState(){
        builder.setMembersTo(uIdsWithOCL.values());
    }

    /**
     * Restores the already added members
     */
    private void restoreState() {
        for (MemberRepresentation member : builder.getMembers()) {
            addNewMember(member);
        }
    }

    /**
     * Describes the behavior of the app when the QR Scanner get its results.
     * @param rawResult the result of the QR scanner
     */
    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        // Close camera and return to activity after successful scan
        mScannerView.stopCamera();
        setContentView(R.layout.members_adding);
        initListeners();
        restoreState();

        // contains UUID and displayName seperated by ","
        String[] decoded = rawResult.getText().split(",");
        if (decoded.length != 2 || decoded[0].length() == 0){
                throw new IllegalArgumentException("Decoded information not proper.");
        }
        if(decoded[0] == Account.shared.getUUID().getOrElse("Default UUID")){
            showToast(this, getString(R.string.event_cration_error_cant_add_yourself), Toast.LENGTH_SHORT);
        }else {
            MemberRepresentation newRep = new MemberRepresentation(decoded[0], decoded[1]);
            addNewMember(newRep);
        }
    }

    /**
     * Describe the behavior of the app when the back button is pressed while using the QR scanner
     */
    @Override
    public void onBackPressed() {
        if (mScannerView != null) {
            mScannerView.stopCamera();
            setContentView(R.layout.members_adding);
            initListeners();
            restoreState();
        } else {
            returnToEventCreation();
        }
    }

    /**
     * Overrides the behavior of the app when the QR Scanner is called. Current state of layout
     * saved.
     * @param view unused
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
     * Adds a line in the member list on the UI with either email or UUDI specified by the user
     * @param memberInfo the MemberRepresentation of the member that will be added
     */
    private void addNewMember(MemberRepresentation memberInfo) {

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
        textView.setText(memberInfo.toString());
        textView.setTextColor(getResources().getColor(R.color.primaryTextColor));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                0.1f);
        params.setMargins(2, 2, 2, 2);
        ImageView minus = new ImageView(this);
        minus.setImageResource(R.drawable.ic_minus_box);
        minus.setLayoutParams(params);
        minus.setBackgroundColor(getResources().getColor(R.color.transparent));

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
        uIdsWithOCL.put(ocl, memberInfo);
    }

    /**
     * Save the members added in the builder and returns to the event creation activity.
     */
    private void returnToEventCreation(){
        builder.setMembersTo(uIdsWithOCL.values());

        Intent intent = new Intent(this, EventCreationActivity.class);
        intent.putExtra(EventCreationActivity.EXTRA_MESSAGE, builder);
        startActivity(intent);
    }
}
