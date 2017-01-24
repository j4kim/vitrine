package com.vitrine.vitrine;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Activité pour créer une vitrine
 */

public class CreateActivity extends AppCompatActivity {

    private int radius;
    private Button btnCreate;
    private String hexColor;
    private SeekBar sbRadius;
    private TextView tvRadius;
    private static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        /* récupération des composants */

        sbRadius = (SeekBar) findViewById(R.id.sbRadius);
        btnCreate = (Button) findViewById(R.id.btnCreate);

        SeekBar sbR = (SeekBar) findViewById(R.id.sbR);
        SeekBar sbG = (SeekBar) findViewById(R.id.sbG);
        SeekBar sbB = (SeekBar) findViewById(R.id.sbB);

        tvRadius = (TextView) findViewById(R.id.tvRadius);

        /* intitialisation des valeurs */

        updateRadius();
        // random colors
        sbR.setProgress(56+random.nextInt(200));
        sbG.setProgress(56+random.nextInt(200));
        sbB.setProgress(56+random.nextInt(200));
        updateColor();

        /* listeners */

        // envoi de la requête

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String name = ((EditText)findViewById(R.id.etName)).getText().toString();

                    SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                    final User user = new User(settings.getString("userJson", null));

                    // new PostVitrineTask(CreateActivity.this, name, radius, hexColor, user.getName()).execute();

                    RequestQueue queue = Volley.newRequestQueue(CreateActivity.this);
                    String url = getString(R.string.post_vitrine_url);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CreateActivity.this.finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("name",name);
                            params.put("radius", radius + "");
                            params.put("latitude", TabActivity.LAST_KNOWN_LATLNG.latitude + "");
                            params.put("longitude", TabActivity.LAST_KNOWN_LATLNG.longitude + "");
                            params.put("hexColor", hexColor);
                            params.put("user", user.getName());

                            return params;
                        }
                    };

                    queue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // mise à jour de la couleur à chaque changement d'une des 3 seekbar

        SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        sbR.setOnSeekBarChangeListener(seekBarListener);
        sbG.setOnSeekBarChangeListener(seekBarListener);
        sbB.setOnSeekBarChangeListener(seekBarListener);

        // mise à jour du rayon

        sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateRadius();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    /**
     * update the raduis text to the slider value
     */
    private void updateRadius() {
        radius = sbRadius.getProgress()+10;
        tvRadius.setText("radius : "+radius+" m");
    }

    /**
     * Update the color of the button to the slider values
     */
    private void updateColor() {
        int r = ((SeekBar) findViewById(R.id.sbR)).getProgress();
        int g = ((SeekBar) findViewById(R.id.sbG)).getProgress();
        int b = ((SeekBar) findViewById(R.id.sbB)).getProgress();
        hexColor = String.format("#%02x%02x%02x", r, g, b);
        btnCreate.setBackgroundColor(Color.parseColor(hexColor));
    }
}
