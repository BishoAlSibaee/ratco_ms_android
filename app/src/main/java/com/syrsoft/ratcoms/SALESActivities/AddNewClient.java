package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AddNewClient extends AppCompatActivity {

    static Activity act ;
    TextView name,city,phone,email,address,field,responsibleName,responsibleJobTitle,responsibleMobile,responsibleEmail,fileName;
    RadioButton yes,no;
    Button save,attach;
    ImageView image;
    int RequestCode = 5 ;
    Loading getLocationLoading ;
    LocationManager locationManager ;
    LocationListener listener ;
    Location THE_LOCATION ;
    String saveNewClientUrl = "https://ratco-solutions.com/RatcoManagementSystem/insertNewClient.php" ;
    int ATTACHFILE_REQCODE = 81 ;
    int CAM_REQCODE = 82 ;
    static Bitmap CardBitmap , QuotationBitmap , LocationBitmap ;
    public static String saveInChargeUrl = "https://ratco-solutions.com/RatcoManagementSystem/insertInChargeClient.php";
    static List<RESPONSIBLE_CLASS> Responsible ;
    static CLIENT_CLASS THE_CLIENT ;
    static int PhotoSource = 1 ;
    Button getFromContacts ;
    int PERMISSIONS_REQUEST_READ_CONTACTS = 90 ;
    public static int ContactsReqCode = 23 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_add_new_client_activity);
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
    }

    void setActivity(){
        act = this ;
        getLocationLoading = new Loading(act);
        name = (TextView) findViewById(R.id.ClientVisitReport_clientNameTextView);
        city = (TextView) findViewById(R.id.AddNewProject_ClientName);
        phone = (TextView) findViewById(R.id.ClientVisitReport_phoneTextView);
        email = (TextView) findViewById(R.id.ClientVisitReport_emailTextView);
        address = (TextView) findViewById(R.id.ClientVisitReport_addressTextView);
        field = (TextView) findViewById(R.id.ClientVisitReport_fieldTextView);
        responsibleName = (TextView) findViewById(R.id.ClientVisitReport_responsibleTextView);
        responsibleJobTitle = (TextView) findViewById(R.id.ClientVisitReport_responsibleJobTitleTextView);
        responsibleMobile = (TextView) findViewById(R.id.ClientVisitReport_responsibleMobileTextView);
        responsibleEmail = (TextView) findViewById(R.id.ClientVisitReport_responsibleEmaileTextView);
        yes = (RadioButton) findViewById(R.id.addNewClientDialog_yesradio);
        no = (RadioButton) findViewById(R.id.addNewClientDialog_noradio);
        save = (Button) findViewById(R.id.ClientVisitReport_saveBtn);
        attach = (Button) findViewById(R.id.AddNewClientDialog_attach);
        fileName = (TextView) findViewById(R.id.textView33);
        image = (ImageView) findViewById(R.id.attachment_image);
        getFromContacts = (Button) findViewById(R.id.button26);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Responsible = new ArrayList<RESPONSIBLE_CLASS>();
    }

    void setActivityActions() {
        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    no.setChecked(false);
                    if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        getLocationLoading.show();
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000*2,1,listener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*2,1,listener);
                        Log.d("locationService", "i am started");
                    }
                    else {
                        Log.d("locationService", "no permission");
                        ActivityCompat.requestPermissions((Activity) act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},RequestCode);
                    }
                }
            }
        });
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yes.setChecked(false);
                    address.setText("");
                    city.setText("");
                }
            }
        });
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                getLocationLoading.close();
                if (location != null ) {
                    THE_LOCATION = location ;
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(act, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(THE_LOCATION.getLatitude(),THE_LOCATION.getLongitude(), 1);
                        address.setText(addresses.get(0).getAddressLine(0));
                        city.setText(addresses.get(0).getLocality());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    locationManager.removeUpdates(this);
                }
            }
        };
        getFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && act.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    act.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, ClientVisitReport.PERMISSIONS_REQUEST_READ_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    act.startActivityForResult(intent,ClientVisitReport.ContactsReqCode);
                }
            }
        });
    }

    public void saveClient(View view) {
            if (THE_LOCATION == null ) {
                ToastMaker.Show(1,act.getResources().getString(R.string.locationIsNull),act);
                return;
            }
            if (name.getText() == null || name.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"please enter name",act);
                return;
            }
            if (city.getText() == null || city.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"please enter city",act);
                return;
            }
            if (responsibleName.getText() == null || responsibleName.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"please enter Responsible Name",act);
                return;
            }
            if (responsibleJobTitle.getText() == null || responsibleJobTitle.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"please enter Responsible JobTitle",act);
                return;
            }
            if (responsibleMobile.getText() == null || responsibleMobile.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"please enter Responsible Email",act);
                return;
            }
            Loading l = new Loading(act);
            l.show();
            StringRequest request = new StringRequest(Request.Method.POST, saveNewClientUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    l.close();
                    if (response.equals("0")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error",getResources().getString(R.string.notSavedErrorMessage));
                    }
                    else if (response.equals("-1")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"no parameters",getResources().getString(R.string.notSavedErrorMessage));
                    }
                    else {
                        try {
                            JSONArray arr = new JSONArray(response) ;
                            JSONObject row = arr.getJSONObject(0);
                            THE_CLIENT = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ToastMaker.Show(0,"client saved",act);
                        String email = "" ;
                        if (responsibleEmail.getText() != null && !responsibleEmail.getText().toString().isEmpty()) {
                            email = responsibleEmail.getText().toString() ;
                        }
                        saveInCharge(responsibleName.getText().toString(),responsibleJobTitle.getText().toString(),responsibleMobile.getText().toString(),email,THE_CLIENT.id); // save responsible employee
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    l.close();
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.notSavedErrorTitle),getResources().getString(R.string.notSavedErrorMessage));
                    MyApp.sendError(error.getMessage(),AddNewClient_DIALOG.class.getName(),MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName,"save");
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> par = new HashMap<String, String>();
                    par.put("ClientName",name.getText().toString());
                    par.put("City",city.getText().toString());
                    par.put("Address" , address.getText().toString());
                    par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                    if (phone.getText() != null && !phone.getText().toString().isEmpty()) {
                        par.put("PhonNumber",phone.getText().toString());
                    }
                    if (email.getText() != null && !email.getText().toString().isEmpty()) {
                        par.put("Email" , email.getText().toString());
                    }
                    if (ClientVisitReport.THE_LOCATION != null ) {
                        par.put("LA",String.valueOf(ClientVisitReport.THE_LOCATION.getLatitude()));
                        par.put("LO" , String.valueOf(ClientVisitReport.THE_LOCATION.getLongitude()));
                    }
                    if (field.getText() != null && !field.getText().toString().isEmpty()) {
                        par.put("FieldOfWork" , field.getText().toString());
                    }

                    return par;
                }
            };
            request.setShouldRetryConnectionErrors(true);
            Volley.newRequestQueue(act).add(request);
    }

    public void attachImageFile(View view) {
        AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
        selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
        selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
        selectDialog.setNegativeButton(getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},ClientVisitReport.CAM_PERMISSION_REQCODE);
                }
                else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        act.startActivityForResult(takePictureIntent, CAM_REQCODE);
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
                act.startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE);
            }
        });
        selectDialog.create().show();
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
                    image.setImageBitmap(bmp);
                    fileName.setText(x+".jpg");
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
                image.setImageBitmap(imageBitmap);
                fileName.setText(String.valueOf(x)+".jpg");
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }
        }
        else if (requestCode == ContactsReqCode) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contactData = data.getData();
                Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    responsibleName.setText(name);
                    String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                    if (phones.moveToFirst()) {
                        String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        responsibleMobile.setText(number);
                    }
                    Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
                    String email = "" ;
                    if (emails.moveToFirst()){
                        email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        responsibleEmail.setText(email);
                    }
                    // Log.d("selectedContact" , name+" "+number+" "+email);
                    // TODO Whatever you want to do with the selected contact name.
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
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,act.getResources().getString(R.string.notSavedErrorMessage),"");
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        Responsible.add(new RESPONSIBLE_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("Name"),row.getString("JobTitle"),row.getString("MobileNumber"),row.getString("Email"),row.getString("Link")));
                        THE_CLIENT.setResponsibles(Responsible);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,act.getResources().getString(R.string.saved),act.getResources().getString(R.string.saved),0);
                    if (CardBitmap != null) {
                        MyApp.savePhoto(CardBitmap,"InChargeClients",THE_CLIENT.getResponsibles().get(0).id,PhotoSource,"C");
                    }
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

    public static void setCardImageNull() {
        CardBitmap = null ;
    }

    public static void setQuotationImageNull() {
        QuotationBitmap = null ;
    }

    public static void setLocationImageNull() {
        LocationBitmap = null ;
    }
}