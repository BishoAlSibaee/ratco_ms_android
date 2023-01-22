package com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.CONTRACT_ITEMS_CLASS;

import java.util.ArrayList;
import java.util.List;

public class Items_Adapter extends RecyclerView.Adapter<Items_Adapter.HOLDER> {

    List<CONTRACT_ITEMS_CLASS> list = new ArrayList<CONTRACT_ITEMS_CLASS>();

    public Items_Adapter(List<CONTRACT_ITEMS_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Items_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull Items_Adapter.HOLDER holder, int position) {
        holder.name.setText(list.get(position).ItemName);
        holder.quantity.setText(String.valueOf(list.get(position).Quantity));
        holder.price.setText(String.valueOf(list.get(position).Price));
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView name , quantity , price ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.ViewItem_Name);
            quantity = (TextView) itemView.findViewById(R.id.ViewItem_Quantity);
            price = (TextView) itemView.findViewById(R.id.ViewItem_Price);
        }
    }
}
