package com.syrsoft.ratcoms;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class select_other_location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener listener ;
    Activity act ;
    Loading LocationLoadingDialog ;
    int PermissionRequestCode = 54 ;
    LatLng SelectedLocation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_other_location_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        act = this ;
        LocationLoadingDialog = new Loading(act);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                LocationLoadingDialog.close();
                locationManager.removeUpdates(listener);
                LatLng D = new LatLng(location.getLatitude(), location.getLongitude());
                SelectedLocation = D ;
                mMap.addMarker(new MarkerOptions().position(D).icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_myplaces)));
                if (mMap != null ) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(D,13));
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionRequestCode) {
            if (grantResults != null && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        LocationLoadingDialog.show();
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1,listener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,listener);
                        Log.d("locationService", "i am started");
                    }
                }
                else {
                    ToastMaker.Show(1,getResources().getString(R.string.mustAcceptLocationPermission),act);
                    act.finish();
                }
            }
        }
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            LocationLoadingDialog.show();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,listener);
        }
        else {
            AlertDialog.Builder B = new AlertDialog.Builder(act);
            B.setTitle(getResources().getString(R.string.acceptLocationPermissionTitle));
            B.setMessage(getResources().getString(R.string.acceptLocationPermissionMessage));
            B.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions((Activity) act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PermissionRequestCode);
                }
            });
            B.create();
            B.show();
        }
        //Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng X = new LatLng(mMap.getCameraPosition().target.latitude ,mMap.getCameraPosition().target.longitude );
        SelectedLocation = X ;
        mMap.addMarker(new MarkerOptions().position(X).icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_myplaces)));
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMap.clear();
                LatLng X = new LatLng(mMap.getCameraPosition().target.latitude ,mMap.getCameraPosition().target.longitude );
                SelectedLocation = X ;
                mMap.addMarker(new MarkerOptions().position(X).icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_myplaces)));
            }
        });
    }

    public void selectLocation(View view) {

        Intent intent = new Intent();
        if (SelectedLocation != null ) {
            intent.putExtra("LA",String.valueOf( SelectedLocation.latitude));
            intent.putExtra("LO" , String.valueOf( SelectedLocation.longitude));
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            ToastMaker.Show(1,"please select location ", act);
        }

    }
}