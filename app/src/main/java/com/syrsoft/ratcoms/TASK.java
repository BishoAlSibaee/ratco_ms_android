package com.syrsoft.ratcoms;

import com.syrsoft.ratcoms.SALESActivities.CLIENT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.Client;


public class TASK {

    int id ;
    int Salesman ;
    String Date ;
    int Client ;
    String Action ;
    int ReferenceID ;
    CLIENT_CLASS client ;


    public TASK(int id, int salesman, String date, int client, String action, int referenceID) {
        this.id = id;
        Salesman = salesman;
        Date = date;
        Client = client;
        Action = action;
        ReferenceID = referenceID;
    }

    public void setClient(CLIENT_CLASS client) {
        this.client = client;
    }

    public CLIENT_CLASS getClient() {
        return client;
    }
}
