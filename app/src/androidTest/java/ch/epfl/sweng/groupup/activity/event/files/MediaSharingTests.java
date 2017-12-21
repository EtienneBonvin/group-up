package ch.epfl.sweng.groupup.activity.event.files;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.*;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.lib.CompressedBitmap;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.io.FileNotFoundException;
import org.hamcrest.*;
import org.junit.*;
import org.junit.runner.*;


@RunWith(AndroidJUnit4.class)
public class MediaSharingTests {

    @Rule
    public final ActivityTestRule<EventCreationActivity> mActivityRule =
            new ActivityTestRule<>(EventCreationActivity.class);
    String imageType = "image/jpeg";
    Resources resources = InstrumentationRegistry.getTargetContext()
                                                 .getResources();
    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                             resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
                             resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
                             resources.getResourceEntryName(R.mipmap.ic_launcher));
    String videoType = "video/mp4";
    Uri videoUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                             resources.getResourcePackageName(R.raw.testvideo) + '/' +
                             resources.getResourceTypeName(R.raw.testvideo) + '/' +
                             resources.getResourceEntryName(R.raw.testvideo));


    @Test
    public void addVideo() {
        mockMediaSelection(videoUri, videoType);

        onView(withParent(withId(R.id.image_grid)))
                .check(matches(isDisplayed()));

        onView(withParent(withId(R.id.image_grid)))
                .perform(click());

        onView(withId(R.id.video_container))
                .check(matches(isDisplayed()));

        onView(withId(R.id.video_container))
                .perform(click());

        onView(withParent(withId(R.id.image_grid)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.tap_view_details))
                .perform(click());

        onView(withId(R.id.remove_event_button)).perform(click());
    }


    /*
    private void mockWrongSelection(Uri imageUri) {
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                Activity.RESULT_CANCELED, resultData);

        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK));
        Intents.init();
        intending(expectedIntent).respondWith(result);

        onView(withId(R.id.add_files)).perform(click());
        intended(expectedIntent);
        Intents.release();
    }
*/
    private void mockMediaSelection(Uri imageUri, String type) {
        Intent resultData = new Intent();
        resultData.setDataAndType(imageUri, type);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, resultData);

        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK));

        Intents.init();
        intending(expectedIntent).respondWith(result);

        onView(withId(R.id.add_files)).perform(click());
        intended(expectedIntent);
        Intents.release();
    }


    @Test
    public void addingPictureWithoutExceptionAndDisplayFullScreen() {
        mockMediaSelection(imageUri, imageType);

        onView(withParent(withId(R.id.image_grid)))
                .check(matches(isDisplayed()));

        onView(withParent(withId(R.id.image_grid)))
                .perform(click());

        onView(withId(R.id.show_image))
                .check(matches(isDisplayed()));

        onView(withId(R.id.show_image))
                .perform(click());

        onView(withParent(withId(R.id.image_grid)))
                .check(matches(isDisplayed()));

        mockMediaSelection(imageUri, imageType);
    }


    @After
    public void clearDatabase() {
        Account.shared.clear();
    }


    /**
     * Theses test are for the method not test in CompressedBitmap class by the other test
     */
    @Test
    public void compressBitmapWithArray() {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(mActivityRule.getActivity()
                                                             .getContentResolver()
                                                             .openInputStream(imageUri));
        } catch (FileNotFoundException e) {
        }
        CompressedBitmap compressedBitmap = new CompressedBitmap(bitmap);
        assertEquals(compressedBitmap, new CompressedBitmap(compressedBitmap.asByteArray()));
        assertFalse(compressedBitmap.equals(bitmap));
        assertFalse(compressedBitmap.equals(null));
        assertTrue(compressedBitmap.equals(compressedBitmap));
    }


/*   @Test
    public void fileNotFoundToastOnWrongURI() {

        mockMediaSelection(Uri.parse("scrogneugneu"), imageType);

        onView(withText(R.string.file_management_toast_error_file_uri))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                        .getWindow()
                        .getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void fileNotFoundToastOnNullURI() {

        mockMediaSelection(null, null);

        onView(withText(R.string.file_management_toast_error_file_uri))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                        .getWindow()
                        .getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void fileNotAddedOnBadResult() {
        mockWrongSelection(imageUri);

        onView(withParent(withId(R.id.image_grid)))
                .check(doesNotExist());
    }*/


    @Before
    public void goToFileManagement() {
        Database.setUp();
        Account.shared.clear();
        Database.setUpEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do nothing
            }


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Do nothing
            }
        });
        createEvent();
        onView(withParent(withId(R.id.linear_layout_event_list))).perform(click());
        onView(withId(R.id.tap_view_media))
                .perform(click());
    }


    private void createEvent() {
        final String EVENT_NAME = "My event";
        onView(withId(R.id.ui_edit_event_name))
                .perform(typeText(EVENT_NAME));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.toolbar_image_right))
                .perform(click());
    }


    @Test
    public void openSlideShowView() {
        mockMediaSelection(imageUri, imageType);

        onView(withParent(withId(R.id.image_grid)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.create_aftermovie)).perform(click());

        onView(withId(R.id.imageSwitcher)).check(matches(isDisplayed()));
    }
}
