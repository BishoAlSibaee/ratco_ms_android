package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.util.ArrayList;
import java.util.List;

public class addUsersToByLocationDialog_Adapter extends BaseAdapter {

    List<USER> list = new ArrayList<USER>();
    LayoutInflater inflater ;
    Context c ;

    public addUsersToByLocationDialog_Adapter(List<USER> list,Context c){
        this.list = list ;
        this.c = c ;
        inflater = LayoutInflater.from(c);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(c).inflate(R.layout.one_text_unit,null) ;
        TextView text = convertView.findViewById(R.id.textView28);
        text.setText(list.get(position).FirstName+" "+list.get(position).LastName);
        return convertView;
    }

}
