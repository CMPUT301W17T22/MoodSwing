package com.ualberta.cmput301w17t22.moodswing.dummy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ualberta.cmput301w17t22.moodswing.R;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    //need callbacks whenver location is changed
    MapView mapView;
    private GoogleMap googleMapView;
    boolean showMap;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }



    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMapView = googleMap;

        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMapView.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMapView.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // setting the maptype as hybrid

        googleMap.setMyLocationEnabled(true);


    }
}
