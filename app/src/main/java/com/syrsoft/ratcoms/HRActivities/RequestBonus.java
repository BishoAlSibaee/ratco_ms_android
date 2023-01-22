package com.syrsoft.ratcoms.HRActivities;

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
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestBonus extends AppCompatActivity {

    Activity act ;
    Spinner EmployeesSpinner ;
    TextView Amount , Notes ;
    USER SelectedUser ;
    String insertBonusRequest = MyApp.MainUrl + "insertNewBonusRequest.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_bonus);
        setActivity();
        setActivityActions();
        drawMyStaff();
    }

    void setActivity () {
        act = this ;
        EmployeesSpinner = findViewById(R.id.spinner5);
        Amount = findViewById(R.id.editTextNumberDecimal);
        Notes = findViewById(R.id.editTextTextMultiLine4);
    }

    void setActivityActions() {
        EmployeesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedUser = MyApp.MyUser.MyStaff.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void drawMyStaff () {
        if (MyApp.MyUser.MyStaff != null && MyApp.MyUser.MyStaff.size() != 0) {
            String[] emps = new String[MyApp.MyUser.MyStaff.size()];
            for (int i=0;i<MyApp.MyUser.MyStaff.size();i++) {
                emps[i] = MyApp.MyUser.MyStaff.get(i).FirstName +" "+MyApp.MyUser.MyStaff.get(i).LastName ;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,emps);
            EmployeesSpinner.setAdapter(adapter);
        }
    }

    public void sendBonus(View view) {
        if (Amount.getText() == null || Amount.getText().toString().isEmpty()) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.enterAmount),getResources().getString(R.string.enterAmount));
            return ;
        }
        if (Notes.getText() == null || Notes.getText().toString().isEmpty()) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.bonusReason),getResources().getString(R.string.bonusReason));
            return ;
        }
        if (SelectedUser == null ) {
            new MESSAGE_DIALOG(act,getResources().getString(R.string.selectEmp),getResources().getString(R.string.selectEmp));
            return ;
        }
        Loading l = new Loading(act); l.show();
        StringRequest req = new StringRequest(Request.Method.POST,insertBonusRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                int res = Integer.valueOf(response);
                if (res > 0) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    Amount.setText("");
                    Notes.setText("");
                    MyApp.sendNotificationsToGroup(MyApp.BonusAuthUsers, "BonusOrder", "NewBonusOrder", SelectedUser.FirstName + " " + SelectedUser.LastName, SelectedUser.JobNumber, "RequestBonus", MyApp.app, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else {
                    new MESSAGE_DIALOG(act,"Error","Error Saving Order");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MESSAGE_DIALOG(act,"Error","Error Saving Order"+error);
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<>();
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                par.put("JobNumber",String.valueOf(SelectedUser.JobNumber));
                par.put("Name",SelectedUser.FirstName+" "+SelectedUser.LastName);
                par.put("RequesterJobNumber", String.valueOf(MyApp.MyUser.JobNumber));
                par.put("RequesterName",MyApp.MyUser.FirstName+" "+MyApp.MyUser.LastName);
                par.put("BonusAmount", Amount.getText().toString());
                par.put("RequestDate",date);
                par.put("Notes" ,Notes.getText().toString());
                par.put("From","Orders");
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }
}