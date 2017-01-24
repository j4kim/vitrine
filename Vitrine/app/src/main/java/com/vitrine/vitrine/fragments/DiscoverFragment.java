package com.vitrine.vitrine.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.vitrine.vitrine.R;
import com.vitrine.vitrine.TabActivity;
import com.vitrine.vitrine.Vitrine;
import com.vitrine.vitrine.VitrineActivity;
import com.vitrine.vitrine.VitrineInfoActivity;
import com.vitrine.vitrine.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Fragment avec une carte pour découvrir les vitrines
 */

public class DiscoverFragment extends Fragment implements OnMapReadyCallback {

    ArrayList<Vitrine> mListVitrine;
    private GoogleMap mMap;
    private HashMap<Circle, Vitrine> circlesVitrines;
    private Circle lastClickedCircle=null;
    private ProgressDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListVitrine = new ArrayList<>();

        retrieveVitrines();

        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    private void retrieveVitrines(){

        final TabActivity activity = (TabActivity)getActivity();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = getString(R.string.all_vitrines_url);

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray responses = jsonObject.getJSONArray("vitrines");
                            for(int i = 0; i < responses.length(); i++)
                            {
                                String str = responses.getJSONObject(i).toString();
                                final Vitrine v = new Vitrine(str);
                                // Add picture path to vitrines
                                RequestQueue queue1 = Volley.newRequestQueue(activity);
                                String url = getString(R.string.getpictures_url)+"?vitrine_id="+v.getId();

                                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>(){
                                            @Override
                                            public void onResponse(String response){
                                                try {
                                                    JSONObject pictureObject = new JSONObject(response);
                                                    JSONArray pictureArray = pictureObject.getJSONArray("pictures");

                                                    for(int i = 0; i < pictureArray.length() - 1; i ++){
                                                        v.addPicture(pictureArray.getJSONObject(i).getString("path"));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Errors while retrieving picture
                                    }
                                });
                                queue1.add(stringRequest1);

                                mListVitrine.add(v);
                            }

                            loadMap();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(activity, "Connection failed", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        dialog = ProgressDialog.show(activity, "",
                "Loading. Please wait...", true);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(((TabActivity)getActivity()).getLocation(),15));


        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Log.i("PERMISSION", "permission accordée");
        } else {
            // Show rationale and request permission.
            Log.i("PERMISSION", "permission refusée");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        }


        circlesVitrines = new HashMap<>();

        for(Vitrine v : mListVitrine)
        {
            addVitrineCircle(v);
        }

        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                final Vitrine vitrine = circlesVitrines.get(circle);

                if(circle.equals(lastClickedCircle)){
                    circle.setZIndex(circle.getZIndex()-1);

                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), vitrine.getName()+" mise à l'arrière plan", Toast.LENGTH_SHORT);
                    toast2.show();

                    lastClickedCircle=null;
                }else{

                    double x = vitrine.getRadius();
                    // fonction trouvée par LoggerPro
                    float y = (float) (25.77*Math.pow(x,-0.09));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(),y));

                    Snackbar snackbar = Snackbar
                            .make(getView(), vitrine.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Open", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(), VitrineInfoActivity.class);
                                    intent.putExtra("vitrine", vitrine);
                                    User user = ((TabActivity) getActivity()).getUser();
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                }
                            });
                    snackbar.show();

                    lastClickedCircle=circle;
                }
            }
        });


    }

    private void addVitrineCircle(Vitrine vitrine){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(vitrine.getLatLng());
        circleOptions.radius(vitrine.getRadius());

        int color = vitrine.getColor();
        circleOptions.fillColor(Color.argb(90,Color.red(color),Color.green(color),Color.blue(color)));
        circleOptions.strokeColor(Color.argb(180,Color.red(color),Color.green(color), Color.blue(color)));
        circleOptions.clickable(true);

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);
        circlesVitrines.put(circle,vitrine);
    }

    private void loadMap(){
        SupportMapFragment mapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        mapFragment.getMapAsync(this);
    }
}
