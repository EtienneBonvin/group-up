package ch.epfl.sweng.groupup.settings;

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
        assert(true);
    }

}
