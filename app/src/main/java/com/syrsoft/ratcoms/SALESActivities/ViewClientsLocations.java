package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewClientsLocations extends AppCompatActivity implements OnMapReadyCallback {
    public Activity act;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location CurrentLocation;
    private LocationListener listener;
    List<Marker> markerList;
    List<USER> user;
    private Spinner EmployeesSpinner;
    List<List<CLIENT_CLASS>> EmployeeClientList = new ArrayList<>();
    ProgressBar prog ;
    String getMyClients = "https://ratco-solutions.com/RatcoManagementSystem/getMyClients.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clients_locations);
        setActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        EmployeesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                if (index == adapterView.getCount() - 1) {
                    Log.d("selectedBefore", "Selected ALL");
                    //Selected ALL
                    for (int i = 0; i < EmployeeClientList.size(); i++) {
                        List<CLIENT_CLASS> cl = EmployeeClientList.get(i);
                        if (cl == null) {
                            getClientLocation(String.valueOf(user.get(i).JobNumber), i);
                        }
                    }
                } else {
                    if (EmployeeClientList.get(index) == null) {
                        Log.d("selectedBefore", "Selected First");
                        // get clients
                        mMap.clear();
                        markerList.clear();
                        getClientLocation(String.valueOf(user.get(index).JobNumber), index);
                    } else {
                        Log.d("selectedBefore", "Selected Before");
                        mMap.clear();
                        markerList.clear();
                        List<CLIENT_CLASS> selectedClientList = EmployeeClientList.get(index);
                        // show on map
                        for (CLIENT_CLASS client : selectedClientList) {
                            LatLng clientLocation = new LatLng(client.LA, client.LO);
                            markerList.add(mMap.addMarker(new MarkerOptions().position(clientLocation).title(client.id + " " + client.SalesMan)));
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setActivity() {
        act = this;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        markerList = new ArrayList<>();
        EmployeesSpinner = findViewById(R.id.Emps_Spinner);
        user = new ArrayList<>();
        prog = findViewById(R.id.progressBar9);
        getEmployee();
        String[] emps;
        emps = new String[user.size() + 1];
        for (int i = 0; i < user.size(); i++) {
            emps[i] = user.get(i).FirstName + " " + user.get(i).LastName;
        }
        emps[user.size()] = "All";
        ArrayAdapter<String> empsArr = new ArrayAdapter<>(act, R.layout.spinner_item, emps);
        EmployeesSpinner.setAdapter(empsArr);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Get current location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // Get latitude and longitude
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d("CurrentLocation", "Latitude: " + latitude);
                Log.d("CurrentLocation", "Longitude: " + longitude);
                LatLng currentLatLng = new LatLng(latitude, longitude);
                //  mMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                locationManager.removeUpdates(this);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                Log.d("CurrentLocation", "onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Log.d("CurrentLocation", "onProviderDisabled");
                checkLocationEnabled();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("CurrentLocation", "onStatusChanged");

            }
        };
        // Request location updates
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        final Marker[] lastMarker = {null};
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (lastMarker[0] != null) {
                    if (marker.getId().equals(lastMarker[0].getId())) {
                        for (List<CLIENT_CLASS> cl : EmployeeClientList) {
                            if (cl != null) {
                                for (CLIENT_CLASS cc :cl) {
                                    USER u = USER.searchUserByJobNumber(MyApp.EMPS,cc.SalesMan);
                                    String v = u.FirstName+" "+u.LastName + ": "+cc.ClientName;
                                    if (v.equals(marker.getTitle())) {
                                        Intent ac = new Intent(act, ViewMyVisitDetailes.class);
                                        ac.putExtra("ItemId", cc.id);
                                        startActivity(ac);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                lastMarker[0] = marker;
                return false;
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void checkLocationEnabled() {
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                CurrentLocation = null;
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 2, 1, listener);
                }
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 2, 1, listener);
                }
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(act);
                final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                final String message = getResources().getString(R.string.locationNotEnabledMessage);
                String title = getResources().getString(R.string.locationNotEnabledTitle);
                builder.setMessage(message).setTitle(title).setPositiveButton("OK",
                        (d, id) -> {
                            act.startActivity(new Intent(action));
                            d.dismiss();
                        }).setNegativeButton("Cancel",
                        (d, id) -> {
                            d.dismiss();
                            //startEndWorkDay.setChecked(false);
                        });
                builder.create().show();
            }
        } else {
            Log.d("locationService", "no permission");
            AlertDialog.Builder B = new AlertDialog.Builder(act);
            B.setTitle(getResources().getString(R.string.acceptLocationPermissionTitle));
            B.setMessage(getResources().getString(R.string.acceptLocationPermissionMessage));
            B.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            });
            B.create();
            B.show();
        }
    }

    public void getClientLocation(String JobNumber, int index) {
        prog.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, getMyClients, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<CLIENT_CLASS> clientList = new ArrayList<>();
                if (response.equals("0")) {
                    //No Record
                    EmployeeClientList.set(index, new ArrayList<>());
                    prog.setVisibility(View.GONE);

                } else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CLIENT_CLASS c = new CLIENT_CLASS(row.getInt("id"), row.getString("ClientName"), row.getString("City"), row.getString("PhonNumber"), row.getString("Address"), row.getString("Email"), row.getInt("SalesMan"), row.getDouble("LA"), row.getDouble("LO"), row.getString("FieldOfWork"));
                            clientList.add(c);
                        }
                        EmployeeClientList.set(index, clientList);
                        for (CLIENT_CLASS C : clientList) {
                            LatLng MyClient = new LatLng(C.LA, C.LO);
                            USER u = USER.searchUserByJobNumber(MyApp.EMPS,C.SalesMan);
                            markerList.add(mMap.addMarker(new MarkerOptions().position(MyClient).title(u.FirstName+" "+u.LastName + ": " + C.ClientName)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    prog.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("locationsResponse", error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("jn", JobNumber);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void getEmployee() {
        for (int i = 0; i < MyApp.EMPS.size(); i++) {
            if (MyApp.db.getUser().JobTitle.equals("Sales Manager") || MyApp.db.getUser().JobTitle.equals("Manager")) {
                if (MyApp.EMPS.get(i).Department.equals("Sales")) {
                    user.add(MyApp.EMPS.get(i));
                }
            }
                else if (MyApp.db.getUser().JobTitle.equals("Branch Manager")) {
                    if (MyApp.EMPS.get(i).DepartmentManager == MyApp.db.getUser().JobNumber) {
                    user.add(MyApp.EMPS.get(i));
                }
            } else {
                ToastMaker.Show(1, "You don't Have Employees", act);
            }
        }
        for (USER u : user) {
            EmployeeClientList.add(null);
        }

    }
}