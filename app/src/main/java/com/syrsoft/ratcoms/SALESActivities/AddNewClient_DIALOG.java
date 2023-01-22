package com.syrsoft.ratcoms.SALESActivities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class AddNewClient_DIALOG {

    public static Dialog D ;
    Context C ;
    public static TextView name , city , phone , email , address , field  , responsibleName , responsibleJobTitle,responsibleMobile,responsibleEmail , fileName;
    public static ImageView image ;
    RadioButton yes , no ;
    Button save , cancel , attach ;
    static int RequestCode = 65 ;

    String saveNewClientUrl = "https://ratco-solutions.com/RatcoManagementSystem/insertNewClient.php" ;
    Button getContact ;


    AddNewClient_DIALOG(Context C) {
        this.C = C ;
        D = new Dialog(this.C);
        D.setContentView(R.layout.dialog_new_client_dialog);
        D.setCancelable(false);
        Window window = D.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getContact = (Button) D.findViewById(R.id.button26);
        name = (TextView) D.findViewById(R.id.ClientVisitReport_clientNameTextView);
        city = (TextView) D.findViewById(R.id.AddNewProject_ClientName);
        phone = (TextView) D.findViewById(R.id.ClientVisitReport_phoneTextView);
        email = (TextView) D.findViewById(R.id.ClientVisitReport_emailTextView);
        address = (TextView) D.findViewById(R.id.ClientVisitReport_addressTextView);
        field = (TextView) D.findViewById(R.id.ClientVisitReport_fieldTextView);
        responsibleName = (TextView) D.findViewById(R.id.ClientVisitReport_responsibleTextView);
        responsibleJobTitle = (TextView) D.findViewById(R.id.ClientVisitReport_responsibleJobTitleTextView);
        responsibleMobile = (TextView) D.findViewById(R.id.ClientVisitReport_responsibleMobileTextView);
        responsibleEmail = (TextView) D.findViewById(R.id.ClientVisitReport_responsibleEmaileTextView);
        yes = (RadioButton) D.findViewById(R.id.addNewClientDialog_yesradio);
        no = (RadioButton) D.findViewById(R.id.addNewClientDialog_noradio);
        save = (Button) D.findViewById(R.id.ClientVisitReport_saveBtn);
        cancel = (Button) D.findViewById(R.id.ClientVisitReport_cancelBtn);
        attach = (Button) D.findViewById(R.id.AddNewClientDialog_attach);
        fileName = (TextView) D.findViewById(R.id.textView33);
        image = (ImageView) D.findViewById(R.id.attachment_image);
        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    no.setChecked(false);
                    if (ClientVisitReport.THE_LOCATION != null ) {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(C, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(ClientVisitReport.THE_LOCATION.getLatitude(), ClientVisitReport.THE_LOCATION.getLongitude(), 1);
                            address.setText(addresses.get(0).getAddressLine(0));
                            city.setText(addresses.get(0).getLocality());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    if (ActivityCompat.checkSelfPermission(C, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(C, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        ClientVisitReport.LocationLoadingDialog.show();
//                        //ClientVisitReport.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000*2,1,ClientVisitReport.listener);
//                        ClientVisitReport.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*2,1,ClientVisitReport.listener);
//                        Log.d("locationService", "i am started");
//                    }
//                    else {
//                        Log.d("locationService", "no permission");
//                        ActivityCompat.requestPermissions((Activity) C, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},RequestCode);
//                    }
                }
            }
        });
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yes.setChecked(false);
                    AddNewClient_DIALOG.address.setText("");
                    AddNewClient_DIALOG.city.setText("");
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (ClientVisitReport.THE_LOCATION == null ) {
                        ToastMaker.Show(1,C.getResources().getString(R.string.locationIsNull),C);
                    }
                    else if (name.getText() == null || name.getText().toString().isEmpty()) {
                        ToastMaker.Show(1,"please enter name",C);
                    }
                    else if (city.getText() == null || city.getText().toString().isEmpty()) {
                        ToastMaker.Show(1,"please enter city",C);
                    }
                    else if (responsibleName.getText() == null || responsibleName.getText().toString().isEmpty()) {
                        ToastMaker.Show(1,"please enter Responsible Name",C);
                    }
                    else if (responsibleJobTitle.getText() == null || responsibleJobTitle.getText().toString().isEmpty()) {
                        ToastMaker.Show(1,"please enter Responsible JobTitle",C);
                    }
                    else if (responsibleMobile.getText() == null || responsibleMobile.getText().toString().isEmpty()) {
                        ToastMaker.Show(1,"please enter Responsible Email",C);
                    }
                    Loading l = new Loading(C);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, saveNewClientUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            l.close();
                            if (response.equals("0")) {
                                MESSAGE_DIALOG m = new MESSAGE_DIALOG(C,"error",C.getResources().getString(R.string.default_error_msg));
                            }
                            else if (response.equals("-1")) {
                                MESSAGE_DIALOG m = new MESSAGE_DIALOG(C,"no parameters",C.getResources().getString(R.string.default_error_msg));
                            }
                            else {
                                try {
                                    JSONArray arr = new JSONArray(response) ;
                                    JSONObject row = arr.getJSONObject(0);
                                    ClientVisitReport.THE_CLIENT = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //ClientVisitReport.TheNewClientID = Integer.parseInt(response);
                                D.dismiss();
                                //MESSAGE_DIALOG m = new MESSAGE_DIALOG(C,C.getResources().getString(R.string.saved),C.getResources().getString(R.string.saved));
                                ToastMaker.Show(0,"client saved",C);
                                String email = "" ;
                                if (responsibleEmail.getText() != null && !responsibleEmail.getText().toString().isEmpty()) {
                                    email = responsibleEmail.getText().toString() ;
                                }
                                ClientVisitReport.saveInCharge(responsibleName.getText().toString(),responsibleJobTitle.getText().toString(),responsibleMobile.getText().toString(),email,ClientVisitReport.THE_CLIENT.id); // save responsible employee
                                ClientVisitReport.ClientNameTextView.setText(ClientVisitReport.THE_CLIENT.ClientName); // set name textview
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            l.close();
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(C,C.getResources().getString(R.string.notSavedErrorTitle),C.getResources().getString(R.string.notSavedErrorMessage));
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
                            if ( ClientVisitReport.THE_LOCATION != null && yes.isChecked() ) {
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
                    Volley.newRequestQueue(C).add(request);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
            }
        });
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity act = (Activity) C ;
                AlertDialog.Builder selectDialog = new AlertDialog.Builder(C);
                selectDialog.setTitle(C.getResources().getString(R.string.selectFileTitle));
                selectDialog.setMessage(C.getResources().getString(R.string.selectFileMessage));
                selectDialog.setNegativeButton(C.getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ActivityCompat.checkSelfPermission(C, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                        {
                            requestPermissions(act,new String[]{Manifest.permission.CAMERA},ClientVisitReport.CAM_PERMISSION_REQCODE);
                        }
                        else {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                act.startActivityForResult(takePictureIntent, ClientVisitReport.CAM_REQCODE);
                            } catch (ActivityNotFoundException e) {
                                // display error state to the user
                            }

                        }
                    }
                });
                selectDialog.setPositiveButton(C.getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                        open.setType("image/*");
                        act.startActivityForResult(Intent.createChooser(open,"select Image"),ClientVisitReport.ATTACHFILE_REQCODE);
                    }
                });
                selectDialog.create().show();
            }
        });
        getContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity A = (Activity) C ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && A.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    A.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, ClientVisitReport.PERMISSIONS_REQUEST_READ_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    A.startActivityForResult(intent,ClientVisitReport.ContactsReqCode);
                }
            }
        });
    }

    void show() {
        D.show();
    }

    void close() {
        D.dismiss();
    }

}
