package ch.epfl.sweng.groupup.activity.event.creation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.MissingResourceException;

import ch.epfl.sweng.groupup.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MembersAddingActivity extends EventCreationActivity implements ZXingScannerView.ResultHandler{

    private HashMap<View.OnClickListener, View> viewsWithOCL;
    private HashMap<View.OnClickListener, String> uIdsWithOCL;
    private ZXingScannerView mScannerView;

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

        for(String member : builder.getMembers()){
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
        for (String member : builder.getMembers()) {
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
        addNewMember(rawResult.getText());
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
     * Adds a line in the member list on the UI with the user ID address specified by the user
     * @param memberUId the identificator of the member (UUID or email)
     */
    private void addNewMember(String memberUId) {

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
        uIdsWithOCL.put(ocl, memberUId);
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
