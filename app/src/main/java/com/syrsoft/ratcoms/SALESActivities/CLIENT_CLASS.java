package com.syrsoft.ratcoms.SALESActivities;

import java.util.List;

public class CLIENT_CLASS {

    public int id ;
    public String ClientName ;
    public String City ;
    public String PhonNumber ;
    public String Address ;
    public String Email ;
    public int SalesMan ;
    public double  LA ;
    public double LO ;
    public String FieldOfWork ;
    public List<RESPONSIBLE_CLASS> Responsibles ;

    public CLIENT_CLASS(int id, String clientName, String city, String phonNumber, String address, String email, int salesMan, double LA, double LO, String fieldOfWork) {
        this.id = id;
        ClientName = clientName;
        City = city;
        PhonNumber = phonNumber;
        Address = address;
        Email = email;
        SalesMan = salesMan;
        this.LA = LA;
        this.LO = LO;
        FieldOfWork = fieldOfWork;
    }

    public void setResponsibles (List<RESPONSIBLE_CLASS> Responsibles){
        this.Responsibles = Responsibles ;
    }

    public List<RESPONSIBLE_CLASS> getResponsibles() {
        return Responsibles ;
    }
}
