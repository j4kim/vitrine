package vitrine.com.testmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int MY_LOCATION_REQUEST_CODE = 12;
    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private HashMap<Circle,Vitrine> circlesVitrines;

    private Circle lastClickedCircle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        circlesVitrines = new HashMap<>();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Log.i("PERMISSION", "permission accordée");
        } else {
            // Show rationale and request permission.
            Log.i("PERMISSION", "permission refusée");
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Connection failed", connectionResult.getErrorMessage());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            //Toast toast = Toast.makeText(this.getApplicationContext(), latlng.toString(), Toast.LENGTH_LONG);
            //toast.show();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

            // trie les plus grande en premier comme ça elle sont dessinée avant les petites
            Collections.sort(VitrineLoader.VITRINES, new Comparator<Vitrine>() {
                @Override
                public int compare(Vitrine o1, Vitrine o2) {
                    return o2.getRadius() - o1.getRadius();
                }
            });

            for (Vitrine vitrine : VitrineLoader.VITRINES) {
                addVitrineCircle(vitrine);
            }

            mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                @Override
                public void onCircleClick(Circle circle) {
                    Vitrine vitrine = circlesVitrines.get(circle);

                    if(circle.equals(lastClickedCircle)){
                        circle.setZIndex(circle.getZIndex()-1);

                        //Toast toast2 = Toast.makeText(getApplicationContext(), vitrine.getNom()+" mise à l'arrière plan", Toast.LENGTH_SHORT);
                        //toast2.show();
                        lastClickedCircle=null;
                    }else{

                        double x = vitrine.getRadius();
                        // fonction trouvée par LoggerPro
                        float y = (float) (25.77*Math.pow(x,-0.09));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(),y));

                        Snackbar snackbar = Snackbar
                                .make(getWindow().getDecorView(),vitrine.getNom(), Snackbar.LENGTH_LONG)
                                .setAction("Voir", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //todo
                                    }
                                });
                        snackbar.show();

                        lastClickedCircle=circle;
                    }
                }
            });
        }
    }

    private void addVitrineCircle(Vitrine vitrine){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(vitrine.getLatLng());
        circleOptions.radius(vitrine.getRadius());

        int color = vitrine.getColor();
        circleOptions.fillColor(Color.argb(90,Color.red(color),Color.green(color),Color.blue(color)));
        circleOptions.strokeColor(Color.argb(180,Color.red(color),Color.green(color),Color.blue(color)));
        circleOptions.clickable(true);

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);
        circlesVitrines.put(circle,vitrine);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Yolo", String.valueOf(i));
    }
}
