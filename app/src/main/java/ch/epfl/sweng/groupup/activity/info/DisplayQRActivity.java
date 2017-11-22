package ch.epfl.sweng.groupup.activity.info;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import ch.epfl.sweng.groupup.R;


public class DisplayQRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray(UserInformationActivity.EXTRA_MESSAGE);

        assert byteArray != null;
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = findViewById(R.id.qrImageView);

        image.setImageBitmap(bmp);
    }
}
