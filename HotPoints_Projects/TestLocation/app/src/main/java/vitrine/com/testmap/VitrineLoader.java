package vitrine.com.testmap;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Boh on 24.11.2016.
 */

public class VitrineLoader {
    public static ArrayList<Vitrine> VITRINES = new ArrayList<>();

    static {
        VITRINES.add(new Vitrine("HE-Arc",123, new LatLng(46.997637, 6.938717), Color.BLUE));
        VITRINES.add(new Vitrine("Gare",212, new LatLng(46.996914, 6.935760), Color.RED));
        VITRINES.add(new Vitrine("Parc technologique St-Imier",356, new LatLng(47.154859, 7.002969), Color.YELLOW));
        VITRINES.add(new Vitrine("La Chaux-de-Fonds",1400, new LatLng(47.103189, 6.827200), Color.GREEN));
        VITRINES.add(new Vitrine("Suze",300, new LatLng(47.149228, 7.003468), Color.CYAN));
        VITRINES.add(new Vitrine("Gare de St-Imier",200, new LatLng(47.151649, 7.001159), Color.MAGENTA));
        VITRINES.add(new Vitrine("Le Coyote Bar",100, new LatLng(47.101729, 6.825017), Color.BLACK));
    }
}
