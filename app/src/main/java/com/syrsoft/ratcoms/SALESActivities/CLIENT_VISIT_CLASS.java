package com.syrsoft.ratcoms.SALESActivities;

import android.location.Location;

public class CLIENT_VISIT_CLASS {

    public int id ;
    public int ClientID ;
    public String ClientName ;
    public int SalesMan ;
    public String Date ;
    public String Time ;
    public String ProjectDescription ;
    public String VisitDetails;
    public double LA ;
    public double LO ;
    public String Address ;
    public String Responsible ;
    public String QuotationLink ;
    public String LocationLink ;
    public String FileLink ;
    public int Interested ;
    public String FollowUpAt ;

    public CLIENT_VISIT_CLASS(int id, int clientID,String ClientName, int salesMan, String date, String time, String projectDescription, String visitDetails, double LA, double LO, String address, String responsible,String quotation,String location,String fileLink,int interested,String follow) {
        this.id = id;
        ClientID = clientID;
        this.ClientName = ClientName ;
        SalesMan = salesMan;
        Date = date;
        Time = time;
        ProjectDescription = projectDescription;
        VisitDetails = visitDetails;
        this.LA = LA;
        this.LO = LO;
        Address = address;
        Responsible = responsible;
        QuotationLink = quotation ;
        LocationLink = location ;
        FileLink = fileLink ;
        Interested = interested ;
        FollowUpAt = follow ;
    }
}
