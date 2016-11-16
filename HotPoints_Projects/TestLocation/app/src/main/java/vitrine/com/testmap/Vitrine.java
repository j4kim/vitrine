package vitrine.com.testmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Boh on 15.11.2016.
 */
public class Vitrine {
    private String nom;

    public Vitrine(String nom, int radius, int color, LatLng latlng){
        this.nom = nom;
    }

    public String getNom(){return nom;}
}
