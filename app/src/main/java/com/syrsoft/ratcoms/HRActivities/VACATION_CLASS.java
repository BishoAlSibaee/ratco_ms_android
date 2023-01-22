package com.syrsoft.ratcoms.HRActivities;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;

import java.util.List;

public class VACATION_CLASS  {

    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String FName ;
    public String LName ;
    public int DirectManager;
    public String DirectManagerName ;
    public String JobTitle ;
    public int VacationType ;
    public String SendDate ;
    public String StartDate ;
    public int VacationDays ;
    public String EndDate ;
    public int AlternativeID ;
    public String AlternativeName ;
    public String Location ;
    public String Notes ;
    public List<Custody_Auth> Auths ;
    public int Status ;
    public int BackStatus ;
    public int VSalaryStatus ;

    public VACATION_CLASS(int id, int empID, int jobNumber, String FName, String LName, int directManager, String directManagerName, String jobTitle, int vacationType, String sendDate, String startDate, int vacationDays, String endDate, int alternativeID, String alternativeName, String location, String notes, List<Custody_Auth> auths, int status,int bstatus,int vsstatus) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        this.FName = FName;
        this.LName = LName;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        JobTitle = jobTitle;
        VacationType = vacationType;
        SendDate = sendDate;
        StartDate = startDate;
        VacationDays = vacationDays;
        EndDate = endDate;
        AlternativeID = alternativeID;
        AlternativeName = alternativeName;
        Location = location;
        Notes = notes;
        Auths = auths;
        Status = status;
        BackStatus = bstatus ;
        VSalaryStatus = vsstatus ;
    }

    public boolean isAuthsRegistered() {
        boolean res = false ;
        for (int i=0 ; i < this.Auths.size() ; i++) {
            if (Auths.get(i).isAuthExists()) {
                res = true ;
            }
        }
        return res ;
    }

    public String getVacationType() {
        String res = "";
        if (VacationType == 0) {
            res = "Annual";
        }
        else if ( VacationType == 1) {
            res = "Emergency" ;
        }
        else if (VacationType == 2) {
            res = "Sick";
        }
        return res ;
    }

    public String getVacationResult() {
        String res = "" ;
        boolean s = true ;
        for(int i=0;i<Auths.size();i++){
            if (Auths.get(i).getAuth() == 2 && Status == 1) {
                s = false ;
            }
        }
        if (s) {
            res = MyApp.app.getResources().getString(R.string.approved);
        }
        else {
            res = MyApp.app.getResources().getString(R.string.rejected);
        }
        return res ;
    }

    public String getFirstAuthResult() {
        String res = "" ;
        if (Auths.get(0).Auth != 0 ) {
            if (Auths.get(0).Auth == 1) {
                res = MyApp.app.getResources().getString(R.string.approved) ;
            }
            else if (Auths.get(0).Auth == 2) {
                res = MyApp.app.getResources().getString(R.string.rejected);
            }
        }
        else {
            res = "لا يوجد";
        }
        return res ;
    }
}
