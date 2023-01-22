package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
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
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.ManageContracts_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.ManagePassports_Adapter;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageContracts extends AppCompatActivity {

    private Activity act ;
    private String getEmpsUrl="https://ratco-solutions.com/RatcoManagementSystem/getAllEmps.php" ;
    private String setWarningUrl = "https://ratco-solutions.com/RatcoManagementSystem/setWarningFieldValue.php";
    private RecyclerView Contracts ;
    private List<USER> emps = new ArrayList<USER>();
    private ManageContracts_Adapter adapter ;
    private RecyclerView.LayoutManager manager ;
    private CheckBox getContractsNotification ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_contracts);
        setActivity();
        getEmps();
    }

    void setActivity () {
        act = this ;
        Contracts = (RecyclerView) findViewById(R.id.manageContracts_Recycler);
        manager = new LinearLayoutManager(act,LinearLayoutManager.VERTICAL,false);
        adapter = new ManageContracts_Adapter(emps);
        Contracts.setLayoutManager(manager);
        getContractsNotification = (CheckBox) findViewById(R.id.checkBox6);
        getContractsNotification.setVisibility(View.GONE);
        MyApp.RefME.child("CONTRACTsWarningNotification").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!= null)
                {
                    if (snapshot.getValue().toString().equals("1"))
                    {
                        getContractsNotification.setChecked(true);
                    }
                    else
                    {
                        getContractsNotification.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getContractsNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    MyApp.RefME.child("CONTRACTsWarningNotification").setValue("1");
                    //setAlarmForAll();
                    setWarningValue("1",2);
                }
                else
                {
                    MyApp.RefME.child("CONTRACTsWarningNotification").setValue("0");
                    setWarningValue("0",2);
                }
            }
        });
        if (MyApp.db.getUser().Department.equals("Account") || MyApp.db.getUser().JobTitle.equals("Manager") || MyApp.db.getUser().JobTitle.equals("Sales Manager")) {
            getContractsNotification.setVisibility(View.VISIBLE);
        }
    }

    void getEmps(){
        Loading d = new Loading(act); d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getEmpsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                if (!response.equals("0")){
                    List<Object> lis = JsonToObject.translate(response, USER.class,act);
                    if (lis.size()>0) {
                        for (Object o : lis) {
                            USER r = (USER) o;
                            emps.add(r);
                        }
                        List<USER> NewList = new ArrayList<USER>();
                        boolean sorted = false;
                        while (!sorted){
                            sorted = true ;
                            for (int i = 0; i < emps.size() - 1; i++) {

                                if (!emps.get(i).compareByContractDate(emps.get(i + 1))) {
                                    USER temp = emps.get(i);
                                    emps.set(i, emps.get(i + 1));
                                    emps.set(i + 1, temp);
                                    sorted = false ;
                                }
                            }
                        }
                        for(USER u:emps){
                            Log.d("afterSort" , u.FirstName);
                        }

                        Contracts.setAdapter(adapter);
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