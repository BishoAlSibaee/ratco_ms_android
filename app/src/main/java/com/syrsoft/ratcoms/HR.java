package com.syrsoft.ratcoms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.syrsoft.ratcoms.HRActivities.CheckEmpsAttendance;
import com.syrsoft.ratcoms.HRActivities.EmployeeRating;
import com.syrsoft.ratcoms.HRActivities.ExitRequest;
import com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE;
import com.syrsoft.ratcoms.HRActivities.JobTitle;
import com.syrsoft.ratcoms.HRActivities.ManageAuthorizedTitles;
import com.syrsoft.ratcoms.HRActivities.ManageOrdersAuthority;
import com.syrsoft.ratcoms.HRActivities.ManageStaffAttendanceByLocation;
import com.syrsoft.ratcoms.HRActivities.MyAttendTable;
import com.syrsoft.ratcoms.HRActivities.MySalaryReports;
import com.syrsoft.ratcoms.HRActivities.RatingEmployees;
import com.syrsoft.ratcoms.HRActivities.RequestBonus;
import com.syrsoft.ratcoms.HRActivities.RequestCustody;
import com.syrsoft.ratcoms.HRActivities.SendNewAdWithImage;
import com.syrsoft.ratcoms.HRActivities.SendSalaryReports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class HR extends AppCompatActivity {

    private Activity act ;
    private TextView name , mobile, email ,passport,passportEDate , ID , IDEDate , contract , contractEDate,jobNum , jobTitle ,department,directManager ;
    private  ImageView pic ;
    private Button MySalaryReports,RatingBtn,MyApprovalsBtn,MyOrdersBtn,VacationSalaryBtn,RequestCustodyBtn,AdvencePaymentBtn,ResignationBtn,BacktoWorkBtn,VacationsBtn,ExitRequestBtn,salaryReportBtn , appUpdateBtn , myApprovals , manageProjectEmps , checkEmpsAttendance ;
    private String getHrOrdersTypes = MyApp.MainUrl + "getHrOrdersTypes.php" ;
    private String getJobTitlesUrl = MyApp.MainUrl +"getJobtitles.php";
    private String insertNewAdUrl = MyApp.MainUrl + "insertNewAd.php" ;
    static List<HR_ORDER_TYPE> Types ;
    public static List<JobTitle> ExitAuthsJobTitles,JobTitles , RequestCustodyAuthsJobTitles , ResignationsAuthsJobtitles , VacationsAuthJobtitles , BacksAuthJobtitles , AdvancePaymentsAuthJobtitles , VacationSalaryAuthJobtitles ;
    public static List<USER>  ExitAuthUsers,CustodyAuthUsers ,  ResignationsAuthUsers , VacationsAuthUsers , BacksAuthUsers , AdvancePaymentaAuthUsers , VacationSalaryAuthUsers ;
    public static List<JobTitle>  ARequestCustodyAuthsJobTitles , AResignationsAuthsJobtitles , AVacationsAuthJobtitles , ABacksAuthJobtitles , AAdvancePaymentsAuthJobtitles , AVacationSalaryAuthJobtitles ;
    public static List<USER>  ACustodyAuthUsers ,  AResignationsAuthUsers , AVacationsAuthUsers , ABacksAuthUsers , AAdvancePaymentaAuthUsers , AVacationSalaryAuthUsers ;
    public static boolean isRunning = false ;
    static CardView ApprovalsCard , RatingCard  ;
    static TextView ApprovalCount ,RatingCount  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_activity);
        isRunning = true ;
        setActivity();
        setHRCounters();
    }

    void setActivity() {
        act = this ;
        JobTitles = new ArrayList<>();
        ResignationsAuthsJobtitles = new ArrayList<>();
        VacationsAuthJobtitles = new ArrayList<>();
        BacksAuthJobtitles = new ArrayList<>();
        AdvancePaymentsAuthJobtitles = new ArrayList<>();
        VacationSalaryAuthJobtitles = new ArrayList<>();
        RequestCustodyAuthsJobTitles = new ArrayList<>();
        ExitAuthsJobTitles = new ArrayList<>();
        appUpdateBtn = (Button) findViewById(R.id.button22);
        if (MyApp.db.getUser().JobTitle.equals("Programmer")) {
            appUpdateBtn.setVisibility(View.VISIBLE);
        }
        Types = new ArrayList<HR_ORDER_TYPE>();
        ResignationsAuthUsers = new ArrayList<USER>();
        VacationsAuthUsers = new ArrayList<USER>();
        BacksAuthUsers = new ArrayList<USER>();
        AdvancePaymentaAuthUsers = new ArrayList<USER>();
        VacationSalaryAuthUsers = new ArrayList<USER>();
        CustodyAuthUsers = new ArrayList<USER>();
        ExitAuthUsers = new ArrayList<USER>();
        salaryReportBtn = (Button) findViewById(R.id.button21);
        name = (TextView) findViewById(R.id.User_Name);
        name.setText(MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName);
        mobile = (TextView) findViewById(R.id.User_Mobile);
        mobile.setText(String.valueOf(MyApp.db.getUser().Mobile));
        email = (TextView) findViewById(R.id.User_Email);
        email.setText(MyApp.db.getUser().Email);
        passport = (TextView) findViewById(R.id.User_PassportNumber);
        passport.setText(MyApp.db.getUser().PassportNumber);
        passportEDate = (TextView) findViewById(R.id.User_PassportExpireDate);
        passportEDate.setText(MyApp.db.getUser().PassportExpireDate);
        ID = (TextView) findViewById(R.id.User_IdNumber);
        ID.setText(MyApp.db.getUser().IDNumber);
        IDEDate = (TextView) findViewById(R.id.User_IdExpireDate);
        IDEDate.setText(MyApp.db.getUser().IDExpireDate);
        contract = (TextView) findViewById(R.id.User_ContractNumber);
        contract.setText(MyApp.db.getUser().ContractNumber);
        contractEDate = (TextView) findViewById(R.id.User_ContractExpireDate);
        contractEDate.setText(MyApp.db.getUser().ContractExpireDate);
        jobNum = (TextView) findViewById(R.id.User_JobNumber);
        jobNum.setText(String.valueOf(MyApp.db.getUser().JobNumber));
        jobTitle = (TextView) findViewById(R.id.User_JobTitle);
        jobTitle.setText(MyApp.db.getUser().JobTitle);
        department = (TextView) findViewById(R.id.User_Department);
        department.setText(MyApp.db.getUser().Department);
        directManager = (TextView) findViewById(R.id.User_DirectManager);
        if (MyApp.DIRECT_MANAGER != null) {
            directManager.setText(MyApp.DIRECT_MANAGER.FirstName+" "+MyApp.DIRECT_MANAGER.LastName);
        }
        pic = (ImageView) findViewById(R.id.User_Pic);
        MyApprovalsBtn = (Button) findViewById(R.id.HR_MyApprovals);
        RatingBtn = findViewById(R.id.HR_EmployeesRating);
        MyOrdersBtn = (Button) findViewById(R.id.myHROrders);
        VacationSalaryBtn = (Button) findViewById(R.id.HR_VacationSalary);
        RequestCustodyBtn = (Button) findViewById(R.id.button16);
        AdvencePaymentBtn = (Button) findViewById(R.id.HR_advancePayment);
        ResignationBtn = (Button) findViewById(R.id.HR_resignation);
        BacktoWorkBtn = (Button) findViewById(R.id.HR_backToWork);
        VacationsBtn = (Button) findViewById(R.id.HR_requestVacation);
        ExitRequestBtn = (Button) findViewById(R.id.HR_requestExit);
        myApprovals = (Button) findViewById(R.id.HR_MyApprovals);
        MySalaryReports = (Button) findViewById(R.id.button211);
        checkEmpsAttendance = (Button) findViewById(R.id.HR_CheckEmpsAttendance);
        manageProjectEmps = (Button) findViewById(R.id.button15);
        ApprovalsCard = (CardView) findViewById(R.id.ApprovalsCounterCard);
        ApprovalCount = (TextView) findViewById(R.id.ApprovalsCounterText);
        RatingCard = (CardView) findViewById(R.id.EmployeesRatingCounterCard);
        RatingCount = (TextView) findViewById(R.id.EmployeesRatingCounterText);
        if(MyApp.db.getUser().Pic != null && !MyApp.db.getUser().Pic.isEmpty()){
            Picasso.get().load(MyApp.db.getUser().Pic).into(pic);
        }
        MyApp.RefUSERS.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.getValue() != null )
                {
                    for (DataSnapshot d : snapshot.getChildren())
                    {
                        if (d != null )
                        {
                            if (d.child("Department").getValue() != null) {
                                if (d.child("Department").getValue().toString().equals("HR") || d.child("Department").getValue().toString().contains("Manager")) {
                                    if (d.child("token").getValue() != null) {
                                        MyApp.ManagersTokens.add(d.child("token").getValue().toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setVisiblePermissions();
        setHRCounters();
    }

     void setVisiblePermissions() {
        if (MyApp.MyUser != null) {
            if (MyApp.MyUser.MyPermissions != null) {
                for (int i=0 ; i < MyApp.MyUser.MyPermissions.size();i++) {
                    if (MyApp.MyUser.MyPermissions.get(i).getDepartment().equals("HR")) {
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 3) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                ExitRequestBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                ExitRequestBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 4) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                VacationsBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                VacationsBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 5) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                BacktoWorkBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                BacktoWorkBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 6) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                ResignationBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                ResignationBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 7) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                AdvencePaymentBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                AdvencePaymentBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 8) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                RequestCustodyBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                RequestCustodyBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 9) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                VacationSalaryBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                VacationSalaryBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 15) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                salaryReportBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                salaryReportBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 16) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                MyOrdersBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                MyOrdersBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 17) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                MyApprovalsBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                MyApprovalsBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 18) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                checkEmpsAttendance.setVisibility(View.VISIBLE);
                            }
                            else {
                                checkEmpsAttendance.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 19) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                RatingBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                RatingBtn.setVisibility(View.GONE);
                            }
                            continue;
                        }
                        if (MyApp.MyUser.MyPermissions.get(i).getId() == 33) {
                            if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                                MySalaryReports.setVisibility(View.VISIBLE);
                            }
                            else {
                                MySalaryReports.setVisibility(View.GONE);
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }

    public static void setHRCounters() {
        if (MyApp.HRCounter == 0 ) {
            ApprovalsCard.setVisibility(View.GONE);
        }
        else {
            ApprovalsCard.setVisibility(View.VISIBLE);
            ApprovalCount.setText(String.valueOf(MyApp.MYApprovalsCounter));
        }
        if (MyApp.RatingCounter == 0 ) {
            RatingCard.setVisibility(View.GONE);
        }
        else {
            RatingCard.setVisibility(View.VISIBLE);
            RatingCount.setText(String.valueOf(MyApp.RatingCounter));
        }
    }

    public void goToResignationActivity(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.Resignation.class);
        startActivity(i);
    }

    public void goToMyOrders(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.HR_Orders.class);
        startActivity(i);
    }

    public void goToMyApprovals(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.MyApprovals.class);
        startActivity(i);
    }

    public void goToMangeEmployeesIds(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.ManageIds.class);
        startActivity(i);
    }

    public void goToVacations(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.Vacation.class);
        startActivity(i);
    }

    public void goToBackToWork(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.BackToWork.class);
        startActivity(i);
    }

    public void gotoAdvancePayment(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.AdvancePayment.class);
        startActivity(i);
    }

    public void goToVacationSalary(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.VacationSalary.class);
        startActivity(i);
    }

    public void gotoManagePassports(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.ManagePassports.class);
        startActivity(i);
    }

    public void goToManageContracts(View view) {
        Intent i = new Intent(getApplicationContext(), com.syrsoft.ratcoms.HRActivities.ManageContracts.class);
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
//                MyApp.RefUSERS.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.getValue() != null ){
//                            Loading dd = new Loading(act); dd.show();
//                            MyApp.sendNotificationsToGroup(MyApp.EMPS,title.getText().toString(),text.getText().toString(),"",x,"AD",getApplicationContext());
////                            for (DataSnapshot data : snapshot.getChildren()){
////                                if (data.child("token").getValue() != null ){
////                                    MyApp.CloudMessage(title.getText().toString(),text.getText().toString(),"",x,data.child("token").getValue().toString(),"AD",getApplicationContext());
////                                }
////                            }
//                            dd.close();
//                            d.dismiss();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });
        d.show();
    }

    public void sendNewSalaryReportAdDialog(View view) {
        Dialog d = new Dialog(act) ;
        d.setContentView(R.layout.send_salary_report_dialog);
        Window window = d.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditText title = (EditText) d.findViewById(R.id.editTextTextPersonName);
        Spinner months = (Spinner) d.findViewById(R.id.salaryAdDialog_month);
        String[] monthsArr = {"1","2","3","4","5","6","7","8","9","10","11","12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,monthsArr);
        months.setAdapter(adapter);
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
//                if (title.getText() == null || title.getText().toString().isEmpty())
//                {
//                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act , getResources().getString(R.string.enterAdTitle),getResources().getString(R.string.enterAdTitle));
//                    return;
//                }
//                if (text.getText() == null || text.getText().toString().isEmpty() ){
//                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act , getResources().getString(R.string.enterAdText),getResources().getString(R.string.enterAdText));
//                    return;
//                }
                String Month = months.getSelectedItem().toString();
                Random r = new Random();
                int x = r.nextInt(10000);
//                for (USER u : MyApp.EMPS) {
//                    if (u.JobNumber == MyApp.db.getUser().JobNumber) {
//                        MyApp.CloudMessage("SalaryReport","Salary Report Created","https://ratco-solutions.com/RatcoManagementSystem/SalaryReports/"+Month+"/",x,u.Token,"AD",MyApp.app);
//                    }
//                }
                MyApp.sendNotificationsToGroup(MyApp.EMPS, "SalaryReport", "Salary Report Created", "https://ratco-solutions.com/RatcoManagementSystem/SalaryReports/" + Month + "/", x, "AD", getApplicationContext(), new VolleyCallback() {
                    @Override
                    public void onSuccess() {
                        d.dismiss();
                    }
                });
            }
        });
        //d.show();
        Intent i = new Intent(act, SendSalaryReports.class);
        startActivity(i);
    }

    public void sendApplicationUpdateAdDialog(View view) {
        Dialog d = new Dialog(act) ;
        d.setContentView(R.layout.send_ad_dialog);
        EditText title = (EditText) d.findViewById(R.id.editTextTextPersonName);
        EditText text = (EditText) d.findViewById(R.id.editTextTextPersonName2);
        TextView c1 = (TextView) d.findViewById(R.id.ad_dialog_title);
        TextView c2 = (TextView) d.findViewById(R.id.textView67);
        TextView c = (TextView) d.findViewById(R.id.textView66);
        //title.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        //c1.setVisibility(View.GONE);
        c1.setText("Enter The Link");
        c2.setVisibility(View.GONE);
        c.setText("Send Application Update Order");
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
//                if (title.getText() == null || title.getText().toString().isEmpty())
//                {
//                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act , getResources().getString(R.string.enterAdTitle),getResources().getString(R.string.enterAdTitle));
//                    return;
//                }
//                if (text.getText() == null || text.getText().toString().isEmpty() ){
//                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act , getResources().getString(R.string.enterAdText),getResources().getString(R.string.enterAdText));
//                    return;
//                }
                if(title.getText() == null || title.getText().toString().isEmpty()){
                    ToastMaker.Show(1,"enter link",act);
                    return;
                }
                Random r = new Random();
                int x = r.nextInt(10000);
                MyApp.sendNotificationsToGroup(MyApp.EMPS, "UpdateApplication", "Update Application Now", title.getText().toString(), x, "AD", getApplicationContext(), new VolleyCallback() {
                    @Override
                    public void onSuccess() {
                        d.dismiss();
                    }
                });

            }
        });
        d.show();
    }



    // ---------------------------------

    void getResignationAuthEmps () {
        ResignationsAuthsJobtitles.clear();
        MyApp.ResignationsAuthsJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("Resignation")) {
                if (Types.get(i).Auth1 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    ResignationsAuthsJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        //Log.d("ResignationJobtitles" , ResignationsAuthsJobtitles.size()+"");
        if (ResignationsAuthsJobtitles.size() > 0 ) {
            //Log.d("ResignationJobtitles" , "in ");
            MyApp.ResignationsAuthsJobtitles = ResignationsAuthsJobtitles ;
            Log.d("ResignationUsers" , MyApp.db.getUser().DirectManager+"");
            for (int p=0; p<ResignationsAuthsJobtitles.size();p++) {
                Log.d("ResignationJobtitles" , ResignationsAuthsJobtitles.get(p).JobTitle+" "+p);
                if (ResignationsAuthsJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
                            ResignationsAuthUsers.add(MyApp.EMPS.get(y));
                        }
                    }
                }
                else
                    if (USER.searchUserByJobtitle(MyApp.EMPS,ResignationsAuthsJobtitles.get(p).JobTitle) != null ) {
                    ResignationsAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,ResignationsAuthsJobtitles.get(p).JobTitle));
                }

            }
            MyApp.ResignationsAuthUsers = ResignationsAuthUsers ;
        }
        //Log.d("ResignationUsers" , ResignationsAuthUsers.size()+" "+MainPage.EMPS.size());
        getAdvancePaymentsAuthEmps();
    }


    void getVacationAuthEmps () {
        MyApp.VacationsAuthJobtitles.clear();
        VacationsAuthJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("Vacations")) {
                if (Types.get(i).Auth1 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    VacationsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        Log.d("VacationJobtitles" , VacationsAuthJobtitles.size()+"");
        if (VacationsAuthJobtitles.size() > 0 ) {
            MyApp.VacationsAuthJobtitles = VacationsAuthJobtitles ;
            Log.d("VacationJobtitles" , "in ");
            Log.d("VacationUsers" , MyApp.db.getUser().DirectManager+"");
            for (int p=0; p<VacationsAuthJobtitles.size();p++) {
                Log.d("VacationJobtitles" , VacationsAuthJobtitles.get(p).JobTitle+" "+VacationsAuthJobtitles.get(p).id+" "+VacationsAuthJobtitles.get(p).ArabicName+" "+VacationsAuthJobtitles.get(p).Department);
                if (VacationsAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
                            VacationsAuthUsers.add(MyApp.EMPS.get(y));
                            break;
                        }
                    }
                }
                else
                    if (USER.searchUserByJobtitle(MyApp.EMPS,VacationsAuthJobtitles.get(p).JobTitle) != null ) {
                    VacationsAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,VacationsAuthJobtitles.get(p).JobTitle));
                }
            }
            MyApp.VacationsAuthUsers = VacationsAuthUsers ;
            Log.d("VacationUsers" , VacationsAuthUsers.size()+"");
        }
        //Log.d("VacationUsers" , VacationsAuthUsers.size()+" "+MainPage.EMPS.size()+" "+VacationsAuthUsers.get(0).JobNumber);
        getVacationSalariesAuthEmps();
    }


    void getBacksAuthEmps () {
        BacksAuthJobtitles.clear();
        MyApp.BacksAuthJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("BackToWork")) {
                if (Types.get(i).Auth1 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    BacksAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        Log.d("backstitles" , BacksAuthJobtitles.size()+"");
        if (BacksAuthJobtitles.size() > 0 ) {
            MyApp.BacksAuthJobtitles = BacksAuthJobtitles ;
            Log.d("backstitles" , "in ");
            Log.d("VacationUsers" , MyApp.db.getUser().DirectManager+"");
            for (int p=0; p<BacksAuthJobtitles.size();p++) {
                Log.d("backstitles" , BacksAuthJobtitles.get(p).JobTitle+" "+p);
                if (BacksAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
                            BacksAuthUsers.add(MyApp.EMPS.get(y));
                            break;
                        }
                    }
                }
                else
                    if (USER.searchUserByJobtitle(MyApp.EMPS,BacksAuthJobtitles.get(p).JobTitle) != null ) {
                    BacksAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,BacksAuthJobtitles.get(p).JobTitle));
                }
            }
            MyApp.BacksAuthUsers = BacksAuthUsers ;
        }
        //Log.d("backstitles" , BacksAuthUsers.size()+" "+MainPage.EMPS.size());
        getVacationAuthEmps();
    }

    void getAdvancePaymentsAuthEmps () {
        AdvancePaymentsAuthJobtitles.clear();
        MyApp.AdvancePaymentsAuthJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("AdvancePayment")) {
                if (Types.get(i).Auth1 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        Log.d("Advancetitles" , AdvancePaymentsAuthJobtitles.size()+"");
        if (AdvancePaymentsAuthJobtitles.size() > 0 ) {
            MyApp.AdvancePaymentsAuthJobtitles = AdvancePaymentsAuthJobtitles ;
            Log.d("Advancetitles" , "in ");
            Log.d("AdvanceUsers" , MyApp.db.getUser().DirectManager+"");
            for (int p=0; p<AdvancePaymentsAuthJobtitles.size();p++) {
                Log.d("Advancetitles" , AdvancePaymentsAuthJobtitles.get(p).JobTitle+" "+p);
                if (AdvancePaymentsAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
                            AdvancePaymentaAuthUsers.add(MyApp.EMPS.get(y));
                            break;
                        }
                    }
                }
                else
                    if (USER.searchUserByJobtitle(MyApp.EMPS,AdvancePaymentsAuthJobtitles.get(p).JobTitle) != null ) {
                    AdvancePaymentaAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,AdvancePaymentsAuthJobtitles.get(p).JobTitle));
                    //Log.d("AdvanceUsers" , AdvancePaymentaAuthUsers.get(p).Token);
                }
            }
            MyApp.AdvancePaymentaAuthUsers = AdvancePaymentaAuthUsers ;
        }
        //Log.d("AdvanceUsers" , AdvancePaymentaAuthUsers.size()+" "+MainPage.EMPS.size());
        getBacksAuthEmps();
    }

    void getVacationSalariesAuthEmps () {
        VacationSalaryAuthJobtitles.clear();
        MyApp.VacationSalaryAuthJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {

            if (Types.get(i).HROrderName.equals("VacationSalary")) {

                if (Types.get(i).Auth1 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    VacationSalaryAuthJobtitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        Log.d("VacationSalarytitles" , VacationSalaryAuthJobtitles.size()+"");
        if (VacationSalaryAuthJobtitles.size() > 0 ) {
            MyApp.VacationSalaryAuthJobtitles = VacationSalaryAuthJobtitles;
            Log.d("VacationSalarytitles" , "in ");
            Log.d("VacationSalaryUsers" , MyApp.db.getUser().DirectManager+"");
            for (int p=0; p<VacationSalaryAuthJobtitles.size();p++) {
                Log.d("VacationSalarytitles" , VacationSalaryAuthJobtitles.get(p).JobTitle+" "+p);
                if (VacationSalaryAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
                            VacationSalaryAuthUsers.add(MyApp.EMPS.get(y));
                            break;
                        }
                    }
                }
                else
                    if (USER.searchUserByJobtitle(MyApp.EMPS,VacationSalaryAuthJobtitles.get(p).JobTitle) != null ) {
                    VacationSalaryAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,VacationSalaryAuthJobtitles.get(p).JobTitle));
                }
            }
            MyApp.VacationSalaryAuthUsers = VacationSalaryAuthUsers ;
        }
        Log.d("VacationSalaryUsers" , VacationSalaryAuthUsers.size()+" "+MyApp.EMPS.size());
        getRequestCustodyAuthEmps();
    }

    void getRequestCustodyAuthEmps () {
        RequestCustodyAuthsJobTitles.clear();
        MyApp.RequestCustodyAuthsJobTitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {

            if (Types.get(i).HROrderName.equals("RequestCustody")) {
                if (Types.get(i).Auth1 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (RequestCustodyAuthsJobTitles.size() > 0 ) {
            MyApp.RequestCustodyAuthsJobTitles = RequestCustodyAuthsJobTitles;
            Log.d("Custodytitles" , "in ");
            Log.d("CustodyUsers" , MyApp.db.getUser().DirectManager+"");
            for (int p=0; p<RequestCustodyAuthsJobTitles.size();p++) {
                Log.d("Custodytitles" , RequestCustodyAuthsJobTitles.get(p).JobTitle+" "+p);
                if (RequestCustodyAuthsJobTitles.get(p).JobTitle.equals("Direct Manager")) {
                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
                            CustodyAuthUsers.add(MyApp.EMPS.get(y));
                            break;
                        }
                    }
                }
                else
                    if (USER.searchUserByJobtitle(MyApp.EMPS,RequestCustodyAuthsJobTitles.get(p).JobTitle) != null ) {
                    CustodyAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,RequestCustodyAuthsJobTitles.get(p).JobTitle));
                }
            }
            //Log.d("CustodyUsers" , RequestCustodyAuthsJobTitles.size()+" "+CustodyAuthUsers.size());
            MyApp.CustodyAuthUsers = CustodyAuthUsers ;
        }
        getRequestExitAuthEmps();
        //showHideMyApprovals();
    }

    void getRequestExitAuthEmps () {
        ExitAuthsJobTitles.clear();
        MyApp.ExitAuthsJobTitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {

            if (Types.get(i).HROrderName.equals("RequestExit")) {
                if (Types.get(i).Auth1 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    ExitAuthsJobTitles.add(JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (ExitAuthsJobTitles.size() > 0 ) {
            MyApp.ExitAuthsJobTitles = ExitAuthsJobTitles;
            Log.d("Custodytitles" , "in ");
            Log.d("CustodyUsers" , MyApp.db.getUser().DirectManager+"");
            for (int p=0; p<ExitAuthsJobTitles.size();p++) {
                Log.d("Custodytitles" , ExitAuthsJobTitles.get(p).JobTitle+" "+p);
                if (ExitAuthsJobTitles.get(p).JobTitle.equals("Direct Manager")) {
                    for (int y =0 ; y<MyApp.EMPS.size();y++) {
                        if (MyApp.EMPS.get(y).JobNumber == MyApp.db.getUser().DirectManager) {
                            ExitAuthUsers.add(MyApp.EMPS.get(y));
                            break;
                        }
                    }
                }
                else
                if (USER.searchUserByJobtitle(MyApp.EMPS,ExitAuthsJobTitles.get(p).JobTitle) != null ) {
                    ExitAuthUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,ExitAuthsJobTitles.get(p).JobTitle));
                }
            }
            //Log.d("CustodyUsers" , RequestCustodyAuthsJobTitles.size()+" "+CustodyAuthUsers.size());
            MyApp.ExitAuthUsers = ExitAuthUsers ;
        }
        showHideMyApprovals();
    }


    //----------------------------

    void getHrOrderTypes() {
        Loading l = new Loading(act); l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getHrOrdersTypes, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response != null ) {
                    if (response.equals("0")) {

                    }
                    else {
                        Log.d("hrOrdersResponse" , response );
                        List<Object> list = JsonToObject.translate(response,HR_ORDER_TYPE.class,act);
                        Types.clear();
                        for(int i=0 ; i<list.size() ; i++) {
                            HR_ORDER_TYPE x = (HR_ORDER_TYPE) list.get(i) ;
                            Types.add(x);
                        }
                        MyApp.Types = Types ;
                        //ordersTypes.setAdapter(adapter);
                        getJobTitles();
                        //getResignationAuthEmps();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        });
        Volley.newRequestQueue(act).add(request);
    }

    void getJobTitles()  {
        Log.d("jobtitles","jotitles started");
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getJobTitlesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jobtitles",response);
                l.close();
                if (response != null ) {
                    List<Object> list = JsonToObject.translate(response,JobTitle.class,act);
                    JobTitles.clear();
                    for (int i = 0; i< list.size();i++) {
                        JobTitle j = (JobTitle) list.get(i) ;
                        JobTitles.add(j);
                    }
                    MyApp.JobTitles = JobTitles ;
                    getResignationAuthEmps();
                    //getAdvancePaymentsAuthEmps();
                    //getBacksAuthEmps();
                    //getVacationAuthEmps();
                    //getVacationSalariesAuthEmps();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("jobtitles",error.getMessage());
            }
        });
        Volley.newRequestQueue(act).add(request);
    }

    void showHideMyApprovals() {
        boolean res = false ;

        for (int i=0 ; i<MyApp.EMPS.size();i++) {
            if (MyApp.EMPS.get(i).DirectManager == MyApp.db.getUser().JobNumber) {
                res = true ;
            }
        }
        for (USER u :VacationsAuthUsers) {
            if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                res = true ;
            }
        }
        for (USER u :AdvancePaymentaAuthUsers) {
            if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                res = true ;
            }
        }
        for (USER u :BacksAuthUsers) {
            if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                res = true ;
            }
        }
        for (USER u :VacationSalaryAuthUsers) {
            if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                res = true ;
            }
        }
        for (USER u :CustodyAuthUsers) {
            if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                res = true ;
            }
        }
        for (USER u :ResignationsAuthUsers) {
            if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                res = true ;
            }
        }

        //Log.d("myApprovalStatus",String.valueOf(res)+" empsare "+MainPage.EMPS.size()+" "+AdvancePaymentaAuthUsers.size()+ResignationsAuthUsers.size()+VacationsAuthUsers.size()+VacationSalaryAuthUsers.size()+BacksAuthUsers.size());

        if (res) {
            //myApprovals.setVisibility(View.VISIBLE);
//            AResignationsAuthsJobtitles = new ArrayList<JobTitle>();
//            AVacationsAuthJobtitles = new ArrayList<JobTitle>();
//            ABacksAuthJobtitles = new ArrayList<JobTitle>();
//            AAdvancePaymentsAuthJobtitles = new ArrayList<JobTitle>();
//            AVacationSalaryAuthJobtitles = new ArrayList<JobTitle>();
//            ARequestCustodyAuthsJobTitles = new ArrayList<JobTitle>();
//            AResignationsAuthUsers = new ArrayList<USER>();
//            AVacationsAuthUsers = new ArrayList<USER>();
//            ABacksAuthUsers = new ArrayList<USER>();
//            AAdvancePaymentaAuthUsers = new ArrayList<USER>();
//            AVacationSalaryAuthUsers = new ArrayList<USER>();
//            ACustodyAuthUsers = new ArrayList<USER>();
//            getVacationAuthEmpsApprove();
//            getVacationSalariesAuthEmpsApprove();
//            getAdvancePaymentsAuthEmpsApprove();
//            getBacksAuthEmpsApprove();
//            getRequestCustodyAuthEmpsApprove();
//            getResignationAuthEmpsApprove();
        }
        else {
            //myApprovals.setVisibility(View.GONE);
        }
    }

    public void goToStaffByLocation(View view) {
        Intent i = new Intent(act, ManageStaffAttendanceByLocation.class);
        startActivity(i);
    }

    public void goToCheckEmpsAttendance(View view) {
        Intent i = new Intent(act, CheckEmpsAttendance.class);
        startActivity(i);
    }

    public void goToRequestCustody(View view) {
        Intent i = new Intent(act, RequestCustody.class);
        startActivity(i);
    }

    public void sendNewAdWithImage(View view) {
        Intent i = new Intent(act, SendNewAdWithImage.class);
        startActivity(i);
    }

    public void goToExitRequest(View view) {
        Intent i = new Intent(act, ExitRequest.class);
        startActivity(i);
    }

    public void goToRating(View view) {
        Intent i = new Intent(act, RatingEmployees.class);
        startActivity(i);
    }

    public void goToMySalaryReports(View view) {
        Intent i = new Intent(act, MySalaryReports.class);
        startActivity(i);
    }

    public void goToRequestBonus(View view) {
        Intent i = new Intent(act, RequestBonus.class);
        startActivity(i);
    }

    public void goToMyAttendance(View view) {
        Intent i = new Intent(act, MyAttendTable.class);
        startActivity(i);
    }
}