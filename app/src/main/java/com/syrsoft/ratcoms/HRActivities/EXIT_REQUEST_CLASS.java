package com.syrsoft.ratcoms.HRActivities;

import java.util.List;

public class EXIT_REQUEST_CLASS {

    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String Name ;
    public int DirectManager;
    public String DirectManagerName ;
    public String Date ;
    public String Time ;
    public String BackTime ;
    public String Notes ;
    public List<Custody_Auth> Auths ;
    public int Status ;

    public EXIT_REQUEST_CLASS(int id, int empID, int jobNumber, String name, int directManager, String directManagerName, String date, String time, String backTime, String notes, List<Custody_Auth> auths, int status) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        Name = name;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        Date = date;
        Time = time;
        BackTime = backTime;
        Notes = notes;
        Auths = auths;
        Status = status;
    }
}
