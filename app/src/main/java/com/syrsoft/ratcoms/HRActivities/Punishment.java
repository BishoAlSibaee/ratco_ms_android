package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Punishment extends AppCompatActivity {
    private EditText Reason, Ammount;
    private Activity act;
    private Button Send;
    Spinner SpinnerMyStaff;
    USER SelectedUser;


    private String sendPunishmentUrl = "https://ratco-solutions.com/RatcoManagementSystem/NewOptions/InsertRequestDiscount.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punishment);
        setActivity();
        setActivityActions();
        drawMyStaff();
    }

    void setActivity() {
        act = this;
        Reason = findViewById(R.id.PunishmentReason);
        Ammount = findViewById(R.id.PunishmentAmmount);
        Send = findViewById(R.id.buttonSend);
        SpinnerMyStaff = findViewById(R.id.SpinnerMyStaff);
        MyApp.getMyStaff(MyApp.db.getUser().JobNumber);
    }

    void setActivityActions() {
        SpinnerMyStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedUser = MyApp.myStaffList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    void drawMyStaff() {
        if (MyApp.myStaffList != null && MyApp.myStaffList.size() != 0) {
            String[] emps = new String[MyApp.myStaffList.size()];
            for (int i = 0; i < MyApp.myStaffList.size(); i++) {
                emps[i] = MyApp.myStaffList.get(i).FirstName + " " + MyApp.myStaffList.get(i).LastName;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(act, R.layout.spinner_item, emps);
            SpinnerMyStaff.setAdapter(adapter);
        }
    }

    public void SendPunishment(View view) {
        if (Reason.getText() == null || Reason.getText().toString().isEmpty()) {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.requestReason), getResources().getString(R.string.requestReason));
            return;
        }
        if (Ammount.getText() == null || Ammount.getText().toString().isEmpty()) {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.enterAmount), getResources().getString(R.string.enterAmount));
            return;
        }
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, sendPunishmentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                if (response.equals("1")) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.sent), getResources().getString(R.string.yourOrderSent));
                    Reason.setText("");
                    Ammount.setText("");

                    //Check Notification
                     MyApp.sendNotificationsToGroup(MyApp.PunishmentAuthUsers, getResources().getString(R.string.Punishment), getResources().getString(R.string.Punishment), MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, MyApp.myStaffList.get(SpinnerMyStaff.getSelectedItemPosition()).JobNumber, "NewPunishment", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("DoneSendNotif", "Done Send");
                            MyApp.CloudMessage(getResources().getString(R.string.Punishment), getResources().getString(R.string.Punishment), " "+MyApp.MyUser.FirstName+ " "+MyApp.MyUser.LastName, MyApp.myStaffList.get(SpinnerMyStaff.getSelectedItemPosition()).JobNumber,MyApp.myStaffList.get(SpinnerMyStaff.getSelectedItemPosition()).Token, "NewPunishment", act);
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
                par.put("Reason", Reason.getText().toString());
                par.put("Amount", Ammount.getText().toString());
                par.put("EmployeeName", SelectedUser.FirstName + " " + SelectedUser.LastName);
                par.put("EmployeeJobNumber", String.valueOf(SelectedUser.JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }
}