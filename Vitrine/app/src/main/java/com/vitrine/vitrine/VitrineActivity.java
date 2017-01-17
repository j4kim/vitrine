package com.vitrine.vitrine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VitrineActivity extends AppCompatActivity {

    private Vitrine mVitrine;
    private ProgressDialog dialog;
    private final Context vitrineContext = this;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine);

        mImageView = (ImageView) findViewById(R.id.imageView);

        Intent i = getIntent();
        mVitrine = i.getParcelableExtra("vitrine");


        // Load the picture list
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =  getString(R.string.getpictures_url) + "?vitrine_id=" + mVitrine.getId();



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();

                    JSONObject pictureObject = new JSONObject(response);
                    JSONArray pictureArray = pictureObject.getJSONArray("pictures");
                    for (int j = 0; j < pictureArray.length() - 1; j++) {
                        mVitrine.addPicture(pictureArray.getJSONObject(j).getString("path"));
                    }

                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(vitrineContext, "Connection failed", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        String imgUrl = "http://j4kim.nexgate.ch/vitrine/uploads/12/JPEG_20170117_100229_.jpg";

        ImageRequest request = new ImageRequest(imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                       // mImageView.setImageResource(R.drawable.image_load_error);
                    }
                });

        queue.add(request);
    }




}
