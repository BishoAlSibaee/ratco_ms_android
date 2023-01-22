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

public class EditEmployeesDirectManager extends AppCompatActivity {

    Activity act ;
    List<USER> DirectManagers ;
    Spinner DirectManagersSpinner ;
    RecyclerView UsersRecycler ;
    LinearLayoutManager Manager ;
    User_Adapter adapter ;
    List<USER> UsersOfSelectedDirectManager ;
    USER SELECTED_DIRECT_MANAGER ;
    LinearLayout UsersLayout ;
    TextView usersCaption , NewDirectManagerTV ;
    USER NEW_DIRECT_MANAGER;
    String editDirectManagerOfEmployeesUrl = MyApp.MainUrl+"editDirectManagerForEmployees.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_edit_employees_direct_manager_activity);
        setActivity();
        setActivityActions();
        getDirectManagers();
    }

    void setActivity() {
        act = this ;
        DirectManagers = new ArrayList<>();
        UsersOfSelectedDirectManager = new ArrayList<>();
        DirectManagersSpinner = findViewById(R.id.spinner6);
        UsersRecycler = findViewById(R.id.usersRecycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        UsersRecycler.setLayoutManager(Manager);
        UsersLayout = findViewById(R.id.usersLayout);
        UsersLayout.setVisibility(View.GONE);
        usersCaption = findViewById(R.id.textView148v);
        NewDirectManagerTV = findViewById(R.id.textView1463);
    }

    void setActivityActions() {
        DirectManagersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTED_DIRECT_MANAGER = DirectManagers.get(position);
                UsersOfSelectedDirectManager.clear();
                for (USER u :MyApp.EMPS) {
                    if (u.DirectManager == SELECTED_DIRECT_MANAGER.JobNumber) {
                        UsersOfSelectedDirectManager.add(u);
                    }
                }
                if (UsersOfSelectedDirectManager.size() > 0) {
                    UsersLayout.setVisibility(View.VISIBLE);
                    usersCaption.setText(SELECTED_DIRECT_MANAGER.FirstName+" "+SELECTED_DIRECT_MANAGER.LastName +" Employees");
                    adapter = new User_Adapter(UsersOfSelectedDirectManager,null);
                    UsersRecycler.setAdapter(adapter);
                }
                else {
                    UsersLayout.setVisibility(View.GONE);
                    new MESSAGE_DIALOG(act,"No employees for this Direct Manager","No employees for this Direct Manager");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void getDirectManagers() {
        if (MyApp.EMPS != null || MyApp.EMPS.size() != 0) {
            for (int i=0;i<MyApp.EMPS.size();i++) {
                USER x = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.EMPS.get(i).DirectManager);
                if (x != null) {
                    USER vv = USER.searchUserByJobNumber(DirectManagers,x.JobNumber);
                    if (vv == null) {
                        DirectManagers.add(x);
                    }
                }
            }
            if (DirectManagers.size() > 0) {
                String[] spinnerArr = new String[DirectManagers.size()];
                for (int i=0;i<DirectManagers.size();i++) {
                    spinnerArr[i] = DirectManagers.get(i).FirstName+" "+DirectManagers.get(i).LastName;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,spinnerArr);
                DirectManagersSpinner.setAdapter(adapter);
            }
            else {
                new MESSAGE_DIALOG(act,"No Direct Managers","No Direct Managers");
            }
        }
    }

    public void goToSearchNewDirectManager(View view) {
        SearchEmployeeDialog D = new SearchEmployeeDialog(act);
        D.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NEW_DIRECT_MANAGER = D.adapter.user ;
                NewDirectManagerTV.setText(NEW_DIRECT_MANAGER.FirstName+" "+NEW_DIRECT_MANAGER.LastName);
                D.stop();
            }
        });
        D.show();
    }

    public void saveNewDirectManager(View view)  {
        if (SELECTED_DIRECT_MANAGER == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectDirectManager),getResources().getString(R.string.selectDirectManager));
        }
        if (NEW_DIRECT_MANAGER == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectNewDirectManager),getResources().getString(R.string.selectNewDirectManager));
            return;
        }
        Loading l = new Loading(act) ; l.show();
        StringRequest req = new StringRequest(Request.Method.POST, editDirectManagerOfEmployeesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("1")) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    for (int i=0;i<UsersOfSelectedDirectManager.size();i++) {
                        UsersOfSelectedDirectManager.get(i).DirectManager = NEW_DIRECT_MANAGER.JobNumber;
                    }
                    NEW_DIRECT_MANAGER = null ;
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
                par.put("OldDirect", String.valueOf(SELECTED_DIRECT_MANAGER.JobNumber));
                par.put("NewDirect", String.valueOf(NEW_DIRECT_MANAGER.JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }
}