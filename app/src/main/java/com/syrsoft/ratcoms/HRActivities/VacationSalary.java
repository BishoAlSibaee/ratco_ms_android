package com.syrsoft.ratcoms.HRActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.VolleyCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VacationSalary extends AppCompatActivity {

    private Spinner vacations ;
    private List<VACATION_CLASS> VacationsList ;
    private Activity act ;
    private String getVacationsUrl = "https://ratco-solutions.com/RatcoManagementSystem/getVacations.php";
    private String[] vacationsArray ;
    private String sendVacationSalary = "https://ratco-solutions.com/RatcoManagementSystem/insertVacationSalaryRequest.php" ;
    private VACATION_CLASS selectedVacation ;
    ArrayAdapter<String> adapter ;
    LinearLayout vacationLayout , noVacationLayout ;
    RadioButton vacationRB , noVacationRB ;
    EditText notes ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_salary);
        setActivity();
        setActivityActions();
        getMyVacations();
    }

    void setActivity() {
        act = this ;
        VacationsList = new ArrayList<VACATION_CLASS>();
        vacations = (Spinner) findViewById(R.id.vacationSalary_vacation);
        vacations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVacation = VacationsList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vacationRB = (RadioButton) findViewById(R.id.vacation);
        noVacationRB = (RadioButton) findViewById(R.id.noVacation);
        vacationLayout = (LinearLayout) findViewById(R.id.vacationLayout);
        vacationLayout.setVisibility(View.GONE);
        noVacationLayout = (LinearLayout) findViewById(R.id.noVacationLayout);
        noVacationLayout.setVisibility(View.GONE);
        notes = (EditText) findViewById(R.id.notes);
    }

    void setActivityActions() {
        vacationRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    noVacationRB.setChecked(false);
                    vacationLayout.setVisibility(View.VISIBLE);
                    noVacationLayout.setVisibility(View.GONE);
                }
            }
        });
        noVacationRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vacationRB.setChecked(false);
                    vacationLayout.setVisibility(View.GONE);
                    noVacationLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void getMyVacations(){
        if (VacationsList.size()>0){
            VacationsList.clear();
            //vacation_adapter.notifyDataSetChanged();
        }
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getVacationsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("vacationsResponse" , response);
                d.close();
                if (!response.equals("0")){
                    List<Object> list = JsonToObject.translate(response,VACATION_CLASS.class,act);
                    Log.d("vacationsResponse" , list.size()+"");
                    VacationsList.clear();
                    vacationsArray = new String[list.size()];
                    for (int i=0;i<list.size();i++){
                        VACATION_CLASS r =(VACATION_CLASS) list.get(i);
                        VacationsList.add(r);
                        vacationsArray[i] ="No "+VacationsList.get(i).id+ " Date: " +VacationsList.get(i).StartDate+" - "+VacationsList.get(i).EndDate ;
                    }
                    Log.d("vacationsResponse" , VacationsList.size()+"");
                    //vacations.setAdapter(vacation_adapter);
                    if (VacationsList.size() > 0 )
                    {
                        adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,vacationsArray);
                        vacations.setAdapter(adapter);
                    }
                    else
                    {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.noAcceptedVacations) , "");
                    }
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
                Map <String,String> par = new HashMap<String, String>();
                par.put("id" , String.valueOf(MyApp.db.getUser().id));
                par.put("status" , String.valueOf("1"));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void sendVacationSalaryRequest(View view) {

//        if (vacations.getSelectedItem() == null || vacations.getSelectedItem().toString().isEmpty() )
//        {
//            MESSAGE_DIALOG m = new MESSAGE_DIALOG( act , getResources().getString(R.string.selectVacationTitle),getResources().getString(R.string.selectVacationMessage));
//            return;
//        }
        if (noVacationRB.isChecked() && !vacationRB.isChecked()){
            if (notes.getText() == null || notes.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"please enter notes ",act);
                return;
            }
        }
        if (!noVacationRB.isChecked() && vacationRB.isChecked()) {
            if (selectedVacation == null ) {
                ToastMaker.Show(1,"please select vacation ",act);
                return;
            }
        }

        Loading d = new Loading(act) ; d.show();
        StringRequest request = new StringRequest(Request.Method.POST, sendVacationSalary , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("requestVacationsalary" , response);
                d.close();
                if (response.equals("1"))
                {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.sent),"");
                    vacations.setVisibility(View.INVISIBLE);
                    MyApp.sendNotificationsToGroup(MyApp.VacationSalaryAuthUsers, getResources().getString(R.string.vacationSalary), getResources().getString(R.string.vacationSalary), MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "NewVacationSalary", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else if (response.equals("0"))
                {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act , getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg));
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
                par.put("EmpID" , String.valueOf( MyApp.db.getUser().id ));
                par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("FirstName" , MyApp.db.getUser().FirstName);
                par.put("LastName" , MyApp.db.getUser().LastName);
                par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                par.put("DirectManager" , String.valueOf(MyApp.db.getUser().DirectManager));
                par.put("DirectManagerName" , MyApp.DIRECT_MANAGER.FirstName+" "+MyApp.DIRECT_MANAGER.LastName);
                if (vacationRB.isChecked() && !noVacationRB.isChecked()) {
                    if (selectedVacation == null) {
                        ToastMaker.Show(1,"please select vacation" , act);
                    }
                    else {
                        par.put("VacationID" , String.valueOf(selectedVacation.id));
                        par.put("StartDate" , selectedVacation.StartDate);
                        par.put("EndDate" , selectedVacation.EndDate);
                        par.put("VacationType" , String.valueOf(selectedVacation.VacationType));
                    }
                }
                else {
                    par.put("VacationID" , String.valueOf( 1 ));
                    par.put("StartDate" , "");
                    par.put("EndDate" , "");
                    par.put("VacationType" , String.valueOf( ""));
                }
                par.put("SendDate" , date );
                if (notes.getText() != null && !notes.getText().toString().isEmpty()) {
                    par.put("Notes",notes.getText().toString());
                }
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);

    }
}