package com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.PURCHASE_CLASS;
import com.syrsoft.ratcoms.SALESActivities.ViewPurchaseOrderToManager;

import java.util.List;

public class PurchaseOrders_Adapter extends RecyclerView.Adapter<PurchaseOrders_Adapter.HOLDER> implements View.OnClickListener {
    List<PURCHASE_CLASS> list;


    public PurchaseOrders_Adapter(List<PURCHASE_CLASS> list) {
        this.list = list;
    }

    @Override
    public void onClick(View v) {
    }

    @NonNull
    @Override
    public PurchaseOrders_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_purchase_unit, parent, false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseOrders_Adapter.HOLDER holder, int position) {
        holder.txtCN.setText(list.get(position).Client_Name);
        holder.txtPN.setText(list.get(position).Project_Name);
        holder.txtSM.setText(MyApp.getNameSalesMan(list.get(position).salesman));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ViewPurchaseOrderToManager.class);
                i.putExtra("index", position);
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView txtCN, txtPN, txtSM;

        public HOLDER(@NonNull View itemView) {
            super(itemView);
            txtCN = itemView.findViewById(R.id.txtCN);
            txtPN = itemView.findViewById(R.id.txtPN);
            txtSM = itemView.findViewById(R.id.txtSM);

        }
    }
}
