package com.syrsoft.ratcoms.Management;

import com.syrsoft.ratcoms.USER;

import java.util.List;

public class MaintenanceDepartment extends Department {

    MaintenanceResponsible Users ;

    public MaintenanceDepartment(int id, String department, int maintenance) {
        super(id, department, maintenance);
    }

    public void setUsers(MaintenanceResponsible users) {
        Users = users;
    }

    public MaintenanceResponsible getUsers() {
        return Users;
    }
}
