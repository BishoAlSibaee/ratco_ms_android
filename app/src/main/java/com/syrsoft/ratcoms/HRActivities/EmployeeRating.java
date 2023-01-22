package com.syrsoft.ratcoms.HRActivities;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRating {

    private int id ;
    private int EmpID ;
    private int JobNumber ;
    private int Month ;
    private int Year ;
    private String Date ;
    private String Type ;
    private int Rating ;

    public EmployeeRating(int id, int empID, int jobNumber, int month,int year, String date, String type, int rating) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        Month = month;
        Year = year ;
        Date = date;
        Type = type;
        Rating = rating;
    }

    public int getId() {
        return id;
    }

    public int getEmpID() {
        return EmpID;
    }

    public int getJobNumber() {
        return JobNumber;
    }

    public int getMonth() {
        return Month;
    }

    public String getDate() {
        return Date;
    }

    public String getType() {
        return Type;
    }

    public int getRating() {
        return Rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmpID(int empID) {
        EmpID = empID;
    }

    public void setJobNumber(int jobNumber) {
        JobNumber = jobNumber;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getStringMonth () {
        String res = "";
        switch (Month) {
            case 1:
                res = "Jan" ;
            case 2:
                res = "Feb" ;
            case 3:
                res = "Mar" ;
            case 4:
                res = "Apr" ;
            case 5:
                res = "May" ;
            case 6:
                res = "Jun" ;
            case 7:
                res = "Jul" ;
            case 8:
                res = "Aug" ;
            case 9:
                res = "Sep" ;
            case 10:
                res = "Oct" ;
            case 11:
                res = "Nov" ;
            case 12:
                res = "Dec" ;
        }
        return res ;
    }

    public static List<EmployeeRating> searchRateByJobNumber(List<EmployeeRating> list, int JobNumber) {
        List<EmployeeRating> res = new ArrayList<>();
        for (int i=0 ;i<list.size();i++) {
            if (list.get(i).JobNumber == JobNumber) {
                res.add(list.get(i));
            }
        }
        return res ;
    }
}
