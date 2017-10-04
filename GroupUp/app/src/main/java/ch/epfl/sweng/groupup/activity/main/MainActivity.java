package ch.epfl.sweng.groupup.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.login.LogInActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchToLoginContentView(); // Make it depend on states "LOGGED_IN" and "LOGGED_OUT".
    }

    private void switchToLoginContentView() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
