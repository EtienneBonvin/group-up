package ch.epfl.sweng.groupup.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.lib.login.FirebaseAuthentication;
import ch.epfl.sweng.groupup.lib.login.GoogleAuthenticationService;
import ch.epfl.sweng.groupup.lib.login.GoogleAuthenticationService.Status;
import ch.epfl.sweng.groupup.lib.login.LoginActivityInterface;
import ch.epfl.sweng.groupup.lib.login.MockAuth;

import static ch.epfl.sweng.groupup.lib.AndroidHelper.showAlert;


/**
 * Activity to handle the sign up / login process of the user. It either asks the user to sign up /
 * login or logs the user automatically in depending of the last state of the app.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginActivityInterface {

    private SignInButton signInButton;
    private ProgressBar progressBar;
    private GoogleAuthenticationService authService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        this.authService = new FirebaseAuthentication(getString(R.string.web_client_id), this, this, this);

        initializeFields();
        setOnClickListener();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            toggleLoading(Status.CONNECTING);
            authService.signIn();
        }
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.sign_in_button_google) {
            toggleLoading(Status.CONNECTING);
            authService.signIn();
        }
    }


    @Override
    public void onFail() {
        showAlert(this /* context  */,
                  getString(R.string.title_connection_failed),
                  getString(R.string.text_firebase_login_failed),
                  getString(R.string.text_button_connection_failed));
        toggleLoading(Status.DISCONNECTED);
    }


    @Override
    public void onSuccess() {
        Intent intent = new Intent(this, EventListingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public Activity getActivity() {
        return this;
    }


    /**
     * Method used to initialize all the fields of the activity.
     */
    private void initializeFields() {
        signInButton = findViewById(R.id.sign_in_button_google);
        progressBar = findViewById(R.id.progress_bar_sign_in_login);

        progressBar.setVisibility(View.GONE);
    }


    /**
     * Method used to set up all on click listener for this activity.
     */
    private void setOnClickListener() {
        signInButton.setOnClickListener(this /* on click listener  */);
    }


    /**
     * This method toggle the GUI parts of the activity depending on the state of the connection.
     *
     * @param connecting - represents if the user is connecting
     */
    private void toggleLoading(Status connecting) {
        if (connecting == Status.CONNECTING) {
            signInButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authService.onActivityResult(requestCode, data);
    }


    public void mock(boolean loginStatus, boolean logoutStatus) {
        this.authService = new MockAuth(this, loginStatus, logoutStatus);
    }
}
