package com.syrsoft.ratcoms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.Management.ManageEmployees;

import java.util.ArrayList;
import java.util.List;
import com.syrsoft.ratcoms.User_Adapter;
import static com.syrsoft.ratcoms.SALESActivities.Search_Item_Dialog.SelectedTV;

public class SearchEmployeeDialog {

    Context C ;
    Dialog D ;
    EditText searchET ;
    RecyclerView ResRecycler ;
    TextView SelectedTV ;
    public Button cancel , select ;
    ProgressBar P ;
    public User_Adapter adapter ;
    List<USER> ResLis ;
    LinearLayoutManager Manager ;
    public USER SELECTED ;

    public SearchEmployeeDialog(Context C) {
        this.C = C ;
        D = new Dialog(this.C);
        D.setContentView(R.layout.search_employee_dialog);
        D.setCancelable(false);
        Window w = D.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        D.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        searchET = D.findViewById(R.id.editTextTextPersonName12);
        ResRecycler = D.findViewById(R.id.resultRecycler);
        SelectedTV = D.findViewById(R.id.textView147);
        cancel = D.findViewById(R.id.button64);
        select = D.findViewById(R.id.button65);
        P = D.findViewById(R.id.progressBar8);
        P.setVisibility(View.INVISIBLE);
        ResLis = new ArrayList<>();
        Manager = new LinearLayoutManager(this.C,RecyclerView.VERTICAL,false);
        ResRecycler.setLayoutManager(Manager);
        setActions();
    }

    void setActions() {
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                P.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("searchRun",s.toString());
                List<USER> res = searchEmployee(MyApp.EMPS,s.toString());
                if (res == null) {
                    ResLis.clear();
                    adapter = new User_Adapter(ResLis,SelectedTV);
                    ResRecycler.setAdapter(adapter);
                    P.setVisibility(View.INVISIBLE);
                }
                else {
                    ResLis = res ;
                    adapter = new User_Adapter(ResLis,SelectedTV);
                    ResRecycler.setAdapter(adapter);
                    P.setVisibility(View.INVISIBLE);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.SELECTED_USER = SELECTED ;
            }
        });
    }


    public void show() {
        if (D != null) {
            D.show();
        }
        else {
            throw new RuntimeException("dialog is null");
        }
    }

    public void stop() {
        D.dismiss();
    }


    public List<USER> searchEmployee(List<USER> list, String name) {
        List<USER> res = new ArrayList<>() ;
        for (int i=0;i<list.size();i++) {
            if (list.get(i).FirstName.contains(name) || list.get(i).LastName.contains(name)) {
                 res.add(list.get(i));
            }
        }
        if (res.size() == 0) {
            return null;
        }
        else {
            return res;
        }
    }

}
