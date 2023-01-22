package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.util.List;

public class Employee_Adapter extends RecyclerView.Adapter<Employee_Adapter.HOLDER> {

    List<USER> list;

    public Employee_Adapter(List<USER> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Employee_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Employee_Adapter.HOLDER holder, int position) {
        holder.name.setText(list.get(position).FirstName+" "+list.get(position).LastName);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView name ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView103);
        }
    }
}
