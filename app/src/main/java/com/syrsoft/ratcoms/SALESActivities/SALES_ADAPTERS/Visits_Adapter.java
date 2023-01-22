package com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.CLIENT_VISIT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.ViewMyVisitDetailes;
import com.syrsoft.ratcoms.ToastMaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Visits_Adapter extends RecyclerView.Adapter<Visits_Adapter.HOLDER> {

    List<CLIENT_VISIT_CLASS> list ;
    int ATTACHFILE_REQCODE_Quotation = 1 ;
    int CAM_REQCODE_QUOTATION = 2 ;
    int CAM_PERMISSION_REQCODE_QUOTATION = 3 ;
    int CAM_REQCODE_LOCATION = 4 ;
    int ATTACHFILE_REQCODE_Location = 5 ;
    int CAM_PERMISSION_REQCODE_LOCATION = 6 ;
    String modifyVisitUrl = MyApp.MainUrl+"editVisitReport.php" ;


    public Visits_Adapter(List<CLIENT_VISIT_CLASS> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public Visits_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_visit_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Visits_Adapter.HOLDER holder, int position) {
        holder.VisitDetails.setText(list.get(position).VisitDetails);
        holder.projectDesc.setText(list.get(position).ProjectDescription);
        holder.dateTime.setText(list.get(position).Date+"    "+list.get(position).Time);
        holder.followupat.setText(list.get(position).FollowUpAt);
        holder.interested.setEnabled(false);
        holder.unInterested.setEnabled(false);
        if (list.get(position).FileLink == null || list.get(position).FileLink.isEmpty()) {
            holder.viewFile.setVisibility(View.GONE);
        }
        if (list.get(position).SalesMan != MyApp.db.getUser().JobNumber) {
            holder.edit.setVisibility(View.GONE);
        }
        if (list.get(position).Interested == 1) {
            holder.interested.setChecked(true);
        }
        else if (list.get(position).Interested == 0) {
            holder.unInterested.setChecked(true);
        }
        if (list.get(position).LocationLink == null || list.get(position).LocationLink.isEmpty()) {
            holder.location.setVisibility(View.GONE);
        }
        else {
            Picasso.get().load(list.get(position).LocationLink).into(holder.location);
            holder.location.setOnClickListener(new View.OnClickListener() {
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
                    Picasso.get().load(list.get(position).LocationLink).into(image);
                    D.show();
                }
            });
        }
        if (list.get(position).QuotationLink == null || list.get(position).QuotationLink.isEmpty()) {
            holder.quotation.setVisibility(View.GONE);
        }
        else {
            Picasso.get().load(list.get(position).QuotationLink).into(holder.quotation);
            holder.quotation.setOnClickListener(new View.OnClickListener() {
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
                    Picasso.get().load(list.get(position).QuotationLink).into(image);
                    D.show();
                }
            });
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(holder.itemView.getContext());
                D.setContentView(R.layout.sales_edit_visit_report_dialog);
                Window w = D.getWindow();
                w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                final int[] Interested = {0};
                EditText projectDesc , visitDet ;
                Button cancel , save , quotationBtn , siteBtn ;
                RadioButton interested , unInterested ;
                interested = (RadioButton)D.findViewById(R.id.VisitUnit_InterestedRB);
                unInterested = (RadioButton) D.findViewById(R.id.VisitUnit_unInterestedRB);
                interested.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            unInterested.setChecked(false);
                            Interested[0] = 1 ;
                        }
                    }
                });
                unInterested.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            interested.setChecked(false);
                            Interested[0] = 0 ;
                        }
                    }
                });
                projectDesc = (EditText) D.findViewById(R.id.EditVisit_projectDesc);
                projectDesc.setText(list.get(position).ProjectDescription);
                visitDet = (EditText) D.findViewById(R.id.EditVisit_visitDetails);
                visitDet.setText(list.get(position).VisitDetails);
                cancel = (Button) D.findViewById(R.id.editVisit_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                quotationBtn = (Button) D.findViewById(R.id.imageView21);
                quotationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder selectDialog = new AlertDialog.Builder(holder.itemView.getContext());
                        selectDialog.setTitle(holder.itemView.getContext().getResources().getString(R.string.selectFileTitle));
                        selectDialog.setMessage(holder.itemView.getContext().getResources().getString(R.string.selectFileMessage));
                        selectDialog.setNegativeButton(holder.itemView.getContext().getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Activity A =(Activity) holder.itemView.getContext();
                                if (ActivityCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                                {
                                    ActivityCompat.requestPermissions(A,new String[]{Manifest.permission.CAMERA},CAM_PERMISSION_REQCODE_QUOTATION);
                                }
                                else {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    try {
                                        A.startActivityForResult(takePictureIntent, CAM_REQCODE_QUOTATION);
                                    } catch (ActivityNotFoundException e) {
                                        // display error state to the user
                                    }

                                }
                            }
                        });
                        selectDialog.setPositiveButton(holder.itemView.getContext().getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                                open.setType("image/*");
                                Activity A =(Activity) holder.itemView.getContext();
                                A.startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE_Quotation);
                            }
                        });
                        selectDialog.create().show();
                    }
                });
                siteBtn = (Button) D.findViewById(R.id.imageView22);
                siteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity A = (Activity) holder.itemView.getContext() ;
                        AlertDialog.Builder selectDialog = new AlertDialog.Builder(holder.itemView.getContext());
                        selectDialog.setTitle(holder.itemView.getContext().getResources().getString(R.string.selectFileTitle));
                        selectDialog.setMessage(holder.itemView.getContext().getResources().getString(R.string.selectFileMessage));
                        selectDialog.setNegativeButton(holder.itemView.getContext().getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ActivityCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                                {
                                    ActivityCompat.requestPermissions(A,new String[]{Manifest.permission.CAMERA},CAM_PERMISSION_REQCODE_LOCATION);
                                }
                                else {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    try {
                                        A.startActivityForResult(takePictureIntent, CAM_REQCODE_LOCATION);
                                    } catch (ActivityNotFoundException e) {
                                        // display error state to the user
                                    }

                                }
                            }
                        });
                        selectDialog.setPositiveButton(A.getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                                open.setType("image/*");
                                A.startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE_Location);
                            }
                        });
                        selectDialog.create().show();
                    }
                });
                save = (Button) D.findViewById(R.id.editVisit_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Loading l = new Loading(holder.itemView.getContext());
                        l.show();
                        StringRequest request = new StringRequest(Request.Method.POST,modifyVisitUrl , new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                l.close();
                                if (response.equals("1")) {
                                    D.dismiss();
                                    ToastMaker.Show(0,holder.itemView.getContext().getResources().getString(R.string.saved),holder.itemView.getContext());
                                    if (ViewMyVisitDetailes.QuotationBitmap != null) {
                                        MyApp.savePhoto(ViewMyVisitDetailes.QuotationBitmap,"ClientVisitReport",list.get(position).id,"QuotationLink",3,"Q");
                                    }
                                    if (ViewMyVisitDetailes.ClientLocation != null ) {
                                        MyApp.savePhoto(ViewMyVisitDetailes.ClientLocation,"ClientVisitReport",list.get(position).id,"LocationLink",3,"L");
                                    }
                                }
                                else if (response.equals("0")) {

                                }
                                else if (response.equals("-1")) {

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                l.close();
                            }
                        })
                        {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map <String,String> par = new HashMap<String, String>();
                                par.put("ID", String.valueOf(list.get(position).id));
                                par.put("PD",projectDesc.getText().toString());
                                par.put("VD",visitDet.getText().toString());
                                par.put("Interested" , String.valueOf(Interested[0]));
                                return par;
                            }
                        };
                        ViewMyVisitDetailes.Q.add(request);
                    }
                });
                D.show();
            }
        });
        holder.viewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).FileLink));
                holder.itemView.getContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView projectDesc , VisitDetails ,dateTime ;
        ImageView quotation , location ;
        Button edit , viewFile ;
        RadioButton interested , unInterested ;
        TextView followupat ;

        public HOLDER(@NonNull View itemView) {
            super(itemView);
            projectDesc = (TextView) itemView.findViewById(R.id.MyVisit_projectDescTextView);
            VisitDetails = (TextView) itemView.findViewById(R.id.MyVisit_VisitTextView);
            dateTime = (TextView) itemView.findViewById(R.id.dateTime);
            quotation = (ImageView) itemView.findViewById(R.id.imageView22);
            location = (ImageView) itemView.findViewById(R.id.imageView21);
            edit = (Button) itemView.findViewById(R.id.editBtn);
            interested = (RadioButton) itemView.findViewById(R.id.VisitUnit_InterestedRB);
            unInterested = (RadioButton) itemView.findViewById(R.id.VisitUnit_unInterestedRB);
            followupat = (TextView) itemView.findViewById(R.id.followUpDate);
            viewFile = (Button) itemView.findViewById(R.id.button36);
        }
    }
}
