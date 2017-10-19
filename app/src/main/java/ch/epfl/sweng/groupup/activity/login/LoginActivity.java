package ch.epfl.sweng.groupup.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.home.inactive.EventListActivity;
import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

import static ch.epfl.sweng.groupup.lib.Login.CONNECTING;
import static ch.epfl.sweng.groupup.lib.Login.FIREBASE_AUTH;
import static ch.epfl.sweng.groupup.lib.Login.REQUEST_CODE;
import static ch.epfl.sweng.groupup.lib.Login.firebaseAuthWithGoogle;
import static ch.epfl.sweng.groupup.lib.Login.firebaseCurrentUser;
import static ch.epfl.sweng.groupup.lib.Login.googleApiClient;
import static ch.epfl.sweng.groupup.lib.Login.googleCurrentUser;
import static ch.epfl.sweng.groupup.lib.Login.setUpApiClient;
import static ch.epfl.sweng.groupup.lib.Login.showAlert;

/**
 * Activity to handle the sign up / login process of the user. It either asks the user to sign up /
 * login or logs the user automatically in depending of the last state of the app.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    // Fields to represent the different objects on the GUI of the activity.
    private SignInButton signInButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initializeFields();
        setOnClickListener();
        setUpApiClient(
                getString(R.string.web_client_id), /* web_client_id  */
                this, /* context  */
                this, /* fragment activity  */
                this /* on connection failed listener  */
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FIREBASE_AUTH.getCurrentUser() != null) {
            signIn();
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.sign_in_button_google) {
            signIn();
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
            handleSignInResult(googleSignInResult);
        }
    }

    /**
     * Method to start the whole sign in process.
     */
    private void signIn() {
        toggleLoading(CONNECTING);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE);
    }

    /**
     * Method to handle the sign in result. It checks of the process was successful or not and
     * proceeds relatively to this result.
     *
     * @param googleSignInResult - representing the result of the sign in process
     */
    private void handleSignInResult(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult == null) {
            throw new NullPointerException("googleSignInResult cannot be null");
        }

        if (googleSignInResult.isSuccess()) {
            googleCurrentUser = googleSignInResult.getSignInAccount();

            firebaseAuthWithGoogle(this /* activity  */, getOnCompleteListener());
        } else {
            logInFailed(googleSignInResult.getStatus().getStatusMessage() + " " + googleSignInResult
                    .getStatus().getStatusCode());
        }
    }

    /**
     * Callback to handle what happens once the authentication with Firebase is complete. It may
     * succeed or fail.
     *
     * @return OnCompleteListener<AuthResult> - on complete listener for the Firebase
     * authentication process
     */
    private OnCompleteListener<AuthResult> getOnCompleteListener() {
        return new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                onFirebaseAuthComplete(task.isSuccessful());
            }
        };
    }

    /**
     * This method is called once the authentication with Firebase is complete. It then
     * proceeds depending on the success of the process.
     *
     * @param success - representing the success of the process
     */
    private void onFirebaseAuthComplete(boolean success) {
        if (success) {
            firebaseCurrentUser = FIREBASE_AUTH.getCurrentUser();

            Account.shared
                    .withEmail(googleCurrentUser.getEmail())
                    .withDisplayName(firebaseCurrentUser.getDisplayName())
                    .withFamilyName(googleCurrentUser.getFamilyName())
                    .withGivenName(googleCurrentUser.getGivenName())
                    .withUUID(firebaseCurrentUser.getUid());
            //.withPoneNumber(firebaseCurrentUser.getPhoneNumber());

            // TODO: remove
            List<Member> memberList = new ArrayList<>();
            memberList.add(new Member(Optional.from("84bgEpReO3MRfHSqERAT5BTxKPz1"),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty()));
            memberList.add(new Member(Optional.from("JRslNXOZBVep41gyODvEvJfuzHe2"),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty()));
            memberList.add(new Member(Optional.from("oicEIZxVtEZAI3eORNWPQmNttHC2"),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty()));
            memberList.add(new Member(Optional.from("qp8GAApu1eURGTA0v2pIsCJyoHA3"),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty()));
            memberList.add(new Member(Optional.from("rGXu4ouVByS8GuE3oiq5FVJ6IiT2"),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty()));
            memberList.add(new Member(Optional.from("KCRyXlzqCWgBLFGh5M3PIgNOin22"),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty(),
                                      Optional.<String>empty()));

            Event event = new Event("SAT",
                                    LocalDateTime.now(),
                                    LocalDateTime.now().plusDays(1),
                                    "SAAAAAAAAT",
                                    memberList);
            Account.shared.addOrUpdateEvent(event);
            // TODO: remove

            Database.updateDatabase();
            Database.setUpEventListener();

            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);
        } else {
            logInFailed(getString(R.string.text_firebase_login_failed));
        }
    }

    /**
     * Method that gets called if the log in process failed. It updates the GUI and informs the
     * user accordingly.
     *
     * @param statusMessage - message to display to the user
     */
    private void logInFailed(String statusMessage) {
        if (statusMessage == null || statusMessage.length() == 0) {
            statusMessage = getString(R.string.text_no_status_message);
        }

        showAlert(this /* context  */,
                  getString(R.string.title_connection_failed),
                  statusMessage,
                  getString(R.string.text_button_connection_failed));
        toggleLoading(!CONNECTING);
    }

    /**
     * Method used to initialize all the fields of the activity.
     */
    private void initializeFields() {
        signInButton = (SignInButton) findViewById(R.id.sign_in_button_google);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_sign_in_login);

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
    private void toggleLoading(boolean connecting) {
        if (connecting) {
            signInButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }
}
