package ch.epfl.sweng.groupup.activity.event.File;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.res.ResourcesCompat;


import static android.support.test.espresso.Espresso.getIdlingResources;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.activity.event.files.FileManagementActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;

@RunWith(AndroidJUnit4.class)
public class MediaSharingTests {

    private final String EVENT_NAME = "My event";

    @Rule
    public final ActivityTestRule<EventCreationActivity> mActivityRule =
            new ActivityTestRule<>(EventCreationActivity.class);

    @Before
    public void goToFileManagement(){
        Database.setUpDatabase();
        createEvent();
        onView(withParent(withId(R.id.linear_layout_event_list)))
                .perform(longClick());
        onView(withId(R.id.upload_file))
                .perform(click());
    }

    @After
    public void clearDatabase(){
        Account.shared.clear();
    }

    @Test
    public void addingPictureWithoutExceptionAndDisplayFullScreen(){

        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceEntryName(R.mipmap.ic_launcher));

        Intent resultData = new Intent();
        resultData.setData(imageUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, resultData);

        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK),
                hasData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        Intents.init();
        intending(expectedIntent).respondWith(result);

        onView(withId(R.id.add_files))
                .perform(click());
        intended(expectedIntent);
        Intents.release();

        onView(withParent(withId(R.id.image_grid)))
                .perform(click());
        onView(withId(R.id.image_to_display))
                .perform(click());

    }

    public void createEvent(){
        onView(withId(R.id.ui_edit_event_name))
                .perform(typeText(EVENT_NAME));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button))
                .perform(click());
    }
}
