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

import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;


/**
 * Created by Besitzer on 21.11.2017.
 */

public class SlideshowActivity extends ToolbarActivity {

    private Event event;
    private int eventIndex;
    private ImageSwitcher imageSwitcher;
    private List<CompressedBitmap> loadedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftermovie);

        Intent intent = getIntent();
        eventIndex = intent.getIntExtra(getString(R.string.event_listing_extraIndex), -1);
        if (eventIndex >-1) {
            event = Account.shared.getEvents().get(eventIndex);
        }

        initImageSwitcher();
        while (true) {
            System.out.println("Loading");
            if (event.isAllRecovered()){
                System.out.println("DONE");
                loadedImages = event.getPictures();
                break;
            }
        }
        System.out.println("LOADED: "+loadedImages.toString());
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
                        new BitmapDrawable(getResources(), loadedImages.get(i).asBitmap())
                );
                i++;
                if (loadedImages.size() < i + 1) i = 0;

                imageSwitcher.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private void initImageSwitcher() {
        System.out.println("Initiating switcher");
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
