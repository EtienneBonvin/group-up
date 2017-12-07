package ch.epfl.sweng.groupup.activity.event.file;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.creation.EventCreationActivity;
import ch.epfl.sweng.groupup.lib.database.Database;
import ch.epfl.sweng.groupup.object.account.Account;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MediaSharingTests {

    @Rule
    public final ActivityTestRule<EventCreationActivity> mActivityRule =
            new ActivityTestRule<>(EventCreationActivity.class);

    @Before
    public void goToFileManagement(){
        Database.setUp();
        Account.shared.clear();
        Database.setUpEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Do nothing
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do nothing
            }
        });
        createEvent();
        onView(withParent(withId(R.id.linear_layout_event_list))).perform(click());
        onView(withId(R.id.swipe_bar))
                .perform(swipeLeft());
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

        mockMediaSelection(imageUri);

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

        mockMediaSelection(imageUri);

    }

    @Test
    public void fileNotFoundToastOnWrongURI(){

        mockMediaSelection(Uri.parse("scrogneugneu"));

        onView(withText(R.string.file_management_toast_error_file_uri))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                        .getWindow()
                        .getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void fileNotFoundToastOnNullURI(){

        mockMediaSelection(null);

        onView(withText(R.string.file_management_toast_error_file_uri))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                        .getWindow()
                        .getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void fileNotAddedOnBadResult(){

        Resources resources = InstrumentationRegistry.getTargetContext().getResources();

        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceEntryName(R.mipmap.ic_launcher));

        mockWrongSelection(imageUri);

        onView(withParent(withId(R.id.image_grid)))
                .check(doesNotExist());
    }

    @Test
    public void openSlideShowView(){
        Resources resources = InstrumentationRegistry.getTargetContext().getResources();

        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceEntryName(R.mipmap.ic_launcher));

        mockMediaSelection(imageUri);

        onView(withParent(withId(R.id.image_grid)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.create_aftermovie)).perform(click());

        onView(withId(R.id.imageSwitcher)).check(matches(isDisplayed()));
    }

    private void mockWrongSelection(Uri imageUri){
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                Activity.RESULT_CANCELED, resultData);

        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK),
                hasData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        Intents.init();
        intending(expectedIntent).respondWith(result);

        onView(withId(R.id.add_files)).perform(click());
        intended(expectedIntent);
        Intents.release();
    }

    private void mockMediaSelection(Uri imageUri){

        Intent resultData = new Intent();
        resultData.setData(imageUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, resultData);

        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK),
                hasData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        Intents.init();
        intending(expectedIntent).respondWith(result);

        onView(withId(R.id.add_files)).perform(click());
        intended(expectedIntent);
        Intents.release();
    }

    private void createEvent(){
        final String EVENT_NAME = "My event";
        onView(withId(R.id.ui_edit_event_name))
                .perform(typeText(EVENT_NAME));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_new_event_button))
                .perform(click());
    }
}
