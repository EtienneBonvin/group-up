package ch.epfl.sweng.groupup.lib.login;

import static ch.epfl.sweng.groupup.object.account.Account.shared;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public final class FirebaseAuthentication
    implements GoogleAuthenticationService, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE = 666;
    private final LoginActivityInterface activity;
    private final Context activityContext;
    private final FragmentActivity fragmentActivity;
    private final GoogleApiClient googleApiClient;
    private final GoogleSignInOptions googleSignInOptions;
    private final String webClientID;


    public FirebaseAuthentication(String webClientID, Context activityContext, LoginActivityInterface activity,
                                  FragmentActivity fragmentActivity) {
        this.webClientID = webClientID;
        this.activityContext = activityContext;
        this.activity = activity;
        this.fragmentActivity = fragmentActivity;
        this.googleSignInOptions = getSignInOptions();
        this.googleApiClient = getApiClient();
    }


    private GoogleSignInOptions getSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(webClientID)
                                                                                   .requestId()
                                                                                   .requestEmail()
                                                                                   .requestProfile()
                                                                                   .build();
    }


    private GoogleApiClient getApiClient() {
        return new GoogleApiClient.Builder(activityContext).enableAutoManage(fragmentActivity, this)
                                                           .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                                                           .build();
    }


    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(googleSignInResult);
        }
    }


    private void handleSignInResult(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult.isSuccess()) {
            firebaseAuthWithGoogle(googleSignInResult.getSignInAccount());
        } else {
            this.activity.onFail();
        }
    }


    private void firebaseAuthWithGoogle(final GoogleSignInAccount googleCurrentUser) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleCurrentUser.getIdToken(), null);
        FirebaseAuth.getInstance()
                    .signInWithCredential(credential)
                    .addOnCompleteListener(activity.getActivity(), getOnCompleteListener(googleCurrentUser));
    }


    private OnCompleteListener<AuthResult> getOnCompleteListener(final GoogleSignInAccount googleCurrentUser) {
        return new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser firebaseCurrentUser = FirebaseAuth.getInstance()
                                                               .getCurrentUser();
                if (task.isSuccessful() && firebaseCurrentUser != null) {

                    Account.shared.withEmail(googleCurrentUser.getEmail())
                                  .withDisplayName(firebaseCurrentUser.getDisplayName())
                                  .withFamilyName(googleCurrentUser.getFamilyName())
                                  .withGivenName(googleCurrentUser.getGivenName())
                                  .withUUID(firebaseCurrentUser.getUid());
                    //.withPoneNumber(firebaseCurrentUser.getPhoneNumber();

                    Database.update();
                    Database.setUpEventListener(null);
                    activity.onSuccess();
                } else {
                    activity.onFail();
                }
            }
        };
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    @Override
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, REQUEST_CODE);
    }


    @Override
    public void signOut() {
        if (FirebaseAuth.getInstance()
                        .getCurrentUser() != null) {
            Auth.GoogleSignInApi.signOut(googleApiClient)
                                .setResultCallback(getResultCallback());
            activity.onSuccess();
        } else {
            activity.onFail();
        }
    }


    private ResultCallback<com.google.android.gms.common.api.Status> getResultCallback() {
        return new ResultCallback<com.google.android.gms.common.api.Status>() {
            @Override
            public void onResult(@NonNull com.google.android.gms.common.api.Status status) {
                FirebaseAuth.getInstance()
                            .signOut();
                shared.clear();
            }
        };
    }
}
