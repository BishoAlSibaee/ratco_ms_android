package com.syrsoft.ratcoms.PROJECTSActivity;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.CountersService;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.ClientVisitReport;
import com.syrsoft.ratcoms.SALESActivities.ShowVisitsOnMap;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VolleyMultipartRequest;
import com.syrsoft.ratcoms.VollyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class SiteVisitOrder_ForProjects extends AppCompatActivity {

    Activity act ;
    RequestQueue Q ;
    int ID ;
    String getSiteVisitOrderUrl = MyApp.MainUrl+"getSiteVisitOrderByID.php";
    List<SITE_VISIT_ORDER_class> list ;
    TextView  ForwardedTo,date , projectName , responsibleName , responsibleMobile ,reason, notes , visitDate , visitTime  ;
    EditText visitResult , visitNotes ;
    SITE_VISIT_ORDER_class VISIT ;
    String saveSitevisitResponseUrl = MyApp.MainUrl+"responseSiteVisitOrder.php" ;
    List<USER> ToList ;
    USER OrderSender ;
    LinearLayout PicsLayout ;
    List<Bitmap> Pics ;
    int CAM_REQCODE=10,ATTACHFILE_REQCODE=11 ;
    String saveImageUrl = MyApp.MainUrl+"insertPhotoToFolderAndTable.php" ;
    String insertLinkToTable = MyApp.MainUrl+"insertLinkToTable.php" ;
    String setForwardToUrl = MyApp.MainUrl+"setSiteVisitOrderForwardedTo.php";
    boolean isManager = false ;
    List<USER> MyEmpsList ;
    LinearLayout ResponseLayout , ForwardLayout ;
    USER SelectedEmp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_site_visit_order__for_projects_activity);
        ID = getIntent().getExtras().getInt("ID");
        setActivity();
        setLayoutView();
        getOrder();
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
                    Pics.add(bmp);
                    View v = LayoutInflater.from(act).inflate(R.layout.images_to_save_unit,null,false);
                    ImageView image = (ImageView) v.findViewById(R.id.imagesToSave_image);
                    image.setImageBitmap(bmp);
                    PicsLayout.addView(v);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //Log.d("path" , "error result "+data.getData().getPath());
            }

        }
        else if (requestCode == CAM_REQCODE)
        {
            //x = r.nextInt(10000);
            if (resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Pics.add(imageBitmap);
                View v = LayoutInflater.from(act).inflate(R.layout.images_to_save_unit,null,false);
                ImageView image = (ImageView) v.findViewById(R.id.imagesToSave_image);
                image.setImageBitmap(imageBitmap);
                PicsLayout.addView(v);
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }
        }

    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        list = new ArrayList<SITE_VISIT_ORDER_class>();
        date = (TextView) findViewById(R.id.SiteVisitOrder_date);
        projectName = (TextView) findViewById(R.id.SiteVisitOrder_pname);
        responsibleName = (TextView) findViewById(R.id.SiteVisitOrder_Responsible);
        responsibleMobile = (TextView) findViewById(R.id.SiteVisitOrder_ResponsibleMobile);
        notes = (TextView) findViewById(R.id.SiteVisitOrder_notes);
        reason = (TextView) findViewById(R.id.SiteVisitOrder_reason);
        visitDate = (TextView) findViewById(R.id.VisitDateTV);
        visitTime = (TextView) findViewById(R.id.VisitTimeTV);
        visitResult = (EditText) findViewById(R.id.SiteVisitOrder_visitResult);
        visitNotes = (EditText) findViewById(R.id.SiteVisitOrder_visitNotes );
        ForwardedTo = findViewById(R.id.textView155);
        ToList = new ArrayList<USER>();
        MyEmpsList = new ArrayList<USER>();
        PicsLayout = (LinearLayout) findViewById(R.id.Pics_Layout);
        ResponseLayout = (LinearLayout) findViewById(R.id.responseLayout);
        ResponseLayout.setVisibility(View.GONE);
        ForwardLayout = (LinearLayout) findViewById(R.id.forwardToBtnsLayout);
        ForwardLayout.setVisibility(View.GONE);
        Pics = new ArrayList<Bitmap>();
        for (USER u : MyApp.EMPS) {
            if (u.DirectManager == MyApp.MyUser.JobNumber) {
                MyEmpsList.add(u);
            }
        }
        if (MyEmpsList.size() > 0) {
            isManager = true ;
        }
    }

    void setLayoutView () {
        if (isManager) {
            ForwardLayout.setVisibility(View.VISIBLE);
            ResponseLayout.setVisibility(View.GONE);
        }
        else {
            ForwardLayout.setVisibility(View.GONE);
            ResponseLayout.setVisibility(View.VISIBLE);
        }
    }

    void getOrder() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getSiteVisitOrderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject row = arr.getJSONObject(0);
                    VISIT = new SITE_VISIT_ORDER_class(row.getInt("id"),row.getInt("SalesMan"),row.getInt("ForwardedTo"),row.getString("VisitReason"),row.getString("ProjectName"),row.getString("ResponsibleName"),row.getString("ResponsibleMobile"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Notes"),row.getString("Date"),row.getString("VisitDate"),row.getString("VisitTime"),row.getString("VisitResult"),row.getString("VisitNotes"),row.getString("DoneDate"),row.getString("DoneTime"),row.getInt("Status"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (VISIT != null ) {
                    USER f = USER.searchUserByJobNumber(MyApp.EMPS,VISIT.ForwardedTo);
                    if (f != null) {
                        ForwardedTo.setText(f.FirstName+" "+f.LastName);
                    }
                    date.setText(VISIT.Date);
                    projectName.setText(VISIT.ProjectName);
                    responsibleName.setText(VISIT.ResponsibleName);
                    responsibleMobile.setText(VISIT.ResponsibleMobile);
                    notes.setText(VISIT.Notes);
                    reason.setText(VISIT.VisitReason);
                    visitDate.setText(VISIT.VisitDate);
                    visitTime.setText(VISIT.VisitTime);
                    for (USER u :MyApp.EMPS) {
                        if (u.JobNumber == VISIT.SalesMan) {
                            ToList.add(u);
                            OrderSender = u ;
                            break;
                        }
                    }
                    if (OrderSender != null) {
                        for (USER u : MyApp.EMPS) {
                            if (u.JobNumber == OrderSender.DirectManager) {
                                ToList.add(u);
                                break;
                            }
                        }
                    }
                    ToList.add(MyApp.DIRECT_MANAGER);
                }
                else {}
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
                par.put("ID" , String.valueOf(ID));
                return par;
            }
        };
        Q.add(request);
    }

    public void showLocationOnMap(View view) {
        Intent i = new Intent(act, ShowVisitsOnMap.class);
        i.putExtra("LA",VISIT.LA);
        i.putExtra("LO" , VISIT.LO);
        i.putExtra("ClientName" , VISIT.ProjectName);
        startActivity(i);
    }

    public void saveSiteVisitOrder(View view) {

        if (visitResult.getText() == null || visitResult.getText().toString().isEmpty()) {
            ToastMaker.Show(1,"please enter Visit Result",act);
            return;
        }
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, saveSitevisitResponseUrl,
                response -> {
                    l.close();
                    if (response.equals("0")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error","error saving .. try later");
                    }
                    else if (response.equals("-1")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error","error no data");
                    }
                    else if (response.equals("1")) {
                        new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved),0);
                        MyApp.MyUser.getNumberOfSiteVisitOrders(new VollyCallback() {
                            @Override
                            public void onSuccess(String s) {

                            }

                            @Override
                            public void onFailed(String error) {

                            }
                        });
                        MyApp.sendNotificationsToGroup(ToList, "updates on " + getResources().getString(R.string.siteVisitOrder), "updates on " + getResources().getString(R.string.siteVisitOrder) + " " + VISIT.ProjectName, OrderSender.FirstName + " " + OrderSender.LastName, OrderSender.JobNumber, "SiteVisitOrderUpdates", act, new VolleyCallback() {
                            @Override
                            public void onSuccess() {

                            }
                        });
                        for (Bitmap b : Pics) {
                            savePhoto(b,"SiteVisitOrderLinks",VISIT.id,"OrderID");
                        }
                    }
                },
                error -> {
                            l.close();
                            new MESSAGE_DIALOG(act,"error",error.toString());
                        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                String Time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
                Map<String,String> par = new HashMap<String, String>();
                par.put("ID" , String.valueOf(ID));
                par.put("VisitResult" ,visitResult.getText().toString() );
                if (visitNotes.getText() != null && !visitNotes.getText().toString().isEmpty() ) {
                    par.put("VisitNotes" , visitNotes.getText().toString());
                }
                par.put("VisitDate" , Date);
                par.put("VisitTime" , Time);
                return par;
            }
        };
        Q.add(request);
    }

    public void attachFile(View view) {

            AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
            selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
            selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
            selectDialog.setNegativeButton(getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    {
                        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA}, ClientVisitReport.CAM_PERMISSION_REQCODE);
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

    public  void savePhoto ( Bitmap bitmap , String Table , int ID ,String Field) {

        Log.d("saveImageResponse" , "save image started");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, saveImageUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("saveImageResponse" , new String(response.data)+"response");
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getString("status").equals("1")) {
                                //Toast.makeText(act, "Image Saved", Toast.LENGTH_SHORT).show();
                                String Link = "https://ratco-solutions.com/RatcoManagementSystem/images/"+obj.getString("file_name");
                                Log.d("saveImageResponse" , Link);
                                insertLinkInTable(Table, ID, Link, Field, new VolleyCallback() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(act, "Image Saved", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("saveImageResponse" , e.getMessage()+"response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(act, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("saveImageResponse" , error.getMessage()+"error");
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(act).add(volleyMultipartRequest);
    }

    public void insertLinkInTable(String Table , int ID , String Link , String Field, VolleyCallback callback) {

        StringRequest request = new StringRequest(Request.Method.POST, insertLinkToTable, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saveImageResponse" , response);
                if (response.equals("1")) {
                    callback.onSuccess();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("saveImageResponse" , error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Table", Table);
                par.put("ID" , String.valueOf(ID));
                par.put("Link" , Link );
                par.put("Field" , Field);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void forwardTo(View view) {
        Dialog D = new Dialog(act);
        D.setContentView(R.layout.dialog_select_employee_dialog);
        Window x = D.getWindow();
        x.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Spinner empsSpinner = (Spinner) D.findViewById(R.id.empSpinner);
        Button cancel = (Button) D.findViewById(R.id.cancelBtn);
        Button select = (Button) D.findViewById(R.id.selectBtn);
        EditText notes = (EditText) D.findViewById(R.id.note);
        notes.setVisibility(View.GONE);
        if (MyEmpsList.size() > 0 ) {
            String[] arr = new String[MyEmpsList.size()];
            for (int i=0;i<MyEmpsList.size();i++) {
                arr[i] = MyEmpsList.get(i).FirstName +" " + MyEmpsList.get(i).LastName ;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,arr);
            empsSpinner.setAdapter(adapter);
        }
        else {
            ToastMaker.Show(1,"no employees in your department",act);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedEmp = MyEmpsList.get(empsSpinner.getSelectedItemPosition());
//                if (notes.getText().toString() == null || notes.getText().toString().isEmpty() ) {
//                    ToastMaker.Show(0,"please enter note" , act);
//                    return;
//                }
                Loading l = new Loading(act);
                l.show();
                StringRequest request = new StringRequest(Request.Method.POST, setForwardToUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        l.close();
                        if (response.equals("1")) {
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved),0);
                            //ToastMaker.Show(1,getResources().getString(R.string.saved),act);
                            D.dismiss();
//                            ForwardedLayout.setVisibility(View.VISIBLE);
//                            ForwardBtnsLayout.setVisibility(View.GONE);
//                            ForwardedTo.setText(SelectedEmp.FirstName+" "+SelectedEmp.LastName);
//                            ForwardNotes.setText(notes.getText().toString());
                            MyApp.CloudMessage(getResources().getString(R.string.siteVisitOrder),getResources().getString(R.string.siteVisitOrder)+" "+VISIT.ProjectName,OrderSender.FirstName+" "+OrderSender.LastName,MyApp.db.getUser().JobNumber,SelectedEmp.Token,"SiteVisitOrder",act);
                        }
                        else if (response.equals("0")) {
                            ToastMaker.Show(1,"not saved",act);
                        }
                        else if (response.equals("-1")) {
                            ToastMaker.Show(1,"not sent",act);
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
                        par.put("ID", String.valueOf(VISIT.id) );
                        par.put("JobNumber" , String.valueOf(SelectedEmp.JobNumber));
                        //par.put("Note" , notes.getText().toString());
                        return par;
                    }
                };
                Q.add(request);
            }
        });
        D.show();
    }

    public void doByMyself(View view) {
        ForwardLayout.setVisibility(View.GONE);
        ResponseLayout.setVisibility(View.VISIBLE);
    }
}