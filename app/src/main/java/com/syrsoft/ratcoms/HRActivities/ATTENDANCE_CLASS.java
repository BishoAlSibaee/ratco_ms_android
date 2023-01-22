package com.syrsoft.ratcoms.HRActivities;


import com.syrsoft.ratcoms.R;

public class ATTENDANCE_CLASS {

    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String Name ;
    public String Date ;
    public String Time ;
    public int Operation ;
    public double LA ;
    public double LO ;


    public ATTENDANCE_CLASS(int id, int empID, int jobNumber, String name, String date, String time, int operation, double LA, double LO) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        Name = name;
        Date = date;
        Time = time;
        Operation = operation;
        this.LA = LA;
        this.LO = LO;
    }

    public ATTENDANCE_CLASS(int empID, String date, String time, int operation) {
        this.EmpID = empID ;
        this.Date = date ;
        this.Time = time ;
        this.Operation = operation ;
    }

    public int getOperation() {
        int res = 0 ;
        if (Operation == 1) {
            res = R.drawable.in ;
        }
        else if (Operation == 0 ) {
            res = R.drawable.out ;
        }
        return res ;
    }
}
