package ch.epfl.sweng.groupup.lib;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Final and uninstantiable class to help the process of logging in. It contains all the
 * different objects needed with some other helpful fields.
 */

public final class Login {

    /**
     * Useful constants to represent the connected/connecting states and to have a request code
     * that gets returned once the sign in intent activates its callback.
     */
    public static final boolean CONNECTED = true;
    public static final boolean CONNECTING = true;
    public static final int REQUEST_CODE = 666;

    // Object that contains the sign in options we what from the user.
    private static GoogleSignInOptions googleSignInOptions;

    // Objects for the standard Google sign in.
    public static GoogleApiClient googleApiClient;
    public static GoogleSignInAccount googleCurrentUser;

    // Objects for the Firebase sign in.
    public static final FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();
    public static FirebaseUser firebaseCurrentUser;

    private Login() {
        // Not instantiable.
    }

    /**
     * Returns a GoogleSignInOptions object containing all the wanted options from the user.
     *
     * @param webClientID - web client id of our Google app
     * @return GoogleSignInOptions - object containing the wanted options of the user
     */
    private static GoogleSignInOptions getSignInOptions(String webClientID) {
        return new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientID)
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();
    }

    /**
     * Returns a GoogleApiClient relative the the given parameters.
     *
     * @param context                    - current context of the activity
     * @param fragmentActivity           - current activity fragment
     * @param onConnectionFailedListener - on connection failed listener of the activity
     * @return GoogleApiClient - Google API client relative the the parameters
     */
    private static GoogleApiClient getApiClient(Context context,
                                                FragmentActivity fragmentActivity,
                                                GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        return new GoogleApiClient.Builder(context)
                .enableAutoManage(fragmentActivity, onConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    /**
     * Used to set up the the Google sign in option and get the api client relative to the given
     * parameters.
     *
     * @param webClientID                - web client id of our Google app
     * @param context                    - current context of the activity
     * @param fragmentActivity           - current activity fragment
     * @param onConnectionFailedListener - on connection failed listener of the activity
     */
    public static void setUpApiClient(String webClientID,
                                      Context context,
                                      FragmentActivity fragmentActivity,
                                      GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        googleSignInOptions = getSignInOptions(webClientID);
        googleApiClient = getApiClient(context, fragmentActivity, onConnectionFailedListener);
    }

    /**
     * Starts the Google authentication with Firebase.
     *
     * @param activity           - current activity
     * @param onCompleteListener - on complete listener that handles the result
     */
    public static void firebaseAuthWithGoogle(Activity activity,
                                              OnCompleteListener<AuthResult> onCompleteListener) {
        AuthCredential credential = GoogleAuthProvider
                .getCredential(googleCurrentUser.getIdToken(), null);
        FIREBASE_AUTH.signInWithCredential(credential)
                .addOnCompleteListener(activity, onCompleteListener);
    }
}
