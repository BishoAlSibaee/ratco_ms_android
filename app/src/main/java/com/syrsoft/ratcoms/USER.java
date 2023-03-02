package com.syrsoft.ratcoms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import androidx.annotation.Nullable;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.ADVANCEPAYMENT_CLASS;
import com.syrsoft.ratcoms.HRActivities.BACKTOWORK_CLASS;
import com.syrsoft.ratcoms.HRActivities.BONUS;
import com.syrsoft.ratcoms.HRActivities.CUSTODY_REQUEST_CLASS;
import com.syrsoft.ratcoms.HRActivities.EXIT_REQUEST_CLASS;
import com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE;
import com.syrsoft.ratcoms.HRActivities.JobTitle;
import com.syrsoft.ratcoms.HRActivities.RESIGNATION_CLASS;
import com.syrsoft.ratcoms.HRActivities.VACATIONSALARY_CLASS;
import com.syrsoft.ratcoms.HRActivities.VACATION_CLASS;
import com.syrsoft.ratcoms.PROJECTSActivity.LOCAL_PURCHASE_ORDER;
import com.syrsoft.ratcoms.PROJECTSActivity.SITE_VISIT_ORDER_class;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.syrsoft.ratcoms.MyApp.MainUrl;
import static com.syrsoft.ratcoms.MyApp.MyUser;

public class USER {

    public int id ;
    public int JobNumber ;
    public String User ;
    public String FirstName ;
    public String LastName ;
    public String Department ;
    public String JobTitle ;
    public int DirectManager ;
    public int DepartmentManager ;
    public double WorkLocationLa ;
    public double WorkLocationLo ;
    public String Mobile ;
    public String Email ;
    public String Pic ;
    public String IDNumber ;
    public String IDExpireDate ;
    public String BirthDate ;
    public String Nationality ;
    public String PassportNumber ;
    public String PassportExpireDate ;
    public String ContractNumber ;
    public String ContractStartDate ;
    public int ContractDuration ;
    public String ContractExpireDate ;
    public String InsuranceExpireDate ;
    public String Bank ;
    public String BankAccount ;
    public String BankIban ;
    public int IDsWarningNotifications ;
    public int PASSPORTsWarningNotification ;
    public int CONTRACTsWarningNotification ;
    public double Salary ;
    public double VacationDays ;
    public int VacationStatus;
    public int VacationAlternative ;
    public int SickDays ;
    public int EmergencyDays ;
    public String JoinDate ;
    public String Token ;
    private String getMyPermissionsUrl = MainUrl + "getMyPermissions.php";
    private String getPermissionsUrl = MainUrl + "getPermissions.php" ;
    private String getHrOrdersTypes = MainUrl+"getHrOrdersTypes.php" ;
    private String getJobTitlesUrl = MainUrl+"getJobtitles.php";
    private String getEmpsUrl = MainUrl+"getAllEmps.php" ;
    static String getResignationsForApprovalUrl = MainUrl + "getResignationsForApproval.php" ;
    static String getVacationsForApprovalUrl = MainUrl + "getVacationsForApproval.php";
    static String getBacksForApprovalUrl = MainUrl + "getBacksForApproval.php";
    static String getAdvanceApprovalsUrl = MainUrl + "getAdvancePaymentsForApproval.php";
    static String getVacationSalaryRequestsForApprove = MainUrl + "getVacationsalaryForApproval.php";
    static String getCustodyForApprovalURL = MainUrl + "getRequestCustodyForApproval.php" ;
    static String getExitRequestsForApprovUrl = MyApp.MainUrl + "getExitRequestForApproval.php" ;
    static String getBonusRequestsForApprovUrl = MyApp.MainUrl + "getBonusRequestsForApprovals.php";
    static String getPurchaseOrdersForApprovUrl = MyApp.MainUrl + "getPurchaseOrdersForApprove.php";
    public List<Permission>  MyPermissions;
    static List<HR_ORDER_TYPE> Types ;
    public static List<JobTitle> PurchaseOrdersJobTitles,BonusAuthsJobTitles,ExitAuthsJobTitles,JobTitles , RequestCustodyAuthsJobTitles , ResignationsAuthsJobtitles , VacationsAuthJobtitles , BacksAuthJobtitles , AdvancePaymentsAuthJobtitles , VacationSalaryAuthJobtitles ;
    public static List<USER>  PurchaseAuthUsers,BonusAuthUsers,EMPS,ExitAuthUsers,CustodyAuthUsers ,  ResignationsAuthUsers , VacationsAuthUsers , BacksAuthUsers , AdvancePaymentaAuthUsers , VacationSalaryAuthUsers ;
    public static List<RESIGNATION_CLASS> resignationsList ;
    public static List<VACATION_CLASS> vacationslist ;
    public static List<BACKTOWORK_CLASS> backList ;
    public static List<ADVANCEPAYMENT_CLASS> advanceList ;
    public static List<VACATIONSALARY_CLASS> vacationSalaryList ;
    public static List<CUSTODY_REQUEST_CLASS> custodyRequestList ;
    public static List<EXIT_REQUEST_CLASS> exiteList ;
    public static List<BONUS> bonusList ;
    public static List<LOCAL_PURCHASE_ORDER> purchaseList ;
    public static List<List<USER>> PurchaseOrdersAuthUsers,BonusOrdersAuthUsers,ExitOrdersAuthUsers,VacationOrdersAuthUsers , ResignationOrdersAuthUsers , VacationSalaryOrdersAuthUsers , CustodyOrdersAuthUsers , BacksOrdersAuthUsers,AdvancesOrdersAuthUsers ;
    public static List<SITE_VISIT_ORDER_class> SiteVisitOrders;
    private String getSiteVisitOrdersUrl = MyApp.MainUrl+"getSiteVisitOrdersByTo.php";
    private String getSiteVisitOrdersForGroupUrl = MainUrl+"getSiteVisiOrdersForGroupOfEmployees.php";
    static String getMaintenanceOrdersToOneUserUrl = MainUrl+"getMaintenanceOrdersNumberByTo.php";
    private String getMaintenanceOrdersToDepartmentManagerUrl = MainUrl+"getMaintenanceOrdersNumberOfDepartmentManager.php";
    private String getMaintenanceOrdersToDirectManagerUrl = MainUrl+"getMaintenanceOrdersNumberOfDirectManager.php";
    public List<USER> MyStaff ;
    public boolean isDirectManager = false , isDepartmentManager = false ;

    public USER(int id, int jobNumber, String user, String firstName, String lastName, String department, String jobTitle, int directManager,int departmentManager, double workLocationLa, double workLocationLo, String mobile, String email, String pic, String IDNumber, String IDExpireDate, String birthDate, String nationality, String passportNumber, String passportExpireDate, String contractNumber, String contractStartDate, int contractDuration, String contractExpireDate , String insurance ,String bank, String bankAccount ,String bankIban ,int idsWarning , int passWarning , int contractWarning , double salary, double vacationDays, int sickDays, int emergencyDays,int vacationStatus,int vacationAlternative, String joinDate, String token) {
        this.id = id;
        JobNumber = jobNumber;
        User = user;
        FirstName = firstName;
        LastName = lastName;
        Department = department;
        JobTitle = jobTitle;
        DirectManager = directManager;
        DepartmentManager = departmentManager;
        WorkLocationLa = workLocationLa;
        WorkLocationLo = workLocationLo;
        Mobile = mobile;
        Email = email;
        Pic = pic;
        this.IDNumber = IDNumber;
        this.IDExpireDate = IDExpireDate;
        BirthDate = birthDate;
        Nationality = nationality;
        PassportNumber = passportNumber;
        PassportExpireDate = passportExpireDate;
        ContractNumber = contractNumber;
        ContractStartDate = contractStartDate;
        ContractDuration = contractDuration;
        ContractExpireDate = contractExpireDate;
        InsuranceExpireDate = insurance ;
        Bank = bank;
        BankAccount = bankAccount ;
        BankIban = bankIban ;
        IDsWarningNotifications = idsWarning ;
        PASSPORTsWarningNotification = passWarning ;
        CONTRACTsWarningNotification =contractWarning ;
        Salary = salary;
        VacationDays = vacationDays;
        SickDays = sickDays;
        EmergencyDays = emergencyDays;
        VacationStatus = vacationStatus;
        VacationAlternative = vacationAlternative;
        JoinDate = joinDate;
        Token = token;
    }

    public boolean compareByIdDate(USER other){
        Date my = null ;
        Date otherDate = null  ;
        try {
             my = new SimpleDateFormat("yyyy-MM-dd").parse(this.IDExpireDate);
             otherDate = new SimpleDateFormat("yyyy-MM-dd").parse(other.IDExpireDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (my.before(otherDate) || my.equals(otherDate)){
            return true ;
        }
        else{
            return false ;
        }
    }

    public boolean compareByPassportDate(USER other){
        Date my = null ;
        Date otherDate = null  ;
        try {
            my = new SimpleDateFormat("yyyy-MM-dd").parse(this.PassportExpireDate);
            otherDate = new SimpleDateFormat("yyyy-MM-dd").parse(other.PassportExpireDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (my.before(otherDate) || my.equals(otherDate)){
            return true ;
        }
        else{
            return false ;
        }
    }

    public boolean compareByContractDate(USER other){
        Date my = null ;
        Date otherDate = null  ;
        try {
            my = new SimpleDateFormat("yyyy-MM-dd").parse(this.ContractExpireDate);
            otherDate = new SimpleDateFormat("yyyy-MM-dd").parse(other.ContractExpireDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (my.before(otherDate) || my.equals(otherDate)){
            return true ;
        }
        else{
            return false ;
        }
    }

    public static USER searchUserByJobtitle(List<USER> list , String title) {

        USER u = null ;

        for (int i=0 ; i < list.size() ; i ++ ) {
            if (list.get(i).JobTitle.equals(title)) {
                u = list.get(i);
            }
        }
        return u ;
    }

    public static USER searchUserByJobNumber(List<USER> list , int number) {
        USER u = null ;
        for (int i=0 ; i < list.size() ; i ++ ) {
            if (list.get(i).JobNumber == number ) {
                u = list.get(i);
                break;
            }
        }
        //Log.d("UsersSearch" , u.FirstName);
        return u ;
    }

    public static USER searchUserByID(List<USER> list , int number) {
        USER u = null ;
        for (int i=0 ; i < list.size() ; i ++ ) {
            if (list.get(i).id == number ) {
                u = list.get(i);
                break;
            }
        }
        //Log.d("UsersSearch" , u.FirstName);
        return u ;
    }

    public static List<USER> getSalesEmployees(){
        List<USER> l = new ArrayList<>();
        if (EMPS != null && EMPS.size() != 0){
            for (int i=0 ; i<EMPS.size();i++){
                if(EMPS.get(i).Department.equals("Sales")){
                    l.add(EMPS.get(i));
                }
            }
        }
        return l;
    }

    public static String[] convertListToArrayOfNames(List<USER> list){
        String[] names = new String[list.size()];
        for (int i=0 ; i<list.size();i++){
            names[i] = list.get(i).FirstName+" "+list.get(i).LastName;
        }
        return names;
    }

    public  void getPermissions(VollyCallback callback , Context C) {
        Loading l = null ;
        if (C != null) {
            l = new Loading(C);
            l.show();
        }
        MyPermissions = new ArrayList<Permission>();
        Loading finalL = l;
        StringRequest req = new StringRequest(Request.Method.POST, getPermissionsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (C != null) {
                    finalL.close();
                }
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i=0;i<arr.length();i++) {
                        JSONObject row = arr.getJSONObject(i);
                        MyPermissions.add(new Permission(row.getInt("id"),row.getString("PermissionEnName"),row.getString("PermissionArName"),row.getString("Department"))); //row.getInt("manageAuthorizedJobTitles"),row.getInt("manageOrdersAuthorities"),row.getInt("exitRequest"),row.getInt("vacations"),row.getInt("backToWork"),row.getInt("resignation"),row.getInt("advancePayment"),row.getInt("requestCustody"),row.getInt("requestVacationSalary"),row.getInt("manageEmployeesContracts"),row.getInt("manageEmployeesPassports"),row.getInt("manageEmployeesIDs"),row.getInt("sendNewAd"),row.getInt("sendNewAdWithImage"),row.getInt("salaryReport"),row.getInt("myOrders"),row.getInt("myApprovals"),row.getInt("checkEmployeesAttendance"),row.getInt("ratingEmployees"),row.getInt("addClientVisitReport"),row.getInt("myVisitReports"),row.getInt("myStaffVisitReports"),row.getInt("addClient"),row.getInt("myClients"),row.getInt("addProjectContract"),row.getInt("myProjectContracts"),row.getInt("addMaintenanceOrder"),row.getInt("myMaintenanceOrders"),row.getInt("addSiteVisitOrder"),row.getInt("mySiteVisitOrders"),row.getInt("siteVisitOrders"),row.getInt("addLocalPurchaseOrder")
                    }
                    Log.d("permissionsResp",MyPermissions.size()+" ");
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("loginProblem",e.toString());
                    callback.onFailed(e.getMessage());
                }
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //l.close();
                if (C != null) {
                    finalL.close();
                }
                callback.onFailed(error.toString());
            }
        });

        Volley.newRequestQueue(MyApp.app).add(req);
    }

    public void getMyPermissions (int id,int JobNum,VollyCallback callback , Context C) {
        Loading l = null;
        if (C != null) {
            l = new Loading(C);
            l.show();
        }
        Loading finalL = l;
        StringRequest req = new StringRequest(Request.Method.POST, getMyPermissionsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (C != null ) {
                    finalL.close();
                }
                //Log.d("loginProblem",response);
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject row = arr.getJSONObject(0);
                    for (int i=0;i<MyPermissions.size();i++) {
                        MyPermissions.get(i).setValue(row.getInt("p"+(i+1)));  // = new Permission(row.getInt("manageAuthorizedJobTitles"),row.getInt("manageOrdersAuthorities"),row.getInt("exitRequest"),row.getInt("vacations"),row.getInt("backToWork"),row.getInt("resignation"),row.getInt("advancePayment"),row.getInt("requestCustody"),row.getInt("requestVacationSalary"),row.getInt("manageEmployeesContracts"),row.getInt("manageEmployeesPassports"),row.getInt("manageEmployeesIDs"),row.getInt("sendNewAd"),row.getInt("sendNewAdWithImage"),row.getInt("salaryReport"),row.getInt("myOrders"),row.getInt("myApprovals"),row.getInt("checkEmployeesAttendance"),row.getInt("ratingEmployees"),row.getInt("addClientVisitReport"),row.getInt("myVisitReports"),row.getInt("myStaffVisitReports"),row.getInt("addClient"),row.getInt("myClients"),row.getInt("addProjectContract"),row.getInt("myProjectContracts"),row.getInt("addMaintenanceOrder"),row.getInt("myMaintenanceOrders"),row.getInt("addSiteVisitOrder"),row.getInt("mySiteVisitOrders"),row.getInt("siteVisitOrders"),row.getInt("addLocalPurchaseOrder"));
                        MyPermissions.get(i).setResult();
                    }
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("loginProblem",e.toString());
                    callback.onFailed(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //l.close();
                if (C != null && finalL != null ) {
                    finalL.close();
                }
                callback.onFailed(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("ID" , String.valueOf(id));
                par.put("JobNumber" , String.valueOf(JobNum));
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(req);
    }

    void getEmps(Context c,VollyCallback callback) {
        MyStaff = new ArrayList<>();
        EMPS = new ArrayList<>();
        MyApp.EMPS.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getEmpsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (!response.equals("0")) {
                    List<Object> lis = JsonToObject.translate(response,USER.class,c);
                    if (lis.size()>0) {
                        Log.d("EmpsNumber" , lis.size()+"");
                        for (Object o : lis) {
                            USER r = (USER) o;
                            Log.d("AllEmps" , r.JobTitle);
                            Log.d("directManager" ,r.JobNumber+" "+r.FirstName ) ;
                            EMPS.add(r);
                        }
                        MyApp.EMPS = EMPS ;
                        if (MyApp.db.getUser() != null) {
                            for (USER emp : EMPS) {
                                if (emp.DirectManager == MyApp.db.getUser().JobNumber) {
                                    MyApp.ManagerStatus = true;
                                    isDirectManager = true ;
                                    MyStaff.add(emp);
                                }
                            }
                            for (USER emp : EMPS) {
                                if (emp.DepartmentManager == MyApp.db.getUser().JobNumber) {
                                    MyApp.ManagerStatus = true;
                                    isDepartmentManager = true ;
                                    if (searchUserByJobNumber(MyStaff,emp.JobNumber) == null && emp.JobNumber != MyApp.db.getUser().JobNumber) {
                                        MyStaff.add(emp);
                                    }
                                }
                            }
                        }
                    }
                    callback.onSuccess("Done");
                }
                else {
                    callback.onFailed("No Emps");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed(error.toString());
            }
        }){

        };
        Volley.newRequestQueue(c).add(request);
    }

    void getHrOrderTypes(Context c , VollyCallback callback) {

        StringRequest request = new StringRequest(Request.Method.POST, getHrOrdersTypes, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null ) {
                    if (response.equals("0")) {
                        callback.onFailed("No Orders Registered");
                    }
                    else {
                        Log.d("hrOrdersResponse" , response );
                        List<Object> list = JsonToObject.translate(response, HR_ORDER_TYPE.class,c);
                        Types = new ArrayList<HR_ORDER_TYPE>();
                        for(int i=0 ; i<list.size() ; i++) {
                            HR_ORDER_TYPE x = (HR_ORDER_TYPE) list.get(i) ;
                            Types.add(x);
                        }
                        MyApp.Types = Types ;
                        callback.onSuccess(response);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed(error.toString());
            }
        });
        Volley.newRequestQueue(c).add(request);
    }

    void getJobTitles(Context c , VollyCallback callback) {
        JobTitles = new ArrayList<JobTitle>();
        StringRequest request = new StringRequest(Request.Method.POST, getJobTitlesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null ) {
                    List<Object> list = JsonToObject.translate(response, com.syrsoft.ratcoms.HRActivities.JobTitle.class,c);
                    JobTitles.clear();
                    for (int i = 0; i< list.size();i++) {
                        JobTitle j = (JobTitle) list.get(i) ;
                        JobTitles.add(j);
                    }
                    MyApp.JobTitles = JobTitles ;
                    callback.onSuccess(response);
                }
                else {
                    callback.onFailed("no Jobtitles");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("jobtitles",error.getMessage());
                callback.onFailed(error.toString());
            }
        });
        Volley.newRequestQueue(c).add(request);
    }

    // get Resignations Data _______________________________________

    void getResignationAuthEmps(VollyCallback callback) {
        ResignationsAuthsJobtitles = new ArrayList<JobTitle>();
        ResignationsAuthUsers = new ArrayList<USER>();
        MyApp.ResignationsAuthsJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("Resignation")) {
                if (Types.get(i).Auth1 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    ResignationsAuthsJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (ResignationsAuthsJobtitles.size() > 0 ) {
            MyApp.ResignationsAuthsJobtitles = ResignationsAuthsJobtitles ;
            for (int p=0; p<ResignationsAuthsJobtitles.size();p++) {
                if (ResignationsAuthsJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            ResignationsAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                ResignationsAuthUsers.add(direct);
                            }
                        }
                    }
                }
                else if (ResignationsAuthsJobtitles.get(p).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
                    if (departmentM != null) {
                        if (departmentM.VacationStatus == 0) {
                            ResignationsAuthUsers.add(departmentM);
                        }
                        else {
                            departmentM = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (departmentM != null) {
                                ResignationsAuthUsers.add(departmentM);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, ResignationsAuthsJobtitles.get(p).JobTitle) ;
                    if (user != null) {
                        if (user.VacationStatus ==0) {
                            ResignationsAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (user != null) {
                                ResignationsAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.ResignationsAuthUsers = ResignationsAuthUsers ;
        }
        callback.onSuccess("done");
    }

    static void getResignationsForApprove(VollyCallback callback) {
        resignationsList = new ArrayList<RESIGNATION_CLASS>();
        StringRequest request = new StringRequest(Request.Method.POST, getResignationsForApprovalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed("error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("status",String.valueOf(0) );
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFResignations(VollyCallback callback) {
        ResignationOrdersAuthUsers = new ArrayList<List<USER>>();
        for ( int i=0; i<resignationsList.size();i++) {
            List<USER> ll = new ArrayList<USER>();
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
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                toRemoveRows[i]=1;
//                try {
//                    resignationsList.remove(i);
//                    ResignationOrdersAuthUsers.remove(i);
//                }
//                catch (Exception e){}
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

        }
        MyApp.ResignationOrdersAuthUsers = ResignationOrdersAuthUsers ;
        MyApp.HRCounter = MyApp.HRCounter+ resignationsList.size() ;
        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + resignationsList.size() ;
        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (HR.isRunning) {
            HR.setHRCounters();
        }
        callback.onSuccess("done");
    }

    void getResignationsData(Context c ,VollyCallback callback) {
        getResignationAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                Log.d("newProsedur","resignations 1 done");
                getResignationsForApprove(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        Log.d("newProsedur","resignations 2 done");
                        setTheListsOfAuthUsersOFResignations(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                Log.d("newProsedur","resignations 3 done");
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    // get Vacations Date __________________________________________

    void getVacationAuthEmps(VollyCallback callback) {
        MyApp.VacationsAuthJobtitles.clear();
        VacationsAuthJobtitles = new ArrayList<JobTitle>();
        VacationsAuthUsers = new ArrayList<USER>();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("Vacations")) {
                if (Types.get(i).Auth1 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    VacationsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (VacationsAuthJobtitles.size() > 0 ) {
            MyApp.VacationsAuthJobtitles = VacationsAuthJobtitles ;
            for (int p=0; p<VacationsAuthJobtitles.size();p++) {
                if (VacationsAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.db.getUser().DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            VacationsAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                VacationsAuthUsers.add(direct);
                            }
                        }
                    }
                }
                else if (VacationsAuthJobtitles.get(p).JobTitle.equals("Department Manager")) {
                    USER department = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
                    if (department != null) {
                        if (department.VacationStatus == 0) {
                            VacationsAuthUsers.add(department);
                        }
                        else {
                            department = USER.searchUserByJobNumber(MyApp.EMPS,department.VacationAlternative);
                            if (department != null) {
                                VacationsAuthUsers.add(department);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, VacationsAuthJobtitles.get(p).JobTitle) ;
                    if (user != null) {
                        if (user.VacationStatus == 0) {
                            VacationsAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (user != null) {
                                VacationsAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.VacationsAuthUsers = VacationsAuthUsers ;

        }
        callback.onSuccess("done");
    }

    static void getVacationsForApprove(VollyCallback callback){
        vacationslist = new ArrayList<VACATION_CLASS>();
        StringRequest request = new StringRequest(Request.Method.POST, getVacationsForApprovalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed("error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("status",String.valueOf(0) );
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFVacations(VollyCallback callback) {
        VacationOrdersAuthUsers = new ArrayList<List<USER>>();
        for ( int i=0; i<vacationslist.size();i++) {
            List<USER> ll = new ArrayList<USER>();
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
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
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
        catch (Exception e){}
        MyApp.VacationOrdersAuthUsers = VacationOrdersAuthUsers ;
        MyApp.HRCounter = MyApp.HRCounter + vacationslist.size() ;
        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + vacationslist.size() ;

        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (HR.isRunning) {
            HR.setHRCounters();
        }
        callback.onSuccess("done");
    }

    void getVacationsData(Context c ,VollyCallback callback) {
        getVacationAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                getVacationsForApprove(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        setTheListsOfAuthUsersOFVacations(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    // get Backs Data _____________________________________________

    void getBacksAuthEmps(VollyCallback callback) {
        BacksAuthJobtitles = new ArrayList<JobTitle>();
        BacksAuthUsers = new ArrayList<USER>();
        MyApp.BacksAuthJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("BackToWork")) {
                if (Types.get(i).Auth1 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    BacksAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (BacksAuthJobtitles.size() > 0 ) {
            MyApp.BacksAuthJobtitles = BacksAuthJobtitles ;
            for (int p=0; p<BacksAuthJobtitles.size();p++) {
                if (BacksAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            BacksAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                BacksAuthUsers.add(direct);
                            }
                        }
                    }
                }
                else if (BacksAuthJobtitles.get(p).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
                    if (departmentM != null) {
                        if (departmentM.VacationStatus == 0) {
                            BacksAuthUsers.add(departmentM);
                        }
                        else {
                            departmentM = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (departmentM != null) {
                                BacksAuthUsers.add(departmentM);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, BacksAuthJobtitles.get(p).JobTitle) ;
                    if (user != null) {
                        if (user.VacationStatus == 0) {
                            BacksAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (user != null) {
                                BacksAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.BacksAuthUsers = BacksAuthUsers ;
        }
        callback.onSuccess("done");
    }

    static void getBacksForApprove(VollyCallback callback){
        backList = new ArrayList<BACKTOWORK_CLASS>();
        StringRequest request = new StringRequest(Request.Method.POST, getBacksForApprovalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed("error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("status",String.valueOf(0) );
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFBacks(VollyCallback callback) {
        BacksOrdersAuthUsers = new ArrayList<List<USER>>();
        for ( int i=0; i<backList.size();i++) {
            List<USER> ll = new ArrayList<USER>();
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
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                //BacksOrdersAuthUsers.remove(i);
                //backList.remove(i);
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
        catch (Exception e){}
        MyApp.BacksOrdersAuthUsers = BacksOrdersAuthUsers ;
        MyApp.HRCounter = MyApp.HRCounter+ backList.size() ;
        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + backList.size();

        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (HR.isRunning) {
            HR.setHRCounters();
        }
        callback.onSuccess("done");
    }

    void getBacksDate(Context c ,VollyCallback callback) {
        getBacksAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                getBacksForApprove(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        setTheListsOfAuthUsersOFBacks(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    // get Advances Data ___________________________________________

    void getAdvancePaymentsAuthEmps(VollyCallback callback) {
        AdvancePaymentsAuthJobtitles = new ArrayList<JobTitle>();
        AdvancePaymentaAuthUsers = new ArrayList<USER>();
        MyApp.AdvancePaymentsAuthJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("AdvancePayment")) {
                if (Types.get(i).Auth1 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    AdvancePaymentsAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (AdvancePaymentsAuthJobtitles.size() > 0 ) {
            MyApp.AdvancePaymentsAuthJobtitles = AdvancePaymentsAuthJobtitles ;
            for (int p=0; p<AdvancePaymentsAuthJobtitles.size();p++) {
                if (AdvancePaymentsAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.db.getUser().DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            AdvancePaymentaAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                AdvancePaymentaAuthUsers.add(direct);
                            }
                        }
                    }
                }
                else if (AdvancePaymentsAuthJobtitles.get(p).JobTitle.equals("Department Manager")) {
                    USER department = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.db.getUser().DepartmentManager);
                    if (department != null) {
                        if (department.VacationStatus == 0) {
                            AdvancePaymentaAuthUsers.add(department);
                        }
                        else {
                            department = USER.searchUserByJobNumber(MyApp.EMPS,department.VacationAlternative);
                            if (department != null ) {
                                AdvancePaymentaAuthUsers.add(department);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, AdvancePaymentsAuthJobtitles.get(p).JobTitle);
                    if (user != null) {
                        if (user.VacationStatus ==0) {
                            AdvancePaymentaAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (user != null) {
                                AdvancePaymentaAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.AdvancePaymentaAuthUsers = AdvancePaymentaAuthUsers ;
        }
        callback.onSuccess("done");
    }

    static void getAdvanceForApprove(VollyCallback callback){
        advanceList = new ArrayList<ADVANCEPAYMENT_CLASS>();
        StringRequest request = new StringRequest(Request.Method.POST, getAdvanceApprovalsUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed("error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> par = new HashMap<String, String>();
                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("status",String.valueOf(0) );
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFAdvance(VollyCallback callback) {
        AdvancesOrdersAuthUsers = new ArrayList<List<USER>>();
        for ( int i=0; i<advanceList.size();i++) {
            List<USER> ll = new ArrayList<USER>();
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
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                //AdvancesOrdersAuthUsers.remove(i);
                // advanceList.remove(i);
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
        catch (Exception e){}
        MyApp.AdvancesOrdersAuthUsers = AdvancesOrdersAuthUsers ;
        MyApp.HRCounter = MyApp.HRCounter + advanceList.size();
        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + advanceList.size();
        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (HR.isRunning) {
            HR.setHRCounters();
        }
        callback.onSuccess("done");
    }

    void getAdvancePaymentsData(Context c ,VollyCallback callback) {
        getAdvancePaymentsAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                Log.d("newProsedur","advance 1 done");
                getAdvanceForApprove(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        Log.d("newProsedur","advance 2 done");
                        setTheListsOfAuthUsersOFAdvance(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                Log.d("newProsedur","advance 3 done");
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    // get Vsalary Data ________________________________________________

    void getVacationSalariesAuthEmps(VollyCallback callback) {
        VacationSalaryAuthJobtitles = new ArrayList<JobTitle>();
        VacationSalaryAuthUsers = new ArrayList<USER>();
        MyApp.VacationSalaryAuthJobtitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("VacationSalary")) {
                if (Types.get(i).Auth1 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    VacationSalaryAuthJobtitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (VacationSalaryAuthJobtitles.size() > 0 ) {
            MyApp.VacationSalaryAuthJobtitles = VacationSalaryAuthJobtitles;
            for (int p=0; p<VacationSalaryAuthJobtitles.size();p++) {
                if (VacationSalaryAuthJobtitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            VacationSalaryAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                VacationSalaryAuthUsers.add(direct);
                            }
                        }
                    }
                }
                else if (VacationSalaryAuthJobtitles.get(p).JobTitle.equals("Department Manager")) {
                    USER department = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
                    if (department != null) {
                        if (department.VacationStatus == 0) {
                            VacationSalaryAuthUsers.add(department);
                        }
                        else {
                            department = USER.searchUserByJobNumber(MyApp.EMPS,department.VacationAlternative);
                            if (department != null) {
                                VacationSalaryAuthUsers.add(department);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, VacationSalaryAuthJobtitles.get(p).JobTitle) ;
                    if (user != null) {
                        if (user.VacationStatus == 0) {
                            VacationSalaryAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (user != null) {
                                VacationSalaryAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.VacationSalaryAuthUsers = VacationSalaryAuthUsers ;
        }
        callback.onSuccess("done");
    }

    static void getVacationSalaryRequests(VollyCallback callback){
        vacationSalaryList = new ArrayList<VACATIONSALARY_CLASS>();
        StringRequest request = new StringRequest(Request.Method.POST, getVacationSalaryRequestsForApprove , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed("error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> par = new HashMap<String, String>();
                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("status",String.valueOf(0) );
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFVSalary(VollyCallback callback) {
        VacationSalaryOrdersAuthUsers = new ArrayList<List<USER>>();
        for ( int i=0; i<vacationSalaryList.size();i++) {
            List<USER> ll = new ArrayList<USER>();
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
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
                    s = true ;
                }
            }
            if (!s) {
                rowaToRemove[i] = 1 ;
//                try {
//                    VacationSalaryOrdersAuthUsers.remove(i);
//                    vacationSalaryList.remove(i);
//                }
//                catch (Exception e ) {
//
//                }
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
        catch (Exception e){}
        MyApp.VacationSalaryOrdersAuthUsers = VacationSalaryOrdersAuthUsers ;
        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + vacationSalaryList.size();
        MyApp.HRCounter = MyApp.HRCounter + vacationSalaryList.size();
        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (HR.isRunning) {
            HR.setHRCounters();
        }
        callback.onSuccess("done");
    }

    void getVSData (Context c ,VollyCallback callback) {
        getVacationSalariesAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                getVacationSalaryRequests(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        setTheListsOfAuthUsersOFVSalary(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    // get Custody Data ____________________________________________

    void getRequestCustodyAuthEmps(VollyCallback callback) {
        RequestCustodyAuthsJobTitles = new ArrayList<JobTitle>();
        CustodyAuthUsers = new ArrayList<USER>();
        MyApp.RequestCustodyAuthsJobTitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("RequestCustody")) {
                if (Types.get(i).Auth1 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    RequestCustodyAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (RequestCustodyAuthsJobTitles.size() > 0 ) {
            MyApp.RequestCustodyAuthsJobTitles = RequestCustodyAuthsJobTitles;
            for (int p=0; p<RequestCustodyAuthsJobTitles.size();p++) {
                if (RequestCustodyAuthsJobTitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            CustodyAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                CustodyAuthUsers.add(direct);
                            }
                        }

                    }
                }
                else if (RequestCustodyAuthsJobTitles.get(p).JobTitle.equals("Department Manager")) {
                    USER department = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
                    if (department != null) {
                        if (department.VacationStatus == 0) {
                            CustodyAuthUsers.add(department);
                        }
                        else {
                            department = USER.searchUserByJobNumber(MyApp.EMPS,department.VacationAlternative);
                            if (department != null) {
                                CustodyAuthUsers.add(department);
                            }
                        }

                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, RequestCustodyAuthsJobTitles.get(p).JobTitle);
                    if (user != null) {
                        if (user.VacationStatus == 0) {
                            CustodyAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if ( user!= null) {
                                CustodyAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.CustodyAuthUsers = CustodyAuthUsers ;
        }
        callback.onSuccess("done");
    }

    static void getCustodyRequests(VollyCallback callback){
        custodyRequestList = new ArrayList<CUSTODY_REQUEST_CLASS>();
        StringRequest request = new StringRequest(Request.Method.POST, getCustodyForApprovalURL , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed("error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> par = new HashMap<String, String>();
                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("status",String.valueOf(0) );
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFCustody(VollyCallback callback) {
        CustodyOrdersAuthUsers = new ArrayList<List<USER>>();
        for ( int i=0; i<custodyRequestList.size();i++) {
            List<USER> ll = new ArrayList<USER>();
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
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
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
                    custodyRequestList.remove(i-removed);
                    CustodyOrdersAuthUsers.remove(i-removed);
                    removed++;
                }
            }
        }
        catch (Exception e){
        }
        MyApp.CustodyOrdersAuthUsers = CustodyOrdersAuthUsers ;
        MyApp.HRCounter = MyApp.HRCounter + custodyRequestList.size();
        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter +custodyRequestList.size();

        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (HR.isRunning) {
            HR.setHRCounters();
        }
        callback.onSuccess("done");
    }

    void getCustodyData(Context c ,VollyCallback callback) {
        getRequestCustodyAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                getCustodyRequests(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        setTheListsOfAuthUsersOFCustody(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    // get Exit Data _____________________________________________

    void getRequestExitAuthEmps(VollyCallback callback) {
        ExitAuthsJobTitles = new ArrayList<JobTitle>();
        ExitAuthUsers = new ArrayList<USER>();
        MyApp.ExitAuthsJobTitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {

            if (Types.get(i).HROrderName.equals("RequestExit")) {
                if (Types.get(i).Auth1 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    ExitAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (ExitAuthsJobTitles.size() > 0 ) {
            MyApp.ExitAuthsJobTitles = ExitAuthsJobTitles;
            for (int p=0; p<ExitAuthsJobTitles.size();p++) {
                if (ExitAuthsJobTitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            ExitAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                ExitAuthUsers.add(direct);
                            }
                        }
                    }
                }
                else if (ExitAuthsJobTitles.get(p).JobTitle.equals("Department Manager")) {
                    USER department = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
                    if (department != null) {
                        if (department.VacationStatus == 0) {
                            ExitAuthUsers.add(department);
                        }
                        else {
                            department = USER.searchUserByJobNumber(MyApp.EMPS,department.VacationAlternative);
                            if (department != null) {
                                ExitAuthUsers.add(department);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, ExitAuthsJobTitles.get(p).JobTitle);
                    if (user != null) {
                        if (user.VacationStatus == 0) {
                            ExitAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (user != null) {
                                ExitAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.ExitAuthUsers = ExitAuthUsers ;
        }
        callback.onSuccess("done");
    }

    static void getExitRequests(VollyCallback callback) {

        exiteList = new ArrayList<EXIT_REQUEST_CLASS>();
        StringRequest request = new StringRequest(Request.Method.POST, getExitRequestsForApprovUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("exitAResponse" , error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> par = new HashMap<String, String>();
                //par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                //par.put("id" , String.valueOf(MyApp.db.getUser().id));
                //par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("status",String.valueOf(0) );
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFExit(VollyCallback callback) {
        ExitOrdersAuthUsers = new ArrayList<List<USER>>();
        for ( int i=0; i<exiteList.size();i++) {
            List<USER> ll = new ArrayList<USER>();
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
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
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
                    exiteList.remove(i-removed);
                    ExitOrdersAuthUsers.remove(i-removed);
                    removed++;
                }
            }
        }
        catch (Exception e){
        }
        MyApp.ExitOrdersAuthUsers = ExitOrdersAuthUsers ;
        MyApp.HRCounter = MyApp.HRCounter + exiteList.size();
        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + exiteList.size();

        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (HR.isRunning) {
            HR.setHRCounters();
        }
        callback.onSuccess("done");
    }

    void getExitData(Context c ,VollyCallback callback) {
        getRequestExitAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                getExitRequests(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        setTheListsOfAuthUsersOFExit(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    // get Bonus Data ____________________________________________

    void getRequestBonusAuthEmps(VollyCallback callback) {
        BonusAuthsJobTitles = new ArrayList<>();
        BonusAuthUsers = new ArrayList<>();
        MyApp.BonusAuthsJobTitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("RequestBonus")) {
                if (Types.get(i).Auth1 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    BonusAuthsJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (BonusAuthsJobTitles.size() > 0 ) {
            MyApp.BonusAuthsJobTitles = BonusAuthsJobTitles;
            Log.d("BonusResponse" , BonusAuthsJobTitles.size()+" jobTitles");
            for (int p=0; p<BonusAuthsJobTitles.size();p++) {
                if (BonusAuthsJobTitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            BonusAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                BonusAuthUsers.add(direct);
                            }
                        }
                    }
                }
                if (BonusAuthsJobTitles.get(p).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
                    if (departmentM != null) {
                        if (departmentM.VacationStatus == 0) {
                            BonusAuthUsers.add(departmentM);
                        }
                        else {
                            departmentM = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (departmentM != null) {
                                BonusAuthUsers.add(departmentM);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, BonusAuthsJobTitles.get(p).JobTitle);
                    if (user != null) {
                        if (user.VacationStatus == 0 ) {
                            BonusAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (user != null) {
                                BonusAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.BonusAuthUsers = BonusAuthUsers ;
            Log.d("BonusResponse" , BonusAuthUsers.size()+" authUsers");
        }
        callback.onSuccess("done");
    }

    static void getBonusRequests(VollyCallback callback) {
        bonusList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, getBonusRequestsForApprovUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("BonusResponse" , error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> par = new HashMap<String, String>();
                par.put("status",String.valueOf(0) );
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
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
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
        catch (Exception e){
        }
        MyApp.BonusOrdersAuthUsers = BonusOrdersAuthUsers ;
        MyApp.HRCounter = MyApp.HRCounter + bonusList.size();
        MyApp.MYApprovalsCounter = MyApp.MYApprovalsCounter + bonusList.size();

        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (HR.isRunning) {
            HR.setHRCounters();
        }
        callback.onSuccess("done");
    }

    void getBonusData(Context c ,VollyCallback callback) {
        getRequestBonusAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                getBonusRequests(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        setTheListsOfAuthUsersOFBonus(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    // get Purchase Orders Data ____________________________________________

    void getPurchaseOrdersAuthEmps(VollyCallback callback) {
        PurchaseOrdersJobTitles = new ArrayList<>();
        PurchaseAuthUsers = new ArrayList<>();
        MyApp.PurchaseOrdersJobTitles.clear();
        for (int i=0; i<Types.size() ; i++ ) {
            if (Types.get(i).HROrderName.equals("Local Purchase Order")) {
                if (Types.get(i).Auth1 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth1));
                }
                if (Types.get(i).Auth2 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth2));
                }
                if (Types.get(i).Auth3 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth3));
                }
                if (Types.get(i).Auth4 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth4));
                }
                if (Types.get(i).Auth5 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth5));
                }
                if (Types.get(i).Auth6 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth6));
                }
                if (Types.get(i).Auth7 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth7));
                }
                if (Types.get(i).Auth8 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth8));
                }
                if (Types.get(i).Auth9 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth9));
                }
                if (Types.get(i).Auth10 != 0 ) {
                    PurchaseOrdersJobTitles.add(com.syrsoft.ratcoms.HRActivities.JobTitle.searchJobTitleById(JobTitles,Types.get(i).Auth10));
                }
            }
        }
        if (PurchaseOrdersJobTitles.size() > 0 ) {
            MyApp.PurchaseOrdersJobTitles = PurchaseOrdersJobTitles;
            Log.d("PurchaseResponse" , PurchaseOrdersJobTitles.size()+" jobTitles");
            for (int p=0; p<PurchaseOrdersJobTitles.size();p++) {
                if (PurchaseOrdersJobTitles.get(p).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DirectManager);
                    if (direct != null) {
                        if (direct.VacationStatus == 0) {
                            PurchaseAuthUsers.add(direct);
                        }
                        else {
                            direct = USER.searchUserByJobNumber(MyApp.EMPS,direct.VacationAlternative);
                            if (direct != null) {
                                PurchaseAuthUsers.add(direct);
                            }
                        }
                    }
                }
                if (PurchaseOrdersJobTitles.get(p).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
                    if (departmentM != null) {
                        if (departmentM.VacationStatus == 0) {
                            PurchaseAuthUsers.add(departmentM);
                        }
                        else {
                            departmentM = USER.searchUserByJobNumber(MyApp.EMPS,departmentM.VacationAlternative);
                            if (departmentM != null) {
                                PurchaseAuthUsers.add(departmentM);
                            }
                        }
                    }
                }
                else {
                    USER user = USER.searchUserByJobtitle(MyApp.EMPS, PurchaseOrdersJobTitles.get(p).JobTitle);
                    if (user != null) {
                        if (user.VacationStatus == 0 ) {
                            PurchaseAuthUsers.add(user);
                        }
                        else {
                            user = USER.searchUserByJobNumber(MyApp.EMPS,user.VacationAlternative);
                            if (user != null) {
                                PurchaseAuthUsers.add(user);
                            }
                        }
                    }
                }
            }
            MyApp.PurchaseAuthUsers = PurchaseAuthUsers ;
            Log.d("PurchaseResponse" , PurchaseAuthUsers.size()+" authUsers");
        }
        callback.onSuccess("done");
    }

    static void getPurchaseRequests(VollyCallback callback) {
        purchaseList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST,getPurchaseOrdersForApprovUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PurchaseResponse" , response);
                if (!response.equals("0")){
                    List<Object> list = JsonToObject.translate(response, LOCAL_PURCHASE_ORDER.class,MyApp.app);
                    for (int i=0;i<list.size();i++){
                        LOCAL_PURCHASE_ORDER r =(LOCAL_PURCHASE_ORDER) list.get(i);
                        purchaseList.add(r);
                    }
                    Log.d("PurchaseResponse" , purchaseList.size()+" ");
                    Collections.reverse(purchaseList);
                    callback.onSuccess("done");
                }
                else {
                    callback.onSuccess("done");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PurchaseResponse" , error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> par = new HashMap<String, String>();
                par.put("status",String.valueOf(0) );
                return par;
            }
        };
        Volley.newRequestQueue(MyApp.app).add(request);
    }

    static void setTheListsOfAuthUsersOFPurchase(VollyCallback callback) {
        Log.d("PurchaseResponse" , "purchaseList "+purchaseList.size());
        PurchaseOrdersAuthUsers = new ArrayList<>();
        for ( int i=0; i<purchaseList.size();i++) {
            List<USER> ll = new ArrayList<>();
            for (int j = 0; j < MyApp.PurchaseOrdersJobTitles.size(); j++) {
                if (MyApp.PurchaseOrdersJobTitles.get(j).JobTitle.equals("Direct Manager")) {
                    USER direct = USER.searchUserByJobNumber(MyApp.EMPS, USER.searchUserByJobNumber(MyApp.EMPS,purchaseList.get(i).Supmitter).DirectManager);
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
                else if (MyApp.PurchaseOrdersJobTitles.get(j).JobTitle.equals("Department Manager")) {
                    USER departmentM = USER.searchUserByJobNumber(MyApp.EMPS, USER.searchUserByJobNumber(MyApp.EMPS,purchaseList.get(i).Supmitter).DepartmentManager);
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
                    USER user =  USER.searchUserByJobtitle(MyApp.EMPS,MyApp.PurchaseOrdersJobTitles.get(j).JobTitle);
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
            PurchaseOrdersAuthUsers.add(ll);
        }
        Log.d("PurchaseResponse" , "usersLists "+PurchaseOrdersAuthUsers.size());
        int[] rowsToRemove = new int[purchaseList.size()];
        for (int i =0 ; i < PurchaseOrdersAuthUsers.size();i++) {
            boolean s = false ;
            for (USER u : PurchaseOrdersAuthUsers.get(i)) {
                if (u.JobNumber == MyApp.db.getUser().JobNumber) {
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
                    purchaseList.remove(i-removed);
                    PurchaseOrdersAuthUsers.remove(i-removed);
                    removed++;
                }
            }
        }
        catch (Exception e){
        }
        MyApp.PurchaseOrdersAuthUsers = PurchaseOrdersAuthUsers ;
        MyApp.ProjectsCounters[2] = purchaseList.size();

        if (MainPage.isRunning ) {
            MainPage.setCounters();
        }
        if (Projects_Activity.isRunning) {
            Projects_Activity.setCounters();
        }
        callback.onSuccess("done");
    }

    void getPurchaseData(Context c ,VollyCallback callback) {
        getPurchaseOrdersAuthEmps(new VollyCallback() {
            @Override
            public void onSuccess(String res) {
                getPurchaseRequests(new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        setTheListsOfAuthUsersOFPurchase(new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                callback.onSuccess("done");
                            }

                            @Override
                            public void onFailed(String error) {
                                callback.onFailed("error");
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        callback.onFailed("error");
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                callback.onFailed("error");
            }
        });
    }

    //___________________________________________________________

    public void getEmployeesData(Context c,VollyCallback callback) {
        MyApp.HRCounter = 0 ;
        MyApp.MYApprovalsCounter = 0 ;
        getEmps(c, new VollyCallback() {
            @Override
            public void onSuccess(String Res) {
                Log.d("newProsedur" , "get Emps Done");
                getHrOrderTypes(c, new VollyCallback() {
                    @Override
                    public void onSuccess(String res) {
                        Log.d("newProsedur" , "get orders Done");
                        getJobTitles(c, new VollyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                Log.d("newProsedur" , "get jobtitles Done");
                                getResignationsData(c,new VollyCallback() {
                                    @Override
                                    public void onSuccess(String res) {
                                        Log.d("newProsedur" , "get resignations Done");
                                        getVacationsData(c,new VollyCallback() {
                                            @Override
                                            public void onSuccess(String res) {
                                                Log.d("newProsedur" , "get vacations Done");
                                                getAdvancePaymentsData(c,new VollyCallback() {
                                                    @Override
                                                    public void onSuccess(String res) {
                                                        Log.d("newProsedur" , "get advances Done");
                                                        getBacksDate(c,new VollyCallback() {
                                                            @Override
                                                            public void onSuccess(String res) {
                                                                Log.d("newProsedur" , "get backs Done");
                                                                getVSData(c,new VollyCallback() {
                                                                    @Override
                                                                    public void onSuccess(String res) {
                                                                        Log.d("newProsedur" , "get vs Done");
                                                                        getCustodyData(c,new VollyCallback() {
                                                                            @Override
                                                                            public void onSuccess(String res) {
                                                                                Log.d("newProsedur" , "get custody Done");
                                                                                getExitData(c,new VollyCallback() {
                                                                                    @Override
                                                                                    public void onSuccess(String res) {
                                                                                        Log.d("newProsedur" , "get exites Done");
                                                                                        getBonusData(c, new VollyCallback() {
                                                                                            @Override
                                                                                            public void onSuccess(String s) {
                                                                                                Log.d("newProsedur" , "get bonus Done");
                                                                                                getPurchaseData(c, new VollyCallback() {
                                                                                                    @Override
                                                                                                    public void onSuccess(String s) {
                                                                                                        Log.d("newProsedur" , "get Purchase Done");
                                                                                                        callback.onSuccess(res);
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onFailed(String error) {

                                                                                                    }
                                                                                                });

                                                                                            }

                                                                                            @Override
                                                                                            public void onFailed(String error) {
                                                                                                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                                                                                alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                                                                                alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting exit");
                                                                                                alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                                        Activity x = (Activity) MyApp.app.getApplicationContext();
                                                                                                        if (x != null) {
                                                                                                            x.finish();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                                alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                                        Login.redirectActivity();
                                                                                                    }
                                                                                                });
                                                                                                alert.create().show();
                                                                                            }
                                                                                        });

                                                                                    }

                                                                                    @Override
                                                                                    public void onFailed(String error) {
                                                                                        AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                                                                        alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                                                                        alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting exit");
                                                                                        alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                Activity x = (Activity) MyApp.app.getApplicationContext();
                                                                                                if (x != null) {
                                                                                                    x.finish();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                        alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                Login.redirectActivity();
                                                                                            }
                                                                                        });
                                                                                        alert.create().show();
                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onFailed(String error) {
                                                                                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                                                                alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                                                                alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting custody");
                                                                                alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        Activity x = (Activity) MyApp.app.getApplicationContext();
                                                                                        if (x != null) {
                                                                                            x.finish();
                                                                                        }
                                                                                    }
                                                                                });
                                                                                alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        Login.redirectActivity();
                                                                                    }
                                                                                });
                                                                                alert.create().show();
                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onFailed(String error) {
                                                                        AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                                                        alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                                                        alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting vacation s");
                                                                        alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                Activity x = (Activity) MyApp.app.getApplicationContext();
                                                                                if (x != null) {
                                                                                    x.finish();
                                                                                }
                                                                            }
                                                                        });
                                                                        alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                Login.redirectActivity();
                                                                            }
                                                                        });
                                                                        alert.create().show();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onFailed(String error) {
                                                                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                                                alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                                                alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting backs");
                                                                alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        Activity x = (Activity) MyApp.app.getApplicationContext();
                                                                        if (x != null) {
                                                                            x.finish();
                                                                        }
                                                                    }
                                                                });
                                                                alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        Login.redirectActivity();
                                                                    }
                                                                });
                                                                alert.create().show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onFailed(String error) {
                                                        AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                                        alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                                        alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting advance");
                                                        alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Activity x = (Activity) MyApp.app.getApplicationContext();
                                                                if (x != null) {
                                                                    x.finish();
                                                                }
                                                            }
                                                        });
                                                        alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Login.redirectActivity();
                                                            }
                                                        });
                                                        alert.create().show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailed(String error) {
                                                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                                alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                                alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting vacations");
                                                alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Activity x = (Activity) MyApp.app.getApplicationContext();
                                                        if (x != null) {
                                                            x.finish();
                                                        }
                                                    }
                                                });
                                                alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Login.redirectActivity();
                                                    }
                                                });
                                                alert.create().show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                        alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                        alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting resignations");
                                        alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Activity x = (Activity) MyApp.app.getApplicationContext();
                                                if (x != null) {
                                                    x.finish();
                                                }
                                            }
                                        });
                                        alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Login.redirectActivity();
                                            }
                                        });
                                        alert.create().show();
                                    }
                                });
                            }

                            @Override
                            public void onFailed(String error) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                alert.setTitle(c.getResources().getString(R.string.problemInGettingDataُTitle));
                                alert.setMessage(c.getResources().getString(R.string.problemInGettingData)+" getting jobtitles");
                                alert.setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Activity x = (Activity) MyApp.app.getApplicationContext();
                                        if (x != null) {
                                            x.finish();
                                        }
                                    }
                                });
                                alert.setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Login.redirectActivity();
                                    }
                                });
                                alert.create().show();
                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MyApp.app);
                        alert.setTitle(MyApp.app.getResources().getString(R.string.problemInGettingDataُTitle));
                        alert.setMessage(MyApp.app.getResources().getString(R.string.problemInGettingData)+" getting HR orders");
                        alert.setNegativeButton(MyApp.app.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Activity x = (Activity) MyApp.app.getApplicationContext();
                                if (x != null) {
                                    x.finish();
                                }
                            }
                        });
                        alert.setPositiveButton(MyApp.app.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Login.redirectActivity();
                            }
                        });
                        alert.create().show();
                    }
                });
            }
            @Override
            public void onFailed(String error) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyApp.app);
                alert.setTitle(MyApp.app.getResources().getString(R.string.problemInGettingDataُTitle) );
                alert.setMessage(MyApp.app.getResources().getString(R.string.problemInGettingData)+" getting employees data");
                alert.setNegativeButton(MyApp.app.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity x = (Activity) MyApp.app.getApplicationContext();
                        if (x != null) {
                            x.finish();
                        }
                    }
                });
                alert.setPositiveButton(MyApp.app.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Login.redirectActivity();
                    }
                });
                alert.create().show();
            }
        });
    }

    public int getVacationStatus() {
        return VacationStatus;
    }

    public void setVacationStatus(int vacationStatus) {
        VacationStatus = vacationStatus;
    }

    // Site Visit Orders

    public void getNumberOfSiteVisitOrders (VollyCallback callback) {
//        List<USER> list = new ArrayList<>();
//        list.add(MyApp.MyUser);
//        for (USER u :MyApp.EMPS) {
//            if (u.DirectManager == MyApp.MyUser.JobNumber ) {
//                list.add(u);
//            }
//            if (u.DepartmentManager == MyApp.MyUser.JobNumber) {
//                USER x = USER.searchUserByJobNumber(list,u.JobNumber);
//                if (x == null ) {
//                    list.add(u);
//                }
//            }
//        }
//        Log.d("siteVisitOrdersUsers" , list.size()+" ");
        if (isDirectManager || isDepartmentManager) {
            StringRequest request = new StringRequest(Request.Method.POST,getSiteVisitOrdersForGroupUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("siteVisitOrdersUsers" , response);
                    try {
                        int res = Integer.valueOf(response);
                        MyApp.ProjectsCounters[1] = res ;
                        callback.onSuccess(response);
                    }
                    catch (Exception e ) {
                        callback.onFailed(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("siteVisitOrdersUsers" , error.toString());
                    callback.onFailed(error.toString());
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> par = new HashMap<>();
                    par.put("Count" , String.valueOf(MyStaff.size()));
                    par.put("Status" , "0");
                    for (int i=0;i<MyStaff.size();i++) {
                        par.put("TO"+i, String.valueOf(MyStaff.get(i).JobNumber));
                    }
                    return par;
                }
            };
            Volley.newRequestQueue(MyApp.app).add(request);
        }
        else {
            StringRequest request = new StringRequest(Request.Method.POST,getSiteVisitOrdersForGroupUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("siteVisitOrdersUsers" , response);
                    try {
                        int res = Integer.valueOf(response);
                        MyApp.ProjectsCounters[1] = res ;
                        callback.onSuccess(response);
                    }
                    catch (Exception e ) {
                        callback.onFailed(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("siteVisitOrdersUsers" , error.toString());
                    callback.onFailed(error.toString());
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> par = new HashMap<>();
                    par.put("Count" , String.valueOf(1));
                    par.put("Status" , "0");
                    par.put("TO"+0, String.valueOf(JobNumber));
                    return par;
                }
            };
            Volley.newRequestQueue(MyApp.app).add(request);
        }
    }

    // Maintenance Orders

    public void getMaintenanceOrders(VollyCallback callback) {
        Log.d("gettingMaintenance","started");
        if (isDirectManager || isDepartmentManager){
            if (isDepartmentManager) {
                Log.d("gettingMaintenance","departmentManager");
                StringRequest req = new StringRequest(Request.Method.POST,getMaintenanceOrdersToDepartmentManagerUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("gettingMaintenance","departmentManager " + response);
                                try{
                                    int res = Integer.parseInt(response);
                                    MyApp.ProjectsCounters[0] = res ;
                                    callback.onSuccess(response);
                                }
                                catch (Exception e) {
                                    callback.onFailed(e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("gettingMaintenance","departmentManager " + error);
                                callback.onFailed(error.toString());
                            }
                        })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> par = new HashMap<>();
                        par.put("Status" , "0");
                        par.put("Department" , MyUser.Department);
                        for (int i=0;i<MyStaff.size();i++) {
                            par.put("To"+i, String.valueOf(MyStaff.get(i).JobNumber));
                        }
                        return par;
                    }
                };
                Volley.newRequestQueue(MyApp.app).add(req);
            }
            else {
                Log.d("gettingMaintenance","directManager");
                StringRequest req = new StringRequest(Request.Method.POST, getMaintenanceOrdersToDirectManagerUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("gettingMaintenance","directManager "+response);
                                try{
                                    int res = Integer.parseInt(response);
                                    MyApp.ProjectsCounters[0] = res ;
                                    callback.onSuccess(response);
                                }
                                catch (Exception e) {
                                    callback.onFailed(e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                callback.onFailed(error.toString());
                            }
                        })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> par = new HashMap<>();
                        par.put("Status" , "0");
                        par.put("TO", String.valueOf(MyApp.MyUser.JobNumber));
                        return par;
                    }
                };
                Volley.newRequestQueue(MyApp.app).add(req);
            }
        }
        else {
            Log.d("gettingMaintenance","employee");
            StringRequest req = new StringRequest(Request.Method.POST, getMaintenanceOrdersToOneUserUrl,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("gettingMaintenance","employee "+response);
                    try{
                        int res = Integer.parseInt(response);
                        MyApp.ProjectsCounters[0] = res ;
                        callback.onSuccess(response);
                    }
                    catch (Exception e) {
                        callback.onFailed(e.getMessage());
                    }
                }
            },
                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onFailed(error.toString());
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> par = new HashMap<>();
                    par.put("Status" , "0");
                    par.put("To", String.valueOf(MyApp.MyUser.JobNumber));
                    return par;
                }
            };
            Volley.newRequestQueue(MyApp.app).add(req);
        }
    }

    @Override
    public String toString() {
        String res = "";
        res+=id+"\n";
        res+=JobNumber+"\n";
        res+=User+"\n";
        res+=FirstName+"\n";
        res+=LastName+"\n";
        res+=Department+"\n";
        res+=JobTitle+"\n";
        res+=DirectManager+"\n";
        res+=DepartmentManager+"\n";
        res+=WorkLocationLa+"\n";
        res+=WorkLocationLo+"\n";
        res+=Mobile+"\n";
        res+=Email+"\n";
        res+=Pic+"\n";
        res+=IDNumber+"\n";
        res+=IDExpireDate+"\n";
        res+=BirthDate+"\n";
        res+=Nationality+"\n";
        res+=PassportNumber+"\n";
        res+=PassportExpireDate+"\n";
        res+=ContractNumber+"\n";
        res+=ContractStartDate+"\n";
        res+=ContractDuration+"\n";
        res+=ContractExpireDate+"\n";
        res+=InsuranceExpireDate+"\n";
        res+=Bank+"\n";
        res+=BankAccount+"\n";
        res+=BankIban+"\n";
        res+=IDsWarningNotifications+"\n";
        res+=PASSPORTsWarningNotification+"\n";
        res+=CONTRACTsWarningNotification+"\n";
        res+=Salary+"\n";
        res+=VacationDays+"\n";
        res+=SickDays+"\n";
        res+=EmergencyDays+"\n";
        res+=VacationStatus+"\n";
        res+=VacationAlternative+"\n";
        res+=JoinDate+"\n";
        res+=Token+"\n";
        return res ;
    }


}
