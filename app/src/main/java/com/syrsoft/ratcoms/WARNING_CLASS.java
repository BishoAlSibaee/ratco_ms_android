package com.syrsoft.ratcoms;

public class WARNING_CLASS
{
    public int JobNumber ;
    public String FirstName ;
    public String LastName ;
    public String warning ;
    public String Date ;

    public WARNING_CLASS(int jobNumber, String firstName, String lastName, String warning, String date) {
        JobNumber = jobNumber;
        FirstName = firstName;
        LastName = lastName;
        this.warning = warning;
        Date = date;
    }
}
