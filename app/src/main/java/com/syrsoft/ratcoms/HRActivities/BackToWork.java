package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

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
import com.syrsoft.ratcoms.VolleyCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BackToWork extends AppCompatActivity {

    private Activity act ;
    private TextView BackDate , EndDate ;
    private CalendarView BackDateSelector ;
    private String getVacation = "https://ratco-solutions.com/RatcoManagementSystem/getEmployeeVacation.php";
    private String sendBackToWorkUrl = "https://ratco-solutions.com/RatcoManagementSystem/insertBackToWork.php" ;
    private List<VACATION_CLASS> vacationList ;
    private Button send ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_back_to_work);
        setActivity();

    }

    void setActivity()
    {
        act = this ;
        BackDate = (TextView) findViewById(R.id.Backtowork_selectedStartDate);
        EndDate = (TextView) findViewById(R.id.Backtowork_EndDate);
        BackDateSelector = (CalendarView) findViewById(R.id.Backtowork_backDate);
        BackDateSelector.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                BackDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
            }
        });
        vacationList = new ArrayList<VACATION_CLASS>();
        send = (Button) findViewById(R.id.button3);
        getVacation();
    }

    void getVacation()
    {
        Loading d = new Loading(act); d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getVacation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //Log.d("backVacations" , response);
                //ToastMaker.Show(1,response,act);
                d.close();
                if (response == null )
                {
                    Log.d("backVacations" , "null");
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg));
                    //send.setEnabled(false);
                }
                else if (response.equals("0"))
                {
                    Log.d("backVacations" , "0");
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.noVacationُTitle),getResources().getString(R.string.noVacationMessage));
                    send.setEnabled(false);
                }
                else
                {
                    List<Object> list = JsonToObject.translate(response,VACATION_CLASS.class,act);
                    Log.d("vacationsResponse" , list.size()+" "+response);
                    vacationList.clear();
                    if (list.size()>0)
                    {
                        for (int i=0;i<list.size();i++){
                            VACATION_CLASS r =(VACATION_CLASS) list.get(i);
                            vacationList.add(r);
                        }
                        EndDate.setText(vacationList.get(vacationList.size()-1).EndDate);
                    }
                    else
                    {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.noVacationُTitle),getResources().getString(R.string.noVacationMessage));
                        send.setEnabled(false);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
                Log.d("backVacations" , error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Log.d("vacationsResponse" , MyApp.db.getUser().id+" "+MyApp.db.getUser().JobNumber+" ");
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Log.d("thedate" , date);
                Map<String,String> par = new HashMap<String, String>();
                par.put("id", String.valueOf( MyApp.db.getUser().id));
                par.put("JobNumber" , String.valueOf( MyApp.db.getUser().JobNumber));
                par.put("now", date);
                return par;
            }
        };

        Volley.newRequestQueue(act).add(request);
    }

    public void sendBackToWork(View view)
    {
        if (BackDate.getText() == null || BackDate.getText().toString().isEmpty())
        {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.dateofback),getResources().getString(R.string.selectBackDate));
            return;
        }
        else
        {
            Loading d = new Loading(act); d.show();
            StringRequest request = new StringRequest(Request.Method.POST, sendBackToWorkUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("sendbackerror" , response);
                    d.close();
                    if (response.equals("1"))
                    {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.sent) , getResources().getString(R.string.sent));
                        MyApp.sendNotificationsToGroup(MyApp.BacksAuthUsers, getResources().getString(R.string.backtowork), getResources().getString(R.string.backtowork), MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "NewBackToWork", act, new VolleyCallback() {
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
                    Log.e("sendbackerror" , error.getMessage());
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> par = new HashMap<String, String>();
                    par.put("EmpID", String.valueOf(MyApp.db.getUser().id));
                    par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                    par.put("FName" , MyApp.db.getUser().FirstName);
                    par.put("LName" , MyApp.db.getUser().LastName);
                    par.put("DirectManager" , String.valueOf( MyApp.db.getUser().DirectManager));
                    par.put("DirectManagerName" , MyApp.DIRECT_MANAGER.FirstName+" "+MyApp.DIRECT_MANAGER.LastName );
                    par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                    par.put("VacationID" , String.valueOf(vacationList.get(vacationList.size()-1).id));
                    par.put("EndDate" , EndDate.getText().toString());
                    par.put("BackDate" , BackDate.getText().toString());
                    Log.e("sendbackerror" ,String.valueOf(MyApp.db.getUser().id)+" "+String.valueOf(MyApp.db.getUser().JobNumber)+" "+MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName+" "+String.valueOf( MyApp.db.getUser().DirectManager)+" "+MyApp.DIRECT_MANAGER.FirstName+" "+MyApp.DIRECT_MANAGER.LastName +" "+MyApp.db.getUser().JobTitle+" "+String.valueOf(vacationList.get(vacationList.size()-1).id));
                    return par;
                }
            };
            Volley.newRequestQueue(act).add(request);
        }

    }
}