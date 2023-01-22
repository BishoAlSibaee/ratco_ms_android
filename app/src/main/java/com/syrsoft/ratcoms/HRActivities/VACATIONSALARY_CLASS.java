package com.syrsoft.ratcoms.HRActivities;

import java.util.List;

public class VACATIONSALARY_CLASS
{
    public int id ;
    public int EmpID ;
    public int JobNumber ;
    public String FirstName ;
    public String LastName ;
    public int DirectManager;
    public String DirectManagerName ;
    public String JobTitle ;
    public int VacationType ;
    public String SendDate ;
    public String StartDate ;
    public int VacationID ;
    public String Notes ;
    public String EndDate ;
    public List<Custody_Auth> Auths ;
    public int Status ;

    public VACATIONSALARY_CLASS(int id, int empID, int jobNumber, String firstName, String lastName, int directManager, String directManagerName, String jobTitle, int vacationType, String sendDate, String startDate, int vacationID,String notes, String endDate, List<Custody_Auth> auths, int status) {
        this.id = id;
        EmpID = empID;
        JobNumber = jobNumber;
        FirstName = firstName;
        LastName = lastName;
        DirectManager = directManager;
        DirectManagerName = directManagerName;
        JobTitle = jobTitle;
        VacationType = vacationType;
        SendDate = sendDate;
        StartDate = startDate;
        VacationID = vacationID;
        Notes = notes ;
        EndDate = endDate;
        Auths = auths;
        Status = status;
    }



}
