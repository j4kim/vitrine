package com.vitrine.vitrine.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.vitrine.vitrine.R;
import com.vitrine.vitrine.Vitrine;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscoverFragment extends Fragment implements OnMapReadyCallback {

    ArrayList<Vitrine> mListVitrine;
    private GoogleMap mMap;
    private HashMap<Circle, Vitrine> circlesVitrines;
    private Circle lastClickedCircle=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListVitrine = new ArrayList<>();

        //TODO : GET LIST FROM THE SERVER
        mListVitrine.add(new Vitrine("HE-Arc",123, 46.997637, 6.938717, Color.BLUE));
        mListVitrine.add(new Vitrine("Gare",212, 46.996914, 6.935760, Color.RED));
        mListVitrine.add(new Vitrine("Parc technologique St-Imier",356, 47.154859, 7.002969, Color.YELLOW));
        mListVitrine.add(new Vitrine("La Chaux-de-Fonds",1400, 47.103189, 6.827200, Color.GREEN));
        mListVitrine.add(new Vitrine("Suze",300, 47.149228, 7.003468, Color.CYAN));
        mListVitrine.add(new Vitrine("Gare de St-Imier",200, 47.151649, 7.001159, Color.MAGENTA));
        mListVitrine.add(new Vitrine("Le Coyote Bar",100, 47.101729, 6.825017, Color.BLACK));

        SupportMapFragment mapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        mapFragment.getMapAsync(this);

        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
                }else{

                    double x = vitrine.getRadius();
                    // fonction trouvée par LoggerPro
                    float y = (float) (25.77*Math.pow(x,-0.09));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(),y));

                    /*
                    //TODO: CONVERT SNACKBAR TO WORK IN A FRAGMENT
                    Snackbar snackbar = Snackbar
                            .make(getWindow().getDecorView(),vitrine.getNom(), Snackbar.LENGTH_INDEFINITE)
                            .setAction("Voir", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();*/

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
}
