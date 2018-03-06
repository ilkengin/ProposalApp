package com.ilkengin.proposalapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.utils.Constants;
import com.ilkengin.proposalapp.utils.GpsOperations;
import com.ilkengin.proposalapp.utils.PermissionOperations;

import java.util.ArrayList;
import java.util.List;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapViewActivity.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private GoogleMap map;

    private Marker currentLocationMarker;
    private List<Marker> markers = new ArrayList<>();

    private boolean isMapViewLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if(map != null) {
                        LatLng current = new LatLng(location.getLatitude(),location.getLongitude());
                        if(currentLocationMarker != null) {
                            currentLocationMarker.setPosition(current);
                        } else {
                            currentLocationMarker = map.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ion_pinpoint))
                                    .position(current)
                                    .title("Konumum"));
                            markers.add(currentLocationMarker);
                        }
                        if(isMapViewLoaded) {
                            animateMapWithBounds(map,markers);
                        }
                    }
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

        if(!PermissionOperations.checkPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionOperations.requestPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            startLocationUpdates();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        Bundle args = MapViewActivity.this.getIntent().getExtras();
        if(args != null) {
            LatLng location = new LatLng(args.getDouble(Constants.ARG_LAT,36.852),args.getDouble(Constants.ARG_LONG,42.211));
            Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title(args.getString(Constants.ARG_TITLE,"Marker")));
            markers.add(marker);
        }

        if(isMapViewLoaded) {
            animateMapWithBounds(googleMap,markers);
        }

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                isMapViewLoaded = true;
                animateMapWithBounds(map,markers);
            }
        });

        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } /*else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }*/
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        if(!PermissionOperations.checkPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionOperations.requestPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            GpsOperations.controlIfGPSEnabled(MapViewActivity.this);
        }
        Log.d(TAG,"startLocationUpdates");
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void animateMapWithBounds(GoogleMap map, List<Marker> markers) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
    }
}
