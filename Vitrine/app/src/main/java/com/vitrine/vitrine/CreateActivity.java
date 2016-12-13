package com.vitrine.vitrine;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class CreateActivity extends AppCompatActivity {

    private int r;
    private int g;
    private int b;
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
                String name = ((EditText)findViewById(R.id.etName)).getText().toString();
                String result = NetworkTools.postVitrine(name, radius, hexColor);
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

    private void updateRadius() {
        radius = sbRadius.getProgress()+10;
        tvRadius.setText("radius : "+radius+" m");
    }

    private void updateColor() {
        r = ((SeekBar)findViewById(R.id.sbR)).getProgress();
        g = ((SeekBar)findViewById(R.id.sbG)).getProgress();
        b = ((SeekBar)findViewById(R.id.sbB)).getProgress();
        hexColor = String.format("#%02x%02x%02x", r, g, b);
        btnCreate.setBackgroundColor(Color.parseColor(hexColor));
    }
}
