package com.syrsoft.ratcoms;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.HOLDER> {

    List<USER> list;
    TextView resTV;
    public USER user;

    public User_Adapter(List<USER> list,TextView tv) {
        this.list = list;
        this.resTV = tv ;
    }

    @NonNull
    @Override
    public HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_unit, parent, false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HOLDER holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(list.get(position).FirstName + " " + list.get(position).LastName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resTV != null) {
                    resTV.setText(list.get(position).FirstName + " " + list.get(position).LastName);
                    resTV.setBackgroundColor(holder.itemView.getResources().getColor(R.color.teal_700));
                }
                user = list.get(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView name;

        public HOLDER(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView115);
        }
    }
}
