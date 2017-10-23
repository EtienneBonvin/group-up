package ch.epfl.sweng.groupup.activity.home.inactive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventListing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.login.LoginActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;

import static ch.epfl.sweng.groupup.lib.Login.CONNECTED;
import static ch.epfl.sweng.groupup.lib.Login.FIREBASE_AUTH;
import static ch.epfl.sweng.groupup.lib.Login.googleApiClient;
import static ch.epfl.sweng.groupup.lib.Login.setUpApiClient;
import static ch.epfl.sweng.groupup.object.account.Account.shared;

/**
 * For now this activity will just display information about the logged in user and gives the
 * user a way to sign out.
 */

public class EventListActivity extends ToolbarActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    // Fields to represent the different objects on the GUI of the activity.
    private TextView displayNameTextView;
    private TextView familyNameTextView;
    private TextView givenNameTextView;
    private TextView emailTextView;
    public final static int QRcodeWidth = 500 ;
    ImageView imageView;
    Bitmap bitmap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        super.initializeToolbar();

        initializeFields();
        updateUI(CONNECTED);
        setUpApiClient(
                getString(R.string.web_client_id), /* web_client_id  */
                this, /* context  */
                this, /* fragment activity  */
                this /* on connection failed listener  */
        );

        findViewById(R.id.button_sign_out)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signOut();
                    }
                });

        findViewById(R.id.buttonDisplayQR)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displayQR(v);
                    }
                });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, EventListingActivity.class);
        startActivity(intent);
    }


    public void displayQR(View view){
        if (!shared.getUUID().isEmpty()){
            String text = shared.getUUID().get();
            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                // pass bitmap to Byte Array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // open QR code in new activity
                switchToDisplayQR(byteArray);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            // TODO: 19.10.2017  after pausing app, email always empty?
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            CharSequence text;
            text = "Unable to generate the QR Code";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    /**
     * Method used to initialize all the fields of the activity.
     */

    private void switchToDisplayQR(byte[] byteArray) {
        Intent intent = new Intent(this, DisplayQRActivity.class);
        intent.putExtra("picture", byteArray);
        startActivity(intent);
    }

    private void initializeFields() {
        displayNameTextView = (TextView) findViewById(R.id.text_view_first_name_text);
        familyNameTextView = (TextView) findViewById(R.id.text_view_last_name_text);
        givenNameTextView = (TextView) findViewById(R.id.text_view_given_name_text);
        emailTextView = (TextView) findViewById(R.id.text_view_email_text);
    }

    /**
     * Updates the UI according to the connected/disconnected state of the user.
     *
     * @param connected -  if the user is connected or not
     */
    private void updateUI(boolean connected) {
        if (connected) {
            displayNameTextView.setText(shared.getDisplayName()
                                                .getOrElse(getString(R.string.text_view_display_name_text)));
            familyNameTextView.setText(shared.getFamilyName()
                                               .getOrElse(getString(R.string.text_view_family_name_text)));
            givenNameTextView.setText(shared.getGivenName()
                                              .getOrElse(getString(R.string.text_view_given_name_text)));
            emailTextView.setText(shared.getEmail()
                                          .getOrElse(getString(R.string.text_view_email_text)));
        } else {
            displayNameTextView.setText(R.string.text_view_display_name_text);
            familyNameTextView.setText(R.string.text_view_family_name_text);
            givenNameTextView.setText(R.string.text_view_given_name_text);
            emailTextView.setText(R.string.text_view_email_text);
        }
    }

    /**
     * Starts the sign out process for the connected user.
     */
    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(getResultCallback(this /* package context  */));
    }

    /**
     * Returns the callback that gets called after the sign out process of the standard Google
     * login. It then proceeds with the Firebase sign out. And updates the UI accordingly.
     *
     * @param PACKAGE_CONTEXT -  the context of the package
     * @return ResultCallback<Status> -  the result callback for the standard Google sign out
     */
    private ResultCallback<Status> getResultCallback(final Context PACKAGE_CONTEXT) {
        return new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                FIREBASE_AUTH.signOut();
                shared.clear();
                updateUI(!CONNECTED);

                Intent intent = new Intent(PACKAGE_CONTEXT, LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    /*
    UNUSED
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // If this gets called, then we have a serious problem.
    }
}
