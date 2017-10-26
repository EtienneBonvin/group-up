package ch.epfl.sweng.groupup.activity.home.inactive;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.eventListing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.login.LoginActivity;
import ch.epfl.sweng.groupup.activity.settings.Settings;
import ch.epfl.sweng.groupup.lib.Helper;
import ch.epfl.sweng.groupup.lib.login.FirebaseAuthentication;
import ch.epfl.sweng.groupup.lib.login.GoogleAuthenticationService;
import ch.epfl.sweng.groupup.lib.login.GoogleAuthenticationService.Status;
import ch.epfl.sweng.groupup.lib.login.LoginActivityInterface;

import static ch.epfl.sweng.groupup.object.account.Account.shared;

/**
 * For now this activity will just display information about the logged in user and gives the
 * user a way to sign out.
 */

public class EventListActivity extends LoginActivityInterface {

    // Fields to represent the different objects on the GUI of the activity.
    private TextView displayNameTextView;
    private TextView familyNameTextView;
    private TextView givenNameTextView;
    private TextView emailTextView;

    private GoogleAuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        authService = new FirebaseAuthentication(getString(R.string.web_client_id),
                                                 this,
                                                 this,
                                                 this);

        initializeFields();
        updateUI(Status.CONNECTED);

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

        findViewById(R.id.icon_access_group_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent
                                intent =
                                new Intent(getApplicationContext(), EventListingActivity.class);
                        startActivity(intent);
                    }
                });

        findViewById(R.id.icon_access_settings)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Settings.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EventListingActivity.class);
        startActivity(intent);
    }


    public void displayQR(View view) {
        if (!shared.getUUID().isEmpty()) {
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
            Helper.showToast(getApplicationContext(),
                             getString(R.string.toast_unable_to_generate_qr),
                             Toast.LENGTH_SHORT);
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
        displayNameTextView = (TextView) findViewById(R.id.text_view_display_name_text);
        familyNameTextView = (TextView) findViewById(R.id.text_view_family_name_text);
        givenNameTextView = (TextView) findViewById(R.id.text_view_given_name_text);
        emailTextView = (TextView) findViewById(R.id.text_view_email_text);
    }

    /**
     * Updates the UI according to the connected/disconnected state of the user.
     *
     * @param connected -  if the user is connected or not
     */
    private void updateUI(GoogleAuthenticationService.Status connected) {
        if (connected == Status.CONNECTED) {
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
        authService.signOut();
    }


    @Override
    public void onFail() {
        Helper.showToast(getApplicationContext(),
                         getString(R.string.toast_unable_to_sign_out),
                         Toast.LENGTH_SHORT);
    }

    @Override
    public void onSuccess() {
        updateUI(Status.DISCONNECTED);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
