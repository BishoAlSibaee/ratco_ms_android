package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.MaintenanceOrder_Response_Adapter;
import com.syrsoft.ratcoms.ProgressLoadingDialog;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.CLIENT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.ClientVisitReport;
import com.syrsoft.ratcoms.SALESActivities.PROJECT_CONTRACT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.ShowVisitsOnMap;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VollyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MaintenanceOrder_ForProjects extends AppCompatActivity {

    Activity act ;
    int OrderIndex ;
    MAINTENANCE_ORDER_CLASS ORDER ;
    List<MaintenanceOrderLink> Links ;
    String getClientUrl = MyApp.MainUrl+"getClient.php";
    String getContractUrl = MyApp.MainUrl+"getProjectContractByID.php";
    String getOrderLinksUrl = MyApp.MainUrl+"getMaintenanceOrderLinks.php";
    String setMaintenanceOrderForwardToUrl = MyApp.MainUrl+"setMaintenanceOrderForwardedTo.php";
    String getOrderResponsesUrl = MyApp.MainUrl+"getMaintenanceOrderResponses.php";
    String saveResponseUrl = MyApp.MainUrl +"insertMaintenanceOrderResponse.php" ;
    CLIENT_CLASS CLIENT ;
    PROJECT_CONTRACT_CLASS CONTRACT ;
    RequestQueue Q ;
    Button addResponseBtn , saveResponseBtn ;
    TextView ClientName , ProjectName , OrderStatus , Date , DamageDesc , Notes ,ContactName,ContactNumber,SendTo,ForwardedTo ;
    TextView SupplyText , InstallText , HandoverText , Warranty , WarrantyDate ,ForwardNotes ;
    LinearLayout LinksLayouts , ForwardedLayout , ForwardBtnsLayout , ResponsesLayout , NewResponsesLayout ;
    List<View> IMAGES ;
    USER SelectedEmp ;
    List<MAINTENANCE_ORDER_RESPONSE_class> RESPONSES ;
    RecyclerView ResponsesRecycler ;
    RecyclerView.LayoutManager Manager ;
    View V ;
    List<USER> RespectiveUsers ;
    int CAM_REQCODE = 3 ;
    int ATTACHFILE_REQCODE = 5 ;
    List<Bitmap> Documents ;
    ProgressLoadingDialog loading ;
    int Forward = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_maintenance_order__for_projects_activity);
        setActivity();
        getOrderLinks();
        getProject();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ATTACHFILE_REQCODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    Documents.add(bmp);
                    View v = LayoutInflater.from(act).inflate(R.layout.images_to_save_unit,null,false);
                    ImageView image = (ImageView) v.findViewById(R.id.imagesToSave_image);
                    image.setImageBitmap(bmp);
                    LinearLayout DocumentsLayout = (LinearLayout) V.findViewById(R.id.DocumentLinks);
                    DocumentsLayout.addView(v);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //Log.d("path" , "error result "+data.getData().getPath());
            }

        }
        else if (requestCode == CAM_REQCODE) {
            if (resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Documents.add(imageBitmap);
                View v = LayoutInflater.from(act).inflate(R.layout.images_to_save_unit,null,false);
                ImageView image = (ImageView) v.findViewById(R.id.imagesToSave_image);
                image.setImageBitmap(imageBitmap);
                LinearLayout DocumentsLayout = (LinearLayout) V.findViewById(R.id.DocumentLinks);
                DocumentsLayout.addView(v);
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }
        }
    }

    void setActivity() {
        act = this ;
        Q = Volley.newRequestQueue(act);
        if (getIntent().getExtras() != null) {
            OrderIndex = getIntent().getExtras().getInt("Index");
            Forward = getIntent().getExtras().getInt("UnForwarded");
            if (Forward == 0) {
                ORDER = MaintenanceOrders.ORDERS.get(OrderIndex) ;
            }
            else if (Forward == 1) {
                ORDER = MaintenanceOrders.UnFOrdersList.get(OrderIndex) ;
            }
        }
        else {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"order is missed","this order is missed",0);
        }
        Links = new ArrayList<MaintenanceOrderLink>();
        Documents = new ArrayList<>();
        ClientName = (TextView) findViewById(R.id.MaintenanceOrder_clientName);
        ProjectName = (TextView) findViewById(R.id.MaintenanceOrder_projectName);
        OrderStatus = (TextView) findViewById(R.id.MaintenanceOrder_status);
        Date = (TextView) findViewById(R.id.MaintenanceOrder_date);
        DamageDesc = (TextView) findViewById(R.id.MaintenanceOrder_damageDesc);
        Notes = (TextView) findViewById(R.id.MaintenanceOrder_notes);
        ContactName = (TextView) findViewById(R.id.MaintenanceOrder_contactName);
        ContactNumber = (TextView) findViewById(R.id.MaintenanceOrder_contactNumber);
        SendTo = (TextView) findViewById(R.id.MaintenanceOrder_department);
        ForwardedTo = (TextView) findViewById(R.id.MaintenanceOrder_forwardTo);
        SupplyText = (TextView) findViewById(R.id.supply_text);
        InstallText = (TextView) findViewById(R.id.install_text);
        HandoverText = (TextView) findViewById(R.id.handover_text);
        Warranty = (TextView) findViewById(R.id.warranty);
        WarrantyDate = (TextView) findViewById(R.id.warrantyDate);
        ForwardNotes = (TextView) findViewById(R.id.MaintenanceOrder_forwardNotes);
        LinksLayouts = (LinearLayout) findViewById(R.id.LinksLayouts);
        ForwardedLayout = (LinearLayout) findViewById(R.id.ForwardLayout);
        ForwardBtnsLayout = (LinearLayout) findViewById(R.id.forwardToBtnsLayout);
        ResponsesLayout = (LinearLayout) findViewById(R.id.responsesLayout);
        NewResponsesLayout = (LinearLayout) findViewById(R.id.newResponsesLayout);
        ResponsesRecycler = (RecyclerView) findViewById(R.id.responsesRecycler);
        addResponseBtn = (Button) findViewById(R.id.addResponseBtn);
        saveResponseBtn = (Button) findViewById(R.id.saveResponse);
        saveResponseBtn.setVisibility(View.GONE);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        ResponsesRecycler.setLayoutManager(Manager);
        ForwardBtnsLayout.setVisibility(View.GONE);
        ForwardedLayout.setVisibility(View.GONE);
        IMAGES = new ArrayList<View>();
        RESPONSES = new ArrayList<MAINTENANCE_ORDER_RESPONSE_class>();
        ProjectName.setText(ORDER.ProjectName);
        Date.setText(ORDER.Date);
        DamageDesc.setText(ORDER.DamageDesc);
        Notes.setText(ORDER.Notes);
        ContactName.setText(ORDER.ContactName);
        ContactNumber.setText(ORDER.Contact);
        OrderStatus.setText(ORDER.getStatus());
        SendTo.setText(ORDER.getDepartment());
        Log.d("forwardedTo" ,ORDER.ForwardedTo+" "+MyApp.EMPS.size() );
        setForwardLayoutsView();
        setResponsesLayout();
        RespectiveUsers = new ArrayList<USER>();
        setRespectiveEmps();
    }

    void getProject() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getContractUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("projectResp" ,response);
                l.close();
                if (response.equals("0")) {
                    Log.d("projectResp" ,"0");
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        CONTRACT = new PROJECT_CONTRACT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ProjectName"),row.getString("Date"),row.getString("City"),row.getString("Address"),row.getDouble("LA"),row.getDouble("LO"),row.getString("ProjectDescription"),row.getString("ProjectManager"),row.getString("MobileNumber"),row.getInt("SalesMan"),row.getString("HandOverDate"),row.getString("WarrantyExpireDate"),row.getString("ContractLink"),row.getInt("Supplied"),row.getInt("Installed"),row.getInt("Handovered"),row.getString("SupplyDate"),row.getString("InstallDate"),row.getString("HandOverDate"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("projectResp" ,e.getMessage());
                    }
                    //CONTRACT.setCLIENT(CLIENT);
                    SupplyText.setText(CONTRACT.getSupplied());
                    InstallText.setText(CONTRACT.getInstalled());
                    HandoverText.setText(CONTRACT.getHandovered());
                    Warranty.setText(CONTRACT.getWarranty());
                    WarrantyDate.setText(CONTRACT.WarrantyExpireDate);
                    getClient();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                //Log.d("xxxxxx" ,error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String> par = new HashMap<String, String>();
                par.put("ID", String.valueOf(ORDER.ProjectID) );
                return par;
            }
        };
        Q.add(request);
    }

    void getClient() {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("clientResp" ,response);
                l.close();
                if (response.equals("0")) {
                    Log.d("clientResp" ,"0");
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        CLIENT = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("clientResp" ,e.getMessage());
                    }
                    CONTRACT.setCLIENT(CLIENT);
                    ClientName.setText(CLIENT.ClientName);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                //Log.d("xxxxxx" ,error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String> par = new HashMap<String, String>();
                par.put("ID", String.valueOf(ORDER.ClientID) );
                return par;
            }
        };
        Q.add(request);
    }

    void getOrderLinks() {
        Loading l = new Loading(act);
        l.show();
        Links.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getOrderLinksUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("linksResp" ,response);
                l.close();
                if (response.equals("0")) {
                    //Log.d("linksResp" ,"0");
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            Links.add(new MaintenanceOrderLink(row.getInt("id"), row.getInt("MaintenanceID"), row.getString("Link")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("linksResp" ,e.getMessage());
                    }
                    //Log.d("linksResp" ,"Links are "+Links.size()+" ");
                    ORDER.setLinks(Links);
                    if (Links.size()>0) {
                        for (int i=0;i<Links.size();i++) {
                            View v = LayoutInflater.from(act).inflate(R.layout.image, null);
                            IMAGES.add(v);
                            LinksLayouts.addView(v);
                            int finalI = i;
                            v.setOnClickListener(new View.OnClickListener() {
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
                                    Picasso.get().load(Links.get(finalI).Link).into(image);
                                    D.show();
                                }
                            });
                            ImageView image = (ImageView) v;
                            Picasso.get().load(Links.get(i).Link).into(image);
                        }
                    }
                }

            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                //Log.d("xxxxxx" ,error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String> par = new HashMap<String, String>();
                par.put("ID", String.valueOf(ORDER.id) );
                return par;
            }
        };
        Q.add(request);
    }

    void setForwardLayoutsView() {
        if (ORDER.ForwardedTo == 0 ) {
            ForwardedLayout.setVisibility(View.GONE);
            ForwardBtnsLayout.setVisibility(View.VISIBLE);
        }
        else {
            ForwardBtnsLayout.setVisibility(View.GONE);
            ForwardedLayout.setVisibility(View.VISIBLE);
            boolean found = false ;
            for(USER u : MyApp.EMPS) {
                if (u.JobNumber == ORDER.ForwardedTo) {
                    ForwardedTo.setText(u.FirstName+" "+u.LastName);
                    found = true ;
                    break;
                }
            }
            if (!found) {
                if (ORDER.ForwardedTo == MyApp.db.getUser().JobNumber) {
                    ForwardedTo.setText(MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName);
                }
            }
        }
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
        List<USER> list = new ArrayList<USER>();
        for(USER u : MyApp.EMPS) {
            if (u.Department.equals(MyApp.db.getUser().Department) && u.JobNumber != MyApp.db.getUser().JobNumber) {
                list.add(u);
            }
        }
        if (list.size() > 0 ) {
            String[] arr = new String[list.size()];
            for (int i=0;i<list.size();i++) {
                arr[i] = list.get(i).FirstName +" " + list.get(i).LastName ;
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
                SelectedEmp = list.get(empsSpinner.getSelectedItemPosition());
                if (notes.getText().toString() == null || notes.getText().toString().isEmpty() ) {
                    ToastMaker.Show(0,"please enter note" , act);
                    return;
                }
                Loading l = new Loading(act);
                l.show();
                StringRequest request = new StringRequest(Request.Method.POST, setMaintenanceOrderForwardToUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        l.close();
                        if (response.equals("1")) {
                            ToastMaker.Show(1,getResources().getString(R.string.saved),act);
                            D.dismiss();
                            ForwardedLayout.setVisibility(View.VISIBLE);
                            ForwardBtnsLayout.setVisibility(View.GONE);
                            ForwardedTo.setText(SelectedEmp.FirstName+" "+SelectedEmp.LastName);
                            ForwardNotes.setText(notes.getText().toString());
                            MyApp.CloudMessage(getResources().getString(R.string.maintenanceOrder),getResources().getString(R.string.maintenanceOrder)+" "+CONTRACT.ProjectName,MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName,MyApp.db.getUser().JobNumber,SelectedEmp.Token,"MaintenanceOrder",act);
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
                        par.put("ID", String.valueOf(ORDER.id) );
                        par.put("JobNumber" , String.valueOf(SelectedEmp.JobNumber));
                        par.put("Note" , notes.getText().toString());
                        return par;
                    }
                };
                Q.add(request);
            }
        });
        D.show();
    }

    public void doByMyself(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act)
                .setTitle(getResources().getString(R.string.areYouSure))
                .setMessage(getResources().getString(R.string.areYouSure))
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Loading l = new Loading(act);
                        l.show();
                        StringRequest request = new StringRequest(Request.Method.POST, setMaintenanceOrderForwardToUrl , new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                l.close();
                                if (response.equals("1")) {
                                    ToastMaker.Show(1,getResources().getString(R.string.saved),act);
                                    dialog.dismiss();
                                    ForwardedLayout.setVisibility(View.VISIBLE);
                                    ForwardBtnsLayout.setVisibility(View.GONE);
                                    ForwardedTo.setText(MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName);
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
                                par.put("ID", String.valueOf(ORDER.id) );
                                par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                                return par;
                            }
                        };
                        Q.add(request);
                    }
                });
        builder.create();
        builder.show();

    }

    void setResponsesLayout () {
        if (ORDER.ForwardedTo == 0 ) {
            ResponsesLayout.setVisibility(View.GONE);
        }
        else {
            ResponsesLayout.setVisibility(View.VISIBLE);
            getOrderResponses();
        }
        if (ORDER.Status == 1) {
            addResponseBtn.setVisibility(View.GONE);
        }
    }

    void getOrderResponses() {
        RESPONSES.clear();
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getOrderResponsesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Responses" ,act);
                }
                else if (response.equals("-1")) {

                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            RESPONSES.add(new MAINTENANCE_ORDER_RESPONSE_class(row.getInt("id"),row.getInt("MaintenanceID"),row.getInt("ProjectID"),row.getInt("ClientID"),row.getString("Response"),row.getInt("Employee"),row.getInt("SparePartsStatus"),row.getString("SpareParts"),row.getInt("OrderStatus"),row.getString("Date"),row.getString("Time"),row.getString("DocumentLink1"),row.getString("DocumentLink2"),row.getString("DocumentLink3")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MaintenanceOrder_Response_Adapter adapter = new MaintenanceOrder_Response_Adapter(RESPONSES);
                    ResponsesRecycler.setAdapter(adapter);
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
                par.put("ID", String.valueOf(ORDER.id) );
                par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Q.add(request);
    }

    public void addResponseView(View view) {
        V = LayoutInflater.from(act).inflate(R.layout.projects_maintenance_order_new_response_unit,null);
        int[] S = {0} ;
        EditText response = (EditText) V.findViewById(R.id.response);
        CheckBox spareCheck = (CheckBox) V.findViewById(R.id.sparePartsStatus);
        EditText spareText = (EditText) V.findViewById(R.id.spareParts);
        LinearLayout DocumentsLayout = (LinearLayout) V.findViewById(R.id.DocumentLinks);
        Button addDocument = (Button) V.findViewById(R.id.button55);
        DocumentsLayout.setVisibility(View.GONE);
        spareText.setVisibility(View.GONE);
        spareCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spareText.setVisibility(View.VISIBLE);
                }
                else {
                    spareText.setVisibility(View.GONE);
                }
            }
        });
        RadioButton done = (RadioButton) V.findViewById(R.id.done);
        RadioButton undone = (RadioButton)V.findViewById(R.id.undone);
        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    undone.setChecked(false);
                    S[0] = 1 ;
                    DocumentsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        undone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    done.setChecked(false);
                    S[0] = 0 ;
                    DocumentsLayout.setVisibility(View.GONE);
                }
            }
        });
        addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Documents.size() == 3 ) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Only 3 files",getResources().getString(R.string.addOnly3Files));
                    return;
                }
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
        });
        NewResponsesLayout.addView(V);
        addResponseBtn.setVisibility(View.GONE);
        saveResponseBtn.setVisibility(View.VISIBLE);
        saveResponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("savingResponse" , "documents are "+Documents.size() );
                if (response.getText() == null || response.getText().toString().isEmpty()) {
                    ToastMaker.Show(1,getResources().getString(R.string.pleaseEnterResponse),act);
                    return;
                }
                if (!done.isChecked() && !undone.isChecked()) {
                    ToastMaker.Show(1,getResources().getString(R.string.pleaseSelectDoneOrNot),act);
                    return;
                }
                int x = 0 ;
                if (spareCheck.isChecked()) {
                    x=1;
                }
                Log.d("savingResponse" ,S[0]+"");
                if (S[0] == 1) {
                    if (Documents.size() == 0 ) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Attache files",getResources().getString(R.string.mustAttachDocument));
                    }
                    else {
                        String[] links = new String[Documents.size()];
                        int finalX1 = x;
                        if (Documents.size() == 1) {
                            loading = new ProgressLoadingDialog(act,2);
                            MyApp.savePhoto(Documents.get(0), new VollyCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    loading.setProgress(1);
                                    links[0] = s ;
                                    saveResponse(response.getText().toString(),S[0], finalX1,spareText.getText().toString(),links,2);
                                }

                                @Override
                                public void onFailed(String error) {
                                    loading.stop();
                                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error","error while saving document "+error);
                                }
                            });
                        }
                        else if (Documents.size() == 2) {
                            loading = new ProgressLoadingDialog(act,3);
                            MyApp.savePhoto(Documents.get(0), new VollyCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    loading.setProgress(1);
                                    links[0] = s ;
                                    MyApp.savePhoto(Documents.get(1), new VollyCallback() {
                                        @Override
                                        public void onSuccess(String s) {
                                            loading.setProgress(2);
                                            links[1] = s ;
                                            saveResponse(response.getText().toString(),S[0], finalX1,spareText.getText().toString(),links,3);
                                        }

                                        @Override
                                        public void onFailed(String error) {
                                            loading.stop();
                                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error","error while saving document "+error);
                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String error) {
                                    loading.stop();
                                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error","error while saving document "+error);
                                }
                            });
                        }
                        else if (Documents.size() == 3) {
                            loading = new ProgressLoadingDialog(act,4);
                            MyApp.savePhoto(Documents.get(0), new VollyCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    loading.setProgress(1);
                                    links[0] = s ;
                                    MyApp.savePhoto(Documents.get(1), new VollyCallback() {
                                        @Override
                                        public void onSuccess(String s) {
                                            loading.setProgress(2);
                                            links[1] = s ;
                                            MyApp.savePhoto(Documents.get(2), new VollyCallback() {
                                                @Override
                                                public void onSuccess(String s) {
                                                    loading.setProgress(3);
                                                    links[2] = s ;
                                                    saveResponse(response.getText().toString(),S[0], finalX1,spareText.getText().toString(),links,4);
                                                }

                                                @Override
                                                public void onFailed(String error) {
                                                    loading.stop();
                                                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error","error while saving document "+error);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailed(String error) {
                                            loading.stop();
                                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error","error while saving document "+error);
                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String error) {
                                    loading.stop();
                                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"error","error while saving document "+error);
                                }
                            });
                        }
                    }
                }
                else {
                    loading.setProgress(1);
                    saveResponse(response.getText().toString(),S[0],x,spareText.getText().toString(),null,1);
                }
            }
        });
    }

    void setRespectiveEmps() {
        if (ORDER.ToMaintenance == 1) {
            for (USER u : MyApp.EMPS) {
                if (u.JobTitle.equals("Project Manager")) {
                    RespectiveUsers.add(u);
                    break;
                }
            }
        }
        if (ORDER.ToAluminum == 1) {
            for (USER u : MyApp.EMPS) {
                if (u.JobTitle.equals("Factory Manager") && u.Department.equals("Aluminum Factory")) {
                    RespectiveUsers.add(u);
                    break;
                }
            }
        }
        if (ORDER.ToDoors == 1) {
            for (USER u : MyApp.EMPS) {
                if (u.JobNumber == 50007) {
                    RespectiveUsers.add(u);
                    break;
                }
            }
        }
        USER U = null ;
        for (USER u : MyApp.EMPS) {
            if (u.JobTitle.equals("Sales Manager") && u.Department.equals("Sales")) {
                RespectiveUsers.add(u);
            }
            if (ORDER.SalesMan == u.JobNumber) {
                RespectiveUsers.add(u);
                U = u ;
                break;
            }
        }
        if (U != null ) {
            for (USER u :MyApp.EMPS) {
                if (u.JobNumber == U.DirectManager) {
                    RespectiveUsers.add(u);
                    break;
                }
            }
        }
    }

    void saveResponse(String resp ,  int S , int spareS , String spare,String[] links,int progress) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
        String Time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        StringRequest request = new StringRequest(Request.Method.POST, saveResponseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saverespresp" , response);
                if (Integer.parseInt(response) > 0) {
                    loading.setProgress(progress);
                    int id = Integer.parseInt(response) ;
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved),0);
                    RESPONSES.add(new MAINTENANCE_ORDER_RESPONSE_class(id,ORDER.id,ORDER.ProjectID,ORDER.ClientID,resp,MyApp.db.getUser().JobNumber,spareS,spare,S,Date,Time,links[0],links[1],links[3]));
                    MaintenanceOrder_Response_Adapter adapter = new MaintenanceOrder_Response_Adapter(RESPONSES);
                    ResponsesRecycler.setAdapter(adapter);
                    addResponseBtn.setVisibility(View.VISIBLE);
                    saveResponseBtn.setVisibility(View.GONE);
                    NewResponsesLayout.removeView(V);
                    String mess = "" ;
                    if (S == 1) {
                        mess = getResources().getString(R.string.maintenanceDone) ;
                    }
                    MyApp.sendNotificationsToGroup(RespectiveUsers, getResources().getString(R.string.updatesOnMaintenanceOrder), mess, ORDER.ProjectName, ORDER.SalesMan, "MaintenanceOrderUpdates", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else if (response.equals("0")) {
                    ToastMaker.Show(0,"error saving response",act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(0,"error not send ",act);
                }
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.stop();
                Log.d("saverespresp" , error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String> par = new HashMap<String, String>();
                par.put("ProjectID", String.valueOf(ORDER.ProjectID) );
                par.put("MaintenanceID" , String.valueOf(ORDER.id));
                par.put("ClientID" ,String.valueOf(ORDER.ClientID));
                par.put("Response" , resp);
                par.put("Employee" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("SparePartsStatus" ,String.valueOf( spareS ));
                par.put("SpareParts" , spare);
                par.put("Date",Date);
                par.put("Time" , Time);
                par.put("OrderStatus",String.valueOf(S));
                if (links != null) {
                    if (links.length == 1) {
                        par.put("DocumentLink1" , links[0]);
                    }
                    if (links.length == 2) {
                        par.put("DocumentLink1" , links[0]);
                        par.put("DocumentLink2" , links[1]);
                    }
                    if (links.length == 3) {
                        par.put("DocumentLink1" , links[0]);
                        par.put("DocumentLink2" , links[1]);
                        par.put("DocumentLink3" , links[2]);
                    }
                }

                return par;
            }
        };
        Q.add(request);
    }

    public void showOnMap(View view) {
        Intent i = new Intent(act, ShowVisitsOnMap.class);
        i.putExtra("LA",ORDER.LA);
        i.putExtra("LO" , ORDER.LO);
        i.putExtra("ClientName" , ORDER.ProjectName);
        startActivity(i);
    }
}