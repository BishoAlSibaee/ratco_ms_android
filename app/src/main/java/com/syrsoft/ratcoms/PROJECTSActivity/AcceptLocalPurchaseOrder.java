package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE;
import com.syrsoft.ratcoms.HRActivities.JobTitle;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.PurchaseOrders_Adapter;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AcceptLocalPurchaseOrder extends AppCompatActivity {

    Activity act ;
    RadioButton Done , Undone ;
    boolean DoneStatus = false ;
    List<JobTitle> titles ;
    HR_ORDER_TYPE MyType ;
    public static List<USER> ApproveUsers ;
    boolean amIApprove = false ;
    String getForApproveUrl = MyApp.MainUrl+"getPurchaseOrdersForApprove.php";
    String getDonePurchaseOrdersUrl = MyApp.MainUrl+"getDonePurchaseOrders.php";
    public static List<LOCAL_PURCHASE_ORDER> MyList ;
    RecyclerView OrdersRecycler ;
    LinearLayoutManager Manager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_accept_local_purchase_order_activity);
        setActivity();
        setActivityActions();
        getPurchaseOrdersApprovalJobTitles();
        Log.d("myClass" , this.getClass().getName());
    }

    void setActivity() {
        act = this ;
        Done = findViewById(R.id.radioButton9);
        Undone = findViewById(R.id.radioButton8);
        ApproveUsers = new ArrayList<>();
        titles = new ArrayList<>();
        MyList = new ArrayList<>();
        OrdersRecycler = findViewById(R.id.ordersRecycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        OrdersRecycler.setLayoutManager(Manager);
    }

    void setActivityActions() {
        Done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DoneStatus = true ;
                    Undone.setChecked(false);
                    getDonePurchaseOrders();
                }
            }
        });
        Undone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DoneStatus = false ;
                    Done.setChecked(false);
                    getPurchaseOrdersForApprove();
                }
            }
        });
        Undone.setChecked(true);
    }

    void getPurchaseOrdersApprovalJobTitles() {
        ApproveUsers.clear();
        for (int i=0;i< MyApp.Types.size();i++) {
            if (MyApp.Types.get(i).HROrderName.equals("Local Purchase Order")) {
                MyType = MyApp.Types.get(i) ;
                if (MyType.Auth1 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth1));
                }
                if (MyType.Auth2 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth2));
                }
                if (MyType.Auth3 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth3));
                }
                if (MyType.Auth4 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth4));
                }
                if (MyType.Auth5 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth5));
                }
                if (MyType.Auth6 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth6));
                }
                if (MyType.Auth7 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth7));
                }
                if (MyType.Auth8 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth8));
                }
                if (MyType.Auth9 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth9));
                }
                if (MyType.Auth10 != 0) {
                    titles.add(JobTitle.searchJobTitleById(MyApp.JobTitles,MyType.Auth10));
                }
                break;
            }
        }
        if (titles.size() > 0) {
            for (int i=0 ;i < titles.size();i++) {
                if (titles.get(i).JobTitle.equals("Direct Manager")) {

                }
                else if (titles.get(i).JobTitle.equals("Department Manager")) {

                }
                else {

                }
                ApproveUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,titles.get(i).JobTitle));
            }
        }
        for (int i=0;i<ApproveUsers.size();i++) {
            if (MyApp.MyUser.JobNumber == ApproveUsers.get(i).JobNumber) {
                amIApprove = true ;
                break;
            }
        }
    }

    void getPurchaseOrdersForApprove () {
//        Loading l = new Loading(act);
//        l.show();
//        StringRequest req = new StringRequest(Request.Method.POST, getForApproveUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                l.close();
//                Log.d("ordersRes" , response);
//                if (!response.equals("0")) {
//                    try {
//                        JSONArray arr = new JSONArray(response);
//                        MyList.clear();
//                        for (int i=0;i<arr.length();i++) {
//                            JSONObject row = arr.getJSONObject(i);
//                            LOCAL_PURCHASE_ORDER or = new LOCAL_PURCHASE_ORDER(row.getInt("id"),row.getInt("ProjectID"),row.getString("ProjectName"),row.getInt("Supmitter"),row.getInt("AcceptedQID"),
//                                    row.getInt("Acc1ID"),row.getString("Acc1Note"),row.getInt("Acc1Status"),row.getInt("Acc2ID"),row.getString("Acc2Note"),row.getInt("Acc2Status")
//                            ,row.getInt("Acc3ID"),row.getString("Acc3Note"),row.getInt("Acc3Status"),row.getInt("Acc4ID"),row.getString("Acc4Note"),row.getInt("Acc4Status"),row.getInt("Acc5ID"),
//                                    row.getString("Acc5Note"),row.getInt("Acc5Status"),row.getInt("Status"),row.getString("Notes"));
//                            MyList.add(or);
//                        }
//                        PurchaseOrders_Adapter adapter = new PurchaseOrders_Adapter(MyList);
//                        OrdersRecycler.setAdapter(adapter);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.d("ordersRes" , e.getMessage());
//                    }
//                }
//                else {
//                    MyList.clear();
//                    PurchaseOrders_Adapter adapter = new PurchaseOrders_Adapter(MyList);
//                    OrdersRecycler.setAdapter(adapter);
//                    ToastMaker.Show(0,"No Purchase Orders" , act);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                l.close();
//                Log.d("ordersRes" , error.toString());
//                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , error.toString());
//            }
//        });
//        Volley.newRequestQueue(act).add(req);

        MyList = MyApp.MyUser.purchaseList ;
        PurchaseOrders_Adapter adapter = new PurchaseOrders_Adapter(MyList);
        OrdersRecycler.setAdapter(adapter);

    }

    void getDonePurchaseOrders () {
        Loading l = new Loading(act);
        l.show();
        StringRequest req = new StringRequest(Request.Method.POST, getDonePurchaseOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("ordersRes" , response);
                if (!response.equals("0")) {
                    try {
                        JSONArray arr = new JSONArray(response);
                        MyList.clear();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            LOCAL_PURCHASE_ORDER or = new LOCAL_PURCHASE_ORDER(row.getInt("id"),row.getInt("ProjectID"),row.getString("ProjectName"),row.getInt("Supmitter"),row.getInt("AcceptedQID"),
                                    row.getInt("Acc1ID"),row.getString("Acc1Note"),row.getInt("Acc1Status"),row.getInt("Acc2ID"),row.getString("Acc2Note"),row.getInt("Acc2Status")
                                    ,row.getInt("Acc3ID"),row.getString("Acc3Note"),row.getInt("Acc3Status"),row.getInt("Acc4ID"),row.getString("Acc4Note"),row.getInt("Acc4Status"),row.getInt("Acc5ID"),
                                    row.getString("Acc5Note"),row.getInt("Acc5Status"),row.getInt("Status"),row.getString("Notes"));
                            MyList.add(or);
                        }
                        PurchaseOrders_Adapter adapter = new PurchaseOrders_Adapter(MyList);
                        OrdersRecycler.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("ordersRes" , e.getMessage());
                    }
                }
                else {
                    MyList.clear();
                    PurchaseOrders_Adapter adapter = new PurchaseOrders_Adapter(MyList);
                    OrdersRecycler.setAdapter(adapter);
                    ToastMaker.Show(0,"No Purchase Orders" , act);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("ordersRes" , error.toString());
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , error.toString());
            }
        });
        Volley.newRequestQueue(act).add(req);
    }

}
