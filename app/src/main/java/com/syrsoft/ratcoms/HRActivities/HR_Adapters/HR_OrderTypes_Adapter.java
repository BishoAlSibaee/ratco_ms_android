package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE;
import com.syrsoft.ratcoms.HRActivities.ManageOrdersAuthority;
import com.syrsoft.ratcoms.R ;
import java.util.List;

public class HR_OrderTypes_Adapter extends RecyclerView.Adapter<HR_OrderTypes_Adapter.HOLDER> {

    List<HR_ORDER_TYPE> list ;
    String getOrderAutheUrl = "";
    Context c ;

    public HR_OrderTypes_Adapter(List<HR_ORDER_TYPE> list ) {
        this.list = list;

    }

    @NonNull
    @Override
    public HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_order_types_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HOLDER holder, int position) {
        holder.ar.setText(list.get(position).ArabicName);
        holder.en.setText(list.get(position).HROrderName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageOrdersAuthority.selectedOrdertypeLayout.setVisibility(View.VISIBLE);
                ManageOrdersAuthority.slectedOrdertypeName.setText(list.get(position).HROrderName +" "+list.get(position).ArabicName);
                ManageOrdersAuthority.SelectedOrderType = list.get(position) ;
                ManageOrdersAuthority.makeCurrentAuthsList() ;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView en , ar ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            en = (TextView) itemView.findViewById(R.id.HROrderTypes_en);
            ar = (TextView) itemView.findViewById(R.id.HROrderTypes_ar);
        }
    }



}
