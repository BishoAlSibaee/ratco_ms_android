package com.syrsoft.ratcoms.PROJECTSActivity;

public class PurchaseOrderQuotationFILES {

    public int id ;
    public int QuotationID ;
    public int PurchaseID ;
    public String Link ;

    public PurchaseOrderQuotationFILES(int id, int quotationID, int purchaseID, String link) {
        this.id = id;
        QuotationID = quotationID;
        PurchaseID = purchaseID;
        Link = link;
    }
}
