package com.syrsoft.ratcoms.HRActivities;

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
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.Employee_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.EmployeesSelect_Adapter;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.ProgressLoadingDialog;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VollyCallback;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SendSalaryReports extends AppCompatActivity {

    Activity act ;
    RecyclerView SelectedUsersRecycler , AllUsersRecycler ;
    LinearLayoutManager sManager , aManager ;
    public static EmployeesSelect_Adapter AllAdapter ;
    public static Employee_Adapter SelectedAdapter ;
    public static List<USER> SelectedUsers ;
    public static boolean[] SelectedArray ;
    Spinner Months ;
    String insertSalaryReportUrl = MyApp.MainUrl+"insertNewSalaryReport.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_send_salary_reports_activity);
        setActivity();
    }

    void setActivity() {
        act = this ;
        SelectedArray = new boolean[MyApp.EMPS.size()];
        for (int i = 0 ; i <MyApp.EMPS.size();i++) {
            SelectedArray[i] = false ;
        }
        SelectedUsers = new ArrayList<USER>();
        SelectedUsersRecycler = (RecyclerView) findViewById(R.id.selectedRecycler);
        AllUsersRecycler = (RecyclerView) findViewById(R.id.allRecycler);
        sManager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        SelectedUsersRecycler.setLayoutManager(sManager);
        aManager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        AllUsersRecycler.setLayoutManager(aManager);
        AllAdapter = new EmployeesSelect_Adapter(MyApp.EMPS);
        SelectedAdapter = new Employee_Adapter(SelectedUsers);
        AllUsersRecycler.setAdapter(AllAdapter);
        SelectedUsersRecycler.setAdapter(SelectedAdapter);
        Months = (Spinner) findViewById(R.id.spinner3);
        String[] monthsArr = {"1","2","3","4","5","6","7","8","9","10","11","12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,monthsArr);
        Months.setAdapter(adapter);
    }

    public void sendSalaryReport(View view) {
        String Month = Months.getSelectedItem().toString();
        sendAllSalaryReports(new VolleyCallback() {
            @Override
            public void onSuccess() {
                Random r = new Random();
                int x = r.nextInt(10000);
                Loading l = new Loading(act); l.show();
                MyApp.sendNotificationsToGroup(SelectedUsers, "SalaryReport", "Salary Report Created", "https://ratco-solutions.com/RatcoManagementSystem/SalaryReports/" + Month + "/", x, "AD", getApplicationContext(), new VolleyCallback() {
                    @Override
                    public void onSuccess() {
                        l.close();
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.sent),getResources().getString(R.string.sent));
                        SelectedUsers.clear();
                        SelectedAdapter.notifyDataSetChanged();
                        for (boolean a :SelectedArray) {
                            a = false;
                        }
                        AllAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    void insertSalaryReport(USER u, int month, VollyCallback callback ) {

        StringRequest request = new StringRequest(Request.Method.POST, insertSalaryReportUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("EmpID", String.valueOf(u.id));
                par.put("JobNumber", String.valueOf(u.JobNumber));
                par.put("Link", "https://ratco-solutions.com/RatcoManagementSystem/SalaryReports/" + month + "/"+u.JobNumber+".pdf");
                par.put("Month",String.valueOf(month));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void sendAllSalaryReports (VolleyCallback callback) {
        ProgressLoadingDialog d = new ProgressLoadingDialog(act,SelectedUsers.size());
        String Month = Months.getSelectedItem().toString();
        for (int i = 0 ;i < SelectedUsers.size();i++) {
            d.setProgress(i+1);
            insertSalaryReport(SelectedUsers.get(i), Integer.parseInt(Month), new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.d("savingReport",s);
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }
        callback.onSuccess();
    }
}