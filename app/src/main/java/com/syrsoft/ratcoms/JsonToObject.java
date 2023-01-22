package com.syrsoft.ratcoms;

import android.content.Context;
import android.util.Log;

import com.syrsoft.ratcoms.HRActivities.ADVANCEPAYMENT_CLASS;
import com.syrsoft.ratcoms.HRActivities.ATTENDANCE_CLASS;
import com.syrsoft.ratcoms.HRActivities.Acceptance;
import com.syrsoft.ratcoms.HRActivities.Auth;
import com.syrsoft.ratcoms.HRActivities.Authrized_Emp;
import com.syrsoft.ratcoms.HRActivities.BACKTOWORK_CLASS;
import com.syrsoft.ratcoms.HRActivities.BONUS;
import com.syrsoft.ratcoms.HRActivities.CUSTODY_REQUEST_CLASS;
import com.syrsoft.ratcoms.HRActivities.Custody_Auth;
import com.syrsoft.ratcoms.HRActivities.EXIT_REQUEST_CLASS;
import com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE;
import com.syrsoft.ratcoms.HRActivities.JobTitle;
import com.syrsoft.ratcoms.HRActivities.RESIGNATION_CLASS;
import com.syrsoft.ratcoms.HRActivities.StaffAttendByLocation_CLASS;
import com.syrsoft.ratcoms.HRActivities.VACATIONSALARY_CLASS;
import com.syrsoft.ratcoms.HRActivities.VACATION_CLASS;
import com.syrsoft.ratcoms.PROJECTSActivity.LOCAL_PURCHASE_ORDER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonToObject {

    public static List<Object> translate(String res , Class o , Context co )
    {
        List<Object> List = new ArrayList<Object>();
        String name = o.getName() ;
        Log.d("className" , name );
        try {
            JSONArray arr = new JSONArray(res);
            if (name.equals("com.syrsoft.ratcoms.USER")) {
                for (int i=0 ; i< arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int jn = row.getInt("JobNumber");
                    String user = row.getString("User");
                    String FirstName = row.getString("FirstName");
                    String LastName = row.getString("LastName");
                    String Department = row.getString("Department");
                    Log.d("hrProblem",Department);
                    String JobTitle = row.getString("JobTitle");
                    int DirectManager = row.getInt("DirectManager");
                    int departmentManager = row.getInt("DepartmentManager");
                    double workLa = row.getDouble("WorkLocationLa");
                    double workLo = row.getDouble("WorkLocationLo");
                    String Mobile = row.getString("Mobile");
                    String Email = row.getString("Email");
                    String Pic = row.getString("Pic");
                    String IDNumber = row.getString("IDNumber") ;
                    String IDExpireDate = row.getString("IDExpireDate") ;
                    String BirthDate = row.getString("BirthDate");
                    String Nationality = row.getString("Nationality");
                    String PassportNumber = row.getString("PassportNumber");
                    String PassportExpireDate = row.getString("PassportExpireDate");
                    String ContractNumber = row.getString("ContractNumber");
                    String ContractStartDate = row.getString("ContractStartDate") ;
                    int ContractDuration = row.getInt("ContractDuration") ;
                    String ContractExpireDate  = row.getString("ContractExpireDate") ;
                    String Insurance = row.getString("InsuranceExpireDate");
                    String bank = row.getString("Bank");
                    String bankAccount = row.getString("BankAccount");
                    String BankIban = row.getString("BankIban");
                    int idsWarning = row.getInt("IDsWarningNotifications");
                    int passWarning = row.getInt("PASSPORTsWarningNotification");
                    int contractWarning = row.getInt("CONTRACTsWarningNotification");
                    double Salary = row.getDouble("Salary");
                    double VacationDays = row.getDouble("VacationDays");
                    int SickDays = row.getInt("SickDays") ;
                    int EmergencyDays =row.getInt("EmergencyDays") ;
                    String JoinDate = row.getString("JoinDate") ;
                    int vacationStatus = row.getInt("VacationStatus");
                    int vacationAlternative = row.getInt("VacationAlternative");
                    String token = row.getString("Token");
                    USER u = new USER(id,jn,user,FirstName,LastName,Department,JobTitle,DirectManager,departmentManager,workLa,workLo,Mobile,Email,Pic,IDNumber,IDExpireDate,BirthDate,Nationality,PassportNumber,PassportExpireDate,ContractNumber,ContractStartDate,ContractDuration,ContractExpireDate,Insurance,bank,bankAccount,BankIban,idsWarning,passWarning,contractWarning,Salary,VacationDays,SickDays,EmergencyDays,vacationStatus,vacationAlternative,JoinDate,token);
                    List.add(u);
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.RESIGNATION_CLASS")){
                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int empId = row.getInt("EmpID");
                    int jnum = row.getInt("JobNumber");
                    String fname = row.getString("FName");
                    String lname = row.getString("LName");
                    int Dmanager = row.getInt("DirectManager");
                    String Dmn = row.getString("DirectManagerName");
                    String jtitle = row.getString("JobTitle");
                    String rreason = row.getString("ResignationReason");
                    String rdate = row.getString("ResignationDate");
                    String rsenddate = row.getString("ResignationSendDate");
                    List<Custody_Auth> auths = new ArrayList<Custody_Auth>() ;
                    for (int j=0;j<10;j++) {
                        auths.add(new Custody_Auth(row.getInt("Auth"+(j+1)+"ID"),row.getInt("Auth"+(j+1)),row.getLong("Auth"+(j+1)+"Date"),row.getString("Auth"+(j+1)+"Note")));
                    }
                    int status = row.getInt("Status");
                    RESIGNATION_CLASS r = new RESIGNATION_CLASS(id,empId,jnum,fname,lname,Dmanager,Dmn,jtitle,rreason,rdate,rsenddate,auths,status);
                    List.add(r);
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.VACATION_CLASS")) {

                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int empId = row.getInt("EmpID");
                    int jnum = row.getInt("JobNumber");
                    String fname = row.getString("FName");
                    String lname = row.getString("LName");
                    int Dmanager = row.getInt("DirectManager");
                    String Dmn = row.getString("DirectManagerName");
                    String jtitle = row.getString("JobTitle");
                    int vtype = row.getInt("VacationType");
                    String sendDate = row.getString("SendDate");
                    String start = row.getString("StartDate");
                    int days = row.getInt("VacationDays");
                    String end = row.getString("EndDate");
                    int altId = row.getInt("AlternativeID");
                    String altName = row.getString("AlternativeName");
                    String Location = row.getString("Location");
                    String Notes = row.getString("Notes");

                    List<Custody_Auth> auths = new ArrayList<Custody_Auth>() ;
                    for (int j=0;j<10;j++) {
                        auths.add(new Custody_Auth(row.getInt("Auth"+(j+1)+"ID"),row.getInt("Auth"+(j+1)),row.getLong("Auth"+(j+1)+"Date"),row.getString("Auth"+(j+1)+"Note")));
                        //Log.d("vacationsResponse"  , "Auth"+(j+1)+" = "+row.getInt("Auth"+(j+1)));
                    }
                    int status = row.getInt("Status");
                    int bs = row.getInt("BackStatus") ;
                    int vss = row.getInt("VSalaryStatus") ;
                    VACATION_CLASS r = new VACATION_CLASS(id,empId,jnum,fname,lname,Dmanager,Dmn,jtitle,vtype,sendDate,start,days,end,altId,altName,Location,Notes,auths,status,bs,vss);
                    List.add(r);
                    Log.d("type" , vtype+"" );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.BONUS")) {

                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int jnum = row.getInt("JobNumber");
                    String namee = row.getString("Name");
                    int Rjn = row.getInt("RequesterJobNumber");
                    String rName = row.getString("RequesterName");
                    double amount = row.getDouble("BonusAmount");
                    String rDate = row.getString("RequestDate");
                    String notes = row.getString("Notes");
                    int status = row.getInt("Status");
                    String from = row.getString("From");
                    List<Acceptance> auths = new ArrayList<>() ;
                    for (int j=0;j<10;j++) {
                        auths.add(new Acceptance(row.getInt("Acc"+(j+1)),row.getInt("Acc"+(j+1)+"Status"),row.getString("Acc"+(j+1)+"Note")));
                    }
                    BONUS r = new BONUS(id,jnum,namee,Rjn,rName,amount,rDate,notes,status,from,auths);
                    List.add(r);
                    Log.d("type" , "" );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.BACKTOWORK_CLASS")){

                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int empId = row.getInt("EmpID");
                    int jnum = row.getInt("JobNumber");
                    String fname = row.getString("FirstName");
                    String lname = row.getString("LastName");
                    int Dmanager = row.getInt("DirectManager");
                    String Dmn = row.getString("DirectManagerName");
                    String jtitle = row.getString("JobTitle");
                    int vtype = row.getInt("VacationID");
                    String end = row.getString("EndDate");
                    String bdate = row.getString("BackDate");
                    List<Custody_Auth> auths = new ArrayList<Custody_Auth>() ;
                    for (int j=0;j<10;j++) {
                        auths.add(new Custody_Auth(row.getInt("Auth"+(j+1)+"ID"),row.getInt("Auth"+(j+1)),row.getLong("Auth"+(j+1)+"Date"),row.getString("Auth"+(j+1)+"Note")));
                    }
                    int status = row.getInt("Status");
                    BACKTOWORK_CLASS r = new BACKTOWORK_CLASS(id,empId,jnum,fname,lname,Dmanager,Dmn,jtitle,vtype,end,bdate,auths,status);
                    List.add(r);
                    Log.d("type" , vtype+"" );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.ADVANCEPAYMENT_CLASS")){

                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int empId = row.getInt("EmpID");
                    int jnum = row.getInt("JobNumber");
                    String fname = row.getString("FirstName");
                    String lname = row.getString("LastName");
                    int Dmanager = row.getInt("DirectManager");
                    String Dmn = row.getString("DirectManagerName");
                    String jtitle = row.getString("JobTitle");
                    double amount = row.getDouble("Amount");
                    String reason = row.getString("Reason");
                    double installment = row.getDouble("Installment");
                    String sdate = row.getString("SendDate");
                    List<Custody_Auth> auths = new ArrayList<Custody_Auth>() ;
                    for (int j=0;j<10;j++) {
                        auths.add(new Custody_Auth(row.getInt("Auth"+(j+1)+"ID"),row.getInt("Auth"+(j+1)),row.getLong("Auth"+(j+1)+"Date"),row.getString("Auth"+(j+1)+"Note")));
                    }
                    int status = row.getInt("Status");
                    ADVANCEPAYMENT_CLASS r = new ADVANCEPAYMENT_CLASS(id,empId,jnum,fname,lname,Dmanager,Dmn,jtitle,amount,reason,installment,sdate,auths,status);
                    List.add(r);
                    Log.d("type" , "" );
                }

            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.VACATIONSALARY_CLASS")){

                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int empId = row.getInt("EmpID");
                    int jnum = row.getInt("JobNumber");
                    String fname = row.getString("FirstName");
                    String lname = row.getString("LastName");
                    int Dmanager = row.getInt("DirectManager");
                    String Dmn = row.getString("DirectManagerName");
                    String jtitle = row.getString("JobTitle");
                    int vtype = row.getInt("VacationType");
                    String sendDate = row.getString("SendDate");
                    String start = row.getString("StartDate");
                    int days = row.getInt("VacationID");
                    String notes = row.getString("Notes");
                    String end = row.getString("EndDate");
                    List<Custody_Auth> auths = new ArrayList<Custody_Auth>() ;
                    for (int j=0;j<10;j++) {
                        auths.add(new Custody_Auth(row.getInt("Auth"+(j+1)+"ID"),row.getInt("Auth"+(j+1)),row.getLong("Auth"+(j+1)+"Date"),row.getString("Auth"+(j+1)+"Note")));
                    }
                    int status = row.getInt("Status");
                    VACATIONSALARY_CLASS r = new VACATIONSALARY_CLASS(id,empId,jnum,fname,lname,Dmanager,Dmn,jtitle,vtype,sendDate,start,days,notes,end,auths,status);
                    List.add(r);
                    Log.d("type" , vtype+"" );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.CUSTODY_REQUEST_CLASS")) {

                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int empId = row.getInt("EmpID");
                    int jnum = row.getInt("JobNumber");
                    String fname = row.getString("FirstName");
                    String lname = row.getString("LastName");
                    int Dmanager = row.getInt("DirectManager");
                    String Dmn = row.getString("DirectManagerName");
                    String jtitle = row.getString("JobTitle");
                    double amount = row.getDouble("Amount");
                    String reason = row.getString("Reason");
                    String sdate = row.getString("Date");
                    List<Custody_Auth> auths = new ArrayList<Custody_Auth>() ;
                    for (int j=0;j<10;j++) {
                        auths.add(new Custody_Auth(row.getInt("Auth"+(j+1)+"ID"),row.getInt("Auth"+(j+1)),row.getLong("Auth"+(j+1)+"Date"),row.getString("Auth"+(j+1)+"Note")));
                    }
                    int status = row.getInt("Status");
                    CUSTODY_REQUEST_CLASS r = new CUSTODY_REQUEST_CLASS(id,empId,fname,lname,jnum,jtitle,Dmanager,Dmn,amount,reason,sdate,auths,status);
                    List.add(r);
                    Log.d("type" , "" );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE")) {

                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    String HROrderName = row.getString("HROrderName");
                    String ArabicName = row.getString("ArabicName");
                    int Auth1 = row.getInt("Auth1");
                    int Auth2 = row.getInt("Auth2");
                    int Auth3 = row.getInt("Auth3");
                    int Auth4 = row.getInt("Auth4");
                    int Auth5 = row.getInt("Auth5");
                    int Auth6 = row.getInt("Auth6");
                    int Auth7 = row.getInt("Auth7");
                    int Auth8 = row.getInt("Auth8");
                    int Auth9 = row.getInt("Auth9");
                    int Auth10 = row.getInt("Auth10");
                    HR_ORDER_TYPE r = new HR_ORDER_TYPE(id,HROrderName,ArabicName,Auth1,Auth2,Auth3,Auth4,Auth5,Auth6,Auth7,Auth8,Auth9,Auth10);
                    List.add(r);
                    Log.d("type" , name );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.Authrized_Emp")) {

                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int JobTitleId = row.getInt("JobTitleId");
                    String AuthorizationName = row.getString("AuthorizationName");
                    String ArabicName = row.getString("ArabicName");
                    Authrized_Emp r = new Authrized_Emp(id,JobTitleId,AuthorizationName,ArabicName);
                    List.add(r);
                    Log.d("type" , name );
                }

            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.JobTitle")) {

                for (int i=0;i<arr.length();i++) {
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    String JobTit = row.getString("JobTitle");
                    int Department = row.getInt("Department");
                    String ArabicName = row.getString("ArabicName");
                    JobTitle r = new JobTitle(id,JobTit,Department,ArabicName);
                    List.add(r);
                    Log.d("type" , name );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.StaffAttendByLocation_CLASS")) {
                for (int i=0;i<arr.length();i++) {
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int EmpID = row.getInt("EmpID");
                    int JN = row.getInt("JobNumber");
                    StaffAttendByLocation_CLASS s = new StaffAttendByLocation_CLASS(id,EmpID,JN);
                    List.add(s);
                    Log.d("type" , name );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.ATTENDANCE_CLASS")) {
                for (int i=0;i<arr.length();i++) {
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int EmpID = row.getInt("EmpID");
                    int JN = row.getInt("JobNumber");
                    String nam = row.getString("Name");
                    String date = row.getString("Date");
                    String time = row.getString("Time");
                    int op = row.getInt("Operation") ;
                    double la = row.getDouble("LA");
                    double lo = row.getDouble("LO");
                    ATTENDANCE_CLASS s = new ATTENDANCE_CLASS(id,EmpID,JN,nam,date,time,op,la,lo);
                    List.add(s);
                    Log.d("type" , name );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.HRActivities.EXIT_REQUEST_CLASS")) {
                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    int id = row.getInt("id");
                    int empId = row.getInt("EmpID");
                    int jnum = row.getInt("JobNumber");
                    String nname = row.getString("Name");
                    int Dmanager = row.getInt("DirectManager");
                    String Dmn = row.getString("DirectManagerName");
                    String date = row.getString("Date");
                    String time = row.getString("Time");
                    String back = row.getString("BackTime");
                    String notes = row.getString("Notes");
                    List<Custody_Auth> auths = new ArrayList<Custody_Auth>() ;
                    for (int j=0;j<10;j++) {
                        auths.add(new Custody_Auth(row.getInt("Auth"+(j+1)+"ID"),row.getInt("Auth"+(j+1)),row.getLong("Auth"+(j+1)+"Date"),row.getString("Auth"+(j+1)+"Note")));
                    }
                    int status = row.getInt("Status");
                    EXIT_REQUEST_CLASS r = new EXIT_REQUEST_CLASS(id,empId,jnum,nname,Dmanager,Dmn,date,time,back,notes,auths,status);
                    List.add(r);
                    Log.d("type" , "" );
                }
            }
            else if (name.equals("com.syrsoft.ratcoms.PROJECTSActivity.LOCAL_PURCHASE_ORDER")) {
                for (int i=0;i<arr.length();i++){
                    JSONObject row = arr.getJSONObject(i);
                    LOCAL_PURCHASE_ORDER r = new LOCAL_PURCHASE_ORDER(row.getInt("id"),row.getInt("ProjectID"),row.getString("ProjectName"),row.getInt("Supmitter"),row.getInt("AcceptedQID"),
                            row.getInt("Acc1ID"),row.getString("Acc1Note"),row.getInt("Acc1Status"),row.getInt("Acc2ID"),row.getString("Acc2Note"),row.getInt("Acc2Status")
                            ,row.getInt("Acc3ID"),row.getString("Acc3Note"),row.getInt("Acc3Status"),row.getInt("Acc4ID"),row.getString("Acc4Note"),row.getInt("Acc4Status"),row.getInt("Acc5ID"),
                            row.getString("Acc5Note"),row.getInt("Acc5Status"),row.getInt("Status"),row.getString("Notes"));
                    List.add(r);
                    Log.d("type" , "" );
                }
            }
            else {
                Log.d("LoginError" , name);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d("LoginError" , e.getMessage());
        }
        return List ;
    }
}
