package com.syrsoft.ratcoms.PROJECTSActivity;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;

import java.util.List;

public class MAINTENANCE_ORDER_CLASS {

    public int id ;
    public int ProjectID ;
    public String ProjectName ;
    public int ClientID ;
    public int SalesMan ;
    public double LA ;
    public double LO ;
    public String DamageDesc ;
    public String Notes ;
    public String Contact ;
    public String ContactName ;
    public String Date ;
    public int ToMaintenance ;
    public int ToAluminum ;
    public int ToDoors ;
    public int ForwardedTo ;
    public String Response ;
    public int Status ;
    public List<MaintenanceOrderLink> Links ;

    public MAINTENANCE_ORDER_CLASS(int id, int projectID, String projectName, int clientID, int salesMan, double LA, double LO, String damageDesc, String notes, String contact, String contactName, String date, int toMaintenance, int toAluminum, int toDoors, int forwardedTo, String response, int status) {
        this.id = id;
        ProjectID = projectID;
        ProjectName = projectName;
        ClientID = clientID;
        SalesMan = salesMan;
        this.LA = LA;
        this.LO = LO;
        DamageDesc = damageDesc;
        Notes = notes;
        Contact = contact;
        ContactName = contactName;
        Date = date;
        ToMaintenance = toMaintenance;
        ToAluminum = toAluminum;
        ToDoors = toDoors;
        ForwardedTo = forwardedTo;
        Response = response;
        Status = status;
    }

    public void setLinks(List<MaintenanceOrderLink> links) {
        Links = links;
    }

    public List<MaintenanceOrderLink> getLinks() {
        return Links;
    }

    public String getStatus () {
        String res = "" ;
        if (Status == 0) {
            res = MyApp.app.getResources().getString(R.string.open);
        }
        else if (Status == 1) {
            res = MyApp.app.getResources().getString(R.string.closed);
        }
        return res ;
    }

    public String getDepartment() {
        String res = "";
        if (ToMaintenance == 1) {
            res = res + MyApp.app.getResources().getString(R.string.maintenanceDepartment);
        }
        if (ToAluminum == 1) {
            res = res+MyApp.app.getResources().getString(R.string.aluminumDepartment);
        }
        if (ToDoors == 1) {
            res = res+MyApp.app.getResources().getString(R.string.doorsDepartment);
        }
        return res ;
    }
}
