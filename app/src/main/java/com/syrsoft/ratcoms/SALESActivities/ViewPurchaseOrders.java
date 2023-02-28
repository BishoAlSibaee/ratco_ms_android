package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
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
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.PurchaseOrders_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPurchaseOrders extends AppCompatActivity {
    RecyclerView ResOrder;
    RadioButton RadioAccept, RadioPending;
    static List<PURCHASE_CLASS> listPurchase;
    LinearLayoutManager manager;
    PurchaseOrders_Adapter adapter;
    Activity act;
    // String UrlGetData = "http://192.168.100.101/EmployeeManagement/GetPurchase.php";
    String UrlGetData = MyApp.MainUrl + "GetPurchase";
    //  String UrlGetDataUpdate = "http://192.168.100.101/EmployeeManagement/GetPurchaseUpdate.php";
    String UrlGetDataUpdate = MyApp.MainUrl + "GetPurchaseUpdate";
    // String UrlGetDateAttachment = "http://192.168.100.101/EmployeeManagement/GetPurchaseAttachment.php";
    String UrlGetDateAttachment = MyApp.MainUrl + "GetPurchaseAttachment";

    private RequestQueue Q;
    String ReceiveStatus = "0", MyJobTitle;
    Loading l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchase_orders);
        setActivity();
        setActivityActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPurchaseOrders(ReceiveStatus);
    }

    void setActivity() {
        listPurchase = new ArrayList<>();
        ResOrder = findViewById(R.id.ResOrder);
        RadioAccept = findViewById(R.id.RadioAccept);
        RadioPending = findViewById(R.id.RadioPending);
        RadioPending.setChecked(true);
        act = this;
        manager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        ResOrder.setLayoutManager(manager);
        Q = Volley.newRequestQueue(act);
        MyJobTitle = MyApp.MyUser.JobTitle;
        l = new Loading(act);
    }

    void setActivityActions() {
        RadioAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listPurchase.clear();
                    ReceiveStatus = "1";
                    RadioPending.setChecked(false);
                    getPurchaseOrders(ReceiveStatus);
                }
            }
        });
        RadioPending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listPurchase.clear();
                    ReceiveStatus = "0";
                    RadioAccept.setChecked(false);
                    getPurchaseOrders(ReceiveStatus);
                }
            }
        });
    }

    void getPurchaseOrders(String Res) {
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlGetData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    listPurchase.clear();
                    l.close();
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject row = arr.getJSONObject(i);
                        PURCHASE_CLASS x = new PURCHASE_CLASS(row.getInt("id"), row.getInt("project_id"), row.getString("project_name"), row.getInt("client_id"), row.getString("client_name"), row.getInt("salesman"), row.getString("date"),
                                row.getString("delivery_date"), row.getString("salesmanager_accept"),
                                row.getString("salesmanager_accept_date"), row.getString("importmanager_accept"),
                                row.getString("importmanager_accept_date"), row.getString("order_status"),
                                row.getString("order_date"), row.getString("ecpected_delevary_date"), row.getString("receive_status"), row.getString("receive_date"));
                        getOrderUpdates(x.id, new OrderUpdateCallback() {
                            @Override
                            public void onSuccess(List<PURCHASE_UPDATE_CLASS> u) {
                                x.listPurchaseUpdate = u;
                            }

                            @Override
                            public void onFailed() {
                            }
                        });
                        listPurchase.add(x);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new PurchaseOrders_Adapter(listPurchase);
                ResOrder.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parm = new HashMap<String, String>();
                parm.put("receive_status", Res);
                if (MyJobTitle.equals("SalesMan")) {
                    parm.put("jobNumber", String.valueOf(MyApp.MyUser.JobNumber));
                }
                return parm;
            }
        };
        Q.add(request);
    }

    void getOrderUpdates(int orderId, OrderUpdateCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, UrlGetDataUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("updatesResp", response);
                try {
                    List<PURCHASE_UPDATE_CLASS> list = new ArrayList<>();
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject op = arr.getJSONObject(i);
                        PURCHASE_UPDATE_CLASS PUC = new PURCHASE_UPDATE_CLASS(op.getInt("id"), op.getInt("order_id"), op.getInt("user_id"),
                                op.getString("Update_date"), op.getString("type"), op.getString("notes"));
                        getOrderUpdateAttachments(PUC.id, new OrderUpdateAttachmentCallbak() {
                            @Override
                            public void onSuccess(List<ImportUpdateAttachment> u) {
                                PUC.Attachments = u;
                            }

                            @Override
                            public void onFailed() {
                            }
                        });
                        list.add(PUC);
                    }
                    callback.onSuccess(list);
                    l.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("updatesResp", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parm = new HashMap<String, String>();
                parm.put("OrderID", String.valueOf(orderId));
                return parm;
            }
        };
        Q.add(request);
    }

    void getOrderUpdateAttachments(int updateId, OrderUpdateAttachmentCallbak callbak) {
        StringRequest request = new StringRequest(Request.Method.POST, UrlGetDateAttachment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("resppp", response);
                    List<ImportUpdateAttachment> list = new ArrayList<>();
                    JSONArray Arr = new JSONArray(response);
                    for (int i = 0; i < Arr.length(); i++) {
                        JSONObject row = Arr.getJSONObject(i);
                        ImportUpdateAttachment IUA = new ImportUpdateAttachment(row.getInt("id"), row.getInt("update_id"), row.getString("link"));
                        list.add(IUA);
                    }
                    callbak.onSuccess(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callbak.onFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("resppp", error.toString());
                callbak.onFailed();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parm = new HashMap<String, String>();
                parm.put("UpdateID", updateId + "");
                return parm;
            }
        };
        Q.add(request);
    }
}

interface OrderUpdateCallback {
    void onSuccess(List<PURCHASE_UPDATE_CLASS> u);

    void onFailed();
}

interface OrderUpdateAttachmentCallbak {
    void onSuccess(List<ImportUpdateAttachment> u);

    void onFailed();
}