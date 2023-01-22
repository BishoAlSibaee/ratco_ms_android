package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.syrsoft.ratcoms.HR;
import com.syrsoft.ratcoms.HRActivities.Auth;
import com.syrsoft.ratcoms.HRActivities.MyApprovals;
import com.syrsoft.ratcoms.HRActivities.RESIGNATION_CLASS;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyApprovals_Resignation_Adapter  extends RecyclerView.Adapter<MyApprovals_Resignation_Adapter.HOLDER> {

    List<RESIGNATION_CLASS> list = new ArrayList<RESIGNATION_CLASS>();
    String ResponseResignation = "https://ratco-solutions.com/RatcoManagementSystem/responseResignation.php" ;
    private String LoginUrl = "https://ratco-solutions.com/RatcoManagementSystem/appLoginEmployees.php" ;
    boolean[] status ;

    public MyApprovals_Resignation_Adapter(List<RESIGNATION_CLASS> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyApprovals_Resignation_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_approval_resignation_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyApprovals_Resignation_Adapter.HOLDER holder, int position) {

        status = new boolean[this.list.size()];
        for (int i=0;i<this.list.size();i++) {
            status[i] = false ;
        }
        holder.order.setText(holder.itemView.getResources().getString(R.string.resignation));
        holder.jnum.setText(String.valueOf(list.get(position).JobNumber));
        holder.jtitle.setText(list.get(position).JobTitle);
        holder.fname.setText(list.get(position).FName);
        holder.lname.setText(list.get(position).LName);
        holder.reason.setText(list.get(position).ResignationReason);
        holder.date.setText(list.get(position).ResignationDate);
        holder.sendDate.setText(list.get(position).ResignationSendDate);
        holder.icon.setImageResource(R.drawable.drop_down_icon);
        for (int i=0 ; i< list.get(position).Auths.size();i++) {
            holder.layouts.get(i).setVisibility(View.GONE);
        }

        if (MyApprovals.Status == 1 ) {
            holder.BTNS.setVisibility(View.GONE);
        }

        if (MyApprovals.Status == 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status[position] == false) {
                        status[position] = true ;
                        holder.icon.setImageResource(R.drawable.bring_up_icon);
                        //Log.d("theProblem" ,MyApp.ResignationsAuthsJobtitles.size() +" "+HR.ResignationsAuthUsers.size());
                        if (MyApp.ResignationOrdersAuthUsers.get(position).size()>0) {
                            for (int i = 0; i< MyApp.ResignationOrdersAuthUsers.get(position).size(); i++) {

                                holder.layouts.get(i).setVisibility(View.VISIBLE);
                                holder.appsCaption.get(i).setText(MyApp.ResignationsAuthsJobtitles.get(i).JobTitle);
                                holder.apps.get(i).setText(MyApp.ResignationOrdersAuthUsers.get(position).get(i).FirstName+" "+MyApp.ResignationOrdersAuthUsers.get(position).get(i).LastName);
                                holder.appsres.get(i).setText(list.get(position).Auths.get(i).getAuthResult());
                                holder.appsdate.get(i).setText(list.get(position).Auths.get(i).getAuthDate());
                                holder.appNotes.get(i).setText(list.get(position).Auths.get(i).Note);
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
                        }
                    }
                    else {
                        status[position] = false ;
                        holder.icon.setImageResource(R.drawable.drop_down_icon);
                        for (int i=0 ; i< list.get(position).Auths.size();i++) {
                            holder.layouts.get(i).setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
        else if (MyApprovals.Status == 1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status[position] == false) {
                        status[position] = true ;
                        holder.icon.setImageResource(R.drawable.bring_up_icon);
                        //Log.d("theProblem" ,MyApp.ResignationsAuthsJobtitles.size() +" "+HR.ResignationsAuthUsers.size());
                        if (MyApprovals.ResignationOrdersAuthUsers.get(position).size()>0) {
                            for (int i = 0; i< MyApprovals.ResignationOrdersAuthUsers.get(position).size(); i++) {

                                holder.layouts.get(i).setVisibility(View.VISIBLE);
                                holder.appsCaption.get(i).setText(MyApp.ResignationsAuthsJobtitles.get(i).JobTitle);
                                holder.apps.get(i).setText(MyApprovals.ResignationOrdersAuthUsers.get(position).get(i).FirstName+" "+MyApprovals.ResignationOrdersAuthUsers.get(position).get(i).LastName);
                                holder.appsres.get(i).setText(list.get(position).Auths.get(i).getAuthResult());
                                holder.appsdate.get(i).setText(list.get(position).Auths.get(i).getAuthDate());
                                holder.appNotes.get(i).setText(list.get(position).Auths.get(i).Note);
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
                        }
                    }
                    else {
                        status[position] = false ;
                        holder.icon.setImageResource(R.drawable.drop_down_icon);
                        for (int i=0 ; i< list.get(position).Auths.size();i++) {
                            holder.layouts.get(i).setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApprovals.POSITION = position ;
//                int index = 0 ;
//                for(int i=0;i<MyApprovals.ResignationOrdersAuthUsers.get(position).size();i++) {
//                    if (MyApp.db.getUser().id == MyApprovals.ResignationOrdersAuthUsers.get(position).get(i).id) {
//                        //Auth a = new Auth();
//                        index = i ;
//                    }
//                }
                if (holder.myNotes.getText() == null || holder.myNotes.getText().toString().isEmpty() ) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(holder.itemView.getContext(),"Please Enter Note","");
                    return;
                }
                boolean result = true ;
                for (Auth a : list.get(position).Auths) {
                    if (a.getAuth() == 2 ) {
                        result = false ;
                    }
                }
                if (result) {
                    MyApprovals.AorR = true ;
                    if (MyApprovals.biometricAvailable) {
                        MyApprovals.showBiometricAuth(list.get(position),holder.myNotes.getText().toString());
                    }
                    else {
                        MyApprovals.confermByPassword(list.get(position),holder.myNotes.getText().toString(),(Activity)holder.itemView.getContext());
                    }
                }
                else {
                    //MESSAGE_DIALOG m = new MESSAGE_DIALOG(holder.itemView.getContext(),holder.itemView.getContext().getResources().getString(R.string.previousAuthsMustDone),"");
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(holder.itemView.getContext(),"This Order Rejected","");
                }

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApprovals.POSITION = position ;
                if (holder.myNotes.getText() == null || holder.myNotes.getText().toString().isEmpty() ) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(holder.itemView.getContext(),"Please Enter Note","");
                    return;
                }
                boolean result = true ;
                for (Auth a : list.get(position).Auths) {
                    if (a.getAuth() == 2 ) {
                        result = false ;
                    }
                }
                if (result) {
                    MyApprovals.AorR = false ;
                    if (MyApprovals.biometricAvailable) {
                        MyApprovals.showBiometricAuth(list.get(position),holder.myNotes.getText().toString());
                    }
                    else {
                        MyApprovals.confermByPassword(list.get(position),holder.myNotes.getText().toString(),(Activity)holder.itemView.getContext());
                    }
                }
                else {
                    //MESSAGE_DIALOG m = new MESSAGE_DIALOG(holder.itemView.getContext(),holder.itemView.getContext().getResources().getString(R.string.previousAuthsMustDone),"");
                    new MESSAGE_DIALOG(holder.itemView.getContext(),"This Order Rejected","");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {

        TextView order , jnum , jtitle , fname , lname , reason , date , sendDate , app1,app1res,app1date,app2,app2res,app2date,app3,app3res,app3date,app4,app4res,app4date,app5,app5res,app5date,app6,app6res,app6date,app7,app7res,app7date,app8,app8res,app8date,app9,app9res,app9date,app10,app10res,app10date,app1Note,app2Note,app3Note,app4Note,app5Note,app6Note,app7Note,app8Note,app9Note,app10Note ;
        TextView app1Caption,app2Caption,app3Caption,app4Caption,app5Caption,app6Caption,app7Caption,app8Caption,app9Caption,app10Caption ;
        RadioButton R1,R2,R3,R4,R5,R6,R7,R8,R9,R10 ;
        LinearLayout L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,BTNS ;
        List<TextView> apps = new ArrayList<TextView>();
        List<TextView> appsres = new ArrayList<TextView>();
        List<TextView> appsdate = new ArrayList<TextView>();
        List<TextView> appsCaption = new ArrayList<TextView>();
        List<TextView> appNotes = new ArrayList<TextView>();
        List<LinearLayout> layouts = new ArrayList<LinearLayout>();
        List<RadioButton> radios = new ArrayList<RadioButton>();
        ImageView icon ;
        Button approve , reject ;
        EditText myNotes ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            BTNS = (LinearLayout) itemView.findViewById(R.id.BtnsLayoutt);
            myNotes = (EditText) itemView.findViewById(R.id.MyApproval_VS_unit_myNotes);
            order = (TextView) itemView.findViewById(R.id.MyApprovals_Resignation_order);
            jnum = (TextView) itemView.findViewById(R.id.MyApprovals_Resignation_jobNumber);
            jtitle = (TextView) itemView.findViewById(R.id.MyApprovals_Resignation_jobTitle);
            fname = (TextView) itemView.findViewById(R.id.MyApprovals_Resignation_fname);
            lname = (TextView) itemView.findViewById(R.id.MyApprovals_Resignation_lname);
            reason = (TextView) itemView.findViewById(R.id.MyApprovals_Resignation_reason);
            date = (TextView) itemView.findViewById(R.id.MyApprovals_Resignation_date);
            sendDate = (TextView) itemView.findViewById(R.id.MyApprovals_Resignation_sendDate);
            icon = (ImageView) itemView.findViewById(R.id.imageView14);
            app1 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth1ID0);
            app1res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth1Res0);
            app1date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth1Date0);
            app2 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth2ID0);
            app2res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth2Res0);
            app2date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth2Date0);
            app3 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth3ID0);
            app3res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth3Res0);
            app3date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth3Date0);
            app4 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth4ID0);
            app4res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth4Res0);
            app4date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth4Date0);
            app5 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth5ID0);
            app5res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth5Res0);
            app5date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth5Date0);
            app6 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth6ID0);
            app6res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth6Res0);
            app6date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth6Date0);
            app7 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth7ID);
            app7res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth7Res);
            app7date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth7Date);
            app8 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth8ID);
            app8res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth8Res);
            app8date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth8Date);
            app9 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth9ID);
            app9res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth9Res);
            app9date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth9Date);
            app10 = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth10ID);
            app10res = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth10Res);
            app10date = (TextView) itemView.findViewById(R.id.MyApprovals_resignation_Auth10Date);
            app1Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth1Note);
            app2Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth2Note);
            app3Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth3Note);
            app4Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth4Note);
            app5Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth5Note);
            app6Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth6Note);
            app7Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth7Note);
            app8Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth8Note);
            app9Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth9Note);
            app10Note = (TextView) itemView.findViewById(R.id.MyApprovals_Custody_Auth10Note);
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
            layouts.add(L1); layouts.add(L2); layouts.add(L3); layouts.add(L4); layouts.add(L9);
            layouts.add(L5); layouts.add(L6); layouts.add(L7); layouts.add(L8);  layouts.add(L10);
            radios.add(R1); radios.add(R2); radios.add(R3); radios.add(R4); radios.add(R5); radios.add(R6);
            radios.add(R7); radios.add(R8); radios.add(R9); radios.add(R10);
            app1Caption = (TextView)itemView.findViewById(R.id.textView27);
            app2Caption = (TextView) itemView.findViewById(R.id.textView24);
            app3Caption = (TextView) itemView.findViewById(R.id.textView270);
            app4Caption = (TextView) itemView.findViewById(R.id.textView240);
            app5Caption = (TextView) itemView.findViewById(R.id.textView271);
            app6Caption = (TextView) itemView.findViewById(R.id.textView241);
            app7Caption = (TextView) itemView.findViewById(R.id.textView2715);
            app8Caption = (TextView) itemView.findViewById(R.id.textView2415);
            app9Caption = (TextView) itemView.findViewById(R.id.textView2714);
            app10Caption = (TextView) itemView.findViewById(R.id.textView2414);
            apps.add(app1); apps.add(app2); apps.add(app3); apps.add(app4); apps.add(app5);
            apps.add(app6); apps.add(app7); apps.add(app8); apps.add(app9); apps.add(app10);
            appsres.add(app1res); appsres.add(app2res); appsres.add(app3res); appsres.add(app4res); appsres.add(app5res);
            appsres.add(app6res);appsres.add(app7res);appsres.add(app8res);appsres.add(app9res);appsres.add(app10res);
            appsdate.add(app1date);appsdate.add(app2date);appsdate.add(app3date);appsdate.add(app4date);appsdate.add(app5date);
            appsdate.add(app6date);appsdate.add(app7date);appsdate.add(app8date);appsdate.add(app9date);appsdate.add(app10date);
            appsCaption.add(app1Caption); appsCaption.add(app2Caption); appsCaption.add(app3Caption); appsCaption.add(app4Caption); appsCaption.add(app5Caption);
            appsCaption.add(app6Caption); appsCaption.add(app7Caption); appsCaption.add(app8Caption); appsCaption.add(app9Caption); appsCaption.add(app10Caption);
            appNotes.add(app1Note);appNotes.add(app2Note);appNotes.add(app3Note);appNotes.add(app4Note);appNotes.add(app5Note);appNotes.add(app6Note);
            appNotes.add(app7Note);appNotes.add(app8Note);appNotes.add(app9Note);appNotes.add(app10Note);
            approve = (Button)itemView.findViewById(R.id.MyApprovals_Resignation_approve);
            reject = (Button) itemView.findViewById(R.id.MyApprovals_Resignation_reject);
        }
    }
}