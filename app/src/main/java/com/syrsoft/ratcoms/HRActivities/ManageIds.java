package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.syrsoft.ratcoms.AlarmReceiver;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.ManageIds_Adapter;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

public class ManageIds extends AppCompatActivity {

    private String getEmpsUrl="https://ratco-solutions.com/RatcoManagementSystem/getAllEmps.php" ;
    private String setWarningUrl = "https://ratco-solutions.com/RatcoManagementSystem/setWarningFieldValue.php";
    private List<USER> list= new ArrayList<USER>();
    private Activity act ;
    private RecyclerView emps ;
    private ManageIds_Adapter adapter ;
    private RecyclerView.LayoutManager manager ;
    AlarmManager alarmMgr ;
    private CheckBox getIdsNotification ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_ids);
        setActivity();
        getEmps();

    }

    void setActivity(){
        act = this;
        emps = (RecyclerView) findViewById(R.id.manageIds_Recycler);
        manager = new LinearLayoutManager(act,LinearLayoutManager.VERTICAL,false);
        adapter = new ManageIds_Adapter(list);
        emps.setLayoutManager(manager);
        getIdsNotification = (CheckBox) findViewById(R.id.checkBox);
        getIdsNotification.setVisibility(View.GONE);
        MyApp.RefME.child("IDsWarningNotifications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!= null)
                {
                    if (snapshot.getValue().toString().equals("1"))
                    {
                        getIdsNotification.setChecked(true);
                    }
                    else
                    {
                        getIdsNotification.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getIdsNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    MyApp.RefME.child("IDsWarningNotifications").setValue("1");
                    setAlarmForAll();
                    setWarningValue("1",0);
                }
                else
                {
                    MyApp.RefME.child("IDsWarningNotifications").setValue("0");
                    setWarningValue("0",0);
                }
            }
        });
        if (MyApp.db.getUser().Department.equals("Account") || MyApp.db.getUser().JobTitle.equals("Manager") || MyApp.db.getUser().JobTitle.equals("Sales Manager")) {
            getIdsNotification.setVisibility(View.VISIBLE);
        }
    }

    void getEmps(){
        Loading d = new Loading(act); d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getEmpsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                if (!response.equals("0")){
                    List<Object> lis = JsonToObject.translate(response,USER.class,act);
                    if (lis.size()>0) {
                        for (Object o : lis) {
                            USER r = (USER) o;
                            list.add(r);
                        }
                        List<USER> NewList = new ArrayList<USER>();
                        boolean sorted = false;
                        while (!sorted){
                            sorted = true ;
                            for (int i = 0; i < list.size() - 1; i++) {

                                if (!list.get(i).compareByIdDate(list.get(i + 1))) {
                                    USER temp = list.get(i);
                                    list.set(i, list.get(i + 1));
                                    list.set(i + 1, temp);
                                    sorted = false ;
                                }
                            }
                         }
                        for(USER u:list){
                            Log.d("afterSort" , u.FirstName);
                        }

                        emps.setAdapter(adapter);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
                    Log.d("getEmpsError" , error.getMessage());
            }
        }){

        };
        Volley.newRequestQueue(act).add(request);
    }

    public void setAlarmForAll() {
        for(USER u :list) {
            Calendar c = Calendar.getInstance(Locale.getDefault());
            try {
                String t = u.IDExpireDate+"-10-00";
                Date date = new SimpleDateFormat("yyyy-MM-dd-hh-mm").parse(t);
                c.setTime(date);
                c.add(Calendar.MONTH,-1);
                alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);
                intent.putExtra("title" , u.FirstName);
                intent.putExtra("order" , "ID");
                intent.putExtra("date" , u.IDExpireDate);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, u.JobNumber, intent, 0);
                Log.d("alarmTime" , c.getTime().toString());
                alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

            }
            catch (ParseException e) {
                e.printStackTrace();
                Log.d("alarmTime" , e.getMessage());
            }

        }


    }

    void setWarningValue(String status, int field)
    {
        StringRequest request = new StringRequest(Request.Method.POST, setWarningUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Log.d("WarningField " , "Done "+status+" IDS"  ) ;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("WarningField " , "error "+error.getMessage() ) ;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("id" , String.valueOf( MyApp.db.getUser().id));
                par.put("status" , status);
                par.put("field" , String.valueOf(field));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }
}