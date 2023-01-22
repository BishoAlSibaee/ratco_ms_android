package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.HRActivities.SendSalaryReports;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import org.w3c.dom.Text;

import java.util.List;

public class EmployeesSelect_Adapter extends RecyclerView.Adapter<EmployeesSelect_Adapter.HOLDER> {

    List<USER> list ;

    public EmployeesSelect_Adapter(List<USER> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public EmployeesSelect_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_user_selection_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesSelect_Adapter.HOLDER holder, int position) {
        holder.name.setText(list.get(position).FirstName+" "+list.get(position).LastName);
        holder.chck.setOnCheckedChangeListener(null);
        holder.chck.setChecked(SendSalaryReports.SelectedArray[position]);
        holder.chck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SendSalaryReports.SelectedUsers.add(list.get(position));
                    SendSalaryReports.SelectedAdapter.notifyDataSetChanged();
                    SendSalaryReports.SelectedArray[position] = true ;
                }
                else {
                    if (USER.searchUserByJobNumber(SendSalaryReports.SelectedUsers,list.get(position).JobNumber) != null) {
                        SendSalaryReports.SelectedUsers.remove(list.get(position));
                        SendSalaryReports.SelectedAdapter.notifyDataSetChanged();
                    }
                    SendSalaryReports.SelectedArray[position] = false ;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView name ;
        CheckBox chck ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView102);
            chck = (CheckBox) itemView.findViewById(R.id.checkBox3);
        }
    }
}
