package com.syrsoft.ratcoms.HRActivities;

import java.util.List;

public class CHAMBER_COMMERCE_CLASS {
    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String FName ;
    public String LName ;
    public int DirectManager;
    public String DirectManagerName ;
    public String JobTitle ;
    public String Title ;
    public String ContractForm;
    public String SendDate ;
    public int Status ;
    public List<Custody_Auth> Auths;

    public CHAMBER_COMMERCE_CLASS(int id, int empID, int jobNumber, String FName, String LName, int directManager, String directManagerName, String jobTitle, String title, String contractForm, String sendDate, int status, List<Custody_Auth> auths) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        this.FName = FName;
        this.LName = LName;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        JobTitle = jobTitle;
        Title = title;
        ContractForm = contractForm;
        SendDate = sendDate;
        Status = status;
        Auths = auths;
    }
}
