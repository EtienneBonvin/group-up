package ch.epfl.sweng.groupup.activity.event.files;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.description.EventDescriptionActivity;

/**
 * FullScreenFile class.
 * Put the image given in extra in full screen, a simple tap on screen makes the user go back
 * to file management.
 */
public class FullScreenFile extends AppCompatActivity {

    //TODO a little zoom-in / zoom-out method could be nice.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_file);

        Intent intent = getIntent();
        CompressedBitmap fileToShow =
                new CompressedBitmap(intent.getByteArrayExtra(FileManager.FILE_EXTRA_NAME));

        final int eventIndex = intent.getIntExtra(getString(R.string.event_listing_extraIndex), -1);

        findViewById(R.id.container)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent back = new Intent(getApplicationContext(), EventDescriptionActivity.class);
                        back.putExtra(getString(R.string.event_listing_extraIndex), eventIndex);
                        startActivity(back);
                    }
                });

        ((ImageView)findViewById(R.id.image_to_display))
                .setImageBitmap(fileToShow.asBitmap());
    }
}
