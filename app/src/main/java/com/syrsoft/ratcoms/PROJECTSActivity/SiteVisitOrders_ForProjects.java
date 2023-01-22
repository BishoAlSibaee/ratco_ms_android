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
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.SiteVisitOrders_Adapter;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteVisitOrders_ForProjects extends AppCompatActivity {

    Activity act ;
    RecyclerView OrdersRecycler , StaffRecycler ;
    List<SITE_VISIT_ORDER_class> ordersList , StaffList ;
    RecyclerView.LayoutManager Manager , StaffManager ;
    RequestQueue Q ;
    String getSiteVisitOrdersUrl = MyApp.MainUrl + "getSiteVisitOrdersByTo.php";
    String getAllSiteVisitOrdersUrl = MyApp.MainUrl + "getSiteVisitOrdersSentToMyStaff.php";
    RadioButton doneRB , undoneRB , StaffDone , StaffUndone ;
    Button MyOrdersBtn , MyStaffOrdersBtn ;
    LinearLayout MyOrdersLayout , MyStaffLayout ;
    int Status = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_site_visit_orders__for_projects_activity);
        setActivity();
        setIfIHaveStaff();
        setActivityActions();
    }

    void setActivity() {
        act = this ;
        Q = Volley.newRequestQueue(act);
        OrdersRecycler = findViewById(R.id.MySiteVisit_ordersRecycler);
        StaffRecycler = findViewById(R.id.MyStaffSiteVisit_ordersRecycler);
        ordersList = new ArrayList<>();
        StaffList = new ArrayList<>();
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        StaffManager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        OrdersRecycler.setLayoutManager(Manager);
        StaffRecycler.setLayoutManager(StaffManager);
        doneRB = (RadioButton) findViewById(R.id.radioButton2);
        StaffDone = (RadioButton) findViewById(R.id.radioButton2staff);
        undoneRB = (RadioButton) findViewById(R.id.radioButton);
        StaffUndone = (RadioButton) findViewById(R.id.radioButtonstaff);
        MyOrdersBtn = findViewById(R.id.button71);
        MyOrdersBtn.setBackgroundResource(R.color.lightGray);
        MyStaffOrdersBtn = findViewById(R.id.button70);
        MyOrdersLayout = findViewById(R.id.myOrdersLayout);
        MyStaffLayout = findViewById(R.id.myStaffLayout);
        MyStaffLayout.setVisibility(View.GONE);
    }

    void setActivityActions() {
        MyOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrdersLayout.setVisibility(View.VISIBLE);
                MyStaffLayout.setVisibility(View.GONE);
                MyOrdersBtn.setBackgroundResource(R.color.lightGray);
                MyStaffOrdersBtn.setBackgroundResource(R.color.trasparent);
            }
        });
        MyStaffOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrdersLayout.setVisibility(View.GONE);
                MyStaffLayout.setVisibility(View.VISIBLE);
                MyOrdersBtn.setBackgroundResource(R.color.trasparent);
                MyStaffOrdersBtn.setBackgroundResource(R.color.lightGray);
            }
        });
        StaffDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StaffUndone.setChecked(false);
                    Status = 1 ;
                    getMyStaffSiteVisitOrders();
                }
            }
        });
        StaffUndone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StaffDone.setChecked(false);
                    Status = 0 ;
                    getMyStaffSiteVisitOrders();
                }
            }
        });
        doneRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    undoneRB.setChecked(false);
                    Status = 1 ;
                    getMySiteVisitOrders();
                }
            }
        });
        undoneRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    doneRB.setChecked(false);
                    Status = 0 ;
                    getMySiteVisitOrders();
                }
            }
        });
        undoneRB.setChecked(true);
    }

    void setIfIHaveStaff() {
        if (MyApp.MyUser.isDirectManager || MyApp.MyUser.isDepartmentManager) {
            MyOrdersBtn.setVisibility(View.VISIBLE);
            MyStaffOrdersBtn.setVisibility(View.VISIBLE);
        }
        else {
            MyOrdersBtn.setVisibility(View.GONE);
            MyStaffOrdersBtn.setVisibility(View.GONE);
        }
    }

    void getMySiteVisitOrders() {
        ordersList.clear();
        SiteVisitOrders_Adapter Adapter = new SiteVisitOrders_Adapter(ordersList);
        OrdersRecycler.setAdapter(Adapter);
        Loading l = new Loading(act) ;
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST,getSiteVisitOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resultsiteVisit" , response+" ");
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Orders",act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"Error",act);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ordersList.add(new SITE_VISIT_ORDER_class(row.getInt("id"),row.getInt("SalesMan"),row.getInt("ForwardedTo"),row.getString("VisitReason"),row.getString("ProjectName"),row.getString("ResponsibleName"),row.getString("ResponsibleMobile"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Notes"),row.getString("Date"),row.getString("VisitDate"),row.getString("VisitTime"),row.getString("VisitResult"),row.getString("VisitNotes"),row.getString("DoneDate"),row.getString("DoneTime"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ordersList.size() > 0) {
                        Collections.reverse(ordersList);
                        SiteVisitOrders_Adapter Adapter = new SiteVisitOrders_Adapter(ordersList);
                        OrdersRecycler.setAdapter(Adapter);
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
                Map<String,String> par = new HashMap<String, String>();
                par.put("To" , String.valueOf(MyApp.MyUser.JobNumber));
                par.put("Status" , String.valueOf(Status));
                return par;
            }
        };
        Q.add(request);
    }

    void getMyStaffSiteVisitOrders() {
        StaffList.clear();
        SiteVisitOrders_Adapter Adapter = new SiteVisitOrders_Adapter(StaffList);
        StaffRecycler.setAdapter(Adapter);
        Loading l = new Loading(act) ;
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST,getAllSiteVisitOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resultsiteVisit" , response+" ");
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Orders",act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"Error",act);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            StaffList.add(new SITE_VISIT_ORDER_class(row.getInt("id"),row.getInt("SalesMan"),row.getInt("ForwardedTo"),row.getString("VisitReason"),row.getString("ProjectName"),row.getString("ResponsibleName"),row.getString("ResponsibleMobile"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Notes"),row.getString("Date"),row.getString("VisitDate"),row.getString("VisitTime"),row.getString("VisitResult"),row.getString("VisitNotes"),row.getString("DoneDate"),row.getString("DoneTime"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("resultsiteVisit" , e.toString());
                    }
                    if (StaffList.size() > 0) {
                        Collections.reverse(StaffList);
                        SiteVisitOrders_Adapter Adapter = new SiteVisitOrders_Adapter(StaffList);
                        StaffRecycler.setAdapter(Adapter);
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
                Map<String,String> par = new HashMap<String, String>();
                par.put("Status" , String.valueOf(Status));
                par.put("Count" , String.valueOf(MyApp.MyUser.MyStaff.size()));
                for (int i=0;i<MyApp.MyUser.MyStaff.size();i++) {
                    par.put("To"+i , String.valueOf(MyApp.MyUser.MyStaff.get(i).JobNumber));
                }
                return par;
            }
        };
        Q.add(request);
//        String url = "" ;
//        if (MyApp.MyUser.JobTitle.equals("Manager") || MyApp.MyUser.JobTitle.equals("Sales Manager")) {
//            url = getAllSiteVisitOrdersUrl ;
//            getOrders("",url);
//        }
//        else {
//            url = getSiteVisitOrdersUrl ;
//            int x=0;
//            getOrders(String.valueOf(MyApp.MyUser.JobNumber),url);
//            //Log.d("times" , x+" ");
//            for (USER u :MyApp.EMPS) {
//                if (u.DirectManager == MyApp.MyUser.JobNumber) {
//                    x++;
//                    Log.d("times" , x+" "+u.DirectManager+" "+MyApp.db.getUser().JobNumber+" "+MyApp.EMPS.size());
//                    getOrders(String.valueOf(u.JobNumber),url);
//                }
//            }
//        }
    }

}