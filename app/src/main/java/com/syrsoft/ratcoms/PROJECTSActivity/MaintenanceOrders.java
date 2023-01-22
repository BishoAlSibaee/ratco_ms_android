package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.MaintenanceOrders_Adapter;
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.Maintenance_Order_Adapter_ForProjects;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintenanceOrders extends AppCompatActivity {

    Activity act ;
    public static List<MAINTENANCE_ORDER_CLASS> ORDERS , UnFOrdersList ;
    String getMaintenanceOrdersUrl  ;
    RequestQueue Q ;
    RecyclerView MaintenanceOrdersRecycler , UnForwardedRecycler ;
    RecyclerView.LayoutManager Manager , UnFManager ;
    RadioButton Done , UnDone ;
    public static int Status = 0 , Forward = 1 ;
    Button Forwarded , UnForwarded ;
    LinearLayout ForwardedLayout , UnForwardedLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_maintenance_orders_activity);
        setActivity();
        setActivityActions();
        setHideAndUnhideItems();
    }

    void setActivity() {
        act = this ;
        Q = Volley.newRequestQueue(act);
        ORDERS = new ArrayList<>();
        UnFOrdersList = new ArrayList<>();
        MaintenanceOrdersRecycler = (RecyclerView) findViewById(R.id.MaintenanceOrders_Recycler);
        UnForwardedRecycler = findViewById(R.id.MaintenanceOrders_RecyclerFarwarded);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        UnFManager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        MaintenanceOrdersRecycler.setLayoutManager(Manager);
        UnForwardedRecycler.setLayoutManager(UnFManager);
        Forwarded = findViewById(R.id.button72);
        UnForwarded = findViewById(R.id.button73);
        ForwardedLayout = findViewById(R.id.FarwardedLayout);
        UnForwardedLayout = findViewById(R.id.unFarwardedLayout);
        Done = (RadioButton) findViewById(R.id.done);
        UnDone = (RadioButton) findViewById(R.id.unDone);
    }

    void setActivityActions() {
        Forwarded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forward = 1 ;
                ForwardedLayout.setVisibility(View.VISIBLE);
                UnForwardedLayout.setVisibility(View.GONE);
                Forwarded.setBackgroundResource(R.color.lightGray);
                UnForwarded.setBackgroundResource(R.color.trasparent);
            }
        });
        UnForwarded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status = 0 ;
                Forward = 0 ;
                ForwardedLayout.setVisibility(View.GONE);
                UnForwardedLayout.setVisibility(View.VISIBLE);
                UnForwarded.setBackgroundResource(R.color.lightGray);
                Forwarded .setBackgroundResource(R.color.trasparent);
                getMaintenanceOrdersOfDepartmentManagerUnForwarded();
            }
        });
        Done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UnDone.setChecked(false);
                    Status = 1 ;
                    Forward = 1 ;
                    getMyMaintenanceOrders();
                }
            }
        });
        UnDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Done.setChecked(false);
                    Status = 0 ;
                    Forward = 1 ;
                    getMyMaintenanceOrders();
                }
            }
        });
    }

    void setHideAndUnhideItems() {
        if (MyApp.MyUser.isDepartmentManager) {
            Forwarded.setVisibility(View.VISIBLE);
            UnForwarded.setVisibility(View.VISIBLE);
        }
        else if (MyApp.MyUser.isDirectManager) {
            Forwarded.setVisibility(View.GONE);
            UnForwarded.setVisibility(View.GONE);
            UnForwardedLayout.setVisibility(View.GONE);
        }
        else {
            Forwarded.setVisibility(View.GONE);
            UnForwarded.setVisibility(View.GONE);
            UnForwardedLayout.setVisibility(View.GONE);
        }
    }

    void getMyMaintenanceOrders() {
        if (MyApp.MyUser.isDepartmentManager) {
            getMaintenanceOrdersOfDepartmentManager();
        }
        else if (MyApp.MyUser.isDirectManager) {
            getMaintenanceOrdersOfDirectManager();
        }
        else {
            getMaintenanceOrders();
        }
    }

    void getMaintenanceOrders() {
        getMaintenanceOrdersUrl = MyApp.MainUrl+"getMaintenanceOrderByForwardedTo.php" ;
        ORDERS.clear();
        Maintenance_Order_Adapter_ForProjects adapter = new Maintenance_Order_Adapter_ForProjects(ORDERS);
        MaintenanceOrdersRecycler.setAdapter(adapter);
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST,getMaintenanceOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" ,act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"Error getting Date" ,act);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ORDERS.add(new MAINTENANCE_ORDER_CLASS(row.getInt("id"),row.getInt("ProjectID"),row.getString("ProjectName"),row.getInt("ClientID"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("DamageDesc"),row.getString("Notes"),row.getString("Contact"),row.getString("ContactName"),row.getString("Date"),row.getInt("ToMaintenance"),row.getInt("ToAluminum"),row.getInt("ToDoors"),row.getInt("ForwardedTo"),row.getString("Response"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Maintenance_Order_Adapter_ForProjects adapter = new Maintenance_Order_Adapter_ForProjects(ORDERS);
                    MaintenanceOrdersRecycler.setAdapter(adapter);
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
                par.put("Status",String.valueOf(Status));
                par.put("To" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Q.add(request);
    }

    void getMaintenanceOrdersOfDepartmentManager() {
        ORDERS.clear();
        Maintenance_Order_Adapter_ForProjects adapter = new Maintenance_Order_Adapter_ForProjects(ORDERS);
        MaintenanceOrdersRecycler.setAdapter(adapter);
        String url = MyApp.MainUrl+"getMaintenanceOrdersOfDepartmentManager.php" ;
        Loading l = new Loading(act);
        l.show();
        StringRequest req = new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("departmentMorders",response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" ,act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"Error getting Date" ,act);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ORDERS.add(new MAINTENANCE_ORDER_CLASS(row.getInt("id"),row.getInt("ProjectID"),row.getString("ProjectName"),row.getInt("ClientID"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("DamageDesc"),row.getString("Notes"),row.getString("Contact"),row.getString("ContactName"),row.getString("Date"),row.getInt("ToMaintenance"),row.getInt("ToAluminum"),row.getInt("ToDoors"),row.getInt("ForwardedTo"),row.getString("Response"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Maintenance_Order_Adapter_ForProjects adapter = new Maintenance_Order_Adapter_ForProjects(ORDERS);
                    MaintenanceOrdersRecycler.setAdapter(adapter);
                }
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("departmentMorders",error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Department" , MyApp.MyUser.Department);
                par.put("Status",String.valueOf(Status));
                par.put("Forward" , String.valueOf(Forward));
                par.put("Count" , String.valueOf(MyApp.MyUser.MyStaff.size()));
                for (int i=0;i<MyApp.MyUser.MyStaff.size();i++) {
                    par.put("To"+i , String.valueOf(MyApp.MyUser.MyStaff.get(i).JobNumber));
                }
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

    void getMaintenanceOrdersOfDirectManager() {
        ORDERS.clear();
        Maintenance_Order_Adapter_ForProjects adapter = new Maintenance_Order_Adapter_ForProjects(ORDERS);
        MaintenanceOrdersRecycler.setAdapter(adapter);
        String url = MyApp.MainUrl+"getMaintenanceOrdersOfDirectManager.php" ;
        Loading l = new Loading(act);
        l.show();
        StringRequest req = new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("departmentMorders",response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" ,act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"Error getting Date" ,act);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ORDERS.add(new MAINTENANCE_ORDER_CLASS(row.getInt("id"),row.getInt("ProjectID"),row.getString("ProjectName"),row.getInt("ClientID"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("DamageDesc"),row.getString("Notes"),row.getString("Contact"),row.getString("ContactName"),row.getString("Date"),row.getInt("ToMaintenance"),row.getInt("ToAluminum"),row.getInt("ToDoors"),row.getInt("ForwardedTo"),row.getString("Response"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Maintenance_Order_Adapter_ForProjects adapter = new Maintenance_Order_Adapter_ForProjects(ORDERS);
                    MaintenanceOrdersRecycler.setAdapter(adapter);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("departmentMorders",error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Department" , MyApp.MyUser.Department);
                par.put("Status",String.valueOf(Status));
                par.put("Count" , String.valueOf(MyApp.MyUser.MyStaff.size()));
                for (int i=0;i<MyApp.MyUser.MyStaff.size();i++) {
                    par.put("To"+i , String.valueOf(MyApp.MyUser.MyStaff.get(i).JobNumber));
                }
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

    void getMaintenanceOrdersOfDepartmentManagerUnForwarded() {
        UnFOrdersList.clear();
        Maintenance_Order_Adapter_ForProjects adapter = new Maintenance_Order_Adapter_ForProjects(UnFOrdersList);
        UnForwardedRecycler.setAdapter(adapter);
        String url = MyApp.MainUrl+"getMaintenanceOrdersOfDepartmentManager.php" ;
        Loading l = new Loading(act);
        l.show();
        StringRequest req = new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("departmentMorders",response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" ,act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"Error getting Date" ,act);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            UnFOrdersList.add(new MAINTENANCE_ORDER_CLASS(row.getInt("id"),row.getInt("ProjectID"),row.getString("ProjectName"),row.getInt("ClientID"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("DamageDesc"),row.getString("Notes"),row.getString("Contact"),row.getString("ContactName"),row.getString("Date"),row.getInt("ToMaintenance"),row.getInt("ToAluminum"),row.getInt("ToDoors"),row.getInt("ForwardedTo"),row.getString("Response"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Maintenance_Order_Adapter_ForProjects adapter = new Maintenance_Order_Adapter_ForProjects(UnFOrdersList);
                    UnForwardedRecycler.setAdapter(adapter);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("departmentMorders",error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Department" , MyApp.MyUser.Department);
                par.put("Status",String.valueOf(Status));
                par.put("Forward" , String.valueOf(Forward));
                par.put("Count" , String.valueOf(MyApp.MyUser.MyStaff.size()));
                for (int i=0;i<MyApp.MyUser.MyStaff.size();i++) {
                    par.put("To"+i , String.valueOf(MyApp.MyUser.MyStaff.get(i).JobNumber));
                }
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }
}