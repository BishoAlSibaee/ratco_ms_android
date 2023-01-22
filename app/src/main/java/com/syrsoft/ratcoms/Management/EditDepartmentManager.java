package com.syrsoft.ratcoms.Management;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDepartmentManager extends AppCompatActivity {

    Activity act ;
    List<USER> DepartmentManagers ;
    Spinner DepartmentManagersSpinner ;
    RecyclerView UsersRecycler ;
    LinearLayoutManager Manager ;
    User_Adapter adapter ;
    List<USER> UsersOfSelectedDepartmentManager ;
    USER SELECTED_DEPARTMENT_MANAGER ;
    LinearLayout UsersLayout ;
    TextView usersCaption , NewDepartmentManagerTV ;
    USER NEW_DEPARTMENT_MANAGER;
    String editDepartmentManagerUrl = MyApp.MainUrl+"editDepartmentManager.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_edit_department_manager_activity);
        setActivity();
        setActivityActions();
        getDepartmentManagers();
    }

    void setActivity() {
        act = this;
        DepartmentManagers = new ArrayList<>();
        UsersOfSelectedDepartmentManager = new ArrayList<>();
        DepartmentManagersSpinner = findViewById(R.id.spinner6);
        UsersRecycler = findViewById(R.id.usersRecycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        UsersRecycler.setLayoutManager(Manager);
        UsersLayout = findViewById(R.id.usersLayout);
        UsersLayout.setVisibility(View.GONE);
        usersCaption = findViewById(R.id.textView148v);
        NewDepartmentManagerTV = findViewById(R.id.textView1463);
    }

    void setActivityActions() {
        DepartmentManagersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTED_DEPARTMENT_MANAGER = DepartmentManagers.get(position);
                UsersOfSelectedDepartmentManager.clear();
                for (USER u :MyApp.EMPS) {
                    if (u.DepartmentManager == SELECTED_DEPARTMENT_MANAGER.JobNumber && u.JobNumber != SELECTED_DEPARTMENT_MANAGER.JobNumber) {
                        UsersOfSelectedDepartmentManager.add(u);
                    }
                }
                if (UsersOfSelectedDepartmentManager.size() > 0) {
                    UsersLayout.setVisibility(View.VISIBLE);
                    usersCaption.setText(SELECTED_DEPARTMENT_MANAGER.FirstName+" "+SELECTED_DEPARTMENT_MANAGER.LastName +" Employees");
                    adapter = new User_Adapter(UsersOfSelectedDepartmentManager,null);
                    UsersRecycler.setAdapter(adapter);
                }
                else {
                    UsersLayout.setVisibility(View.GONE);
                    new MESSAGE_DIALOG(act,"No employees for this Department Manager","No employees for this Department Manager");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                NEW_DEPARTMENT_MANAGER = D.adapter.user ;
                NewDepartmentManagerTV.setText(NEW_DEPARTMENT_MANAGER.FirstName+" "+NEW_DEPARTMENT_MANAGER.LastName);
                D.stop();
            }
        });
        D.show();
    }

    public void saveNewDepartmentManager(View view) {
        if (SELECTED_DEPARTMENT_MANAGER == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectDirectManager),getResources().getString(R.string.selectDirectManager));
        }
        if (NEW_DEPARTMENT_MANAGER == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectDepartmentManager),getResources().getString(R.string.selectDepartmentManager));
            return;
        }
        Loading l = new Loading(act) ; l.show();
        StringRequest req = new StringRequest(Request.Method.POST, editDepartmentManagerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("1")) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    for (int i=0;i<UsersOfSelectedDepartmentManager.size();i++) {
                        UsersOfSelectedDepartmentManager.get(i).DirectManager = NEW_DEPARTMENT_MANAGER.JobNumber;
                    }
                    NEW_DEPARTMENT_MANAGER = null ;
                    NewDepartmentManagerTV.setText("");
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
                par.put("OldDirect", String.valueOf(SELECTED_DEPARTMENT_MANAGER.JobNumber));
                par.put("NewDirect", String.valueOf(NEW_DEPARTMENT_MANAGER.JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }
}