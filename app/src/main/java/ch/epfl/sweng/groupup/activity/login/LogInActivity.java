package ch.epfl.sweng.groupup.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.object.account.Account;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE = 666;
    private static final boolean CONNECTED = true;

    private SignInButton signInButton;
    private Button signOutButton;

    private LinearLayout userStatsLinearLayout;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initializeFields();
        setOnClickListener();
        setUpClient();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.sign_in_button_google) {
            signIn();
        } else if (id == R.id.button_sign_out) {
            signOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi
                    .getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(!CONNECTED);
                    }
                });
    }

    public void handleResult(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            assert googleSignInAccount != null;
            Account.shared.withFirstName(googleSignInAccount.getGivenName()).withLastName
                    (googleSignInAccount.getFamilyName()).withEmail(googleSignInAccount.getEmail());

            updateUI(CONNECTED);
        } else {
            updateUI(!CONNECTED);
        }
    }

    public void updateUI(boolean connected) {
        if (connected) {
            signInButton.setVisibility(View.GONE);
            updateFields(CONNECTED);
            userStatsLinearLayout.setVisibility(View.VISIBLE);
        } else {
            userStatsLinearLayout.setVisibility(View.GONE);
            updateFields(!CONNECTED);
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    private void initializeFields() {
        signInButton = (SignInButton) findViewById(R.id.sign_in_button_google);

        signOutButton = (Button) findViewById(R.id.button_sign_out);

        userStatsLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_log_in_user_stats);
        firstNameTextView = (TextView) findViewById(R.id.text_view_first_name_text);
        lastNameTextView = (TextView) findViewById(R.id.text_view_last_name_text);
        emailTextView = (TextView) findViewById(R.id.text_view_email_text);

        userStatsLinearLayout.setVisibility(View.GONE);
    }

    private void updateFields(boolean connected) {
        if (connected) {
            firstNameTextView.setText(Account.shared.getFirstName());
            lastNameTextView.setText(Account.shared.getLastName());
            emailTextView.setText(Account.shared.getEmail());
        } else {
            firstNameTextView.setText(R.string.text_view_first_name_text);
            lastNameTextView.setText(R.string.text_view_last_name_text);
            emailTextView.setText(R.string.text_view_email_text);

            Account.shared.clear();
        }
    }

    private void setOnClickListener() {
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
    }

    private void setUpClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi
                (Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
    }
}