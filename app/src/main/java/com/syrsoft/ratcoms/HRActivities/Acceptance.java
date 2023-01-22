package com.syrsoft.ratcoms.HRActivities;

public class Acceptance {

    public int Acc ;
    public int AccStatus ;
    public String Note ;

    public Acceptance(int acc, int accStatus, String note) {
        Acc = acc;
        AccStatus = accStatus;
        Note = note;
    }

    public String getAuthResult() {
        String result = "" ;
        if (this.AccStatus == 0 ) {
            result = "Pending";
        }
        else if (this.AccStatus == 1) {
            result = "Accepted" ;
        }
        else if (this.AccStatus == 2) {
            result = "Rejected" ;
        }
        return result ;
    }

    public int getAcc() {
        return AccStatus;
    }
}
