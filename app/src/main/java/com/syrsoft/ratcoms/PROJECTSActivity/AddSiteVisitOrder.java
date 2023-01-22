package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.ClientVisitReport;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.select_other_location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddSiteVisitOrder extends AppCompatActivity {

    Activity act ;
    Spinner ToEmps  ;
    EditText ReasonET , ProjectET , ResponsibleET , ResponsibleMobileET , NotesET ;
    TextView Address ;
    int PermissionRequestCode = 9 ;
    int  PERMISSIONS_REQUEST_READ_CONTACTS = 10 ;
    int  ContactsReqCode = 11 ;
    int ReqCodeOtherLocation = 12 ;
    Loading LocationLoadingDialog ;
    LocationManager locationManager;
    LocationListener listener;
    Location THE_LOCATION ;
    List<android.location.Address> addresses ;
    List<USER> ToEmpsList ;
    String[] ToEmpsArr ;
    USER SelectedEmp ;
    String saveSiteVisitOrderUrl = MyApp.MainUrl+"insertSiteVisitOrder.php" ;
    RequestQueue Q ;
    TextView VisitDate , VisitTime ;
    String visitDate , visitTime ;
    List<USER> SendToList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_add_site_visit_order_activity);
        setActivity();
        setActivityActions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults != null && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    act.startActivityForResult(intent,ClientVisitReport.ContactsReqCode);
                }
            }
        }
        else if (requestCode == PermissionRequestCode) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContactsReqCode) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contactData = data.getData();
                Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    ResponsibleET.setText(name);
                    String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                    if (phones.moveToFirst()) {
                        String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        ResponsibleMobileET.setText(number);
                    }
                }
            }
        }
        else if (requestCode == ReqCodeOtherLocation) {
            String LA = data.getStringExtra("LA");
            String LO = data.getStringExtra("LO");
            Log.d("otherLocation" , LA+" "+LO);
            THE_LOCATION = new Location("");
            THE_LOCATION.setLatitude(Double.parseDouble(LA));
            THE_LOCATION.setLongitude(Double.parseDouble(LO));
            Geocoder geocoder;
            geocoder = new Geocoder(act, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(THE_LOCATION.getLatitude(), THE_LOCATION.getLongitude(), 1);
                if (addresses.size() > 0 ) {
                    Address.setText(addresses.get(0).getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setActivity() {
        act = this ;
        Q = Volley.newRequestQueue(act);
        ToEmps = (Spinner) findViewById(R.id.AddSiteVisitOrder_targetedEmp);
        ReasonET = (EditText) findViewById(R.id.AddSiteVisitOrder_Reason);
        ProjectET = (EditText) findViewById(R.id.AddSiteVisitOrder_projectName);
        ResponsibleET = (EditText) findViewById(R.id.AddSiteVisitOrder_responsibleName);
        ResponsibleMobileET = (EditText) findViewById(R.id.AddSiteVisitOrder_responsibleMobile);
        NotesET = (EditText) findViewById(R.id.AddSiteVisitOrder_notes);
        Address = (TextView) findViewById(R.id.AddSiteVisitOrder_LocationTV);
        VisitDate = (TextView) findViewById(R.id.VisitDateTV);
        VisitTime = (TextView) findViewById(R.id.VisitTimeTV);
        LocationLoadingDialog = new Loading(act);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ToEmpsList = new ArrayList<USER>();
        SendToList = new ArrayList<USER>();
        Log.d("empsNum" , MyApp.EMPS.size()+" ");
        for (USER u : MyApp.EMPS) {
            if (u.id != MyApp.db.getUser().id) {
                ToEmpsList.add(u);
            }
        }
        ToEmpsArr = new String[ToEmpsList.size()+1];
        ToEmpsArr[0] = "select Employee" ;
        for (int i =0 ; i<ToEmpsList.size();i++) {
            ToEmpsArr[i+1] = ToEmpsList.get(i).FirstName+" "+ToEmpsList.get(i).LastName;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,ToEmpsArr);
        ToEmps.setAdapter(adapter);
        //SendToList.add(MyApp.DIRECT_MANAGER);
    }

    void setActivityActions() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                LocationLoadingDialog.close();
                THE_LOCATION = location;
                locationManager.removeUpdates(listener);
                Geocoder geocoder;
                geocoder = new Geocoder(act, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(THE_LOCATION.getLatitude(), THE_LOCATION.getLongitude(), 1);
                    Address.setText(addresses.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
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
        ToEmps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 ) {
                    SelectedEmp = ToEmpsList.get(position-1);
                    SendToList.add(SelectedEmp);
                    for (USER u :MyApp.EMPS) {
                        if (u.JobNumber == SelectedEmp.DirectManager) {
                            SendToList.add(u);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        VisitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        visitDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(visitDate);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VisitDate.setText(visitDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        VisitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.select_time_dialog);
                TimePicker picker=(TimePicker) D.findViewById(R.id.timePicker1);
                picker.setIs24HourView(true);
                TextView time = (TextView) D.findViewById(R.id.timeTV);
                picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay+":"+minute);
                        visitTime = hourOfDay+":"+minute ;
                    }
                });
                Button cancel = (Button) D.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Button select = (Button) D.findViewById(R.id.selectTim_select);
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (visitTime != null ) {
                            VisitTime.setText(visitTime);
                            D.dismiss();
                        }
                        else {
                            ToastMaker.Show(0,"select time",act);
                        }
                    }
                });
                D.show();
            }
        });
    }

    public void getCurrentLocation(View view) {
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
    }

    public void selectOtherLocation(View view) {
        Intent i = new Intent(act, select_other_location.class);
        act.startActivityForResult(i,ReqCodeOtherLocation);
    }

    public void importFromContacts(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && act.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            act.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            act.startActivityForResult(intent,ContactsReqCode);
        }
    }

    public void saveOrder(View view) {
        if (ReasonET.getText() == null || ReasonET.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.enterVisitReason),act);
            ReasonET.setHint(getResources().getString(R.string.enterVisitReason));
            ReasonET.setHintTextColor(Color.RED);
            return;
        }
        if (SelectedEmp == null ) {
            ToastMaker.Show(1,getResources().getString(R.string.pleaseSelectEmployee),act);
            return;
        }
        if (ProjectET.getText() == null || ProjectET.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.pleaseEnterProjectName),act);
            ProjectET.setHint(getResources().getString(R.string.pleaseEnterProjectName));
            ProjectET.setHintTextColor(Color.RED);
            return;
        }
        if (ResponsibleET.getText() == null || ResponsibleET.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.responsibleName),act);
            ResponsibleET.setHint(getResources().getString(R.string.responsibleName));
            ResponsibleET.setHintTextColor(Color.RED);
            return;
        }
        if (ResponsibleMobileET.getText() == null || ResponsibleMobileET.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.responsibleMobile),act);
            ResponsibleMobileET.setHint(getResources().getString(R.string.responsibleMobile));
            ResponsibleMobileET.setHintTextColor(Color.RED);
            return;
        }
        if (THE_LOCATION == null ) {
            ToastMaker.Show(1,getResources().getString(R.string.selectLocation),act);
            return;
        }
        if (VisitDate.getText() == null || VisitDate.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.pleaseSelectVisitDate),act);
            return;
        }
        if (VisitTime.getText() == null || VisitTime.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.pleaseSelectVisitTime),act);
            return;
        }
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, saveSiteVisitOrderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (Integer.parseInt(response) > 0 ) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved),0);
                    MyApp.sendNotificationsToGroup(SendToList, getResources().getString(R.string.newSiteVisitOrder), getResources().getString(R.string.newSiteVisitOrder) + " From " + MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "NewSiteVisitOrder", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else if (response.equals("0")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"not saved","not saved .. try again",0);
                }
                else if (response.equals("-1")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"not saved","not saved .. try again",0);
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
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Map<String,String> par = new HashMap<String, String>();
                par.put("SalesMan", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("ForwardedTo", String.valueOf(SelectedEmp.JobNumber));
                par.put("VisitReason" , ReasonET.getText().toString());
                par.put("ProjectName" , ProjectET.getText().toString());
                par.put("ResponsibleName" , ResponsibleET.getText().toString());
                par.put("ResponsibleMobile" , ResponsibleMobileET.getText().toString());
                par.put("LA" , String.valueOf(THE_LOCATION.getLatitude()));
                par.put("LO", String.valueOf(THE_LOCATION.getLongitude()));
                par.put("Date",Date );
                par.put("VisitDate", VisitDate.getText().toString());
                par.put("VisitTime", VisitTime.getText().toString());
                if (NotesET.getText() != null && !NotesET.getText().toString().isEmpty()) {
                    par.put("Notes" , NotesET.getText().toString());
                }
                return par;
            }
        };
        Q.add(request);
    }

}