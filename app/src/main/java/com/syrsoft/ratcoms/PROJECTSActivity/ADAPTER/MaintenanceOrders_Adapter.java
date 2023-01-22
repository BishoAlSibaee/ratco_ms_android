package com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER;

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
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.util.List;

public class MaintenanceOrders_Adapter extends RecyclerView.Adapter<MaintenanceOrders_Adapter.HOLDER> {

    List<MAINTENANCE_ORDER_CLASS> list ;

    public MaintenanceOrders_Adapter(List<MAINTENANCE_ORDER_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public MaintenanceOrders_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_maintenance_order_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceOrders_Adapter.HOLDER holder, int position) {
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
                Intent i = new Intent(holder.itemView.getContext(), MaintenanceOrder.class);
                i.putExtra("Index",position);
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
