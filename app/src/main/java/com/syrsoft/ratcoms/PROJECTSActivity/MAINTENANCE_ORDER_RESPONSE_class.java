package com.syrsoft.ratcoms.PROJECTSActivity;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;

public class MAINTENANCE_ORDER_RESPONSE_class {
    public int id ;
    public int MaintenanceID ;
    public int ProjectID ;
    public int ClientID ;
    public String Response ;
    public int Employee ;
    public int SparePartsStatus ;
    public String SpareParts ;
    public int OrderStatus ;
    public String Date ;
    public String Time ;
    public String document1;
    public String document2;
    public String document3;

    public MAINTENANCE_ORDER_RESPONSE_class(int id, int maintenanceID, int projectID, int clientID, String response, int employee, int sparePartsStatus, String spareParts, int orderStatus,String date,String time,String d1,String d2,String d3) {
        this.id = id;
        MaintenanceID = maintenanceID;
        ProjectID = projectID;
        ClientID = clientID;
        Response = response;
        Employee = employee;
        SparePartsStatus = sparePartsStatus;
        SpareParts = spareParts;
        OrderStatus = orderStatus;
        Date = date ;
        Time = time ;
        document1 = d1 ;
        document2 = d2 ;
        document3 = d3 ;
    }

    public String getSpareParts() {
        String res = "" ;
        if (SparePartsStatus == 0 ) {
            res = MyApp.app.getResources().getString(R.string.notRequired);
        }
        else if (SparePartsStatus == 1) {
            res = MyApp.app.getResources().getString(R.string.required);
        }
        return res ;
    }

    public boolean getOrderStatus() {
        boolean res = false ;
        if (OrderStatus == 0 ) {
            res = false ;
        }
        else if (OrderStatus == 1) {
            res = true ;
        }
        return res ;
    }
}
