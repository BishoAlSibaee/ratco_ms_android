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
import com.syrsoft.ratcoms.Management.Adapters.Permissions_Adapter;
import com.syrsoft.ratcoms.ManagementActivity.*;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VollyCallback;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

public class SetEmployeesPermissions extends AppCompatActivity {

    Activity act ;
    Spinner UsersSpinner ;
    USER SelectedUser ;
    RecyclerView PermissionsRecycler ;
    LinearLayoutManager Manager ;
    Permissions_Adapter adapter ;
    String savePermissions = MyApp.MainUrl + "setUserPermissions.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_employees_permissions);
        setActivity();
        setActivityActions();
    }

    void setActivity() {
        act = this ;
        UsersSpinner = (Spinner) findViewById(R.id.spinner2);
        setUsersSpinner();
        PermissionsRecycler = (RecyclerView) findViewById(R.id.permissionsRecycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false ) ;
        PermissionsRecycler.setLayoutManager(Manager);
    }

    void setActivityActions() {
        UsersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedUser = MyApp.EMPS.get(position);
                SelectedUser.getPermissions(new VollyCallback() {
                    @Override
                    public void onSuccess(String s) {
                        SelectedUser.getMyPermissions(SelectedUser.id, SelectedUser.JobNumber, new VollyCallback() {
                            @Override
                            public void onSuccess(String s) {
                                adapter = new Permissions_Adapter(SelectedUser.MyPermissions);
                                PermissionsRecycler.setAdapter(adapter);
                            }

                            @Override
                            public void onFailed(String error) {

                            }
                        },act);
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                },act);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void setUsersSpinner() {
        String[] usersArray = new String[MyApp.EMPS.size()];
        for (int i=0;i<MyApp.EMPS.size();i++) {
            usersArray[i] = MyApp.EMPS.get(i).FirstName+" "+MyApp.EMPS.get(i).LastName ;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,usersArray);
        UsersSpinner.setAdapter(adapter);
    }

    public void savePermissions(View view) {
        if (SelectedUser.MyPermissions != null) {
            Loading l = new Loading(act);
            l.show();
            StringRequest request = new StringRequest(Request.Method.POST, savePermissions, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("savepermission",response);
                    l.close();
                    if (response.equals("1")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    }
                    else if (response.equals("0")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.notSavedErrorTitle),getResources().getString(R.string.notSavedErrorMessage));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    l.close();
                    if (error != null) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error",error.toString());
                    }
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> par = new HashMap<String, String>();
                    par.put("Eid" , String.valueOf(SelectedUser.id));
                    par.put("JobNumber" , String.valueOf(SelectedUser.JobNumber));
                    for (int i=0 ; i < SelectedUser.MyPermissions.size();i++) {
                        Log.d("savepermission","p"+(i+1));
                        par.put("p"+(i+1), String.valueOf(SelectedUser.MyPermissions.get(i).getValue()));
                    }
                    return par;
                }
            };
            Volley.newRequestQueue(act).add(request);
        }
    }

    public void checkSalesmanPermissions(View view) {
        int[] pers = new int[] {3,4,5,6,7,8,9,16,20,21,23,24,25,26,27,28,29,30} ;
        for (int i = 0 ; i <SelectedUser.MyPermissions.size();i++) {
            if ( MyApp.BinarySearch(pers,SelectedUser.MyPermissions.get(i).getId())) {
                Log.d("foundPers" ,SelectedUser.MyPermissions.get(i).getId()+" ");
                SelectedUser.MyPermissions.get(i).setValue(1);
                SelectedUser.MyPermissions.get(i).setResult();
            }
            else {
                SelectedUser.MyPermissions.get(i).setValue(0);
                SelectedUser.MyPermissions.get(i).setResult();
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void chechkLabourPermissions(View view) {
        int[] pers = new int[] {3,4,5,6,7,8,9,16} ;
        for (int i = 0 ; i <SelectedUser.MyPermissions.size();i++) {
            if ( MyApp.BinarySearch(pers,SelectedUser.MyPermissions.get(i).getId())) {
                Log.d("foundPers" ,SelectedUser.MyPermissions.get(i).getId()+" ");
                SelectedUser.MyPermissions.get(i).setValue(1);
                SelectedUser.MyPermissions.get(i).setResult();
            }
            else {
                SelectedUser.MyPermissions.get(i).setValue(0);
                SelectedUser.MyPermissions.get(i).setResult();
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void checkTecniciansPermissions(View view) {
        int[] pers = new int[] {3,4,5,6,7,8,9,16,31} ;
        for (int i = 0 ; i <SelectedUser.MyPermissions.size();i++) {
            if ( MyApp.BinarySearch(pers,SelectedUser.MyPermissions.get(i).getId())) {
                Log.d("foundPers" ,SelectedUser.MyPermissions.get(i).getId()+" ");
                SelectedUser.MyPermissions.get(i).setValue(1);
                SelectedUser.MyPermissions.get(i).setResult();
            }
            else {
                SelectedUser.MyPermissions.get(i).setValue(0);
                SelectedUser.MyPermissions.get(i).setResult();
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void checkAllPermissions(View view) {
        for (int i = 0 ; i <SelectedUser.MyPermissions.size();i++) {
            SelectedUser.MyPermissions.get(i).setValue(1);
            SelectedUser.MyPermissions.get(i).setResult();
        }
        adapter.notifyDataSetChanged();
    }

    public void encheckAllPermissions(View view) {
        for (int i = 0 ; i <SelectedUser.MyPermissions.size();i++) {
            SelectedUser.MyPermissions.get(i).setValue(0);
            SelectedUser.MyPermissions.get(i).setResult();
        }
        adapter.notifyDataSetChanged();
    }

}