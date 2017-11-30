package ch.epfl.sweng.groupup.activity.event.files;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;


/**
 * Created by Besitzer on 21.11.2017.
 */

public class Aftermovie extends ToolbarActivity {

    private Event event;
    private int eventIndex;
    private ImageSwitcher imageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftermovie);
        super.initializeToolbarActivity();

        Intent intent = getIntent();
        eventIndex = intent.getIntExtra(getString(R.string.event_listing_extraIndex), -1);
        if (eventIndex >-1) {
            event = Account.shared.getEvents().get(eventIndex);
        }

        Log.d("event: ", event.toString());

        initImageSwitcher();
        loadImages();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, FileManager.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(getString(R.string.event_listing_extraIndex), eventIndex);
        startActivity(i);
    }

    private void loadImages() {
        imageSwitcher.postDelayed(new Runnable() {
            int i = 0;
            public void run() {
                imageSwitcher.setImageDrawable(
                        new BitmapDrawable(getResources(), event.getPictures().get(i).asBitmap())
                );
                i++;
                if (event.getPictures().size() < i + 1) i = 0;

                imageSwitcher.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private void initImageSwitcher() {
        imageSwitcher = (ImageSwitcher)findViewById(R.id.imageSwitcher);

        // implement ViewFactory interface
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        });

        Animation in = AnimationUtils.makeInAnimation(this, false);
        Animation out = AnimationUtils.makeOutAnimation(this, false);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
    }

}
