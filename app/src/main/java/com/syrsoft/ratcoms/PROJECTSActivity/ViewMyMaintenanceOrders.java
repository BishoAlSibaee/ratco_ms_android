package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.MaintenanceOrders_Adapter;
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

public class ViewMyMaintenanceOrders extends AppCompatActivity {

    Activity act ;
    LinearLayout ByProjectLayout , ByDateLayout ;
    RadioButton ByProjectRB , ByDateRB , OpenRB , ClosedRB ;
    TextView StartDateTV , EndDateTV ;
    Spinner searchBySpinner , ProjectsResultSpinner ;
    EditText searchField ;
    String[] SearchByArr , resultArray ;
    ProgressBar p ;
    String searchProjectUrl = MyApp.MainUrl + "searchProject.php" ;
    String getOrdersByDateUrl = MyApp.MainUrl + "getMaintenanceOrdersByDateSalesman.php" ;
    String getOrdersByProjectUrl = MyApp.MainUrl + "getMaintenanceOrdersByProjectSalesman.php" ;
    List<PROJECT_CONTRACT_CLASS> ContractsResult ;
    RequestQueue Q ;
    String startDate , endDate ;
    PROJECT_CONTRACT_CLASS CONTRACT ;
    public static List<MAINTENANCE_ORDER_CLASS> ORDERS ;
    RecyclerView OrdersRecycler ;
    RecyclerView.LayoutManager Manager ;
    int OPENCLOSED = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_view_my_maintenance_orders_activity);
        setActivity();
    }

    void setActivity() {
        act = this ;
        Q = Volley.newRequestQueue(act);
        StartDateTV = (TextView) findViewById(R.id.MyContracts_StartDate);
        EndDateTV = (TextView) findViewById(R.id.MyContracts_EndDate);
        ByProjectRB = (RadioButton) findViewById(R.id.byCLientRB);
        ByDateRB = (RadioButton) findViewById(R.id.byDateRB);
        OpenRB = (RadioButton) findViewById(R.id.Open);
        ClosedRB = (RadioButton) findViewById(R.id.Closed);
        ByProjectLayout = (LinearLayout) findViewById(R.id.byProjectsLayout);
        ByDateLayout = (LinearLayout) findViewById(R.id.ByDateLayout);
        SearchByArr = getResources().getStringArray(R.array.searchProjectByArray);
        searchField = (EditText) findViewById(R.id.MyVisitsReports_searchWord);
        ArrayAdapter<String> SearchByAdapter = new ArrayAdapter<String>(act,R.layout.spinner_item,SearchByArr);
        searchBySpinner = (Spinner) findViewById(R.id.MyVisitsReports_searchBySpinner);
        ProjectsResultSpinner = (Spinner) findViewById(R.id.MyVisitsReports_searchResultSpinner);
        searchBySpinner.setAdapter(SearchByAdapter);
        ProgressBar P = (ProgressBar) findViewById(R.id.progressBar3);
        ContractsResult = new ArrayList<PROJECT_CONTRACT_CLASS>();
        p = (ProgressBar) findViewById(R.id.progressBar3);
        p.setVisibility(View.GONE);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (searchField.getText() != null && !searchField.getText().toString().isEmpty()) {
                    if (searchBySpinner.getSelectedItem() != null && !searchBySpinner.getSelectedItem().toString().isEmpty()) {
                        P.setVisibility(View.VISIBLE);
                        StringRequest request = new StringRequest(Request.Method.POST, searchProjectUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("searchProjectResp" , response);
                                P.setVisibility(View.GONE);
                                if (response.equals("0")) {

                                }
                                else if (response.equals("-1")) {

                                }
                                else {
                                    ContractsResult.clear();
                                    try {
                                        JSONArray arr = new JSONArray(response);
                                        String[] resArr = new String[arr.length()];
                                        for (int i=0;i<arr.length();i++) {
                                            JSONObject row = arr.getJSONObject(i);
                                            ContractsResult.add(new PROJECT_CONTRACT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ProjectName"),row.getString("Date"),row.getString("City"),row.getString("Address"),row.getDouble("LA"),row.getDouble("LO"),row.getString("ProjectDescription"),row.getString("ProjectManager"),row.getString("MobileNumber"),row.getInt("SalesMan"),row.getString("HandOverDate"),row.getString("WarrantyExpireDate"),row.getString("ContractLink"),row.getInt("Supplied"),row.getInt("Installed"),row.getInt("Handovered"),row.getString("SupplyDate"),row.getString("InstallDate"),row.getString("HandOverDate")));
                                            resArr[i] = row.getString("ProjectName");
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resArr);
                                        ProjectsResultSpinner.setAdapter(adapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("searchProjectResp" , e.getMessage());
                                    }

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                P.setVisibility(View.GONE);
                            }
                        })
                        {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> par = new HashMap<String, String>();
                                par.put("searchBy" , String.valueOf(searchBySpinner.getSelectedItemPosition()));
                                par.put("Field" , searchField.getText().toString());
                                par.put("SalesMan", String.valueOf(MyApp.db.getUser().JobNumber));
                                return par;
                            }
                        };
                        Q.add(request);
                    }
                    else {

                    }
                }
                else {

                }
            }
        });
        ByProjectLayout.setVisibility(View.GONE);
        ByDateLayout.setVisibility(View.GONE);
        ByProjectRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ByDateRB.setChecked(false);
                    ByProjectLayout.setVisibility(View.VISIBLE);
                    ByDateLayout.setVisibility(View.GONE);
                }
            }
        });
        ByDateRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ByProjectRB.setChecked(false);
                    ByDateLayout.setVisibility(View.VISIBLE);
                    ByProjectLayout.setVisibility(View.GONE);
                }
            }
        });
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p.setVisibility(View.VISIBLE);
                searchClient(searchBySpinner.getSelectedItemPosition(),searchField.getText().toString());

            }
        });
        StartDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        startDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(startDate);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartDateTV.setText(startDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        EndDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        endDate = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(endDate);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EndDateTV.setText(endDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        ProjectsResultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (ContractsResult != null) {
                    CONTRACT = ContractsResult.get(position);
                }
                else {
                    ToastMaker.Show(1,"search client first" ,act);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        OrdersRecycler = (RecyclerView) findViewById(R.id.MaintenanceOrders_Recycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        OrdersRecycler.setLayoutManager(Manager);
        ORDERS = new ArrayList<MAINTENANCE_ORDER_CLASS>();
        OpenRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ClosedRB.setChecked(false);
                    OPENCLOSED = 0 ;
                }
            }
        });
        ClosedRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OpenRB.setChecked(false);
                    OPENCLOSED = 1 ;
                }
            }
        });
        OpenRB.setChecked(true);
        ClosedRB.setChecked(false);
    }

    void searchClient (int searchBy , String field) {

        if (field.isEmpty()) {
            resultArray = new String[]{""};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resultArray);
            ProjectsResultSpinner.setAdapter(adapter);
            p.setVisibility(View.GONE);
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST,searchProjectUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                p.setVisibility(View.GONE);
                if (response.equals("0")) {
                    ToastMaker.Show(1,"no results",act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"error",act);
                }
                else {
                    try {
                        ContractsResult.clear();
                        JSONArray arr = new JSONArray(response);
                        resultArray = new String[arr.length()];
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ContractsResult.add(new PROJECT_CONTRACT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ProjectName"),row.getString("Date"),row.getString("City"),row.getString("Address"),row.getDouble("LA"),row.getDouble("LO"),row.getString("ProjectDescription"),row.getString("ProjectManager"),row.getString("MobileNumber"),row.getInt("SalesMan"),row.getString("HandOverDate"),row.getString("WarrantyExpireDate"),row.getString("ContractLink"),row.getInt("Supplied"),row.getInt("Installed"),row.getInt("Handovered"),row.getString("SupplyDate"),row.getString("InstallDate"),row.getString("HandOverDate")));
                            resultArray[i] = row.getString("ProjectName") ;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resultArray);
                    ProjectsResultSpinner.setAdapter(adapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.setVisibility(View.GONE);
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
                par.put("searchBy" , String.valueOf( searchBy ));
                par.put("Field" , field) ;
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void goSearch(View view) {

        if (ByDateRB.isChecked() && !ByProjectRB.isChecked()) {
            searchMaintenanceOrder(1);
        }
        else if (!ByDateRB.isChecked() && ByProjectRB.isChecked()) {
            searchMaintenanceOrder(2);
        }
    }

    void searchMaintenanceOrder(int By) {

        if (By == 1) {
            if (StartDateTV.getText() == null || StartDateTV.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"Select Start Date",act);
                StartDateTV.setHintTextColor(Color.RED);
                return;
            }
            if (EndDateTV.getText() == null || EndDateTV.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"Select End Date",act);
                EndDateTV.setHintTextColor(Color.RED);
                return;
            }
            ORDERS.clear();
            MaintenanceOrders_Adapter adapter = new MaintenanceOrders_Adapter(ORDERS);
            OrdersRecycler.setAdapter(adapter);
            Loading l = new Loading(act);
            l.show();
            StringRequest request = new StringRequest(Request.Method.POST, getOrdersByDateUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    l.close();
                    if (response.equals("0")) {
                        ToastMaker.Show(1,"No Records" ,act);
                    }
                    else if (response.equals("-1")) {

                    }
                    else {
                        try {
                            JSONArray arr = new JSONArray(response);
                            ORDERS.clear();
                            for (int i=0;i<arr.length();i++) {
                                JSONObject row = arr.getJSONObject(i);
                                ORDERS.add(new MAINTENANCE_ORDER_CLASS(row.getInt("id"),row.getInt("ProjectID"),row.getString("ProjectName"),row.getInt("ClientID"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("DamageDesc"),row.getString("Notes"),row.getString("Contact"),row.getString("ContactName"),row.getString("Date"),row.getInt("ToMaintenance"),row.getInt("ToAluminum"),row.getInt("ToDoors"),row.getInt("ForwardedTo"),row.getString("Response"),row.getInt("Status")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MaintenanceOrders_Adapter adapter = new MaintenanceOrders_Adapter(ORDERS);
                        OrdersRecycler.setAdapter(adapter);
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
                    par.put("Start" , StartDateTV.getText().toString() );
                    par.put("End" , EndDateTV.getText().toString()) ;
                    par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                    par.put("Status" , String.valueOf(OPENCLOSED));
                    return par;
                }
            };
            Q.add(request);
        }
        else if (By == 2){

            if (CONTRACT == null ){
                ToastMaker.Show(1,"please select project ",act);
                return;
            }
            ORDERS.clear();
            MaintenanceOrders_Adapter adapter = new MaintenanceOrders_Adapter(ORDERS);
            OrdersRecycler.setAdapter(adapter);
            Loading l = new Loading(act);
            l.show();
            StringRequest request = new StringRequest(Request.Method.POST, getOrdersByProjectUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    l.close();
                    if (response.equals("0")) {
                        ToastMaker.Show(1,"No Records" ,act);
                    }
                    else if (response.equals("-1")) {

                    }
                    else {
                        try {
                            JSONArray arr = new JSONArray(response);
                            ORDERS.clear();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject row = arr.getJSONObject(i);
                                ORDERS.add(new MAINTENANCE_ORDER_CLASS(row.getInt("id"), row.getInt("ProjectID"), row.getString("ProjectName"), row.getInt("ClientID"), row.getInt("SalesMan"), row.getDouble("LA"), row.getDouble("LO"), row.getString("DamageDesc"), row.getString("Notes"), row.getString("Contact"), row.getString("ContactName"), row.getString("Date"), row.getInt("ToMaintenance"), row.getInt("ToAluminum"), row.getInt("ToDoors"), row.getInt("ForwardedTo"), row.getString("Response"), row.getInt("Status")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MaintenanceOrders_Adapter adapter = new MaintenanceOrders_Adapter(ORDERS);
                        OrdersRecycler.setAdapter(adapter);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> par = new HashMap<String, String>();
                    par.put("PID" , String.valueOf(CONTRACT.id));
                    par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                    par.put("Status" , String.valueOf(OPENCLOSED));
                    return par;
                }
            };
            Q.add(request);
        }
    }
}