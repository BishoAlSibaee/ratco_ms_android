package com.syrsoft.ratcoms.HRActivities;

import java.util.List;

public class CUSTODY_REQUEST_CLASS {

    public int id ;
    public int EmpID ;
    public String FirstName ;
    public String LastName ;
    public int JobNumber ;
    public String Jobtitle ;
    public int DirectManager ;
    public String DirectManagerName ;
    public double Amount ;
    public String Reason ;
    public String Date ;
    public List<Custody_Auth> Auths ;
    public int Status ;

    public CUSTODY_REQUEST_CLASS(int id, int empID, String firstName, String lastName, int jobNumber, String jobtitle, int directManager, String directManagerName, double amount, String reason, String date, List<Custody_Auth> auths, int status) {
        this.id = id;
        EmpID = empID;
        FirstName = firstName;
        LastName = lastName;
        JobNumber = jobNumber;
        Jobtitle = jobtitle;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        Amount = amount;
        Reason = reason;
        Date = date;
        Auths = auths;
        Status = status;
    }
}
