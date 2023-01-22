package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.HRActivities.ATTENDANCE_CLASS;
import com.syrsoft.ratcoms.R;

import java.util.List;

public class Attendances_Adapter extends RecyclerView.Adapter<Attendances_Adapter.HOLDER> {

    List<ATTENDANCE_CLASS> list ;

    public Attendances_Adapter(List<ATTENDANCE_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Attendances_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_attendance_result_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Attendances_Adapter.HOLDER holder, int position) {
            holder.Date.setText(list.get(position).Date);
            holder.Time.setText(list.get(position).Time);
            holder.operation.setImageResource(list.get(position).getOperation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView Date , Time ;
        ImageView operation ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            Date = (TextView) itemView.findViewById(R.id.textView30);
            Time = (TextView) itemView.findViewById(R.id.textView29);
            operation = (ImageView) itemView.findViewById(R.id.imageView18);
        }
    }
}
