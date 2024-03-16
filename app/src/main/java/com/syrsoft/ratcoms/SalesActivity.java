package com.syrsoft.ratcoms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.syrsoft.ratcoms.SALESActivities.AddNewPurchaseOrder;
import com.syrsoft.ratcoms.SALESActivities.AddNewClient;
import com.syrsoft.ratcoms.SALESActivities.AddNewProjectContract;
import com.syrsoft.ratcoms.SALESActivities.Catalogs;
import com.syrsoft.ratcoms.SALESActivities.ClientVisitReport;
import com.syrsoft.ratcoms.SALESActivities.Clients;
import com.syrsoft.ratcoms.SALESActivities.DataSheets;
import com.syrsoft.ratcoms.SALESActivities.MyVisitReports;
import com.syrsoft.ratcoms.SALESActivities.ViewClientsLocations;
import com.syrsoft.ratcoms.SALESActivities.ViewMySalesProjectContracts;
import com.syrsoft.ratcoms.SALESActivities.ViewMyStaffVisitsReport;
import com.syrsoft.ratcoms.SALESActivities.ViewPurchaseOrders;

public class SalesActivity extends AppCompatActivity {

    Activity act ;
    Button viewMyStaffVisits , addNewProjectContract ,ImportOrders , addClientVisit,AddImportOrder , ViewMyClientVisits,AddClient,myClients,myContracts , btnGoBuyOrder, ButtonViewClients ;

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
        btnGoBuyOrder = (Button) findViewById(R.id.btnGoBuyOrder);
        myClients = findViewById(R.id.myClients);
        ImportOrders = findViewById(R.id.btnViewPurchase);
        ButtonViewClients = findViewById(R.id.ButtonViewClients);
        setButtonsView();
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
        myContracts.setVisibility(View.GONE);//26
        AddClient.setVisibility(View.GONE);//23
        ViewMyClientVisits.setVisibility(View.GONE);//21
        addClientVisit.setVisibility(View.GONE);//20
        addNewProjectContract.setVisibility(View.GONE);//25
        viewMyStaffVisits.setVisibility(View.GONE);//22
        myClients.setVisibility(View.GONE);//24
        btnGoBuyOrder.setVisibility(View.GONE);//42
        ImportOrders.setVisibility(View.GONE);
        ButtonViewClients.setVisibility(View.GONE);//44;
        for (int i=0;i<MyApp.MyUser.MyPermissions.size();i++) {
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 26) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    myContracts.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 23) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    AddClient.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 21) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    ViewMyClientVisits.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 20) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    addClientVisit.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 25) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    addNewProjectContract.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 22) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    viewMyStaffVisits.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 24) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    myClients.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 42) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    btnGoBuyOrder.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 43) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    ImportOrders.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 44) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    ButtonViewClients.setVisibility(View.VISIBLE);
                }
            }
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

    public void goToAddNewBuyOrder(View view ){
        Intent i = new Intent(act, AddNewPurchaseOrder.class);
        startActivity(i);
    }

    public void goToViewPurchase(View view){
        Intent i = new Intent(act, ViewPurchaseOrders.class);
        startActivity(i);
    }

    public void goToViewClients(View view){
        Intent i =new Intent(act, ViewClientsLocations.class);
        startActivity(i);
    }
}