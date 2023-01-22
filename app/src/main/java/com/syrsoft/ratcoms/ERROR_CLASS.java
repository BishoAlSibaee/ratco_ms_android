package com.syrsoft.ratcoms;

public class ERROR_CLASS {

    int id ;
    String ErrorText ;
    String Activity ;
    String User ;
    String MethodName ;
    String Date ;
    String Time ;

    public ERROR_CLASS(int id, String errorText, String activity, String user, String methodName, String date, String time) {
        this.id = id;
        ErrorText = errorText;
        Activity = activity;
        User = user;
        MethodName = methodName;
        Date = date;
        Time = time;
    }

}
