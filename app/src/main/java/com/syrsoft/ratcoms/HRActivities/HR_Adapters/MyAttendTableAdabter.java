package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.HRActivities.ATTENDANCE_CLASS;
import com.syrsoft.ratcoms.R;

import java.util.List;

public class MyAttendTableAdabter extends RecyclerView.Adapter<MyAttendTableAdabter.HOLDER> {
    List<ATTENDANCE_CLASS> list;


    public MyAttendTableAdabter(List<ATTENDANCE_CLASS> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public MyAttendTableAdabter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_attend_table_unit, parent, false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAttendTableAdabter.HOLDER holder, int position) {
        // holder.txtEmpID.setText("EmpID : " + list.get(position).EmpID);
        holder.txtDate.setText("Date : " + list.get(position).Date);
        holder.txtTime.setText("Time : " + list.get(position).Time);
        holder.operation.setImageResource(list.get(position).getOperation());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime;
        ImageView operation;

        public HOLDER(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            operation = itemView.findViewById(R.id.img);
        }
    }
}
