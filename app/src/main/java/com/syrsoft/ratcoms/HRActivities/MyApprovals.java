package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HR;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_BONUS_REQUEST_ADAPTER;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_EXIT_REQUEST_ADAPTER;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_AdvancePayment_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_Custody_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApproval_VacationSalary_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApprovals_Backs_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApprovals_Resignation_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApprovals_Vacation_Adapter;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.Login;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MainPage;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VollyCallback;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class MyApprovals extends AppCompatActivity {

    public Activity act ;
    static RecyclerView bonusRecycler,resignations , advancePayments , backToWorks , CustodyRequestsRecycler , vacations , vacationSalarys ,ExitRequestsRecycler;
    static MyApprovals_Resignation_Adapter resignationsAdapter ;
    static MyApprovals_Vacation_Adapter vacationAdapter;
    static MyApprovals_Backs_Adapter backsAdapter ;
    static MyApproval_AdvancePayment_Adapter advanceAdapter ;
    static MyApproval_VacationSalary_Adapter vacationsalary_Adapter ;
    static MyApproval_Custody_Adapter custodyRequest_Adapter ;
    static MyApproval_EXIT_REQUEST_ADAPTER exitRequest_Adapter ;
    static MyApproval_BONUS_REQUEST_ADAPTER bonusRequest_Adapter;
    static List<RESIGNATION_CLASS> resignationsList ;
    static List<VACATION_CLASS> vacationslist ;
    static List<BACKTOWORK_CLASS> backList ;
    static List<ADVANCEPAYMENT_CLASS> advanceList ;
    static List<VACATIONSALARY_CLASS> vacationSalaryList ;
    static List<CUSTODY_REQUEST_CLASS> custodyRequestList ;
    static List<EXIT_REQUEST_CLASS> exiteList ;
    public static List<BONUS> bonusList ;
    private RadioButton done , undone ;
    public static int Status = 0 ;
    static String getResignationsForApprovalUrl = MyApp.MainUrl+"getResignationsForApproval.php" ;
    static String getExitRequestsForApprovUrl = MyApp.MainUrl+"getExitRequestForApproval.php" ;
    static String getVacationsForApprovalUrl = MyApp.MainUrl+ "getVacationsForApproval.php";
    static String getBonusRequestsForApprovUrl = MyApp.MainUrl + "getBonusRequestsForApprovals.php";
    static String getBacksForApprovalUrl = MyApp.MainUrl+"getBacksForApproval.php";
    static String getAdvanceApprovalsUrl = MyApp.MainUrl+"getAdvancePaymentsForApproval.php";
    static String getVacationSalaryRequestsForApprove = MyApp.MainUrl+"getVacationsalaryForApproval.php";
    static String getCustodyForApprovalURL = MyApp.MainUrl+"getRequestCustodyForApproval.php" ;
    static String[] approveOrderUrl = new String[]{MyApp.MainUrl+"responseAdvancePaymentOrder.php",MyApp.MainUrl+"responseResignation.php",MyApp.MainUrl+"responseVacation.php",MyApp.MainUrl+"responseVacationSalary.php",MyApp.MainUrl+"responseBack.php",MyApp.MainUrl+"responseCustodyRequest.php",MyApp.MainUrl+"responseExitRequest.php",MyApp.MainUrl+"responseBonusRequests.php"} ;
    static String LoginUrl = MyApp.MainUrl+"appLoginEmployees.php" ;
    public static List<String> tokens = new ArrayList<>() ;
    static BiometricPrompt biometricPrompt;
    static BiometricPrompt.PromptInfo promptInfo;
    public static boolean biometricAvailable = false ;
    private static Object THE_ORDER ;
    public static boolean AorR ;
    static String Notes ;
    static JobTitle DirectManagerObject ;
    public static List<List<USER>> BonusOrdersAuthUsers,VacationOrdersAuthUsers,ExitOrdersAuthUsers,ResignationOrdersAuthUsers,VacationSalaryOrdersAuthUsers,CustodyOrdersAuthUsers , BacksOrdersAuthUsers,AdvancesOrdersAuthUsers ;
    public static int POSITION = 0 ;
    public static boolean isRunning = false ;
    private LinearLayout BonusCaption,ExitCaption,ResignationCaption , VacationCaption , VacationSalaryCaption , CustodyCaption , BackCaption , AdvanceCaption ,SpinnersLayout,VacationLayout,VSLayout,AdvanceLayout,ResignationLayout,BackLayout,CustodyLayout,ExitLayout,BonusLayout ;
    private Spinner OrderTypeSpinner , EmployeesSpinner ;
    String[] ordertypes ;
    String SelectedOrderType , START , END ;
    USER SelectedUser ;
    List<USER> XX ;
    SharedPreferences prefs ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_approvals);
        prefs = getSharedPreferences("com.syrsoft.ratcoms", MODE_PRIVATE);
        if (MyApp.app == null || MyApp.MyUser == null) {
            Intent i = new Intent(getBaseContext(), Login.class);
            startActivity(i);
        }
        isRunning = true ;
        setActivity();
        setActivityActions();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false ;
    }

    void setActivity() {
        act = this ;
        ordertypes = new String[] {"All",getResources().getString(R.string.vacation),getResources().getString(R.string.vacationSalary),getResources().getString(R.string.advancePayment),getResources().getString(R.string.resignation),getResources().getString(R.string.backtowork),getResources().getString(R.string.requestCustody),getResources().getString(R.string.requestBonus)} ;
        VacationLayout = findViewById(R.id.VacationLayout);
        VSLayout = findViewById(R.id.VacationSalaryLayout);
        AdvanceLayout = findViewById(R.id.AdvanceLayout);
        ExitLayout = findViewById(R.id.ExitLayout);
        ResignationLayout = findViewById(R.id.ResignationLayout);
        BackLayout = findViewById(R.id.BackLayout);
        CustodyLayout = findViewById(R.id.CustodyLayout);
        BonusLayout = findViewById(R.id.BonusLayout);
        ResignationCaption = findViewById(R.id.ResignationCaption);
        VacationCaption = findViewById(R.id.VacationCaption);
        VacationSalaryCaption = findViewById(R.id.VacationSalaryCaption);
        CustodyCaption = findViewById(R.id.CustodyCaption);
        BackCaption = findViewById(R.id.BackCaption);
        AdvanceCaption = findViewById(R.id.AdvanceCaption);
        ExitCaption = findViewById(R.id.ExitCaption);
        BonusCaption = findViewById(R.id.BonusCaption);
        SpinnersLayout = findViewById(R.id.Spinners_Layout);
        OrderTypeSpinner = findViewById(R.id.OrderTypes_Spinner);
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(act, R.layout.spinner_item, ordertypes);
        OrderTypeSpinner.setAdapter(typesAdapter);
        EmployeesSpinner = findViewById(R.id.Emps_Spinner);
        XX = new ArrayList<>();
        String[] emps ;
        if (MyApp.MyUser.JobTitle.equals("Manager") || MyApp.MyUser.JobTitle.equals("Sales Manager")|| MyApp.MyUser.JobTitle.equals("Accountant")) {
            emps = new String[MyApp.EMPS.size()+1];
            emps[0] = "All";
            for (USER u :MyApp.EMPS) {
                XX.add(u);
            }
            for (int i=0;i<XX.size();i++) {
                emps[i+1] = XX.get(i).FirstName+" "+XX.get(i).LastName ;
            }
            ArrayAdapter<String> empsArr = new ArrayAdapter<>(act, R.layout.spinner_item, emps);
            EmployeesSpinner.setAdapter(empsArr);
        }
        else {
            for (int i=0;i<MyApp.EMPS.size();i++) {
                if (MyApp.EMPS.get(i).DirectManager == MyApp.MyUser.JobNumber) {
                    XX.add(MyApp.EMPS.get(i));
                }
            }
            emps = new String[XX.size()+1];
            emps[0] = "All";
            for (int i=0;i<XX.size();i++) {
                emps[i+1] = XX.get(i).FirstName+" "+XX.get(i).LastName ;
            }
            ArrayAdapter<String> empsArr = new ArrayAdapter<>(act,R.layout.spinner_item,emps);
            EmployeesSpinner.setAdapter(empsArr);
        }
        SpinnersLayout.setVisibility(View.GONE);
        TextView ResignationMarker = findViewById(R.id.ResignationMarkerText);
        TextView VacationMarker = findViewById(R.id.VMarkerText) ;
        TextView VSMarker = findViewById(R.id.VSMarkerText) ;
        TextView ExitMarker = findViewById(R.id.ExitMarkerText) ;
        TextView CustodyMarker = findViewById(R.id.CustodyMarkerText);
        TextView BackMarker  = findViewById(R.id.BackMarkerText);
        TextView AdvanceMarker = findViewById(R.id.AdvanceMarkerText);
        TextView BonusMarker = findViewById(R.id.BonusMarkerText);
        DirectManagerObject = new JobTitle(4,"Direct Manager",9,"المدير المباشر");
        bonusRecycler = findViewById(R.id.MyApprovals_Bonus_Recycler);
        bonusRecycler.setNestedScrollingEnabled(false);
        bonusRecycler.setVisibility(View.GONE);
        ExitRequestsRecycler = findViewById(R.id.Exit_Recycler);
        ExitRequestsRecycler.setNestedScrollingEnabled(false);
        ExitRequestsRecycler.setVisibility(View.GONE);
        resignations = findViewById(R.id.MyApprovals_Resignations_Recycler);
        resignations.setNestedScrollingEnabled(false);
        resignations.setVisibility(View.GONE);
        vacations = findViewById(R.id.vacations_recycler);
        vacations.setNestedScrollingEnabled(false);
        vacations.setVisibility(View.GONE);
        backToWorks = findViewById(R.id.backs_recycler);
        backToWorks.setNestedScrollingEnabled(false);
        backToWorks.setVisibility(View.GONE);
        advancePayments = findViewById(R.id.advanceRecycler);
        advancePayments.setNestedScrollingEnabled(false);
        advancePayments.setVisibility(View.GONE);
        vacationSalarys = findViewById(R.id.VacationSalary_Recycler);
        vacationSalarys.setNestedScrollingEnabled(false);
        vacationSalarys.setVisibility(View.GONE);
        CustodyRequestsRecycler = findViewById(R.id.Custody_Recycler);
        CustodyRequestsRecycler.setNestedScrollingEnabled(false);
        CustodyRequestsRecycler.setVisibility(View.GONE);
        resignationsList = new ArrayList<>();
        vacationslist = new ArrayList<>();
        backList = new ArrayList<>();
        advanceList = new ArrayList<>();
        vacationSalaryList = new ArrayList<>();
        custodyRequestList = new ArrayList<>();
        exiteList = new ArrayList<>();
        bonusList = new ArrayList<>();
        VacationOrdersAuthUsers = new ArrayList<>();
        ResignationOrdersAuthUsers = new ArrayList<>();
        VacationSalaryOrdersAuthUsers = new ArrayList<>();
        CustodyOrdersAuthUsers = new ArrayList<>();
        BacksOrdersAuthUsers = new ArrayList<>();
        AdvancesOrdersAuthUsers = new ArrayList<>();
        ExitOrdersAuthUsers = new ArrayList<>();
        RecyclerView.LayoutManager resignationsManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager vacationsManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager backsManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager advanceManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager vacationsalaryManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager custodyManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager exitManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager bonusManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        bonusRecycler.setLayoutManager(bonusManager);
        resignations.setLayoutManager(resignationsManager);
        vacations.setLayoutManager(vacationsManager);
        backToWorks.setLayoutManager(backsManager);
        advancePayments.setLayoutManager(advanceManager);
        vacationSalarys.setLayoutManager(vacationsalaryManager);
        CustodyRequestsRecycler.setLayoutManager(custodyManager);
        ExitRequestsRecycler.setLayoutManager(exitManager);
        resignationsAdapter = new MyApprovals_Resignation_Adapter(resignationsList);
        vacationAdapter = new MyApprovals_Vacation_Adapter(vacationslist);
        backsAdapter = new MyApprovals_Backs_Adapter(backList);
        advanceAdapter = new MyApproval_AdvancePayment_Adapter(advanceList);
        vacationsalary_Adapter = new MyApproval_VacationSalary_Adapter(vacationSalaryList);
        custodyRequest_Adapter = new MyApproval_Custody_Adapter(custodyRequestList);
        done = findViewById(R.id.radioButton2);
        done.setChecked(false);
        done.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                undone.setChecked(false);
                Status = 1 ;
                SpinnersLayout.setVisibility(View.VISIBLE);
                EmployeesSpinner.setSelection(0);
                ResignationMarker.setText(String.valueOf(resignationsList.size()));
                resignationsAdapter = new MyApprovals_Resignation_Adapter(resignationsList);
                resignations.setAdapter(resignationsAdapter);
                VacationMarker.setText(String.valueOf(vacationslist.size()));
                vacationAdapter = new MyApprovals_Vacation_Adapter(vacationslist);
                vacations.setAdapter(vacationAdapter);
                BackMarker.setText(String.valueOf(backList.size()));
                backsAdapter = new MyApprovals_Backs_Adapter(backList);
                backToWorks.setAdapter(backsAdapter);
                AdvanceMarker.setText(String.valueOf(advanceList.size()));
                advanceAdapter = new MyApproval_AdvancePayment_Adapter(advanceList);
                advancePayments.setAdapter(advanceAdapter);
                VSMarker.setText(String.valueOf(vacationSalaryList.size()));
                vacationsalary_Adapter = new MyApproval_VacationSalary_Adapter(vacationSalaryList);
                vacationSalarys.setAdapter(vacationsalary_Adapter);
                CustodyMarker.setText(String.valueOf(custodyRequestList.size()));
                custodyRequest_Adapter = new MyApproval_Custody_Adapter(custodyRequestList);
                CustodyRequestsRecycler.setAdapter(custodyRequest_Adapter);
                ExitMarker.setText(String.valueOf(exiteList.size()));
                exitRequest_Adapter = new MyApproval_EXIT_REQUEST_ADAPTER(exiteList);
                ExitRequestsRecycler.setAdapter(exitRequest_Adapter);
                BonusMarker.setText(String.valueOf(bonusList.size()));
                bonusRequest_Adapter = new MyApproval_BONUS_REQUEST_ADAPTER(bonusList);
                bonusRecycler.setAdapter(bonusRequest_Adapter);
            }
            else{
                undone.setChecked(true);
                Status = 0 ;
                SpinnersLayout.setVisibility(View.GONE);
            }
        });
        undone = findViewById(R.id.radioButton);
        undone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                done.setChecked(false);
                Status = 0 ;
                VacationLayout.setVisibility(View.VISIBLE);
                VSLayout.setVisibility(View.VISIBLE);
                AdvanceLayout.setVisibility(View.VISIBLE);
                ResignationLayout.setVisibility(View.VISIBLE);
                BackLayout.setVisibility(View.VISIBLE);
                CustodyLayout.setVisibility(View.VISIBLE);
                getResignationsForApprove(act);
                getVacationsForApprove(act);
                getBacksForApprove(act);
                getAdvanceForApprove(act);
                getVacationSalaryRequests(act);
                getCustodyRequests(act);
                getExitRequests(act);
                getBonusRequests(act);
            }
            else{
                done.setChecked(true);
                Status = 1 ;
            }
        });
        undone.setChecked(true);
        if (tokens.size()>0){
            Log.d("targetTokens", tokens.toString());
        }
        Executor executor = ContextCompat.getMainExecutor(act);
        biometricPrompt = new BiometricPrompt((FragmentActivity) act,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                confermByPassword(THE_ORDER,act);
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                if (AorR) {
                    approveOrder(THE_ORDER,act);
                }
                else {
                    rejectOrder(THE_ORDER,act);
                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.bimetricDialogTitle))
                .setSubtitle(getResources().getString(R.string.bimetricDialogMessage))
                .setNegativeButtonText("Use Password")
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) act.getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager != null ) {
                // User hasn't enrolled any fingerprints to authenticate with
                // Everything is ready for fingerprint authentication
                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                    biometricAvailable = false;
                } else biometricAvailable = fingerprintManager.hasEnrolledFingerprints();
            }

        }
    }

    void  setActivityActions () {
        ResignationCaption.setOnClickListener(v -> {
            if (resignations.getVisibility() == View.VISIBLE) {
                resignations.setVisibility(View.GONE);
            }
            else {
                resignations.setVisibility(View.VISIBLE);
            }
        });
        VacationCaption.setOnClickListener(v -> {
            if (vacations.getVisibility() == View.VISIBLE) {
                vacations.setVisibility(View.GONE);
            }
            else {
                vacations.setVisibility(View.VISIBLE);
            }
        });
        VacationSalaryCaption.setOnClickListener(v -> {
            if (vacationSalarys.getVisibility() == View.VISIBLE) {
                vacationSalarys.setVisibility(View.GONE);
            }
            else {
                vacationSalarys.setVisibility(View.VISIBLE);
            }
        });
        CustodyCaption.setOnClickListener(v -> {
            if (CustodyRequestsRecycler.getVisibility() == View.VISIBLE) {
                CustodyRequestsRecycler.setVisibility(View.GONE);
            }
            else {
                CustodyRequestsRecycler.setVisibility(View.VISIBLE);
            }
        });
        BackCaption.setOnClickListener(v -> {
            if (backToWorks.getVisibility() == View.VISIBLE) {
                backToWorks.setVisibility(View.GONE);
            }
            else {
                backToWorks.setVisibility(View.VISIBLE);
            }
        });
        AdvanceCaption.setOnClickListener(v -> {
            if (advancePayments.getVisibility() == View.VISIBLE) {
                advancePayments.setVisibility(View.GONE);
            }
            else {
                advancePayments.setVisibility(View.VISIBLE);
            }
        });
        ExitCaption.setOnClickListener(v -> {
            if (ExitRequestsRecycler.getVisibility() == View.VISIBLE) {
                ExitRequestsRecycler.setVisibility(View.GONE);
            }
            else {
                ExitRequestsRecycler.setVisibility(View.VISIBLE);
            }
        });
        BonusCaption.setOnClickListener(v -> {
            if (bonusRecycler.getVisibility() == View.VISIBLE) {
                bonusRecycler.setVisibility(View.GONE);
            }
            else {
                bonusRecycler.setVisibility(View.VISIBLE);
            }
        });
        OrderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedOrderType = ordertypes[position] ;
                if (SelectedOrderType.equals(getResources().getString(R.string.vacation))) {
                    VacationLayout.setVisibility(View.VISIBLE);
                    VSLayout.setVisibility(View.GONE);
                    AdvanceLayout.setVisibility(View.GONE);
                    ResignationLayout.setVisibility(View.GONE);
                    BackLayout.setVisibility(View.GONE);
                    CustodyLayout.setVisibility(View.GONE);
                    ExitLayout.setVisibility(View.GONE);
                    BonusLayout.setVisibility(View.GONE);
                }
                else if (SelectedOrderType.equals(getString(R.string.vacationSalary))) {
                    VacationLayout.setVisibility(View.GONE);
                    VSLayout.setVisibility(View.VISIBLE);
                    AdvanceLayout.setVisibility(View.GONE);
                    ResignationLayout.setVisibility(View.GONE);
                    BackLayout.setVisibility(View.GONE);
                    CustodyLayout.setVisibility(View.GONE);
                    ExitLayout.setVisibility(View.GONE);
                    BonusLayout.setVisibility(View.GONE);
                }
                else if (SelectedOrderType.equals(getResources().getString(R.string.advancePayment))) {
                    VacationLayout.setVisibility(View.GONE);
                    VSLayout.setVisibility(View.GONE);
                    AdvanceLayout.setVisibility(View.VISIBLE);
                    ResignationLayout.setVisibility(View.GONE);
                    BackLayout.setVisibility(View.GONE);
                    CustodyLayout.setVisibility(View.GONE);
                    ExitLayout.setVisibility(View.GONE);
                    BonusLayout.setVisibility(View.GONE);
                }
                else if (SelectedOrderType.equals(getResources().getString(R.string.resignation))) {
                    VacationLayout.setVisibility(View.GONE);
                    VSLayout.setVisibility(View.GONE);
                    AdvanceLayout.setVisibility(View.GONE);
                    ResignationLayout.setVisibility(View.VISIBLE);
                    BackLayout.setVisibility(View.GONE);
                    CustodyLayout.setVisibility(View.GONE);
                    ExitLayout.setVisibility(View.GONE);
                    BonusLayout.setVisibility(View.GONE);
                }
                else if (SelectedOrderType.equals(getResources().getString(R.string.backtowork))) {
                    VacationLayout.setVisibility(View.GONE);
                    VSLayout.setVisibility(View.GONE);
                    AdvanceLayout.setVisibility(View.GONE);
                    ResignationLayout.setVisibility(View.GONE);
                    BackLayout.setVisibility(View.VISIBLE);
                    CustodyLayout.setVisibility(View.GONE);
                    ExitLayout.setVisibility(View.GONE);
                    BonusLayout.setVisibility(View.GONE);
                }
                else if (SelectedOrderType.equals(getResources().getString(R.string.requestCustody))) {
                    VacationLayout.setVisibility(View.GONE);
                    VSLayout.setVisibility(View.GONE);
                    AdvanceLayout.setVisibility(View.GONE);
                    ResignationLayout.setVisibility(View.GONE);
                    BackLayout.setVisibility(View.GONE);
                    CustodyLayout.setVisibility(View.VISIBLE);
                    ExitLayout.setVisibility(View.GONE);
                    BonusLayout.setVisibility(View.GONE);
                }
                else if (SelectedOrderType.equals("All")) {
                    VacationLayout.setVisibility(View.VISIBLE);
                    VSLayout.setVisibility(View.VISIBLE);
                    AdvanceLayout.setVisibility(View.VISIBLE);
                    ResignationLayout.setVisibility(View.VISIBLE);
                    BackLayout.setVisibility(View.VISIBLE);
                    CustodyLayout.setVisibility(View.VISIBLE);
                    ExitLayout.setVisibility(View.VISIBLE);
                    BonusLayout.setVisibility(View.VISIBLE);
                }
                else if ( SelectedOrderType.equals(getResources().getString(R.string.exitRequest))) {
                    VacationLayout.setVisibility(View.GONE);
                    VSLayout.setVisibility(View.GONE);
                    AdvanceLayout.setVisibility(View.GONE);
                    ResignationLayout.setVisibility(View.GONE);
                    BackLayout.setVisibility(View.GONE);
                    CustodyLayout.setVisibility(View.GONE);
                    ExitLayout.setVisibility(View.VISIBLE);
                    BonusLayout.setVisibility(View.GONE);
                }
                else if ( SelectedOrderType.equals(getResources().getString(R.string.requestBonus))) {
                    VacationLayout.setVisibility(View.GONE);
                    VSLayout.setVisibility(View.GONE);
                    AdvanceLayout.setVisibility(View.GONE);
                    ResignationLayout.setVisibility(View.GONE);
                    BackLayout.setVisibility(View.GONE);
                    CustodyLayout.setVisibility(View.GONE);
                    ExitLayout.setVisibility(View.GONE);
                    BonusLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        EmployeesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 ) {
                    SelectedUser = XX.get(position-1);
                    Log.d("FilterProblem",SelectedUser.FirstName +" "+custodyRequestList.size());
                    if (vacationslist.size() != 0) {
                        List<VACATION_CLASS> vv = new ArrayList<>();
                        for (int i = 0; i < vacationslist.size(); i++) {
                            if (vacationslist.get(i).EmpID == SelectedUser.id) {
                                vv.add(vacationslist.get(i));
                            }
                        }

                            MyApprovals_Vacation_Adapter adapter = new MyApprovals_Vacation_Adapter(vv);
                            vacations.setAdapter(adapter);
                            TextView VacationMarker = findViewById(R.id.VMarkerText) ;
                            VacationMarker.setText(String.valueOf(vv.size()));

                    }
                    if (vacationSalaryList.size() > 0) {
                        List<VACATIONSALARY_CLASS> vss = new ArrayList<>();
                        for (int i = 0; i < vacationSalaryList.size(); i++) {
                            if (vacationSalaryList.get(i).EmpID == SelectedUser.id) {
                                vss.add(vacationSalaryList.get(i));
                            }
                        }

                            MyApproval_VacationSalary_Adapter adapter = new MyApproval_VacationSalary_Adapter(vss);
                            vacationSalarys.setAdapter(adapter);
                            TextView VSMarker = findViewById(R.id.VSMarkerText) ;
                            VSMarker.setText(String.valueOf(vss.size()));

                    }
                    if (advanceList.size()>0) {
                        List<ADVANCEPAYMENT_CLASS> ad = new ArrayList<>();
                        for ( int i=0; i < advanceList.size() ; i++ ) {
                            if (advanceList.get(i).EmpID == SelectedUser.id) {
                                ad.add(advanceList.get(i));
                            }
                        }

                            MyApproval_AdvancePayment_Adapter adap = new MyApproval_AdvancePayment_Adapter(ad);
                            advancePayments.setAdapter(adap);
                            TextView AdvanceMarker = findViewById(R.id.AdvanceMarkerText);
                            AdvanceMarker.setText(String.valueOf(ad.size()));
                    }
                    if (resignationsList.size()>0) {
                        List<RESIGNATION_CLASS> rs = new ArrayList<>();
                        for (int i=0;i<resignationsList.size();i++) {
                            if (resignationsList.get(i).EmpID == SelectedUser.id) {
                                rs.add(resignationsList.get(i));
                            }
                        }

                            MyApprovals_Resignation_Adapter ad = new MyApprovals_Resignation_Adapter(rs);
                            resignations.setAdapter(ad);
                            TextView ResignationMarker = findViewById(R.id.ResignationMarkerText);
                            ResignationMarker.setText(String.valueOf(rs.size()));

                    }
                    if (backList.size()>0) {
                        List<BACKTOWORK_CLASS> bk = new ArrayList<>();
                        for(int i=0;i<backList.size();i++) {
                            if (backList.get(i).EmpID == SelectedUser.id) {
                                bk.add(backList.get(i));
                            }
                        }

                            MyApprovals_Backs_Adapter ada = new MyApprovals_Backs_Adapter(bk);
                            backToWorks.setAdapter(ada);
                            TextView BackMarker  = findViewById(R.id.BackMarkerText);
                            BackMarker.setText(String.valueOf(bk.size()));

                    }
                    if (custodyRequestList.size()>0) {
                        List<CUSTODY_REQUEST_CLASS> cs = new ArrayList<>();
                        for (int i=0 ; i < custodyRequestList.size();i++) {
                            Log.d("FilterProblem",custodyRequestList.get(i).JobNumber +" "+ SelectedUser.JobNumber);
                            if (custodyRequestList.get(i).JobNumber == SelectedUser.JobNumber) {
                                cs.add(custodyRequestList.get(i));
                            }
                        }
                        Log.d("FilterProblem",cs.size()+"");

                            MyApproval_Custody_Adapter adapterr = new MyApproval_Custody_Adapter(cs);
                            CustodyRequestsRecycler.setAdapter(adapterr);
                            TextView CustodyMarker = findViewById(R.id.CustodyMarkerText);
                            CustodyMarker.setText(String.valueOf(cs.size()));

                    }
                    if (exiteList.size()>0) {
                        List<EXIT_REQUEST_CLASS> ex = new ArrayList<>();
                        for (int i=0 ; i < exiteList.size();i++) {
                            Log.d("FilterProblem",exiteList.get(i).JobNumber +" "+ SelectedUser.JobNumber);
                            if (exiteList.get(i).JobNumber == SelectedUser.JobNumber) {
                                ex.add(exiteList.get(i));
                            }
                        }
                        Log.d("FilterProblem",ex.size()+"");

                        MyApproval_EXIT_REQUEST_ADAPTER adapterr = new MyApproval_EXIT_REQUEST_ADAPTER(ex);
                        ExitRequestsRecycler.setAdapter(adapterr);
                        TextView ExitMarker = findViewById(R.id.ExitMarkerText) ;
                        ExitMarker.setText(String.valueOf(ex.size()));
                    }
                }
                else {
                    MyApprovals_Vacation_Adapter adapter = new MyApprovals_Vacation_Adapter(vacationslist);
                    vacations.setAdapter(adapter);
                    MyApproval_VacationSalary_Adapter adapt = new MyApproval_VacationSalary_Adapter(vacationSalaryList);
                    vacationSalarys.setAdapter(adapt);
                    MyApproval_AdvancePayment_Adapter adap = new MyApproval_AdvancePayment_Adapter(advanceList);
                    advancePayments.setAdapter(adap);
                    MyApprovals_Resignation_Adapter ad = new MyApprovals_Resignation_Adapter(resignationsList);
                    resignations.setAdapter(ad);
                    MyApprovals_Backs_Adapter ada = new MyApprovals_Backs_Adapter(backList);
                    backToWorks.setAdapter(ada);
                    MyApproval_Custody_Adapter adapterr = new MyApproval_Custody_Adapter(custodyRequestList);
                    CustodyRequestsRecycler.setAdapter(adapterr);
                    TextView ResignationMarker = findViewById(R.id.ResignationMarkerText);
                    TextView VacationMarker = findViewById(R.id.VMarkerText) ;
                    TextView VSMarker = findViewById(R.id.VSMarkerText) ;
                    TextView ExitMarker = findViewById(R.id.ExitMarkerText) ;
                    TextView CustodyMarker = findViewById(R.id.CustodyMarkerText);
                    TextView BackMarker  = findViewById(R.id.BackMarkerText);
                    TextView AdvanceMarker = findViewById(R.id.AdvanceMarkerText);
                    TextView BonusMarker = findViewById(R.id.BonusMarkerText);
                    VacationMarker.setText(String.valueOf(vacationslist.size()));
                    VSMarker.setText(String.valueOf(vacationSalaryList.size()));
                    AdvanceMarker.setText(String.valueOf(advanceList.size()));
                    ResignationMarker.setText(String.valueOf(resignationsList.size()));
                    BackMarker.setText(String.valueOf(backList.size()));
                    CustodyMarker.setText(String.valueOf(custodyRequestList.size()));
                    ExitMarker.setText(String.valueOf(exiteList.size()));
                    BonusMarker.setText(String.valueOf(bonusList.size()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView startDateTV = findViewById(R.id.startDate);
        TextView endDateTV = findViewById(R.id.endDate);
        startDateTV.setOnClickListener(v -> {
            Dialog D = new Dialog(act);
            D.setContentView(R.layout.dialog_select_date_dialog);
            Window window = D.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            D.setCancelable(false);
            CalendarView C = D.findViewById(R.id.SelectDateDialog_calender);
            TextView date = D.findViewById(R.id.SelectDateDialog_dateTv);
            C.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                START = year+"-"+(month+1)+"-"+dayOfMonth;
                date.setText(START);
            });
            Button Cancel = D.findViewById(R.id.SelectDateDialog_cancel);
            Button Select = D.findViewById(R.id.SelectDateDialog_select);
            Cancel.setOnClickListener(v13 -> D.dismiss());
            Select.setOnClickListener(v14 -> {
                startDateTV.setText(START);
                D.dismiss();
            });
            D.show();
        });
        Calendar c = Calendar.getInstance(Locale.getDefault());
        String x = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
        endDateTV.setText(x);
        endDateTV.setOnClickListener(v -> {
            Dialog D = new Dialog(act);
            D.setContentView(R.layout.dialog_select_date_dialog);
            Window window = D.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            D.setCancelable(false);
            CalendarView C = D.findViewById(R.id.SelectDateDialog_calender);
            TextView date = D.findViewById(R.id.SelectDateDialog_dateTv);
            C.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                END = year+"-"+(month+1)+"-"+dayOfMonth;
                date.setText(END);
            });
            Button Cancel = D.findViewById(R.id.SelectDateDialog_cancel);
            Button Select = D.findViewById(R.id.SelectDateDialog_select);
            Cancel.setOnClickListener(v1 -> D.dismiss());
            Select.setOnClickListener(v12 -> {
                endDateTV.setText(END);
                D.dismiss();
            });
            D.show();
        });
    }

    static void getResignationsForApprove(Activity a) {
        if (USER.resignationsList != null) {
            TextView ResignationMarker = a.findViewById(R.id.ResignationMarkerText);
            ResignationMarker.setText(String.valueOf(USER.resignationsList.size()));
            resignationsAdapter = new MyApprovals_Resignation_Adapter(USER.resignationsList);
            resignations.setAdapter(resignationsAdapter);
        }

    }

    static void getDoneResignations(String start ,String end,VollyCallback callback) {
        resignationsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getResignationsForApprovalUrl, response -> {
            Log.d("resignationsResp" , response);
            if (!response.equals("0")) {
                List<Object> list = JsonToObject.translate(response, RESIGNATION_CLASS.class,MyApp.app);
                resignationsList.clear();
                for (int i=0;i<list.size();i++){
                    RESIGNATION_CLASS r = (RESIGNATION_CLASS) list.get(i);
                    resignationsList.add(r);
                }
                Collections.reverse(resignationsList);
                callback.onSuccess("done");
            }
            else {
                callback.onSuccess("done");
            }

        }, error -> callback.onFailed("error")){
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> par = new HashMap<>();
                par.put("status",String.valueOf(1) );
                par.put("Start",start);
                par.put("End",end);
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFResignations(VollyCallback callback) {
        ResignationOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<resignationsList.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.ResignationsAuthsJobtitles.size(); j++) {
                if (MyApp.ResignationsAuthsJobtitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER directManager = USER.searchUserByJobNumber(MyApp.EMPS,resignationsList.get(i).DirectManager);
                    if (directManager != null ) {
                        if (directManager.VacationStatus == 0) {
                            ll.add(directManager);
                        }
                        else if (directManager.VacationStatus == 1) {
                            USER alternative = USER.searchUserByJobNumber(MyApp.EMPS,directManager.VacationAlternative);
                            if (alternative != null) {
                                ll.add(alternative);
                            }
                        }
                    }
                }
                else if (MyApp.ResignationsAuthsJobtitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,USER.searchUserByJobNumber(MyApp.EMPS,resignationsList.get(i).JobNumber).DepartmentManager);
                    if (departmentM != null ) {
                        if (departmentM.VacationStatus == 0) {
                            ll.add(departmentM);
                        }
                        else if (departmentM.VacationStatus == 1) {
                            USER alternative = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (alternative != null) {
                                ll.add(alternative);
                            }
                        }
                    }
                }
                else {
                    USER x = USER.searchUserByJobtitle(MyApp.EMPS,MyApp.ResignationsAuthsJobtitles.get(j).JobTitle);
                    if ( x != null) {
                        if (x.VacationStatus == 0 ) {
                            ll.add(x);
                        }
                        else if (x.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,x.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
            }
            ResignationOrdersAuthUsers.add(ll);
        }

        int[] toRemoveRows = new int[resignationsList.size()];
        for (int i =0 ; i < ResignationOrdersAuthUsers.size();i++) {
            boolean s = false ;
            for (USER u : ResignationOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.MyUser.JobNumber) {
                    s = true;
                    break;
                }
            }
            if (!s) {
                toRemoveRows[i]=1;
            }
            else {
                toRemoveRows[i] = -1 ;
            }
        }
        try {
            int removed = 0 ;
            for (int i = 0; i < toRemoveRows.length; i++) {
                if (toRemoveRows[i] == 1) {
                    resignationsList.remove(i-removed);
                    ResignationOrdersAuthUsers.remove(i-removed);
                    removed++;
                }
            }
        }
        catch (Exception e)
        {
            Log.d("error",e.getMessage());
        }
        callback.onSuccess("done");
    }

    // ____________________________________________________________________________

    static void getVacationsForApprove(Activity a){
        if (USER.vacationslist != null) {
            TextView VacationMarker = a.findViewById(R.id.VMarkerText) ;
            VacationMarker.setText(String.valueOf(USER.vacationslist.size()));
            vacationAdapter = new MyApprovals_Vacation_Adapter(USER.vacationslist);
            vacations.setAdapter(vacationAdapter);
        }
    }

    static void getDoneVacations(String start ,String end,VollyCallback callback){
        vacationslist = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getVacationsForApprovalUrl, response -> {
            if (!response.equals("0")){
                List<Object> list = JsonToObject.translate(response, VACATION_CLASS.class,MyApp.app);
                vacationslist.clear();
                for (int i=0;i<list.size();i++){
                    VACATION_CLASS r = (VACATION_CLASS) list.get(i);
                    vacationslist.add(r);
                }
                //vacations.setAdapter(vacationAdapter);
                Collections.reverse(vacationslist);
                callback.onSuccess("done");
            }
            else {
                callback.onSuccess("done");
            }

        }, error -> callback.onFailed("error")){
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> par = new HashMap<>();
                par.put("status",String.valueOf(1) );
                par.put("Start",start);
                par.put("End",end);
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFVacations(VollyCallback callback) {
        VacationOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<vacationslist.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.VacationsAuthJobtitles.size(); j++) {
                if (MyApp.VacationsAuthJobtitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER directManager = USER.searchUserByJobNumber(MyApp.EMPS,vacationslist.get(i).DirectManager);
                    if (directManager != null ) {
                        if (directManager.VacationStatus == 0 ) {
                            ll.add(directManager);
                        }
                        else if (directManager.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,directManager.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else if (MyApp.VacationsAuthJobtitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,USER.searchUserByJobNumber(MyApp.EMPS,vacationslist.get(i).JobNumber).DepartmentManager);
                    if (departmentM != null ) {
                        if (departmentM.VacationStatus == 0 ) {
                            ll.add(departmentM);
                        }
                        else if (departmentM.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS,MyApp.VacationsAuthJobtitles.get(j).JobTitle);
                    if (user != null) {
                        if (user.VacationStatus == 0) {
                            ll.add(user);
                        }
                        else if (user.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
            }
            VacationOrdersAuthUsers.add(ll);
        }
        int[] toRemoveRows = new int[vacationslist.size()];
        for (int i =0 ; i < vacationslist.size();i++) {
            boolean s = false ;
            for (USER u : VacationOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.MyUser.JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                toRemoveRows[i] = 1 ;
            }
            else {
                toRemoveRows[i] = -1 ;
            }
        }
        try {
            int removed = 0;
            for (int i = 0; i < toRemoveRows.length; i++) {
                if (toRemoveRows[i] == 1) {
                    vacationslist.remove(i-removed);
                    VacationOrdersAuthUsers.remove(i-removed);
                    removed++;
                }
            }
        }
        catch (Exception e){Log.d("error",e.getMessage());}
        callback.onSuccess("done");
    }

    // ______________________________________________________________________________

    static void getBacksForApprove(Activity a) {
        if (USER.backList != null) {
            TextView BackMarker = a.findViewById(R.id.BackMarkerText);
            BackMarker.setText(String.valueOf(USER.backList.size()));
            backsAdapter = new MyApprovals_Backs_Adapter(USER.backList);
            backToWorks.setAdapter(backsAdapter);
        }
    }

    static void getDoneBacks(String start ,String end,VollyCallback callback){
        backList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getBacksForApprovalUrl, response -> {
            if (!response.equals("0")){
                List<Object> list = JsonToObject.translate(response, BACKTOWORK_CLASS.class,MyApp.app);
                backList.clear();
                for (int i=0;i<list.size();i++){
                    BACKTOWORK_CLASS r = (BACKTOWORK_CLASS) list.get(i);
                    backList.add(r);
                }
                Collections.reverse(backList);
                callback.onSuccess("done");
            }
            else {
                callback.onSuccess("done");
            }

        }, error -> callback.onFailed("error")){
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> par = new HashMap<>();
                par.put("status",String.valueOf(1) );
                par.put("Start",start);
                par.put("End",end);
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFBacks(VollyCallback callback) {
        BacksOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<backList.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.BacksAuthJobtitles.size(); j++) {
                if (MyApp.BacksAuthJobtitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,backList.get(i).DirectManager);
                    if (direct != null ) {
                        if (direct.VacationStatus == 0 ) {
                            ll.add(direct);
                        }
                        else if (direct.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else if (MyApp.BacksAuthJobtitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS, USER.searchUserByJobNumber(MyApp.EMPS,backList.get(i).JobNumber).DepartmentManager);
                    if (departmentM != null ) {
                        if (departmentM.VacationStatus == 0 ) {
                            ll.add(departmentM);
                        }
                        else if (departmentM.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS,MyApp.BacksAuthJobtitles.get(j).JobTitle);
                    if (user != null) {
                        if (user.VacationStatus == 0 ) {
                            ll.add(user);
                        }
                        else if (user.VacationStatus == 1) {
                            USER alt  = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
            }
            BacksOrdersAuthUsers.add(ll);
        }
        int[] rowsToRemove = new int[backList.size()];
        for (int i =0 ; i < BacksOrdersAuthUsers.size();i++) {
            boolean s = false ;
            for (USER u : BacksOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.MyUser.JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                rowsToRemove[i] = 1 ;
            }
            else {
                rowsToRemove[i] = -1 ;
            }
        }
        try {
            int removed = 0 ;
            for (int i = 0; i < rowsToRemove.length; i++) {
                if (rowsToRemove[i] == 1) {
                    BacksOrdersAuthUsers.remove(i-removed);
                    backList.remove(i-removed);
                    removed++ ;
                }
            }
        }
        catch (Exception e){Log.d("error",e.getMessage());}
        callback.onSuccess("done");
    }

    // ________________________________________________________________________

    static void getAdvanceForApprove(Activity a) {
            if (USER.advanceList != null) {
                TextView AdvanceMarker = a.findViewById(R.id.AdvanceMarkerText);
                AdvanceMarker.setText(String.valueOf(USER.advanceList.size()));
                advanceAdapter = new MyApproval_AdvancePayment_Adapter(USER.advanceList);
                advancePayments.setAdapter(advanceAdapter);
            }
    }

    static void getDoneAdvance(String start ,String end,VollyCallback callback){
        advanceList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getAdvanceApprovalsUrl , response -> {
            if (!response.equals("0")){
                List<Object> list = JsonToObject.translate(response, ADVANCEPAYMENT_CLASS.class,MyApp.app);
                Log.d("advanceResponse" , list.size()+"");
                advanceList.clear();
                for (int i=0;i<list.size();i++){
                    ADVANCEPAYMENT_CLASS r =(ADVANCEPAYMENT_CLASS) list.get(i);
                    advanceList.add(r);
                }
                Log.d("advanceResponse" , advanceList.size()+"");
                Collections.reverse(advanceList);
                callback.onSuccess("done");
            }
            else {
                callback.onSuccess("done");
            }

        }, error -> callback.onFailed("error")){
            @Override
            protected Map<String, String> getParams() {
                Map <String,String> par = new HashMap<>();
                par.put("status",String.valueOf(1) );
                par.put("Start",start);
                par.put("End",end);
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFAdvance(VollyCallback callback) {
        AdvancesOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<advanceList.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.AdvancePaymentsAuthJobtitles.size(); j++) {
                if (MyApp.AdvancePaymentsAuthJobtitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,advanceList.get(i).DirectManager);
                    if (direct != null ) {
                        if (direct.VacationStatus == 0) {
                            ll.add(direct);
                        }
                        else if (direct.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                if (MyApp.AdvancePaymentsAuthJobtitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,USER.searchUserByJobNumber(MyApp.EMPS,advanceList.get(i).JobNumber).DepartmentManager);
                    if (departmentM != null ) {
                        if (departmentM.VacationStatus == 0) {
                            ll.add(departmentM);
                        }
                        else if (departmentM.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else {
                    if (USER.searchUserByJobtitle(MyApp.EMPS,MyApp.AdvancePaymentsAuthJobtitles.get(j).JobTitle) != null) {
                        ll.add(USER.searchUserByJobtitle(MyApp.EMPS,MyApp.AdvancePaymentsAuthJobtitles.get(j).JobTitle));
                    }
                }
            }
            AdvancesOrdersAuthUsers.add(ll);
        }
        int[] rowsToRemove = new int[advanceList.size()];
        for (int i =0 ; i < AdvancesOrdersAuthUsers.size();i++) {
            boolean s = false ;
            for (USER u : AdvancesOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.MyUser.JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                rowsToRemove[i] = 1 ;
            }
            else {
                rowsToRemove[i] = -1 ;
            }
        }
        try {
            int removed = 0 ;
            for (int i = 0; i < rowsToRemove.length; i++) {
                if (rowsToRemove[i] == 1) {
                    AdvancesOrdersAuthUsers.remove(i-removed);
                    advanceList.remove(i-removed);
                    removed++ ;
                }
            }
        }
        catch (Exception e){Log.d("error",e.getMessage());}
        callback.onSuccess("done");
    }

    // _______________________________________________________________________

    static void getVacationSalaryRequests(Activity a) {
        if (USER.vacationSalaryList != null) {
            TextView VSMarker = a.findViewById(R.id.VSMarkerText) ;
            VSMarker.setText(String.valueOf(USER.vacationSalaryList.size()));
            vacationsalary_Adapter = new MyApproval_VacationSalary_Adapter(USER.vacationSalaryList);
            vacationSalarys.setAdapter(vacationsalary_Adapter);
        }
    }

    static void getDoneVacationSalaryRequests(String start ,String end,VollyCallback callback){
        vacationSalaryList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getVacationSalaryRequestsForApprove , response -> {
            if (!response.equals("0")){
                List<Object> list = JsonToObject.translate(response, VACATIONSALARY_CLASS.class,MyApp.app);
                vacationSalaryList.clear();
                for (int i=0;i<list.size();i++){
                    VACATIONSALARY_CLASS r =(VACATIONSALARY_CLASS) list.get(i);
                    vacationSalaryList.add(r);
                }
                Collections.reverse(vacationSalaryList);
                callback.onSuccess("done");
            }
            else {
                callback.onSuccess("done");
            }
        }, error -> callback.onFailed("error")){
            @Override
            protected Map<String, String> getParams() {
                Map <String,String> par = new HashMap<>();
                par.put("status",String.valueOf(1) );
                par.put("Start",start);
                par.put("End",end);
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFVSalary(VollyCallback callback) {
        VacationSalaryOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<vacationSalaryList.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.VacationSalaryAuthJobtitles.size(); j++) {
                if (MyApp.VacationSalaryAuthJobtitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,vacationSalaryList.get(i).DirectManager);
                    if (direct != null ) {
                        if (direct.VacationStatus == 0) {
                            ll.add(direct);
                        }
                        else if (direct.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else if (MyApp.VacationSalaryAuthJobtitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,USER.searchUserByJobNumber(MyApp.EMPS,vacationSalaryList.get(i).JobNumber).DepartmentManager);
                    if (departmentM != null ) {
                        if (departmentM.VacationStatus == 0) {
                            ll.add(departmentM);
                        }
                        else if (departmentM.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS,MyApp.VacationSalaryAuthJobtitles.get(j).JobTitle);
                    if ( user!= null) {
                        if (user.VacationStatus == 0) {
                            ll.add(user);
                        }
                        else if (user.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
            }
            VacationSalaryOrdersAuthUsers.add(ll);
        }
        int[] rowaToRemove = new int[vacationSalaryList.size()] ;
        for (int i =0 ; i < VacationSalaryOrdersAuthUsers.size();i++) {
            boolean s = false ;
            for (USER u : VacationSalaryOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.MyUser.JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                rowaToRemove[i] = 1 ;
            }
            else {
                rowaToRemove[i] = -1;
            }
        }
        try {
            int removed = 0 ;
            for (int i = 0; i < rowaToRemove.length; i++) {
                if (rowaToRemove[i] == 1) {
                    vacationSalaryList.remove(i-removed);
                    VacationSalaryOrdersAuthUsers.remove(i-removed);
                    removed++ ;
                }
            }
        }
        catch (Exception e){Log.d("error",e.getMessage());}
        callback.onSuccess("done");
    }

    // _________________________________________________________________________

    static void getCustodyRequests(Activity a){
        if (USER.custodyRequestList != null ) {
            TextView CustodyMarker =  a.findViewById(R.id.CustodyMarkerText);
            CustodyMarker.setText(String.valueOf(USER.custodyRequestList.size()));
            custodyRequest_Adapter = new MyApproval_Custody_Adapter(USER.custodyRequestList);
            CustodyRequestsRecycler.setAdapter(custodyRequest_Adapter);
        }
    }

    static void getDoneCustodyRequests(String start ,String end,VollyCallback callback){
        custodyRequestList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getCustodyForApprovalURL , response -> {
            if (!response.equals("0")){
                List<Object> list = JsonToObject.translate(response, CUSTODY_REQUEST_CLASS.class,MyApp.app);
                Log.d("custodyAResponse" , list.size()+"");

                for (int i=0;i<list.size();i++){
                    CUSTODY_REQUEST_CLASS r =(CUSTODY_REQUEST_CLASS) list.get(i);
                    custodyRequestList.add(r);
                }
                //Log.d("AcustodyAResponse" , custodyRequestList.size()+" "+MyApp.EMPS.size());
                Collections.reverse(custodyRequestList);
                callback.onSuccess("done");
            }
            else {
                callback.onSuccess("done");
            }

        }, error -> callback.onFailed("error")){
            @Override
            protected Map<String, String> getParams() {
                Map <String,String> par = new HashMap<>();
                par.put("status",String.valueOf(1) );
                par.put("Start",start);
                par.put("End",end);
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFCustody(VollyCallback callback) {
        CustodyOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<custodyRequestList.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.RequestCustodyAuthsJobTitles.size(); j++) {
                if (MyApp.RequestCustodyAuthsJobTitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,custodyRequestList.get(i).DirectManager);
                    if (direct != null ) {
                        if (direct.VacationStatus == 0) {
                            ll.add(direct);
                        }
                        else if (direct.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else if (MyApp.RequestCustodyAuthsJobTitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,USER.searchUserByJobNumber(MyApp.EMPS,custodyRequestList.get(i).JobNumber).DepartmentManager);
                    if (departmentM != null ) {
                        if (departmentM.VacationStatus == 0) {
                            ll.add(departmentM);
                        }
                        else if (departmentM.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS,MyApp.RequestCustodyAuthsJobTitles.get(j).JobTitle);
                    if ( user != null) {
                        if (user.VacationStatus == 0) {
                            ll.add(user);
                        }
                        else if (user.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
            }
            CustodyOrdersAuthUsers.add(ll);
        }
        int[] rowsToRemove = new int[custodyRequestList.size()];
        for (int i =0 ; i < CustodyOrdersAuthUsers.size();i++) {
            boolean s = false ;
            for (USER u : CustodyOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.MyUser.JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                rowsToRemove[i] = 1;
            }
            else {
                rowsToRemove[i] = -1 ;
            }
        }
        try {
            int removed = 0 ;
            for (int i = 0; i < rowsToRemove.length; i++) {
                if (rowsToRemove[i] == 1) {
                    custodyRequestList.remove(i-removed);
                    CustodyOrdersAuthUsers.remove(i-removed);
                    removed++;
                }
            }
        }
        catch (Exception e){
            Log.d("error",e.getMessage());
        }
        callback.onSuccess("done");
    }

    //_________________________________________________________________________

    static void getExitRequests(Activity a){
        if (USER.exiteList != null) {
            TextView ExitMarker = a.findViewById(R.id.ExitMarkerText) ;
            ExitMarker.setText(String.valueOf(USER.exiteList.size()));
            exitRequest_Adapter = new MyApproval_EXIT_REQUEST_ADAPTER(USER.exiteList);
            ExitRequestsRecycler.setAdapter(exitRequest_Adapter);
        }
    }

    static void getDoneExitRequests(String start ,String end,VollyCallback callback) {

        exiteList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getExitRequestsForApprovUrl, response -> {
            if (!response.equals("0")){
                List<Object> list = JsonToObject.translate(response, EXIT_REQUEST_CLASS.class,MyApp.app);
                for (int i=0;i<list.size();i++){
                    EXIT_REQUEST_CLASS r =(EXIT_REQUEST_CLASS) list.get(i);
                    exiteList.add(r);
                }
                Collections.reverse(exiteList);
                callback.onSuccess("done");
            }
            else {
                callback.onSuccess("done");
            }

        }, error -> Log.e("exitAResponse" , error.toString())){
            @Override
            protected Map<String, String> getParams() {
                Map <String,String> par = new HashMap<>();
                par.put("status",String.valueOf(1) );
                par.put("Start",start);
                par.put("End",end);
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFExit(VollyCallback callback) {
        ExitOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<exiteList.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.ExitAuthsJobTitles.size(); j++) {
                if (MyApp.ExitAuthsJobTitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,exiteList.get(i).DirectManager);
                    if ( direct != null ) {
                        if (direct.VacationStatus == 0) {
                            ll.add(direct);
                        }
                        else if (direct.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else if (MyApp.ExitAuthsJobTitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,USER.searchUserByJobNumber(MyApp.EMPS,exiteList.get(i).JobNumber).DepartmentManager);
                    if ( departmentM != null ) {
                        if (departmentM.VacationStatus == 0) {
                            ll.add(departmentM);
                        }
                        else if (departmentM.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else {
                    USER user =  USER.searchUserByJobtitle(MyApp.EMPS,MyApp.ExitAuthsJobTitles.get(j).JobTitle);
                    if ( user != null) {
                        if (user.VacationStatus == 0) {
                            ll.add(user);
                        }
                        else if (user.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
            }
            ExitOrdersAuthUsers.add(ll);
        }
        int[] rowsToRemove = new int[exiteList.size()];
        for (int i =0 ; i < ExitOrdersAuthUsers.size();i++) {
            boolean s = false ;
            for (USER u : ExitOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.MyUser.JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                rowsToRemove[i] = 1;
            }
            else {
                rowsToRemove[i] = -1 ;
            }
        }
        try {
            int removed = 0 ;
            for (int i = 0; i < rowsToRemove.length; i++) {
                if (rowsToRemove[i] == 1) {
                    exiteList.remove(i-removed);
                    ExitOrdersAuthUsers.remove(i-removed);
                    removed++;
                }
            }
        }
        catch (Exception e){
            Log.d("error",e.getMessage());
        }
        callback.onSuccess("done");
    }

    //___________________________________________________

    static void getBonusRequests(Activity a){
        if (USER.bonusList != null) {
            TextView BonusMarker = a.findViewById(R.id.BonusMarkerText);
            BonusMarker.setText(String.valueOf(USER.bonusList.size()));
            bonusRequest_Adapter = new MyApproval_BONUS_REQUEST_ADAPTER(USER.bonusList);
            bonusRecycler.setAdapter(bonusRequest_Adapter);
        }
    }

    static void getDoneBonusRequests(String start ,String end,VollyCallback callback) {
        bonusList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getBonusRequestsForApprovUrl, response -> {
            Log.d("BonusResponse" , response);
            if (!response.equals("0")){
                List<Object> list = JsonToObject.translate(response, BONUS.class,MyApp.app);
                for (int i=0;i<list.size();i++){
                    BONUS r =(BONUS) list.get(i);
                    bonusList.add(r);
                }
                Log.d("BonusResponse" , bonusList.size()+" ");
                Collections.reverse(bonusList);
                callback.onSuccess("done");
            }
            else {
                callback.onSuccess("done");
            }

        }, error -> Log.d("BonusResponse" , error.toString())){
            @Override
            protected Map<String, String> getParams() {
                Map <String,String> par = new HashMap<>();
                par.put("status",String.valueOf(1) );
                par.put("Start",start);
                par.put("End",end);
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFBonus(VollyCallback callback) {
        Log.d("BonusResponse" , bonusList.size()+" bonusList");
        BonusOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<bonusList.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.BonusAuthsJobTitles.size(); j++) {
                if (MyApp.BonusAuthsJobTitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS, USER.searchUserByJobNumber(MyApp.EMPS,bonusList.get(i).JobNumber).DirectManager);
                    if ( direct != null ) {
                        if (direct.VacationStatus == 0) {
                            ll.add(direct);
                        }
                        else if (direct.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else if (MyApp.BonusAuthsJobTitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS, USER.searchUserByJobNumber(MyApp.EMPS,bonusList.get(i).JobNumber).DepartmentManager);
                    if ( departmentM != null ) {
                        if (departmentM.VacationStatus == 0) {
                            ll.add(departmentM);
                        }
                        else if (departmentM.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
                else {
                    USER user =  USER.searchUserByJobtitle(MyApp.EMPS,MyApp.BonusAuthsJobTitles.get(j).JobTitle);
                    if ( user != null) {
                        if (user.VacationStatus == 0) {
                            ll.add(user);
                        }
                        else if (user.VacationStatus == 1) {
                            USER alt = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (alt != null) {
                                ll.add(alt);
                            }
                        }
                    }
                }
            }
            BonusOrdersAuthUsers.add(ll);
        }
        Log.d("BonusResponse" , " usersLists"+BonusOrdersAuthUsers.size());
        int[] rowsToRemove = new int[bonusList.size()];
        for (int i =0 ; i < BonusOrdersAuthUsers.size();i++) {
            boolean s = false ;
            for (USER u : BonusOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.MyUser.JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                //CustodyOrdersAuthUsers.remove(i);
                rowsToRemove[i] = 1;
            }
            else {
                rowsToRemove[i] = -1 ;
            }
        }
        try {
            int removed = 0 ;
            for (int i = 0; i < rowsToRemove.length; i++) {
                if (rowsToRemove[i] == 1) {
                    bonusList.remove(i-removed);
                    BonusOrdersAuthUsers.remove(i-removed);
                    removed++;
                }
            }
        }
        catch (Exception e){Log.d("error",e.getMessage());
        }
        callback.onSuccess("done");
    }

    public static void  showBiometricAuth(Object O , String Note ) {
        biometricPrompt.authenticate(promptInfo);
        Log.d("objectName" , O.getClass().getName());
        THE_ORDER = O ;
        Notes = Note ;
    }

    public static void  confermByPassword (Object O , Activity a) {
        THE_ORDER = O ;
        Dialog d = new Dialog(a);
        d.setContentView(R.layout.conferm_by_password);
        EditText pass = d.findViewById(R.id.confermByPassword_pass);
        Button cancel = d.findViewById(R.id.confermByPassword_cancel);
        Button ok = d.findViewById(R.id.confermByPassword_ok);
        cancel.setOnClickListener(v -> d.dismiss());
        ok.setOnClickListener(v -> {
            if (pass.getText() == null || pass.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"enter password",a);
                return;
            }
            Loading l = new Loading(a);
            StringRequest loginrequest = new StringRequest(Request.Method.POST, LoginUrl, response -> {
                l.close();
                Log.d("LoginResponse" , response);
                if (response.equals("0")){
                    ToastMaker.Show(1,"Wrong Username Or Password ",a);
                }
                else
                {
                    d.dismiss();
                    if (AorR) {
                        approveOrder(THE_ORDER,a);
                    }
                    else {
                        rejectOrder(THE_ORDER,a);
                    }
                }
            }, error -> l.close())
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String ,String> pars = new HashMap<>();
                    pars.put("user" , MyApp.MyUser.User);
                    pars.put("password" , pass.getText().toString());
                    return pars;
                }
            };
            Volley.newRequestQueue(a).add(loginrequest);
        });
        d.show();
    }

    public static void  confermByPassword (Object O , String note , Activity a) {
        THE_ORDER = O ;
        Notes = note ;
        Dialog d = new Dialog(a);
        d.setContentView(R.layout.conferm_by_password);
        EditText pass = d.findViewById(R.id.confermByPassword_pass);
        Button cancel = d.findViewById(R.id.confermByPassword_cancel);
        Button ok = d.findViewById(R.id.confermByPassword_ok);
        cancel.setOnClickListener(v -> d.dismiss());
        ok.setOnClickListener(v -> {
            if (pass.getText() == null || pass.getText().toString().isEmpty()) {
                ToastMaker.Show(1,"enter password",a);
                return;
            }
            Loading l = new Loading(a);
            StringRequest loginrequest = new StringRequest(Request.Method.POST, LoginUrl, response -> {
                l.close();
                Log.d("LoginResponse" , response);
                if (response.equals("0")){
                    ToastMaker.Show(1,"Wrong Username Or Password ",a);
                }
                else
                {
                    d.dismiss();
                    if (AorR) {
                        approveOrder(THE_ORDER,a);
                    }
                    else {
                        rejectOrder(THE_ORDER,a);
                    }
                }
            }, error -> {
                //ToastMaker.Show(1,error.getMessage(),act);
                l.close();
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String ,String> pars = new HashMap<>();
                    pars.put("user" , MyApp.MyUser.User);
                    pars.put("password" , pass.getText().toString());
                    return pars;
                }
            };
            Volley.newRequestQueue(a).add(loginrequest);
        });
        d.show();
    }

    static void approveOrder(Object O, Activity a) {
        Log.d("className",O.getClass().getName());
        if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.ADVANCEPAYMENT_CLASS")) {
            ADVANCEPAYMENT_CLASS A = (ADVANCEPAYMENT_CLASS) O ;
            int index = 0 ;
            if (MyApp.AdvancesOrdersAuthUsers != null && MyApp.AdvancesOrdersAuthUsers.size() > 0) {
                for(int i = 0; i< MyApp.AdvancesOrdersAuthUsers.get(POSITION).size(); i++) {
                    if (MyApp.MyUser.JobNumber == MyApp.AdvancesOrdersAuthUsers.get(POSITION).get(i).JobNumber && A.Auths.get(i).getAuth() == 0 ) {
                        index = i+1 ;
                        break;
                    }
                }
                int finalIndex = index;
                if (finalIndex > 0 ) {
                    boolean status = true ;
                    for (int i=0;i<(finalIndex-1);i++) {
                        if (A.Auths.get(i).getAuth() == 0 ){
                            status = false ;
                        }
                    }
                    if (status) {
                        Loading l = new Loading(a);
                        l.show();
                        StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[0], response -> {
                            l.close();
                            if (response.equals("1")) {
                                new MESSAGE_DIALOG(a, "Approval Saved", "Approval Saved");
                                resetCountersService(() -> MyApprovals.getAdvanceForApprove(a),a);
                                for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                    if (A.EmpID == MyApp.EMPS.get(i).id) {
                                        MyApp.CloudMessage(a.getResources().getString(R.string.advancePayment), a.getResources().getString(R.string.advancePayment), A.FName + " " + A.LName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesAdvancePayment", a);
                                        break;
                                    }

                                }
                                MyApp.sendNotificationsToGroup(MyApp.AdvancesOrdersAuthUsers.get(POSITION), a.getResources().getString(R.string.advancePayment), a.getResources().getString(R.string.advancePayment), A.FName + " " + A.LName, A.JobNumber, "NewUpdatesAdvancePayment", a, () -> {
                                });
                            }
                            else {
                                new MESSAGE_DIALOG(a, "Error", "Saving Failed");
                            }
                        }, error -> {
                            l.close();
                            new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed "+error.toString());
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Calendar c = Calendar.getInstance(Locale.getDefault());
                                Map<String, String> par = new HashMap<>();
                                par.put("orderID", String.valueOf(A.id));
                                par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                                par.put("Index", String.valueOf(finalIndex));
                                par.put("Response", "1");
                                par.put("Time", String.valueOf(c.getTimeInMillis()));
                                par.put("total", String.valueOf(USER.AdvancesOrdersAuthUsers.get(POSITION).size()));
                                par.put("Notes", Notes);
                                return par;
                            }
                        };
                        Volley.newRequestQueue(a).add(request);
                    }
                    else {
                        ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",a);
                    }
                }
                else {
                    ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",a);
                }
            }
        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.RESIGNATION_CLASS")) {
            RESIGNATION_CLASS A = (RESIGNATION_CLASS) O ;
            int index = 0 ;
            for(int i = 0; i< MyApp.ResignationOrdersAuthUsers.get(POSITION).size(); i++) {
                if (MyApp.MyUser.JobNumber == MyApp.ResignationOrdersAuthUsers.get(POSITION).get(i).JobNumber && A.Auths.get(i).getAuth() == 0 ) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0 ) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(a);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[1], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(a, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getResignationsForApprove(a),a);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(a.getResources().getString(R.string.resignation), a.getResources().getString(R.string.resignation), A.FName + " " + A.LName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesResignation", a);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(USER.ResignationOrdersAuthUsers.get(POSITION), a.getResources().getString(R.string.resignation), a.getResources().getString(R.string.resignation), A.FName + " " + A.LName, A.JobNumber, "NewUpdatesResignation", a, () -> {

                            });
                        }
                        else {
                            new MESSAGE_DIALOG(a, "Error", "Approval Save Failed");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed");
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "1");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(USER.ResignationOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(a).add(request);
                }
                else {
                        ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",a);
                    }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",a);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.VACATION_CLASS")) {
            VACATION_CLASS A = (VACATION_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.VacationOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.JobNumber == MyApp.VacationOrdersAuthUsers.get(POSITION).get(i).JobNumber && A.Auths.get(i).getAuth() == 0) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex>0) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(a);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[2], response -> {
                        l.close();
                        if (response.equals("1")) {
                            resetCountersService(() -> MyApprovals.getVacationsForApprove(a),a);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(a.getResources().getString(R.string.vacation), a.getResources().getString(R.string.vacation), A.FName + " " + A.LName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesLeave", a);
                                    break;
                                }
                            }
                             MyApp.sendNotificationsToGroup(MyApp.VacationOrdersAuthUsers.get(POSITION), a.getResources().getString(R.string.vacation), a.getResources().getString(R.string.vacation), A.FName + " " + A.LName, A.JobNumber, "NewUpdatesLeave", a, () -> new MESSAGE_DIALOG(a, "Approval Saved", "Approval Saved"));
                        }
                        else {
                           new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed try again");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "1");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.VacationOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(a).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",a);
                }
            }
            else {
                ToastMaker.Show(1, "لقد وافقت على هذا الطلب سابقا", a);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.VACATIONSALARY_CLASS")) {
            VACATIONSALARY_CLASS A = (VACATIONSALARY_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.VacationSalaryOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.JobNumber == MyApp.VacationSalaryOrdersAuthUsers.get(POSITION).get(i).JobNumber && A.Auths.get(i).getAuth() == 0) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0 ) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(a);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[3], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(a, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getVacationSalaryRequests(a),a);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(a.getResources().getString(R.string.vacationSalary), a.getResources().getString(R.string.vacationSalary), MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesVacationSalary", a);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.VacationSalaryOrdersAuthUsers.get(POSITION), a.getResources().getString(R.string.vacationSalary), a.getResources().getString(R.string.vacationSalary), A.FirstName + " " + A.LastName, A.JobNumber, "NewUpdatesVacationSalary", a, () -> {

                            });
                        }
                        else {
                            new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("VID" , String.valueOf(A.VacationID));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "1");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.VacationSalaryOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(a).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",a);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",a);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.BACKTOWORK_CLASS")) {
            BACKTOWORK_CLASS A = (BACKTOWORK_CLASS) O ;
            int index = 0 ;
            for(int i = 0; i< MyApp.BacksOrdersAuthUsers.get(POSITION).size(); i++) {
                if (MyApp.MyUser.JobNumber == MyApp.BacksOrdersAuthUsers.get(POSITION).get(i).JobNumber && A.Auths.get(i).getAuth() == 0 ) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(a);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[4], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(a, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getBacksForApprove(a),a);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(a.getResources().getString(R.string.backtowork), a.getResources().getString(R.string.backtowork), A.FName + " " + A.LName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesBackToWork", a);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.BacksOrdersAuthUsers.get(POSITION), a.getResources().getString(R.string.backtowork), a.getResources().getString(R.string.backtowork), A.FName + " " + A.LName, A.JobNumber, "NewUpdatesBackToWork", a, () -> {


                            });
                        }
                        else {
                            new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "1");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.BacksOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(a).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",a);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",a);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.CUSTODY_REQUEST_CLASS")) {
            CUSTODY_REQUEST_CLASS C = (CUSTODY_REQUEST_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.CustodyOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.JobNumber == MyApp.CustodyOrdersAuthUsers.get(POSITION).get(i).JobNumber && C.Auths.get(i).getAuth() == 0 ) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0 ) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (C.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(a);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[5], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(a, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getCustodyRequests(a),a);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (C.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(a.getResources().getString(R.string.requestCustody), a.getResources().getString(R.string.requestCustody), C.FirstName + " " + C.LastName, C.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesCustodyRequest", a);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.CustodyOrdersAuthUsers.get(POSITION), a.getResources().getString(R.string.requestCustody), a.getResources().getString(R.string.requestCustody), C.FirstName + " " + C.LastName, C.JobNumber, "NewUpdatesCustodyRequest", a, () -> {


                            });
                        }
                        else {
                            new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(C.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "1");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.CustodyOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(a).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",a);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",a);
            }
        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.EXIT_REQUEST_CLASS")) {
            EXIT_REQUEST_CLASS C = (EXIT_REQUEST_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.ExitOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.JobNumber == MyApp.ExitOrdersAuthUsers.get(POSITION).get(i).JobNumber && C.Auths.get(i).getAuth() == 0 ) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0 ) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (C.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(a);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[6], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(a, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getExitRequests(a),a);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (C.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(a.getResources().getString(R.string.exitRequest), a.getResources().getString(R.string.exitRequest), C.Name, C.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesExitRequest", a);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.ExitOrdersAuthUsers.get(POSITION), a.getResources().getString(R.string.exitRequest), a.getResources().getString(R.string.exitRequest), C.Name, C.JobNumber, "NewUpdatesExitRequest", a, () -> {

                            });
                        }
                        else {
                            new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed Try again");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(C.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "1");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.ExitOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(a).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",a);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",a);
            }
        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.BONUS")) {
            BONUS C = (BONUS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.BonusOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.JobNumber == MyApp.BonusOrdersAuthUsers.get(POSITION).get(i).JobNumber && C.Accs.get(i).getAcc() == 0 ) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0 ) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (C.Accs.get(i).getAcc() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(a);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[7], response -> {
                        Log.d("approveResponse" , response);
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(a, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getBonusRequests(a),a);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (C.JobNumber == MyApp.EMPS.get(i).JobNumber) {
                                    MyApp.CloudMessage(a.getResources().getString(R.string.requestBonus), a.getResources().getString(R.string.requestBonus), C.Name, C.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesBonusRequest", a);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.BonusOrdersAuthUsers.get(POSITION), a.getResources().getString(R.string.requestBonus), a.getResources().getString(R.string.requestBonus), C.Name, C.JobNumber, "NewUpdatesBonusRequest", a, () -> {
                            });
                        }
                        else {
                            new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed Try again");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(a, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(C.id));
                            par.put("Acc", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "1");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.BonusOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(a).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",a);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",a);
            }
        }
    }

    static void rejectOrder(Object O,Activity act) {
        Log.d("className",O.getClass().getName());
        if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.ADVANCEPAYMENT_CLASS")) {
            ADVANCEPAYMENT_CLASS A = (ADVANCEPAYMENT_CLASS) O ;
            int index = 0 ;
            for(int i = 0; i< MyApp.AdvancesOrdersAuthUsers.get(POSITION).size(); i++) {
                if (MyApp.MyUser.id == MyApp.AdvancesOrdersAuthUsers.get(POSITION).get(i).id && A.Auths.get(i).getAuth() == 0) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex>0) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(act);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[0], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(act, "Reject Saved", "Reject Saved");
                            resetCountersService(() -> MyApprovals.getAdvanceForApprove(act),act);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(act.getResources().getString(R.string.advancePayment), act.getResources().getString(R.string.advancePayment), A.FName + " " + A.LName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesAdvancePayment", act);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.AdvancesOrdersAuthUsers.get(POSITION), act.getResources().getString(R.string.advancePayment), act.getResources().getString(R.string.advancePayment), A.FName + " " + A.LName, A.JobNumber, "NewUpdatesAdvancePayment", act, () -> {

                            });
                        }
                        else {
                            new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "2");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(USER.AdvancesOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",act);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",act);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.RESIGNATION_CLASS")) {
            RESIGNATION_CLASS A = (RESIGNATION_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.ResignationOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.id == MyApp.ResignationOrdersAuthUsers.get(POSITION).get(i).id && A.Auths.get(i).getAuth() == 0) {
                    //Auth a = new Auth();
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(act); l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[1], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(act, "Reject Saved", "Reject Saved");
                            resetCountersService(() -> MyApprovals.getResignationsForApprove(act),act);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(act.getResources().getString(R.string.resignation), act.getResources().getString(R.string.resignation), A.FName + " " + A.LName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesResignation", act);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.ResignationOrdersAuthUsers.get(POSITION), act.getResources().getString(R.string.resignation), act.getResources().getString(R.string.resignation), A.FName + " " + A.LName, A.JobNumber, "NewUpdatesResignation", act, () -> {

                            });
                        }
                        else {
                            new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "2");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.ResignationOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",act);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",act);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.VACATION_CLASS")) {
            VACATION_CLASS A = (VACATION_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.VacationOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.id == MyApp.VacationOrdersAuthUsers.get(POSITION).get(i).id && A.Auths.get(i).getAuth() == 0) {
                    //Auth a = new Auth();
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0 ) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(act);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[2], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(act, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getVacationsForApprove(act),act);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(act.getResources().getString(R.string.vacation), act.getResources().getString(R.string.vacation), A.FName + " " + A.LName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesLeave", act);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.VacationOrdersAuthUsers.get(POSITION), act.getResources().getString(R.string.vacation), act.getResources().getString(R.string.vacation), A.FName + " " + A.LName, A.JobNumber, "NewUpdatesLeave", act, () -> {
                            });
                        }
                        else {
                            new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "2");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(USER.VacationOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",act);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",act);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.VACATIONSALARY_CLASS")) {
            VACATIONSALARY_CLASS A = (VACATIONSALARY_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.VacationSalaryOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.id == MyApp.VacationSalaryOrdersAuthUsers.get(POSITION).get(i).id && A.Auths.get(i).getAuth() == 0) {
                    //Auth a = new Auth();
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0 ) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(act);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[3], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(act, "Reject Saved", "Reject Saved");
                            resetCountersService(() -> MyApprovals.getVacationSalaryRequests(act),act);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(act.getResources().getString(R.string.vacationSalary), act.getResources().getString(R.string.vacationSalary), MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesVacationSalary", act);
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.VacationSalaryOrdersAuthUsers.get(POSITION), act.getResources().getString(R.string.vacationSalary), act.getResources().getString(R.string.vacationSalary), A.FirstName + " " + A.LastName, A.JobNumber, "NewUpdatesVacationSalary", act, () -> {
                            });
                        }
                        else {
                            new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("VID" , String.valueOf(A.VacationID));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "2");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(USER.VacationSalaryOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",act);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",act);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.BACKTOWORK_CLASS")) {
            BACKTOWORK_CLASS A = (BACKTOWORK_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.BacksOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.id == MyApp.BacksOrdersAuthUsers.get(POSITION).get(i).id && A.Auths.get(i).getAuth() == 0) {
                    //Auth a = new Auth();
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (A.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(act);
                    l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[4], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(act, "Reject Saved", "Reject Saved");
                            resetCountersService(() -> MyApprovals.getBacksForApprove(act),act);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (A.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(act.getResources().getString(R.string.backtowork), act.getResources().getString(R.string.backtowork), A.FName + " " + A.LName, A.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesBackToWork", act);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.BacksOrdersAuthUsers.get(POSITION), act.getResources().getString(R.string.backtowork), act.getResources().getString(R.string.backtowork), A.FName + " " + A.LName, A.JobNumber, "NewUpdatesBackToWork", act, () -> {
                            });
                        }

                    }, error -> {
                        l.close();
                       new MESSAGE_DIALOG(act, "Reject Save Failed", "Reject Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(A.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "2");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.BacksOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",act);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",act);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.CUSTODY_REQUEST_CLASS")) {
            CUSTODY_REQUEST_CLASS C = (CUSTODY_REQUEST_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.CustodyOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.id == MyApp.CustodyOrdersAuthUsers.get(POSITION).get(i).id && C.Auths.get(i).getAuth() == 0 ) {
                    //Auth a = new Auth();
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (C.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(act); l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[5], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(act, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getCustodyRequests(act),act);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (C.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(act.getResources().getString(R.string.requestCustody), act.getResources().getString(R.string.requestCustody), C.FirstName + " " + C.LastName, C.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesCustodyRequest", act);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.CustodyOrdersAuthUsers.get(POSITION), act.getResources().getString(R.string.requestCustody), act.getResources().getString(R.string.requestCustody), C.FirstName + " " + C.LastName, C.JobNumber, "NewUpdatesCustodyRequest", act, () -> {

                            });
                        }
                        else {
                            new MESSAGE_DIALOG(act, "Approval Save Failed", "Approval Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(act, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(C.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "2");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.CustodyOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",act);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",act);
            }

        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.EXIT_REQUEST_CLASS")) {
            EXIT_REQUEST_CLASS C = (EXIT_REQUEST_CLASS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.ExitOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.id == MyApp.ExitOrdersAuthUsers.get(POSITION).get(i).id && C.Auths.get(i).getAuth() == 0 ) {
                    //Auth a = new Auth();
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (C.Auths.get(i).getAuth() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(act); l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[6], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(act, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getExitRequests(act),act);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (C.EmpID == MyApp.EMPS.get(i).id) {
                                    MyApp.CloudMessage(act.getResources().getString(R.string.exitRequest), act.getResources().getString(R.string.exitRequest), C.Name , C.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesExitRequest", act);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.ExitOrdersAuthUsers.get(POSITION), act.getResources().getString(R.string.exitRequest), act.getResources().getString(R.string.exitRequest), C.Name, C.JobNumber, "NewUpdatesExitRequest", act, () -> {

                            });
                        }
                        else {
                            new MESSAGE_DIALOG(act, "Approval Save Failed", "Approval Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(act, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(C.id));
                            par.put("AuthId", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "2");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.ExitOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",act);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",act);
            }
        }
        else if (O.getClass().getName().equals("com.syrsoft.ratcoms.HRActivities.BONUS")) {
            BONUS C = (BONUS) O ;
            int index = 0 ;
            for(int i=0;i<MyApp.BonusOrdersAuthUsers.get(POSITION).size();i++) {
                if (MyApp.MyUser.id == MyApp.BonusOrdersAuthUsers.get(POSITION).get(i).id && C.Accs.get(i).getAcc() == 0 ) {
                    index = i+1 ;
                    break;
                }
            }
            int finalIndex = index;
            if (finalIndex > 0) {
                boolean status = true ;
                for (int i=0;i<(finalIndex-1);i++) {
                    if (C.Accs.get(i).getAcc() == 0 ){
                        status = false ;
                    }
                }
                if (status) {
                    Loading l = new Loading(act); l.show();
                    StringRequest request = new StringRequest(Request.Method.POST, approveOrderUrl[7], response -> {
                        l.close();
                        if (response.equals("1")) {
                            new MESSAGE_DIALOG(act, "Approval Saved", "Approval Saved");
                            resetCountersService(() -> MyApprovals.getBonusRequests(act),act);
                            for (int i = 0; i < MyApp.EMPS.size(); i++) {
                                if (C.JobNumber == MyApp.EMPS.get(i).JobNumber) {
                                    MyApp.CloudMessage(act.getResources().getString(R.string.requestBonus), act.getResources().getString(R.string.requestBonus), C.Name , C.JobNumber, MyApp.EMPS.get(i).Token, "NewUpdatesBonusRequest", act);
                                    break;
                                }
                            }
                            MyApp.sendNotificationsToGroup(MyApp.BonusOrdersAuthUsers.get(POSITION), act.getResources().getString(R.string.requestBonus), act.getResources().getString(R.string.requestBonus), C.Name, C.JobNumber, "NewUpdatesBonusRequest", act, () -> {
                            });
                        }
                        else {
                            new MESSAGE_DIALOG(act, "Approval Save Failed", "Approval Save Failed ");
                        }
                    }, error -> {
                        l.close();
                        new MESSAGE_DIALOG(act, "Approval Save Failed", "Approval Save Failed "+error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            Map<String, String> par = new HashMap<>();
                            par.put("orderID", String.valueOf(C.id));
                            par.put("Acc", String.valueOf(MyApp.MyUser.id));
                            par.put("Index", String.valueOf(finalIndex));
                            par.put("Response", "2");
                            par.put("Time", String.valueOf(c.getTimeInMillis()));
                            par.put("total", String.valueOf(MyApp.BonusOrdersAuthUsers.get(POSITION).size()));
                            par.put("Notes", Notes);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }
                else {
                    ToastMaker.Show(1,"يوجد موافقة قبل موافقتك",act);
                }
            }
            else {
                ToastMaker.Show(1,"لقد وافقت على هذا الطلب سابقا",act);
            }
        }
    }

    static void resetCountersService(VolleyCallback callback , Activity A) {
        MyApp.MYApprovalsCounter = 0 ;
        MyApp.HRCounter = 0 ;
        MyApp.MyUser.getEmployeesData(A, new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                if (HR.isRunning) {
                    HR.setHRCounters();
                }
                if (MainPage.isRunning) {
                    MainPage.setCounters();
                }
                callback.onSuccess();
            }

            @Override
            public void onFailed(String error) {
                callback.onSuccess();
            }
        });
    }

    // get Old Orders _____________________________________________

    public void goSearch(View view) {
        if (Status == 1) {
            TextView startDateTV = findViewById(R.id.startDate);
            TextView endDateTV = findViewById(R.id.endDate);
            if (startDateTV.getText() == null || startDateTV.getText().toString().isEmpty()) {
                new MESSAGE_DIALOG(act,getResources().getString(R.string.startDate),getResources().getString(R.string.startDate));
                return;
            }
            getDoneResignations(startDateTV.getText().toString(),endDateTV.getText().toString(),new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    setTheListsOfAuthUsersOFResignations(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            TextView ResignationMarker = findViewById(R.id.ResignationMarkerText);
                            ResignationMarker.setText(String.valueOf(resignationsList.size()));
                            resignationsAdapter = new MyApprovals_Resignation_Adapter(resignationsList);
                            resignations.setAdapter(resignationsAdapter);
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });

                }

                @Override
                public void onFailed(String error) {

                }
            });
            getDoneVacations(startDateTV.getText().toString(),endDateTV.getText().toString(),new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    setTheListsOfAuthUsersOFVacations(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            TextView VacationMarker = findViewById(R.id.VMarkerText) ;
                            VacationMarker.setText(String.valueOf(vacationslist.size()));
                            vacationAdapter = new MyApprovals_Vacation_Adapter(vacationslist);
                            vacations.setAdapter(vacationAdapter);
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
            getDoneBacks(startDateTV.getText().toString(),endDateTV.getText().toString(),new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    setTheListsOfAuthUsersOFBacks(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            TextView BackMarker  = findViewById(R.id.BackMarkerText);
                            BackMarker.setText(String.valueOf(backList.size()));
                            backsAdapter = new MyApprovals_Backs_Adapter(backList);
                            backToWorks.setAdapter(backsAdapter);
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
            getDoneAdvance(startDateTV.getText().toString(),endDateTV.getText().toString(),new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    setTheListsOfAuthUsersOFAdvance(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            TextView AdvanceMarker = findViewById(R.id.AdvanceMarkerText);
                            AdvanceMarker.setText(String.valueOf(advanceList.size()));
                            advanceAdapter = new MyApproval_AdvancePayment_Adapter(advanceList);
                            advancePayments.setAdapter(advanceAdapter);
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
            getDoneVacationSalaryRequests(startDateTV.getText().toString(),endDateTV.getText().toString(),new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    setTheListsOfAuthUsersOFVSalary(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            TextView VSMarker = findViewById(R.id.VSMarkerText) ;
                            VSMarker.setText(String.valueOf(vacationSalaryList.size()));
                            vacationsalary_Adapter = new MyApproval_VacationSalary_Adapter(vacationSalaryList);
                            vacationSalarys.setAdapter(vacationsalary_Adapter);
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
            getDoneCustodyRequests(startDateTV.getText().toString(),endDateTV.getText().toString(),new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    setTheListsOfAuthUsersOFCustody(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            TextView CustodyMarker = findViewById(R.id.CustodyMarkerText);
                            CustodyMarker.setText(String.valueOf(custodyRequestList.size()));
                            custodyRequest_Adapter = new MyApproval_Custody_Adapter(custodyRequestList);
                            CustodyRequestsRecycler.setAdapter(custodyRequest_Adapter);
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
            getDoneExitRequests(startDateTV.getText().toString(),endDateTV.getText().toString(),new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    setTheListsOfAuthUsersOFExit(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            TextView ExitMarker = findViewById(R.id.ExitMarkerText) ;
                            ExitMarker.setText(String.valueOf(exiteList.size()));
                            exitRequest_Adapter = new MyApproval_EXIT_REQUEST_ADAPTER(exiteList);
                            ExitRequestsRecycler.setAdapter(exitRequest_Adapter);
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
            getDoneBonusRequests(startDateTV.getText().toString(),endDateTV.getText().toString(),new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    setTheListsOfAuthUsersOFBonus(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            TextView BonusMarker = findViewById(R.id.BonusMarkerText);
                            BonusMarker.setText(String.valueOf(bonusList.size()));
                            bonusRequest_Adapter = new MyApproval_BONUS_REQUEST_ADAPTER(bonusList);
                            bonusRecycler.setAdapter(bonusRequest_Adapter);
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }
    }
}