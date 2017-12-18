package ch.epfl.sweng.groupup.activity.info;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.login.LoginActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.AndroidHelper;
import ch.epfl.sweng.groupup.lib.login.FirebaseAuthentication;
import ch.epfl.sweng.groupup.lib.login.GoogleAuthenticationService;
import ch.epfl.sweng.groupup.lib.login.GoogleAuthenticationService.Status;
import ch.epfl.sweng.groupup.lib.login.LoginActivityInterface;
import ch.epfl.sweng.groupup.lib.login.MockAuth;

import static ch.epfl.sweng.groupup.object.account.Account.shared;

/**
 * For now this activity will just display information about the logged in user and gives the
 * user a way to sign out.
 */

public class UserInformationActivity extends ToolbarActivity implements LoginActivityInterface {


    // Fields to represent the different objects on the GUI of the activity.
    private TextView displayNameTextView;
    private TextView familyNameTextView;
    private TextView givenNameTextView;
    private TextView emailTextView;

    private GoogleAuthenticationService authService;

    public static final String EXTRA_MESSAGE = "picture";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            authService = new FirebaseAuthentication(
                    getString(R.string.web_client_id),
                    this,
                    this,
                    this);
        } else {
            authService = new MockAuth(this, true, true);
        }

        initializeFields();
        updateUI(Status.CONNECTED);
        displayQR();

        findViewById(R.id.button_sign_out)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        authService.signOut();
                    }
                });

    }

    @Override
    public void initializeToolbar(){
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(R.string.toolbar_title_user_profile);

        // home button
        findViewById(R.id.toolbar_image_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(EventListingActivity.class);
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), EventListingActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void displayQR() {
        if (!shared.getUUID().isEmpty()) {
            String text = shared.getUUID().get() + ","+ shared.getDisplayName().getOrElse("Unknown User");
            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix
                        bitMatrix =
                        writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap
                        bitmap =
                        Bitmap.createBitmap(width,
                                            height,
                                            Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bitmap.setPixel(x,
                                     y,
                                     bitMatrix.get(x, y) ?
                                     Color.BLACK :
                                     ContextCompat.getColor(this,R.color.background));
                    }
                }

                // display QR code
                ImageView image = findViewById(R.id.qrImageView);
                image.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            AndroidHelper.showToast(getApplicationContext(),
                                    getString(R.string.toast_unable_to_generate_qr),
                                    Toast.LENGTH_SHORT);
        }
    }

    /**
     * Method used to initialize all the fields of the activity.
     */

    private void initializeFields() {
        displayNameTextView = findViewById(R.id.text_view_display_name_text);
        familyNameTextView = findViewById(R.id.text_view_family_name_text);
        givenNameTextView = findViewById(R.id.text_view_given_name_text);
        emailTextView = findViewById(R.id.text_view_email_text);
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

    @Override
    public void onFail() {
        AndroidHelper.showToast(getApplicationContext(),
                                getString(R.string.toast_unable_to_sign_out),
                                Toast.LENGTH_SHORT);
    }

    @Override
    public void onSuccess() {
        updateUI(Status.DISCONNECTED);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public Activity getActivity() {
        return this;
    }
}
