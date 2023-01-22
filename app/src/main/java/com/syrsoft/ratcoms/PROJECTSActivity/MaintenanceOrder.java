package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.MaintenanceOrder_Response_Adapter;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.CLIENT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.PROJECT_CONTRACT_CLASS;
import com.syrsoft.ratcoms.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintenanceOrder extends AppCompatActivity {

    Activity act ;
    int OrderIndex ;
    MAINTENANCE_ORDER_CLASS ORDER ;
    List<MaintenanceOrderLink> Links ;
    String getClientUrl = MyApp.MainUrl+"getClient.php";
    String getContractUrl = MyApp.MainUrl+"getProjectContractByID.php";
    String getOrderLinksUrl = MyApp.MainUrl+"getMaintenanceOrderLinks.php";
    String getOrderResponsesUrl = MyApp.MainUrl+"getMaintenanceOrderResponses.php";
    CLIENT_CLASS CLIENT ;
    PROJECT_CONTRACT_CLASS CONTRACT ;
    RequestQueue Q ;
    TextView ClientName , ProjectName , OrderStatus , Date , DamageDesc , Notes ,ContactName,ContactNumber,SendTo ;
    LinearLayout LinksLayouts ;
    List<MAINTENANCE_ORDER_RESPONSE_class> RESPONSES ;
    RecyclerView ResponsesRecycler ;
    RecyclerView.LayoutManager Manager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_maintenance_order_activity);
        Bundle B = getIntent().getExtras();
        OrderIndex = B.getInt("Index");
        setActivity();
        getOrderLinks();
        getProject();
    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        ORDER = ViewMyMaintenanceOrders.ORDERS.get(OrderIndex) ;
        Links = new ArrayList<MaintenanceOrderLink>();
        ClientName = (TextView) findViewById(R.id.MaintenanceOrder_clientName);
        ProjectName = (TextView) findViewById(R.id.MaintenanceOrder_projectName);
        OrderStatus = (TextView) findViewById(R.id.MaintenanceOrder_status);
        Date = (TextView) findViewById(R.id.MaintenanceOrder_date);
        DamageDesc = (TextView) findViewById(R.id.MaintenanceOrder_damageDesc);
        Notes = (TextView) findViewById(R.id.MaintenanceOrder_notes);
        ContactName = (TextView) findViewById(R.id.MaintenanceOrder_contactName);
        ContactNumber = (TextView) findViewById(R.id.MaintenanceOrder_contactNumber);
        SendTo = (TextView) findViewById(R.id.MaintenanceOrder_department);
        LinksLayouts = (LinearLayout) findViewById(R.id.LinksLayouts);
        RESPONSES = new ArrayList<MAINTENANCE_ORDER_RESPONSE_class>();
        ResponsesRecycler = (RecyclerView) findViewById(R.id.responsesRecycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        ResponsesRecycler.setLayoutManager(Manager);
        ProjectName.setText(ORDER.ProjectName);
        Date.setText(ORDER.Date);
        DamageDesc.setText(ORDER.DamageDesc);
        Notes.setText(ORDER.Notes);
        ContactName.setText(ORDER.ContactName);
        ContactNumber.setText(ORDER.Contact);
        OrderStatus.setText(ORDER.getStatus());
        SendTo.setText(ORDER.getDepartment());
        getOrderResponses();
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
                    CONTRACT.setCLIENT(CLIENT);
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
                Log.d("linksResp" ,response);
                l.close();
                if (response.equals("0")) {
                    Log.d("linksResp" ,"0");
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        Links.add(new MaintenanceOrderLink(row.getInt("id"),row.getInt("MaintenanceID"),row.getString("Link")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("linksResp" ,e.getMessage());
                    }
                    ORDER.setLinks(Links);
                    if (Links.size()>0) {
                        for (int i=0;i<Links.size();i++) {
                            View v = LayoutInflater.from(act).inflate(R.layout.image, null);
                            LinksLayouts.addView(v);
                            ImageView image = (ImageView) v;
                            Picasso.get().load(Links.get(i).Link).into(image);
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
                        }
                    }
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
                par.put("ID", String.valueOf(ORDER.id) );
                return par;
            }
        };
        Q.add(request);
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
}