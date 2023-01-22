package com.syrsoft.ratcoms.Management;

public class MaintenanceResponsible {

    public int id ;
    public int BranchID ;
    public String BranchName ;
    public int DepartmentID ;
    public String DepartmentName ;
    public int EmpID ;
    public String EmpName ;

    public MaintenanceResponsible(int id, int branchID, String branchName, int departmentID, String departmentName, int empID, String empName) {
        this.id = id;
        BranchID = branchID;
        BranchName = branchName;
        DepartmentID = departmentID;
        DepartmentName = departmentName;
        EmpID = empID;
        EmpName = empName;
    }
}
