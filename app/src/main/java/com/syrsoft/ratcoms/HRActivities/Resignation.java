package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.google.firebase.messaging.FirebaseMessaging.*;
import com.syrsoft.ratcoms.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Resignation extends AppCompatActivity {

    private TextView fname , lname , jobNumber , jobTitle , directManager , department ;
    private EditText reason , date ;
    private String getDirectManagerUrl = "https://ratco-solutions.com/RatcoManagementSystem/getDirectManager.php" ;
    private String sendResignationUrl = "https://ratco-solutions.com/RatcoManagementSystem/insertResignation.php";
    private Activity act = this ;
    private String DirectManagerText ="";
    private CalendarView cal ;
    private String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    final static private String serverKey = "key=" + "AAAANacBb74:APA91bHRas6269tP9BLt2_qghgMf_UuiQPQfrP5KZscRwNX1MgsqdwF_rLlAcAKO2Bs-ZPaYWQE4c-TlFXgP7E4UnOEkzmIzLevwlYCuhusz4knqvZCxWfQ0AzfRM37eL6-V10l42QRh";
    final static private String contentType = "application/json";
    private String token ;
    private USER manager ;
    private List<String> tokens =new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resignation_activity);
        MyApp.RefME.child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    token = snapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getDirectManager();
        setActivity();
    }

    void setActivity(){
        fname = (TextView) findViewById(R.id.Resignation_fname);
        lname = (TextView) findViewById(R.id.Resignation_lname);
        jobNumber = (TextView) findViewById(R.id.Resignation_jobNumber);
        jobTitle = (TextView) findViewById(R.id.Resignation_jobTitle);
        directManager = (TextView) findViewById(R.id.Resignation_DirectManager);
        department = (TextView) findViewById(R.id.Resignation_department);
        reason = (EditText) findViewById(R.id.Resignation_reason);
        date = (EditText) findViewById(R.id.Resignation_Date);
        date.setActivated(false);
        fname.setText(MyApp.db.getUser().FirstName);
        lname.setText(MyApp.db.getUser().LastName);
        jobNumber.setText(String.valueOf(MyApp.db.getUser().JobNumber));
        jobTitle.setText(MyApp.db.getUser().JobTitle);
        directManager.setText(DirectManagerText);
        department.setText(MyApp.db.getUser().Department);
        cal = (CalendarView) findViewById(R.id.calendarView);
        Calendar c = Calendar.getInstance(Locale.getDefault());
        cal.setDate(c.getTimeInMillis());
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar ccc = Calendar.getInstance();
                ccc.set(year,month,dayOfMonth);
                cal.setDate(ccc.getTimeInMillis());
                date.setText(ccc.get(Calendar.YEAR)+"-"+(ccc.get(Calendar.MONTH)+1)+"-"+ccc.get(Calendar.DAY_OF_MONTH));
            }
        });
    }

    void getDirectManager(){
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getDirectManagerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                Log.d("getDirectManager",response);
                if (response != null){
                    List<Object> list = JsonToObject.translate(response, USER.class,act);
                    manager = (USER) list.get(0);
                    DirectManagerText = manager.FirstName+" "+manager.LastName ;
                    directManager.setText(DirectManagerText);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("jn" , String.valueOf(MyApp.db.getUser().DirectManager));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void sendResignation(View view) {

        if (reason.getText().toString() == null || reason.getText().toString().isEmpty() ){
            ToastMaker.Show(1,"Enter Resignation Reason" , act);
            return;
        }
        if (date.getText().toString() == null || date.getText().toString().isEmpty()){
            //ToastMaker.Show(1,"Enter Resignation Date" , act);
            Calendar cc = Calendar.getInstance();
            cc.setTimeInMillis(cal.getDate());
            ToastMaker.Show(1,cc.get(Calendar.YEAR)+"-"+(cc.get(Calendar.MONTH)+1)+"-"+cc.get(Calendar.DAY_OF_MONTH),act);
            return;
        }

        Loading d = new Loading(act); d.show();
        StringRequest request = new StringRequest(Request.Method.POST, sendResignationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                if (response.equals("1")){
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.ResignationSent),getResources().getString(R.string.ResignationSent));
                    MyApp.sendNotificationsToGroup(MyApp.ResignationsAuthUsers, getResources().getString(R.string.resignation), getResources().getString(R.string.resignation), MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "NewResignation", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {
                            reason.setText("");
                            date.setText("");
                        }
                    });
                        //CloudMessage(getResources().getString(R.string.resignation) , getResources().getString(R.string.resignation) ,MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName , MyApp.db.getUser().JobNumber,t ,"NewResignation");

                }
                else {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Map<String,String> par = new HashMap<String, String>();
                par.put("EmpID" , String.valueOf(MyApp.db.getUser().id));
                par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("FName" , MyApp.db.getUser().FirstName);
                par.put("LName" , MyApp.db.getUser().LastName);
                par.put("DirectManager" ,String.valueOf(MyApp.db.getUser().DirectManager));
                par.put("DirectManagerName" , DirectManagerText);
                par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                par.put("ResignationReason" , reason.getText().toString());
                par.put("ResignationDate" , date.getText().toString());
                par.put("ResignationSendDate" , Date);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void CloudMessage(String Title , String Message , String Name , int JobNumber , String Topic , String order){

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", Title);
            notifcationBody.put("message", Message);
            notifcationBody.put("Name", Name);
            notifcationBody.put("JobNumber", JobNumber);
            notifcationBody.put("order", order);
            notification.put("to", Topic);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            //Log.e(TAG, "onCreate: " + e.getMessage() );
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_MESSAGE_URL, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            ToastMaker.Show(1,"message sent" , act);
                            Log.d("messageresponse" , response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastMaker.Show(1,error.getMessage() , act);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        Volley.newRequestQueue(act).add(jsonObjectRequest);
    }

}