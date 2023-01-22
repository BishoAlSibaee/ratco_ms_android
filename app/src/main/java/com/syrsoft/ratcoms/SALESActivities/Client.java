package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class Client extends AppCompatActivity {

    Activity act ;
    int ID ;
    RequestQueue Q ;
    String getClientUrl = MyApp.MainUrl+"getClient.php";
    String getResponsibletUrl = MyApp.MainUrl+"getInCharges.php";
    String editClientUrl = MyApp.MainUrl+"editClient.php";
    String editResponsibleUrl = MyApp.MainUrl+"editResponsible.php";
    String addNewResponsibleUrl = MyApp.MainUrl+"addResponsible.php";
    CLIENT_CLASS CLIENT ;
    TextView ClientName , City , Phone , Field , Address ;
    List<RESPONSIBLE_CLASS> RESPONSIBLES ;
    LinearLayout responsiblesLayout ;
    Button ClientEditBtn , SHowOnMap , viewVisits;
    int CAM_REQCODE=4 , ATTACHFILE_REQCODE=5 ;
    public static Bitmap B ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_client_activity);
        ID = getIntent().getExtras().getInt("ClientID");
        setActivity();
        setActivityActions();
        getClient();
        getResponsible();
    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        ClientName = (TextView) findViewById(R.id.CLientName);
        City = (TextView) findViewById(R.id.MyVisit_cityTextView);
        Phone = (TextView) findViewById(R.id.MyVisit_phoneTextView);
        Field = (TextView) findViewById(R.id.MyVisit_fieldTextView);
        Address = (TextView) findViewById(R.id.MyVisit_addressTextView);
        ClientEditBtn = (Button) findViewById(R.id.editBtn);
        SHowOnMap = (Button) findViewById(R.id.button29);
        viewVisits = (Button) findViewById(R.id.button30);
        RESPONSIBLES = new ArrayList<RESPONSIBLE_CLASS>();
        responsiblesLayout = (LinearLayout) findViewById(R.id.responsiblesLayout);
    }

    void setActivityActions() {
        ClientEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.sales_edit_client_dialog);
                Window w = D.getWindow();
                w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                EditText name , city , phone , field ;
                Button cancel , save ;
                name = (EditText) D.findViewById(R.id.EditClient_clientName);
                name.setText(CLIENT.ClientName);
                city = (EditText) D.findViewById(R.id.EditClient_city);
                city.setText(CLIENT.City);
                phone = (EditText) D.findViewById(R.id.EditClient_mobile);
                phone.setText(CLIENT.PhonNumber);
                field = (EditText) D.findViewById(R.id.EditClient_field);
                field.setText(CLIENT.FieldOfWork);
                cancel = (Button) D.findViewById(R.id.EditClient_cancelBtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                save = (Button) D.findViewById(R.id.EditClient_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Loading l = new Loading(act);
                        l.show();
                        StringRequest request = new StringRequest(Request.Method.POST, editClientUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                l.close();
                                if (response.equals("1")) {
                                    D.dismiss();
                                    ToastMaker.Show(0,getResources().getString(R.string.saved),act);
                                    getClient();
                                }
                                else if (response.equals("0")) {
                                    ToastMaker.Show(0,"error not saved",act);
                                }
                                else if (response.equals("-1")) {
                                    ToastMaker.Show(0,"no parameters",act);
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

                                Map <String,String> par = new HashMap<String, String>();
                                par.put("Name", name.getText().toString() );
                                par.put("City" , city.getText().toString() );
                                par.put("Phone" , phone.getText().toString() );
                                par.put("Field" , field.getText().toString()) ;
                                par.put("ID" , String.valueOf(ID));
                                return par;
                            }
                        };
                        Q.add(request);
                    }
                });
                D.show();
            }
        });
        SHowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, ShowVisitsOnMap.class);
                i.putExtra("LA",CLIENT.LA);
                i.putExtra("LO" , CLIENT.LO);
                i.putExtra("ClientName" , CLIENT.ClientName);
                startActivity(i);
            }
        });
        viewVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, ViewMyVisitDetailes.class);
                i.putExtra("ItemId",CLIENT.id);
                startActivity(i);
            }
        });
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
                        ClientName.setText(CLIENT.ClientName);
                        City.setText(CLIENT.City);
                        Phone.setText(CLIENT.PhonNumber);
                        Field.setText(CLIENT.FieldOfWork);
                        Address.setText(CLIENT.Address);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        RESPONSIBLES.clear();
        responsiblesLayout.removeAllViews();
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
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            RESPONSIBLES.add( new RESPONSIBLE_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("Name"),row.getString("JobTitle"),row.getString("MobileNumber"),row.getString("Email"),row.getString("Link")));
                            View v = LayoutInflater.from(act).inflate(R.layout.sales_responsible_unit_view,null,false);
                            int RID = row.getInt("id") ;
                            TextView Name , JJobtitle , MMobile , EEmail ;
                            Button card = (Button) v.findViewById(R.id.button18);
                            Button edit = (Button) v.findViewById(R.id.button28);
                            Name = (TextView) v.findViewById(R.id.MyVisit_responsibleTextView);
                            JJobtitle = (TextView) v.findViewById(R.id.MyVisit_responsibleJobTitleTextView);
                            MMobile = (TextView) v.findViewById(R.id.MyVisit_responsibleMobileTextView);
                            EEmail = (TextView) v.findViewById(R.id.MyVisit_responsibleEmaileTextView);
                            Name.setText(row.getString("Name"));
                            JJobtitle.setText(row.getString("JobTitle"));
                            MMobile.setText(row.getString("MobileNumber"));
                            EEmail.setText(row.getString("Email"));
                            final String xx = row.getString("Link") ;
                            if (xx == null || xx.isEmpty()) {
                                card.setVisibility(View.GONE);
                            }
                            card.setOnClickListener(new View.OnClickListener() {
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
                                    Picasso.get().load(xx).into(image);
                                    D.show();
                                }
                            });
                            edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Dialog DD = new Dialog(act);
                                    DD.setContentView(R.layout.sales_add_edit_responsible_dialog);
                                    Window w = DD.getWindow();
                                    w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    EditText name , jobtitle,mobile,email;
                                    Button cancel , save , addCard ;
                                    name = (EditText) DD.findViewById(R.id.EditResponsible_name);
                                    name.setText(Name.getText().toString());
                                    jobtitle = (EditText) DD.findViewById(R.id.EditResponsible_JobTitle);
                                    jobtitle.setText(JJobtitle.getText().toString());
                                    mobile = (EditText) DD.findViewById(R.id.EditResponsible_Mobile);
                                    mobile.setText(MMobile.getText().toString());
                                    email = (EditText) DD.findViewById(R.id.EditResponsible_Emaile);
                                    email.setText(EEmail.getText().toString());
                                    addCard = (Button) DD.findViewById(R.id.button31);
                                    addCard.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
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
                                    });
                                    cancel = (Button) DD.findViewById(R.id.button18);
                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DD.dismiss();
                                        }
                                    });
                                    save = (Button) DD.findViewById(R.id.button28);
                                    save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Loading l = new Loading(act);
                                            l.show();
                                            StringRequest requesttt = new StringRequest(Request.Method.POST,editResponsibleUrl , new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    l.close();
                                                    if (response.equals("1")) {
                                                        DD.dismiss();
                                                        ToastMaker.Show(0,getResources().getString(R.string.saved),act);
                                                        if (B != null) {
                                                            MyApp.savePhoto(B,"InChargeClients",RID,2,"C");
                                                        }
                                                        getResponsible();
                                                    }
                                                    else if (response.equals("0")) {
                                                        ToastMaker.Show(0,"error not saved",act);
                                                    }
                                                    else if (response.equals("-1")) {
                                                        ToastMaker.Show(0,"no parameters",act);
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

                                                    Map <String,String> par = new HashMap<String, String>();
                                                    par.put("ID",String.valueOf(RID));
                                                    par.put("Name" , name.getText().toString());
                                                    par.put("JobTitle" , jobtitle.getText().toString());
                                                    par.put("Mobile",mobile.getText().toString());
                                                    par.put("Email",email.getText().toString());
                                                    return par;
                                                }
                                            };
                                            Q.add(requesttt);
                                        }
                                    });
                                    DD.show();
                                }
                            });
                            responsiblesLayout.addView(v);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    if (RESPONSIBLE.Link != null && !RESPONSIBLE.Link.isEmpty()) {
//                        showCard.setVisibility(View.VISIBLE);
//                    }
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

    public void addResponsibleBtn(View view) {
        Dialog DD = new Dialog(act);
        DD.setContentView(R.layout.sales_add_edit_responsible_dialog);
        Window w = DD.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditText name , jobtitle,mobile,email;
        Button cancel , save , addCard ;
        name = (EditText) DD.findViewById(R.id.EditResponsible_name);
        jobtitle = (EditText) DD.findViewById(R.id.EditResponsible_JobTitle);
        mobile = (EditText) DD.findViewById(R.id.EditResponsible_Mobile);
        email = (EditText) DD.findViewById(R.id.EditResponsible_Emaile);
        cancel = (Button) DD.findViewById(R.id.button18);
        addCard = (Button) DD.findViewById(R.id.button31);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DD.dismiss();
            }
        });
        save = (Button) DD.findViewById(R.id.button28);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loading l = new Loading(act);
                l.show();
                StringRequest requesttt = new StringRequest(Request.Method.POST,addNewResponsibleUrl , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        l.close();
                        if (Integer.parseInt(response) > 0) {
                            DD.dismiss();
                            ToastMaker.Show(0,getResources().getString(R.string.saved),act);
                            if (B != null) {
                                MyApp.savePhoto(B,"InChargeClients",Integer.parseInt(response),2,"C");
                            }
                            getResponsible();
                        }
                        else if (response.equals("0")) {
                            ToastMaker.Show(0,"error not saved",act);
                        }
                        else if (response.equals("-1")) {
                            ToastMaker.Show(0,"no parameters",act);
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
                        Map <String,String> par = new HashMap<String, String>();
                        par.put("ClientID",String.valueOf(CLIENT.id));
                        par.put("Name" , name.getText().toString());
                        par.put("JobTitle" , jobtitle.getText().toString());
                        par.put("MobileNumber",mobile.getText().toString());
                        par.put("Email",email.getText().toString());
                        return par;
                    }
                };
                Q.add(requesttt);
            }
        });
        DD.show();
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
                    B = bmp ;
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
                B = imageBitmap ;
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }
        }

    }

    public void shareLocation(View view) {

        String uri = "https://www.google.com/maps/?q=" + CLIENT.LA+ "," +CLIENT.LO ;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,  uri);
        startActivity(Intent.createChooser(sharingIntent, "Share in..."));
    }
}