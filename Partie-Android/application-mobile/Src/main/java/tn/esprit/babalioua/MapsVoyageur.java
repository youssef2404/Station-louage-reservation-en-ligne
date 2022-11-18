package tn.esprit.babalioua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import tn.esprit.babalioua.databinding.ActivityMapsVoyageurBinding;

public class MapsVoyageur extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener
        , GoogleMap.OnMarkerDragListener , NavigationView.OnNavigationItemSelectedListener  {

    private GoogleMap mMap;
    private ActivityMapsVoyageurBinding binding;

    private static final String TAG = "MapsActivity";
    private Geocoder geocoder;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private Handler mHandler = new Handler();


    //TextView t;

    private final String url="http://192.168.211.232/android/getLoc.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsVoyageurBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        /* ------------- FIN -------------------------------------*/



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* -------------------------------------------------------------------- */
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = locationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_loc);

        mHandler.postDelayed(mToastRunnablle, 1000);


    }



        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMarkerDragListener(this);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // enableUserLocation();
                // zoomToUserLocation();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[]
                                    {Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_LOCATION_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]
                                    {Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_LOCATION_REQUEST_CODE);
                }
            }

            // Add a marker in Sydney and move the camera
            /**********************************************************************************************************************************/
            int n = 7;
            /* --------------------- get latitude et longitude --------------------- */
            String qrystring = "?t1=" + n;
            class dbclass extends AsyncTask<String, Void, String> {

                protected void onPostExecute(String data) {
                    int pos = data.indexOf("&");

                    String latitude = data.substring(0, pos - 1);
                    String longitude = data.substring(pos + 1);
                    double lati = Double.parseDouble(latitude);
                    double longi = Double.parseDouble(longitude);

                    LatLng Tunisie = new LatLng(36.78941281192551, 10.18144834188903);
                    MarkerOptions markerOptions = new MarkerOptions().position(Tunisie).title("Beb Alioua").snippet("Station");
                    mMap.addMarker(markerOptions);
                    CameraPosition cameraPosition = new CameraPosition.Builder().zoom(10).target(Tunisie).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    LatLng Haouaria = new LatLng(lati, longi);
                    MarkerOptions markerOptions2 = new MarkerOptions().position(Haouaria).title("Haouaria").snippet("Louage");
                    mMap.clear();
                    mMap.addMarker(markerOptions);
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.xxx));
                    mMap.addMarker(markerOptions2);
                    CameraPosition cameraPosition2 = new CameraPosition.Builder().zoom(10).target(Haouaria).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2));



                }


                @Override
                protected String doInBackground(String... param) {
                    try {
                        URL url = new URL(param[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        return br.readLine();


                    } catch (Exception ex) {
                        return ex.getMessage();
                    }
                }
            }

            dbclass obj = new dbclass();
            obj.execute(url + qrystring);


        }


        @Override
        public void onMapLongClick (@NonNull LatLng latLng){


        }
        @Override
        public void onMarkerDrag (@NonNull Marker marker){
            Log.d(TAG, "onMarkerDrag: ");
        }
        @Override
        public void onMarkerDragEnd (@NonNull Marker marker){
            Log.d(TAG, "onMarkerDragEnd: ");
            LatLng latLng = marker.getPosition();
            try {

                List<Address> Addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (Addresses.size() > 0) {
                    Address address = Addresses.get(0);
                    String streetAddress = address.getAddressLine(0);
                    marker.setTitle(streetAddress);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        @Override
        public void onMarkerDragStart (@NonNull Marker marker){
            Log.d(TAG, "onMarkerDragStart: ");
        }


        @Override
        public void onBackPressed () {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }

        }

        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){

            switch (item.getItemId()) {

                case R.id.nav_bienvenue:
                    Intent intent = new Intent(MapsVoyageur.this, Bienvenue.class);
                    startActivity(intent);
                    break;

                case R.id.nav_station:
                    Intent intent3 = new Intent(MapsVoyageur.this, list_stations.class);
                    startActivity(intent3);
                    break;


                case R.id.nav_localisation:
                    break;

                case R.id.nav_reserver:
                    Intent intent2 = new Intent(MapsVoyageur.this, Reservation.class);
                    startActivity(intent2);
                    break;

            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        }


    private Runnable mToastRunnablle = new Runnable() {
        @Override
        public void run() {

            onMapReady (mMap);
            mHandler.postDelayed(this,2000);
        }
    };

    private BitmapDescriptor bitmapDescriptor(Context context, int vectorResId){

        Drawable vectorDrawble = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawble.setBounds(0,0,vectorDrawble.getIntrinsicWidth(),vectorDrawble.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawble.getIntrinsicWidth(), vectorDrawble.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }
}