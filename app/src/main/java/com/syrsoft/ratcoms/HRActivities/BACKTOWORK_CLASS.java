package com.syrsoft.ratcoms.HRActivities;

import java.util.List;

public class BACKTOWORK_CLASS {

    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String FName ;
    public String LName ;
    public int DirectManager;
    public String DirectManagerName ;
    public String JobTitle ;
    public int VacationID ;
    public String EndDate ;
    public String BackDate ;
    public List<Custody_Auth> Auths ;
    public int Status ;

    public BACKTOWORK_CLASS(int id, int empID, int jobNumber, String FName, String LName, int directManager, String directManagerName, String jobTitle, int vacationID, String endDate, String backDate, List<Custody_Auth> auths, int status) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        this.FName = FName;
        this.LName = LName;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        JobTitle = jobTitle;
        VacationID = vacationID;
        EndDate = endDate;
        BackDate = backDate;
        Auths = auths;
        Status = status;
    }
}
