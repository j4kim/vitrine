package com.vitrine.vitrine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by loris.ceschin on 20/11/2016.
 */

public class NetworkTools {
    public static String connect(String urlToGet) throws IOException {
        URL url = new URL(urlToGet);
        HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
        mUrlConnection.setDoInput(true);

        InputStream is = new BufferedInputStream(mUrlConnection.getInputStream());
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }

        return total.toString();

    }

    public static String postVitrine(String name, int radius, String hexColor, String userName, Context context) throws IOException {

        URL url = new URL(context.getResources().getString(R.string.post_vitrine_url));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            double lat = TabActivity.LAST_KNOWN_LATLNG.latitude;
            double lon = TabActivity.LAST_KNOWN_LATLNG.longitude;

            // merci à http://stackoverflow.com/questions/9767952/how-to-add-parameters-to-httpurlconnection-using-post
            // Attention : doc de Uri.Builder : "Helper class for building or manipulating URI references. Not safe for concurrent use."
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("name", name)
                    .appendQueryParameter("radius", radius+"")
                    .appendQueryParameter("latitude", lat+"")
                    .appendQueryParameter("longitude", lon+"")
                    .appendQueryParameter("hexColor", hexColor)
                    .appendQueryParameter("user", userName);
            String query = builder.build().getEncodedQuery();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            out.close();




            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            in.close();

            return total.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
