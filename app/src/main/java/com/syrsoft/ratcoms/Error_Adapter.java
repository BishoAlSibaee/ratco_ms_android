package com.syrsoft.ratcoms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Error_Adapter  extends RecyclerView.Adapter<Error_Adapter.HOLDER> {

    List<ERROR_CLASS> list = new ArrayList<ERROR_CLASS>();

    Error_Adapter(List<ERROR_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Error_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.errors_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Error_Adapter.HOLDER holder, int position) {

        holder.id.setText(String.valueOf(list.get(position).id));
        holder.error.setText(list.get(position).ErrorText);
        holder.method.setText(list.get(position).MethodName);
        holder.activity.setText(list.get(position).Activity);
        holder.date.setText(list.get(position).Date);
        holder.time.setText(list.get(position).Time);
        holder.user.setText(list.get(position).User);
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView id,error,activity,user,method,date,time ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.errorUnit_id);
            error = (TextView) itemView.findViewById(R.id.errorUnit_error);
            activity = (TextView) itemView.findViewById(R.id.errorUnit_activity);
            user = (TextView) itemView.findViewById(R.id.errorUnit_user);
            method = (TextView) itemView.findViewById(R.id.errorUnit_method);
            date = (TextView) itemView.findViewById(R.id.errorUnit_date);
            time = (TextView) itemView.findViewById(R.id.errorUnit_time);
        }
    }
}
