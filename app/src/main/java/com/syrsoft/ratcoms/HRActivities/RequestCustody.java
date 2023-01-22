package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.VolleyCallback;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestCustody extends AppCompatActivity {

    EditText amount , reason ;
    Activity act ;
    String RequestCustodyUrl = "https://ratco-solutions.com/RatcoManagementSystem/insertCustodyRequest.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_custody_activity);
        setActivity();
    }

    void setActivity () {
        act = this ;
        amount = (EditText) findViewById(R.id.Custody_amount);
        reason = (EditText) findViewById(R.id.Custody_reason);
    }

    public void sendCustodyRequest(View view) {
        if (amount.getText() == null || amount.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.enterAmount),act);
            return;
        }
        if (reason.getText() == null || reason.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.requestReason),act);
            return;
        }
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, RequestCustodyUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("requestCustodyResp" , response);
                if (response.equals("1")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    amount.setText("");
                    reason.setText("");
                    MyApp.sendNotificationsToGroup(MyApp.CustodyAuthUsers, getResources().getString(R.string.requestCustody), getResources().getString(R.string.requestCustody), MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "NewCustodyRequest", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else if (response.equals("0")){
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("requestCustodyResp" , error.getMessage());
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg));
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH) ;
                Map<String,String> par = new HashMap<String, String>();
                par.put("EmpID", String.valueOf( MyApp.db.getUser().id));
                par.put("JobNumber",String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("FName", MyApp.db.getUser().FirstName);
                par.put("LName", MyApp.db.getUser().LastName);
                par.put("DirectManager" , String.valueOf( MyApp.DIRECT_MANAGER.JobNumber));
                par.put("DirectManagerName", MyApp.DIRECT_MANAGER.FirstName+" "+MyApp.DIRECT_MANAGER.LastName);
                par.put("JobTitle", MyApp.db.getUser().JobTitle);
                par.put("Amount",amount.getText().toString());
                par.put("Reason", reason.getText().toString());
                par.put("Date",Date);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }
}