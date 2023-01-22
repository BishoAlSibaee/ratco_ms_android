package com.syrsoft.ratcoms.Management;

import androidx.appcompat.app.AppCompatActivity;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SearchEmployeeDialog;
import com.syrsoft.ratcoms.USER;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RemoveAccount extends AppCompatActivity {

    Activity act ;
    USER SELECTED_USER ;
    TextView EmployeeTV ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_remove_account_activity);
        setActivity();
    }

    void setActivity() {
        act = this ;
        EmployeeTV = findViewById(R.id.textView146);
    }

    public void goToSearchUser(View view) {
        SearchEmployeeDialog D = new SearchEmployeeDialog(act);
        D.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_USER = D.adapter.user ;
                EmployeeTV.setText(SELECTED_USER.FirstName+" "+SELECTED_USER.LastName);
                D.stop();
            }
        });
        D.show();
    }
}