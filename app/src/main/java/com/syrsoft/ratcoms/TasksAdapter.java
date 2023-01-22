package com.syrsoft.ratcoms;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.SALESActivities.ViewMyVisitDetailes;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.HOLDER> {

        List<TASK> list = new ArrayList<TASK>();

    TasksAdapter(List<TASK> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public TasksAdapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.HOLDER holder, int position) {
        holder.action.setText(list.get(position).Action);
        if (list.get(position).client != null) {
            holder.client.setText(list.get(position).client.ClientName);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).Action.equals("visit")) {
                    Intent i = new Intent(holder.itemView.getContext(), ViewMyVisitDetailes.class);
                    i.putExtra("ItemId",list.get(position).client.id);
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
        TextView action , client ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            action = (TextView) itemView.findViewById(R.id.textView75);
            client = (TextView) itemView.findViewById(R.id.textView78);
        }
    }
}
