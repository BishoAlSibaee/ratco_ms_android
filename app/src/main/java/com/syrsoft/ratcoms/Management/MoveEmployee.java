package com.syrsoft.ratcoms.Management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SearchEmployeeDialog;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.User_Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveEmployee extends AppCompatActivity {

    Activity act ;
    TextView EmployeeTV ,CurrentDirectManagerTV , CurrentDepartmentManagerTV , NewDirectManagerTV;
    USER SELECTED_USER , CURRENT_DIRECT_MANAGER , CURRENT_DEPARTMENT_MANAGER ;
    USER NEW_DEPARTMENT_MANAGER , NEW_DIRECT_MANAGER ;
    List<USER> DepartmentManagers ;
    Spinner DepartmentManagersSpinner ;
    String saveMoveEmployeeUrl = MyApp.MainUrl+"moveEmployee.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_move_employee_activity);
        setActivity();
        setActivityActions();
        getDepartmentManagers();
    }

    void setActivity() {
        act = this ;
        DepartmentManagers = new ArrayList<>();
        EmployeeTV = findViewById(R.id.textView146);
        NewDirectManagerTV = findViewById(R.id.textView1463);
        CurrentDirectManagerTV = findViewById(R.id.textView149);
        CurrentDepartmentManagerTV = findViewById(R.id.textView150);
        DepartmentManagersSpinner = findViewById(R.id.spinner6);
    }

    void setActivityActions() {
        DepartmentManagersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NEW_DEPARTMENT_MANAGER = DepartmentManagers.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void goToSearchUser(View view) {
        SearchEmployeeDialog D = new SearchEmployeeDialog(act);
        D.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_USER = D.adapter.user ;
                EmployeeTV.setText(SELECTED_USER.FirstName+" "+SELECTED_USER.LastName);
                USER dm = USER.searchUserByJobNumber(MyApp.EMPS,SELECTED_USER.DirectManager);
                if (dm != null) {
                    CURRENT_DIRECT_MANAGER = dm ;
                    CurrentDirectManagerTV.setText("Direct Manager Is "+CURRENT_DIRECT_MANAGER.FirstName+" "+CURRENT_DIRECT_MANAGER.LastName);
                }
                USER dep = USER.searchUserByJobNumber(MyApp.EMPS,SELECTED_USER.DepartmentManager);
                if (dep != null) {
                    CURRENT_DEPARTMENT_MANAGER = dep ;
                    CurrentDepartmentManagerTV.setText("Department Manager Is "+CURRENT_DEPARTMENT_MANAGER.FirstName+" "+CURRENT_DEPARTMENT_MANAGER.LastName);
                }
                D.stop();
            }
        });
        D.show();
    }

    void getDepartmentManagers() {
        if (MyApp.EMPS != null || MyApp.EMPS.size() != 0) {
            for (int i=0;i<MyApp.EMPS.size();i++) {
                USER x = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.EMPS.get(i).DepartmentManager);
                if (x != null) {
                    USER vv = USER.searchUserByJobNumber(DepartmentManagers,x.JobNumber);
                    if (vv == null) {
                        DepartmentManagers.add(x);
                    }
                }
            }
            if (DepartmentManagers.size() > 0) {
                String[] spinnerArr = new String[DepartmentManagers.size()];
                for (int i=0;i<DepartmentManagers.size();i++) {
                    spinnerArr[i] = DepartmentManagers.get(i).FirstName+" "+DepartmentManagers.get(i).LastName+"  "+DepartmentManagers.get(i).Department;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,spinnerArr);
                DepartmentManagersSpinner.setAdapter(adapter);
            }
            else {
                new MESSAGE_DIALOG(act,"No Department Managers","No Department Managers");
            }
        }
    }

    public void goToSearchNewDirectManager(View view) {
        SearchEmployeeDialog D = new SearchEmployeeDialog(act);
        D.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NEW_DIRECT_MANAGER = D.adapter.user ;
                if (NEW_DIRECT_MANAGER.Department.equals(NEW_DEPARTMENT_MANAGER.Department)) {
                    NewDirectManagerTV.setText(NEW_DIRECT_MANAGER.FirstName+" "+NEW_DIRECT_MANAGER.LastName);
                    D.stop();
                }
                else {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.notInTheSameDepartmentTitle),getResources().getString(R.string.thisEmployeeIsNotInTheSameSelectedDepartmentMessage));
                }
            }
        });
        D.show();
    }

    public void saveNewDirectManager(View view) {
        if (NEW_DEPARTMENT_MANAGER == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectDepartmentManager),getResources().getString(R.string.selectDepartmentManager));
            return;
        }
        if (NEW_DIRECT_MANAGER == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectNewDirectManager),getResources().getString(R.string.selectNewDirectManager));
            return;
        }
        Loading l = new Loading(act) ; l.show();
        StringRequest req = new StringRequest(Request.Method.POST,saveMoveEmployeeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("1")) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    SELECTED_USER.Department = NEW_DEPARTMENT_MANAGER.Department ;
                    SELECTED_USER.DirectManager = NEW_DIRECT_MANAGER.JobNumber ;
                    SELECTED_USER.DepartmentManager = NEW_DEPARTMENT_MANAGER.JobNumber ;
                    EmployeeTV.setText("");
                    CurrentDirectManagerTV.setText("");
                    CurrentDepartmentManagerTV.setText("");
                    NewDirectManagerTV.setText("");
                }
                else {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg) + " "+error);
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<>();
                par.put("ID", String.valueOf(SELECTED_USER.id));
                par.put("Department",NEW_DEPARTMENT_MANAGER.Department);
                par.put("DepartmentManager", String.valueOf(NEW_DEPARTMENT_MANAGER.JobNumber));
                par.put("DirectManager", String.valueOf(NEW_DIRECT_MANAGER.JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }
}