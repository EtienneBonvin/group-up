package ch.epfl.sweng.groupup.lib.navigation;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;

public interface NavigationModelInterface {
    String findRoute(double originX, double originY, double destinationX, double destinationY) throws IOException;
}
