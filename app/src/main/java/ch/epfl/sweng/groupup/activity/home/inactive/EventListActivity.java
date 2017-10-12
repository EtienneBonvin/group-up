package ch.epfl.sweng.groupup.activity.home.inactive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.login.LoginActivity;
import ch.epfl.sweng.groupup.object.account.Account;

import static ch.epfl.sweng.groupup.activity.login.Login.CONNECTED;
import static ch.epfl.sweng.groupup.activity.login.Login.FIREBASE_AUTH;
import static ch.epfl.sweng.groupup.activity.login.Login.googleApiClient;
import static ch.epfl.sweng.groupup.activity.login.Login.setUpApiClient;

/**
 * For now this activity will just display information about the logged in user and gives the
 * user a way to sign out.
 */

public class EventListActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    // Fields to represent the different objects on the GUI of the activity.
    private TextView displayNameTextView;
    private TextView familyNameTextView;
    private TextView givenNameTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        initializeFields();
        updateUI(CONNECTED);
        setUpApiClient(
                getString(R.string.web_client_id), /* web_client_id  */
                this, /* context  */
                this, /* fragment activity  */
                this /* on connection failed listener  */
        );
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.button_sign_out) {
            signOut();
        }
    }

    /**
     * Method used to initialize all the fields of the activity.
     */
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
            displayNameTextView.setText(Account.shared.getDisplayName()
                                                .getOrElse(getString(R.string.text_view_display_name_text)));
            familyNameTextView.setText(Account.shared.getFamilyName()
                                               .getOrElse(getString(R.string.text_view_family_name_text)));
            givenNameTextView.setText(Account.shared.getGivenName()
                                              .getOrElse(getString(R.string.text_view_given_name_text)));
            emailTextView.setText(Account.shared.getEmail()
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
                Account.shared.clear();
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
