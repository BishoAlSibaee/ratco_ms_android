package com.syrsoft.ratcoms.Management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.Management.MaintenanceDepartment;
import com.syrsoft.ratcoms.Management.MaintenanceResponsible;
import com.syrsoft.ratcoms.Management.SetMaintenanceResponsibles;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.util.List;

public class DepartmentMaintenanceResponsible_Adapter extends RecyclerView.Adapter<DepartmentMaintenanceResponsible_Adapter.HOLDER> {

    List<MaintenanceDepartment> list ;

    public DepartmentMaintenanceResponsible_Adapter(List<MaintenanceDepartment> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public DepartmentMaintenanceResponsible_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout._managementdepartment_maintenance_responsible_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentMaintenanceResponsible_Adapter.HOLDER holder, int position) {
        holder.department.setText(list.get(position).Department);
        if (list.get(position).getUsers() == null ) {
            String[] emps = new String[MyApp.EMPS.size()];
            for (int i=0;i<MyApp.EMPS.size();i++) {
                emps[i] = MyApp.EMPS.get(i).FirstName + " "+MyApp.EMPS.get(i).LastName ;
            }
            ArrayAdapter<String> ad = new ArrayAdapter<String>(holder.itemView.getContext(),R.layout.spinner_item,emps);
            holder.emps.setAdapter(ad);
        }
        else {
            for (int i=0;i<MyApp.EMPS.size();i++) {
                if (MyApp.EMPS.get(i).id == list.get(position).getUsers().EmpID) {
                    String[] emps = new String[MyApp.EMPS.size()];
                    for (int j=0;j<MyApp.EMPS.size();j++) {
                        emps[j] = MyApp.EMPS.get(j).FirstName + " "+MyApp.EMPS.get(j).LastName ;
                    }
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(holder.itemView.getContext(),R.layout.spinner_item,emps);
                    holder.emps.setAdapter(ad);
                    holder.emps.setSelection(i);
                    break;
                }
            }
        }
        holder.emps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionn, long id) {
                list.get(position).setUsers(new MaintenanceResponsible(1, SetMaintenanceResponsibles.SelectedBranch.id,SetMaintenanceResponsibles.SelectedBranch.BranchName,list.get(position).id,list.get(position).Department,MyApp.EMPS.get(positionn).id,MyApp.EMPS.get(positionn).FirstName+" "+MyApp.EMPS.get(positionn).LastName));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView department ;
        Spinner emps ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            department = (TextView) itemView.findViewById(R.id.textView110);
            emps = (Spinner) itemView.findViewById(R.id.spinner4);
        }
    }
}
