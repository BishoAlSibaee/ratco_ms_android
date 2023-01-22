package com.syrsoft.ratcoms.HRActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.R ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageAuthorizedTitles extends AppCompatActivity {

    ListView theList ;
    String getAuthorizedEmpsUrl = "https://ratco-solutions.com/RatcoManagementSystem/getAuthorizedEmps.php" ;
    String getJobTitlesUrl = "https://ratco-solutions.com/RatcoManagementSystem/getAllJobTitles.php" ;
    String saveUrl = "https://ratco-solutions.com/RatcoManagementSystem/updateAuthorizedJobTitles.php" ;
    List<Authrized_Emp> titlesList ;
    Activity act ;
    List<JobTitle> JobTitles ;
    String[] titles , authorizeds ;
    JobTitle selectedJobTitle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_authorized_titles);
        setActivity();
        getAuthorizedTitles();
        getJobTitles();
    }

    void setActivity() {
        act = this;
        theList = (ListView) findViewById(R.id.authorisedJobtitleList);
        JobTitles = new ArrayList<JobTitle>();
        titlesList = new ArrayList<Authrized_Emp>();
    }

    void getAuthorizedTitles() {
        Loading l = new Loading(act) ; l.show();
        StringRequest request = new StringRequest(Request.Method.POST,getAuthorizedEmpsUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("authoriedEmpsResponse" , response );
                if (response.equals("0")) {

                }
                else {
                    List<Object> list = JsonToObject.translate(response,Authrized_Emp.class,act);
                    authorizeds = new String[list.size()];
                    for (int i=0;i<list.size();i++) {
                        titlesList.add((Authrized_Emp) list.get(i)) ;
                        authorizeds[i] = titlesList.get(i).AuthorizationName+ " " +titlesList.get(i).ArabicName ;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,authorizeds);
                    theList.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        });
        Volley.newRequestQueue(act).add(request);
    }

    void getJobTitles () {
        StringRequest request = new StringRequest(Request.Method.POST,getJobTitlesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jobtitlesResponse" , response );
                if (response.equals("0")) {

                }
                else {
                    List<Object> list = JsonToObject.translate(response,JobTitle.class,act);
                    titles = new String[list.size()] ;
                    for (int i=0;i<list.size();i++ ) {
                        JobTitles.add((JobTitle) list.get(i));
                        titles[i] = JobTitles.get(i).JobTitle +" "+JobTitles.get(i).ArabicName ;
                    }
                    Log.d("jobtitlesResponse" , titles.length+" "+JobTitles.size() );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(act).add(request);
    }

    public void openAddJobtitleDialog(View view) {

        Dialog d = new Dialog(act);
        d.setContentView(R.layout.add_jobtitle_dialog);
        ListView jobTitles = (ListView) d.findViewById(R.id.jobtitles_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,titles);
        jobTitles.setAdapter(adapter);
        jobTitles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedJobTitle = JobTitles.get(position);
                view.setBackgroundColor(Color.LTGRAY);
            }
        });
        Button cancel = (Button) d.findViewById(R.id.button12);
        Button ok = (Button) d.findViewById(R.id.button11);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedJobTitle != null ) {
                    boolean status = false ;
                    for (int j=0;j<titlesList.size();j++) {
                        if (titlesList.get(j).JobTitleId == selectedJobTitle.id ) {
                            status = true ;
                        }
                    }
                    if (!status) {
                        titlesList.add(new Authrized_Emp(0,selectedJobTitle.id,selectedJobTitle.JobTitle,selectedJobTitle.ArabicName));
                        authorizeds = new String[titlesList.size()];
                        for (int i=0 ; i<titlesList.size();i++) {
                            authorizeds[i] = titlesList.get(i).AuthorizationName+" "+titlesList.get(i).ArabicName ;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,authorizeds);
                        theList.setAdapter(adapter);
                    }
                    else {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Exists","this jobtitle already exists");
                    }
                }

            }
        });
        d.show();
    }

    public void save(View view) {

        Loading l = new Loading(act); l.show();
        StringRequest request = new StringRequest(Request.Method.POST,saveUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("saveResult" , response) ;
                if (response.equals("0")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Nothing Changed ","Nothing Chenged in the list",1);
                }
                else if (response.equals("1")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"List Updated","The List Has Updated Successfully",1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("saveResult" , error.getMessage()) ;
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("count" , String.valueOf(titlesList.size()));
                for (int i=0;i<titlesList.size();i++) {
                    par.put("jId"+i,String.valueOf(titlesList.get(i).JobTitleId));
                    par.put("name"+i,titlesList.get(i).AuthorizationName);
                    par.put("aName"+i,titlesList.get(i).ArabicName);
                }
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);

    }
}