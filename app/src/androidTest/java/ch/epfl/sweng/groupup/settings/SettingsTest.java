package ch.epfl.sweng.groupup.settings;

import android.support.test.espresso.contrib.BuildConfig;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.groupup.activity.settings.Settings;

public class SettingsTest {

    @Rule
    public ActivityTestRule<Settings> mActivityRule = new ActivityTestRule<>(
            Settings.class);

    @Test
    public void launchedWithoutErrors(){
        if (BuildConfig.DEBUG){
            throw new AssertionError();
        }
    }

}
