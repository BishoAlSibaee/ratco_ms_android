package com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.PROJECTSActivity.PURCHASE_ORDER_QUOTATION;
import com.syrsoft.ratcoms.PROJECTSActivity.VeiwPurchaseOrder;
import com.syrsoft.ratcoms.R;

import java.util.List;

public class PurchaseQuotations_Adapter extends RecyclerView.Adapter<PurchaseQuotations_Adapter.HOLDER> {

    List<PURCHASE_ORDER_QUOTATION> list ;

    public PurchaseQuotations_Adapter(List<PURCHASE_ORDER_QUOTATION> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public PurchaseQuotations_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_purchase_quotation_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseQuotations_Adapter.HOLDER holder, int position) {
        holder.Name.setText(list.get(position).getSupplierName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ViewSource",VeiwPurchaseOrder.Source);
                VeiwPurchaseOrder.Quotation = list.get(position);
                VeiwPurchaseOrder.SupplierNameTV.setText(list.get(position).getSupplierName());
                PurchaseOrderQuotationFile_Adapter adapter = new PurchaseOrderQuotationFile_Adapter(VeiwPurchaseOrder.Files.get(position));
                VeiwPurchaseOrder.FilesRecycler.setAdapter(adapter);
                if (VeiwPurchaseOrder.Source.equals("com.syrsoft.ratcoms.PROJECTSActivity.AcceptLocalPurchaseOrder")) {
                    if (VeiwPurchaseOrder.ORDER.getStatus()) {
                        VeiwPurchaseOrder.ApproveRejectLayout.setVisibility(View.GONE);
                    } else {
                        VeiwPurchaseOrder.ApproveRejectLayout.setVisibility(View.VISIBLE);
                    }
                }
                else if (VeiwPurchaseOrder.Source.equals("com.syrsoft.ratcoms.PROJECTSActivity.MyLocalPurchaseOrders")) {
                    VeiwPurchaseOrder.ApproveRejectLayout.setVisibility(View.VISIBLE);
                    LinearLayout l = VeiwPurchaseOrder.act.findViewById(R.id.notesLayout);
                    l.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView Name ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.textView121);
        }
    }
}
