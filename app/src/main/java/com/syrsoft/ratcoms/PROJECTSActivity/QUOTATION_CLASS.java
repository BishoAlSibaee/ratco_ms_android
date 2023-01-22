package com.syrsoft.ratcoms.PROJECTSActivity;

import java.util.List;

public class QUOTATION_CLASS {

    int id ;
    SUPPLIER_CLASS supplier ;
    List<String> files ;

    public QUOTATION_CLASS(int id, SUPPLIER_CLASS supplier, List<String> files) {
        this.id = id;
        this.supplier = supplier;
        this.files = files;
    }
}
