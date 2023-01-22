package com.syrsoft.ratcoms;

public class ADS_CLASS
{
    int id ;
    String Title;
    String Message;
    String ImageLink;
    String FileLink;
    String Date;

    public ADS_CLASS(int id ,String title, String text, String img , String number) {
        this.Title = title;
        this.Message = text;
        this.ImageLink = img;
        this.FileLink = number ;
    }

    public ADS_CLASS(int id ,String title, String message, String img , String fileLink , String date) {
        this.id = id ;
        this.Title = title;
        this.Message = message;
        this.ImageLink = img;
        this.FileLink = fileLink ;
        this.Date = date ;
    }
}
