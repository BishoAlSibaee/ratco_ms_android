package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.VolleyCallback;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExitRequest extends AppCompatActivity {

    Activity act ;
    TextView ExitDateTV , ExitTimeTV , BackTimeTV , NotesET ;
    String Date ,Time ,BTime;
    RequestQueue Q ;
    String saveExitRequestOrder = MyApp.MainUrl+"insertExitRequestOrder.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_exit_request_activity);
        setActivity();
        setActivityActions();
    }

    void setActivity() {
        act = this ;
        Q = Volley.newRequestQueue(act);
        ExitDateTV = (TextView) findViewById(R.id.Exit_exitDate);
        ExitTimeTV = (TextView) findViewById(R.id.Exit_exitTime);
        BackTimeTV = (TextView) findViewById(R.id.Exit_expectedBackTime);
        NotesET = (EditText) findViewById(R.id.Exit_reason);
    }

    void setActivityActions () {
        ExitDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        Date = year+"-"+(month+1)+"-"+dayOfMonth;
                        date.setText(Date);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExitDateTV.setText(Date);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        ExitTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.select_time_dialog);
                TimePicker picker=(TimePicker) D.findViewById(R.id.timePicker1);
                picker.setIs24HourView(true);
                TextView time = (TextView) D.findViewById(R.id.timeTV);
                picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay+":"+minute);
                        Time = hourOfDay+":"+minute ;
                    }
                });
                Button cancel = (Button) D.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Button select = (Button) D.findViewById(R.id.selectTim_select);
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Time != null ) {
                            ExitTimeTV.setText(Time);
                            D.dismiss();
                        }
                        else {
                            ToastMaker.Show(0,"select time",act);
                        }
                    }
                });
                D.show();
            }
        });
        BackTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.select_time_dialog);
                TimePicker picker=(TimePicker) D.findViewById(R.id.timePicker1);
                picker.setIs24HourView(true);
                TextView time = (TextView) D.findViewById(R.id.timeTV);
                picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay+":"+minute);
                        BTime = hourOfDay+":"+minute ;
                    }
                });
                Button cancel = (Button) D.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Button select = (Button) D.findViewById(R.id.selectTim_select);
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BTime != null ) {
                            BackTimeTV.setText(BTime);
                            D.dismiss();
                        }
                        else {
                            ToastMaker.Show(0,"select time",act);
                        }
                    }
                });
                D.show();
            }
        });
    }

    public void saveExiteRequest(View view) {

        if (ExitDateTV.getText() == null || ExitDateTV.getText().toString().isEmpty()) {
            ToastMaker.Show(1,"select date",act);
            return;
        }
        if (ExitTimeTV.getText() == null || ExitTimeTV.getText().toString().isEmpty()) {
            ToastMaker.Show(1,"select time",act);
            return;
        }
        if (BackTimeTV.getText() == null || BackTimeTV.getText().toString().isEmpty()) {
            ToastMaker.Show(1,"select back time",act);
            return;
        }

        Loading l = new Loading(act) ;
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST,saveExitRequestOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                    if (response.equals("1")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved),0);
                        MyApp.sendNotificationsToGroup(MyApp.ExitAuthUsers, getResources().getString(R.string.exitRequest), getResources().getString(R.string.exitRequest), MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "NewExitRequest", act, new VolleyCallback() {
                            @Override
                            public void onSuccess() {

                            }
                        });
                    }
                    else if (response.equals("0")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"not saved","not saved try again");
                    }
                    else if (response.equals("-1")) {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"not saved","error");
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
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Map<String,String> par = new HashMap<String, String>();
                par.put("EmpID", String.valueOf(MyApp.db.getUser().id));
                par.put("JobNumber", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Name" , MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName);
                par.put("DirectManager" , String.valueOf(MyApp.db.getUser().DirectManager));
                par.put("DirectManagerName" , MyApp.DIRECT_MANAGER.FirstName+" "+MyApp.DIRECT_MANAGER.LastName);
                par.put("Date" ,ExitDateTV.getText().toString());
                par.put("Time" , ExitTimeTV.getText().toString());
                par.put("BackTime", BackTimeTV.getText().toString());
                if (NotesET.getText() != null && !NotesET.getText().toString().isEmpty()) {
                    par.put("Notes" , NotesET.getText().toString());
                }
                return par;
            }
        };
        Q.add(request);
    }
}