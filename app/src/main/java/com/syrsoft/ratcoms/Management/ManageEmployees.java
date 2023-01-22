package com.syrsoft.ratcoms.Management;

import androidx.appcompat.app.AppCompatActivity;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ManageEmployees extends AppCompatActivity {

    Activity act ;
    public static USER SELECTED_USER ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_manage_employees_activity);
        setActivity();
    }

    void setActivity () {
        act = this ;
    }

    public void goToEditDirectManagerOfEmployee(View view) {
        Intent i = new Intent(act,EditEmployeeDirectManager.class);
        startActivity(i);
    }

    public void goToEditDirectManagerOfEmployees(View view) {
        Intent i = new Intent(act,EditEmployeesDirectManager.class);
        startActivity(i);
    }

    public void goToEditDepartmentManager(View view) {
        Intent i = new Intent(act,EditDepartmentManager.class);
        startActivity(i);
    }

    public void goToMoveEmployee(View view) {
        Intent i = new Intent(act,MoveEmployee.class);
        startActivity(i);
    }

    public void goToAddNewEmployee(View view) {
        Intent i = new Intent(act,AddNewEmployee.class);
        startActivity(i);
    }

    public void gotoManagePassports(View view) {
        Intent i = new Intent(act,RemoveAccount.class);
        startActivity(i);
    }
}