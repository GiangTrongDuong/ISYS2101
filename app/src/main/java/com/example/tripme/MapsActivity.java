package com.example.tripme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.tripme.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code = 101;
    private double lat, lng;
    ImageButton hospital, restaurant, attraction, shopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hospital = findViewById(R.id.hospital);
        restaurant = findViewById(R.id.restaurant);
        attraction = findViewById(R.id.attraction);
        shopping = findViewById(R.id.shop);

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location=" + lat + "," + lng);
                stringBuilder.append("&radius=2000");
                stringBuilder.append("&type=hospital");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key=" + getResources().getString(R.string.map_api_key));

                String url = stringBuilder.toString();
                Object dataFetch[] = new Object[2];
                dataFetch[0] = mMap;
                dataFetch[1] = url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);

            }
        });

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location=" + lat + "," + lng);
                stringBuilder.append("&radius=2000");
                stringBuilder.append("&type=restaurant");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key=" + getResources().getString(R.string.map_api_key));

                String url = stringBuilder.toString();
                Object dataFetch[] = new Object[2];
                dataFetch[0] = mMap;
                dataFetch[1] = url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);
            }
        });

        attraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location=" + lat + "," + lng);
                stringBuilder.append("&radius=2000");
                stringBuilder.append("&type=amusement_park");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key=" + getResources().getString(R.string.map_api_key));

                String url = stringBuilder.toString();
                Object dataFetch[] = new Object[2];
                dataFetch[0] = mMap;
                dataFetch[1] = url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);
            }
        });

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location=" + lat + "," + lng);
                stringBuilder.append("&radius=2000");
                stringBuilder.append("&type=clothing_store");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key=" + getResources().getString(R.string.map_api_key));

                String url = stringBuilder.toString();
                Object dataFetch[] = new Object[2];
                dataFetch[0] = mMap;
                dataFetch[1] = url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);
            }
        });
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

        getCurrentLocation();
    }

    private void getCurrentLocation(){

        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_code);
            return;

        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
//                Toast.makeText(getApplicationContext(),"location result is=" + locationResult,
//                        Toast.LENGTH_LONG).show();
                if(locationResult == null){
//                    Toast.makeText(getApplicationContext(),"Current location is null",
//                            Toast.LENGTH_LONG).show();
                    return;
                }

                for(Location location:locationResult.getLocations()){

                    if(location!=null){
//                        Toast.makeText(getApplicationContext(),"Current location" + location.getLongitude(),
//                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    LatLng latLng = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                }
            }
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (Request_code){

            case Request_code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
        }
    }
}