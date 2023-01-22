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

public class MySiteVisitOrders extends AppCompatActivity {

    Activity act ;
    RecyclerView OrdersRecycler , StaffRecycler ;
    List<SITE_VISIT_ORDER_class> myOrdersList , myStaffOrdersList;
    RecyclerView.LayoutManager Manager , StaffManager ;
    RequestQueue Q ;
    String getMySiteVisitOrdersUrl = MyApp.MainUrl+"getMySiteVisitOrders.php";
    String getMyStaffSiteVisitOrdersUrl = MyApp.MainUrl+"getMyStaffSiteVisitOrders.php";
    RadioButton myDoneRB, myUndoneRB ,myStaffDoneRB , myStaffUndoneRB;
    int Status = 0 ;
    Button myOrders , myStaffOrders ;
    LinearLayout MyOrdersLayout , MyStaffLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_my_site_visit_orders_activity);
        setActivity();
        setActivityActions();
        setIfIHaveStaff();
    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        OrdersRecycler = findViewById(R.id.MySiteVisit_ordersRecycler);
        StaffRecycler = findViewById(R.id.MySiteVisit_ordersRecyclerstaff);
        myOrdersList = new ArrayList<>();
        myStaffOrdersList = new ArrayList<>();
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        StaffManager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        OrdersRecycler.setLayoutManager(Manager);
        StaffRecycler.setLayoutManager(StaffManager);
        myDoneRB = (RadioButton) findViewById(R.id.radioButton2);
        myStaffDoneRB = findViewById(R.id.radioButton2staff);
        myStaffUndoneRB = findViewById(R.id.radioButtonstaff);
        myUndoneRB = (RadioButton) findViewById(R.id.radioButton);
        myOrders = findViewById(R.id.button69);
        myOrders.setBackgroundResource(R.color.lightGray);
        myStaffOrders = findViewById(R.id.button68);
        MyOrdersLayout = findViewById(R.id.myLayout);
        MyStaffLayout = findViewById(R.id.myStaffLayout);
        MyStaffLayout.setVisibility(View.GONE);
        myOrders.setVisibility(View.GONE);
        myStaffOrders.setVisibility(View.GONE);
    }

    void setActivityActions() {
        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStaffLayout.setVisibility(View.GONE);
                MyOrdersLayout.setVisibility(View.VISIBLE);
                myOrders.setBackgroundResource(R.color.lightGray);
                myStaffOrders.setBackgroundResource(R.color.trasparent);
            }
        });
        myStaffOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStaffLayout.setVisibility(View.VISIBLE);
                MyOrdersLayout.setVisibility(View.GONE);
                myOrders.setBackgroundResource(R.color.trasparent);
                myStaffOrders.setBackgroundResource(R.color.lightGray);
            }
        });
        myDoneRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myUndoneRB.setChecked(false);
                    Status = 1 ;
                    getMySiteVisitOrders();
                }
            }
        });
        myUndoneRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myDoneRB.setChecked(false);
                    Status = 0 ;
                    getMySiteVisitOrders();
                }
            }
        });
        myStaffDoneRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myStaffUndoneRB.setChecked(false);
                    Status = 1 ;
                    getMyStaffSiteVisitOrders();
                }
            }
        });
        myStaffUndoneRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myStaffDoneRB.setChecked(false);
                    Status = 0 ;
                    getMyStaffSiteVisitOrders();
                }
            }
        });
        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStaffLayout.setVisibility(View.GONE);
                MyOrdersLayout.setVisibility(View.VISIBLE);
                myOrders.setBackgroundResource(R.color.lightGray);
                myStaffOrders.setBackgroundResource(R.color.trasparent);
            }
        });
        myStaffOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStaffLayout.setVisibility(View.VISIBLE);
                MyOrdersLayout.setVisibility(View.GONE);
                myOrders.setBackgroundResource(R.color.trasparent);
                myStaffOrders.setBackgroundResource(R.color.lightGray);
            }
        });
    }

    void setIfIHaveStaff() {
        if (MyApp.MyUser.isDirectManager || MyApp.MyUser.isDepartmentManager) {
            myOrders.setVisibility(View.VISIBLE);
            myStaffOrders.setVisibility(View.VISIBLE);
        }
        else {
            myOrders.setVisibility(View.GONE);
            myStaffOrders.setVisibility(View.GONE);
        }
    }

    void getMySiteVisitOrders() {
        myOrdersList.clear();
        SiteVisitOrders_Adapter Adapter = new SiteVisitOrders_Adapter(myOrdersList);
        OrdersRecycler.setAdapter(Adapter);
        Loading l = new Loading(act) ;
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST,getMySiteVisitOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resultsiteVisit" , response);
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
                            myOrdersList.add(new SITE_VISIT_ORDER_class(row.getInt("id"),row.getInt("SalesMan"),row.getInt("ForwardedTo"),row.getString("VisitReason"),row.getString("ProjectName"),row.getString("ResponsibleName"),row.getString("ResponsibleMobile"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Notes"),row.getString("Date"),row.getString("VisitDate"),row.getString("VisitTime"),row.getString("VisitResult"),row.getString("VisitNotes"),row.getString("DoneDate"),row.getString("DoneTime"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("resultsiteVisit" , e.getMessage());
                    }
                    if (myOrdersList.size() > 0) {
                        Collections.reverse(myOrdersList);
                        SiteVisitOrders_Adapter Adapter = new SiteVisitOrders_Adapter(myOrdersList);
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
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Status",String.valueOf(Status));
                return par;
            }
        };
        Q.add(request);
    }

    void getMyStaffSiteVisitOrders() {
        myStaffOrdersList.clear();
        SiteVisitOrders_Adapter Adapter = new SiteVisitOrders_Adapter(myStaffOrdersList);
        StaffRecycler.setAdapter(Adapter);
        Loading l = new Loading(act) ;
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST,getMyStaffSiteVisitOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resultsiteVisit" , response);
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
                            myStaffOrdersList.add(new SITE_VISIT_ORDER_class(row.getInt("id"),row.getInt("SalesMan"),row.getInt("ForwardedTo"),row.getString("VisitReason"),row.getString("ProjectName"),row.getString("ResponsibleName"),row.getString("ResponsibleMobile"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Notes"),row.getString("Date"),row.getString("VisitDate"),row.getString("VisitTime"),row.getString("VisitResult"),row.getString("VisitNotes"),row.getString("DoneDate"),row.getString("DoneTime"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("resultsiteVisit" , e.getMessage());
                    }
                    if (myStaffOrdersList.size() > 0) {
                        Collections.reverse(myStaffOrdersList);
                        SiteVisitOrders_Adapter Adapter = new SiteVisitOrders_Adapter(myStaffOrdersList);
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
                par.put("Count",String.valueOf(MyApp.MyUser.MyStaff.size()));
                par.put("Status",String.valueOf(Status));
                for (int i=0;i<MyApp.MyUser.MyStaff.size();i++) {
                    par.put("user"+i, String.valueOf(MyApp.MyUser.MyStaff.get(i).JobNumber));
                }
                return par;
            }
        };
        Q.add(request);
    }
}