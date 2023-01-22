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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class EditEmployeeDirectManager extends AppCompatActivity {

    Activity act ;
    TextView EmployeeTV , NewDirectManagerTV , CurrentDM ;
    USER SELECTED_USER ;
    USER NEW_DIRECT_MANAGER ;
    String saveDirectManagerUrl = MyApp.MainUrl+"editEmployeeDirectManager.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_edit_employee_direct_manager_activity);
        setActivity();
    }

    void setActivity() {
        act = this ;
        EmployeeTV = findViewById(R.id.textView146);
        NewDirectManagerTV = findViewById(R.id.textView1463);
        CurrentDM = findViewById(R.id.textView145v);
    }

    public void goToSearchUser(View view) {
        SearchEmployeeDialog D = new SearchEmployeeDialog(act);
        D.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_USER = D.adapter.user ;
                EmployeeTV.setText(SELECTED_USER.FirstName+" "+SELECTED_USER.LastName);
                USER dm = USER.searchUserByJobNumber(MyApp.EMPS,SELECTED_USER.DirectManager);
                CurrentDM.setText(dm.FirstName+" "+dm.LastName);
                D.stop();
            }
        });
        D.show();
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

    public void saveNewDirectManager(View view) {
        if (SELECTED_USER == null ) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectEmployee),getResources().getString(R.string.selectEmployee));
            return;
        }
        if (NEW_DIRECT_MANAGER == null) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectNewDirectManager),getResources().getString(R.string.selectNewDirectManager));
            return;
        }
        Loading l = new Loading(act) ; l.show();
        StringRequest req = new StringRequest(Request.Method.POST, saveDirectManagerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("1")) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    SELECTED_USER.DirectManager = NEW_DIRECT_MANAGER.JobNumber ;
                    SELECTED_USER = null ;
                    NEW_DIRECT_MANAGER = null ;
                    EmployeeTV.setText("");
                    NewDirectManagerTV.setText("");
                    CurrentDM.setText("");

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
                par.put("NewDirect", String.valueOf(NEW_DIRECT_MANAGER.JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }
}