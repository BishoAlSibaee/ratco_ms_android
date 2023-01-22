package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.SalaryReport_Adapter;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySalaryReports extends AppCompatActivity {

    Activity act ;
    List<SalaryReport> SalaryReporst ;
    String getMyReportsUrl = MyApp.MainUrl +"getMySalaryReports.php";
    RecyclerView ReportsRecycler ;
    LinearLayoutManager Manager ;
    SalaryReport_Adapter Adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_my_salary_reports_activity);
        setActivity();
        getMySalaryReports();
    }

    void setActivity () {
        act = this ;
        SalaryReporst = new ArrayList<SalaryReport>();
        ReportsRecycler = (RecyclerView) findViewById(R.id.reports);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        ReportsRecycler.setLayoutManager(Manager);
    }

    void getMySalaryReports () {
        Loading l = new Loading(act); l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getMyReportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("reportsResp",response);
                if (!response.equals("0")) {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            SalaryReport r = new SalaryReport(row.getInt("id"),row.getInt("EmpID"),row.getInt("JobNumber"),row.getString("Link"),row.getInt("Month"));
                            SalaryReporst.add(r);
                        }
                        Adapter = new SalaryReport_Adapter(SalaryReporst);
                        ReportsRecycler.setAdapter(Adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"No Reports","No reports");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("reportsResp",error.toString());
                l.close();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"No Reports","No reports "+error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> par = new HashMap<String, String>();
                par.put("ID", String.valueOf(MyApp.db.getUser().id));
                par.put("JobNumber", String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }
}