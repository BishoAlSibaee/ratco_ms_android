package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.syrsoft.ratcoms.HR;
import com.syrsoft.ratcoms.HRActivities.Auth;
import com.syrsoft.ratcoms.HRActivities.MyApprovals;
import com.syrsoft.ratcoms.HRActivities.VACATION_CLASS;
import com.syrsoft.ratcoms.HRActivities.VacationForm;
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

public class MyApprovals_Vacation_Adapter extends RecyclerView.Adapter<MyApprovals_Vacation_Adapter.HOLDER> {

    List<VACATION_CLASS> list = new ArrayList<VACATION_CLASS>();
    private String LoginUrl = MyApp.MainUrl + "appLoginEmployees.php" ;
    private String responseVacationUrl = MyApp.MainUrl + "responseVacation.php";
    boolean[] status ;

    public MyApprovals_Vacation_Adapter(List<VACATION_CLASS> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyApprovals_Vacation_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_approval_vacation_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyApprovals_Vacation_Adapter.HOLDER holder, @SuppressLint("RecyclerView") int position) {
        status = new boolean[this.list.size()];
        for (int i=0;i<this.list.size();i++) {
            status[i] = false ;
        }
        holder.orderDate.setText(list.get(position).SendDate);
        holder.PDF.setVisibility(View.GONE);
        holder.order.setText(holder.itemView.getResources().getString(R.string.vacation));
        holder.name.setText(list.get(position).FName+" "+list.get(position).LName);
        holder.jNum.setText(list.get(position).JobNumber+"");
        holder.jTitle.setText(list.get(position).JobTitle);
        holder.location.setText(list.get(position).Location);
        holder.Notes.setText(list.get(position).Notes);
        String Type="";
        if (list.get(position).VacationType == 0 ){Type = "Annual";}else if (list.get(position).VacationType == 1){Type = "Emergency";}else if (list.get(position).VacationType == 2){Type="Sick";}
        holder.type.setText(Type);
        holder.days.setText(list.get(position).VacationDays+"");
        holder.start.setText(list.get(position).StartDate);
        holder.alt.setText(list.get(position).AlternativeName);

        for (int i=0 ; i< list.get(position).Auths.size();i++) {
            holder.layouts.get(i).setVisibility(View.GONE);
        }

        if (MyApprovals.Status == 1) {
            holder.BTNS.setVisibility(View.GONE);
        }

        if (MyApp.db.getUser().JobTitle.equals("Accountant") ||  MyApp.db.getUser().JobTitle.equals("Programmer")) {
           holder.PDF.setVisibility(View.VISIBLE);
        }

        if (MyApprovals.Status == 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status[position] == false) {

                        status[position] = true ;
                        holder.icon.setImageResource(R.drawable.bring_up_icon);
                        if (MyApp.VacationOrdersAuthUsers.get(position).size()>0) {
                            for (int i = 0; i< MyApp.VacationOrdersAuthUsers.get(position).size(); i++) {
                                //Log.d("VacationProblem" , MyApprovals.VacationOrdersAuthUsers.get(position).get(i).FirstName);
                                holder.layouts.get(i).setVisibility(View.VISIBLE);
                                holder.appsCaption.get(i).setText(MyApp.VacationsAuthJobtitles.get(i).JobTitle);
                                holder.apps.get(i).setText(MyApp.VacationOrdersAuthUsers.get(position).get(i).FirstName+" "+MyApp.VacationOrdersAuthUsers.get(position).get(i).LastName);
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
                        if (MyApprovals.VacationOrdersAuthUsers.get(position).size()>0) {
                            for (int i = 0; i< MyApprovals.VacationOrdersAuthUsers.get(position).size(); i++) {
                                holder.layouts.get(i).setVisibility(View.VISIBLE);
                                holder.appsCaption.get(i).setText(MyApp.VacationsAuthJobtitles.get(i).JobTitle);
                                holder.apps.get(i).setText(MyApprovals.VacationOrdersAuthUsers.get(position).get(i).FirstName+" "+MyApprovals.VacationOrdersAuthUsers.get(position).get(i).LastName);
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
                if (holder.myNotes.getText() == null || holder.myNotes.getText().toString().isEmpty() ) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(holder.itemView.getContext(),"Please Enter Note","");
                    return;
                }
                int index = 0 ;
//                for(int i=0;i<MyApprovals.VacationOrdersAuthUsers.get(position).size();i++) {
//                    if (MyApp.db.getUser().id == MyApprovals.VacationOrdersAuthUsers.get(position).get(i).id) {
//                        index = i ;
//                    }
//                }
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
//                int index = 0 ;
//
//                for(int i=0;i<MyApprovals.VacationOrdersAuthUsers.get(position).size();i++) {
//                    if (MyApp.db.getUser().id == MyApprovals.VacationOrdersAuthUsers.get(position).get(i).id) {
//                        index = i+1 ;
//
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
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(holder.itemView.getContext(),"This Order Rejected","");
                }


            }
        });

        holder.PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), VacationForm.class);
                i.putExtra("Position" , position);
                holder.itemView.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView orderDate,order , name , jNum , jTitle , location , Notes , type,days,start,alt,app1,app1res,app1date,app2,app2res,app2date,app3,app3res,app3date,app4,app4res,app4date,app5,app5res,app5date,app6,app6res,app6date,app7,app7res,app7date,app8,app8res,app8date,app9,app9res,app9date,app10,app10res,app10date,app1Note,app2Note,app3Note,app4Note,app5Note,app6Note,app7Note,app8Note,app9Note,app10Note;
        TextView app1Caption,app2Caption,app3Caption,app4Caption,app5Caption,app6Caption,app7Caption,app8Caption,app9Caption,app10Caption ;
        List<TextView> apps = new ArrayList<TextView>();
        List<TextView> appsres = new ArrayList<TextView>();
        List<TextView> appsdate = new ArrayList<TextView>();
        List<TextView> appsCaption = new ArrayList<TextView>();
        List<TextView> appNotes = new ArrayList<TextView>();
        RadioButton R1,R2,R3,R4,R5,R6,R7,R8,R9,R10 ;
        LinearLayout L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,BTNS ;
        List<LinearLayout> layouts = new ArrayList<LinearLayout>();
        List<RadioButton> radios = new ArrayList<RadioButton>();
        ImageView icon ;
        Button reject , approve ;
        EditText myNotes ;
        CardView PDF ;
        public HOLDER(@NonNull View itemView)
        {
            super(itemView);
            orderDate = (TextView)itemView.findViewById(R.id.orderDate);
            PDF = (CardView) itemView.findViewById(R.id.myApprovalUnit_PDFbtn);
            myNotes = (EditText) itemView.findViewById(R.id.MyApproval_VS_unit_myNotes);
            BTNS = (LinearLayout) itemView.findViewById(R.id.BtnsLayoutm);
            order = (TextView)itemView.findViewById(R.id.VacationUnit_order);
            name = (TextView)itemView.findViewById(R.id.VacationUnit_name);
            jNum = (TextView)itemView.findViewById(R.id.VacationUnit_jobNum);
            jTitle = (TextView)itemView.findViewById(R.id.VacationUnit_jobTitle);
            location = (TextView)itemView.findViewById(R.id.VacationUnit_location);
            start = (TextView)itemView.findViewById(R.id.VacationUnit_sendDate);
            alt = (TextView)itemView.findViewById(R.id.VacationUnit_alternative);
            days = (TextView)itemView.findViewById(R.id.VacationUnit_days);
            type = (TextView)itemView.findViewById(R.id.VacationUnit_vacationType);
            Notes = (TextView) itemView.findViewById(R.id.VacationUnit_notes);
            icon = (ImageView) itemView.findViewById(R.id.imageView15);
            app1 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth1ID0);
            app1res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth1Res0);
            app1date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth1Date0);
            app2 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth2ID0);
            app2res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth2Res0);
            app2date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth2Date0);
            app3 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth3ID0);
            app3res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth3Res0);
            app3date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth3Date0);
            app4 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth4ID0);
            app4res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth4Res0);
            app4date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth4Date0);
            app5 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth5ID0);
            app5res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth5Res0);
            app5date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth5Date0);
            app6 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth6ID0);
            app6res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth6Res0);
            app6date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth6Date0);
            app7 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth7ID);
            app7res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth7Res);
            app7date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth7Date);
            app8 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth8ID);
            app8res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth8Res);
            app8date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth8Date);
            app9 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth9ID);
            app9res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth9Res);
            app9date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth9Date);
            app10 = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth10ID);
            app10res = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth10Res);
            app10date = (TextView) itemView.findViewById(R.id.MyApprovals_vacation_Auth10Date);
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
            reject = (Button)itemView.findViewById(R.id.VacationUnit_reject);
            approve = (Button)itemView.findViewById(R.id.VacationUnit_approve);
            //directcaption = (TextView)itemView.findViewById(R.id.textView27);
            //directdatecaption = (TextView)itemView.findViewById(R.id.textView26);
        }
    }

}
