package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syrsoft.ratcoms.HRActivities.ATTENDANCE_CLASS;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyMultipartRequest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class ClientVisitReport extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    RadioButton newClientRadio, oldClientRadio;
    public static LocationManager locationManager;
    public static LocationListener listener;
    public static Activity act;
    public static Location THE_LOCATION;
    public static Loading LocationLoadingDialog;
    public static int TheNewClientID;
    public static CLIENT_CLASS THE_CLIENT;
    public static List<CLIENT_CLASS> THE_Result_CLIENTS , THE_Result_CLIENTSByLocation ;
    public static List<RESPONSIBLE_CLASS> Responsible;
    public static TextView ClientNameTextView, ResponsibleTextView, ResponsibleJobTextView, ResponsibleMobileTextView;
    EditText projectDesc, visitDetails;
    public static String saveInChargeUrl = MyApp.MainUrl + "insertInChargeClient.php";
    String saveVisitUrl = MyApp.MainUrl + "insertNewClientVisit.php";
    String getInChargesUrl = MyApp.MainUrl + "getInCharges.php";
    String getClientsByLocationUrl = MyApp.MainUrl + "searchClientByLocation.php";
    public static int CAM_PERMISSION_REQCODE = 100;
    public static int CAM_PERMISSION_REQCODE_QUOTATION = 31;
    public static int CAM_PERMISSION_REQCODE_LOCATION = 33;
    public static int CAM_REQCODE = 101;
    public static int CAM_REQCODE_QUOTATION = 32;
    public static int CAM_REQCODE_LOCATION = 34;
    public static int ATTACHFILE_REQCODE = 102;
    public static int ATTACHFILE_REQCODE_Quotation = 36;
    public static int ATTACHFILE_REQCODE_Location = 37;
    public static int ContactsReqCode = 23;
    AddNewClient_DIALOG AddNewdialog;
    public static Bitmap CardBitmap, QuotationBitmap, ClientLocation;
    String saveImageUrl = MyApp.MainUrl +"insertPhotoToFolderAndTable.php";
    String recordFileLinkInTableUrl = MyApp.MainUrl + "updateTableLinkField.php";
    private String upload_URL = MyApp.MainUrl + "insertFile.php";
    String searchClientUrl = MyApp.MainUrl + "searchClient.php" ;
    String insertNewClientLocationUrl = MyApp.MainUrl +"insertNewClientLocation.php";
    static int PhotoSource = 0;
    static int RequestCode = 65;
    static int PERMISSIONS_REQUEST_READ_CONTACTS = 78;
    static int AttachFileCode = 44;
    RequestQueue Q;
    List<Marker> markers;
    ImageView transparent;
    ScrollView scroll;
    Spinner responsiblesSpinner;
    RESPONSIBLE_CLASS SelectedResponsible;
    RadioButton InterestedRB, UnInterestedRB;
    int INTERESTED = 0;
    TextView FollowUpDateTV;
    String FDate = "";
    Uri UriFile;
    String FileName;
    Button attachFileBtn , addClientLocation;
    Dialog D ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_client_visit_report_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setActivity();
        setViewsActions();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        // Add a marker in Sydney and move the camera
        LatLng riyadh = new LatLng(24.701070, 46.669990);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(riyadh, 12));
    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        newClientRadio = (RadioButton) findViewById(R.id.ClientVisitReport_newClientRb);
        oldClientRadio = (RadioButton) findViewById(R.id.ClientVisitReport_oldClientRb);
        ClientNameTextView = (TextView) findViewById(R.id.ClientVisitReport_clientNameTextView);
        ResponsibleTextView = (TextView) findViewById(R.id.ClientVisitReport_responsibleNameTextView);
        ResponsibleJobTextView = (TextView) findViewById(R.id.ClientVisitReport_responsibleJobTextView);
        ResponsibleMobileTextView = (TextView) findViewById(R.id.ClientVisitReport_responsibleMobileTextView);
        projectDesc = (EditText) findViewById(R.id.ClientVisitReport_projectDescTextView);
        visitDetails = (EditText) findViewById(R.id.ClientVisitReport_VisitTextView);
        InterestedRB = (RadioButton) findViewById(R.id.ClientVisitReport_InterestedRB);
        UnInterestedRB = (RadioButton) findViewById(R.id.ClientVisitReport_unInterestedRB);
        responsiblesSpinner = (Spinner) findViewById(R.id.ClientVisitReport_responsibleNameSpinner);
        attachFileBtn = (Button) findViewById(R.id.button35);
        addClientLocation = (Button) findViewById(R.id.button38);
        addClientLocation.setVisibility(View.GONE);
        FollowUpDateTV = (TextView) findViewById(R.id.followUpDate);
        LocationLoadingDialog = new Loading(act);
        transparent = (ImageView) findViewById(R.id.imageView123);
        scroll = (ScrollView) findViewById(R.id.scroll);
        Responsible = new ArrayList<RESPONSIBLE_CLASS>();
        THE_Result_CLIENTS = new ArrayList<CLIENT_CLASS>();
        THE_Result_CLIENTSByLocation = new ArrayList<CLIENT_CLASS>();
        markers = new ArrayList<Marker>();
        D = new Dialog(act);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location == null) {
                    ToastMaker.Show(0, "searching location", act);
                } else {
                    if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (location.getAccuracy() < 12) {
                        LocationLoadingDialog.close();
                        THE_LOCATION = location ;
                        LatLng riyadh = new LatLng(THE_LOCATION.getLatitude(), THE_LOCATION.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(riyadh,10));
                        locationManager.removeUpdates(listener);
                    }
                }
//                Geocoder geocoder;
//                List<Address> addresses;
//                geocoder = new Geocoder(act, Locale.getDefault());
//                try {
//                    addresses = geocoder.getFromLocation(THE_LOCATION.getLatitude(),THE_LOCATION.getLongitude(), 1);
//                    if (AddNewClient_DIALOG.D.isShowing()) {
//                        AddNewClient_DIALOG.address.setText(addresses.get(0).getAddressLine(0));
//                        AddNewClient_DIALOG.city.setText(addresses.get(0).getLocality());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

        };
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationLoadingDialog.show();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000*2,1,listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*2,1,listener);
            Log.d("locationService", "i am started");
        }
        else {
            Log.d("locationService", "no permission");
            AlertDialog.Builder B = new AlertDialog.Builder(act);
            B.setTitle(getResources().getString(R.string.acceptLocationPermissionTitle));
            B.setMessage(getResources().getString(R.string.acceptLocationPermissionMessage));
            B.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions((Activity) act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},RequestCode);
                }
            });
            B.create();
            B.show();
        }

    }

    void setViewsActions() {
        newClientRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    oldClientRadio.setChecked(false);
                    AddNewdialog = new AddNewClient_DIALOG(act);
                    AddNewdialog.show();
                    addClientLocation.setVisibility(View.GONE);
                }
            }
        });
        oldClientRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    newClientRadio.setChecked(false);
                    //OldClient_DIALOG D = new OldClient_DIALOG(act);
                    //D.show();
                    addClientLocation.setVisibility(View.VISIBLE);
                    getClientsByLocation();
                }
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
        responsiblesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedResponsible = Responsible.get(position);
                ResponsibleTextView.setText(SelectedResponsible.Name);
                ResponsibleJobTextView.setText(SelectedResponsible.JobTitle);
                ResponsibleMobileTextView.setText(SelectedResponsible.MobileNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        InterestedRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UnInterestedRB.setChecked(false);
                    INTERESTED = 1 ;
                }
            }
        });
        UnInterestedRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    InterestedRB.setChecked(false);
                    INTERESTED = 0 ;
                }
            }
        });
        FollowUpDateTV.setOnClickListener(new View.OnClickListener() {
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
                        FDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(FDate);
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
                        FollowUpDateTV.setText(FDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode) {
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
        else if (requestCode == CAM_PERMISSION_REQCODE) {

        }
        else if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults != null && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    act.startActivityForResult(intent,ClientVisitReport.ContactsReqCode);
                }
            }
        }
    }

    public static void saveInCharge(String name , String job , String Mobile , String email , int CID) {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, saveInChargeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("-1")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"no parameters","");
                }
                else if (response.equals("0")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,act.getResources().getString(R.string.default_error_msg),"");
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        Responsible.add(new RESPONSIBLE_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("Name"),row.getString("JobTitle"),row.getString("MobileNumber"),row.getString("Email"),row.getString("Link")));
                        THE_CLIENT.setResponsibles(Responsible);
                        ResponsibleTextView.setText(THE_CLIENT.getResponsibles().get(0).Name);
                        ResponsibleJobTextView.setText(ClientVisitReport.THE_CLIENT.getResponsibles().get(0).JobTitle);
                        ResponsibleMobileTextView.setText(ClientVisitReport.THE_CLIENT.getResponsibles().get(0).MobileNumber);
                        if (CardBitmap != null ) {
                            Log.d("saveImageResponse" , "not null");
                            MyApp.savePhoto(CardBitmap,"InChargeClients",Responsible.get(0).id,PhotoSource,"C");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("saveImageResponse" , e.getMessage());
                    }
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,act.getResources().getString(R.string.saved),act.getResources().getString(R.string.saved));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                ToastMaker.Show(1,"error try again ",act);
                MyApp.sendError(error.getMessage(),act.getLocalClassName(),MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName,"saveInCharge");
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("ClientID" , String.valueOf(CID));
                par.put("Name",name);
                par.put("JobTitle",job);
                par.put("MobileNumber" , Mobile);
                par.put("Email",email);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void saveVisit(View view) {
        if (THE_CLIENT == null ) {
            ToastMaker.Show(1,"please select client first",act);
            return;
        }
        if (projectDesc.getText() == null || projectDesc.getText().toString().isEmpty()) {
            ToastMaker.Show(1,"please enter project description",act);
            return;
        }
        if (visitDetails.getText() == null || visitDetails.getText().toString().isEmpty()) {
            ToastMaker.Show(1,"please enter visit details",act);
            return;
        }
        Loading l = new Loading(act); l.show();
        StringRequest request = new StringRequest(Request.Method.POST, saveVisitUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LocationLoadingDialog.close();
                l.close();
                Log.d("saveVisitResponse",response);
                if (response.equals("0")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "error" ,"error while saving .. try again ");
                }
                else if (response.equals("-1")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.notSavedErrorTitle),getResources().getString(R.string.notSavedErrorMessage));
                }
                else {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved),0);
                    int ID = Integer.parseInt(response);
                    if (QuotationBitmap != null ) {
                        MyApp.savePhoto(QuotationBitmap,"ClientVisitReport",ID,"QuotationLink",PhotoSource,"Q");
                    }
                    if (ClientLocation != null ) {
                        MyApp.savePhoto(ClientLocation,"ClientVisitReport",ID,"LocationLink",PhotoSource,"L");
                    }
                    if (UriFile != null && FileName != null ) {
                        uploadPDF(FileName,UriFile,ID);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("gfgfgfgfg" , error.toString());
                l.close();
                LocationLoadingDialog.close();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.notSavedErrorTitle),getResources().getString(R.string.notSavedErrorMessage));
                String Error = "" ;
                if (error.getMessage() == null ) {
                    Error = error.toString();
                }
                else {
                    Error = error.getMessage() ;
                }
                MyApp.sendError(Error,act.getLocalClassName(),MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName,"saveVisit");
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                String Time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                Map<String,String> par = new HashMap<String, String>();
                par.put("ClientID" , String.valueOf( THE_CLIENT.id));
                par.put("ClientName",THE_CLIENT.ClientName);
                par.put("Date",Date);
                par.put("Time",Time);
                par.put("Address", THE_CLIENT.Address);
                par.put("SalesMan", String.valueOf(THE_CLIENT.SalesMan));
                par.put("ProjectDescription",projectDesc.getText().toString());
                par.put("VisitDetails",visitDetails.getText().toString());
                if (THE_LOCATION != null ) {
                    par.put("LA", String.valueOf(THE_LOCATION.getLatitude()));
                    par.put("LO",String.valueOf(THE_LOCATION.getLongitude()));
                }
                else {
                    par.put("LA", String.valueOf(0));
                    par.put("LO",String.valueOf(0));
                }
                par.put("Responsible",ResponsibleTextView.getText().toString());
                if (INTERESTED != 2) {
                    par.put("Interested" , String.valueOf(INTERESTED));
                }
                if (FollowUpDateTV.getText() != null && !FollowUpDateTV.getText().toString().isEmpty()) {
                    par.put("FollowUpAt" , FollowUpDateTV.getText().toString());
                }
                return par;
            }
        };
        request.setShouldRetryConnectionErrors(true);
        Volley.newRequestQueue(act).add(request);

//        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            ClientVisitReport.LocationLoadingDialog.show();
//            ClientVisitReport.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000*2,1,saveListener);
//            ClientVisitReport.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*2,1,saveListener);
//            Log.d("locationService", "i am started");
//        }
//        else {
//            Log.d("locationService", "no permission");
//            ActivityCompat.requestPermissions((Activity) act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},ReqCode);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Random r = new Random() ;
        int x = r.nextInt(10000);
        if (requestCode == ATTACHFILE_REQCODE) {

            if (resultCode == RESULT_OK)
            {
                if (data == null) {
                    //Display an error
                    return;
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    CardBitmap = bmp ;
                    AddNewdialog.image.setImageBitmap(bmp);
                    AddNewdialog.fileName.setText(x+".jpg");
                    //ConvertedImage = convertImageToBase64(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.d("path" , "error result "+data.getData().getPath());
            }

        }
        else if (requestCode == CAM_REQCODE)
        {
            //x = r.nextInt(10000);
            if (resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                CardBitmap = imageBitmap ;
                AddNewdialog.image.setImageBitmap(imageBitmap);
                AddNewdialog.fileName.setText(String.valueOf(x)+".jpg");
                //ConvertedImage = convertImageToBase64(imageBitmap);
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }
        }
        else if (requestCode == CAM_REQCODE_LOCATION) {
            if (resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ClientLocation = imageBitmap ;
                ImageView image = (ImageView) findViewById(R.id.locationImage);
                image.setImageBitmap(ClientLocation);
                //fileName.setText(String.valueOf(x)+".jpg");
                //ConvertedImage = convertImageToBase64(imageBitmap);
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }
        }
        else if ( requestCode == ATTACHFILE_REQCODE_Location) {

            if (resultCode == RESULT_OK)
            {
                if (data == null) {
                    //Display an error
                    return;
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    ClientLocation = bmp ;
                    ImageView image = (ImageView) findViewById(R.id.locationImage);
                    image.setImageBitmap(ClientLocation);
                    //AddNewdialog.fileName.setText(x+".jpg");
                    //ConvertedImage = convertImageToBase64(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //Log.d("path" , "error result "+data.getData().getPath());
            }
        }
        else if (requestCode == CAM_REQCODE_QUOTATION) {
            if (resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                QuotationBitmap = imageBitmap ;
                ImageView image = (ImageView) findViewById(R.id.quotationImage);
                image.setImageBitmap(QuotationBitmap);
                //fileName.setText(String.valueOf(x)+".jpg");
                //ConvertedImage = convertImageToBase64(imageBitmap);
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }
        }
        else if (requestCode == ATTACHFILE_REQCODE_Quotation) {
            if (resultCode == RESULT_OK)
            {
                if (data == null) {
                    //Display an error
                    return;
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    QuotationBitmap = bmp ;
                    ImageView image = (ImageView) findViewById(R.id.quotationImage);
                    image.setImageBitmap(QuotationBitmap);
                    //AddNewdialog.fileName.setText(x+".jpg");
                    //ConvertedImage = convertImageToBase64(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //Log.d("path" , "error result "+data.getData().getPath());
            }
        }
        else if (requestCode == ContactsReqCode) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contactData = data.getData();
                Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    AddNewClient_DIALOG.responsibleName.setText(name);
                    String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                    if (phones.moveToFirst()) {
                        String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        AddNewClient_DIALOG.responsibleMobile.setText(number);
                    }
                    Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
                    String email = "" ;
                    if (emails.moveToFirst()){
                        email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        AddNewClient_DIALOG.responsibleEmail.setText(email);
                    }
                   // Log.d("selectedContact" , name+" "+number+" "+email);
                    // TODO Whatever you want to do with the selected contact name.
                }
            }
        }
        else if (requestCode == AttachFileCode) {
            Log.d("fileselection", "here");
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Log.d("fileselection", "error");
                    return;
                }
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    UriFile = uri;
                    // Perform operations on the document using its URI.
                    File F = new File(uri.toString());
                    String path = F.getAbsolutePath();
                    String displayName = null;
                    if (uri.toString().startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = this.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                FileName = displayName;
                                Log.d("fileselection", displayName);
                                attachFileBtn.setText(FileName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uri.toString().startsWith("file://")) {
                        displayName = F.getName();
                        Log.d("fileselection", displayName);
                    }
                }
//                try {
//                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
//                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
//                    try {
//                        File F = bufferedInputStream.read() ;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    CardBitmap = bmp ;
//                    image.setImageBitmap(bmp);
//                    fileName.setText(x+".jpg");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            } else {
                //Log.d("path", "error result " + data.getData().getPath());
            }
        }
    }

    public void addClientLocationPhoto(View view) {
        AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
        selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
        selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
        selectDialog.setNegativeButton(getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},CAM_PERMISSION_REQCODE_LOCATION);
                }
                else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        act.startActivityForResult(takePictureIntent, CAM_REQCODE_LOCATION);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }

                }
            }
        });
        selectDialog.setPositiveButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                open.setType("image/*");
                act.startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE_Location);
            }
        });
        selectDialog.create().show();
    }

    public void addQuotationPhoto(View view) {
        AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
        selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
        selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
        selectDialog.setNegativeButton(getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},CAM_PERMISSION_REQCODE_QUOTATION);
                }
                else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        act.startActivityForResult(takePictureIntent, CAM_REQCODE_QUOTATION);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }

                }
            }
        });
        selectDialog.setPositiveButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                open.setType("image/*");
                act.startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE_Quotation);
            }
        });
        selectDialog.create().show();
    }

    public static void setCardImageNull() {
        CardBitmap = null ;
    }

    public static void setQuotationImageNull() {
        QuotationBitmap = null ;
    }

    public static void setLocationImageNull() {
        ClientLocation = null ;
    }

    void getClientsByLocation() {
        THE_Result_CLIENTSByLocation.clear();
        markers.clear();
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST,getClientsByLocationUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("locations" , response);
                if (response.equals("0")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"No Clients",getResources().getString(R.string.noClientsAtThisLocation));
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"error",act);
                }
                else {
                    try {
                        THE_Result_CLIENTSByLocation.clear();
                        JSONArray arr = new JSONArray(response);
                        Log.d("locations" , arr.length()+"");
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CLIENT_CLASS c = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                            THE_Result_CLIENTSByLocation.add(c);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mMap.clear();

                    if (THE_Result_CLIENTSByLocation.size()>0) {
                        for (CLIENT_CLASS C : THE_Result_CLIENTSByLocation) {
                                LatLng r = new LatLng(C.LA, C.LO);
                                if (C.SalesMan == MyApp.db.getUser().JobNumber) {
                                    markers.add(mMap.addMarker(new MarkerOptions().position(r).title(C.id+" "+C.ClientName).icon(BitmapDescriptorFactory.fromResource(R.drawable.green_location))));
                                }
                                else {
                                    markers.add(mMap.addMarker(new MarkerOptions().position(r).title(C.id+" "+C.ClientName).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_location))));
                                }

                        }
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Marker marker : markers) {
                            marker.showInfoWindow();
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,10));
                        for (Marker M : markers) {
                            M.showInfoWindow();
                        }
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                Log.d("markerLocation" , "LA "+THE_LOCATION.getLatitude()+" "+marker.getPosition().latitude+" LO "+THE_LOCATION.getLongitude()+" "+marker.getPosition().longitude);
                                for(int i=0;i<THE_Result_CLIENTSByLocation.size();i++) {
                                    if (marker.getTitle().equals(THE_Result_CLIENTSByLocation.get(i).id+" "+THE_Result_CLIENTSByLocation.get(i).ClientName)) {
                                        if (MyApp.db.getUser().JobTitle.equals("Manager") || MyApp.db.getUser().JobTitle.equals("Sales Manager")) {
                                            THE_CLIENT = THE_Result_CLIENTSByLocation.get(i);
                                            ClientNameTextView.setText(THE_CLIENT.ClientName);
                                            getInCharges();
                                            break;
                                        }
                                        else {
                                            if (THE_Result_CLIENTSByLocation.get(i).SalesMan == MyApp.db.getUser().JobNumber) {
                                                THE_CLIENT = THE_Result_CLIENTSByLocation.get(i);
                                                ClientNameTextView.setText(THE_CLIENT.ClientName);
                                                getInCharges();
                                                break;
                                            }
                                            else {
                                                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.thisClientIsNotYours),getResources().getString(R.string.thisClientIsNotYoursMessage));
                                            }
                                        }


                                    }
                                }
                                return false;
                            }
                        });
                    }
                    else {
                        ToastMaker.Show(1,"No Records",act);
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
                double MaxLA = THE_LOCATION.getLatitude() + 0.0010000 ;
                double MinLA = THE_LOCATION.getLatitude() - 0.0010000 ;
                double MaxLO = THE_LOCATION.getLongitude() + 0.0010000 ;
                double MinLO = THE_LOCATION.getLongitude() - 0.0010000 ;
                Log.d("locations" , MaxLA+" "+MinLA+" "+MaxLO+" "+MinLO);
                Map<String,String> par = new HashMap<String, String>();
                par.put("MaxLA" , String.valueOf(MaxLA) );
                par.put("MinLA" , String.valueOf(MinLA) );
                par.put("MaxLO" , String.valueOf(MaxLO) );
                par.put("MinLO", String.valueOf(MinLO) );
                return par;
            }
        };
        Q.add(request);
    }

    void getInCharges() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getInChargesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {

                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        ClientVisitReport.Responsible.clear();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            RESPONSIBLE_CLASS r = new RESPONSIBLE_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("Name"),row.getString("JobTitle"),row.getString("MobileNumber"),row.getString("Email"),row.getString("Link"));
                            ClientVisitReport.Responsible.add(r);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Responsible.size() > 0 ) {
                        String[] xx = new String[Responsible.size()];
                        for (int i=0;i<Responsible.size();i++) {
                            xx[i] = Responsible.get(i).Name ;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,xx);
                        responsiblesSpinner.setAdapter(adapter);
                    }
                    THE_CLIENT.setResponsibles(Responsible);
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
                par.put("ClientID" , String.valueOf(ClientVisitReport.THE_CLIENT.id));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void attachPdfFile(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        act.startActivityForResult(Intent.createChooser(intent, "select File"), AttachFileCode);
    }

    private void uploadPDF(final String pdfname, Uri pdffile , int ID) {

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo", new String(response.data));
                            Q.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                jsonObject.toString().replace("\\\\", "");
                                recordFileLinkInTable(jsonObject.getString("message"),"ClientVisitReport","FileLink",ID);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("image", new DataPart(pdfname, inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Q = Volley.newRequestQueue(act);
            Q.add(volleyMultipartRequest);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void recordFileLinkInTable(String Link , String Table , String Column , int ID) {

        StringRequest request = new StringRequest(Request.Method.POST, recordFileLinkInTableUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("insertIntoTable" , response);
                if (response.equals("1")) {
                    ToastMaker.Show(0,"file saved" ,act);
                }
                else if (response.equals("0")) {
                    ToastMaker.Show(0,"file not saved" ,act);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("insertIntoTable" , error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Table",Table);
                par.put("ID",String.valueOf(ID));
                par.put("Link",Link);
                par.put("Field",Column);
                return par;
            }
        };
        Q.add(request);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void openClientSearchDialog(View view) {
        D.setContentView(R.layout.sales_add_new_location_to_client_dialog);
        Window w = D.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        Spinner searchBySpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchBySpinner);
        Spinner ClientsResultSpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchResultSpinner);
        EditText searchField = (EditText) D.findViewById(R.id.MyVisitsReports_searchWord);
        EditText LocationName = (EditText) D.findViewById(R.id.MyVisitsReports_locationName);
        ProgressBar p = (ProgressBar) D.findViewById(R.id.progressBar3);
        p.setVisibility(View.GONE);
        String[] searchByArray = getResources().getStringArray(R.array.searchByArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,searchByArray);
        searchBySpinner.setAdapter(adapter);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p.setVisibility(View.VISIBLE);
                searchClient(searchBySpinner.getSelectedItemPosition(),searchField.getText().toString());
            }
        });
        Button cancel = (Button) D.findViewById(R.id.oldClientsDialog_cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
            }
        });
        Button select = (Button) D.findViewById(R.id.oldClientsDialog_selectBtn);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClientsResultSpinner.getSelectedItem() == null ) {
                    ToastMaker.Show(0,"select client first",act);
                    return;
                }
                if (LocationName.getText() == null || LocationName.getText().toString().isEmpty()) {
                    ToastMaker.Show(0,"please enter location name",act);
                    return;
                }
                THE_CLIENT = THE_Result_CLIENTS.get(ClientsResultSpinner.getSelectedItemPosition());
                boolean y = false ;
                for (CLIENT_CLASS u : THE_Result_CLIENTSByLocation) {
                    if (u.id == THE_CLIENT.id) {
                        y = true ;
                    }
                }
                if (y) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Check ",getResources().getString(R.string.clientIsInMapList));
                }
                else {
                    Loading l = new Loading(act);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, insertNewClientLocationUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            l.close();
                            if (Integer.parseInt(response) > 0 ) {
                                ClientVisitReport.ClientNameTextView.setText(ClientVisitReport.THE_CLIENT.ClientName);
                                LatLng r = new LatLng(THE_LOCATION.getLatitude(),THE_LOCATION.getLongitude());
                                markers.add(mMap.addMarker(new MarkerOptions().position(r).title(THE_CLIENT.id+" "+THE_CLIENT.ClientName).icon(BitmapDescriptorFactory.fromResource(R.drawable.green_location))));
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (Marker marker : markers) {
                                    marker.showInfoWindow();
                                    builder.include(marker.getPosition());
                                }
                                LatLngBounds bounds = builder.build();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,10));
                                getInCharges();
                                D.dismiss();
                            }
                            else if(response.equals("0")) {
                                ToastMaker.Show(1,"error try again ",act);
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
                            par.put("ClientID" , String.valueOf( THE_CLIENT.id ));
                            par.put("ClientName" , THE_CLIENT.ClientName) ;
                            par.put("LocationName" , LocationName.getText().toString());
                            par.put("LA" , String.valueOf(THE_LOCATION.getLatitude()));
                            par.put("LO" , String.valueOf(THE_LOCATION.getLongitude()));
                            return par;
                        }
                    };
                    Q.add(request);
                }
            }
        });
        D.show();
    }

    void searchClient (int searchBy , String field) {
        ProgressBar P = (ProgressBar) D.findViewById(R.id.progressBar3);
        Spinner ClientsResultSpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchResultSpinner);

        if (field.isEmpty()) {
            String[] g = new String[]{""} ;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,g);
            ClientsResultSpinner.setAdapter(adapter);
            P.setVisibility(View.GONE);
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST,searchClientUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("searchResp" , response);
                P.setVisibility(View.GONE);
                if (response.equals("0")) {
                    ToastMaker.Show(1,"no results",act);
                    String[] yy = new String[]{""};
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,yy);
                    ClientsResultSpinner.setAdapter(adapter);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"error",act);
                }
                else {
                    try {
                        THE_Result_CLIENTS.clear();
                        JSONArray arr = new JSONArray(response);
                        String[] resultArray = new String[arr.length()];
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CLIENT_CLASS c = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                            THE_Result_CLIENTS.add(c);
                            resultArray[i] = c.ClientName ;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resultArray);
                        ClientsResultSpinner.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("searchResp" , e.getMessage());
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                P.setVisibility(View.GONE);
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
                par.put("searchBy" , String.valueOf( searchBy ));
                par.put("Field" , field) ;
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }
}