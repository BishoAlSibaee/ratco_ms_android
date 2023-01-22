package com.syrsoft.ratcoms.SALESActivities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syrsoft.ratcoms.R;

public class ShowVisitsOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double LA ;
    double LO ;
    String ClientName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_visits_on_mapactivity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle b = getIntent().getExtras() ;
        LA = b.getDouble("LA");
        LO = b.getDouble("LO");
        ClientName = b.getString("ClientName");
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
        LatLng Location = new LatLng(LA, LO);
        mMap.addMarker(new MarkerOptions().position(Location).title(ClientName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location,15));
    }
}