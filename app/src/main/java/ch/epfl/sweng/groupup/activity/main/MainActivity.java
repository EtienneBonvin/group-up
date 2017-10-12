package ch.epfl.sweng.groupup.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                switchToLoginContentView(); // Make it depend on states "LOGGED_IN" and "LOGGED_OUT".
            }
        }, 3000); // 3000ms delay
    }

    private void switchToLoginContentView() {
        Intent intent = new Intent(this, LoginActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
    }
}
