package com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.CLIENT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.Client;

import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.HOLDER> {

    List<CLIENT_CLASS> list ;

    public ClientsAdapter(List<CLIENT_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public ClientsAdapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_clients_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsAdapter.HOLDER holder, int position) {
        holder.name.setText(list.get(position).ClientName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), Client.class);
                i.putExtra("ClientID" , list.get(position).id );
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView name ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.clientName);
        }
    }
}
