package com.syrsoft.ratcoms.Management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.JobTitle;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SearchEmployeeDialog;
import com.syrsoft.ratcoms.USER;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewEmployee extends AppCompatActivity {

    Activity act ;
    EditText JN , FN , LN ,EM , MB , ID , PS ;
    TextView DM , DEPM ;
    Spinner JT ;
    USER DirectManager , DepartmentManager ;
    JobTitle jobTitle ;
    String saveNewEmployeeUrl = MyApp.MainUrl + "addEmployeeApp.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_add_new_account_activity);
        setActivity();
        setJobTitlesSpinner();
    }

    void setActivity() {
        act = this ;
        JN = findViewById(R.id.editTextNumber2);
        FN = findViewById(R.id.editTextNumber21);
        LN = findViewById(R.id.editTextNumber22);
        JT = findViewById(R.id.editTextNumber23);
        DM = findViewById(R.id.textView146);
        DEPM = findViewById(R.id.textView146v);
        EM = findViewById(R.id.editTextNumber26);
        MB = findViewById(R.id.editTextNumber28);
        ID = findViewById(R.id.editTextNumber29);
        PS = findViewById(R.id.editTextNumber210);
    }

    void setJobTitlesSpinner() {
        if (MyApp.JobTitles != null && MyApp.JobTitles.size() > 0) {
            List<JobTitle> temp = new ArrayList<>();
            for (int i=0;i<MyApp.JobTitles.size();i++) {
                if (!MyApp.JobTitles.get(i).JobTitle.equals("Direct Manager") && !MyApp.JobTitles.get(i).JobTitle.equals("Department Manager")) {
                    temp.add(MyApp.JobTitles.get(i));
                }
            }
            String[] ttls = new String[temp.size()];
            for (int i=0;i<temp.size();i++) {
                ttls[i] = temp.get(i).ArabicName ;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,ttls);
            JT.setAdapter(adapter);
            JT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    jobTitle = MyApp.JobTitles.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void goToSearchUserDepartmentManager(View view) {
        SearchEmployeeDialog D = new SearchEmployeeDialog(act);
        D.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DirectManager != null) {
                    if ( D.adapter.user.Department.equals(DirectManager.Department)) {
                        DepartmentManager = D.adapter.user ;
                        DEPM.setText(DepartmentManager.FirstName+" "+DepartmentManager.LastName);
                        D.stop();
                    }
                    else {
                        new MESSAGE_DIALOG(act,getResources().getString(R.string.notInTheSameDepartmentTitle),getResources().getString(R.string.thisEmployeeIsNotInTheSameSelectedDepartmentMessage));
                    }
                }
                else {
                    DepartmentManager = D.adapter.user ;
                    DEPM.setText(DepartmentManager.FirstName+" "+DepartmentManager.LastName);
                    D.stop();
                }

            }
        });
        D.show();
    }

    public void goToSearchUserDirectManager(View view) {
        SearchEmployeeDialog D = new SearchEmployeeDialog(act);
        D.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DepartmentManager != null ) {
                    if (D.adapter.user.Department.equals(DepartmentManager.Department)) {
                        DirectManager = D.adapter.user ;
                        DM.setText(DirectManager.FirstName+" "+DirectManager.LastName);
                        D.stop();
                    }
                    else {
                        new MESSAGE_DIALOG(act,getResources().getString(R.string.notInTheSameDepartmentTitle),getResources().getString(R.string.thisEmployeeIsNotInTheSameSelectedDepartmentMessage));
                    }
                }
                else {
                    DirectManager = D.adapter.user ;
                    DM.setText(DirectManager.FirstName+" "+DirectManager.LastName);
                    D.stop();
                }

            }
        });
        D.show();
    }

    public void savNewEmployee(View view) {
        if (JN.getText() == null || JN.getText().toString().isEmpty()) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.enterJobNumber),getResources().getString(R.string.enterJobNumber));
            return;
        }
        if (FN.getText() == null || FN.getText().toString().isEmpty()) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.enterFirstName),getResources().getString(R.string.enterFirstName));
            return;
        }
        if (LN.getText() == null || LN.getText().toString().isEmpty()) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.enterLastName),getResources().getString(R.string.enterLastName));
            return;
        }
        if (jobTitle == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.pleaseSelectJobTitle),getResources().getString(R.string.pleaseSelectJobTitle));
            return;
        }
        if (DirectManager == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectDirectManager),getResources().getString(R.string.selectDirectManager));
            return;
        }
        if (DepartmentManager == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectDepartmentManager),getResources().getString(R.string.selectDepartmentManager));
            return;
        }
        Loading l = new Loading(act); l.show();
        StringRequest req = new StringRequest(Request.Method.POST,saveNewEmployeeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saveEmpResp",response);
                l.close();
                if (response.equals("1")) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    JN.setText("");
                    FN.setText("");
                    LN.setText("");
                    DM.setText("");
                    DEPM.setText("");
                    DepartmentManager = null ;
                    DirectManager = null ;
                }
                else {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MESSAGE_DIALOG(act,"error","error "+error);
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<>();
                par.put("JobNumber",JN.getText().toString());
                par.put("FirstName",FN.getText().toString());
                par.put("LastName",LN.getText().toString());
                par.put("JobTitle",jobTitle.JobTitle);
                par.put("Department" , DepartmentManager.Department);
                par.put("DirectManager" , String.valueOf(DirectManager.JobNumber));
                par.put("DepartmentManager", String.valueOf(DepartmentManager.JobNumber));
                if (EM.getText() == null || EM.getText().toString().isEmpty()) {
                    par.put("Email","");
                }
                else {
                    par.put("Email",EM.getText().toString());
                }
                if (MB.getText() == null || MB.getText().toString().isEmpty()) {
                    par.put("Mobile","");
                }
                else {
                    par.put("Mobile",MB.getText().toString());
                }
                if (ID.getText() == null || ID.getText().toString().isEmpty()) {
                    par.put("ID","");
                }
                else {
                    par.put("ID",ID.getText().toString());
                }
                if (PS.getText() == null || PS.getText().toString().isEmpty()) {
                    par.put("Passport","");
                }
                else {
                    par.put("Passport",PS.getText().toString());
                }
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }
}