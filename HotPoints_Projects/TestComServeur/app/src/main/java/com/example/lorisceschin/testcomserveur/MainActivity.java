package com.example.lorisceschin.testcomserveur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        try {
                            tryConnexion("http://10.0.2.2:8080/edsa-vitrines_test/");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
    protected void tryConnexion(String urlToGet) throws IOException {
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

        Log.i("RESPONSE", total.toString());

    }
}
