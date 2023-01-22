package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.Attendances_Adapter;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckEmpsAttendance extends FragmentActivity implements OnMapReadyCallback {

    Activity act ;
    private GoogleMap mMap;
    private Spinner empsSpinner  ;
    private String[] empsArray ;
    private RelativeLayout mapLayout ;
    private USER SelectedEmp ;
    private String Start ="", End ="" ;
    private String getAttendacesUrl = "https://ratco-solutions.com/RatcoManagementSystem/getEmpAttendance.php" ;
    CalendarView calendar ;
    TextView StartTextView , EndTextView ;
    private List<ATTENDANCE_CLASS> attendancesList ;
    private Attendances_Adapter Adapter ;
    RecyclerView.LayoutManager manager ;
    private RecyclerView Attendances ;
    LatLngBounds.Builder builder ;
    ImageView transparent ;
    ScrollView scroll ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_check_emps_attendance_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setActivity();
    }

    void setActivity() {
        act = this ;
        StartTextView = (TextView) findViewById(R.id.CheckAttendance_Start);
        EndTextView = (TextView) findViewById(R.id.CheckAttendance_End);
        empsSpinner = (Spinner) findViewById(R.id.CheckAttend_empsSpinner);
        transparent = (ImageView) findViewById(R.id.imageView123);
        scroll = (ScrollView) findViewById(R.id.scroll);
        setEmpsSpinner();
        empsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedEmp = MyApp.EMPS.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mapLayout = (RelativeLayout) findViewById(R.id.MapLayout);
        mapLayout.setVisibility(View.GONE);
        Attendances = (RecyclerView) findViewById(R.id.Results);
        attendancesList = new ArrayList<ATTENDANCE_CLASS>();
        manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        Attendances.setLayoutManager(manager);
        StartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                D.setCancelable(false);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                Button cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button set = (Button) D.findViewById(R.id.SelectDateDialog_select);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        Start = year+"-"+(month+1)+"-"+dayOfMonth ;
                        date.setText(Start);

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (date.getText() == null || date.getText().toString().isEmpty() ) {
                            ToastMaker.Show(0,"select date",act);
                        }
                        else {
                            StartTextView.setText(Start);
                            D.dismiss();
                        }

                    }
                });
                D.show();
            }
        });
        EndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                D.setCancelable(false);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                Button cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button set = (Button) D.findViewById(R.id.SelectDateDialog_select);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        End = year+"-"+(month+1)+"-"+dayOfMonth ;
                        date.setText(End);

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (date.getText() == null || date.getText().toString().isEmpty() ) {
                            ToastMaker.Show(0,"select date",act);
                        }
                        else {
                            EndTextView.setText(End);
                            D.dismiss();
                        }

                    }
                });
                D.show();
            }
        });
        transparent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
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
        // Add a marker in Sydney and move the camera
        LatLng riyadh = new LatLng(24.701070, 46.669990);
        //mMap.addMarker(new MarkerOptions().position(riyadh).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(riyadh,12));

    }

    void setEmpsSpinner() {
        empsArray = new String[MyApp.EMPS.size()];
        for (int i=0; i< MyApp.EMPS.size(); i++) {
            empsArray[i] = MyApp.EMPS.get(i).FirstName+" "+MyApp.EMPS.get(i).LastName ;
        }
        ArrayAdapter<String> ad = new ArrayAdapter<String>(act,R.layout.spinner_item,empsArray);
        empsSpinner.setAdapter(ad);
    }

    public void getAttendances(View view) {

        Log.d("getAttendanceResp" , Start + " " + End);

        if (SelectedEmp == null ) {
            ToastMaker.Show(1,"Select Employee",act);
            return;
        }
        if ( StartTextView.getText().toString().isEmpty() || StartTextView.getText() == null  ) {
            ToastMaker.Show(1,"Select Start Date",act);
            return;
        }
        if (EndTextView.getText().toString().isEmpty() || EndTextView.getText() == null ) {
            ToastMaker.Show(1,"Select End Date",act);
            return;
        }
        mapLayout.setVisibility(View.GONE);
        attendancesList.clear();
        Loading l = new Loading(act) ;
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getAttendacesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("getAttendanceResp" , response);
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" , act);
                }
                else if (response.equals("-1")) {

                }
                else {
                    mapLayout.setVisibility(View.GONE);
                    List<Object> list = JsonToObject.translate(response,ATTENDANCE_CLASS.class,act);
                    attendancesList.clear();
                    for (Object o : list) {
                        ATTENDANCE_CLASS a = (ATTENDANCE_CLASS) o ;
                        attendancesList.add(a);
                    }
                    if (attendancesList.size()>0) {
                        Adapter = new Attendances_Adapter(attendancesList);
                        Attendances.setAdapter(Adapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("EmpID" , String.valueOf(SelectedEmp.id));
                par.put("Start" , StartTextView.getText().toString()) ;
                par.put("End" , EndTextView.getText().toString()) ;
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void showOnMap(View view) {
        mMap.clear();
        List<Marker> markers = new ArrayList<Marker>();
        if (attendancesList.size()>0) {
            mapLayout.setVisibility(View.VISIBLE);
            for (ATTENDANCE_CLASS a : attendancesList) {
                if (a.Operation == 1 ) {
                    LatLng r = new LatLng(a.LA, a.LO);
                    markers.add(mMap.addMarker(new MarkerOptions().position(r).title(a.Date)));
                }
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,400,300,0));
        }
        else {
            ToastMaker.Show(1,"No Records",act);
            mapLayout.setVisibility(View.GONE);
        }

    }
}