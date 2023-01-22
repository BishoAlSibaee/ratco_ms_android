package com.syrsoft.ratcoms.Management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.Permission;
import com.syrsoft.ratcoms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Permissions_Adapter extends RecyclerView.Adapter<Permissions_Adapter.HOLDER> {

    List<Permission> list = new ArrayList<Permission>();

    public Permissions_Adapter(List<Permission> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_unit,parent,false);
        HOLDER holder = new HOLDER(V);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HOLDER holder, int position) {
        if (Locale.getDefault().getLanguage().equals("ar")) {
            holder.name.setText(list.get(position).getPermissionArName());
        }
        else {
            holder.name.setText(list.get(position).getPermissionEnName());
        }
        holder.chk.setOnCheckedChangeListener(null);

        holder.chk.setChecked(list.get(position).getResult());

        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    list.get(position).setValue(1);
                    list.get(position).setResult();
                }
                else {
                    list.get(position).setValue(0);
                    list.get(position).setResult();
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
        CheckBox chk ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView97);
            chk = (CheckBox) itemView.findViewById(R.id.checkBox2);
        }
    }
}
