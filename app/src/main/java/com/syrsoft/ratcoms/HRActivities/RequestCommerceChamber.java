package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class RequestCommerceChamber extends AppCompatActivity {

    private Activity act;
    private EditText ReqReason, txtRequestForm;
    private Button buttonSend;
    private final String SendUrl = "https://ratco-solutions.com/RatcoManagementSystem/NewOptions/InsertRequestChamberCommerce.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_commerce_chamber);
        setActivity();
    }

    void setActivity() {
        act = this;
        ReqReason = findViewById(R.id.ReqReason);
        txtRequestForm = findViewById(R.id.txtRequestForm);
        buttonSend = findViewById(R.id.buttonSend);
    }

    public void sendRequest(View view) {
        if (ReqReason.getText() == null || ReqReason.getText().toString().isEmpty()) {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.OrderType1), getResources().getString(R.string.EnterOrderType));
            return;
        }
        if (txtRequestForm.getText() == null || txtRequestForm.getText().toString().isEmpty()) {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.RequestForm), getResources().getString(R.string.EnterRequestForm));
            return;
        }
        Loading d = new Loading(act);
        d.show();

        StringRequest request = new StringRequest(Request.Method.POST, SendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                if (response.equals("1")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.sent), getResources().getString(R.string.yourOrderSent));
                    ReqReason.setText("");
                    txtRequestForm.setText("");
                    //check Notification
                    MyApp.sendNotificationsToGroup(MyApp.ChamberAuthUsers, getResources().getString(R.string.RequestChamberCommerce), getResources().getString(R.string.RequestChamberCommerce), MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "New Commerce Chamber", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("DoneSendNotif", "Done Send");
                        }
                    });
                } else if (response.equals("0")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.default_error_msg), getResources().getString(R.string.default_error_msg));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
                Map<String, String> par = new HashMap<String, String>();
                par.put("EmpID", String.valueOf(MyApp.db.getUser().id));
                par.put("JobNumber", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("FName", MyApp.db.getUser().FirstName);
                par.put("LName", MyApp.db.getUser().LastName);
                par.put("DirectManager", String.valueOf(MyApp.db.getUser().DirectManager));
                par.put("DirectManagerName", MyApp.DIRECT_MANAGER.FirstName + " " + MyApp.DIRECT_MANAGER.LastName);
                par.put("JobTitle", MyApp.db.getUser().JobTitle);
                par.put("SendDate", date);
                par.put("Title", ReqReason.getText().toString());
                par.put("ContractForm", txtRequestForm.getText().toString());
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }
}