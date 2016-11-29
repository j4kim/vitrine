package com.vitrine.vitrine.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.vitrine.vitrine.NetworkTools;
import com.vitrine.vitrine.R;
import com.vitrine.vitrine.TabActivity;
import com.vitrine.vitrine.Vitrine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscoverFragment extends Fragment implements OnMapReadyCallback {

    ArrayList<Vitrine> mListVitrine;
    private GoogleMap mMap;
    private HashMap<Circle, Vitrine> circlesVitrines;
    private Circle lastClickedCircle=null;
    private RetrieveCloseTask mRetrieveCloseTask;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mListVitrine = new ArrayList<>();

        retrieveVitrines();

        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    private void retrieveVitrines(){
        if (mRetrieveCloseTask != null) {
            return;
        }

        mRetrieveCloseTask = new RetrieveCloseTask(((TabActivity)getActivity()).getUser().getToken());
        mRetrieveCloseTask.execute((Void) null);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.996914,6.93576),15));


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
                Vitrine vitrine = circlesVitrines.get(circle);

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
                            .setAction("Voir", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

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

    /**
     * Represents an asynchronous retrieving task for vitrines
     */
    public class RetrieveCloseTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserToken;

        RetrieveCloseTask(String userToken) {
            mUserToken = userToken;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //attempt retrieving a list of vitrines.
            boolean success = false;
            try {
                String response = NetworkTools.connect(getString(R.string.subscription_url) + "?token=" + mUserToken);

                JSONObject jsonObject = new JSONObject(response);
                JSONArray responses = jsonObject.getJSONArray("vitrines");
                for(int i = 0; i < responses.length()-1; i++)
                {
                    String str = responses.getJSONObject(i).toString();
                    Vitrine v = new Vitrine(str);
                    // Add picture path to vitrines
                    try {
                        String pictureList = NetworkTools.connect(getString(R.string.getpictures_url) + "?vitrine_id=" + v.getId());
                        JSONObject pictureObject = new JSONObject(pictureList);
                        JSONArray pictureArray = pictureObject.getJSONArray("pictures");
                        for (int j = 0; j < pictureArray.length() - 1; j++) {
                            v.addPicture(pictureArray.getJSONObject(i).getString("path"));
                        }

                        mListVitrine.add(v);
                    } catch (IOException |JSONException e)
                    {
                        // Error while retrieving pictures
                        e.printStackTrace();
                    }
                }
                success = true;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRetrieveCloseTask = null;

            loadMap();
        }

        @Override
        protected void onCancelled() {
            mRetrieveCloseTask = null;

            loadMap();
        }
    }
}
