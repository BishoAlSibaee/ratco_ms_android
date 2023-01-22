package com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.PROJECTSActivity.SITE_VISIT_ORDER_class;
import com.syrsoft.ratcoms.PROJECTSActivity.SiteVisitOrder;
import com.syrsoft.ratcoms.PROJECTSActivity.SiteVisitOrder_ForProjects;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.util.List;

public class SiteVisitOrders_Adapter  extends RecyclerView.Adapter<SiteVisitOrders_Adapter.HOLDER> {

    List<SITE_VISIT_ORDER_class> list ;


    public SiteVisitOrders_Adapter(List<SITE_VISIT_ORDER_class> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public SiteVisitOrders_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_my_site_visit_orders_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SiteVisitOrders_Adapter.HOLDER holder, @SuppressLint("RecyclerView") int position) {

        holder.pName.setText(list.get(position).ProjectName);
        holder.dateTime.setText(list.get(position).VisitDate+"  "+list.get(position).VisitTime);
        USER to = null ;
        for (USER u : MyApp.EMPS) {
            if (u.JobNumber == list.get(position).SalesMan) {
                to = u ;
            }
        }
        if (to != null ) {
            holder.name.setText(to.FirstName+" "+to.LastName);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("className" , holder.itemView.getContext().getClass().getName());
                if (holder.itemView.getContext().getClass().getName().equals("com.syrsoft.ratcoms.PROJECTSActivity.SiteVisitOrders_ForProjects")) {
                    if (list.get(position).Status == 1) {
                        Intent i = new Intent(holder.itemView.getContext(), SiteVisitOrder.class);
                        i.putExtra("ID",list.get(position).id);
                        holder.itemView.getContext().startActivity(i);
                    }
                    else if (list.get(position).Status == 0 ) {
                        Intent i = new Intent(holder.itemView.getContext(), SiteVisitOrder_ForProjects.class);
                        i.putExtra("ID",list.get(position).id);
                        holder.itemView.getContext().startActivity(i);
                    }

                }
                else {
                    Intent i = new Intent(holder.itemView.getContext(), SiteVisitOrder.class);
                    i.putExtra("ID",list.get(position).id);
                    holder.itemView.getContext().startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {

        TextView pName , dateTime , name ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            pName = (TextView) itemView.findViewById(R.id.siteVisitOrders_Unit_pname);
            dateTime = (TextView) itemView.findViewById(R.id.siteVisitOrders_Unit_dateTime);
            name = (TextView) itemView.findViewById(R.id.siteVisitOrders_Unit_name);
        }
    }
}
