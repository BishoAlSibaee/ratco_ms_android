package com.syrsoft.ratcoms.HRActivities;

import java.util.List;

public class BONUS {

    public int id ;
    public int JobNumber ;
    public String Name ;
    public int RequesterJobNumber ;
    public String RequesterName ;
    public double BonusAmount ;
    public String RequestDate ;
    public String Notes ;
    public int Status ;
    public String From ;
    public List<Acceptance> Accs ;

    public BONUS(int id, int jobNumber, String name, int requesterJobNumber, String requesterName, double bonusAmount, String requestDate, String notes, int status, String from, List<Acceptance> accs) {
        this.id = id;
        JobNumber = jobNumber;
        Name = name;
        RequesterJobNumber = requesterJobNumber;
        RequesterName = requesterName;
        BonusAmount = bonusAmount;
        RequestDate = requestDate;
        Notes = notes;
        Status = status;
        From = from;
        Accs = accs;
    }
}
