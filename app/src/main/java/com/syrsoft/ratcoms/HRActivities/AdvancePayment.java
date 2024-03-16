package com.syrsoft.ratcoms.HRActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
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
import com.syrsoft.ratcoms.VolleyCallback;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdvancePayment extends AppCompatActivity {

    private EditText amount , reason , installment ;
    private Activity act ;
    private String sendAdvancePaymentUrl = "https://ratco-solutions.com/RatcoManagementSystem/insertAdvancePayment.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_advance_payment);
        setActivity();
    }

    void setActivity() {
        act = this ;
        amount = (EditText) findViewById(R.id.AdvancePayment_Ammount);
        reason = (EditText) findViewById(R.id.AdvancePayment_Reason);
        installment = (EditText) findViewById(R.id.AdvancePayment_installment);
    }

    public void sendAdvancePayment(View view) {
        if (amount.getText() == null || amount.getText().toString().isEmpty() )
        {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.enterAmount),getResources().getString(R.string.enterAmount));
            return;
        }
        if (installment.getText() == null || installment.getText().toString().isEmpty())
        {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.enterinstallment),getResources().getString(R.string.enterinstallment));
            return;
        }
        Loading d = new Loading(act); d.show();
        StringRequest request = new StringRequest(Request.Method.POST, sendAdvancePaymentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();

                if (response.equals("1"))
                {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.sent) , getResources().getString(R.string.yourOrderSent));
                    amount.setText("");
                    reason.setText("");
                    installment.setText("");
                    MyApp.sendNotificationsToGroup(MyApp.AdvancePaymentaAuthUsers, getResources().getString(R.string.advancePayment), getResources().getString(R.string.advancePayment), MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "NewAdvancePayment", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else if (response.equals("0"))
                {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg) , getResources().getString(R.string.default_error_msg));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Map<String,String> par = new HashMap<String, String>();
                par.put("EmpID" , String.valueOf(MyApp.db.getUser().id));
                par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("FirstName" , MyApp.db.getUser().FirstName);
                par.put("LastName" , MyApp.db.getUser().LastName);
                par.put("DirectManager" , String.valueOf(MyApp.db.getUser().DirectManager));
                par.put("DirectManagerName" , MyApp.DIRECT_MANAGER.FirstName+" "+MyApp.DIRECT_MANAGER.LastName);
                par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                par.put("Amount" , amount.getText().toString());
                par.put("Reason" ,reason.getText().toString() );
                par.put("Installment" , installment.getText().toString());
                par.put("SendDate" , date);
                return par;
            }
        };

        Volley.newRequestQueue(act).add(request);
    }
}