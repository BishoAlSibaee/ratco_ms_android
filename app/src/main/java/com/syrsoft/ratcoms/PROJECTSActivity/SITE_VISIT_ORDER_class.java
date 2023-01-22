package com.syrsoft.ratcoms.PROJECTSActivity;

public class SITE_VISIT_ORDER_class {

    public int id ;
    public int SalesMan ;
    public int ForwardedTo ;
    public String VisitReason ;
    public String ProjectName ;
    public String ResponsibleName ;
    public String ResponsibleMobile ;
    public double LA ;
    public double LO ;
    public String Notes ;
    public String Date ;
    public String VisitDate ;
    public String VisitTime ;
    public String VisitResult;
    public String VisitNotes ;
    public String DoneDate ;
    public String DoneTime ;
    public int Status ;

    public SITE_VISIT_ORDER_class(int id, int salesMan, int forwardedTo, String visitReason, String projectName, String responsibleName, String responsibleMobile, double LA, double LO, String notes, String date, String visitDate, String visitTime, String visitResult, String visitNotes, String doneDate, String doneTime,int status) {
        this.id = id;
        SalesMan = salesMan;
        ForwardedTo = forwardedTo;
        VisitReason = visitReason;
        ProjectName = projectName;
        ResponsibleName = responsibleName;
        ResponsibleMobile = responsibleMobile;
        this.LA = LA;
        this.LO = LO;
        Notes = notes;
        Date = date;
        VisitDate = visitDate;
        VisitTime = visitTime;
        VisitResult = visitResult;
        VisitNotes = visitNotes;
        DoneDate = doneDate;
        DoneTime = doneTime;
        Status = status ;
    }
}
