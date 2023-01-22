package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.Attendances_Adapter;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyAttendTableAdabter;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyAttendTable extends AppCompatActivity {
    TextView StartTextView, EndTextView;
    Button btnGetAttend;
    private String getAttendacesUrl = "https://ratco-solutions.com/RatcoManagementSystem/getEmpAttendance.php";
    Activity act;
    public static List<ATTENDANCE_CLASS> attendanceClassList;
    RecyclerView RecShowAttend;
    LinearLayoutManager managerMyAttendTable;
    public static MyAttendTableAdabter adabter;
    private String Start = "", End = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attend_table);
        setActivity();

    }

    void setActivity() {
        StartTextView = findViewById(R.id.CheckAttendance_Start);
        EndTextView = findViewById(R.id.CheckAttendance_End);
        btnGetAttend = findViewById(R.id.btnGetAttend);
        attendanceClassList = new ArrayList<>();
        RecShowAttend = findViewById(R.id.RecShowAttend);
        act = this;
        managerMyAttendTable = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecShowAttend.setLayoutManager(managerMyAttendTable);
        StartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                D.setCancelable(false);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                Button cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button set = (Button) D.findViewById(R.id.SelectDateDialog_select);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        Start = year + "-" + (month + 1) + "-" + dayOfMonth;
                        date.setText(Start);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (date.getText() == null || date.getText().toString().isEmpty()) {
                            ToastMaker.Show(0, "select date", act);
                        } else {
                            StartTextView.setText(Start);
                            D.dismiss();
                        }
                    }
                });
                D.show();
            }
        });
        EndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                D.setCancelable(false);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                Button cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button set = (Button) D.findViewById(R.id.SelectDateDialog_select);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        End = year + "-" + (month + 1) + "-" + dayOfMonth;
                        date.setText(End);

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (date.getText() == null || date.getText().toString().isEmpty()) {
                            ToastMaker.Show(0, "select date", act);
                        } else {
                            EndTextView.setText(End);
                            D.dismiss();
                        }

                    }
                });
                D.show();
            }
        });
    }

    public void GetAttend(View view) {
        Loading loading = new Loading(act);
        if (StartTextView.getText().toString().isEmpty() || StartTextView.getText() == null) {
            ToastMaker.Show(1, "Select Start Date", act);
            return;
        }
        if (EndTextView.getText().toString().isEmpty() || EndTextView.getText() == null) {
            ToastMaker.Show(1, "Select End Date", act);
            return;
        }
        //   progressBarAtt.setVisibility(View.VISIBLE);
        loading.show();

        attendanceClassList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getAttendacesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getAttendanceResp", response);
                if (response.equals("0")) {
                    loading.close();
                    ToastMaker.Show(1, "No Records", act);
                    attendanceClassList.clear();
                    adabter.notifyDataSetChanged();
                    Log.d("getAttendanceResp", "zero ");
                } else if (response.equals("-1")) {
                    Log.d("getAttendanceResp", "-1");
                } else {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            //her is Result
                            attendanceClassList.add(new ATTENDANCE_CLASS(object.getInt("EmpID"),
                                    object.getString("Date"), object.getString("Time"), object.getInt("Operation")));
                        }
                        adabter = new MyAttendTableAdabter(attendanceClassList);
                        RecShowAttend.setAdapter(adabter);
                        loading.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("getAttendanceResp", e.toString());

                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getAttendanceResp", error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<String, String>();
                parms.put("EmpID", String.valueOf(MyApp.MyUser.id));
                parms.put("Start", StartTextView.getText().toString());
                parms.put("End", EndTextView.getText().toString());
                return parms;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }
}