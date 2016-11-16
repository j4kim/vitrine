package vitrine.com.testmap;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int MY_LOCATION_REQUEST_CODE = 12;
    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private HashMap<String,Vitrine> vitrines;

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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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
        // Add a marker in Neuchatel and move the camera
        LatLng neuchatel = new LatLng(46.989351, 6.929884);
        //mMap.addMarker(new MarkerOptions().position(neuchatel).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(neuchatel));
        //mMap.setMyLocationEnabled(true);

        vitrines = new HashMap<>();


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

            addVitrineCircle("Gare",200, Color.RED, new LatLng(46.996914, 6.935760));
            addVitrineCircle("HE-Arc",100, Color.BLUE, latlng);


            mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                @Override
                public void onCircleClick(Circle circle) {
                    //Toast toast = Toast.makeText(getApplicationContext(), circle.getCenter().toString()+" : "+circle.getId(), Toast.LENGTH_LONG);
                    //toast.show();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(),16));

                    Vitrine vitrine = vitrines.get(circle.getId());

                    Snackbar snackbar = Snackbar
                            .make(getWindow().getDecorView(),vitrine.getNom(), Snackbar.LENGTH_LONG)
                            .setAction("Contribuer", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //todo
                                }
                            });

                    snackbar.show();
                }
            });


            /*

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.info_window,null);
                    TextView tv = (TextView) view.findViewById(R.id.textView);
                    tv.setText("HE-ARC");
                    return view;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

            Marker melbourne = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("HE-Arc")
                    .snippet("Nombre de participations : 12")
            );
            melbourne.showInfoWindow();

            */
        }
    }

    private void addVitrineCircle(String name, int radius, int color, LatLng latlng) {
        Vitrine vitrine = new Vitrine(name,radius,color,latlng);

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latlng);
        circleOptions.radius(radius);

        circleOptions.fillColor(Color.argb(90,Color.red(color),Color.green(color),Color.blue(color)));
        circleOptions.strokeColor(Color.argb(120,Color.red(color),Color.green(color),Color.blue(color)));
        circleOptions.clickable(true);

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);
        vitrines.put(circle.getId(),vitrine);

        //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
        //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Yolo", String.valueOf(i));
    }
}
