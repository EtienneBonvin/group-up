package ch.epfl.sweng.groupup.lib.navigation;


import org.junit.Test;

import java.io.IOException;

public class NavigationTest {
    @Test
    public void test(){
        NavigationModelInterface model = new GoogleMapsNavigationModel();
        try {
            System.out.println(model.findRoute(46.5197, 6.6323, 47.3769, 8.5417));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
