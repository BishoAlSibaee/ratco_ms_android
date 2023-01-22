package com.syrsoft.ratcoms.SALESActivities;

public class DBItem {

    public int id ;
    public int StoreId ;
    public String ItemCode ;
    public String Model ;
    public String ItemName ;
    public String ItemNameArabic ;
    public String ItemDesc ;
    public int ManufacturId ;
    public String Manufacture ;
    public int SupplierId ;
    public String Supplier ;
    public int ItemSource ;
    public int Quantity ;
    public int ReservedQuantity ;
    public int RequestedQuantity ;
    public double Price ;
    public double MinPrice ;
    public String Pic ;
    public String Unit ;

    public DBItem(int id, int storeId, String itemCode, String model, String itemName, String itemNameArabic, String itemDesc, int manufacturId, String manufacture, int supplierId, String supplier, int itemSource, int quantity, int reservedQuantity, int requestedQuantity, double price, double minPrice, String pic, String unit) {
        this.id = id;
        StoreId = storeId;
        ItemCode = itemCode;
        Model = model;
        ItemName = itemName;
        ItemNameArabic = itemNameArabic;
        ItemDesc = itemDesc;
        ManufacturId = manufacturId;
        Manufacture = manufacture;
        SupplierId = supplierId;
        Supplier = supplier;
        ItemSource = itemSource;
        Quantity = quantity;
        ReservedQuantity = reservedQuantity;
        RequestedQuantity = requestedQuantity;
        Price = price;
        MinPrice = minPrice;
        Pic = pic;
        Unit = unit;
    }
}
