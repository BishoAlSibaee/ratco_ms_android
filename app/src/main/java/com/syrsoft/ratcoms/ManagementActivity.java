package com.syrsoft.ratcoms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.ManageAuthorizedTitles;
import com.syrsoft.ratcoms.HRActivities.ManageOrdersAuthority;
import com.syrsoft.ratcoms.HRActivities.SendNewAdWithImage;
import com.syrsoft.ratcoms.Management.EmployeesRates;
import com.syrsoft.ratcoms.Management.ManageEmployees;
import com.syrsoft.ratcoms.Management.SetEmployeesPermissions;
import com.syrsoft.ratcoms.Management.SetMaintenanceResponsibles;
import com.syrsoft.ratcoms.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ManagementActivity extends AppCompatActivity {

    Activity act ;
    Button ManageAuthorizedJobtitles ,ManageOrdersAuthorities, sendAdWithImage,manageIds , managePassports , manageContracts, sendAd;
    private String insertNewAdUrl = MyApp.MainUrl + "insertNewAd.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        setActivity();
        setVisiblePermissions();
    }

    void setActivity() {
        act = this;
        ManageAuthorizedJobtitles = (Button) findViewById(R.id.button10);
        ManageOrdersAuthorities = (Button) findViewById(R.id.HR_manageorderTypesAuthorities);
        manageIds = (Button) findViewById(R.id.HR_manageEmployeesId);
        managePassports = (Button) findViewById(R.id.HR_managePassports);
        manageContracts = (Button) findViewById(R.id.HR_manageEmployeesContracts);
        sendAd = (Button) findViewById(R.id.HR_sendNewAd);
        sendAdWithImage = (Button) findViewById(R.id.HR_sendNewAdWithImage);
    }

    public void goTOPermisstions(View view) {
        Intent i = new Intent(act , SetEmployeesPermissions.class);
        startActivity(i);
    }

    public void goToManageOrdertypesAuthorities(View view) {
        Intent i = new Intent(act, ManageOrdersAuthority.class);
        startActivity(i);
    }

    public void goToAuthorizedJobTitles(View view) {
        Intent i = new Intent(act, ManageAuthorizedTitles.class);
        startActivity(i);
    }

    void setVisiblePermissions() {
        for (int i=0 ; i < MyApp.MyUser.MyPermissions.size();i++) {
            if (MyApp.MyUser.MyPermissions.get(i).getDepartment().equals("HR")) {
                if (MyApp.MyUser.MyPermissions.get(i).getId() == 1) {
                    if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                        ManageAuthorizedJobtitles.setVisibility(View.VISIBLE);
                    }
                    else {
                        ManageAuthorizedJobtitles.setVisibility(View.GONE);
                    }
                    continue;
                }
                if (MyApp.MyUser.MyPermissions.get(i).getId() == 2) {
                    if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                        ManageOrdersAuthorities.setVisibility(View.VISIBLE);
                    }
                    else {
                        ManageOrdersAuthorities.setVisibility(View.GONE);
                    }
                    continue;
                }
                if (MyApp.MyUser.MyPermissions.get(i).getId() == 10) {
                    if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                        manageContracts.setVisibility(View.VISIBLE);
                    }
                    else {
                        manageContracts.setVisibility(View.GONE);
                    }
                    continue;
                }
                if (MyApp.MyUser.MyPermissions.get(i).getId() == 11) {
                    if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                        managePassports.setVisibility(View.VISIBLE);
                    }
                    else {
                        managePassports.setVisibility(View.GONE);
                    }
                    continue;
                }
                if (MyApp.MyUser.MyPermissions.get(i).getId() == 12) {
                    if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                        manageIds.setVisibility(View.VISIBLE);
                    }
                    else {
                        manageIds.setVisibility(View.GONE);
                    }
                    continue;
                }
                if (MyApp.MyUser.MyPermissions.get(i).getId() == 13) {
                    if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                        sendAd.setVisibility(View.VISIBLE);
                    }
                    else {
                        sendAd.setVisibility(View.GONE);
                    }
                    continue;
                }
                if (MyApp.MyUser.MyPermissions.get(i).getId() == 14) {
                    if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                        sendAdWithImage.setVisibility(View.VISIBLE);
                    }
                    else {
                        sendAdWithImage.setVisibility(View.GONE);
                    }
                    continue;
                }
            }
        }
    }

    public void goToSetMaintenanceResponsibles(View view) {
        Intent i = new Intent(act, SetMaintenanceResponsibles.class);
        startActivity(i);
    }

    public void goToManageContracts(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.ManageContracts.class);
        startActivity(i);
    }

    public void gotoManagePassports(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.ManagePassports.class);
        startActivity(i);
    }

    public void goToMangeEmployeesIds(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.ManageIds.class);
        startActivity(i);
    }

    public void sendNewAdDialog(View view) {
        Dialog d = new Dialog(act) ;
        d.setContentView(R.layout.send_ad_dialog);
        Window window = d.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditText title = (EditText) d.findViewById(R.id.editTextTextPersonName);
        EditText text = (EditText) d.findViewById(R.id.editTextTextPersonName2);
        Button cancel = (Button) d.findViewById(R.id.button7);
        Button send = (Button) d.findViewById(R.id.button8);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText() == null || title.getText().toString().isEmpty())
                {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act , getResources().getString(R.string.enterAdTitle),getResources().getString(R.string.enterAdTitle));
                    return;
                }
                if (text.getText() == null || text.getText().toString().isEmpty() ){
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act , getResources().getString(R.string.enterAdText),getResources().getString(R.string.enterAdText));
                    return;
                }
                Loading l = new Loading(act); l.show();
                StringRequest request = new StringRequest(Request.Method.POST, insertNewAdUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        l.close();
                        Log.d("sendAdResponse",response);
                        if (response.equals("1")) {
                            Random r = new Random();
                            int x = r.nextInt(10000);
                            MyApp.sendNotificationsToGroup(MyApp.EMPS, title.getText().toString(), text.getText().toString(), "", x, "AD", MyApp.app, new VolleyCallback() {
                                @Override
                                public void onSuccess() {
                                    d.dismiss();
                                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.sent),getResources().getString(R.string.sent));
                                }
                            });
                        }
                        else {
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error","Sending Message Failed ");
                        }
                    }
                }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        l.close();
                        Log.d("sendAdResponse",error.toString());
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error","Sending Message Failed "+error.toString());
                    }
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> par = new HashMap<String,String>();
                        par.put("Title",title.getText().toString());
                        par.put("Message",text.getText().toString());
                        return par;
                    }
                };
                Volley.newRequestQueue(act).add(request);
            }
        });
        d.show();
    }

    public void sendNewAdWithImage(View view) {
        Intent i = new Intent(act, SendNewAdWithImage.class);
        startActivity(i);
    }

    public void goToEmployeesRates(View view) {
        Intent i = new Intent(act, EmployeesRates.class);
        startActivity(i);
    }

    public void goToMangeEmployees(View view) {
        Intent i = new Intent(act, ManageEmployees.class);
        startActivity(i);
    }
}