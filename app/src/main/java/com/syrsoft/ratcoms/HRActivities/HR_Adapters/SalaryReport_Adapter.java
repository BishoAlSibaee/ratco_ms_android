package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.HRActivities.SalaryReport;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;

import java.util.List;

public class SalaryReport_Adapter extends RecyclerView.Adapter<SalaryReport_Adapter.HOLDER> {

    List<SalaryReport> list ;

    public SalaryReport_Adapter(List<SalaryReport> list) {
        this.list = list ;
    }
    @NonNull
    @Override
    public SalaryReport_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryReport_Adapter.HOLDER holder, int position) {
            holder.date.setVisibility(View.GONE);
            holder.title.setText("Month "+list.get(position).Month);
            holder.message.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);
            holder.go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).Link));
                    holder.itemView.getContext().startActivity(browserIntent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView date , title , message ;
        ImageView img ;
        Button go ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.textView100);
            title = (TextView) itemView.findViewById(R.id.adsunit_title);
            message = (TextView) itemView.findViewById(R.id.adsunit_text);
            img = (ImageView) itemView.findViewById(R.id.adsunit_img);
            go = (Button) itemView.findViewById(R.id.button20);
        }

    }
}
