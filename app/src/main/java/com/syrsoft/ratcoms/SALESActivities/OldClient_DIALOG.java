package com.syrsoft.ratcoms.SALESActivities;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldClient_DIALOG {

    Dialog D ;
    Context C ;
    Spinner searchBySpinner , ClientsResultSpinner ;
    EditText searchField ;
    String[] searchByArray ;
    Button cancel , select ;
    String searchClientUrl = "https://ratco-solutions.com/RatcoManagementSystem/searchClient.php" ;
    String getInChargesUrl = "https://ratco-solutions.com/RatcoManagementSystem/getInCharges.php" ;
    String[] resultArray ;
    ProgressBar p ;


    OldClient_DIALOG(Context C) {
        this.C = C ;
        D = new Dialog(this.C);
        D.setContentView(R.layout.dialog_old_client_dialog);
        D.setCancelable(false);
        Window window = D.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        searchBySpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchBySpinner);
        ClientsResultSpinner = (Spinner) D.findViewById(R.id.MyVisitsReports_searchResultSpinner);
        searchField = (EditText) D.findViewById(R.id.MyVisitsReports_searchWord);
        p = (ProgressBar) D.findViewById(R.id.progressBar3);
        p.setVisibility(View.GONE);
        searchByArray = C.getResources().getStringArray(R.array.searchByArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(C,R.layout.spinner_item,searchByArray);
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
                    ToastMaker.Show(0,"select client first",C);
                    return;
                }
                ClientVisitReport.THE_CLIENT = ClientVisitReport.THE_Result_CLIENTS.get(ClientsResultSpinner.getSelectedItemPosition());
                ClientVisitReport.ClientNameTextView.setText(ClientVisitReport.THE_CLIENT.ClientName);
                getInCharges();
                D.dismiss();
            }
        });
    }

    void show () {
        D.show();
    }

    void close () {
        D.dismiss();
    }

    void searchClient (int searchBy , String field) {

        if (field.isEmpty()) {
            resultArray = new String[]{""};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(C,R.layout.spinner_item,resultArray);
            ClientsResultSpinner.setAdapter(adapter);
            p.setVisibility(View.GONE);
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST,searchClientUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                p.setVisibility(View.GONE);
                if (response.equals("0")) {
                    ToastMaker.Show(1,"no results",C);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"error",C);
                }
                else {
                    try {
                        ClientVisitReport.THE_Result_CLIENTS.clear();
                        JSONArray arr = new JSONArray(response);
                        resultArray = new String[arr.length()];
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CLIENT_CLASS c = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                            ClientVisitReport.THE_Result_CLIENTS.add(c);
                            resultArray[i] = c.ClientName ;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(C,R.layout.spinner_item,resultArray);
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
        Volley.newRequestQueue(C).add(request);
    }

    void getInCharges() {
        Loading l = new Loading(C);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getInChargesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {

                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        ClientVisitReport.Responsible.clear();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            RESPONSIBLE_CLASS r = new RESPONSIBLE_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("Name"),row.getString("JobTitle"),row.getString("MobileNumber"),row.getString("Email"),row.getString("Link"));
                            ClientVisitReport.Responsible.add(r);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ClientVisitReport.THE_CLIENT.setResponsibles(ClientVisitReport.Responsible);
                    ClientVisitReport.ResponsibleTextView.setText(ClientVisitReport.THE_CLIENT.getResponsibles().get(0).Name);
                    ClientVisitReport.ResponsibleJobTextView.setText(ClientVisitReport.THE_CLIENT.getResponsibles().get(0).JobTitle);
                    ClientVisitReport.ResponsibleMobileTextView.setText(ClientVisitReport.THE_CLIENT.getResponsibles().get(0).MobileNumber);
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
                par.put("ClientID" , String.valueOf(ClientVisitReport.THE_CLIENT.id));
                return par;
            }
        };
        Volley.newRequestQueue(C).add(request);
    }
}
