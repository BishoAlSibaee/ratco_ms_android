package com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.syrsoft.ratcoms.PROJECTSActivity.PurchaseOrderQuotationFILES;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderQuotationFile_Adapter extends RecyclerView.Adapter<PurchaseOrderQuotationFile_Adapter.HOLDER> {

    List<PurchaseOrderQuotationFILES> list ;

    PurchaseOrderQuotationFile_Adapter(List<PurchaseOrderQuotationFILES> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public PurchaseOrderQuotationFile_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_purchase_order_file_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseOrderQuotationFile_Adapter.HOLDER holder, int position) {
        String[] arr = list.get(position).Link.split("\\.") ;
        if (arr.length > 0) {
            if (arr[arr.length-1].contains("png")) {
                Picasso.get().load(list.get(position).Link).into(holder.Image);
                holder.Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog D = new Dialog(holder.itemView.getContext());
                        D.setContentView(R.layout.view_zoomable_image);
                        Window window = D.getWindow();
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        PhotoView image = (PhotoView)D.findViewById(R.id.photo_view);
                        ImageButton x = (ImageButton) D.findViewById(R.id.close);
                        x.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                D.dismiss();
                            }
                        });
                        Picasso.get().load(list.get(position).Link).into(image);
                        D.show();
                    }
                });
            }
            else if (arr[arr.length-1].contains("pdf")) {
                holder.Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).Link));
                        holder.itemView.getContext().startActivity(browserIntent);
                    }
                });
            }
        }
        else {
            ToastMaker.Show(1,"no files attached to this Quotation",holder.itemView.getContext());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        ImageView Image ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.imageView29);
        }
    }
}
