package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
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
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.ClientVisitReport_Adapter;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMyStaffVisitsReport extends AppCompatActivity {

    Activity act ;
    TextView startDateTV, endDateTV , selectedClientTV;
    String startDate , endDate ;
    Spinner myStaffSpinner ,searchBySpinner , ClientsResultSpinner ;
    RadioButton byClientRB, byAllClientsRB , InterestedRB , UnInterestedRB ;
    CLIENT_CLASS CLIENT ;
    USER SALESMAN ;
    EditText searchField ;
    String[] searchByArray , resultArray , myStaffArray ;
    List<CLIENT_CLASS> THE_Result_CLIENTS ;
    ProgressBar p ;
    Button cancel , select ,search ;
    String searchClientUrl = MyApp.MainUrl+"searchClient.php" ;
    String searchClientNoSalesmanUrl = MyApp.MainUrl+"searchClientNoSalesman.php";
    String getMyStaffUrl = MyApp.MainUrl+"getMyStaff.php" ;
    String getStaffReportsURL = MyApp.MainUrl+"getStaffClientVisitReports.php";
    List<USER> myStaffList ;
    RequestQueue Q ;
    List<CLIENT_VISIT_CLASS> VISITS ;
    ClientVisitReport_Adapter visitAdapters ;
    RecyclerView VISITS_RECYCLER ;
    RecyclerView.LayoutManager Manager ;
    int Interested = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_view_my_staff_visits_report_activity);
        setActivity();
        setActivityActions();
        getMyStaff();
    }

    void setActivity () {
        act = this ;
        Q = Volley.newRequestQueue(act);
        startDateTV = (TextView) findViewById(R.id.startDateTV);
        endDateTV = (TextView) findViewById(R.id.endDateTV);
        selectedClientTV = (TextView) findViewById(R.id.MyStaffReport_selectedClientTV);
        myStaffSpinner = (Spinner) findViewById(R.id.MyStaffReport_EmpSpinner);
        byClientRB = (RadioButton) findViewById(R.id.MyStaffReports_byClientRB);
        byAllClientsRB = (RadioButton) findViewById(R.id.MyStaffReports_byAllClientsRB);
        InterestedRB = (RadioButton) findViewById(R.id.MyStaffReports_interested);
        UnInterestedRB = (RadioButton) findViewById(R.id.MyStaffReports_unInterested);
        visitAdapters = new ClientVisitReport_Adapter(VISITS);
        VISITS_RECYCLER = (RecyclerView) findViewById(R.id.VisitsRecycler);
        search = (Button) findViewById(R.id.searchBtn);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        VISITS_RECYCLER.setLayoutManager(Manager);
        myStaffList = new ArrayList<USER>();
        THE_Result_CLIENTS = new ArrayList<CLIENT_CLASS>();
        VISITS = new ArrayList<CLIENT_VISIT_CLASS>();
    }

    void setActivityActions () {
        startDateTV.setOnClickListener(new View.OnClickListener() {
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
                        startDateTV.setText(startDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        endDateTV.setOnClickListener(new View.OnClickListener() {
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
                        endDateTV.setText(endDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        byClientRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    byAllClientsRB.setChecked(false);
                    Dialog D = new Dialog(act);
                    D.setContentView(R.layout.dialog_old_client_dialog);
                    D.setCancelable(false);
                    Window window = D.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    searchBySpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchBySpinner);
                    ClientsResultSpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchResultSpinner);
                    searchField = (EditText) D.findViewById(R.id.MyVisitsReports_searchWord);
                    p = (ProgressBar) D.findViewById(R.id.progressBar3);
                    p.setVisibility(View.GONE);
                    searchByArray = getResources().getStringArray(R.array.searchByArray);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,searchByArray);
                    searchBySpinner.setAdapter(adapter);
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
                    cancel = (Button) D.findViewById(R.id.oldClientsDialog_cancelBtn);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            D.dismiss();
                        }
                    });
                    select = (Button) D.findViewById(R.id.oldClientsDialog_selectBtn);
                    select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ClientsResultSpinner.getSelectedItem() == null ) {
                                ToastMaker.Show(1,getResources().getString(R.string.pleaseSelectClient),act);
                            }
                            else {
                                CLIENT = THE_Result_CLIENTS.get(ClientsResultSpinner.getSelectedItemPosition());
                                Log.d("selectedClient" , CLIENT.ClientName);
                                selectedClientTV.setText(CLIENT.ClientName);
                                D.dismiss();
                            }
                        }
                    });
                    D.show();
                }
            }
        });
        byAllClientsRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    byClientRB.setChecked(false);
                    CLIENT = null ;
                    selectedClientTV.setText("");
                }
            }
        });
        myStaffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SALESMAN = myStaffList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReports();
            }
        });
        InterestedRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UnInterestedRB.setChecked(false);
                    Interested = 1 ;
                }
            }
        });
        UnInterestedRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    InterestedRB.setChecked(false);
                    Interested = 0 ;
                }
            }
        });
    }

    void searchClient (int searchBy , String field) {

        if (field.isEmpty()) {
            resultArray = new String[]{""};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resultArray);
            ClientsResultSpinner.setAdapter(adapter);
            p.setVisibility(View.GONE);
            return;
        }
        String URL = "" ;
        if (SALESMAN == null ) {
            URL = searchClientNoSalesmanUrl ;
        }
        else {
            URL = searchClientUrl ;
        }
        String finalURL = URL;
        StringRequest request = new StringRequest(Request.Method.POST, finalURL, new Response.Listener<String>() {
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
                if (finalURL.equals(searchClientUrl)) {
                    par.put("SalesMan" , String.valueOf(SALESMAN.JobNumber));
                }
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getMyStaff() {
        Loading l = new Loading(act);
        l.show();
        String URL = "" ;
        if (MyApp.db.getUser().JobTitle.equals("Manager") || MyApp.db.getUser().JobTitle.equals("Sales Manager") || MyApp.db.getUser().JobTitle.equals("Programmer")) {
            URL = MyApp.MainUrl+"getSalesStaff.php" ;
        }
        else {
            URL = getMyStaffUrl ;
        }
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("myStaffResponse" , response );
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,getResources().getString(R.string.youHaveNoStaff),act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,getResources().getString(R.string.orderNotSent),act);
                }
                else {
                    List<Object> lis = JsonToObject.translate(response,USER.class,act);
                    if (lis.size()>0) {
                        myStaffArray = new String[lis.size()];
                        myStaffList.clear();
                        for (int i=0;i<lis.size();i++) {
                            USER r = (USER) lis.get(i);
                            myStaffArray[i] = r.FirstName+" "+r.LastName ;
                            myStaffList.add(r);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,myStaffArray);
                        myStaffSpinner.setAdapter(adapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("myStaffResponse" , error.toString() );
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Q.add(request);
    }

    void getReports() {

        if (MyApp.db.getUser().JobTitle.equals("SalesMan")) {
            ToastMaker.Show(1,"You Are Not Allowed To View Visit Reports",act);
            return;
        }
        if (startDate == null ) {
            ToastMaker.Show(1,"Select Start Date" ,act);
            return;
        }
        if (endDate == null ) {
            ToastMaker.Show(1,"Select End Date",act);
            return;
        }
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date ssDate = s1.parse(startDate);
            Date eeDate = s1.parse(endDate);
            if (ssDate.getTime() > eeDate.getTime()) {
                ToastMaker.Show(1,"End Date Must Be After Start Date",act);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (SALESMAN == null ) {
            ToastMaker.Show(1,"please select salesman",act);
            return;
        }
        VISITS.clear();
        visitAdapters = new ClientVisitReport_Adapter(VISITS);
        VISITS_RECYCLER.setAdapter(visitAdapters);
        Loading l = new Loading(act) ;
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getStaffReportsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("searchResponse" , response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" ,act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,getResources().getString(R.string.orderNotSent) ,act);
                }
                else {
                    try {
                            JSONArray arr = new JSONArray(response);
                            VISITS.clear();
                            for (int i=0;i<arr.length();i++) {
                                JSONObject row = arr.getJSONObject(i);
                                VISITS.add(new CLIENT_VISIT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ClientName"),row.getInt("SalesMan"),row.getString("Date"),row.getString("Time"),row.getString("ProjectDescription"),row.getString("VisitDetails"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Address"),row.getString("Responsible"),row.getString("QuotationLink"),row.getString("LocationLink"),row.getString("FileLink"),row.getInt("Interested"),row.getString("FollowUpAt")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("myReportsResp" , e.getMessage());
                        }
                        Collections.reverse(VISITS);
                        visitAdapters = new ClientVisitReport_Adapter(VISITS);
                        VISITS_RECYCLER.setAdapter(visitAdapters);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("searchResponse" , error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Start" , startDate ) ;
                par.put("End" , endDate ) ;

                if (SALESMAN != null ) {
                    par.put("SalesMan" , String.valueOf(SALESMAN.JobNumber)) ;
                }
                if (CLIENT != null ) {
                    par.put("ClientID" , String.valueOf(CLIENT.id)) ;
                }
                if (Interested != 2) {
                    par.put("Interested" , String.valueOf(Interested));
                }
                return par;
            }
        };
        Q.add(request);
    }
}