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
        VITRINES.add(new Vitrine("HE-Arc",100, new LatLng(46.997637, 6.938717), Color.BLUE));
        VITRINES.add(new Vitrine("Gare",200, new LatLng(46.996914, 6.935760), Color.RED));
        VITRINES.add(new Vitrine("Parc technologique St-Imier",400, new LatLng(47.154859, 7.002969), Color.YELLOW));
    }
}
