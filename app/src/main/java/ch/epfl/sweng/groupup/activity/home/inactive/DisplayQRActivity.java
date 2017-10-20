package ch.epfl.sweng.groupup.activity.home.inactive;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import ch.epfl.sweng.groupup.R;


public class DisplayQRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.qrImageView);

        image.setImageBitmap(bmp);
    }

    @Override
    public void onBackPressed() {
        switchToQRMenu();
    }

    private void switchToQRMenu(){
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
    }
}