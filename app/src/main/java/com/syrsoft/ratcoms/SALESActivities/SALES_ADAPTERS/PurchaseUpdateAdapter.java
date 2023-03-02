package com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.ImportUpdateAttachment;
import com.syrsoft.ratcoms.SALESActivities.PURCHASE_UPDATE_CLASS;
import com.syrsoft.ratcoms.USER;

import java.util.List;

public class PurchaseUpdateAdapter extends RecyclerView.Adapter<PurchaseUpdateAdapter.HOLDER> implements View.OnClickListener {
    List<PURCHASE_UPDATE_CLASS> list;

    public PurchaseUpdateAdapter(List<PURCHASE_UPDATE_CLASS> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PurchaseUpdateAdapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.import_update_unit, parent, false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseUpdateAdapter.HOLDER holder, @SuppressLint("RecyclerView") int position) {
        holder.datetime.setText(list.get(position).UpdateDate);
        USER x = USER.searchUserByID(MyApp.EMPS, list.get(position).UserId);
        holder.txtEmployeeName.setText(x.FirstName + " " + x.LastName);
        holder.txtOrderNotes.setText(list.get(position).Notes);
        if (list.get(position).Attachments != null) {
            for (int i = 0; i < list.get(position).Attachments.size(); i++) {
                Button bt = new Button(holder.itemView.getContext());
                holder.LinearBtnAttachment.addView(bt);
                bt.setText("المرفق " + (i + 1));
                bt.setBackgroundResource(R.drawable.approve_btn_background);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bt.getLayoutParams();
                params.setMargins(5,5,5,5);
                int finalI = i;
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).Attachments.get(finalI).Link));
                        holder.itemView.getContext().startActivity(browserIntent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView datetime, txtEmployeeName, txtOrderNotes;
        LinearLayout LinearBtnAttachment;

        public HOLDER(@NonNull View itemView) {
            super(itemView);
            datetime = itemView.findViewById(R.id.datetime);
            txtEmployeeName = itemView.findViewById(R.id.txtEmployeeName);
            txtOrderNotes = itemView.findViewById(R.id.txtOrderNotes);
            LinearBtnAttachment = itemView.findViewById(R.id.LinearBtnAttachment);
        }
    }
}
