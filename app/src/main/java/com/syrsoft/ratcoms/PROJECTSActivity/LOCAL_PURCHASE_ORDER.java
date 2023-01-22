package com.syrsoft.ratcoms.PROJECTSActivity;

public class LOCAL_PURCHASE_ORDER {

    public int id ;
    public int ProjectID ;
    public String ProjectName ;
    public int Supmitter ;
    public int AcceptedQID ;
    public int Acc1ID ;
    public String Acc1Note ;
    public int Acc1Status ;
    public int Acc2ID ;
    public String Acc2Note ;
    public int Acc2Status ;
    public int Acc3ID ;
    public String Acc3Note ;
    public int Acc3Status ;
    public int Acc4ID ;
    public String Acc4Note ;
    public int Acc4Status ;
    public int Acc5ID ;
    public String Acc5Note ;
    public int Acc5Status ;
    public int Status ;
    public String Notes ;

    public LOCAL_PURCHASE_ORDER(int id, int projectID, String projectName,int supmitter, int acceptedQID, int acc1ID, String acc1Note, int acc1Status, int acc2ID, String acc2Note, int acc2Status, int acc3ID, String acc3Note, int acc3Status, int acc4ID, String acc4Note, int acc4Status, int acc5ID, String acc5Note, int acc5Status, int status, String notes) {
        this.id = id;
        ProjectID = projectID;
        ProjectName = projectName;
        this.Supmitter = supmitter;
        AcceptedQID = acceptedQID;
        Acc1ID = acc1ID;
        Acc1Note = acc1Note;
        Acc1Status = acc1Status;
        Acc2ID = acc2ID;
        Acc2Note = acc2Note;
        Acc2Status = acc2Status;
        Acc3ID = acc3ID;
        Acc3Note = acc3Note;
        Acc3Status = acc3Status;
        Acc4ID = acc4ID;
        Acc4Note = acc4Note;
        Acc4Status = acc4Status;
        Acc5ID = acc5ID;
        Acc5Note = acc5Note;
        Acc5Status = acc5Status;
        Status = status;
        Notes = notes;
    }

    public int getId () {
        return id ;
    }

    public String getProjectName () {
        return ProjectName ;
    }

    public boolean getStatus() {
        boolean status = false ;
        if (this.Status == 1) {
            status = true ;
        }
        else {
            status = false ;
        }
        return status ;
    }

    public int getAcc1ID () {
        return Acc1ID ;
    }

    public int getAcc2ID () {
        return Acc2ID ;
    }

    public int getAcc3ID () {
        return Acc3ID ;
    }

    public int getAcc4ID () {
        return Acc4ID ;
    }

    public int getAcc5ID () {
        return Acc5ID ;
    }

    public String getAcc1Res() {
        String res = "";
        if (Acc1Status == 0) {
            res = "pending" ;
        }
        else if (Acc1Status == 1) {
            res = "accepted" ;
        }
        else if (Acc1Status == 2) {
            res = "rejected" ;
        }
        return res ;
    }

    public String getAcc2Res() {
        String res = "";
        if (Acc2Status == 0) {
            res = "pending" ;
        }
        else if (Acc2Status == 1) {
            res = "accepted" ;
        }
        else if (Acc2Status == 2) {
            res = "rejected" ;
        }
        return res ;
    }

    public String getAcc3Res() {
        String res = "";
        if (Acc3Status == 0) {
            res = "pending" ;
        }
        else if (Acc3Status == 1) {
            res = "accepted" ;
        }
        else if (Acc3Status == 2) {
            res = "rejected" ;
        }
        return res ;
    }

    public String getAcc4Res() {
        String res = "";
        if (Acc4Status == 0) {
            res = "pending" ;
        }
        else if (Acc4Status == 1) {
            res = "accepted" ;
        }
        else if (Acc4Status == 2) {
            res = "rejected" ;
        }
        return res ;
    }

    public String getAcc5Res() {
        String res = "";
        if (Acc5Status == 0) {
            res = "pending" ;
        }
        else if (Acc5Status == 1) {
            res = "accepted" ;
        }
        else if (Acc5Status == 2) {
            res = "rejected" ;
        }
        return res ;
    }
}
