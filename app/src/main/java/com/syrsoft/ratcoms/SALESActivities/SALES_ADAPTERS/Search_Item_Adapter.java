package com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.collection.LLRBNode;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.DBItem;
import com.syrsoft.ratcoms.SALESActivities.Search_Item_Dialog;

import java.util.List;

public class Search_Item_Adapter extends RecyclerView.Adapter<Search_Item_Adapter.HOLDER> {


    List<DBItem> list ;

    public Search_Item_Adapter(List<DBItem> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Search_Item_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_unit,parent,false);
        HOLDER holder = new HOLDER(V);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Search_Item_Adapter.HOLDER holder, int position) {
        holder.name.setText(list.get(position).Model+" "+list.get(position).ItemNameArabic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search_Item_Dialog.SelectedItem = list.get(position);
                Search_Item_Dialog.SelectedTV.setText(list.get(position).Model+" "+list.get(position).ItemNameArabic);
                holder.name.setTextColor(Color.RED);
                for (int i=0;i<list.size();i++) {
                    if (list.get(i) != Search_Item_Dialog.SelectedItem) {
                        LinearLayout l = (LinearLayout) Search_Item_Dialog.ItemsRes.getChildAt(i);
                        if (l != null) {
                            TextView t = l.findViewById(R.id.textView115);
                            t.setTextColor(Color.WHITE);
                        }
                    }
                }
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
            name = itemView.findViewById(R.id.textView115);
        }
    }
}
