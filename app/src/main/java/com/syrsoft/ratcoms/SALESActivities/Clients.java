package com.syrsoft.ratcoms.SALESActivities;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.ClientsAdapter;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clients extends AppCompatActivity {

    Activity act ;
    List<CLIENT_CLASS> CLIENTS ;
    List<RESPONSIBLE_CLASS> RESPONSIBLES ;
    Spinner SearchBySpinner ;
    EditText Field ;
    ProgressBar P ;
    RecyclerView RESULTS_RECYCLER ;
    RecyclerView.LayoutManager Manager ;
    ClientsAdapter Adapter ;
    String searchClientUrl = MyApp.MainUrl+"searchClient.php" ;
    String[] searchByArray ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_clients_activity);
        setActivity();
        setActivityActions();
    }

    void setActivity() {
        act = this;
        CLIENTS = new ArrayList<CLIENT_CLASS>();
        RESPONSIBLES = new ArrayList<RESPONSIBLE_CLASS>();
        SearchBySpinner = (Spinner) findViewById(R.id.MyVisitsReports_searchBySpinner);
        searchByArray = getResources().getStringArray(R.array.searchByArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,searchByArray);
        SearchBySpinner.setAdapter(adapter);
        Field = (EditText) findViewById(R.id.MyVisitsReports_searchWord);
        P = (ProgressBar) findViewById(R.id.progressBar3);
        P.setVisibility(View.GONE);
        RESULTS_RECYCLER = (RecyclerView) findViewById(R.id.results_recycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        RESULTS_RECYCLER.setLayoutManager(Manager);
        Adapter = new ClientsAdapter(CLIENTS);
    }

    void setActivityActions () {
        Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                P.setVisibility(View.VISIBLE);
                searchClient(SearchBySpinner.getSelectedItemPosition(),Field.getText().toString());
            }
        });
    }

    void searchClient (int searchBy , String field) {
        CLIENTS.clear();
        Adapter = new ClientsAdapter(CLIENTS);
        RESULTS_RECYCLER.setAdapter(Adapter);

        if (field.isEmpty()) {
            CLIENTS.clear();
            Adapter = new ClientsAdapter(CLIENTS);
            RESULTS_RECYCLER.setAdapter(Adapter);
            P.setVisibility(View.GONE);
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST,searchClientUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("searchRes" , response);
                P.setVisibility(View.GONE);
                if (response.equals("0")) {
                    ToastMaker.Show(1,"no results",act);
                    CLIENTS.clear();
                    Adapter = new ClientsAdapter(CLIENTS);
                    RESULTS_RECYCLER.setAdapter(Adapter);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"error",act);
                }
                else {
                    try {
                        CLIENTS.clear();
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CLIENT_CLASS c = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                            CLIENTS.add(c);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Adapter = new ClientsAdapter(CLIENTS);
                    RESULTS_RECYCLER.setAdapter(Adapter);
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
                par.put("searchBy" , String.valueOf( searchBy ));
                par.put("Field" , field) ;
                par.put("SalesMan" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

}