package tn.esprit.babalioua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tn.esprit.babalioua.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener
        , GoogleMap.OnMarkerDragListener ,NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
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

    private final String url="http://192.168.211.232/android/v1/addGPS.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        navigationView.setCheckedItem(R.id.nav_localisation );

        mHandler.postDelayed(mToastRunnablle, 1000);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
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

        /**********************************************************************************************************************************/

        try {
            List<Address> addresses = geocoder.getFromLocationName("باب عليوة", 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng Tunisie = new LatLng(address.getLatitude(), address.getLongitude());
                // LatLng  Tunisie = new LatLng(36.78748815671416, 10.179720999339981);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(address.getLocality());
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Tunisie, 10));

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_GPS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message")
                                            ,Toast.LENGTH_LONG).show();
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{

                        Map<String, String> params = new HashMap<>();

                        String s=String.valueOf(address.getLatitude());
                        String s2=String.valueOf(address.getLongitude());

                        params.put("latitude", s);
                        params.put("longitude", s2);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        // Add a marker in Sydney and move the camera
        //LatLng  Tunisie = new LatLng(36.78748815671416, 10.179720999339981);
        //MarkerOptions markerOptions = new MarkerOptions().position(Tunisie).title("Beb Alioua").snippet("Station");
        //mMap.addMarker(markerOptions);
        // CameraPosition cameraPosition = new CameraPosition.Builder().zoom(10).target( Tunisie).build();
        // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

       /* try {
            List<Address> addresses = geocoder.getFromLocationName("باب عليوة", 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng Tunisie = new LatLng(address.getLatitude(), address.getLongitude());
                // LatLng  Tunisie = new LatLng(36.78748815671416, 10.179720999339981);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(address.getLocality());
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Tunisie, 10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        */
    }
    private void enableUserLocation() {
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
        mMap.setMyLocationEnabled(true);
    }
    private void zoomToUserLocation() {
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
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                mMap.addMarker(new MarkerOptions().position(latLng));


            }
        });
    }
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.toString());

        try {
            List<Address> Addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (Addresses.size() > 0) {
                Address address = Addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions().position(latLng).title(streetAddress).draggable(true));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        Log.d(TAG, "onMarkerDrag: ");
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
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
    public void onMarkerDragStart(@NonNull Marker marker) {
        Log.d(TAG, "onMarkerDragStart: ");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {

            }
        }

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            if (mMap != null){
                setUserLocationMarker(locationResult.getLastLocation());

            }

        }
    };
    private void setUserLocationMarker (Location location){
        LatLng latLng= new LatLng(location.getLatitude(), location.getLongitude());

        if (userLocationMarker == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.xxx));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);

            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

        }
        else {
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
        if ( userLocationAccuracyCircle == null){
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(2);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(52, 255, 0, 0));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle =  mMap.addCircle(circleOptions);

        }
        else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());

        }
    }
    @Override
    protected void onStart() {

        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            startLocationUpdates();
        }
        else{

        }

    }
    @Override
    protected void onStop() {

        super.onStop();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
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
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
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
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_profile:
                Intent intent2 = new Intent(MapsActivity.this, louagistes_profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_rec:
                Intent intent = new Intent(MapsActivity.this, Reclamations.class);
                startActivity(intent);
                break;

            case R.id.nav_localisation:
                break;
            case R.id.nav_dcx:
                logout();
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

    public void logout() {

        SharedPreferences sp1 = getSharedPreferences("credentials", MODE_PRIVATE);

        if (sp1.contains("id")) {
            SharedPreferences.Editor editor1 = sp1.edit();
            editor1.remove("id");
            editor1.putString("msg", "Logged out successfully");
            editor1.commit();
            Intent intent = new Intent(MapsActivity.this, Bienvenue.class);
            startActivity(intent);

        }
    }

}