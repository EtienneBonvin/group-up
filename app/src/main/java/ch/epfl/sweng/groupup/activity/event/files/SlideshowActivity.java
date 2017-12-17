package ch.epfl.sweng.groupup.activity.event.files;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.description.EventDescriptionActivity;
import ch.epfl.sweng.groupup.activity.event.listing.EventListingActivity;
import ch.epfl.sweng.groupup.activity.info.UserInformationActivity;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.CompressedBitmap;
import ch.epfl.sweng.groupup.lib.Watcher;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;


public class SlideshowActivity extends ToolbarActivity implements Watcher{

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
        event.addWatcher(this);
        loadedImages = event.getPictures();

        loadImages();
    }

    @Override
    public void initializeToolbar() {
        TextView title = findViewById(R.id.toolbar_title);
        ImageView rightImage = findViewById(R.id.toolbar_image_right);
        ImageView secondRightImage = findViewById(R.id.toolbar_image_second_from_right);

        rightImage.setImageResource(R.drawable.ic_user);
        findViewById(R.id.toolbar_image_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(UserInformationActivity.class);
            }
        });

        // home button
        findViewById(R.id.toolbar_image_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpListener(EventListingActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, EventDescriptionActivity.class);
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
                if (loadedImages.size()>0) {
                    imageSwitcher.setImageDrawable(
                            new BitmapDrawable(getResources(), loadedImages.get(i).asBitmap())
                    );
                    i++;
                    if (loadedImages.size() < i + 1) i = 0;
                }
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

    @Override
    public void notifyWatcher() {
        loadedImages = event.getPictures();
    }

    /**
     * Override onPause method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    protected void onPause() {
        super.onPause();
        event.removeWatcher(this);
    }

    /**
     * Override onStop method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    public void onStop() {
        super.onStop();
        event.removeWatcher(this);
    }

    /**
     * Override onDestroy method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        event.removeWatcher(this);
    }
}
