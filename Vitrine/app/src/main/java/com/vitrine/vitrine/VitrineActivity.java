package com.vitrine.vitrine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    private RequestQueue queue;

    private int imgIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine);

        mImageView = (ImageView) findViewById(R.id.imageView);

        imgIndex = 0;

        Intent i = getIntent();
        mVitrine = i.getParcelableExtra("vitrine");


        // Load the picture list
        queue = Volley.newRequestQueue(this);
        String url =  getString(R.string.getpictures_url) + "?vitrine_id=" + mVitrine.getId();



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject pictureObject = new JSONObject(response);
                    JSONArray pictureArray = pictureObject.getJSONArray("pictures");
                    for (int j = 0; j < pictureArray.length() - 1; j++) {
                        mVitrine.addPicture(pictureArray.getJSONObject(j).getString("path"));
                    }

                    load_img();
                    dialog.dismiss();

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



        // Click listener to go to next img
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgIndex++;
                load_img();
            }
        });


    }


    private void load_img()
    {

        if(mVitrine.getPictures().size() == 0)
            return;

        String imgUrl = getString(R.string.get_vitrine_image_url) + mVitrine.getPictureAtIndex(imgIndex);

        ImageRequest request = new ImageRequest(imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(request);
    }

}
