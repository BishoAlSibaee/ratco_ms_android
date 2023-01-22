package com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.PROJECTSActivity.MAINTENANCE_ORDER_CLASS;
import com.syrsoft.ratcoms.PROJECTSActivity.MaintenanceOrder;
import com.syrsoft.ratcoms.PROJECTSActivity.MaintenanceOrder_ForProjects;
import com.syrsoft.ratcoms.PROJECTSActivity.MaintenanceOrders;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.util.List;

public class Maintenance_Order_Adapter_ForProjects extends RecyclerView.Adapter<Maintenance_Order_Adapter_ForProjects.HOLDER> {

    List<MAINTENANCE_ORDER_CLASS> list ;

    public Maintenance_Order_Adapter_ForProjects(List<MAINTENANCE_ORDER_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Maintenance_Order_Adapter_ForProjects.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_maintenance_order_unit,parent,false);
        Maintenance_Order_Adapter_ForProjects.HOLDER holder = new Maintenance_Order_Adapter_ForProjects.HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Maintenance_Order_Adapter_ForProjects.HOLDER holder, @SuppressLint("RecyclerView") int position) {
        holder.pname.setText(list.get(position).ProjectName);
        for (USER u : MyApp.EMPS) {
            if (u.JobNumber == list.get(position).SalesMan) {
                holder.salesman.setText(u.FirstName +" "+u.LastName);
                break;
            }
        }
        holder.date.setText(list.get(position).Date);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), MaintenanceOrder_ForProjects.class);
                i.putExtra("Index",position);
                if (MaintenanceOrders.Forward == 0) {
                    i.putExtra("UnForwarded" , 1) ;
                }
                else {
                    i.putExtra("UnForwarded" , 0) ;
                }
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView pname , salesman , date ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            pname = (TextView) itemView.findViewById(R.id.maintenanceOrderUnit_projectName);
            salesman = (TextView) itemView.findViewById(R.id.maintenanceOrderUnit_salesman);
            date = (TextView) itemView.findViewById(R.id.maintenanceOrderUnit_date);
        }
    }
}
