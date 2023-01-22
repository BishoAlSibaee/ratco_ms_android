package com.syrsoft.ratcoms.HRActivities;

import java.security.PublicKey;
import java.util.List;

public class ADVANCEPAYMENT_CLASS {

    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String FName ;
    public String LName ;
    public int DirectManager;
    public String DirectManagerName ;
    public String JobTitle ;
    public Double Amount ;
    public String Reason ;
    public Double Installment ;
    public String SendDate ;
    public List<Custody_Auth> Auths;
    public int Status ;

    public ADVANCEPAYMENT_CLASS(int id, int empID, int jobNumber, String FName, String LName, int directManager, String directManagerName, String jobTitle, Double amount, String reason, Double installment, String sendDate, List<Custody_Auth> auths, int status) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        this.FName = FName;
        this.LName = LName;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        JobTitle = jobTitle;
        Amount = amount;
        Reason = reason;
        Installment = installment;
        SendDate = sendDate;
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
