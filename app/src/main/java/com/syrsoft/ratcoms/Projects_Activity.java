package com.syrsoft.ratcoms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.PROJECTSActivity.AcceptLocalPurchaseOrder;
import com.syrsoft.ratcoms.PROJECTSActivity.AddMaintenanceOrder;
import com.syrsoft.ratcoms.PROJECTSActivity.AddPurchaseOrder;
import com.syrsoft.ratcoms.PROJECTSActivity.AddSiteVisitOrder;
import com.syrsoft.ratcoms.PROJECTSActivity.MaintenanceOrders;
import com.syrsoft.ratcoms.PROJECTSActivity.MyLocalPurchaseOrders;
import com.syrsoft.ratcoms.PROJECTSActivity.MySiteVisitOrders;
import com.syrsoft.ratcoms.PROJECTSActivity.SiteVisitOrders_ForProjects;
import com.syrsoft.ratcoms.PROJECTSActivity.ViewMyMaintenanceOrders;
import com.syrsoft.ratcoms.SALESActivities.PROJECT_CONTRACT_CLASS;

import java.util.ArrayList;
import java.util.List;

public class Projects_Activity extends AppCompatActivity {

    Activity act ;
    String[] SearchByArr ;
    PROJECT_CONTRACT_CLASS CONTRACT ;
    String searchProjectUrl = MyApp.MainUrl + "searchProject.php" ;
    RequestQueue Q ;
    List<PROJECT_CONTRACT_CLASS> ContractsResult ;
    Button addMaintenanceOrder , myMaintenanceOrders , addSiteVisitOrder , addPurchaseOrder , maintenanceOrders ,mySiteVisitOrders , SiteVisitOrders , MyPurchaseOrders ;
    FrameLayout MaintenanceOrdersFL , ApprovePurchaseOrders ;
    static CardView MaintenanceOrdersCard , SiteVisitOrdersCard , PurchaseOrdersCard ;
    static TextView MaintenanceOrdersCount , SiteVisitOrdersCount , PurchaseOrdersCount ;
    public static boolean isRunning ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_activity);
        isRunning = true ;
        setActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCounters();
    }

    void setActivity() {
        act = this ;
        Q = Volley.newRequestQueue(act);
        SearchByArr = getResources().getStringArray(R.array.searchProjectByArray);
        ContractsResult = new ArrayList<PROJECT_CONTRACT_CLASS>();
        addMaintenanceOrder = (Button) findViewById(R.id.addMaintenanceOrder);
        myMaintenanceOrders = (Button) findViewById(R.id.myMaintenanceOrders);
        addSiteVisitOrder = (Button) findViewById(R.id.addSiteVisitORder);
        addPurchaseOrder = (Button) findViewById(R.id.addPuchaseOrder);
        maintenanceOrders = (Button) findViewById(R.id.maintenanceOrders);
        mySiteVisitOrders = (Button) findViewById(R.id.mySiteVisitOrders);
        SiteVisitOrders = (Button) findViewById(R.id.SiteVisitOrders);
        MyPurchaseOrders = (Button) findViewById(R.id.myPurchaseOrders);
        MaintenanceOrdersCard = (CardView) findViewById(R.id.MaintenanceOrdersCounterCard);
        SiteVisitOrdersCard = (CardView) findViewById(R.id.SiteVisitOrdersCounterCard);
        PurchaseOrdersCard = (CardView) findViewById(R.id.SiteVisitOrdersCounterCard4);
        MaintenanceOrdersCount = (TextView) findViewById(R.id.MaintenanceOrdersCounterText);
        SiteVisitOrdersCount = (TextView) findViewById(R.id.SiteVisitOrdersCounterText);
        PurchaseOrdersCount = (TextView) findViewById(R.id.SiteVisitOrdersCounterText4);
        MaintenanceOrdersFL = (FrameLayout) findViewById(R.id.MaintenanceOrders_Framelayout);
        ApprovePurchaseOrders = (FrameLayout) findViewById(R.id.SiteVisit_Framelayout4);
        viewHideBtns();
        setCounters();
    }

    public static void setCounters () {
        if (MyApp.ProjectsCounters[0] <= 0 ) {
            MaintenanceOrdersCard.setVisibility(View.GONE);
        }
        else {
            MaintenanceOrdersCard.setVisibility(View.VISIBLE);
            MaintenanceOrdersCount.setText(String.valueOf(MyApp.ProjectsCounters[0]));
        }
        if (MyApp.ProjectsCounters[1] <= 0 ) {
            SiteVisitOrdersCard.setVisibility(View.GONE);
        }
        else {
            SiteVisitOrdersCard.setVisibility(View.VISIBLE);
            SiteVisitOrdersCount.setText(MyApp.ProjectsCounters[1]+"");
        }
        if (MyApp.ProjectsCounters[2] <= 0) {
            PurchaseOrdersCard.setVisibility(View.GONE);
        }
        else {
            PurchaseOrdersCard.setVisibility(View.VISIBLE);
            PurchaseOrdersCount.setText(String.valueOf(MyApp.ProjectsCounters[2]));
        }
    }

    public void goToAddMaintenanceOrder(View view) {

        Intent i = new Intent(act, AddMaintenanceOrder.class);
        startActivity(i);
    }

    public void goToAddLocationVisitRequest(View view) {
        Intent i = new Intent(act, AddSiteVisitOrder.class);
        startActivity(i);
    }

    public void goToAddPurchaseOrder(View view) {
        Intent i = new Intent(act, AddPurchaseOrder.class);
        startActivity(i);
    }

    public void goToMyMaintenanceOrders(View view) {

        Intent i = new Intent(act, ViewMyMaintenanceOrders.class);
        startActivity(i);
    }

    void viewHideBtns() {
        addMaintenanceOrder.setVisibility(View.GONE);
        myMaintenanceOrders.setVisibility(View.GONE);
        addSiteVisitOrder.setVisibility(View.GONE);
        addPurchaseOrder.setVisibility(View.GONE);
        MaintenanceOrdersFL.setVisibility(View.GONE);
        mySiteVisitOrders.setVisibility(View.GONE);
        SiteVisitOrders.setVisibility(View.GONE);
        ApprovePurchaseOrders.setVisibility(View.GONE);
        MyPurchaseOrders.setVisibility(View.GONE);
        for (int i=0;i < MyApp.MyUser.MyPermissions.size() ; i++) {
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 27) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    addMaintenanceOrder.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 28) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    myMaintenanceOrders.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 29) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    addSiteVisitOrder.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 30) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    mySiteVisitOrders.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 31) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    SiteVisitOrders.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 32) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    addPurchaseOrder.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 34) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    ApprovePurchaseOrders.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 35) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    MyPurchaseOrders.setVisibility(View.VISIBLE);
                }
            }
            if (MyApp.MyUser.MyPermissions.get(i).getId() == 36) {
                if (MyApp.MyUser.MyPermissions.get(i).getResult()) {
                    MaintenanceOrdersFL.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void goToMaintenanceOrders(View view) {
        Intent i = new Intent(act, MaintenanceOrders.class);
        startActivity(i);
    }

    public void goToMyMySiteVisitOrders(View view) {
        Intent i = new Intent(act, MySiteVisitOrders.class);
        startActivity(i);
    }

    public void goToSiteVisitOrders(View view) {
        Intent i = new Intent(act, SiteVisitOrders_ForProjects.class);
        startActivity(i);
    }

    public void goToApprovePurchaseOrders(View view) {
        Intent i = new Intent(act, AcceptLocalPurchaseOrder.class);
        startActivity(i);
    }

    public void goToMyPurchaseOrders(View view) {
        Intent i = new Intent(act, MyLocalPurchaseOrders.class);
        startActivity(i);
    }
}