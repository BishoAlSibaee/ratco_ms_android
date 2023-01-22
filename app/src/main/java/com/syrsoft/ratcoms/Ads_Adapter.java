package com.syrsoft.ratcoms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ads_Adapter extends RecyclerView.Adapter<Ads_Adapter.HOLDER>
{

    List<ADS_CLASS> list = new ArrayList<ADS_CLASS>();
    Ads_Adapter ad ;

    public Ads_Adapter(List<ADS_CLASS> list) {
        this.list = list;
        ad = this ;
    }

    @NonNull
    @Override
    public Ads_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder ;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull HOLDER holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.itemView.getContext().getClass().getName().equals("com.syrsoft.ratcoms.MainPage")) {
            MainPage.CurrentAd = holder.getAdapterPosition() ;
            MainPage.setAdsDots();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Ads_Adapter.HOLDER holder, int position)
    {
        holder.title.setText(list.get(position).Title);
        holder.text.setText(list.get(position).Message);
        holder.date.setText(list.get(position).Date);
        Log.d("imagesAre",list.get(position).ImageLink);
        if (list.get(position).ImageLink != null && !list.get(position).ImageLink.isEmpty())
        {
            Picasso.get().load(list.get(position).ImageLink).into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
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
                    Picasso.get().load(list.get(position).ImageLink).into(image);
                    D.show();
                }
            });
        }
        else
        {
            holder.img.setVisibility(View.GONE);
        }
        if (list.get(position).FileLink == null || list.get(position).FileLink.isEmpty()) {
            holder.go.setVisibility(View.GONE);
        }
        else {
            holder.go.setVisibility(View.VISIBLE);
        }
        holder.go.setText("details");
        holder.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).Title.equals("SalaryReport")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).Date + MyApp.db.getUser().JobNumber+".pdf"));
                    holder.itemView.getContext().startActivity(browserIntent);
                }
                else if (list.get(position).Title.equals("UpdateApplication")) {
                    Log.d("linkText",list.get(position).Date);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getDataDirectory()+ "/download/" + list.get(position).Date)), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //holder.itemView.getContext().startActivity(intent);
                }
//                StringRequest request = new StringRequest(Request.Method.GET, list.get(position).link + MyApp.db.getUser().JobNumber+".pdf", new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//                Volley.newRequestQueue(holder.itemView.getContext()).add(request);
            }
        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
//                builder.setTitle("Delete ..?");
//                builder.setMessage("are you sure ..?");
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        CurrentAutheList.remove(position);
////                        String[] xxx = new String[CurrentAutheList.size()];
////                        for (int x = 0 ; x < CurrentAutheList.size();x++) {
////                            xxx[x] = CurrentAutheList.get(x).AuthorizationName+" " + CurrentAutheList.get(x).ArabicName ;
////                        }
////                        ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,xxx);
////                        authsList.setAdapter(adapter);
//                        MyApp.ADS_DB.deleteAd(Integer.parseInt(list.get(position).FileLink));
//                        list.remove(position);
//                        ad.notifyDataSetChanged();
//                    }
//                });
//                builder.show();
//                return false;
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView title , text , date ;
        ImageView img ;
        Button go ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.adsunit_title);
            text = (TextView) itemView.findViewById(R.id.adsunit_text);
            date = (TextView) itemView.findViewById(R.id.textView100);
            img = (ImageView) itemView.findViewById(R.id.adsunit_img);
            go = (Button) itemView.findViewById(R.id.button20);
        }
    }

}
