package com.vitrine.vitrine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

public class VitrineInfoActivity extends AppCompatActivity {

    private Vitrine mVitrine;
    private Button btn_unsubscribe;
    private Button btn_subscribe;
    private Button btn_show;
    private ProgressDialog dialog;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine_info);

        Intent i = getIntent();
        mVitrine = i.getParcelableExtra("vitrine");
        User user = i.getParcelableExtra("user");

        btn_show = (Button) findViewById(R.id.btn_show);
        btn_subscribe = (Button) findViewById(R.id.btn_subscribe);
        btn_unsubscribe = (Button) findViewById(R.id.btn_unsubscribe);

        setTitle(mVitrine.getName());

        queue = Volley.newRequestQueue(this);

        final String params = String.format(
                "?token=%s&vitrine_id=%s",
                user.getToken(),
                mVitrine.getId());
        String url = getString(R.string.is_subscribed) + params;

        StringRequest stringRequest = new StringRequest(
            Request.Method.GET,
            url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("0")){
                        btn_subscribe.setVisibility(View.VISIBLE);
                    }else if (response.equals("1")){
                        btn_unsubscribe.setVisibility(View.VISIBLE);
                    }
                    dialog.dismiss();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(VitrineInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );

        queue.add(stringRequest);
        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitrineInfoActivity.this, VitrineActivity.class);
                intent.putExtra("vitrine", mVitrine);
                startActivity(intent);
            }
        });

        btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.subscribe) + params;
                execRequest(url);
            }
        });

        btn_unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.unsubscribe) + params;
                execRequest(url);
            }
        });

    }

    private void execRequest(String url) {
        StringRequest req = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(VitrineInfoActivity.this, response, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VitrineInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(req);
        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
    }
}
