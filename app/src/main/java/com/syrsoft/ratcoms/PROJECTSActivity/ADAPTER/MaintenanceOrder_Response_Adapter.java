package com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.PROJECTSActivity.MAINTENANCE_ORDER_RESPONSE_class;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceOrder_Response_Adapter extends RecyclerView.Adapter<MaintenanceOrder_Response_Adapter.HOLDER> {

    List<MAINTENANCE_ORDER_RESPONSE_class> list = new ArrayList<MAINTENANCE_ORDER_RESPONSE_class>();
    public MaintenanceOrder_Response_Adapter(List<MAINTENANCE_ORDER_RESPONSE_class> list) {
        this.list = list ;
    }
    @NonNull
    @Override
    public MaintenanceOrder_Response_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_maintenance_order_response_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceOrder_Response_Adapter.HOLDER holder, int position) {
        holder.documents.setVisibility(View.GONE);
        holder.response.setText(list.get(position).Response);
        for (USER u : MyApp.EMPS) {
            if (u.JobNumber == list.get(position).Employee) {
                holder.emp.setText(u.FirstName+" "+u.LastName);
                break;
            }
        }
        holder.date.setText(list.get(position).Date);
        holder.time.setText(list.get(position).Time);
        holder.spareStatus.setText(list.get(position).getSpareParts());
        holder.spare.setText(list.get(position).SpareParts);
        holder.done.setChecked(list.get(position).getOrderStatus());
        if (holder.done.isChecked()) {
            holder.undone.setChecked(false);
        }
        else {
            holder.undone.setChecked(true);
        }
        holder.done.setEnabled(false);
        holder.undone.setEnabled(false);
        if (list.get(position).document1 != null && !list.get(position).document1.isEmpty()) {
            holder.documents.setVisibility(View.VISIBLE);
            Picasso.get().load(list.get(position).document1).into(holder.documents1);
            holder.documents1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog D = new Dialog(holder.itemView.getContext());
                    D.setContentView(R.layout.view_zoomable_image);
                    Window window = D.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    PhotoView image = (PhotoView)D.findViewById(R.id.photo_view);
                    ImageButton x = (ImageButton) D.findViewById(R.id.close);
                    x.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            D.dismiss();
                        }
                    });
                    Picasso.get().load(list.get(position).document1).into(image);
                    D.show();
                }
            });
        }
        else {
            holder.documents1.setVisibility(View.GONE);
        }
        if (list.get(position).document2 != null && !list.get(position).document2.isEmpty()) {
            holder.documents.setVisibility(View.VISIBLE);
            Picasso.get().load(list.get(position).document2).into(holder.documents2);
            holder.documents2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog D = new Dialog(holder.itemView.getContext());
                    D.setContentView(R.layout.view_zoomable_image);
                    Window window = D.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    PhotoView image = (PhotoView)D.findViewById(R.id.photo_view);
                    ImageButton x = (ImageButton) D.findViewById(R.id.close);
                    x.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            D.dismiss();
                        }
                    });
                    Picasso.get().load(list.get(position).document2).into(image);
                    D.show();
                }
            });
        }
        else {
            holder.documents2.setVisibility(View.GONE);
        }
        if (list.get(position).document3 != null && !list.get(position).document3.isEmpty()) {
            holder.documents.setVisibility(View.VISIBLE);
            Picasso.get().load(list.get(position).document3).into(holder.documents3);
            holder.documents3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog D = new Dialog(holder.itemView.getContext());
                    D.setContentView(R.layout.view_zoomable_image);
                    Window window = D.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    PhotoView image = (PhotoView)D.findViewById(R.id.photo_view);
                    ImageButton x = (ImageButton) D.findViewById(R.id.close);
                    x.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            D.dismiss();
                        }
                    });
                    Picasso.get().load(list.get(position).document3).into(image);
                    D.show();
                }
            });
        }
        else {
            holder.documents3.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView response , emp , date , time , spareStatus ,spare  ;
        RadioButton done ,undone ;
        LinearLayout documents ;
        ImageView documents1,documents2,documents3 ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            response = (TextView) itemView.findViewById(R.id.response);
            emp = (TextView) itemView.findViewById(R.id.employee);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            spareStatus = (TextView) itemView.findViewById(R.id.sparePartsStatus);
            spare = (TextView) itemView.findViewById(R.id.spareParts);
            done = (RadioButton) itemView.findViewById(R.id.done);
            undone = (RadioButton) itemView.findViewById(R.id.undone);
            documents = (LinearLayout) itemView.findViewById(R.id.documentsLayout);
            documents1 = (ImageView) itemView.findViewById(R.id.imageView5);
            documents2 = (ImageView) itemView.findViewById(R.id.imageView27);
            documents3 = (ImageView) itemView.findViewById(R.id.imageView28);
        }
    }
}
