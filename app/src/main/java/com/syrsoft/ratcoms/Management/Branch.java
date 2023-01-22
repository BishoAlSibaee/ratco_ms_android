package com.syrsoft.ratcoms.Management;

import java.util.ArrayList;
import java.util.List;

public class Branch  {
    public int id ;
    public String BranchName ;
    public String BranchType ;
    public String City ;
    double LA ;
    double LO ;
    List<MaintenanceDepartment> MaintenanceDepartments ;

    public Branch(int id, String branchName, String branchType, String city, double LA, double LO) {
        this.id = id;
        BranchName = branchName;
        BranchType = branchType;
        City = city;
        this.LA = LA;
        this.LO = LO;
        MaintenanceDepartments = new ArrayList<MaintenanceDepartment>();
    }

    public void setMaintenanceDepartments(List<MaintenanceDepartment> maintenanceDepartments) {
        MaintenanceDepartments = maintenanceDepartments;
    }

    public List<MaintenanceDepartment> getMaintenanceDepartments() {
        return MaintenanceDepartments;
    }
}
