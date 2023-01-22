package com.syrsoft.ratcoms.PROJECTSActivity;

public class PURCHASE_ORDER_QUOTATION {

    int id ;
    int PurchaseID ;
    int SupplierID ;
    String SupplierName ;
    int ProjectID ;
    int FilesCount ;
    int Accepted ;

    public PURCHASE_ORDER_QUOTATION(int id, int purchaseID, int supplierID, String supplierName, int projectID, int filesCount, int accepted) {
        this.id = id;
        PurchaseID = purchaseID;
        SupplierID = supplierID;
        SupplierName = supplierName;
        ProjectID = projectID;
        FilesCount = filesCount;
        Accepted = accepted;
    }

    public String getSupplierName() {
        return SupplierName;
    }
}
