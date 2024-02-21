package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HR;
import com.syrsoft.ratcoms.HRActivities.EXIT_REQUEST_CLASS;
import com.syrsoft.ratcoms.HRActivities.HR_Orders;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExitRequest_ADAPTER  extends RecyclerView.Adapter<ExitRequest_ADAPTER.HOLDER> {

    List<EXIT_REQUEST_CLASS> list ;
    boolean[] status  ;
    String deleteExitUrl = MyApp.MainUrl + "deleteExit.php";

    public ExitRequest_ADAPTER(List<EXIT_REQUEST_CLASS> list) {
        this.list = list ;
    }
    @NonNull
    @Override
    public ExitRequest_ADAPTER.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_exit_request_unit,parent,false);
        ExitRequest_ADAPTER.HOLDER holder = new ExitRequest_ADAPTER.HOLDER(v);
        return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ExitRequest_ADAPTER.HOLDER holder, @SuppressLint("RecyclerView") int position) {
        status = new boolean[this.list.size()];
        for (int i=0;i<this.list.size();i++) {
            status[i] = false ;
        }
        holder.Order.setText(holder.itemView.getResources().getString(R.string.exitRequest));
        holder.time.setText(list.get(position).Time);
        holder.Date.setText(list.get(position).Date);
        holder.Reason.setText(list.get(position).Notes);
        holder.back.setText(list.get(position).BackTime);
        for (int i=0 ; i< list.get(position).Auths.size();i++) {
            holder.layouts.get(i).setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status[position] ) {
                    if (MyApp.ExitAuthUsers.size()>0) {
                        holder.icon.setImageResource(R.drawable.bring_up_icon);
                        for (int i = 0; i< MyApp.ExitAuthUsers.size(); i++) {
                            holder.layouts.get(i).setVisibility(View.VISIBLE);
                            holder.appsCaption.get(i).setText(MyApp.ExitAuthsJobTitles.get(i).JobTitle);
                            holder.apps.get(i).setText(MyApp.ExitAuthUsers.get(i).FirstName+" "+MyApp.ExitAuthUsers.get(i).LastName);
                            holder.appsres.get(i).setText(list.get(position).Auths.get(i).getAuthResult());
                            holder.appsdate.get(i).setText(list.get(position).Auths.get(i).getAuthDate());
                            holder.addNotes.get(i).setText(list.get(position).Auths.get(i).Note);
                            if (list.get(position).Auths.get(i).getAuth() == 1) {
                                holder.appsres.get(i).setTextColor(Color.GREEN);
                                holder.radios.get(i).setChecked(true);
                            }
                            else if (list.get(position).Auths.get(i).getAuth() == 2) {
                                holder.appsres.get(i).setTextColor(Color.RED);
                                holder.radios.get(i).setChecked(true);
                            }
                            else if (list.get(position).Auths.get(i).getAuth() == 0) {
                                holder.appsres.get(i).setTextColor(Color.BLUE);
                                holder.radios.get(i).setChecked(false);
                            }
                        }
                        status[position] = true ;
                    }
                    else {
                        ToastMaker.Show(0,"No Auths For Custody Request" ,holder.itemView.getContext());
                    }
                }
                else {
                    holder.icon.setImageResource(R.drawable.drop_down_icon);
                    for (int i=0 ; i< list.get(position).Auths.size();i++) {
                        holder.layouts.get(i).setVisibility(View.GONE);
                    }
                    status[position] = false ;
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(holder.itemView.getContext());
                b.setTitle(holder.itemView.getContext().getResources().getString(R.string.areYouSure))
                        .setMessage(holder.itemView.getContext().getResources().getString(R.string.areYouSureDelete))
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringRequest req = new StringRequest(Request.Method.POST, deleteExitUrl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("1")) {
                                            HR_Orders.getExitRequests();
                                        }
                                        else {
                                            new MESSAGE_DIALOG(holder.itemView.getContext(),holder.itemView.getContext().getResources().getString(R.string.deleteFailed),holder.itemView.getContext().getResources().getString(R.string.deleteFailed));
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        new MESSAGE_DIALOG(holder.itemView.getContext(),holder.itemView.getContext().getResources().getString(R.string.deleteFailed),holder.itemView.getContext().getResources().getString(R.string.deleteFailed)+" "+error);
                                    }
                                }){
                                    @Nullable
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> par = new HashMap<>();
                                        par.put("ID", String.valueOf(list.get(position).id));
                                        par.put("JobNumber", String.valueOf(list.get(position).JobNumber));
                                        return par;
                                    }
                                };
                                if (list.get(position).Status == 0) {
                                    Volley.newRequestQueue(holder.itemView.getContext()).add(req);
                                }
                                else {
                                    new MESSAGE_DIALOG(holder.itemView.getContext(),holder.itemView.getContext().getResources().getString(R.string.thisOrderIsClosed),holder.itemView.getContext().getResources().getString(R.string.thisOrderIsClosed));
                                }
                            }
                        });
                b.create().show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView Order , time , back , Date , Reason , app1,app1res,app1date,app1Note,app2,app2res,app2date,app2Note,app3,app3res,app3date,app3Note,app4,app4res,app4date,app4Note,app5,app5res,app5date,app5Note,app6,app6res,app6date,app6Note,app7,app7res,app7date,app7Note,app8,app8res,app8date,app8Note,app9,app9res,app9date,app9Note,app10,app10res,app10date,app10Note ;
        TextView app1Caption,app2Caption,app3Caption,app4Caption,app5Caption,app6Caption,app7Caption,app8Caption,app9Caption,app10Caption;
        List<TextView> apps = new ArrayList<TextView>();
        List<TextView> appsres = new ArrayList<TextView>();
        List<TextView> appsdate = new ArrayList<TextView>();
        List<TextView> appsCaption = new ArrayList<TextView>();
        List<TextView> addNotes = new ArrayList<TextView>();
        RadioButton R1,R2,R3,R4,R5,R6,R7,R8,R9,R10 ;
        LinearLayout L1,L2,L3,L4,L5,L6,L7,L8,L9,L10 ;
        List<RadioButton> radios = new ArrayList<RadioButton>();
        List<LinearLayout> layouts = new ArrayList<LinearLayout>();
        ImageView icon ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            Order = (TextView) itemView.findViewById(R.id.Custody_order);
            time = (TextView) itemView.findViewById(R.id.Custody_time);
            back = (TextView) itemView.findViewById(R.id.Custody_backime);
            Date = (TextView) itemView.findViewById(R.id.Custody_date);
            Reason = (TextView) itemView.findViewById(R.id.Custody_reason);
            app1 = (TextView) itemView.findViewById(R.id.Custody_Auth1ID);
            app1res = (TextView) itemView.findViewById(R.id.Custody_Auth1Res);
            app1date = (TextView) itemView.findViewById(R.id.Custody_Auth1Date);
            app1Caption = (TextView)itemView.findViewById(R.id.textView27);
            app2 = (TextView) itemView.findViewById(R.id.Custody_Auth2ID);
            app2res = (TextView) itemView.findViewById(R.id.Custody_Auth2Res);
            app2date = (TextView) itemView.findViewById(R.id.Custody_Auth2Date);
            app2Caption = (TextView) itemView.findViewById(R.id.textView24);
            app3 = (TextView) itemView.findViewById(R.id.Custody_Auth3ID);
            app3res = (TextView) itemView.findViewById(R.id.Custody_Auth3Res);
            app3date = (TextView) itemView.findViewById(R.id.Custody_Auth3Date);
            app3Caption = (TextView) itemView.findViewById(R.id.textView270);
            app4 = (TextView) itemView.findViewById(R.id.Custody_Auth4ID);
            app4res = (TextView) itemView.findViewById(R.id.Custody_Auth4Res);
            app4date = (TextView) itemView.findViewById(R.id.Custody_Auth4Date);
            app4Caption = (TextView) itemView.findViewById(R.id.textView240);
            app5 = (TextView) itemView.findViewById(R.id.Custody_Auth5ID);
            app5res = (TextView) itemView.findViewById(R.id.Custody_Auth5Res);
            app5date = (TextView) itemView.findViewById(R.id.Custody_Auth5Date);
            app5Caption = (TextView) itemView.findViewById(R.id.textView271);
            app6 = (TextView) itemView.findViewById(R.id.Custody_Auth6ID);
            app6res = (TextView) itemView.findViewById(R.id.Custody_Auth6Res);
            app6date = (TextView) itemView.findViewById(R.id.Custody_Auth6Date);
            app6Caption = (TextView) itemView.findViewById(R.id.textView241);
            app7 = (TextView) itemView.findViewById(R.id.Custody_Auth7ID);
            app7res = (TextView) itemView.findViewById(R.id.Custody_Auth7Res);
            app7date = (TextView) itemView.findViewById(R.id.Custody_Auth7Date);
            app7Caption = (TextView) itemView.findViewById(R.id.textView2715);
            app8 = (TextView) itemView.findViewById(R.id.Custody_Auth8ID);
            app8res = (TextView) itemView.findViewById(R.id.Custody_Auth8Res);
            app8date = (TextView) itemView.findViewById(R.id.Custody_Auth8Date);
            app8Caption = (TextView) itemView.findViewById(R.id.textView2415);
            app9 = (TextView) itemView.findViewById(R.id.Custody_Auth9ID);
            app9res = (TextView) itemView.findViewById(R.id.Custody_Auth9Res);
            app9date = (TextView) itemView.findViewById(R.id.Custody_Auth9Date);
            app9Caption = (TextView) itemView.findViewById(R.id.textView2714);
            app10 = (TextView) itemView.findViewById(R.id.Custody_Auth10ID);
            app10res = (TextView) itemView.findViewById(R.id.Custody_Auth10Res);
            app10date = (TextView) itemView.findViewById(R.id.Custody_Auth10Date);
            app10Caption = (TextView) itemView.findViewById(R.id.textView2414);
            app1Note = (TextView) itemView.findViewById(R.id.Custody_Auth1Note);
            app2Note = (TextView) itemView.findViewById(R.id.Custody_Auth2Note);
            app3Note = (TextView) itemView.findViewById(R.id.Custody_Auth3Note);
            app4Note = (TextView) itemView.findViewById(R.id.Custody_Auth4Note);
            app5Note = (TextView) itemView.findViewById(R.id.Custody_Auth5Note);
            app6Note = (TextView) itemView.findViewById(R.id.Custody_Auth6Note);
            app7Note = (TextView) itemView.findViewById(R.id.Custody_Auth7Note);
            app8Note = (TextView) itemView.findViewById(R.id.Custody_Auth8Note);
            app9Note = (TextView) itemView.findViewById(R.id.Custody_Auth9Note);
            app10Note = (TextView) itemView.findViewById(R.id.Custody_Auth10Note);
            L1 = (LinearLayout) itemView.findViewById(R.id.layout1);
            L2 = (LinearLayout) itemView.findViewById(R.id.layout2);
            L3 = (LinearLayout) itemView.findViewById(R.id.layout3);
            L4 = (LinearLayout) itemView.findViewById(R.id.layout4);
            L5 = (LinearLayout) itemView.findViewById(R.id.layout5);
            L6 = (LinearLayout) itemView.findViewById(R.id.layout6);
            L7 = (LinearLayout) itemView.findViewById(R.id.layout7);
            L8 = (LinearLayout) itemView.findViewById(R.id.layout8);
            L9 = (LinearLayout) itemView.findViewById(R.id.layout9);
            L10 = (LinearLayout) itemView.findViewById(R.id.layout10);
            R1 = (RadioButton) itemView.findViewById(R.id.radioButton3);
            R2 = (RadioButton) itemView.findViewById(R.id.radioButton30);
            R3 = (RadioButton) itemView.findViewById(R.id.radioButton300);
            R4 = (RadioButton) itemView.findViewById(R.id.radioButton3000);
            R5 = (RadioButton) itemView.findViewById(R.id.radioButton30000);
            R6 = (RadioButton) itemView.findViewById(R.id.radioButton300000);
            R7 = (RadioButton) itemView.findViewById(R.id.radioButton3000000);
            R8 = (RadioButton) itemView.findViewById(R.id.radioButton30000000);
            R9 = (RadioButton) itemView.findViewById(R.id.radioButton300000000);
            R10 = (RadioButton) itemView.findViewById(R.id.radioButton3000000000);
            R1.setEnabled(false);R2.setEnabled(false);R3.setEnabled(false);R4.setEnabled(false);R5.setEnabled(false);
            R6.setEnabled(false);R7.setEnabled(false);R8.setEnabled(false);R9.setEnabled(false);R10.setEnabled(false);
            radios.add(R1); radios.add(R2); radios.add(R3); radios.add(R4); radios.add(R5); radios.add(R6);
            radios.add(R7); radios.add(R8); radios.add(R9); radios.add(R10);
            layouts.add(L1); layouts.add(L2); layouts.add(L3); layouts.add(L4); layouts.add(L9);
            layouts.add(L5); layouts.add(L6); layouts.add(L7); layouts.add(L8);  layouts.add(L10);
            apps.add(app1); apps.add(app2); apps.add(app3); apps.add(app4); apps.add(app5);
            apps.add(app6); apps.add(app7); apps.add(app8); apps.add(app9); apps.add(app10);
            appsres.add(app1res); appsres.add(app2res); appsres.add(app3res); appsres.add(app4res); appsres.add(app5res);
            appsres.add(app6res);appsres.add(app7res);appsres.add(app8res);appsres.add(app9res);appsres.add(app10res);
            appsdate.add(app1date);appsdate.add(app2date);appsdate.add(app3date);appsdate.add(app4date);appsdate.add(app5date);
            appsdate.add(app6date);appsdate.add(app7date);appsdate.add(app8date);appsdate.add(app9date);appsdate.add(app10date);
            appsCaption.add(app1Caption); appsCaption.add(app2Caption); appsCaption.add(app3Caption); appsCaption.add(app4Caption); appsCaption.add(app5Caption);
            appsCaption.add(app6Caption); appsCaption.add(app7Caption); appsCaption.add(app8Caption); appsCaption.add(app9Caption); appsCaption.add(app10Caption);
            addNotes.add(app1Note);addNotes.add(app2Note);addNotes.add(app3Note);addNotes.add(app4Note);addNotes.add(app5Note);addNotes.add(app6Note);
            addNotes.add(app7Note);addNotes.add(app8Note);addNotes.add(app9Note);addNotes.add(app10Note);
            icon = (ImageView) itemView.findViewById(R.id.imageView11);
        }
    }
}
