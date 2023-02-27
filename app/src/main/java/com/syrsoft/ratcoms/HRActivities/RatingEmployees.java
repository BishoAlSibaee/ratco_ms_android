package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RatingEmployees extends AppCompatActivity {
    Activity act;
    Spinner EmpsSpinner, spinnerMonth;
    String getMyStaffUrl = MyApp.MainUrl + "getMyStaff.php";
    String getEmployeeRatingsByMonth = MyApp.MainUrl + "getEmployeeRatingByMonth.php";
    String getRatingCriteriaUrl = MyApp.MainUrl + "getRatingCriteria.php";
    String insertNewEmpRatingUrl = MyApp.MainUrl + "insertEmployeeRating.php";
    List<USER> myStaffList;
    String[] myStaffArray;
    USER selectedUser;
    List<EmployeeRating> RATES;
    LinearLayout RatingLayout;
    List<RatingCriteria> RatingCriterias;
    List<SeekBar> SeekBarsList;
    TextView RatingResult;
    List<String> listDate = new ArrayList<>();
    String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_rating_employees_activity);
        setActivity();
        setActivityActions();
        getRatingCriteria();
        getMonth();
    }

    void setActivity() {
        act = this;
        EmpsSpinner = (Spinner) findViewById(R.id.spinner);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        RatingLayout = (LinearLayout) findViewById(R.id.ratingLayout);
        RatingResult = (TextView) findViewById(R.id.textView83);
        RatingCriterias = new ArrayList<RatingCriteria>();
        myStaffList = new ArrayList<USER>();
        SeekBarsList = new ArrayList<SeekBar>();
        RATES = new ArrayList<EmployeeRating>();
    }

    void setActivityActions() {
        EmpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("selectEmp", myStaffList.get(position).FirstName + " ");
                selectedUser = myStaffList.get(position);
                getEmployeeRating(selectedUser.JobNumber, selectedUser.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void getMyStaff() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getMyStaffUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("myStaffResponse" , response );
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1, getResources().getString(R.string.youHaveNoStaff), act);
                } else if (response.equals("-1")) {
                    ToastMaker.Show(1, getResources().getString(R.string.orderNotSent), act);
                } else {
                    List<Object> lis = JsonToObject.translate(response, USER.class, act);
                    if (lis.size() > 0) {
                        myStaffArray = new String[lis.size()];
                        myStaffList.clear();
                        for (int i = 0; i < lis.size(); i++) {
                            USER r = (USER) lis.get(i);
                            myStaffArray[i] = r.FirstName + " " + r.LastName;
                            myStaffList.add(r);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, R.layout.spinner_item, myStaffArray);
                        EmpsSpinner.setAdapter(adapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                //Log.d("myStaffResponse" , error.toString() );
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("JobNumber", String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getRatingCriteria() {
        Loading l = new Loading(act);
        l.show();
        StringRequest req = new StringRequest(Request.Method.POST, getRatingCriteriaUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("ratingCriteria",response);
                l.close();
                if (!response.equals("0")) {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            RatingCriteria cr = new RatingCriteria(row.getInt("id"), row.getString("Arabic"), row.getString("English"));
                            RatingCriterias.add(cr);
                        }
                        if (RatingCriterias.size() > 0) {
                            getMyStaff();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                //Log.d("ratingCriteria",error.toString());
                getRatingCriteria();
            }
        });
        Volley.newRequestQueue(act).add(req);
    }

    void drawRatingCriteria() {
        SeekBarsList.clear();
        RatingLayout.removeAllViews();
        final int[] ratingSum = {0};
        if (RatingCriterias.size() > 0) {
            for (int i = 0; i < RatingCriterias.size(); i++) {
                View v = LayoutInflater.from(act).inflate(R.layout.hr_rating_emps_unit, null);
                TextView type = v.findViewById(R.id.textView80);
                type.setText(RatingCriterias.get(i).getArabic());
                SeekBar rating = v.findViewById(R.id.seekBar);
                rating.setMax(10);
                rating.setProgress(0);
                TextView value = v.findViewById(R.id.textView81);
                value.setText("0");
                rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        ratingSum[0] = ratingSum[0] - Integer.parseInt(value.getText().toString());
                        ratingSum[0] = ratingSum[0] + progress;
                        RatingResult.setText(ratingSum[0] / RatingCriterias.size() + "");
                        value.setText(String.valueOf(progress));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                v.setPadding(0, 20, 0, 0);
                ratingSum[0] = ratingSum[0] + rating.getProgress();
                SeekBarsList.add(rating);
                RatingLayout.addView(v);
            }
            RatingResult.setText(ratingSum[0] / RatingCriterias.size() + "");
        }
    }

    void drawOldRating(List<EmployeeRating> rates) {
        SeekBarsList.clear();
        RatingLayout.removeAllViews();
        int ratesSum = 0;
        if (rates.size() > 0) {
            for (int i = 0; i < rates.size(); i++) {
                ratesSum = ratesSum + rates.get(i).getRating();
                View v = LayoutInflater.from(act).inflate(R.layout.hr_rating_emps_unit, null);
                TextView type = v.findViewById(R.id.textView80);
                type.setText(rates.get(i).getType());
                SeekBar rating = v.findViewById(R.id.seekBar);
                rating.setMax(10);
                rating.setProgress(rates.get(i).getRating());
                TextView value = v.findViewById(R.id.textView81);
                value.setText(String.valueOf(rates.get(i).getRating()));
                rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value.setText(String.valueOf(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                rating.setEnabled(false);
                v.setPadding(0, 20, 0, 0);
                SeekBarsList.add(rating);
                RatingLayout.addView(v);
            }
            RatingResult.setText(ratesSum / rates.size() + "");
        }
    }

    void getEmployeeRating(int jn, int eid) {
        if (selectedUser != null) {
            RATES.clear();
            Loading l = new Loading(act);
            l.show();
            StringRequest req = new StringRequest(Request.Method.POST, getEmployeeRatingsByMonth, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    l.close();
                    if (response.equals("0")) {
                        drawRatingCriteria();
                        Button saveButton = (Button) findViewById(R.id.button40);
                        saveButton.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            JSONArray res = new JSONArray(response);
                            for (int i = 0; i < res.length(); i++) {
                                JSONObject row = res.getJSONObject(i);
                                RATES.add(new EmployeeRating(row.getInt("id"), row.getInt("EmpID"), row.getInt("JobNumber"), row.getInt("Month"), row.getInt("Year"), row.getString("Date"), row.getString("Type"), row.getInt("Rating")));
                            }
                            drawOldRating(RATES);
                            Button saveButton = (Button) findViewById(R.id.button40);
                            saveButton.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("employeeRates", error.toString());
                    l.close();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Calendar ca = Calendar.getInstance(Locale.getDefault());
                    Log.d("employeeRates", eid + " " + jn + " " + ca.get(Calendar.MONTH));
                    Map<String, String> par = new HashMap<String, String>();
                    par.put("EmpID", String.valueOf(eid));
                    par.put("JobNumber", String.valueOf(jn));
                    par.put("Month", String.valueOf(ca.get(Calendar.MONTH)));
                    par.put("Year", String.valueOf(ca.get(Calendar.YEAR)));
                    return par;
                }
            };
            Volley.newRequestQueue(act).add(req);
        }
    }

    public void saveRating(View view) {
        boolean s = false;
        for (int i = 0; i < SeekBarsList.size(); i++) {
            if (SeekBarsList.get(i).getProgress() > 0) {
                s = true;
            }
            if (s) {
                break;
            }
        }

        if (!s) {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.noRatingTitle), getResources().getString(R.string.noRatingTitle));
        } else {
            if (RatingCriterias.size() > 0) {
                Loading l = new Loading(act);
                l.show();
                Calendar ca = Calendar.getInstance(Locale.getDefault());
                String Date = ca.get(Calendar.YEAR) + "-" + (ca.get(Calendar.MONTH) + 1) + "-" + ca.get(Calendar.DAY_OF_MONTH);
                RATES.clear();
                for (int i = 0; i < RatingCriterias.size(); i++) {
                    EmployeeRating e = new EmployeeRating(i + 1, selectedUser.id, selectedUser.JobNumber, Integer.parseInt(month) , ca.get(Calendar.YEAR), Date, RatingCriterias.get(i).getEnglish(), SeekBarsList.get(i).getProgress());
                    RATES.add(e);
                }
                StringRequest req = new StringRequest(Request.Method.POST, insertNewEmpRatingUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        l.close();
                        //ToastMaker.Show(1,response,act);
                        Log.d("saveRatingError", response);
                        if (response.equals("-3")) {
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "Already Saved", "This employee rating already saved");
                        } else if (!response.contains("0")) {
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "Done", getResources().getString(R.string.saved));
                        } else {
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "Save Failed", "Rating Save Failed .. please try later");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        l.close();
                        //ToastMaker.Show(1,error.toString(),act);
                        Log.d("saveRatingError", error.toString());
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        Map<String, String> par = new HashMap<String, String>();
                        par.put("count", String.valueOf(RATES.size()));
                        for (int i = 0; i < RATES.size(); i++) {
                            par.put("EmpID" + i, String.valueOf(selectedUser.id));
                            par.put("JobNumber" + i, String.valueOf(selectedUser.JobNumber));
                           // par.put("Month", month);
                            par.put("Month" + i, String.valueOf(RATES.get(i).getMonth()));
                            par.put("Year" + i, String.valueOf(c.get(Calendar.YEAR)));
                            par.put("Date" + i, RATES.get(i).getDate());
                            par.put("Type" + i, RATES.get(i).getType());
                            par.put("Rating" + i, String.valueOf(RATES.get(i).getRating()));
                            Log.d("saveRatingError", RATES.get(i).getRating() + "");
                        }
                        return par;
                    }
                };
                Volley.newRequestQueue(act).add(req);
            }
        }
    }

    void getMonth() {
        Calendar ca = Calendar.getInstance(Locale.getDefault());
        int DateMonth = (ca.get(Calendar.MONTH) + 1);
        for (int i = 1; i <= DateMonth; i++) {
            listDate.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(act, R.layout.spinner_item, listDate);
        spinnerMonth.setAdapter(adapter);
    }
}