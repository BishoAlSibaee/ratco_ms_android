package com.syrsoft.ratcoms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.syrsoft.ratcoms.HRActivities.ADVANCEPAYMENT_CLASS;
import com.syrsoft.ratcoms.HRActivities.BACKTOWORK_CLASS;
import com.syrsoft.ratcoms.HRActivities.CUSTODY_REQUEST_CLASS;
import com.syrsoft.ratcoms.HRActivities.EXIT_REQUEST_CLASS;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_AdvancePayment_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_Custody_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_EXIT_REQUEST_ADAPTER;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_VacationSalary_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApprovals_Backs_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApprovals_Resignation_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApprovals_Vacation_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE;
import com.syrsoft.ratcoms.HRActivities.JobTitle;
import com.syrsoft.ratcoms.HRActivities.RESIGNATION_CLASS;
import com.syrsoft.ratcoms.HRActivities.VACATIONSALARY_CLASS;
import com.syrsoft.ratcoms.HRActivities.VACATION_CLASS;
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.Maintenance_Order_Adapter_ForProjects;
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.SiteVisitOrders_Adapter;
import com.syrsoft.ratcoms.PROJECTSActivity.MAINTENANCE_ORDER_CLASS;
import com.syrsoft.ratcoms.PROJECTSActivity.SITE_VISIT_ORDER_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountersService extends Service {

    static Service S ;
    static RequestQueue Q ;

    String getSiteVisitOrdersUrl = MyApp.MainUrl+"getSiteVisitOrdersByTo.php";
    public static int HR_COUNTER = 0 ;
    static List<SITE_VISIT_ORDER_class> SiteVisitOrders;
    public static boolean isRunning = false ;

    void setService() {
        S = this ;
        Q = Volley.newRequestQueue(this);
        SiteVisitOrders = new ArrayList<SITE_VISIT_ORDER_class>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("serviceCounter" , "started");
        isRunning = true ;
        setService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false ;
        Log.d("counterService" , "i am off ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public static void getMaintenanceOrders() {

        String getMaintenanceOrdersUrl  ;
        List<MAINTENANCE_ORDER_CLASS> ORDERS = new ArrayList<MAINTENANCE_ORDER_CLASS>() ;
        if (MyApp.db.getUser().JobTitle.equals("Project Manager")) {
            getMaintenanceOrdersUrl = MyApp.MainUrl + "getTechnicalMaintenanceOrders.php";
        } else if (MyApp.db.getUser().JobTitle.equals("Factory Manager") && MyApp.db.getUser().Department.equals("Aluminum Factory")) {
            getMaintenanceOrdersUrl = MyApp.MainUrl + "getAluminumMaintenanceOrders.php";
        }
//            } else if (MyApp.db.getUser().JobNumber == 50007) {
//                getMaintenanceOrdersUrl = MyApp.MainUrl + "getDoorsMaintenanceOrders.php";
//            }
        else {
            getMaintenanceOrdersUrl = MyApp.MainUrl + "getMaintenanceOrderByForwardedTo.php";
        }
        ORDERS.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getMaintenanceOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getMaintenance", response);
                if (response.equals("0")) {

                } else if (response.equals("-1")) {

                } else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ORDERS.add(new MAINTENANCE_ORDER_CLASS(row.getInt("id"), row.getInt("ProjectID"), row.getString("ProjectName"), row.getInt("ClientID"), row.getInt("SalesMan"), row.getDouble("LA"), row.getDouble("LO"), row.getString("DamageDesc"), row.getString("Notes"), row.getString("Contact"), row.getString("ContactName"), row.getString("Date"), row.getInt("ToMaintenance"), row.getInt("ToAluminum"), row.getInt("ToDoors"), row.getInt("ForwardedTo"), row.getString("Response"), row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MyApp.ProjectsCounter = MyApp.ProjectsCounter+ORDERS.size() ;
                    MyApp.MaintenanceCounter = MyApp.MaintenanceCounter + ORDERS.size();
                    if (MainPage.isRunning) {
                        MainPage.setCounters();
                    }
                    if (Projects_Activity.isRunning) {
                        Projects_Activity.setCounters();
                    }
                    S.stopSelf();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("Status", String.valueOf(0));
                par.put("To", String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Q.add(request);
    }

    //_________________________________________

    void getMySiteVisitOrders() {
        SiteVisitOrders.clear();
        //MyApp.SiteVisitOrdersCounter = 0 ;
        String url = "" ;
        url = getSiteVisitOrdersUrl ;
        getOrders(String.valueOf(MyApp.db.getUser().JobNumber),url);
        for (USER u :MyApp.EMPS) {
            if (u.DirectManager == MyApp.db.getUser().JobNumber) {
                getOrders(String.valueOf(u.JobNumber),url);
            }
        }

    }

    void getOrders(String To, String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resultsiteVisit" , response);
                if (response.equals("0")) {
                    //ToastMaker.Show(1,"No Orders",MyApp.app);
                }
                else if (response.equals("-1")) {
                    //ToastMaker.Show(1,"Error",MyApp.app);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            SiteVisitOrders.add(new SITE_VISIT_ORDER_class(row.getInt("id"),row.getInt("SalesMan"),row.getInt("ForwardedTo"),row.getString("VisitReason"),row.getString("ProjectName"),row.getString("ResponsibleName"),row.getString("ResponsibleMobile"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Notes"),row.getString("Date"),row.getString("VisitDate"),row.getString("VisitTime"),row.getString("VisitResult"),row.getString("VisitNotes"),row.getString("DoneDate"),row.getString("DoneTime"),row.getInt("Status")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (SiteVisitOrders.size() > 0) {
                        //MyApp.SiteVisitOrdersCounter = MyApp.SiteVisitOrdersCounter + SiteVisitOrders.size() ;
                        //MyApp.ProjectsCounter = MyApp.ProjectsCounter + MyApp.SiteVisitOrdersCounter ;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("To" , To);
                par.put("Status" , String.valueOf(0));
                return par;
            }
        };
        Q.add(request);
    }

}

//    void getEmps() {
//        EMPS.clear();
//        //MyApp.EMPS.clear();
//        StringRequest request = new StringRequest(Request.Method.POST, getEmpsUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response)
//            {
//                if (!response.equals("0")){
//                    List<Object> lis = JsonToObject.translate(response,USER.class,MyApp.app);
//                    if (lis.size()>0)
//                    {
//                        Log.d("EmpsNumber" , lis.size()+"");
//                        for (Object o : lis) {
//                            USER r = (USER) o;
//                            Log.d("AllEmps" , r.JobTitle);
//                            EMPS.add(r);
//                        }
//                        MyApp.EMPS = EMPS ;
//                    }
//                    getHrOrderTypes();
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }){
//
//        };
//        Q.add(request);
//    }
//
//    void getHrOrderTypes() {
//
//        StringRequest request = new StringRequest(Request.Method.POST, getHrOrdersTypes, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (response != null ) {
//                    if (response.equals("0")) {
//
//                    }
//                    else {
//                        Log.d("hrOrdersResponse" , response );
//                        List<Object> list = JsonToObject.translate(response, HR_ORDER_TYPE.class,MyApp.app);
//                        Types.clear();
//                        for(int i=0 ; i<list.size() ; i++) {
//                            HR_ORDER_TYPE x = (HR_ORDER_TYPE) list.get(i) ;
//                            Types.add(x);
//                        }
//                        MyApp.Types = Types ;
//                        //ordersTypes.setAdapter(adapter);
//                        getJobTitles();
//                        //getResignationAuthEmps();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Q.add(request);
//    }
//
//    void getJobTitles() {
//
//        if (MyApp.db.isLoggedIn()) {
//            StringRequest request = new StringRequest(Request.Method.POST, getJobTitlesUrl, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    if (response != null) {
//                        List<Object> list = JsonToObject.translate(response, JobTitle.class, MyApp.app);
//                        JobTitles.clear();
//                        for (int i = 0; i < list.size(); i++) {
//                            JobTitle j = (JobTitle) list.get(i);
//                            JobTitles.add(j);
//                        }
//                        MyApp.JobTitles = JobTitles;
//                        getResignationAuthEmps();
//                        //getAdvancePaymentsAuthEmps();
//                        //getBacksAuthEmps();
//                        //getVacationAuthEmps();
//                        //getVacationSalariesAuthEmps();
//
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                }
//            });
//            Q.add(request);
//        }
//    }
//
//    void getResignationAuthEmps () {
//        ResignationsAuthsJobtitles.clear();
//        for (int i=0; i<Types.size() ; i++ ) {
//            if (Types.get(i).HROrderName.equals("Resignation")) {
//                if (Types.get(i).Auth1 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
//                }
//                if (Types.get(i).Auth2 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
//                }
//                if (Types.get(i).Auth3 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
//                }
//                if (Types.get(i).Auth4 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
//                }
//                if (Types.get(i).Auth5 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
//                }
//                if (Types.get(i).Auth6 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
//                }
//                if (Types.get(i).Auth7 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
//                }
//                if (Types.get(i).Auth8 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
//                }
//                if (Types.get(i).Auth9 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
//                }
//                if (Types.get(i).Auth10 != 0 ) {
//                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
//                }
//            }
//        }
//        //Log.d("ResignationJobtitles" , ResignationsAuthsJobtitles.size()+"");
//        if (ResignationsAuthsJobtitles.size() > 0 ) {
//            //Log.d("ResignationJobtitles" , "in ");
//            MyApp.ResignationsAuthsJobtitles = ResignationsAuthsJobtitles ;
//            //Log.d("ResignationUsers" , MyApp.db.getUser().DirectManager+"");
//            for (int p=0; p<ResignationsAuthsJobtitles.size();p++) {
//                Log.d("ResignationJobtitles" , ResignationsAuthsJobtitles.get(p).JobTitle+" "+p);
//                if (ResignationsAuthsJobtitles.get(p).JobTitle.equals("Direct Manager")) {
//                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
//                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
//                            ResignationsAuthUsers.add(MyApp.EMPS.get(y));
//                        }
//                    }
//                }
//                else
//                if (USER.searchUserByJobtitle(MyApp.EMPS,ResignationsAuthsJobtitles.get(p).JobTitle) != null ) {
//                    ResignationsAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,ResignationsAuthsJobtitles.get(p).JobTitle));
//                }
//
//            }
//            MyApp.ResignationsAuthUsers = ResignationsAuthUsers ;
//        }
//        //Log.d("ResignationUsers" , ResignationsAuthUsers.size()+" "+MainPage.EMPS.size());
//        getAdvancePaymentsAuthEmps();
//    }
//
//    void getVacationAuthEmps () {
//
//        VacationsAuthJobtitles.clear();
//        for (int i=0; i<Types.size() ; i++ ) {
//            if (Types.get(i).HROrderName.equals("Vacations")) {
//                if (Types.get(i).Auth1 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
//                }
//                if (Types.get(i).Auth2 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
//                }
//                if (Types.get(i).Auth3 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
//                }
//                if (Types.get(i).Auth4 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
//                }
//                if (Types.get(i).Auth5 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
//                }
//                if (Types.get(i).Auth6 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
//                }
//                if (Types.get(i).Auth7 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
//                }
//                if (Types.get(i).Auth8 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
//                }
//                if (Types.get(i).Auth9 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
//                }
//                if (Types.get(i).Auth10 != 0 ) {
//                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
//                }
//            }
//        }
//        Log.d("VacationJobtitles" , VacationsAuthJobtitles.size()+"");
//        if (VacationsAuthJobtitles.size() > 0 ) {
//            MyApp.VacationsAuthJobtitles = VacationsAuthJobtitles ;
//           // Log.d("VacationJobtitles" , "in ");
//           // Log.d("VacationUsers" , MyApp.db.getUser().DirectManager+"");
//            for (int p=0; p<VacationsAuthJobtitles.size();p++) {
//                Log.d("VacationJobtitles" , VacationsAuthJobtitles.get(p).JobTitle+" "+VacationsAuthJobtitles.get(p).id+" "+VacationsAuthJobtitles.get(p).ArabicName+" "+VacationsAuthJobtitles.get(p).Department);
//                if (VacationsAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
//                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
//                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
//                            VacationsAuthUsers.add(MyApp.EMPS.get(y));
//                            break;
//                        }
//                    }
//                }
//                else
//                if (USER.searchUserByJobtitle(MyApp.EMPS,VacationsAuthJobtitles.get(p).JobTitle) != null ) {
//                    VacationsAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,VacationsAuthJobtitles.get(p).JobTitle));
//                }
//            }
//            MyApp.VacationsAuthUsers = VacationsAuthUsers ;
//            Log.d("VacationUsers" , VacationsAuthUsers.size()+"");
//        }
//        //Log.d("VacationUsers" , VacationsAuthUsers.size()+" "+MainPage.EMPS.size()+" "+VacationsAuthUsers.get(0).JobNumber);
//        getVacationSalariesAuthEmps();
//    }
//
//    void getBacksAuthEmps () {
//        BacksAuthJobtitles.clear();
//        for (int i=0; i<Types.size() ; i++ ) {
//            if (Types.get(i).HROrderName.equals("BackToWork")) {
//                if (Types.get(i).Auth1 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
//                }
//                if (Types.get(i).Auth2 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
//                }
//                if (Types.get(i).Auth3 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
//                }
//                if (Types.get(i).Auth4 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
//                }
//                if (Types.get(i).Auth5 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
//                }
//                if (Types.get(i).Auth6 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
//                }
//                if (Types.get(i).Auth7 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
//                }
//                if (Types.get(i).Auth8 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
//                }
//                if (Types.get(i).Auth9 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
//                }
//                if (Types.get(i).Auth10 != 0 ) {
//                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
//                }
//            }
//        }
//        Log.d("backstitles" , BacksAuthJobtitles.size()+"");
//        if (BacksAuthJobtitles.size() > 0 ) {
//            MyApp.BacksAuthJobtitles = BacksAuthJobtitles ;
//            Log.d("backstitles" , "in ");
//            Log.d("VacationUsers" , MyApp.db.getUser().DirectManager+"");
//            for (int p=0; p<BacksAuthJobtitles.size();p++) {
//                Log.d("backstitles" , BacksAuthJobtitles.get(p).JobTitle+" "+p);
//                if (BacksAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
//                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
//                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
//                            BacksAuthUsers.add(MyApp.EMPS.get(y));
//                            break;
//                        }
//                    }
//                }
//                else
//                if (USER.searchUserByJobtitle(MyApp.EMPS,BacksAuthJobtitles.get(p).JobTitle) != null ) {
//                    BacksAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,BacksAuthJobtitles.get(p).JobTitle));
//                }
//            }
//            MyApp.BacksAuthUsers = BacksAuthUsers ;
//        }
//        //Log.d("backstitles" , BacksAuthUsers.size()+" "+MainPage.EMPS.size());
//        getVacationAuthEmps();
//    }
//
//    void getAdvancePaymentsAuthEmps () {
//        AdvancePaymentsAuthJobtitles.clear();
//        for (int i=0; i<Types.size() ; i++ ) {
//            if (Types.get(i).HROrderName.equals("AdvancePayment")) {
//                if (Types.get(i).Auth1 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
//                }
//                if (Types.get(i).Auth2 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
//                }
//                if (Types.get(i).Auth3 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
//                }
//                if (Types.get(i).Auth4 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
//                }
//                if (Types.get(i).Auth5 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
//                }
//                if (Types.get(i).Auth6 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
//                }
//                if (Types.get(i).Auth7 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
//                }
//                if (Types.get(i).Auth8 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
//                }
//                if (Types.get(i).Auth9 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
//                }
//                if (Types.get(i).Auth10 != 0 ) {
//                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
//                }
//            }
//        }
//        Log.d("Advancetitles" , AdvancePaymentsAuthJobtitles.size()+"");
//        if (AdvancePaymentsAuthJobtitles.size() > 0 ) {
//            MyApp.AdvancePaymentsAuthJobtitles = AdvancePaymentsAuthJobtitles ;
//            Log.d("Advancetitles" , "in ");
//            Log.d("AdvanceUsers" , MyApp.db.getUser().DirectManager+"");
//            for (int p=0; p<AdvancePaymentsAuthJobtitles.size();p++) {
//                Log.d("Advancetitles" , AdvancePaymentsAuthJobtitles.get(p).JobTitle+" "+p);
//                if (AdvancePaymentsAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
//                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
//                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
//                            AdvancePaymentaAuthUsers.add(MyApp.EMPS.get(y));
//                            break;
//                        }
//                    }
//                }
//                else
//                if (USER.searchUserByJobtitle(MyApp.EMPS,AdvancePaymentsAuthJobtitles.get(p).JobTitle) != null ) {
//                    AdvancePaymentaAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,AdvancePaymentsAuthJobtitles.get(p).JobTitle));
//                    //Log.d("AdvanceUsers" , AdvancePaymentaAuthUsers.get(p).Token);
//                }
//            }
//            MyApp.AdvancePaymentaAuthUsers = AdvancePaymentaAuthUsers ;
//        }
//        //Log.d("AdvanceUsers" , AdvancePaymentaAuthUsers.size()+" "+MainPage.EMPS.size());
//        getBacksAuthEmps();
//    }
//
//    void getVacationSalariesAuthEmps () {
//        VacationSalaryAuthJobtitles.clear();
//        for (int i=0; i<Types.size() ; i++ ) {
//
//            if (Types.get(i).HROrderName.equals("VacationSalary")) {
//
//                if (Types.get(i).Auth1 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
//                }
//                if (Types.get(i).Auth2 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
//                }
//                if (Types.get(i).Auth3 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
//                }
//                if (Types.get(i).Auth4 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
//                }
//                if (Types.get(i).Auth5 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
//                }
//                if (Types.get(i).Auth6 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
//                }
//                if (Types.get(i).Auth7 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
//                }
//                if (Types.get(i).Auth8 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
//                }
//                if (Types.get(i).Auth9 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
//                }
//                if (Types.get(i).Auth10 != 0 ) {
//                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
//                }
//            }
//        }
//        Log.d("VacationSalarytitles" , VacationSalaryAuthJobtitles.size()+"");
//        if (VacationSalaryAuthJobtitles.size() > 0 ) {
//            MyApp.VacationSalaryAuthJobtitles = VacationSalaryAuthJobtitles;
//            Log.d("VacationSalarytitles" , "in ");
//            Log.d("VacationSalaryUsers" , MyApp.db.getUser().DirectManager+"");
//            for (int p=0; p<VacationSalaryAuthJobtitles.size();p++) {
//                Log.d("VacationSalarytitles" , VacationSalaryAuthJobtitles.get(p).JobTitle+" "+p);
//                if (VacationSalaryAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
//                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
//                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
//                            VacationSalaryAuthUsers.add(MyApp.EMPS.get(y));
//                            break;
//                        }
//                    }
//                }
//                else
//                if (USER.searchUserByJobtitle(MyApp.EMPS,VacationSalaryAuthJobtitles.get(p).JobTitle) != null ) {
//                    VacationSalaryAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,VacationSalaryAuthJobtitles.get(p).JobTitle));
//                }
//            }
//            MyApp.VacationSalaryAuthUsers = VacationSalaryAuthUsers ;
//        }
//        //Log.d("VacationSalaryUsers" , VacationSalaryAuthUsers.size()+" "+MainPage.EMPS.size());
//        getRequestCustodyAuthEmps();
//    }
//
//    void getRequestCustodyAuthEmps () {
//        RequestCustodyAuthsJobTitles.clear();
//        for (int i=0; i<Types.size() ; i++ ) {
//
//            if (Types.get(i).HROrderName.equals("RequestCustody")) {
//                if (Types.get(i).Auth1 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
//                }
//                if (Types.get(i).Auth2 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
//                }
//                if (Types.get(i).Auth3 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
//                }
//                if (Types.get(i).Auth4 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
//                }
//                if (Types.get(i).Auth5 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
//                }
//                if (Types.get(i).Auth6 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
//                }
//                if (Types.get(i).Auth7 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
//                }
//                if (Types.get(i).Auth8 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
//                }
//                if (Types.get(i).Auth9 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
//                }
//                if (Types.get(i).Auth10 != 0 ) {
//                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
//                }
//            }
//        }
//        if (RequestCustodyAuthsJobTitles.size() > 0 ) {
//            MyApp.RequestCustodyAuthsJobTitles = RequestCustodyAuthsJobTitles;
//            Log.d("Custodytitles" , "in ");
//            Log.d("CustodyUsers" , MyApp.db.getUser().DirectManager+"");
//            for (int p=0; p<RequestCustodyAuthsJobTitles.size();p++) {
//                Log.d("Custodytitles" , RequestCustodyAuthsJobTitles.get(p).JobTitle+" "+p);
//                if (RequestCustodyAuthsJobTitles.get(p).JobTitle.equals("Direct Manager")) {
//                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
//                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
//                            CustodyAuthUsers.add(MyApp.EMPS.get(y));
//                            break;
//                        }
//                    }
//                }
//                else
//                if (USER.searchUserByJobtitle(MyApp.EMPS,RequestCustodyAuthsJobTitles.get(p).JobTitle) != null ) {
//                    CustodyAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,RequestCustodyAuthsJobTitles.get(p).JobTitle));
//                }
//            }
//            //Log.d("CustodyUsers" , RequestCustodyAuthsJobTitles.size()+" "+CustodyAuthUsers.size());
//            MyApp.CustodyAuthUsers = CustodyAuthUsers ;
//            getRequestExitAuthEmps();
//        }
//    }
//
//    void getRequestExitAuthEmps () {
//
//        for (int i=0; i<Types.size() ; i++ ) {
//            ExitAuthsJobTitles.clear();
//            if (Types.get(i).HROrderName.equals("RequestExit")) {
//                if (Types.get(i).Auth1 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
//                }
//                if (Types.get(i).Auth2 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
//                }
//                if (Types.get(i).Auth3 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
//                }
//                if (Types.get(i).Auth4 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
//                }
//                if (Types.get(i).Auth5 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
//                }
//                if (Types.get(i).Auth6 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
//                }
//                if (Types.get(i).Auth7 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
//                }
//                if (Types.get(i).Auth8 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
//                }
//                if (Types.get(i).Auth9 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
//                }
//                if (Types.get(i).Auth10 != 0 ) {
//                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
//                }
//            }
//        }
//        if (ExitAuthsJobTitles.size() > 0 ) {
//            MyApp.ExitAuthsJobTitles = ExitAuthsJobTitles;
//            Log.d("Custodytitles" , "in ");
//            Log.d("CustodyUsers" , MyApp.db.getUser().DirectManager+"");
//            for (int p=0; p<ExitAuthsJobTitles.size();p++) {
//                Log.d("Custodytitles" , ExitAuthsJobTitles.get(p).JobTitle+" "+p);
//                if (ExitAuthsJobTitles.get(p).JobTitle.equals("Direct Manager")) {
//                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
//                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
//                            ExitAuthUsers.add(MyApp.EMPS.get(y));
//                            break;
//                        }
//                    }
//                }
//                else
//                if (USER.searchUserByJobtitle(MainPage.EMPS,ExitAuthsJobTitles.get(p).JobTitle) != null ) {
//                    ExitAuthUsers.add(USER.searchUserByJobtitle(MainPage.EMPS,ExitAuthsJobTitles.get(p).JobTitle));
//                }
//            }
//            //Log.d("CustodyUsers" , RequestCustodyAuthsJobTitles.size()+" "+CustodyAuthUsers.size());
//            MyApp.ExitAuthUsers = ExitAuthUsers ;
//            getPurchaseOrdersAuthEmps();
////            MyApp.db.setHRCounter(0);
////            getResignationsForApprove();
////            getVacationsForApprove();
////            getBacksForApprove();
////            getAdvanceForApprove();
////            getVacationSalaryRequests();
////            getCustodyRequests();
////            getExitRequests();
//        }
//
//    }
//
//    void getPurchaseOrdersAuthEmps () {
//        Log.d("purchaseOrdersj" ,"started ");
//        for (int i=0; i<Types.size() ; i++ ) {
//            PurchaseOrderJobTitles.clear();
//            if (Types.get(i).HROrderName.equals("Local Purchase Order")) {
//                if (Types.get(i).Auth1 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
//                }
//                if (Types.get(i).Auth2 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
//                }
//                if (Types.get(i).Auth3 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
//                }
//                if (Types.get(i).Auth4 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
//                }
//                if (Types.get(i).Auth5 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
//                }
//                if (Types.get(i).Auth6 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
//                }
//                if (Types.get(i).Auth7 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
//                }
//                if (Types.get(i).Auth8 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
//                }
//                if (Types.get(i).Auth9 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
//                }
//                if (Types.get(i).Auth10 != 0 ) {
//                    PurchaseOrderJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
//                }
//            }
//        }
//        Log.d("purchaseOrdersj" ,PurchaseOrderJobTitles.size()+" ");
//        if (PurchaseOrderJobTitles.size() > 0 ) {
//            MyApp.PurchaseOrderJobTitles = PurchaseOrderJobTitles;
//
//            for (int p=0; p<PurchaseOrderJobTitles.size();p++) {
//                //Log.d("Custodytitles" , ExitAuthsJobTitles.get(p).JobTitle+" "+p);
//                if (PurchaseOrderJobTitles.get(p).JobTitle.equals("Direct Manager")) {
//                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
//                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
//                            PurchaseOrderAuthUsers.add(MyApp.EMPS.get(y));
//                            break;
//                        }
//                    }
//                }
//                else
//                if (USER.searchUserByJobtitle(MainPage.EMPS,PurchaseOrderJobTitles.get(p).JobTitle) != null ) {
//                    PurchaseOrderAuthUsers.add(USER.searchUserByJobtitle(MainPage.EMPS,PurchaseOrderJobTitles.get(p).JobTitle));
//                }
//            }
//            Log.d("purchaseOrdersE" ,PurchaseOrderAuthUsers.size()+" ");
//            MyApp.PurchaseOrderAuthUsers = PurchaseOrderAuthUsers ;
//            MyApp.db.setHRCounter(0);
//            getResignationsForApprove();
//            getVacationsForApprove();
//            getBacksForApprove();
//            getAdvanceForApprove();
//            getVacationSalaryRequests();
//            getCustodyRequests();
//            getExitRequests();
//        }
//
//    }

//______________________________

//    static void getResignationsForApprove(){
//        if (resignationsList.size()>0){
//            resignationsList.clear();
//           // resignationsAdapter.notifyDataSetChanged();
//        }
//        resignationsList.clear();
//        StringRequest request = new StringRequest(Request.Method.POST, getResignationsForApprovalUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (!response.equals("0")){
//                    List<Object> list = JsonToObject.translate(response, RESIGNATION_CLASS.class,MyApp.app);
//                    resignationsList.clear();
//                    for (int i=0;i<list.size();i++){
//                        RESIGNATION_CLASS r = (RESIGNATION_CLASS) list.get(i);
//                        resignationsList.add(r);
//                    }
//                    Collections.reverse(resignationsList);
//                    setTheListsOfAuthUsersOFResignations();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String,String> par = new HashMap<String, String>();
//                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
//                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
//                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
//                par.put("status",String.valueOf(0) );
//                return par;
//            }
//        };
//        Volley.newRequestQueue(MyApp.app).add(request);
//    }
//
//    static void setTheListsOfAuthUsersOFResignations() {
//        ResignationOrdersAuthUsers.clear();
//        for ( int i=0; i<resignationsList.size();i++) {
//            List<USER> ll = new ArrayList<USER>();
//            for (int j = 0; j < MyApp.ResignationsAuthsJobtitles.size(); j++) {
//                if (MyApp.ResignationsAuthsJobtitles.get(j).JobTitle.equals("Direct Manager")) {
//                    if (USER.searchUserByJobNumber(MyApp.EMPS,resignationsList.get(i).DirectManager) != null ) {
//                        ll.add(USER.searchUserByJobNumber(MyApp.EMPS,resignationsList.get(i).DirectManager));
//                    }
//                }
//                else {
//                    if (USER.searchUserByJobtitle(MyApp.EMPS,MyApp.ResignationsAuthsJobtitles.get(j).JobTitle) != null) {
//                        ll.add(USER.searchUserByJobtitle(MyApp.EMPS,MyApp.ResignationsAuthsJobtitles.get(j).JobTitle));
//                    }
//                }
//            }
//            ResignationOrdersAuthUsers.add(ll);
//        }
//
//        int[] toRemoveRows = new int[resignationsList.size()];
//        for (int i =0 ; i < ResignationOrdersAuthUsers.size();i++) {
//            boolean s = false ;
//            for (USER u : ResignationOrdersAuthUsers.get(i)) {
//                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
//                    s = true ;
//                }
//            }
//            if (!s) {
//                toRemoveRows[i]=1;
////                try {
////                    resignationsList.remove(i);
////                    ResignationOrdersAuthUsers.remove(i);
////                }
////                catch (Exception e){}
//            }
//            else {
//                toRemoveRows[i] = -1 ;
//            }
//        }
//        Log.d("Rlists" , resignationsList.size()+" "+ResignationOrdersAuthUsers.size()+" "+toRemoveRows.length);
//        try {
//            int removed = 0 ;
//            for (int i = 0; i < toRemoveRows.length; i++) {
//                if (toRemoveRows[i] == 1) {
//                    resignationsList.remove(i-removed);
//                    ResignationOrdersAuthUsers.remove(i-removed);
//                    removed++;
//                }
//            }
//        }
//        catch (Exception e)
//        {
//
//        }
//        MyApp.HRCounter = MyApp.HRCounter+ resignationsList.size() ;
//        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + resignationsList.size() ;
//        if (MainPage.isRunning ) {
//            MainPage.setCounters();
//        }
//        if (HR.isRunning) {
//            HR.setHRCounters();
//        }
//    }
//
//    static void getVacationsForApprove(){
//        //resignationsList.clear();
//        if (vacationslist.size()>0){
//            vacationslist.clear();
//           // vacationAdapter.notifyDataSetChanged();
//        }
//        vacationslist.clear();
//        StringRequest request = new StringRequest(Request.Method.POST, getVacationsForApprovalUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (!response.equals("0")){
//                    List<Object> list = JsonToObject.translate(response, VACATION_CLASS.class,MyApp.app);
//                    vacationslist.clear();
//                    VacationOrdersAuthUsers.clear();
//                    for (int i=0;i<list.size();i++){
//                        VACATION_CLASS r = (VACATION_CLASS) list.get(i);
//                        vacationslist.add(r);
//                    }
//                    //vacations.setAdapter(vacationAdapter);
//                    Collections.reverse(vacationslist);
//                    setTheListsOfAuthUsersOFVacations();
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String,String> par = new HashMap<String, String>();
//                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
//                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
//                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
//                par.put("status",String.valueOf(0) );
//                return par;
//            }
//        };
//        Volley.newRequestQueue(MyApp.app).add(request);
//    }
//
//    static void setTheListsOfAuthUsersOFVacations() {
//        VacationOrdersAuthUsers.clear();
//        for ( int i=0; i<vacationslist.size();i++) {
//            List<USER> ll = new ArrayList<USER>();
//            for (int j = 0; j < MyApp.VacationsAuthJobtitles.size(); j++) {
//                if (MyApp.VacationsAuthJobtitles.get(j).JobTitle.equals("Direct Manager")) {
//                    if (USER.searchUserByJobNumber(MyApp.EMPS,vacationslist.get(i).DirectManager) != null ) {
//                        ll.add(USER.searchUserByJobNumber(MyApp.EMPS,vacationslist.get(i).DirectManager));
//                    }
//                }
//                else {
//                    if (USER.searchUserByJobtitle(MyApp.EMPS,MyApp.VacationsAuthJobtitles.get(j).JobTitle) != null) {
//                        ll.add(USER.searchUserByJobtitle(MyApp.EMPS,MyApp.VacationsAuthJobtitles.get(j).JobTitle));
//                    }
//                }
//            }
//            VacationOrdersAuthUsers.add(ll);
//        }
//        int[] toRemoveRows = new int[vacationslist.size()];
//        for (int i =0 ; i < vacationslist.size();i++) {
//            boolean s = false ;
//            for (USER u : VacationOrdersAuthUsers.get(i)) {
//                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
//                    s = true ;
//                }
//            }
//            if (!s) {
//                toRemoveRows[i] = 1 ;
//            }
//            else {
//                toRemoveRows[i] = -1 ;
//            }
//        }
//        //Log.d("Vlists" , vacationslist.size() +" "+VacationOrdersAuthUsers.size()+" "+toRemoveRows.length);
//        try {
//            int removed = 0;
//            for (int i = 0; i < toRemoveRows.length; i++) {
//                if (toRemoveRows[i] == 1) {
//                    vacationslist.remove(i-removed);
//                    VacationOrdersAuthUsers.remove(i-removed);
//                    removed++;
//                }
//            }
//        }
//        catch (Exception e){}
//
//        MyApp.HRCounter = MyApp.HRCounter + vacationslist.size() ;
//        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + vacationslist.size() ;
//
//        if (MainPage.isRunning ) {
//            MainPage.setCounters();
//        }
//        if (HR.isRunning) {
//            HR.setHRCounters();
//        }
//    }
//
//    static void getBacksForApprove(){
//        //resignationsList.clear();
//        if (backList.size()>0){
//            backList.clear();
//           // backsAdapter.notifyDataSetChanged();
//        }
//        backList.clear();
//
//        StringRequest request = new StringRequest(Request.Method.POST, getBacksForApprovalUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (!response.equals("0")){
//                    List<Object> list = JsonToObject.translate(response, BACKTOWORK_CLASS.class,MyApp.app);
//                    backList.clear();
//                    for (int i=0;i<list.size();i++){
//                        BACKTOWORK_CLASS r = (BACKTOWORK_CLASS) list.get(i);
//                        backList.add(r);
//                    }
//                    Collections.reverse(backList);
//                    setTheListsOfAuthUsersOFBacks();
//
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String,String> par = new HashMap<String, String>();
//                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
//                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
//                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
//                par.put("status",String.valueOf(0) );
//                return par;
//            }
//        };
//        Volley.newRequestQueue(MyApp.app).add(request);
//    }
//
//    static void setTheListsOfAuthUsersOFBacks() {
//        BacksOrdersAuthUsers.clear();
//        for ( int i=0; i<backList.size();i++) {
//            List<USER> ll = new ArrayList<USER>();
//            for (int j = 0; j < MyApp.BacksAuthJobtitles.size(); j++) {
//                if (MyApp.BacksAuthJobtitles.get(j).JobTitle.equals("Direct Manager")) {
//                    if (USER.searchUserByJobNumber(MyApp.EMPS,backList.get(i).DirectManager) != null ) {
//                        ll.add(USER.searchUserByJobNumber(MyApp.EMPS,backList.get(i).DirectManager));
//                    }
//                }
//                else {
//                    if (USER.searchUserByJobtitle(MyApp.EMPS,MyApp.BacksAuthJobtitles.get(j).JobTitle) != null) {
//                        ll.add(USER.searchUserByJobtitle(MyApp.EMPS,MyApp.BacksAuthJobtitles.get(j).JobTitle));
//                    }
//                }
//            }
//            BacksOrdersAuthUsers.add(ll);
//        }
//        int[] rowsToRemove = new int[backList.size()];
//        for (int i =0 ; i < BacksOrdersAuthUsers.size();i++) {
//            boolean s = false ;
//            for (USER u : BacksOrdersAuthUsers.get(i)) {
//                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
//                    s = true ;
//                }
//            }
//            if (!s) {
//                //BacksOrdersAuthUsers.remove(i);
//                //backList.remove(i);
//                rowsToRemove[i] = 1 ;
//            }
//            else {
//                rowsToRemove[i] = -1 ;
//            }
//        }
//        //Log.d("Blists",backList.size()+" "+BacksOrdersAuthUsers.size()+" "+rowsToRemove.length);
//        try {
//            int removed = 0 ;
//            for (int i = 0; i < rowsToRemove.length; i++) {
//                if (rowsToRemove[i] == 1) {
//                    BacksOrdersAuthUsers.remove(i-removed);
//                    backList.remove(i-removed);
//                    removed++ ;
//                }
//            }
//        }
//        catch (Exception e){}
//
//        MyApp.HRCounter = MyApp.HRCounter+ backList.size() ;
//        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + backList.size();
//
//        if (MainPage.isRunning ) {
//            MainPage.setCounters();
//        }
//        if (HR.isRunning) {
//            HR.setHRCounters();
//        }
//    }
//
//    static void getAdvanceForApprove(){
//
//        if (advanceList.size()>0){
//            advanceList.clear();
//            //advanceAdapter.notifyDataSetChanged();
//        }
//        advanceList.clear();
//        StringRequest request = new StringRequest(Request.Method.POST, getAdvanceApprovalsUrl , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (!response.equals("0")){
//                    List<Object> list = JsonToObject.translate(response, ADVANCEPAYMENT_CLASS.class,MyApp.app);
//                    Log.d("advanceResponse" , list.size()+"");
//                    advanceList.clear();
//                    for (int i=0;i<list.size();i++){
//                        ADVANCEPAYMENT_CLASS r =(ADVANCEPAYMENT_CLASS) list.get(i);
//                        advanceList.add(r);
//                    }
//                    //Log.d("advanceResponse" , advanceList.size()+"");
//                    Collections.reverse(advanceList);
//                    setTheListsOfAuthUsersOFAdvance();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map <String,String> par = new HashMap<String, String>();
//                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
//                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
//                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
//                par.put("status",String.valueOf(0) );
//                return par;
//            }
//        };
//        Q.add(request);
//    }
//
//    static void setTheListsOfAuthUsersOFAdvance() {
//        AdvancesOrdersAuthUsers.clear();
//        for ( int i=0; i<advanceList.size();i++) {
//            List<USER> ll = new ArrayList<USER>();
//            for (int j = 0; j < MyApp.AdvancePaymentsAuthJobtitles.size(); j++) {
//                if (MyApp.AdvancePaymentsAuthJobtitles.get(j).JobTitle.equals("Direct Manager")) {
//                    if (USER.searchUserByJobNumber(MyApp.EMPS,advanceList.get(i).DirectManager) != null ) {
//                        ll.add(USER.searchUserByJobNumber(MyApp.EMPS,advanceList.get(i).DirectManager));
//                    }
//                }
//                else {
//                    if (USER.searchUserByJobtitle(MyApp.EMPS,MyApp.AdvancePaymentsAuthJobtitles.get(j).JobTitle) != null) {
//                        ll.add(USER.searchUserByJobtitle(MyApp.EMPS,MyApp.AdvancePaymentsAuthJobtitles.get(j).JobTitle));
//                    }
//                }
//            }
//            AdvancesOrdersAuthUsers.add(ll);
//        }
//        int[] rowsToRemove = new int[advanceList.size()];
//        for (int i =0 ; i < AdvancesOrdersAuthUsers.size();i++) {
//            boolean s = false ;
//            for (USER u : AdvancesOrdersAuthUsers.get(i)) {
//                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
//                    s = true ;
//                }
//            }
//            if (!s) {
//                //AdvancesOrdersAuthUsers.remove(i);
//                // advanceList.remove(i);
//                rowsToRemove[i] = 1 ;
//            }
//            else {
//                rowsToRemove[i] = -1 ;
//            }
//        }
//        Log.d("Alists" ,advanceList.size()+" "+AdvancesOrdersAuthUsers.size()+" "+rowsToRemove.length );
//        try {
//            int removed = 0 ;
//            for (int i = 0; i < rowsToRemove.length; i++) {
//                if (rowsToRemove[i] == 1) {
//                    AdvancesOrdersAuthUsers.remove(i-removed);
//                    advanceList.remove(i-removed);
//                    removed++ ;
//                }
//            }
//        }
//        catch (Exception e){}
//
//        MyApp.HRCounter = MyApp.HRCounter + advanceList.size();
//        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + advanceList.size();
//        //Log.d("counterService" , MyApp.db.getHRCounter()+"");
//        if (MainPage.isRunning ) {
//            MainPage.setCounters();
//        }
//        if (HR.isRunning) {
//            HR.setHRCounters();
//        }
//    }
//
//    static void getVacationSalaryRequests(){
//
//        if (vacationSalaryList.size()>0){
//            vacationSalaryList.clear();
//            //vacationsalary_Adapter.notifyDataSetChanged();
//        }
//        vacationSalaryList.clear();
//        StringRequest request = new StringRequest(Request.Method.POST, getVacationSalaryRequestsForApprove , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (!response.equals("0")){
//                    List<Object> list = JsonToObject.translate(response, VACATIONSALARY_CLASS.class,MyApp.app);
//                    Log.d("vsalaryAResponse" , list.size()+"");
//                    vacationSalaryList.clear();
//                    for (int i=0;i<list.size();i++){
//                        VACATIONSALARY_CLASS r =(VACATIONSALARY_CLASS) list.get(i);
//                        vacationSalaryList.add(r);
//                    }
//                    //Log.d("vsalaryAResponse" , vacationSalaryList.size()+"");
//                    Collections.reverse(vacationSalaryList);
//                    setTheListsOfAuthUsersOFVSalary();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map <String,String> par = new HashMap<String, String>();
//                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
//                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
//                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
//                par.put("status",String.valueOf(0) );
//                return par;
//            }
//        };
//        Q.add(request);
//    }
//
//    static void setTheListsOfAuthUsersOFVSalary() {
//        VacationSalaryOrdersAuthUsers.clear();
//        for ( int i=0; i<vacationSalaryList.size();i++) {
//            List<USER> ll = new ArrayList<USER>();
//            for (int j = 0; j < MyApp.VacationSalaryAuthJobtitles.size(); j++) {
//                if (MyApp.VacationSalaryAuthJobtitles.get(j).JobTitle.equals("Direct Manager")) {
//                    if (USER.searchUserByJobNumber(MyApp.EMPS,vacationSalaryList.get(i).DirectManager) != null ) {
//                        ll.add(USER.searchUserByJobNumber(MyApp.EMPS,vacationSalaryList.get(i).DirectManager));
//                    }
//                }
//                else {
//                    if (USER.searchUserByJobtitle(MyApp.EMPS,MyApp.VacationSalaryAuthJobtitles.get(j).JobTitle) != null) {
//                        ll.add(USER.searchUserByJobtitle(MyApp.EMPS,MyApp.VacationSalaryAuthJobtitles.get(j).JobTitle));
//                    }
//                }
//            }
//            VacationSalaryOrdersAuthUsers.add(ll);
//        }
//        int[] rowaToRemove = new int[vacationSalaryList.size()] ;
//        for (int i =0 ; i < VacationSalaryOrdersAuthUsers.size();i++) {
//            boolean s = false ;
//            for (USER u : VacationSalaryOrdersAuthUsers.get(i)) {
//                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
//                    s = true ;
//                }
//            }
//            if (!s) {
//                rowaToRemove[i] = 1 ;
////                try {
////                    VacationSalaryOrdersAuthUsers.remove(i);
////                    vacationSalaryList.remove(i);
////                }
////                catch (Exception e ) {
////
////                }
//            }
//            else {
//                rowaToRemove[i] = -1;
//            }
//        }
//        Log.d("VSlists" , vacationSalaryList.size()+" "+ VacationSalaryOrdersAuthUsers.size()+" "+rowaToRemove.length);
//        try {
//            int removed = 0 ;
//            for (int i = 0; i < rowaToRemove.length; i++) {
//                if (rowaToRemove[i] == 1) {
//                    vacationSalaryList.remove(i-removed);
//                    VacationSalaryOrdersAuthUsers.remove(i-removed);
//                    removed++ ;
//                }
//            }
//        }
//        catch (Exception e){}
//        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + vacationSalaryList.size();
//        MyApp.HRCounter = MyApp.HRCounter + vacationSalaryList.size();
//        //Log.d("counterService" , MyApp.db.getHRCounter()+"");
//        if (MainPage.isRunning ) {
//            MainPage.setCounters();
//        }
//        if (HR.isRunning) {
//            HR.setHRCounters();
//        }
//    }
//
//    static void getCustodyRequests(){
//
//        if (custodyRequestList.size()>0){
//            custodyRequestList.clear();
//            //custodyRequest_Adapter.notifyDataSetChanged();
//        }
//        custodyRequestList.clear();
//        StringRequest request = new StringRequest(Request.Method.POST, getCustodyForApprovalURL , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (!response.equals("0")){
//                    List<Object> list = JsonToObject.translate(response, CUSTODY_REQUEST_CLASS.class,MyApp.app);
//                    Log.d("custodyAResponse" , list.size()+"");
//
//                    for (int i=0;i<list.size();i++){
//                        CUSTODY_REQUEST_CLASS r =(CUSTODY_REQUEST_CLASS) list.get(i);
//                        custodyRequestList.add(r);
//                    }
//                    //Log.d("AcustodyAResponse" , custodyRequestList.size()+" "+MyApp.EMPS.size());
//                    Collections.reverse(custodyRequestList);
//                    setTheListsOfAuthUsersOFCustody();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map <String,String> par = new HashMap<String, String>();
//                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
//                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
//                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
//                par.put("status",String.valueOf(0) );
//                return par;
//            }
//        };
//        Q.add(request);
//    }
//
//    static void setTheListsOfAuthUsersOFCustody() {
//        CustodyOrdersAuthUsers.clear();
//        for ( int i=0; i<custodyRequestList.size();i++) {
//            List<USER> ll = new ArrayList<USER>();
//            for (int j = 0; j < MyApp.RequestCustodyAuthsJobTitles.size(); j++) {
//                if (MyApp.RequestCustodyAuthsJobTitles.get(j).JobTitle.equals("Direct Manager")) {
//                    if (USER.searchUserByJobNumber(MyApp.EMPS,custodyRequestList.get(i).DirectManager) != null ) {
//                        ll.add(USER.searchUserByJobNumber(MyApp.EMPS,custodyRequestList.get(i).DirectManager));
//                    }
//                }
//                else {
//                    if (USER.searchUserByJobtitle(MyApp.EMPS,MyApp.RequestCustodyAuthsJobTitles.get(j).JobTitle) != null) {
//                        ll.add(USER.searchUserByJobtitle(MyApp.EMPS,MyApp.RequestCustodyAuthsJobTitles.get(j).JobTitle));
//                    }
//                }
//            }
//            CustodyOrdersAuthUsers.add(ll);
//        }
//        Log.d("Clists" ,custodyRequestList.size()+" "+CustodyOrdersAuthUsers.size() );
//        int[] rowsToRemove = new int[custodyRequestList.size()];
//        for (int i =0 ; i < CustodyOrdersAuthUsers.size();i++) {
//            Log.d("approvViewProblem", i+" "+CustodyOrdersAuthUsers.size());
//            boolean s = false ;
//            for (USER u : CustodyOrdersAuthUsers.get(i)) {
//                Log.d("approvViewProblem", u.FirstName+" "+u.LastName);
//                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
//                    s = true ;
//                }
//            }
//            if (!s) {
//                //CustodyOrdersAuthUsers.remove(i);
//                rowsToRemove[i] = 1;
//            }
//            else {
//                rowsToRemove[i] = -1 ;
//            }
//        }
//        Log.d("Clists" ,custodyRequestList.size()+" "+CustodyOrdersAuthUsers.size()+" "+rowsToRemove.length );
//        try {
//            int removed = 0 ;
//            for (int i = 0; i < rowsToRemove.length; i++) {
//                if (rowsToRemove[i] == 1) {
//                    custodyRequestList.remove(i-removed);
//                    CustodyOrdersAuthUsers.remove(i-removed);
//                    removed++;
//                }
//            }
//        }
//        catch (Exception e){
//            //Log.d("Clists" ,custodyRequestList.size()+" "+CustodyOrdersAuthUsers.size()+" "+rowsToRemove.length + " "+e.getMessage() );
//        }
//
//        MyApp.HRCounter = MyApp.HRCounter + custodyRequestList.size();
//        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter +custodyRequestList.size();
//        //Log.d("counterService" , MyApp.db.getHRCounter()+"");
//        if (MainPage.isRunning ) {
//            MainPage.setCounters();
//        }
//        if (HR.isRunning) {
//            HR.setHRCounters();
//        }
//    }
//
//    static void getExitRequests(){
//
//        exiteList.clear();
//        StringRequest request = new StringRequest(Request.Method.POST, getExitRequestsForApprovUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("exitAResponse" , response);
//                //ToastMaker.Show(1,response,act);
//                if (!response.equals("0")){
//                    List<Object> list = JsonToObject.translate(response, EXIT_REQUEST_CLASS.class,MyApp.app);
//                    Log.d("exitAResponse" , list.size()+"");
//
//                    for (int i=0;i<list.size();i++){
//                        EXIT_REQUEST_CLASS r =(EXIT_REQUEST_CLASS) list.get(i);
//                        exiteList.add(r);
//                    }
//                    //Log.d("AcustodyAResponse" , custodyRequestList.size()+" "+MyApp.EMPS.size());
//                    Collections.reverse(exiteList);
//                    setTheListsOfAuthUsersOFExit();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("exitAResponse" , error.toString());
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map <String,String> par = new HashMap<String, String>();
//                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
//                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
//                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
//                par.put("status",String.valueOf(0) );
//                return par;
//            }
//        };
//        Volley.newRequestQueue(MyApp.app).add(request);
//    }
//
//    static void setTheListsOfAuthUsersOFExit() {
//        ExitOrdersAuthUsers.clear();
//        for ( int i=0; i<exiteList.size();i++) {
//            List<USER> ll = new ArrayList<USER>();
//            for (int j = 0; j < MyApp.ExitAuthsJobTitles.size(); j++) {
//                if (MyApp.ExitAuthsJobTitles.get(j).JobTitle.equals("Direct Manager")) {
//                    if (USER.searchUserByJobNumber(MyApp.EMPS,exiteList.get(i).DirectManager) != null ) {
//                        ll.add(USER.searchUserByJobNumber(MyApp.EMPS,exiteList.get(i).DirectManager));
//                    }
//                }
//                else {
//                    if (USER.searchUserByJobtitle(MyApp.EMPS,MyApp.ExitAuthsJobTitles.get(j).JobTitle) != null) {
//                        ll.add(USER.searchUserByJobtitle(MyApp.EMPS,MyApp.ExitAuthsJobTitles.get(j).JobTitle));
//                    }
//                }
//            }
//            ExitOrdersAuthUsers.add(ll);
//        }
//        Log.d("Clists" ,exiteList.size()+" "+ExitOrdersAuthUsers.size() );
//        int[] rowsToRemove = new int[exiteList.size()];
//        for (int i =0 ; i < ExitOrdersAuthUsers.size();i++) {
//            Log.d("approvViewProblem", i+" "+ExitOrdersAuthUsers.size());
//            boolean s = false ;
//            for (USER u : ExitOrdersAuthUsers.get(i)) {
//                Log.d("approvViewProblem", u.FirstName+" "+u.LastName);
//                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
//                    s = true ;
//                }
//            }
//            if (!s) {
//                //CustodyOrdersAuthUsers.remove(i);
//                rowsToRemove[i] = 1;
//            }
//            else {
//                rowsToRemove[i] = -1 ;
//            }
//        }
//        Log.d("Clists" ,exiteList.size()+" "+ExitOrdersAuthUsers.size()+" "+rowsToRemove.length );
//        try {
//            int removed = 0 ;
//            for (int i = 0; i < rowsToRemove.length; i++) {
//                if (rowsToRemove[i] == 1) {
//                    exiteList.remove(i-removed);
//                    ExitOrdersAuthUsers.remove(i-removed);
//                    removed++;
//                }
//            }
//        }
//        catch (Exception e){
//            Log.d("Clists" ,exiteList.size()+" "+ExitOrdersAuthUsers.size()+" "+rowsToRemove.length + " "+e.getMessage() );
//        }
//        MyApp.HRCounter = MyApp.HRCounter + exiteList.size();
//        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + exiteList.size();
//        //Log.d("counterService" , MyApp.db.getHRCounter()+"");
//        if (MainPage.isRunning ) {
//            MainPage.setCounters();
//        }
//        if (HR.isRunning) {
//            HR.setHRCounters();
//        }
//    }

//_________________________________________
//resignationsList = new ArrayList<RESIGNATION_CLASS>();
//vacationslist = new ArrayList<VACATION_CLASS>();
//backList = new ArrayList<BACKTOWORK_CLASS>();
//advanceList = new ArrayList<ADVANCEPAYMENT_CLASS>();
//vacationSalaryList = new ArrayList<VACATIONSALARY_CLASS>();
//custodyRequestList = new ArrayList<CUSTODY_REQUEST_CLASS>();
//exiteList = new ArrayList<EXIT_REQUEST_CLASS>();
//VacationOrdersAuthUsers = new ArrayList<List<USER>>();
//ResignationOrdersAuthUsers = new ArrayList<List<USER>>();
//VacationSalaryOrdersAuthUsers = new ArrayList<List<USER>>();
//CustodyOrdersAuthUsers = new ArrayList<List<USER>>();
//BacksOrdersAuthUsers = new ArrayList<List<USER>>();
//AdvancesOrdersAuthUsers = new ArrayList<List<USER>>();
//ExitOrdersAuthUsers = new ArrayList<List<USER>>();
//ExitAuthUsers =new  ArrayList<USER>();
//Types = new ArrayList<HR_ORDER_TYPE>();
//JobTitles = new ArrayList<JobTitle>();
//ResignationsAuthsJobtitles = new ArrayList<JobTitle>();
//VacationsAuthJobtitles = new ArrayList<JobTitle>();
//BacksAuthJobtitles = new ArrayList<JobTitle>();
//AdvancePaymentsAuthJobtitles = new ArrayList<JobTitle>();
//VacationSalaryAuthJobtitles = new ArrayList<JobTitle>();
//RequestCustodyAuthsJobTitles = new ArrayList<JobTitle>();
//ExitAuthsJobTitles = new ArrayList<JobTitle>();
//PurchaseOrderJobTitles = new ArrayList<JobTitle>();
//ResignationsAuthUsers = new ArrayList<USER>();
//VacationsAuthUsers = new ArrayList<USER>();
//BacksAuthUsers = new ArrayList<USER>();
//AdvancePaymentaAuthUsers = new ArrayList<USER>();
//VacationSalaryAuthUsers = new ArrayList<USER>();
//CustodyAuthUsers = new ArrayList<USER>();
//PurchaseOrderAuthUsers = new ArrayList<USER>();
//EMPS = new ArrayList<USER>();

//public static List<JobTitle> PurchaseOrderJobTitles,ExitAuthsJobTitles,JobTitles , RequestCustodyAuthsJobTitles , ResignationsAuthsJobtitles , VacationsAuthJobtitles , BacksAuthJobtitles , AdvancePaymentsAuthJobtitles , VacationSalaryAuthJobtitles ;
//public static List<USER>  PurchaseOrderAuthUsers,ExitAuthUsers,CustodyAuthUsers ,  ResignationsAuthUsers , VacationsAuthUsers , BacksAuthUsers , AdvancePaymentaAuthUsers , VacationSalaryAuthUsers ;
//public static List<USER> EMPS ;
//static List<HR_ORDER_TYPE> Types ;
//static MyApprovals_Resignation_Adapter resignationsAdapter ;
//static MyApprovals_Vacation_Adapter vacationAdapter;
//static MyApprovals_Backs_Adapter backsAdapter ;
//static MyApproval_AdvancePayment_Adapter advanceAdapter ;
//static MyApproval_VacationSalary_Adapter vacationsalary_Adapter ;
//static MyApproval_Custody_Adapter custodyRequest_Adapter ;
//static List<RESIGNATION_CLASS> resignationsList ;
//static List<VACATION_CLASS> vacationslist ;
//static List<BACKTOWORK_CLASS> backList ;
//static List<ADVANCEPAYMENT_CLASS> advanceList ;
//static List<VACATIONSALARY_CLASS> vacationSalaryList ;
//static List<CUSTODY_REQUEST_CLASS> custodyRequestList ;
//static List<EXIT_REQUEST_CLASS> exiteList ;
//public static List<List<USER>> ExitOrdersAuthUsers,VacationOrdersAuthUsers , ResignationOrdersAuthUsers , VacationSalaryOrdersAuthUsers , CustodyOrdersAuthUsers , BacksOrdersAuthUsers,AdvancesOrdersAuthUsers ;
//private String getEmpsUrl="https://ratco-solutions.com/RatcoManagementSystem/getAllEmps.php" ;
//static String getResignationsForApprovalUrl = "https://ratco-solutions.com/RatcoManagementSystem/getResignationsForApproval.php" ;
//static String getVacationsForApprovalUrl = "https://ratco-solutions.com/RatcoManagementSystem/getVacationsForApproval.php";
//static String getBacksForApprovalUrl = "https://ratco-solutions.com/RatcoManagementSystem/getBacksForApproval.php";
//static String getAdvanceApprovalsUrl = "https://ratco-solutions.com/RatcoManagementSystem/getAdvancePaymentsForApproval.php";
//static String getVacationSalaryRequestsForApprove = "https://ratco-solutions.com/RatcoManagementSystem/getVacationsalaryForApproval.php";
//static String getCustodyForApprovalURL = "https://ratco-solutions.com/RatcoManagementSystem/getRequestCustodyForApproval.php" ;
//private String getHrOrdersTypes = "https://ratco-solutions.com/RatcoManagementSystem/getHrOrdersTypes.php" ;
//private String getJobTitlesUrl = "https://ratco-solutions.com/RatcoManagementSystem/getJobtitles.php";
//static String getExitRequestsForApprovUrl = MyApp.MainUrl+"getExitRequestForApproval.php" ;