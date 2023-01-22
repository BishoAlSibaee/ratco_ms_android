package com.syrsoft.ratcoms.HRActivities.HR_Adapters;

import android.app.Dialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MainPage;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.WARNING_CLASS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ManageContracts_Adapter extends RecyclerView.Adapter<ManageContracts_Adapter.HOLDER>
{

    List<USER> list = new ArrayList<USER>();
    String updateContractExpireDateUrl = "https://ratco-solutions.com/RatcoManagementSystem/updateContractExpireDate.php";

    public ManageContracts_Adapter(List<USER> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ManageContracts_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_ids_unit,parent,false);
        HOLDER holder = new HOLDER(v);
        return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageContracts_Adapter.HOLDER holder, int position)
    {
        holder.jNum.setText(list.get(position).JobNumber+"");
        holder.Name.setText(list.get(position).FirstName + " " + list.get(position).LastName);
        holder.ExpireDate.setText(list.get(position).ContractExpireDate);
        Calendar ec = Calendar.getInstance();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(position).ContractExpireDate);
            ec.setTime(date);
            ec.add(Calendar.MONTH,-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance(Locale.getDefault());
        //c.add(Calendar.MONTH,-1);
        Log.d("dates" , "E date "+ec.getTime().toString()+" befor one month"+c.getTime().toString());
        if (c.after(ec)){
            holder.ExpireDate.setTextColor(Color.RED);
        }
        else {
            holder.ExpireDate.setTextColor(Color.BLUE);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Dialog d = new Dialog(holder.itemView.getContext());
                d.setContentView(R.layout.update_expire_date_dialog);
                TextView jn = (TextView) d.findViewById(R.id.updateDate_jnum);
                TextView name = (TextView) d.findViewById(R.id.updateDate_name);
                TextView date = (TextView) d.findViewById(R.id.updateDate_endDate);
                EditText newDate = (EditText) d.findViewById(R.id.updateDate_NewDate);
                Button cancel = (Button) d.findViewById(R.id.updateDate_cancel);
                Button send = (Button) d.findViewById(R.id.updateDate_send);
                jn.setText(String.valueOf(list.get(position).JobNumber));
                name.setText(list.get(position).FirstName+" "+list.get(position).LastName);
                date.setText(list.get(position).ContractExpireDate);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newDate.getText() == null || newDate.getText().toString().isEmpty() ){
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(holder.itemView.getContext(),holder.itemView.getContext().getResources().getString(R.string.enterNewDate),"");
                            return;
                        }
                        Loading lll = new Loading(holder.itemView.getContext()) ; lll.show();
                        StringRequest re = new StringRequest(Request.Method.POST, updateContractExpireDateUrl , new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                if (response.equals("1"))
                                {
                                    lll.close();
                                    d.dismiss();
                                    ToastMaker.Show(1,"Updated" , holder.itemView.getContext());
                                    holder.ExpireDate.setText(newDate.getText().toString());
                                    for(int i=0;i< MainPage.WarningList.size();i++){
                                        if (MainPage.WarningList.get(i).JobNumber == list.get(position).JobNumber && MainPage.WarningList.get(i).warning.equals("Passport")){
                                            MainPage.WarningList.remove(MainPage.WarningList.get(i));
                                            MainPage.warningADApter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                else if (response.equals("0"))
                                {
                                    lll.close();
                                    ToastMaker.Show(1,"Error" , holder.itemView.getContext());
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                lll.close();
                            }
                        })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> par = new HashMap<String, String>();
                                par.put("id" , String.valueOf(list.get(position).id));
                                par.put("newDate" , newDate.getText().toString());
                                par.put("jnum" , String.valueOf(list.get(position).JobNumber));
                                return par ;
                            }
                        };

                        Volley.newRequestQueue(holder.itemView.getContext()).add(re);
                    }
                });
                d.show();
                return false ;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView jNum , Name , ExpireDate ;
        public HOLDER(@NonNull View itemView) {
            super(itemView);
            jNum = (TextView) itemView.findViewById(R.id.manage_ids_Jnum);
            Name = (TextView) itemView.findViewById(R.id.manage_ids_name);
            ExpireDate = (TextView) itemView.findViewById(R.id.manage_ids_expireDate);
        }
    }
}
