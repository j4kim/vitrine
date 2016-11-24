package vitrine.com.testmap;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Boh on 15.11.2016.
 */
public class Vitrine {
    private String nom;
    private LatLng latLng;
    private int radius;
    private int color;

    public Vitrine(String nom, int radius, LatLng latlng, int color){
        this.nom = nom;
        this.radius = radius;
        this.latLng = latlng;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getNom(){return nom;}

    public LatLng getLatLng() {
        return latLng;
    }

    public int getRadius() {
        return radius;
    }
}
