package com.vitrine.vitrine;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

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
import java.net.MalformedURLException;
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

    public static String postVitrine(String name, int radius, String hexColor) throws IOException {

        URL url = new URL("http://j4kim.nexgate.ch/vitrine/postVitrine.php");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            // TODO : récupérer la position actuelle et le pseudo de l'utilisateur (dans les préférences)
            // TODO : côté serveur, abonner l'utilisateur à sa nouvelle vitrine

            // merci à http://stackoverflow.com/questions/9767952/how-to-add-parameters-to-httpurlconnection-using-post
            // Attention : doc de Uri.Builder : "Helper class for building or manipulating URI references. Not safe for concurrent use."
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("name", name)
                    .appendQueryParameter("radius", radius+"")
                    .appendQueryParameter("latitude", 46.997637+"")
                    .appendQueryParameter("longitude", 6.938717+"")
                    .appendQueryParameter("hexColor", hexColor)
                    .appendQueryParameter("user", "loris");
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
