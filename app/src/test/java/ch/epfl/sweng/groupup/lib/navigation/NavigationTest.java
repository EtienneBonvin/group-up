package ch.epfl.sweng.groupup.lib.navigation;


import android.location.Location;

import org.junit.Test;

import java.io.IOException;

public class NavigationTest {
    private static class MyLocation extends Location {

        private final double latitude;
        private final double longitude;

        public MyLocation(String provider) {
            super(provider);
            latitude = 0.0;
            longitude = 0.0;
        }

        public MyLocation(double latitude, double longitude){
            super("");
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude(){
            return latitude;
        }

        public double getLongitude(){
            return longitude;
        }
    }

    @Test
    public void test(){
        NavigationModelInterface model = new GoogleMapsNavigationModel();
        try {
            String provider = null;
            Location origin = new MyLocation(46.5197, 6.6323);
            Location destination = new MyLocation(47.3769, 8.5417);
            System.out.println(model.findRoute(origin, destination));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
