package com.syrsoft.ratcoms.HRActivities;

public class SalaryReport {

    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String Link ;
    public int Month ;

    public SalaryReport(int id, int empID, int jobNumber, String link, int month) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        Link = link;
        Month = month;
    }
}
