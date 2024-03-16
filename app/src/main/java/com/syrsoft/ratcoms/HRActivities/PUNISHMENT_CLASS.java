package com.syrsoft.ratcoms.HRActivities;

import java.util.List;

public class PUNISHMENT_CLASS {
    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String FName ;
    public String LName ;
    public int DirectManager;
    public String DirectManagerName ;
    public String JobTitle ;
    public String Reason ;
    public Double Amount ;
    public String SendDate ;
    public String EmployeeName;
    public int EmployeeJobNumber;
    public int Status ;
    public List<Custody_Auth> Auths;

    public PUNISHMENT_CLASS(int id, int empID, int jobNumber, String FName, String LName, int directManager, String directManagerName, String jobTitle, String reason, Double amount, String sendDate, String employeeName, int employeeJobNumber, int status, List<Custody_Auth> auths) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        this.FName = FName;
        this.LName = LName;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        JobTitle = jobTitle;
        Reason = reason;
        Amount = amount;
        SendDate = sendDate;
        EmployeeName = employeeName;
        EmployeeJobNumber = employeeJobNumber;
        Status = status;
        Auths = auths;
    }

}
