package com.syrsoft.ratcoms.HRActivities;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.USER;

import java.util.Calendar;
import java.util.Locale;

public class Auth {

    int AuthID ;
    int Auth;
    long AuthDate ;

    public Auth(int authID, int autn, long authDate) {
        AuthID = authID;
        Auth = autn;
        AuthDate = authDate;
    }

    public int getAuthID() {
        return AuthID;
    }

    public int getAuth() {
        return Auth;
    }

    public String getAuthDate() {
        String res = "" ;
        if (AuthDate != 0) {
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTimeInMillis(AuthDate);
            res = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DAY_OF_MONTH) ;
        }

        return res;
    }

    public void setAuthID(int authID) {
        AuthID = authID;
    }

    public void setAuth(int auth) {
        Auth = auth;
    }

    public void setAuthDate(long authDate) {
        AuthDate = authDate;
    }

    public boolean isAuthExists() {
        boolean result = false ;
        if (this.AuthID != 0 ) {
            result = true ;
        }
        return result ;
    }

    public String getAuthResult() {
        String result = "" ;
        if (this.Auth == 0 ) {
            result = "Pending";
        }
        else if (this.Auth == 1) {
            result = "Accepted" ;
        }
        else if (this.Auth == 2) {
            result = "Rejected" ;
        }
        return result ;
    }

    public USER getAuthUser() {
        USER x = null ;
        if (MyApp.EMPS != null ) {
            for (USER u :MyApp.EMPS) {
                if (u.id == getAuthID()) {
                    x = u ;
                }
            }
        }
        return x ;
    }

}
