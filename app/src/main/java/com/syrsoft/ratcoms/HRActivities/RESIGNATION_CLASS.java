package com.syrsoft.ratcoms.HRActivities;

import java.util.List;

public class RESIGNATION_CLASS {

    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String FName ;
    public String LName ;
    public int DirectManager;
    public String DirectManagerName ;
    public String JobTitle ;
    public String ResignationReason ;
    public String ResignationDate ;
    public String ResignationSendDate ;
    public List<Custody_Auth> Auths ;
    public int Status ;

    public RESIGNATION_CLASS(int id, int empID, int jobNumber, String FName, String LName, int directManager, String directManagerName, String jobTitle, String resignationReason, String resignationDate, String resignationSendDate, List<Custody_Auth> auths, int status) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        this.FName = FName;
        this.LName = LName;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        JobTitle = jobTitle;
        ResignationReason = resignationReason;
        ResignationDate = resignationDate;
        ResignationSendDate = resignationSendDate;
        Auths = auths;
        Status = status;
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
}
