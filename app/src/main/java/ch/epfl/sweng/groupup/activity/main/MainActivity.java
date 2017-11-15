package ch.epfl.sweng.groupup.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.login.LoginActivity;
import ch.epfl.sweng.groupup.lib.database.Database;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database.setUpDatabase();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                switchToLoginContentView();
            }
        }, 2000); // 3000ms delay
    }

    private void switchToLoginContentView() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
