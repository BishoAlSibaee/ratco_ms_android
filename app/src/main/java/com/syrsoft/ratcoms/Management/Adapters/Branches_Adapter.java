package com.syrsoft.ratcoms.Management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.Management.Branch;
import com.syrsoft.ratcoms.Management.SetMaintenanceResponsibles;
import com.syrsoft.ratcoms.R;

import java.util.List;

public class Branches_Adapter extends RecyclerView.Adapter<Branches_Adapter.HOLDER> {

    List<Branch> list ;

    public Branches_Adapter(List<Branch> list) {
        this.list = list ;
    }


    @NonNull
    @Override
    public Branches_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.management_branch_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Branches_Adapter.HOLDER holder, int position) {
        holder.branch.setText(list.get(position).BranchName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetMaintenanceResponsibles.selectedBranchIndex = position ;
                SetMaintenanceResponsibles.SelectedBranch = list.get(position) ;
                SetMaintenanceResponsibles.setDepartments();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView branch ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            branch = (TextView) itemView.findViewById(R.id.textView109);
        }
    }
}
