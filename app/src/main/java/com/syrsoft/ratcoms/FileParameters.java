package com.syrsoft.ratcoms;

import android.net.Uri;

public class FileParameters {
    public String pdfName ;
    public Uri pdfUri;

    public FileParameters(String pdfName, Uri pdfUri) {
        this.pdfName = pdfName;
        this.pdfUri = pdfUri;
    }
}
