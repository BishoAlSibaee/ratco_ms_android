package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.ClientVisitReport_Adapter;
import com.syrsoft.ratcoms.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyVisitReports extends AppCompatActivity {

    RecyclerView visitsRecycler ;
    public static List<CLIENT_VISIT_CLASS> Visits ;
    Activity act ;
    String getVisitReportsUrl = "https://ratco-solutions.com/RatcoManagementSystem/getMyVisitReports.php" ;
    String getMyVisitReportsByClientUrl = MyApp.MainUrl+"getMyVisitReportsByClient.php";
    String searchClientUrl = "https://ratco-solutions.com/RatcoManagementSystem/searchClient.php" ;
    ClientVisitReport_Adapter Adapter ;
    RecyclerView.LayoutManager Manager ;
    CalendarView calenderView ;
    String Date ;
    TextView SelectedDate ;
    RadioButton ByDate , ByClient ;
    LinearLayout ClientLayout , DateLayout ;
    EditText field ;
    String[] resultArray ;
    String[] searchByArray ;
    Spinner searchBySpinner , ClientsResultSpinner ;
    ProgressBar p ;
    RequestQueue VolleyQ ;
    List<CLIENT_CLASS> THE_Result_CLIENTS ; //getStaffClientVisitReports.php

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_visit_reports_activity);
        setActivity();

    }

    void setActivity() {
        act = this ;
        VolleyQ = Volley.newRequestQueue(act);
        THE_Result_CLIENTS = new ArrayList<CLIENT_CLASS>();
        calenderView = (CalendarView) findViewById(R.id.calendarView3);
        visitsRecycler = (RecyclerView) findViewById(R.id.Contracts_Recycler);
        SelectedDate = (TextView) findViewById(R.id.MyVisitReports_Date);
        ByDate = (RadioButton) findViewById(R.id.byDateRB);
        ByClient = (RadioButton) findViewById(R.id.byCLientRB);
        ClientLayout = (LinearLayout) findViewById(R.id.clientsLayout) ; ClientLayout.setVisibility(View.GONE);
        DateLayout = (LinearLayout) findViewById(R.id.sendToLayout); DateLayout.setVisibility(View.GONE);
        field = (EditText) findViewById(R.id.MyVisitsReports_searchWord) ;
        searchBySpinner = (Spinner) findViewById(R.id.MyVisitsReports_searchBySpinner);
        searchByArray = getResources().getStringArray(R.array.searchByArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,searchByArray);
        searchBySpinner.setAdapter(adapter);
        ClientsResultSpinner = (Spinner) findViewById(R.id.MyVisitsReports_searchResultSpinner);
        p = (ProgressBar) findViewById(R.id.progressBar3);
        p.setVisibility(View.GONE);
        ByDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ByClient.setChecked(false);
                    DateLayout.setVisibility(View.VISIBLE);
                    ClientLayout.setVisibility(View.GONE);
                }
            }
        });
        ByClient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ByDate.setChecked(false);
                    DateLayout.setVisibility(View.GONE);
                    ClientLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        Visits = new ArrayList<CLIENT_VISIT_CLASS>();
        Adapter = new ClientVisitReport_Adapter(Visits);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        visitsRecycler.setLayoutManager(Manager);
        //visitsRecycler.setNestedScrollingEnabled(false);
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date = year+"-"+(month+1)+"-"+dayOfMonth;
                SelectedDate.setText(Date);
            }
        });
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p.setVisibility(View.VISIBLE);
                searchClient(searchBySpinner.getSelectedItemPosition(),field.getText().toString());
            }
        });
    }

    void getMyClientVisitReportsByDate() {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getVisitReportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("myReportsResp" , response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records",act);
                }
                else  {
                    try {
                        JSONArray arr = new JSONArray(response);
                        Visits.clear();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            Visits.add(new CLIENT_VISIT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ClientName"),row.getInt("SalesMan"),row.getString("Date"),row.getString("Time"),row.getString("ProjectDescription"),row.getString("VisitDetails"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Address"),row.getString("Responsible"),row.getString("QuotationLink"),row.getString("LocationLink"),row.getString("FileLink"),row.getInt("Interested"),row.getString("FollowUpAt")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("myReportsResp" , e.getMessage());
                    }
                    Collections.reverse(Visits);
                }
                visitsRecycler.setAdapter(Adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,act.getResources().getString(R.string.notSavedErrorTitle),act.getResources().getString(R.string.notSavedErrorMessage));
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Date",Date);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void searchClient (int searchBy , String field) {

        if (field.isEmpty()) {
            resultArray = new String[]{""};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resultArray);
            ClientsResultSpinner.setAdapter(adapter);
            p.setVisibility(View.GONE);
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST,searchClientUrl , new Response.Listener<String>() {
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
                        THE_Result_CLIENTS.clear();
                        JSONArray arr = new JSONArray(response);
                        resultArray = new String[arr.length()];
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CLIENT_CLASS c = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                            THE_Result_CLIENTS.add(c);
                            resultArray[i] = c.ClientName ;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resultArray);
                    ClientsResultSpinner.setAdapter(adapter);
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
                par.put("SalesMan",String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        VolleyQ.add(request);
    }

    void getMyClientVisitReportsByClient() {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getMyVisitReportsByClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("myReportsResp" , response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records",act);
                }
                else  {
                    try {
                        JSONArray arr = new JSONArray(response);
                        Visits.clear();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            Visits.add(new CLIENT_VISIT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ClientName"),row.getInt("SalesMan"),row.getString("Date"),row.getString("Time"),row.getString("ProjectDescription"),row.getString("VisitDetails"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Address"),row.getString("Responsible"),row.getString("QuotationLink"),row.getString("LocationLink"),row.getString("FileLink"),row.getInt("Interested"),row.getString("FollowUpAt")));
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("myReportsResp" , e.getMessage());
                        }
                    Collections.reverse(Visits);
                }
                visitsRecycler.setAdapter(Adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,act.getResources().getString(R.string.notSavedErrorTitle),act.getResources().getString(R.string.notSavedErrorMessage));
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("ClientID", String.valueOf(THE_Result_CLIENTS.get(ClientsResultSpinner.getSelectedItemPosition()).id));
                return par;
            }
        };
        VolleyQ.add(request);

    }

    public void goSearch(View view) {

        if (!ByClient.isChecked() && !ByDate.isChecked()) {
            ToastMaker.Show(1,"please select Search Method",act);
            return;
        }
        else if (ByDate.isChecked() && !ByClient.isChecked() ) {

            if (Date == null || Date.isEmpty() ) {
                ToastMaker.Show(1,"please select date",act);
            }
            else {
                getMyClientVisitReportsByDate();
            }
        }
        else if ( !ByDate.isChecked() && ByClient.isChecked() ) {
            if (ClientsResultSpinner.getSelectedItem() != null ) {
                getMyClientVisitReportsByClient();
            }
            else {
                ToastMaker.Show(1,"Please Select Client",act);
            }
        }

    }
}