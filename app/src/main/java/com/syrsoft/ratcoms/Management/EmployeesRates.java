package com.syrsoft.ratcoms.Management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.EmployeeRating;
import com.syrsoft.ratcoms.HRActivities.RatingCriteria;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class EmployeesRates extends AppCompatActivity {

    Activity act ;
    String getRatingCriteriaUrl = MyApp.MainUrl+"getRatingCriteria.php";
    String getEmployeesRatesByMonth = MyApp.MainUrl+"getEmployeesRatesByMonth.php";
    String insertEOMUrl = MyApp.MainUrl+"insertEOM.php";
    private String insertNewAdUrl = MyApp.MainUrl + "insertNewAd.php" ;
    String insertBonusRequest = MyApp.MainUrl + "insertNewBonusRequest.php";
    private String getThisMonthSelectedEmployees = MyApp.MainUrl+"getEmployeesOfTheMonthByMonth.php";
    List<RatingCriteria> RatingCriterias ;
    List<EmployeeRating> RATES ;
    Map<USER,List<EmployeeRating>> EmpRatesMap ;
    Map<USER,String> EmpsUnratedMap ;
    LinearLayout RatedLayout , UnRatedLayout , CriteriaLayout , EOMLayout ;
    List<USER> SelectedUsers ;
    List<EmployeeOfTheMonth> EOMs ;
    TextView eomCaption ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_rates);
        setActivity();
        getRatingCriteria();
        getSelectedEmployees();
    }

    void setActivity() {
        act = this ;
        RatingCriterias = new ArrayList<>();
        RATES = new ArrayList<>();
        EmpRatesMap = new HashMap<>();
        EmpsUnratedMap = new HashMap<>();
        SelectedUsers = new ArrayList<>();
        EOMs = new ArrayList<>();
        RatedLayout = findViewById(R.id.ratedLayout);
        UnRatedLayout = findViewById(R.id.unratedLayout);
        CriteriaLayout = findViewById(R.id.riteriaLayout);
        EOMLayout = findViewById(R.id.eomLayout);
        EOMLayout.setVisibility(View.GONE);
        eomCaption = findViewById(R.id.textView141);
    }

    void getRatingCriteria() {
        Loading l = new Loading(act);
        l.show();
        StringRequest req = new StringRequest(Request.Method.POST, getRatingCriteriaUrl, response -> {
            Log.d("ratingActivity",response);
            l.close();
            if (!response.equals("0")) {
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i=0;i<arr.length();i++) {
                        JSONObject row = arr.getJSONObject(i);
                        RatingCriteria cr = new RatingCriteria(row.getInt("id"),row.getString("Arabic"),row.getString("English"));
                        RatingCriterias.add(cr);
                    }
                    if (RatingCriterias.size()>0) {
                        getEmployeeRating();
                        for (int i=0;i<RatingCriterias.size();i++) {
                            View v = LayoutInflater.from(act).inflate(R.layout.user_rate_unit,null);
                            TextView nameArabic = v.findViewById(R.id.textView136);
                            TextView num = v.findViewById(R.id.textView135);
                            num.setText(String.valueOf(RatingCriterias.get(i).getId()));
                            nameArabic.setText(RatingCriterias.get(i).getArabic() +" "+RatingCriterias.get(i).getEnglish());
                            v.setBackgroundColor(getResources().getColor(R.color.trasparent));
                            CriteriaLayout.addView(v);
                        }
                    }
                    else {
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.noRatingCriterias),getResources().getString(R.string.noRatingCriterias));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            l.close();
            //Log.d("ratingCriteria",error.toString());
            getRatingCriteria();
        });
        Volley.newRequestQueue(act).add(req);
    }

    void getEmployeeRating () {
            StringRequest req = new StringRequest(Request.Method.POST, getEmployeesRatesByMonth, response -> {
                Log.d("ratingActivity",response);
                if (response.equals("0")) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.noRatesDoneYes),getResources().getString(R.string.noRatesDoneYes),0);
                }
                else {
                    try {
                        JSONArray res = new JSONArray(response);
                        for (int i=0;i<res.length();i++) {
                            JSONObject row = res.getJSONObject(i);
                            RATES.add(new EmployeeRating(row.getInt("id"),row.getInt("EmpID"),row.getInt("JobNumber"),row.getInt("Month"),row.getInt("Year"),row.getString("Date"),row.getString("Type"),row.getInt("Rating")));
                        }
                        if (RATES.size() > 0) {
                            drawEmployeesRates();
                        }
                        else {
                            new MESSAGE_DIALOG(act,getResources().getString(R.string.noRatesDoneYes),getResources().getString(R.string.noRatesDoneYes));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, error -> Log.d("employeeRates",error.toString()))
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Calendar ca = Calendar.getInstance(Locale.getDefault());
                    Map<String,String> par = new HashMap<String,String>();
                    par.put("Month", String.valueOf(ca.get(Calendar.MONTH)));
                    par.put("Year", String.valueOf(ca.get(Calendar.YEAR)));
                    return par;
                }
            };
            Volley.newRequestQueue(act).add(req);
    }

    void getSelectedEmployees () {
        StringRequest req = new StringRequest(Request.Method.POST, getThisMonthSelectedEmployees, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("eomResp" , response);
                if (response.equals("0")) {

                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for(int i=0 ;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            EOMs.add(new EmployeeOfTheMonth(row.getInt("id"),row.getInt("JobNumber"),row.getString("Name"),row.getString("Year"),row.getString("Month"),row.getInt("Rank"),row.getInt("Rating")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (EOMs.size() > 0) {
                        for (int i=0;i<EOMs.size();i++) {
                            View v = LayoutInflater.from(act).inflate(R.layout.user_rate_unit,null);
                            TextView name = v.findViewById(R.id.textView135);
                            TextView result = v.findViewById(R.id.textView136);
                            name.setText(EOMs.get(i).Name);
                            result.setText(String.valueOf(EOMs.get(i).Rank));
                            name.setTextColor(Color.WHITE);
                            result.setTextColor(Color.WHITE);
                            EOMLayout.addView(v);
                        }
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        EOMLayout.setVisibility(View.VISIBLE);
                        eomCaption.setText(eomCaption.getText().toString()+" "+MyApp.getStringMonth(c.get(Calendar.MONTH)));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getSelectedEmployees();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String year = String.valueOf(c.get(Calendar.YEAR));
                String Month = String.valueOf(c.get(Calendar.MONTH)+1);
                Map<String,String> par = new HashMap<>();
                par.put("Year",year);
                par.put("Month",Month);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

    void drawEmployeesRates () {
        for (int i=0 ;i<MyApp.EMPS.size();i++) {
            List<EmployeeRating> oneEmpList = EmployeeRating.searchRateByJobNumber(RATES,MyApp.EMPS.get(i).JobNumber);
            if (oneEmpList.size() > 0) {
                EmpRatesMap.put(MyApp.EMPS.get(i),oneEmpList) ;
            }
            else {
                EmpsUnratedMap.put(MyApp.EMPS.get(i),"Unrated");
            }
        }
        if (EmpRatesMap.size() > 0) {
            List<USER> keys = new ArrayList<USER>(EmpRatesMap.keySet());
            List<List<EmployeeRating>> values = new ArrayList<List<EmployeeRating>>(EmpRatesMap.values());
            for (int i=0;i<EmpRatesMap.size();i++) {
                View v = LayoutInflater.from(act).inflate(R.layout.user_rate_unit,null);
                TextView name = v.findViewById(R.id.textView135);
                TextView result = v.findViewById(R.id.textView136);
                name.setText(keys.get(i).FirstName+" "+keys.get(i).LastName +"       "+values.get(i).get(0).getStringMonth());
                int resTotal = 0 ;
                for (int j=0;j<values.get(i).size();j++) {
                    resTotal = resTotal + values.get(i).get(j).getRating() ;
                }
                result.setText(String.valueOf(resTotal/RatingCriterias.size()));
                int finalI = i;
                v.setOnClickListener(v1 -> {
                    Dialog D = new Dialog(act);
                    LinearLayout layout = new LinearLayout(act);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    TextView EmpName = new TextView(act);
                    layout.addView(EmpName);
                    EmpName.setText(keys.get(finalI).FirstName+" "+keys.get(finalI).LastName);
                    EmpName.setGravity(Gravity.CENTER);
                    EmpName.setTextColor(getResources().getColor(R.color.purple_700));
                    layout.setPadding(20,20,20,20);
                    layout.setBackgroundResource(R.drawable.dialog_bg);
                    Button Emp1 = new Button(act);
                    Emp1.setText(getResources().getString(R.string.selectAsMonthEmployee));
                    Emp1.setBackgroundResource(R.drawable.btns);
                    Emp1.setTextColor(Color.WHITE);
                    Emp1.setTextSize(12);
                    Emp1.setAllCaps(false);
                    Emp1.setPadding(10,0,10,0);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,5,0);
                    Emp1.setLayoutParams(params);
                    Emp1.setOnClickListener(v11 -> {
                        AlertDialog.Builder b = new AlertDialog.Builder(act);
                        b.setTitle(getResources().getString(R.string.areYouSure))
                                .setMessage(getResources().getString(R.string.areYouSure) +" "+keys.get(finalI).FirstName+" "+keys.get(finalI).LastName )
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        makeSelectionDialog(keys.get(finalI),values.get(finalI));
                                        dialog.dismiss();
                                        D.dismiss();
                                    }
                                });
                        b.create().show();
                    });
                    LinearLayout btnsLayout = new LinearLayout(act);
                    btnsLayout.addView(Emp1);
                    btnsLayout.setPadding(0,20,0,0);
                    for (int j=0;j<RatingCriterias.size();j++) {
                        View rate = LayoutInflater.from(act).inflate(R.layout.hr_rating_emps_unit,null);
                        TextView name1 = rate.findViewById(R.id.textView80);
                        SeekBar rating = rate.findViewById(R.id.seekBar);
                        TextView value = rate.findViewById(R.id.textView81);
                        name1.setText(values.get(finalI).get(j).getType());
                        rating.setMax(10);
                        rating.setProgress(values.get(finalI).get(j).getRating());
                        value.setText(String.valueOf(values.get(finalI).get(j).getRating()));
                        rating.setEnabled(false);
                        layout.addView(rate);
                    }
                    layout.addView(btnsLayout);
                    D.setContentView(layout);
                    Window x = D.getWindow();
                    x.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    D.show();
                });
                RatedLayout.addView(v);
            }

        }
        drawEmployeesUnRated();
    }

    void drawEmployeesUnRated () {
        if (EmpsUnratedMap.size() > 0) {
            List<USER> keys = new ArrayList<>(EmpsUnratedMap.keySet());
            List<String> values = new ArrayList<>(EmpsUnratedMap.values());
            for (int i=0;i<EmpsUnratedMap.size();i++) {
                View v = LayoutInflater.from(act).inflate(R.layout.user_rate_unit,null);
                TextView name = v.findViewById(R.id.textView135);
                TextView result = v.findViewById(R.id.textView136);
                name.setText(keys.get(i).FirstName+" "+keys.get(i).LastName );
                result.setText(getResources().getString(R.string.unRetedEmployees));
                result.setTextColor(Color.RED);
                UnRatedLayout.addView(v);
            }
        }
    }

    void makeSelectionDialog (USER u , List<EmployeeRating> list) {
        if (u != null) {
            Dialog D = new Dialog(act);
            D.setContentView(R.layout.management_select_eom_dialog);
            Window w = D.getWindow();
            w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView name = D.findViewById(R.id.textView138) ;
            name.setText(u.FirstName+" "+u.LastName);
            EditText amount = D.findViewById(R.id.editTextNumber) ;
            amount.setVisibility(View.INVISIBLE);
            EditText announcementText = D.findViewById(R.id.editTextTextMultiLine3) ;
            announcementText.setVisibility(View.INVISIBLE);
            CheckBox bonus = D.findViewById(R.id.checkBox5) , announce = D.findViewById(R.id.checkBox7) ;
            bonus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        amount.setVisibility(View.VISIBLE);
                    }
                    else {
                        amount.setVisibility(View.INVISIBLE);
                    }
                }
            });
            announce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        announcementText.setVisibility(View.VISIBLE);
                    }
                    else {
                        announcementText.setVisibility(View.INVISIBLE);
                    }
                }
            });
            Button cancel = D.findViewById(R.id.button61) , yes = D.findViewById(R.id.button62);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    D.dismiss();
                }
            });
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Loading l = new Loading(act); l.show();
                    StringRequest req = new StringRequest(Request.Method.POST, insertEOMUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            l.close();
                            int res = Integer.valueOf(response);
                            if (res > 0) {
                                getSelectedEmployees();
                                if (bonus.isChecked()) {
                                    insertNewBonusRequest(Double.valueOf(amount.getText().toString()),MyApp.MyUser,u,"Bonus for employee of the month");
                                }
                                if (announce.isChecked()) {
                                    sendAd(getResources().getString(R.string.employeeOfTheMonth),getResources().getString(R.string.employeeOfTheMonth) +" "+u.FirstName+" "+u.LastName + " Rank "+SelectedUsers.size()+1);
                                }
                                D.dismiss();
                            }
                            else {
                                new MESSAGE_DIALOG(act,"Error","Error Saving Order ");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            l.close();
                            new MESSAGE_DIALOG(act,"Error","Error Saving Order "+error.toString());
                        }
                    })
                    {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Calendar c = Calendar.getInstance(Locale.getDefault());
                            String year = String.valueOf(c.get(Calendar.YEAR));
                            String month = String.valueOf(c.get(Calendar.MONTH)+1);
                            int resTotal = 0 ;
                            for (int j=0;j<list.size();j++) {
                                resTotal = resTotal + list.get(j).getRating() ;
                            }
                            Map<String,String> par = new HashMap<>();
                            par.put("jn",String.valueOf(MyApp.MyUser.JobNumber));
                            par.put("name",u.FirstName+" "+u.LastName);
                            par.put("year",year);
                            par.put("month",month);
                            par.put("rank", String.valueOf(SelectedUsers.size()+1));
                            par.put("rating",String.valueOf(resTotal/RatingCriterias.size()));
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(req);
                }
            });
            D.show();
        }
    }

    void sendAd(String title,String message) {
        Loading l = new Loading(act); l.show();
        StringRequest request = new StringRequest(Request.Method.POST, insertNewAdUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("sendAdResponse",response);
                if (response.equals("1")) {
                    Random r = new Random();
                    int x = r.nextInt(10000);
                    MyApp.sendNotificationsToGroup(MyApp.EMPS, title, message, "", x, "AD", MyApp.app, new VolleyCallback() {
                        @Override
                        public void onSuccess() {
                        }
                    });
                }
                else {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error","Sending Message Failed ");
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("sendAdResponse",error.toString());
                new MESSAGE_DIALOG(act,"Error","Sending Message Failed "+error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<>();
                par.put("Title",title);
                par.put("Message",message);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void insertNewBonusRequest(double amount , USER requester ,USER to , String notes) {
        Loading l = new Loading(act); l.show();
        StringRequest req = new StringRequest(Request.Method.POST, insertBonusRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                int res = Integer.valueOf(response);
                if (res > 0) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                }
                else {
                    new MESSAGE_DIALOG(act,"Error","Error Saving Order");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                new MESSAGE_DIALOG(act,"Error","Error Saving Order "+error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Map<String,String> par = new HashMap<>();
                par.put("JobNumber",String.valueOf(to.JobNumber));
                par.put("Name",to.FirstName+" "+to.LastName);
                par.put("RequesterJobNumber", String.valueOf(requester.JobNumber));
                par.put("RequesterName",requester.FirstName+" "+requester.LastName);
                par.put("BonusAmount", String.valueOf(amount));
                par.put("RequestDate",date);
                par.put("Notes" ,notes);
                par.put("From","EmployeeOfTheMonth");
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

}