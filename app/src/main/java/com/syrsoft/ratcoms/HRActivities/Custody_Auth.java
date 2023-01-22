package com.syrsoft.ratcoms.HRActivities;

public class Custody_Auth extends Auth {

    public String Note ;

    public Custody_Auth(int authID, int autn, long authDate , String note) {
        super(authID, autn, authDate);
        Note = note ;
    }
}
