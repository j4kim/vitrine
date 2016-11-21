package com.vitrine.vitrine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
}
