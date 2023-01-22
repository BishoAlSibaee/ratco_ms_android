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
import com.syrsoft.ratcoms.SALESActivities.PROJECT_CONTRACT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.ShowVisitsOnMap;
import com.syrsoft.ratcoms.SALESActivities.ViewContract;

import java.util.ArrayList;
import java.util.List;

public class Contracts_Adapter extends RecyclerView.Adapter<Contracts_Adapter.HOLDER> {

    List<PROJECT_CONTRACT_CLASS> list = new ArrayList<PROJECT_CONTRACT_CLASS>();

    public Contracts_Adapter(List<PROJECT_CONTRACT_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Contracts_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_contract_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull Contracts_Adapter.HOLDER holder, int position) {
        holder.ProjectName.setText(list.get(position).ProjectName);
        holder.ProjectResponsible.setText(list.get(position).ProjectManager);
        if (list.get(position).CLIENT != null ) {
            holder.ClientName.setText(list.get(position).CLIENT.ClientName);
        }
        holder.ShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), ShowVisitsOnMap.class);
                i.putExtra("LA",list.get(position).LA);
                i.putExtra("LO" , list.get(position).LO);
                i.putExtra("ClientName" , list.get(position).ProjectName);
                holder.itemView.getContext().startActivity(i);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), ViewContract.class);
                i.putExtra("ContractID",list.get(position).id);
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
            TextView ProjectName , ClientName , ProjectResponsible ;
            Button ShowOnMap ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            ProjectName = (TextView) itemView.findViewById(R.id.ContractUnit_ProjectName);
            ClientName = (TextView) itemView.findViewById(R.id.ContractUnit_ClientName);
            ProjectResponsible = (TextView) itemView.findViewById(R.id.ContractUnit_ProjectManager);
            ShowOnMap = (Button) itemView.findViewById(R.id.ContractUnit_showOnMap);
        }
    }
}
