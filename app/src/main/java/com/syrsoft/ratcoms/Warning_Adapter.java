package com.syrsoft.ratcoms;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class Warning_Adapter extends RecyclerView.Adapter<Warning_Adapter.HOLDER>
{

    List<WARNING_CLASS> list = new ArrayList<WARNING_CLASS>();

    public Warning_Adapter(List<WARNING_CLASS> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Warning_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.warning_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull Warning_Adapter.HOLDER holder, int position)
    {
        holder.jnum.setText(String.valueOf(list.get(position).JobNumber));
        holder.name.setText(list.get(position).FirstName+" "+list.get(position).LastName);
        holder.order.setText(list.get(position).warning);
        holder.date.setText(list.get(position).Date);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                //ToastMaker.Show(1, holder.text.getSolidColor()+"" ,holder.itemView.getContext());
                if (holder.name.getCurrentTextColor() == Color.WHITE)
                {
                    holder.jnum.setTextColor(holder.itemView.getResources().getColor(R.color.lightGray1));
                    holder.name.setTextColor(holder.itemView.getResources().getColor(R.color.lightGray1));
                    holder.order.setTextColor(holder.itemView.getResources().getColor(R.color.lightGray1));
                }
                else
                {
                    holder.jnum.setTextColor(Color.WHITE);
                    holder.name.setTextColor(Color.WHITE);
                    holder.order.setTextColor(Color.WHITE);
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView jnum , name , order , date ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            jnum = (TextView) itemView.findViewById(R.id.textView68);
            name = (TextView) itemView.findViewById(R.id.textView65);
            order = (TextView) itemView.findViewById(R.id.textView69);
            date = (TextView) itemView.findViewById(R.id.textView70);
        }
    }
}
