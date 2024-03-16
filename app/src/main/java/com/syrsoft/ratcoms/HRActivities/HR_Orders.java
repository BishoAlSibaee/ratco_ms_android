package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.AdvancePayment_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.Backtowork_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.Bonus_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.Chamber_Commerce_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.CustodyRequest_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.ExitRequest_ADAPTER;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_BONUS_REQUEST_ADAPTER;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.Punishment_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.Resignation_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.VacationSalary_Adapter;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HR_Orders extends AppCompatActivity {

    static Activity act;
    static RecyclerView Bonuses, exits, resignations, advancePayments, Punishment_Recycler, vacations, backTooWork, custodyRequestRecycler, vacationSalaryRequest, Chamber_Recycler;
    static Resignation_Adapter resignation_adapter;
    static Vacations_Adapter vacation_adapter;
    static Backtowork_Adapter backtowork_adapter;
    static AdvancePayment_Adapter advance_adapter;
    static Punishment_Adapter punishment_adapter;
    static Chamber_Commerce_Adapter chamber_commerce_adapter;
    static VacationSalary_Adapter vacationsalary_Adapter;
    static CustodyRequest_Adapter custodyRequest_adapter;
    static ExitRequest_ADAPTER exitAdapter;
    static Bonus_Adapter bonusAdapter;
    static List<RESIGNATION_CLASS> ResignationsList = new ArrayList<RESIGNATION_CLASS>();
    static List<VACATION_CLASS> VacationsList = new ArrayList<VACATION_CLASS>();
    static List<BACKTOWORK_CLASS> BackToWork = new ArrayList<BACKTOWORK_CLASS>();
    static List<ADVANCEPAYMENT_CLASS> AdvancePayment = new ArrayList<ADVANCEPAYMENT_CLASS>();
    static List<PUNISHMENT_CLASS> punishmentList = new ArrayList<PUNISHMENT_CLASS>();
    static List<CHAMBER_COMMERCE_CLASS>chamberCommerceList = new ArrayList<>();
    static List<VACATIONSALARY_CLASS> vacationsalaryList = new ArrayList<VACATIONSALARY_CLASS>();
    static List<CUSTODY_REQUEST_CLASS> custodyrequestList = new ArrayList<CUSTODY_REQUEST_CLASS>();
    static List<EXIT_REQUEST_CLASS> exitRequestList = new ArrayList<EXIT_REQUEST_CLASS>();
    static List<BONUS> bonusRequestsList = new ArrayList<>();
    static RecyclerView.LayoutManager bonusManager, exitManager, resignationsManager, vacationsManager, backtoworkManager, advanceManager, punishmentManager, ChamberManager, vacationsalaryManager, custodyRequestManager;
    static String getResignationsUrl = MyApp.MainUrl + "getResignations.php";
    static String getVacationsUrl = MyApp.MainUrl + "getVacations.php";
    static String getBackToWorksUrl = MyApp.MainUrl + "getBackToWorks.php";
    static String getAdvancePayments = MyApp.MainUrl + "getAdvancePayments.php";
    static String getVacationSalaryRequestUrl = MyApp.MainUrl + "getVacationSalaryRequests.php";
    static String getCustodyRequestsUrl = MyApp.MainUrl + "getRequestCustody.php";
    static String getExitRequestsUrl = MyApp.MainUrl + "getExitRequests.php";
    static String getBonusesRequestsUrl = MyApp.MainUrl + "getBonuses.php";
    static String getPunishmentRequestsUrl = "https://ratco-solutions.com/RatcoManagementSystem/NewOptions/getRequestDiscount.php";
    static String getRequestChamberCommerce = "https://ratco-solutions.com/RatcoManagementSystem/NewOptions/getRequestChamberCommerce.php";

    private String getHrOrdersTypes = "https://ratco-solutions.com/RatcoManagementSystem/getHrOrdersTypes.php";
    private String getJobTitlesUrl = "https://ratco-solutions.com/RatcoManagementSystem/getJobtitles.php";
    private RadioButton done, undone;
    static int Status = 0;
    static List<HR_ORDER_TYPE> Types;
    LinearLayout ResignationsLayout, BonusLayout, AdvanceLayout, PunishmentLayout, BackLayout, CustodyLayout, ExitLayout, VacationLayout, VSalaryLayout, ChamberLayout;
    static TextView ResignationsNum, BacksNum, BonusNum, AdvanceNum, PunishmentNum, VacationNum, VsalaryNum, CustodyNum, ExitNum, ChamberNum;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr__orders);
        setActivity();
        setActivityActions();
        //getHrOrderTypes();
        getResignations();
        getVacations();
        getBackToWorks();
        getAdvancePayments();
        getVacationSalaryRequests();
        getCustodyRequests();
        getPunishment();
        getChamberCommerce();
    }

    void setActivity() {
        Types = new ArrayList<>();
        resignations = findViewById(R.id.Resignations_Recycler);
        resignations.setVisibility(View.GONE);
        advancePayments = findViewById(R.id.AdvancePayment_Recycler);
        advancePayments.setVisibility(View.GONE);
        Punishment_Recycler = findViewById(R.id.Punishment_Recycler);
        Punishment_Recycler.setVisibility(View.GONE);
        Chamber_Recycler = findViewById(R.id.Chamber_Recycler);
        Chamber_Recycler.setVisibility(View.GONE);
        vacations = findViewById(R.id.Vacations_Recycler);
        vacations.setVisibility(View.GONE);
        backTooWork = findViewById(R.id.BackToWork_Recycler);
        backTooWork.setVisibility(View.GONE);
        custodyRequestRecycler = findViewById(R.id.RequestCustody_Recycler);
        custodyRequestRecycler.setVisibility(View.GONE);
        vacationSalaryRequest = findViewById(R.id.vacationSalary_recycler);
        vacationSalaryRequest.setVisibility(View.GONE);
        exits = findViewById(R.id.exitRecycler);
        exits.setVisibility(View.GONE);
        Bonuses = findViewById(R.id.Bonus_Recycler);
        Bonuses.setVisibility(View.GONE);
        bonusAdapter = new Bonus_Adapter(bonusRequestsList);
        resignation_adapter = new Resignation_Adapter(ResignationsList);
        vacation_adapter = new Vacations_Adapter(VacationsList);
        backtowork_adapter = new Backtowork_Adapter(BackToWork);
        advance_adapter = new AdvancePayment_Adapter(AdvancePayment);
        punishment_adapter = new Punishment_Adapter(punishmentList);
        vacationsalary_Adapter = new VacationSalary_Adapter(vacationsalaryList);
        custodyRequest_adapter = new CustodyRequest_Adapter(custodyrequestList);
        chamber_commerce_adapter = new Chamber_Commerce_Adapter(chamberCommerceList);
        resignationsManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        vacationsManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        backtoworkManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        advanceManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        punishmentManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        ChamberManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        vacationsalaryManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        custodyRequestManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        exitManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        bonusManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        ResignationsLayout = findViewById(R.id.ResignationLayout);
        BonusLayout = findViewById(R.id.BonusLayout);
        AdvanceLayout = findViewById(R.id.AdvanceLayout);
        PunishmentLayout = findViewById(R.id.PunishmentLayout);
        ChamberLayout = findViewById(R.id.ChamberLayout);
        BackLayout = findViewById(R.id.BackLayout);
        CustodyLayout = findViewById(R.id.CustodyLayout);
        ExitLayout = findViewById(R.id.ExitLayout);
        VacationLayout = findViewById(R.id.VacationLayout);
        VSalaryLayout = findViewById(R.id.VacationSalaryLayout);
        ResignationsNum = findViewById(R.id.textView143);
        BacksNum = findViewById(R.id.textView1435re);
        BonusNum = findViewById(R.id.textView1fd435r);
        AdvanceNum = findViewById(R.id.textView1435r);
        PunishmentNum = findViewById(R.id.PunishmentNum);
        VacationNum = findViewById(R.id.textView1435rv);
        VsalaryNum = findViewById(R.id.textView1435);
        CustodyNum = findViewById(R.id.textView14e35r);
        ExitNum = findViewById(R.id.textView143);
        ChamberNum = findViewById(R.id.ChamberNum);
        Bonuses.setLayoutManager(bonusManager);
        exits.setLayoutManager(exitManager);
        exits.setNestedScrollingEnabled(false);
        custodyRequestRecycler.setLayoutManager(custodyRequestManager);
        custodyRequestRecycler.setNestedScrollingEnabled(false);
        resignations.setLayoutManager(resignationsManager);
        resignations.setNestedScrollingEnabled(false);
        vacations.setLayoutManager(vacationsManager);
        vacations.setNestedScrollingEnabled(false);
        backTooWork.setLayoutManager(backtoworkManager);
        backTooWork.setNestedScrollingEnabled(false);
        advancePayments.setLayoutManager(advanceManager);
        advancePayments.setNestedScrollingEnabled(false);
        Punishment_Recycler.setLayoutManager(punishmentManager);
        Punishment_Recycler.setNestedScrollingEnabled(false);
        Chamber_Recycler.setLayoutManager(ChamberManager);
        Chamber_Recycler.setNestedScrollingEnabled(false);
        vacationSalaryRequest.setLayoutManager(vacationsalaryManager);
        vacationSalaryRequest.setNestedScrollingEnabled(false);
        done = findViewById(R.id.radioButton_Done);
        done.setChecked(false);
        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    undone.setChecked(false);
                    Status = 1;
                } else {
                    undone.setChecked(true);
                    Status = 0;
                }
                getResignations();
                getVacations();
                getBackToWorks();
                getAdvancePayments();
                getVacationSalaryRequests();
                getCustodyRequests();
                getExitRequests();
                getBonusRequests();
                getPunishment();
                getChamberCommerce();
            }
        });
        undone = findViewById(R.id.radioButton_undone);
        undone.setChecked(true);
        undone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    done.setChecked(false);
                    Status = 0;
                } else {
                    done.setChecked(true);
                    Status = 1;
                }
            }
        });
        act = this;
    }

    void setActivityActions() {
        ResignationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resignations.getVisibility() == View.VISIBLE) {
                    resignations.setVisibility(View.GONE);
                } else {
                    resignations.setVisibility(View.VISIBLE);
                }
            }
        });
        BonusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bonuses.getVisibility() == View.VISIBLE) {
                    Bonuses.setVisibility(View.GONE);
                } else {
                    Bonuses.setVisibility(View.VISIBLE);
                }
            }
        });
        BackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backTooWork.getVisibility() == View.VISIBLE) {
                    backTooWork.setVisibility(View.GONE);
                } else {
                    backTooWork.setVisibility(View.VISIBLE);
                }
            }
        });
        AdvanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advancePayments.getVisibility() == View.VISIBLE) {
                    advancePayments.setVisibility(View.GONE);
                } else {
                    advancePayments.setVisibility(View.VISIBLE);
                }
            }
        });
        PunishmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Punishment_Recycler.getVisibility() == View.VISIBLE) {
                    Punishment_Recycler.setVisibility(View.GONE);
                } else {
                    Punishment_Recycler.setVisibility(View.VISIBLE);
                }
            }
        });
        VacationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vacations.getVisibility() == View.VISIBLE) {
                    vacations.setVisibility(View.GONE);
                } else {
                    vacations.setVisibility(View.VISIBLE);
                }
            }
        });
        VSalaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vacationSalaryRequest.getVisibility() == View.VISIBLE) {
                    vacationSalaryRequest.setVisibility(View.GONE);
                } else {
                    vacationSalaryRequest.setVisibility(View.VISIBLE);
                }
            }
        });
        CustodyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (custodyRequestRecycler.getVisibility() == View.VISIBLE) {
                    custodyRequestRecycler.setVisibility(View.GONE);
                } else {
                    custodyRequestRecycler.setVisibility(View.VISIBLE);
                }
            }
        });
        ExitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exits.getVisibility() == View.VISIBLE) {
                    exits.setVisibility(View.GONE);
                } else {
                    exits.setVisibility(View.VISIBLE);
                }
            }
        });
        ChamberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Chamber_Recycler.getVisibility() == View.VISIBLE) {
                    Chamber_Recycler.setVisibility(View.GONE);
                } else {
                    Chamber_Recycler.setVisibility(View.VISIBLE);
                }
            }
        });
    }

//    void getHrOrderTypes() {
//        Loading l = new Loading(act); l.show();
//        StringRequest request = new StringRequest(Request.Method.POST, getHrOrdersTypes, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                l.close();
//                if (response != null ) {
//                    if (response.equals("0")) {
//
//                    }
//                    else {
//                        Log.d("hrOrdersResponse" , response );
//                        List<Object> list = JsonToObject.translate(response,HR_ORDER_TYPE.class,act);
//                        Types.clear();
//                        for(int i=0 ; i<list.size() ; i++) {
//                            HR_ORDER_TYPE x = (HR_ORDER_TYPE) list.get(i) ;
//                            Types.add(x);
//                        }
//                        //ordersTypes.setAdapter(adapter);
//                        //getJobTitles();
//                        //getResignationAuthEmps();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                l.close();
//            }
//        });
//        Volley.newRequestQueue(act).add(request);
//    }

//    void getJobTitles() {
//        Log.d("jobtitles","jotitles started");
//        Loading l = new Loading(act);
//        l.show();
//        StringRequest request = new StringRequest(Request.Method.POST, getJobTitlesUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("jobtitles",response);
//                    l.close();
//                if (response != null ) {
//                    List<Object> list = JsonToObject.translate(response,JobTitle.class,act);
//                    JobTitles.clear();
//                    for (int i = 0; i< list.size();i++) {
//                        JobTitle j = (JobTitle) list.get(i) ;
//                        JobTitles.add(j);
//                    }
//                    MyApp.JobTitles = JobTitles ;
//                   // getResignationAuthEmps();
//                    //getVacationAuthEmps();
//                    //getBacksAuthEmps();
//                    //getAdvancePaymentsAuthEmps();
//                    //getVacationSalariesAuthEmps();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                l.close();
//                Log.d("jobtitles",error.getMessage());
//            }
//        });
//        Volley.newRequestQueue(act).add(request);
//    }

    public static void getResignations() {

        ResignationsList.clear();
        resignation_adapter = new Resignation_Adapter(ResignationsList);
        resignations.setAdapter(resignation_adapter);
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getResignationsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resignationsResponse", response);
                d.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, RESIGNATION_CLASS.class, act);
                    Log.d("resignationsResponse", list.size() + "");
                    ResignationsList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        RESIGNATION_CLASS r = (RESIGNATION_CLASS) list.get(i);
                        ResignationsList.add(r);
                    }
                    Collections.reverse(ResignationsList);
                    ResignationsNum.setText(ResignationsList.size() + "");
                    resignation_adapter = new Resignation_Adapter(ResignationsList);
                    resignations.setAdapter(resignation_adapter);
                } else {
                    ResignationsNum.setText("0");
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
                Map<String, String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.MyUser.id));
                par.put("status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getVacations() {

        VacationsList.clear();
        vacation_adapter = new Vacations_Adapter(VacationsList);
        vacations.setAdapter(vacation_adapter);
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getVacationsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("vacationsResponse", response + " " + String.valueOf(MyApp.db.getUser().id) + String.valueOf(Status));
                d.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, VACATION_CLASS.class, act);
                    Log.d("vacationsResponse", list.size() + "");
                    VacationsList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        VACATION_CLASS r = (VACATION_CLASS) list.get(i);
                        VacationsList.add(r);
                    }
                    //Log.d("vacationsResponse" , VacationsList.size()+"");
                    Collections.reverse(VacationsList);
                    VacationNum.setText(VacationsList.size() + "");
                    vacation_adapter = new Vacations_Adapter(VacationsList);
                    vacations.setAdapter(vacation_adapter);
                } else {
                    VacationNum.setText("0");
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
                Map<String, String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.db.getUser().id));
                par.put("status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getBackToWorks() {

        Log.d("Status", Status + "");
        BackToWork.clear();
        backtowork_adapter = new Backtowork_Adapter(BackToWork);
        backTooWork.setAdapter(backtowork_adapter);
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getBackToWorksUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("backResponse", response);
                //ToastMaker.Show(1,response,act);
                d.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, BACKTOWORK_CLASS.class, act);
                    Log.d("backResponse", list.size() + "");
                    BackToWork.clear();
                    for (int i = 0; i < list.size(); i++) {
                        BACKTOWORK_CLASS r = (BACKTOWORK_CLASS) list.get(i);
                        BackToWork.add(r);
                    }
                    //Log.d("backResponse" , BackToWork.size()+"");
                    Collections.reverse(BackToWork);
                    BacksNum.setText(BackToWork.size() + "");
                    backtowork_adapter = new Backtowork_Adapter(BackToWork);
                    backTooWork.setAdapter(backtowork_adapter);
                } else {
                    BacksNum.setText("0");
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
                Map<String, String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.db.getUser().id));
                par.put("status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getAdvancePayments() {
        AdvancePayment.clear();
        advance_adapter = new AdvancePayment_Adapter(AdvancePayment);
        advancePayments.setAdapter(advance_adapter);
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getAdvancePayments, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("advanceResponse", response);
                //ToastMaker.Show(1,response,act);
                d.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, ADVANCEPAYMENT_CLASS.class, act);
                    Log.d("advanceResponse", list.size() + "");
                    AdvancePayment.clear();
                    for (int i = 0; i < list.size(); i++) {
                        ADVANCEPAYMENT_CLASS r = (ADVANCEPAYMENT_CLASS) list.get(i);
                        AdvancePayment.add(r);
                    }
                    Collections.reverse(AdvancePayment);
                    AdvanceNum.setText(AdvancePayment.size() + "");
                    advance_adapter = new AdvancePayment_Adapter(AdvancePayment);
                    advancePayments.setAdapter(advance_adapter);
                } else {
                    AdvanceNum.setText("0");
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
                Map<String, String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.db.getUser().id));
                par.put("status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getVacationSalaryRequests() {
        vacationsalaryList.clear();
        vacationsalary_Adapter = new VacationSalary_Adapter(vacationsalaryList);
        vacationSalaryRequest.setAdapter(vacationsalary_Adapter);
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getVacationSalaryRequestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("vsalaryResponse", response);
                //ToastMaker.Show(1,response,act);
                d.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, VACATIONSALARY_CLASS.class, act);
                    Log.d("vsalaryResponse", list.size() + "");
                    vacationsalaryList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        VACATIONSALARY_CLASS r = (VACATIONSALARY_CLASS) list.get(i);
                        vacationsalaryList.add(r);
                    }
                    Collections.reverse(vacationsalaryList);
                    VsalaryNum.setText(vacationsalaryList.size() + "");
                    vacationsalary_Adapter = new VacationSalary_Adapter(vacationsalaryList);
                    vacationSalaryRequest.setAdapter(vacationsalary_Adapter);
                } else {
                    VsalaryNum.setText("0");
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
                Map<String, String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.db.getUser().id));
                par.put("status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getCustodyRequests() {
        custodyrequestList.clear();
        custodyRequest_adapter = new CustodyRequest_Adapter(custodyrequestList);
        custodyRequestRecycler.setAdapter(custodyRequest_adapter);
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getCustodyRequestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("vsalaryResponse", response);
                //ToastMaker.Show(1,response,act);
                d.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, CUSTODY_REQUEST_CLASS.class, act);
                    Log.d("vsalaryResponse", list.size() + "");
                    custodyrequestList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        CUSTODY_REQUEST_CLASS r = (CUSTODY_REQUEST_CLASS) list.get(i);
                        custodyrequestList.add(r);
                    }
                    //Log.d("vsalaryResponse" , custodyrequestList.size()+"");
                    Collections.reverse(custodyrequestList);
                    CustodyNum.setText(custodyrequestList.size() + "");
                    custodyRequest_adapter = new CustodyRequest_Adapter(custodyrequestList);
                    custodyRequestRecycler.setAdapter(custodyRequest_adapter);
                } else {
                    CustodyNum.setText("0");
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
                Map<String, String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.db.getUser().id));
                par.put("status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getExitRequests() {
        exitRequestList.clear();
        exitAdapter = new ExitRequest_ADAPTER(exitRequestList);
        exits.setAdapter(exitAdapter);
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getExitRequestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("exitResponse", response);
                //ToastMaker.Show(1,response,act);
                d.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, EXIT_REQUEST_CLASS.class, act);
                    Log.d("exitResponse", list.size() + "");
                    exitRequestList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        EXIT_REQUEST_CLASS r = (EXIT_REQUEST_CLASS) list.get(i);
                        exitRequestList.add(r);
                    }
                    //Log.d("vsalaryResponse" , custodyrequestList.size()+"");
                    Collections.reverse(exitRequestList);
                    ExitNum.setText(exitRequestList.size() + "");
                    exitAdapter = new ExitRequest_ADAPTER(exitRequestList);
                    exits.setAdapter(exitAdapter);
                } else {
                    ExitNum.setText("0");
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
                Map<String, String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.db.getUser().id));
                par.put("status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getBonusRequests() {
        bonusRequestsList.clear();
        bonusAdapter = new Bonus_Adapter(bonusRequestsList);
        Bonuses.setAdapter(bonusAdapter);
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getBonusesRequestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("bonusESResponse", response);
                d.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, BONUS.class, act);
                    Log.d("bonusESResponse", list.size() + "");
                    bonusRequestsList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        BONUS r = (BONUS) list.get(i);
                        bonusRequestsList.add(r);
                    }
                    //Log.d("vsalaryResponse" , custodyrequestList.size()+"");
                    Collections.reverse(bonusRequestsList);
                    BonusNum.setText(bonusRequestsList.size() + "");
                    bonusAdapter = new Bonus_Adapter(bonusRequestsList);
                    Bonuses.setAdapter(bonusAdapter);
                } else {
                    BonusNum.setText("0");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
                Log.d("bonusESResponse", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("jn", String.valueOf(MyApp.MyUser.JobNumber));
                par.put("status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getPunishment() {
        punishmentList.clear();
        punishment_adapter = new Punishment_Adapter(punishmentList);
        Punishment_Recycler.setAdapter(punishment_adapter);
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getPunishmentRequestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, PUNISHMENT_CLASS.class, act);
                    punishmentList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        PUNISHMENT_CLASS p = (PUNISHMENT_CLASS) list.get(i);
                        punishmentList.add(p);
                    }
                    Collections.reverse(punishmentList);
                    PunishmentNum.setText(punishmentList.size() + "");
                    punishment_adapter = new Punishment_Adapter(punishmentList);
                    Punishment_Recycler.setAdapter(punishment_adapter);
                } else {
                    PunishmentNum.setText("0");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("PunishmentResponse", error + "");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.db.getUser().id));
                par.put("Status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public static void getChamberCommerce(){
        chamberCommerceList.clear();
        chamber_commerce_adapter = new Chamber_Commerce_Adapter(chamberCommerceList);
        Chamber_Recycler.setAdapter(chamber_commerce_adapter);
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getRequestChamberCommerce, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("ChamberCommerceResponse", response + "");

                if (!response.equals("0")) {
                    List<Object> list = JsonToObject.translate(response, CHAMBER_COMMERCE_CLASS.class, act);
                    chamberCommerceList.clear();
                    Log.d("ChamberCommerceResponse", list.size() + " Size");
                    for (int i = 0; i < list.size(); i++) {
                        CHAMBER_COMMERCE_CLASS C = (CHAMBER_COMMERCE_CLASS) list.get(i);
                        chamberCommerceList.add(C);
                        Log.d("ChamberCommerceResponse", ((CHAMBER_COMMERCE_CLASS) list.get(i)).id + "LIST ID");

                    }
                    Collections.reverse(chamberCommerceList);
                    ChamberNum.setText(chamberCommerceList.size() + "");
                    chamber_commerce_adapter = new Chamber_Commerce_Adapter(chamberCommerceList);
                    Chamber_Recycler.setAdapter(chamber_commerce_adapter);
                } else {
                    ChamberNum.setText("0");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("ChamberCommerceResponse", error + "");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("EmpID", String.valueOf(MyApp.db.getUser().id));
                par.put("Status", String.valueOf(Status));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }
}