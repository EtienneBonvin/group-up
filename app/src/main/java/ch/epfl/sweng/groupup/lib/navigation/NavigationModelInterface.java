package ch.epfl.sweng.groupup.lib.navigation;

import android.location.Location;

import java.io.IOException;

public interface NavigationModelInterface {
    String findRoute(Location origin, Location destination) throws IOException;
}
