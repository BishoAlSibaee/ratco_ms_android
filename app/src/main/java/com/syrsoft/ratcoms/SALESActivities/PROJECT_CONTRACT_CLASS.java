package com.syrsoft.ratcoms.SALESActivities;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PROJECT_CONTRACT_CLASS {

    public int id ;
    public int ClientID ;
    public String ProjectName ;
    public String Date ;
    public String City ;
    public String Address ;
    public Double LA ;
    public Double LO ;
    public String ProjectDescription ;
    public String ProjectManager ;
    public String MobileNumber ;
    public int SalesMan ;
    public String HandOverDate ;
    public String WarrantyExpireDate ;
    public String ContractLink ;
    public CLIENT_CLASS CLIENT ;
    public TermsAndConditions Terms ;
    public List<CONTRACT_ITEMS_CLASS> Items ;
    public int Supplied ;
    public int Installed ;
    public int Handover ;
    public String SupplyDate ;
    public String InstallDate ;
    public String HandoverDate ;


    public PROJECT_CONTRACT_CLASS(int id, int clientID, String projectName,String Date, String city, String address, Double LA, Double LO, String projectDescription, String projectManager, String mobileNumber, int salesMan, String handOverDate, String warrantyExpireDate, String contractLink ,int sup,int ins,int hand,String supDate,String InsDate,String handDate) {
        this.id = id;
        ClientID = clientID;
        ProjectName = projectName;
        this.Date = Date ;
        City = city;
        Address = address;
        this.LA = LA;
        this.LO = LO;
        ProjectDescription = projectDescription;
        ProjectManager = projectManager;
        MobileNumber = mobileNumber;
        SalesMan = salesMan;
        HandOverDate = handOverDate;
        WarrantyExpireDate = warrantyExpireDate;
        ContractLink = contractLink;
        Supplied = sup ;
        Installed = ins ;
        Handover = hand ;
        SupplyDate = supDate ;
        InstallDate = InsDate ;
        HandoverDate = handDate ;
    }

    public void setCLIENT(CLIENT_CLASS CLIENT) {
        this.CLIENT = CLIENT;
    }

    public void setTerms(TermsAndConditions terms) {
        Terms = terms;
    }

    public void setItems(List<CONTRACT_ITEMS_CLASS> items) {
        Items = items;
    }

    public CLIENT_CLASS getCLIENT() {
        return CLIENT;
    }

    public TermsAndConditions getTerms() {
        return Terms;
    }

    public List<CONTRACT_ITEMS_CLASS> getItems() {
        return Items;
    }

    public String getSupplied() {
        String res = "" ;
        if (Supplied == 0 ) {
            res = "NO" ;
        }
        else if (Supplied == 1) {
            res = "YES" ;
        }
        return res ;
    }

    public String getInstalled() {
        String res = "" ;
        if (Installed == 0 ) {
            res = "NO" ;
        }
        else if (Installed == 1) {
            res = "YES" ;
        }
        return res ;
    }

    public String getHandovered() {
        String res = "" ;
        if (Handover == 0 ) {
            res = "NO" ;
        }
        else if (Handover == 1) {
            res = "YES" ;
        }
        return res ;
    }

    public String getWarranty() {
        String res = "" ;
        if (WarrantyExpireDate != null) {
            if (WarrantyExpireDate.equals("0000-00-00")) {
                res = MyApp.app.getResources().getString(R.string.projectHasNotHandovered);
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = sdf.parse(WarrantyExpireDate);
                    Calendar warr = Calendar.getInstance();
                    warr.setTime(date);
                    Calendar now = Calendar.getInstance(Locale.getDefault());
                    if (now.after(warr)) {
                        res = MyApp.app.getResources().getString(R.string.warrantyFinished) ;
                    }
                    else if (warr.after(now)) {
                        res = MyApp.app.getResources().getString(R.string.underWarranty) ;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return res ;
    }

    public int getWarrantyResult() {
        int res = 0 ;
        if (WarrantyExpireDate != null) {
            if (WarrantyExpireDate.equals("0")) {
                res = 0 ;
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = sdf.parse(WarrantyExpireDate);
                    Calendar warr = Calendar.getInstance();
                    warr.setTime(date);
                    Calendar now = Calendar.getInstance(Locale.getDefault());
                    if (now.after(warr)) {
                        res = 2 ;
                    }
                    else if (warr.after(now)) {
                        res = 1 ;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            res = 0 ;
        }
        return res ;
    }
}
