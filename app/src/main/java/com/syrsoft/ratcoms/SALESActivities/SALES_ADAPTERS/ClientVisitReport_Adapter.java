package com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.CLIENT_VISIT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.ShowVisitsOnMap;
import com.syrsoft.ratcoms.SALESActivities.ViewMyVisitDetailes;

import java.util.ArrayList;
import java.util.List;

public class ClientVisitReport_Adapter extends RecyclerView.Adapter<ClientVisitReport_Adapter.HOLDER> {

    List<CLIENT_VISIT_CLASS> list = new ArrayList<CLIENT_VISIT_CLASS>();

    public ClientVisitReport_Adapter(List<CLIENT_VISIT_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public ClientVisitReport_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_client_visit_report_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientVisitReport_Adapter.HOLDER holder, int position) {
        holder.name.setText(list.get(position).ClientName);
        holder.time.setText(list.get(position).Date+" "+list.get(position).Time);
        holder.responsible.setText(list.get(position).Responsible);
        holder.showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), ShowVisitsOnMap.class);
                i.putExtra("LA",list.get(position).LA);
                i.putExtra("LO" , list.get(position).LO);
                i.putExtra("ClientName" , list.get(position).ClientName);
                holder.itemView.getContext().startActivity(i);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), ViewMyVisitDetailes.class);
                i.putExtra("ItemId",list.get(position).ClientID);
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView name , time , responsible ;
        Button showOnMap ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.clientvisitreportUnit_clientName);
            time = (TextView)itemView.findViewById(R.id.clientvisitreportUnit_time);
            responsible = (TextView) itemView.findViewById(R.id.clientvisitreportUnit_responsible);
            showOnMap = (Button) itemView.findViewById(R.id.clientvisitreportUnit_mapBtn);
        }
    }
}
