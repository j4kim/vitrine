package com.vitrine.vitrine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VitrineInfoActivity extends AppCompatActivity {

    private Vitrine mVitrine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine_info);

        mVitrine = getIntent().getParcelableExtra("vitrine");

        Button btn_show = (Button) findViewById(R.id.btn_show);
        Button btn_subscribe = (Button) findViewById(R.id.btn_subscribe);

        setTitle(mVitrine.getName());

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitrineInfoActivity.this, VitrineActivity.class);
                intent.putExtra("vitrine", mVitrine);
                startActivity(intent);
            }
        });
    }
}
