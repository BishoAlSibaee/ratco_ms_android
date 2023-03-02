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
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.Contracts_Adapter;
import com.syrsoft.ratcoms.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMySalesProjectContracts extends AppCompatActivity {
    Activity act ;
    LinearLayout ByClientLayout , ByDateLayout ;
    RadioButton ByClientRB , ByDateRB ;
    TextView StartDateTV , EndDateTV ;
    Spinner searchBySpinner , ClientsResultSpinner ;
    EditText searchField ;
    String[] searchByArray , resultArray ;
    ProgressBar p ;
    String searchClientUrl = MyApp.MainUrl+"searchClient.php" ;
    String getClientUrl = MyApp.MainUrl+"getClient.php";
    List<CLIENT_CLASS> THE_Result_CLIENTS ;
    String startDate , endDate ;
    CLIENT_CLASS CLIENT ;
    String getContractsByClientUrl = MyApp.MainUrl+"getMyContractsByClient.php" ;
    String getContractsByDateUrl = MyApp.MainUrl+"getMyContractsByDate.php";
    RequestQueue Q ;
    RecyclerView ContractsRecycler ;
    RecyclerView.LayoutManager Manager ;
    Contracts_Adapter Adapter ;
    public static List<PROJECT_CONTRACT_CLASS> ContractsList ;
    List<CLIENT_CLASS> ClientsList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_view_my_sales_project_contracts_activity);
        setActivity();
        setActivityActions();
    }

    void setActivity () {
        act = this;
        ByClientLayout = (LinearLayout) findViewById(R.id.clientsLayout);
        ByDateLayout = (LinearLayout) findViewById(R.id.ByDateLayout);
        ByClientRB = (RadioButton) findViewById(R.id.byCLientRB);
        ByDateRB = (RadioButton) findViewById(R.id.byDateRB);
        StartDateTV = (TextView) findViewById(R.id.MyContracts_StartDate);
        EndDateTV = (TextView) findViewById(R.id.MyContracts_EndDate);
        searchBySpinner = (Spinner) findViewById(R.id.MyVisitsReports_searchBySpinner);
        ClientsResultSpinner = (Spinner) findViewById(R.id.MyVisitsReports_searchResultSpinner);
        searchByArray = getResources().getStringArray(R.array.searchByArray);
        searchField = (EditText) findViewById(R.id.MyVisitsReports_searchWord);
        p = (ProgressBar) findViewById(R.id.progressBar3);
        p.setVisibility(View.GONE);
        THE_Result_CLIENTS = new ArrayList<CLIENT_CLASS>();
        Q = Volley.newRequestQueue(act);
        ContractsRecycler = (RecyclerView) findViewById(R.id.Contracts_Recycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        ContractsList = new ArrayList<PROJECT_CONTRACT_CLASS>();
        ClientsList = new ArrayList<CLIENT_CLASS>();
    }

    void setActivityActions () {
        ByClientLayout.setVisibility(View.GONE);
        ByDateLayout.setVisibility(View.GONE);
        ByClientRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ByDateRB.setChecked(false);
                    ByClientLayout.setVisibility(View.VISIBLE);
                    ByDateLayout.setVisibility(View.GONE);
                }
            }
        });
        ByDateRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ByClientRB.setChecked(false);
                    ByDateLayout.setVisibility(View.VISIBLE);
                    ByClientLayout.setVisibility(View.GONE);
                }
            }
        });
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
        ClientsResultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (THE_Result_CLIENTS != null) {
                    CLIENT = THE_Result_CLIENTS.get(position);
                }
                else {
                    ToastMaker.Show(1,"search client first" ,act);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ContractsRecycler.setLayoutManager(Manager);
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
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void goSearch(View view) {

        if (ByClientRB.isChecked() && !ByDateRB.isChecked()) {
            if (CLIENT == null) {
                ToastMaker.Show(1,"select client first" ,act);
            }
            else {
                getContractsByClient();
            }
        }

        else if (!ByClientRB.isChecked() && ByDateRB.isChecked()) {

            if (StartDateTV.getText() == null || StartDateTV.getText().toString().isEmpty()) {
                ToastMaker.Show(0,"please "+getResources().getString(R.string.startDate),act);
                return;
            }
            if (EndDateTV.getText() == null || EndDateTV.getText().toString().isEmpty()) {
                ToastMaker.Show(0,"please "+getResources().getString(R.string.EndDate),act);
                return;
            }
            getContractsByDate();
        }
    }

    void getContractsByClient() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getContractsByClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("contractsRespclient" , response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" , act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"Error" , act);
                }
                else {
                    try {
                        ContractsList.clear();
                        ClientsList.clear();
                        JSONArray arr = new JSONArray(response);
                        for ( int i = 0 ; i < arr.length() ; i++ ) {
                            JSONObject row = arr.getJSONObject(i);
                            ContractsList.add(new PROJECT_CONTRACT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ProjectName"),row.getString("Date"),row.getString("City"),row.getString("Address"),row.getDouble("LA"),row.getDouble("LO"),row.getString("ProjectDescription"),row.getString("ProjectManager"),row.getString("MobileNumber"),row.getInt("SalesMan"),row.getString("HandOverDate"),row.getString("WarrantyExpireDate"),row.getString("ContractLink"),row.getInt("Supplied"),row.getInt("Installed"),row.getInt("Handovered"),row.getString("SupplyDate"),row.getString("InstallDate"),row.getString("HandOverDate")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(PROJECT_CONTRACT_CLASS c : ContractsList) {
                        getClient(c.ClientID);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("contractsRespclient" , error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
                par.put("ClientID" , String.valueOf( CLIENT.id ));
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Q.add(request);
    }

    void getClient(int ID ) {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xxxxxx" ,response);
                l.close();
                if (response.equals("0")) {
                    Log.d("xxxxxx" ,"0");
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        ClientsList.add( new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xxxxxx" ,e.getMessage());
                    }
                    Log.d("xxxxxx" , ContractsList.size()+" "+ClientsList.size());
                    if (ClientsList.size() == ContractsList.size()) {
                        for (int i=0;i<ClientsList.size();i++) {
                            ContractsList.get(i).setCLIENT(ClientsList.get(i));
                        }
                        Adapter = new Contracts_Adapter(ContractsList) ;
                        ContractsRecycler.setAdapter(Adapter);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("xxxxxx" ,error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String> par = new HashMap<String, String>();
                par.put("ID", String.valueOf(ID) );
                return par;
            }
        };
        Q.add(request);
    }

    void getContractsByDate() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getContractsByDateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("contractsRespdate" , response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Records" , act);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"Error" , act);
                }
                else {
                    try {
                        ContractsList.clear();
                        ClientsList.clear();
                        JSONArray arr = new JSONArray(response);
                        for ( int i = 0 ; i < arr.length() ; i++ ) {
                            JSONObject row = arr.getJSONObject(i);
                            ContractsList.add(new PROJECT_CONTRACT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ProjectName"),row.getString("Date"),row.getString("City"),row.getString("Address"),row.getDouble("LA"),row.getDouble("LO"),row.getString("ProjectDescription"),row.getString("ProjectManager"),row.getString("MobileNumber"),row.getInt("SalesMan"),row.getString("HandOverDate"),row.getString("WarrantyExpireDate"),row.getString("ContractLink"),row.getInt("Supplied"),row.getInt("Installed"),row.getInt("Handovered"),row.getString("SupplyDate"),row.getString("InstallDate"),row.getString("HandOverDate")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("contractsRespdate" , response);
                    }
                    for(PROJECT_CONTRACT_CLASS c : ContractsList) {
                        getClient(c.ClientID);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("contractsRespdate" , error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
                par.put("Start" , StartDateTV.getText().toString());
                par.put("End" , EndDateTV.getText().toString());
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Q.add(request);
    }

}