package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.HRActivities.EmployeeRating;
import com.syrsoft.ratcoms.R;

import org.w3c.dom.Text;

import java.util.List;

public class EmployeesRating_Adapter extends RecyclerView.Adapter<EmployeesRating_Adapter.HOLDER> {

    List<EmployeeRating> list ;

    EmployeesRating_Adapter(List<EmployeeRating> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public EmployeesRating_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_rating_emps_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesRating_Adapter.HOLDER holder, int position) {
        holder.Type.setText(list.get(position).getType());
        if (list.get(position).getRating() > 0) {

        }
        else if (list.get(position).getRating() == 0)  {
            holder.Rating.setProgress(list.get(position).getRating());
            holder.Rating.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView Type ;
        SeekBar Rating ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            Type = (TextView) itemView.findViewById(R.id.textView80);
            Rating = (SeekBar) itemView.findViewById(R.id.seekBar);
            Rating.setMax(10);
        }
    }
}
