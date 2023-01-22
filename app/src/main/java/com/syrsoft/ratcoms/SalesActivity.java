package com.syrsoft.ratcoms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.syrsoft.ratcoms.SALESActivities.AddNewClient;
import com.syrsoft.ratcoms.SALESActivities.AddNewProjectContract;
import com.syrsoft.ratcoms.SALESActivities.Catalogs;
import com.syrsoft.ratcoms.SALESActivities.ClientVisitReport;
import com.syrsoft.ratcoms.SALESActivities.Clients;
import com.syrsoft.ratcoms.SALESActivities.DataSheets;
import com.syrsoft.ratcoms.SALESActivities.MyVisitReports;
import com.syrsoft.ratcoms.SALESActivities.ViewMySalesProjectContracts;
import com.syrsoft.ratcoms.SALESActivities.ViewMyStaffVisitsReport;

public class SalesActivity extends AppCompatActivity {

    Activity act ;
    Button viewMyStaffVisits , addNewProjectContract , addClientVisit , ViewMyClientVisits,AddClient,myContracts ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_activity);
        setActivity();
    }

    void setActivity () {
        act = this ;
        viewMyStaffVisits = (Button) findViewById(R.id.ViewMyStaffVisits);
        addNewProjectContract = (Button) findViewById(R.id.AddNewProjectContract);
        addClientVisit = (Button) findViewById(R.id.addClientVisitReport);
        ViewMyClientVisits = (Button) findViewById(R.id.ViewMyClientVisitReports);
        AddClient = (Button) findViewById(R.id.addNewClient);
        myContracts = (Button) findViewById(R.id.ViewMyProjectContracts);
        setButtonsView();
//        if (MyApp.db.getUser().JobTitle.equals("SalesMan")) {
//            viewMyStaffVisits.setVisibility(View.GONE);
//            addNewProjectContract.setVisibility(View.GONE);
//        }
//        if (MyApp.db.getUser().JobTitle.equals("Programmer")) {
//            addNewProjectContract.setVisibility(View.VISIBLE);
//            viewMyStaffVisits.setVisibility(View.VISIBLE);
//        }
//        else {
//            addNewProjectContract.setVisibility(View.GONE);
//        }
    }

    public void goToClientVisitReport(View view) {
        Intent i = new Intent(act, ClientVisitReport.class);
        startActivity(i);
    }

    public void goToMyVisitReports(View view) {
        Intent i = new Intent(act, MyVisitReports.class);
        startActivity(i);
    }

    public void goToAddNewClientActivity(View view) {
        Intent i = new Intent(act, AddNewClient.class);
        startActivity(i);
    }

    public void goToMyStaffVisitsReport(View view) {

        Intent i = new Intent(act, ViewMyStaffVisitsReport.class) ;
        startActivity(i);
    }

    public void goToAddNewProject(View view) {
        Intent i = new Intent(act, AddNewProjectContract.class);
        startActivity(i);
    }

    public void goToViewMyContractProjects(View view) {
        Intent i = new Intent(act, ViewMySalesProjectContracts.class);
        startActivity(i);
    }

    void setButtonsView() {
        myContracts.setVisibility(View.GONE);
        AddClient.setVisibility(View.GONE);
        ViewMyClientVisits.setVisibility(View.GONE);
        addClientVisit.setVisibility(View.GONE);
        addNewProjectContract.setVisibility(View.GONE);
        viewMyStaffVisits.setVisibility(View.GONE);
        if (MyApp.db.getUser().JobTitle.equals("SalesMan")) {
            myContracts.setVisibility(View.VISIBLE);
            AddClient.setVisibility(View.VISIBLE);
            ViewMyClientVisits.setVisibility(View.VISIBLE);
            addClientVisit.setVisibility(View.VISIBLE);
            addNewProjectContract.setVisibility(View.VISIBLE);
        }
        else if (MyApp.db.getUser().JobTitle.equals("Sales Manager") || MyApp.db.getUser().JobTitle.equals("Manager") || MyApp.db.getUser().JobTitle.equals("Branch Manager") || MyApp.db.getUser().JobTitle.equals("Programmer")) {
            myContracts.setVisibility(View.VISIBLE);
            AddClient.setVisibility(View.VISIBLE);
            ViewMyClientVisits.setVisibility(View.VISIBLE);
            addClientVisit.setVisibility(View.VISIBLE);
            addNewProjectContract.setVisibility(View.VISIBLE);
            viewMyStaffVisits.setVisibility(View.VISIBLE);
        }
    }

    public void goToMyClientsActivity(View view) {
        Intent i = new Intent(act, Clients.class);
        startActivity(i);
    }

    public void goToDataSheet(View view) {
        Intent i = new Intent(act, DataSheets.class);
        startActivity(i);
    }

    public void goToCatalogs(View view) {
        Intent i = new Intent(act, Catalogs.class);
        startActivity(i);
    }
}