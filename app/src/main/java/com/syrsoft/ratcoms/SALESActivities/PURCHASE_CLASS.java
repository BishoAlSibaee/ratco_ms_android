package com.syrsoft.ratcoms.SALESActivities;

import java.util.List;

public class PURCHASE_CLASS {
    public int id ;
    public int Project_id ;
    public String Project_Name ;
    public int Client_id;
    public String Client_Name ;
    public int salesman ;
    public String date ;
    public String DeliveryDate;
    public String SalesManagerAccept ;
    public String SalesManagerAcceptDate ;
    public String ImportManagerAccept;
    public String ImportManagerAcceptDate ;
    public String OrderStatus ;
    public String OrderDate;
    public String EcpectedDelevaryDate ;
    public String ReceiveStatus;
    public String ReceiveDate;
    public List<PURCHASE_UPDATE_CLASS> listPurchaseUpdate;

    public PURCHASE_CLASS(int id, int project_id, String project_Name, int client_id, String client_Name, int salesman, String date, String deliveryDate, String salesManagerAccept, String salesManagerAcceptDate, String importManagerAccept, String importManagerAcceptDate, String orderStatus, String orderDate, String ecpectedDelevaryDate, String receiveStatus, String receiveDate) {
        this.id = id;
        Project_id = project_id;
        Project_Name = project_Name;
        Client_id = client_id;
        Client_Name = client_Name;
        this.salesman = salesman;
        this.date = date;
        DeliveryDate = deliveryDate;
        SalesManagerAccept = salesManagerAccept;
        SalesManagerAcceptDate = salesManagerAcceptDate;
        ImportManagerAccept = importManagerAccept;
        ImportManagerAcceptDate = importManagerAcceptDate;
        OrderStatus = orderStatus;
        OrderDate = orderDate;
        EcpectedDelevaryDate = ecpectedDelevaryDate;
        ReceiveStatus = receiveStatus;
        ReceiveDate = receiveDate;
    }
}