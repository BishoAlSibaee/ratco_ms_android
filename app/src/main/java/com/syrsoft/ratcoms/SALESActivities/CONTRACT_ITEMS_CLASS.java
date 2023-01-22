package com.syrsoft.ratcoms.SALESActivities;

public class CONTRACT_ITEMS_CLASS {

    public int id ;
    public int ProjectID ;
    public String ItemName ;
    public int Quantity ;
    public double Price ;

    public CONTRACT_ITEMS_CLASS(int id,int ProjectID,String itemName, int quantity, double price) {
        this.id = id ;
        this.ProjectID = ProjectID ;
        ItemName = itemName;
        Quantity = quantity;
        Price = price;
    }
}
