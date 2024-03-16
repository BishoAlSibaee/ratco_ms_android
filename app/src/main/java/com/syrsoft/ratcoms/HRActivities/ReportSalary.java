package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class ReportSalary extends AppCompatActivity {
    Activity act;
    Spinner spinnerMonth, spinnerYear;
    List<String> monthList = new ArrayList<>();
    List<String> yearList = new ArrayList<>();
    String m, y;
    TextView txtAbsence, txtAbsencePenalty, txtLate, txtLatePenalty, txtAdvancePenalty, txtCustodyPenalty, txtTotal, txtSalaryAfterPenalty, txtNotes;
    LinearLayout LinearInfo;
    final String getEmployeeSalaryReports = "https://ratco-solutions.com/RatcoManagementSystem/NewOptions/getEmployeeSalaryReports.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_salary);
        setActivity();
        setActivityActions();
    }

    void setActivity() {
        act = this;
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        txtAbsencePenalty = findViewById(R.id.txtAbsencePenalty);
        txtAbsence = findViewById(R.id.txtAbsence);
        txtLate = findViewById(R.id.txtLate);
        txtLatePenalty = findViewById(R.id.txtLatePenalty);
        txtAdvancePenalty = findViewById(R.id.txtAdvancePenalty);
        txtCustodyPenalty = findViewById(R.id.txtCustodyPenalty);
        txtTotal = findViewById(R.id.txtTotal);
        txtSalaryAfterPenalty = findViewById(R.id.txtSalaryAfterPenalty);
        LinearInfo = findViewById(R.id.LinearInfo);
        txtNotes = findViewById(R.id.txtNotes);
        LinearInfo.setVisibility(View.GONE);
        getMonth();
        getYears();
    }

    void setActivityActions() {
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                y = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void getMonth() {
        Calendar ca = Calendar.getInstance(Locale.getDefault());
        int CurrentMonth = (ca.get(Calendar.MONTH) + 1);
        for (int i = 1; i <= 12; i++) {
            monthList.add(String.valueOf(i));
        }
        ArrayAdapter<String> monthArr = new ArrayAdapter<>(this, R.layout.spinner_item, monthList);
        spinnerMonth.setAdapter(monthArr);
        spinnerMonth.setSelection(CurrentMonth - 1);
    }

    void getYears() {
        for (int i = 2024; i <= 2034; i++) {
            yearList.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearArr = new ArrayAdapter<>(this, R.layout.spinner_item, yearList);
        spinnerYear.setAdapter(yearArr);
    }

    public void getReportSalary(View view) {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getEmployeeSalaryReports, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("0")) {
                    l.close();
                    if (LinearInfo.getVisibility() == View.VISIBLE) {
                        LinearInfo.setVisibility(View.GONE);
                    }
                    ToastMaker.Show(0, "No Result", act);
                } else {
                    clearAllVariable();
                    if (LinearInfo.getVisibility() == View.GONE) {
                        LinearInfo.setVisibility(View.VISIBLE);
                    }
                    l.close();
                    Log.d("Response Salary", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        txtAbsence.setText(String.valueOf(jsonObject.getInt("Absence")));
                        txtAbsencePenalty.setText(String.valueOf(jsonObject.getDouble("AbsencePenalty")));
                        txtLate.setText(jsonObject.getString("Late"));
                        txtLatePenalty.setText(String.valueOf(jsonObject.getDouble("LatePenalty")));
                        txtAdvancePenalty.setText(String.valueOf(jsonObject.getDouble("AdvancePenalty")));
                        txtCustodyPenalty.setText(String.valueOf(jsonObject.getDouble("CustodyPenalty")));
                        txtTotal.setText(String.valueOf(jsonObject.getDouble("Total")));
                        txtSalaryAfterPenalty.setText(String.valueOf(jsonObject.getDouble("SalaryAfterPenalty")));
                        txtNotes.setText(jsonObject.getString("Notes"));
                    } catch (JSONException e) {
                        LinearInfo.setVisibility(View.GONE);
                        throw new RuntimeException(e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LinearInfo.setVisibility(View.GONE);
                Log.d("Response Salary", "Error : " + error);
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("ID_User", String.valueOf(MyApp.db.getUser().id));
                par.put("Year", y);
                par.put("Month", m);
                return par;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    void clearAllVariable() {
        txtAbsence.setText("");
        txtAbsencePenalty.setText("");
        txtLate.setText("");
        txtLatePenalty.setText("");
        txtAdvancePenalty.setText("");
        txtCustodyPenalty.setText("");
        txtTotal.setText("");
        txtSalaryAfterPenalty.setText("");
        txtNotes.setText("");
    }
}