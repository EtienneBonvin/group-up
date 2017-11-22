package ch.epfl.sweng.groupup.activity.map;

import android.support.test.espresso.contrib.BuildConfig;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class MapActivityTest {

    @Rule
    public ActivityTestRule<MapActivity> mActivityRule = new ActivityTestRule<>(
            MapActivity.class);

    @Test
    public void launchedWithoutErrors(){
        if (BuildConfig.DEBUG){
            throw new AssertionError();
        }
    }

}
