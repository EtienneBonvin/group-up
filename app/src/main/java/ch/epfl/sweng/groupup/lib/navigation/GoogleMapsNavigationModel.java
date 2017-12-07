package ch.epfl.sweng.groupup.lib.navigation;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class GoogleMapsNavigationModel extends AsyncTask<URL, Void, String> implements NavigationModelInterface {
    private final static String baseURL = "https://maps.googleapis.com/maps/api/directions/";
    private final static String format = "json?";
    private final static String key = "AIzaSyDtv0o9SNKJWLWt51YyYhZK0nxsR5FWMdY";

    @Override
    protected String doInBackground(URL... urls) {
        if(urls.length == 1){
            HttpURLConnection urlConnection = null;
            StringBuilder response = new StringBuilder();
            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                int c;
                while((c = in.read()) != -1){
                    response.append((char) c);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
                return response.toString();
            }
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String findRoute(double originX, double originY, double destinationX, double destinationY) throws IOException {
        URL url = new URL(baseURL + format + "origin=" + originX + "," + originY + "&destination=" + destinationX + "," + destinationY + "&key=" + key);
        return doInBackground(url);
    }
}
