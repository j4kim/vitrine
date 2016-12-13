package com.vitrine.vitrine;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vitrine);

        final Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText)findViewById(R.id.etName)).getText().toString();
                int radius = ((SeekBar)findViewById(R.id.sbRaius)).getProgress();
                int r = ((SeekBar)findViewById(R.id.sbR)).getProgress();
                int g = ((SeekBar)findViewById(R.id.sbG)).getProgress();
                int b = ((SeekBar)findViewById(R.id.sbB)).getProgress();
                String hexColor = String.format("#%02x%02x%02x", r, g, b);
                btnCreate.setBackgroundColor(Color.parseColor(hexColor));
                Toast.makeText(CreateActivity.this, hexColor, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
