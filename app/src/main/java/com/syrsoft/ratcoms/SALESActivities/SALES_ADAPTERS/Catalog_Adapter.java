package com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.Catalog_Class;

import java.util.List;

public class Catalog_Adapter  extends RecyclerView.Adapter<Catalog_Adapter.HOLDER> {

    List<Catalog_Class> list ;

    public Catalog_Adapter(List<Catalog_Class> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Catalog_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_sheet_unit,parent,false);
        Catalog_Adapter.HOLDER holder = new Catalog_Adapter.HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Catalog_Adapter.HOLDER holder, int position) {
        holder.name.setText(list.get(position).FileName);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).Link));
                holder.itemView.getContext().startActivity(browserIntent);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,list.get(position).Link );
                holder.itemView.getContext().startActivity(Intent.createChooser(shareIntent,"Select"));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView name ;
        Button view , share ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.sheet_name);
            view = (Button) itemView.findViewById(R.id.sheet_viewfile);
            share = (Button) itemView.findViewById(R.id.button37);
        }
    }
}
