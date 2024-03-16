package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.Visits_Adapter;
import com.syrsoft.ratcoms.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ViewMyVisitDetailes extends AppCompatActivity {

    Activity act ;
    int ID ;
    CLIENT_CLASS CLIENT ;
    RESPONSIBLE_CLASS RESPONSIBLE ;
    CLIENT_VISIT_CLASS VISIT ;
    TextView ClientName , City , Phone , Address , Field , ResponsibleName , JobTitle , MobileNumber , Email ;
    String getClientUrl = MyApp.MainUrl+"getClient.php";
    String getResponsibletUrl = MyApp.MainUrl+"getInCharges.php";
    public static RequestQueue Q ;
    Button showCard ;
    String getVisitURL = MyApp.MainUrl+"getClientVisitByVisitID.php";
    String getVisitsByClientUrl = MyApp.MainUrl+"getClientVisitsByClientID.php";
    List<CLIENT_VISIT_CLASS> VISITS ;
    RecyclerView VisitsRecycler ;
    RecyclerView.LayoutManager Manager ;
    int ATTACHFILE_REQCODE_Quotation = 1 ;
    int CAM_REQCODE_QUOTATION = 2 ;
    int CAM_PERMISSION_REQCODE_QUOTATION = 3 ;
    int CAM_REQCODE_LOCATION = 4 ;
    int ATTACHFILE_REQCODE_Location = 5 ;
    int CAM_PERMISSION_REQCODE_LOCATION = 6 ;
    public static Bitmap ClientLocation , QuotationBitmap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_my_visit_detailes_activity);
        Bundle b = getIntent().getExtras() ;
        ID = b.getInt("ItemId");
        //VISIT = MyVisitReports.Visits.get(ID);
        Log.d("checkID" , ID + "");
        setActivity();
        getClient();
        getTheVisits();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Random r = new Random() ;
        int x = r.nextInt(10000);
        if (requestCode == CAM_REQCODE_LOCATION) {
            if (resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ClientLocation = imageBitmap ;
                //ImageView image = (ImageView) findViewById(R.id.locationImage);
                //image.setImageBitmap(ClientLocation);
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
                    //ImageView image = (ImageView) findViewById(R.id.locationImage);
                    //image.setImageBitmap(ClientLocation);
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
                //ImageView image = (ImageView) findViewById(R.id.quotationImage);
                //image.setImageBitmap(QuotationBitmap);
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
                   //ImageView image = (ImageView) findViewById(R.id.quotationImage);
                    //image.setImageBitmap(QuotationBitmap);
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
    }

    void setActivity () {
        act = this ;
        Q = Volley.newRequestQueue(act);
        ClientName = (TextView)findViewById(R.id.MyVisit_clientNameTextView);
        City = (TextView) findViewById(R.id.MyVisit_cityTextView);
        Phone = (TextView) findViewById(R.id.MyVisit_phoneTextView);
        Address = (TextView) findViewById(R.id.MyVisit_addressTextView);
        Field = (TextView) findViewById(R.id.MyVisit_fieldTextView);
        ResponsibleName = (TextView) findViewById(R.id.MyVisit_responsibleTextView);
        JobTitle = (TextView) findViewById(R.id.MyVisit_responsibleJobTitleTextView);
        MobileNumber = (TextView) findViewById(R.id.MyVisit_responsibleMobileTextView);
        Email = (TextView) findViewById(R.id.MyVisit_responsibleEmaileTextView);
        showCard = (Button) findViewById(R.id.button18);
        VISITS = new ArrayList<CLIENT_VISIT_CLASS>();
        showCard.setVisibility(View.GONE);
        showCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.view_zoomable_image);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                PhotoView image = (PhotoView)D.findViewById(R.id.photo_view);
                ImageButton x = (ImageButton) D.findViewById(R.id.close);
                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Picasso.get().load(RESPONSIBLE.Link).into(image);
                D.show();
            }
        });
        VisitsRecycler = (RecyclerView) findViewById(R.id.visitsRecycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        VisitsRecycler.setLayoutManager(Manager);
        VisitsRecycler.setNestedScrollingEnabled(false);
    }

    void getClient() {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("clientResponse" ,response+" "+ID);
                l.close();
                if (response.equals("0")) {

                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        CLIENT = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                        City.setText(CLIENT.City);
                        Phone.setText(CLIENT.PhonNumber);
                        Field.setText(CLIENT.FieldOfWork);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getResponsible();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("clientResponse" ,error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String> par = new HashMap<String, String>();
                par.put("ID", String.valueOf(ID) );
                return par;
            }
        };
        Q.add(request);
    }

    void getResponsible() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getResponsibletUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("clientResponse" ,response);
                l.close();
                if (response.equals("0")) {

                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        RESPONSIBLE = new RESPONSIBLE_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("Name"),row.getString("JobTitle"),row.getString("MobileNumber"),row.getString("Email"),row.getString("Link"));
                        JobTitle.setText(RESPONSIBLE.JobTitle);
                        MobileNumber.setText(RESPONSIBLE.MobileNumber);
                        Email.setText(RESPONSIBLE.Email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("clientResponse" ,e.getMessage());
                    }
                    if (RESPONSIBLE.Link != null && !RESPONSIBLE.Link.isEmpty()) {
                        showCard.setVisibility(View.VISIBLE);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("clientResponse" ,error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String> par = new HashMap<String, String>();
                par.put("ClientID",String.valueOf(ID));
                return par;
            }
        };
        Q.add(request);
    }

    void getTheVisits() {
        Loading l = new Loading(act) ;
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getVisitsByClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("searchResponse" , response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" ,act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,getResources().getString(R.string.orderNotSent) ,act);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        VISITS.clear();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            VISITS.add(new CLIENT_VISIT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ClientName"),row.getInt("SalesMan"),row.getString("Date"),row.getString("Time"),row.getString("ProjectDescription"),row.getString("VisitDetails"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Address"),row.getString("Responsible"),row.getString("QuotationLink"),row.getString("LocationLink"),row.getString("FileLink"),row.getInt("Interested"),row.getString("FollowUpAt")));
                        }
                        VISIT = VISITS.get(0);
                        ClientName.setText(VISIT.ClientName);
                        Address.setText(VISIT.Address);
                        ResponsibleName.setText(VISIT.Responsible);
                        Visits_Adapter adapter = new Visits_Adapter(VISITS);
                        VisitsRecycler.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("myReportsResp" , e.getMessage());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("searchResponse" , error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("ID" , String.valueOf(ID)) ;
                return par;
            }
        };
        Q.add(request);
    }
}