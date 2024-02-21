package com.syrsoft.ratcoms;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.SpinKitView;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    public static boolean isConnected = false ;
    static Activity act  ;
    private EditText UserName , Password ;
    private String USERNAME , PASSWORD ;
    public static USER THE_USER ;
    SharedPreferences prefs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        prefs = getSharedPreferences("com.syrsoft.ratcoms", MODE_PRIVATE);
        setActivity();
        setActivityActions();
        isConnected = isNetworkAvailable();
        redirectActivity();
    }

    void setActivity() {
        act = this ;
        MyApp.ActList.add(act);
        setViews();
    }

    void setActivityActions() {
        UserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                USERNAME = UserName.getText().toString() ;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PASSWORD = Password.getText().toString() ;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void setViews() {
        UserName = findViewById(R.id.Login_username);
        Password = findViewById(R.id.Login_password);
        TextView Version = findViewById(R.id.version);
        String versionName = BuildConfig.VERSION_NAME;
        Version.setText(MessageFormat.format("Version {0}", versionName));
    }

    public void login(View view) {
        if (USERNAME == null || USERNAME.isEmpty()){
            ToastMaker.Show(1,"Enter UserName" , act);
            return;
        }
        if (PASSWORD == null || PASSWORD.isEmpty()){
            ToastMaker.Show(1,"Enter Password" , act);
            return;
        }
        Loading l = new Loading(act);
        l.show();
        TextView ProgressText = findViewById(R.id.textView94) ;
        LinearLayout progressLayout = findViewById(R.id.progressLayout);
        ProgressBar P = findViewById(R.id.progressBar6);
        progressLayout.setVisibility(View.VISIBLE);
        ProgressText.setText("0");
        P.setMax(4);
        LinearLayout loginLayout = (LinearLayout) act.findViewById(R.id.loginLayout);
        loginLayout.setVisibility(View.GONE);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, ProjectUrls.LoginUrl, response -> {
            Log.d("LoginResponse" , response);
            if (response.equals("0")) {
                l.close();
                ToastMaker.Show(1,"Wrong Username Or Password ",act);
                loginLayout.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.GONE);
            }
            else {
                int v = (100)/P.getMax();
                ProgressText.setText(MessageFormat.format("{0} %", v));
                P.setProgress(1);
                l.close();
                List<Object> list = JsonToObject.translate(response,USER.class,act);
                Log.d("LoginResponse" , list.size()+"");
                try {
                    THE_USER = (USER) list.get(0);
                    Log.d("hrProblem",THE_USER.Department);
                    prefs.edit().putInt("JobNumber",THE_USER.JobNumber).apply();
                    THE_USER.getPermissions(new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            int v0 = (2*100)/P.getMax();
                            ProgressText.setText(MessageFormat.format("{0} %", v0));
                            P.setProgress(2);
                            THE_USER.getMyPermissions(THE_USER.id, THE_USER.JobNumber, new VollyCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    int v1 = (3*100)/P.getMax();
                                    ProgressText.setText(MessageFormat.format("{0} %", v1));
                                    P.setProgress(3);
                                    MyApp.MyUser = THE_USER ;
                                    setMyFireBaseReference();
                                    MyApp.db.insertUser(THE_USER.id,THE_USER.JobNumber,THE_USER.User,THE_USER.FirstName,THE_USER.LastName,THE_USER.Department,THE_USER.JobTitle,THE_USER.DirectManager,THE_USER.DepartmentManager,THE_USER.WorkLocationLa,THE_USER.WorkLocationLo,THE_USER.Mobile,THE_USER.Email,THE_USER.Pic,THE_USER.IDNumber,THE_USER.IDExpireDate,THE_USER.BirthDate,THE_USER.Nationality,THE_USER.PassportNumber,THE_USER.PassportExpireDate,THE_USER.ContractNumber,THE_USER.ContractStartDate,THE_USER.ContractDuration,THE_USER.ContractExpireDate,THE_USER.InsuranceExpireDate,THE_USER.Bank,THE_USER.BankAccount,THE_USER.BankIban,THE_USER.IDsWarningNotifications,THE_USER.PASSPORTsWarningNotification,THE_USER.CONTRACTsWarningNotification,THE_USER.Salary,THE_USER.VacationDays,THE_USER.SickDays,THE_USER.EmergencyDays,THE_USER.VacationStatus,THE_USER.VacationAlternative,THE_USER.JoinDate);
                                    ToastMaker.Show(1,"Welcome "+THE_USER.FirstName+" "+THE_USER.LastName ,act);
                                    THE_USER.getEmployeesData(act, new VollyCallback() {
                                        @Override
                                        public void onSuccess(String res) {
                                            int v2 = (4*100)/P.getMax();
                                            ProgressText.setText(MessageFormat.format("{0} %", v2));
                                            P.setProgress(4);
                                            Intent i = new Intent(act,MainPage.class);
                                            startActivity(i);
                                        }

                                        @Override
                                        public void onFailed(String error) {

                                            AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                            alert.setTitle(getResources().getString(R.string.problemInGettingDataُTitle));
                                            alert.setMessage(getResources().getString(R.string.problemInGettingData));
                                            alert.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> act.finish());
                                            alert.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> redirectActivity());
                                            alert.create().show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String error) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                    alert.setTitle(getResources().getString(R.string.problemInGettingDataُTitle));
                                    alert.setMessage(getResources().getString(R.string.problemInGettingData));
                                    alert.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> act.finish());
                                    alert.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> redirectActivity());
                                    alert.create().show();
                                }
                            },null);
                        }

                        @Override
                        public void onFailed(String error) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(act);
                            alert.setTitle(getResources().getString(R.string.problemInGettingDataُTitle));
                            alert.setMessage(getResources().getString(R.string.problemInGettingData));
                            alert.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> act.finish());
                            alert.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> redirectActivity());
                            alert.create().show();
                        }
                    },null);
                }
                catch (Exception e){
                    new MESSAGE_DIALOG(act,"Error",e.getMessage());
                }

            }
        }, error -> {
            l.close();
            AlertDialog.Builder alert = new AlertDialog.Builder(act);
            alert.setTitle(getResources().getString(R.string.problemInGettingDataُTitle));
            alert.setMessage(getResources().getString(R.string.problemInGettingData));
            alert.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> act.finish());
            alert.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> redirectActivity());
            alert.create().show();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String ,String> pars = new HashMap<>();
                pars.put("user" , USERNAME);
                pars.put("password" , PASSWORD);
                return pars;
            }
        };
        Volley.newRequestQueue(act).add(loginRequest);
    }

    static void redirectActivity() {
        TextView ProgressText = act.findViewById(R.id.textView94) ;
        ProgressBar P = act.findViewById(R.id.progressBar6);
        Thread t = new Thread(() -> {
                if (isConnected) {
                    if (MyApp.db != null) {
                        if (MyApp.db.isLoggedIn()) {
                            SpinKitView spin = (SpinKitView) act.findViewById(R.id.spin_kit0);
                            act.runOnUiThread(() -> {
                                spin.setVisibility(View.VISIBLE);
                                LinearLayout progressLayout = act.findViewById(R.id.progressLayout);
                                progressLayout.setVisibility(View.VISIBLE);
                                ProgressText.setText("0");
                                P.setMax(4);
                            });
                            refreshMyData(new VollyCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    setCountersToZero();
                                    ProgressText.setText(MessageFormat.format("{0}%", (100) / P.getMax()));
                                    P.setProgress(1);
                                    MyApp.db.logOut();
                                    MyApp.db.insertUser(THE_USER.id,THE_USER.JobNumber,THE_USER.User,THE_USER.FirstName,THE_USER.LastName,THE_USER.Department,THE_USER.JobTitle,THE_USER.DirectManager,THE_USER.DepartmentManager,THE_USER.WorkLocationLa,THE_USER.WorkLocationLo,THE_USER.Mobile,THE_USER.Email,THE_USER.Pic,THE_USER.IDNumber,THE_USER.IDExpireDate,THE_USER.BirthDate,THE_USER.Nationality,THE_USER.PassportNumber,THE_USER.PassportExpireDate,THE_USER.ContractNumber,THE_USER.ContractStartDate,THE_USER.ContractDuration,THE_USER.ContractExpireDate,THE_USER.InsuranceExpireDate,THE_USER.Bank,THE_USER.BankAccount,THE_USER.BankIban,THE_USER.IDsWarningNotifications,THE_USER.PASSPORTsWarningNotification,THE_USER.CONTRACTsWarningNotification,THE_USER.Salary,THE_USER.VacationDays,THE_USER.SickDays,THE_USER.EmergencyDays,THE_USER.VacationStatus,THE_USER.VacationAlternative,THE_USER.JoinDate);
                                    THE_USER.getPermissions(new VollyCallback() {
                                        @Override
                                        public void onSuccess(String s) {
                                            int v = (2*100)/P.getMax();
                                            ProgressText.setText(MessageFormat.format("{0} %", v));
                                            P.setProgress(2);
                                            THE_USER.getMyPermissions(THE_USER.id, THE_USER.JobNumber, new VollyCallback() {
                                                @Override
                                                public void onSuccess(String s) {
                                                    int v0 = (3*100)/P.getMax();
                                                    ProgressText.setText(MessageFormat.format("{0} %", v0));
                                                    P.setProgress(3);
                                                    MyApp.MyUser = THE_USER;
                                                    setMyFireBaseReference();
                                                    THE_USER.getEmployeesData(act, new VollyCallback() {
                                                        @Override
                                                        public void onSuccess(String res) {
                                                            int v1 = (4*100)/P.getMax();
                                                            ProgressText.setText(MessageFormat.format("{0} %", v1));
                                                            P.setProgress(4);
                                                            Intent i = new Intent(act, MainPage.class);
                                                            act.runOnUiThread(() -> ToastMaker.Show(1, "Welcome " + MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, act));
                                                            act.startActivity(i);
                                                        }

                                                        @Override
                                                        public void onFailed(String error) {
                                                            AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                                            alert.setTitle(act.getResources().getString(R.string.problemInGettingDataُTitle));
                                                            alert.setMessage(act.getResources().getString(R.string.problemInGettingData));
                                                            alert.setNegativeButton(act.getResources().getString(R.string.no), (dialog, which) -> act.finish());
                                                            alert.setPositiveButton(act.getResources().getString(R.string.yes), (dialog, which) -> redirectActivity());
                                                            alert.create().show();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailed(String error) {
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                                    alert.setTitle(act.getResources().getString(R.string.problemInGettingDataُTitle));
                                                    alert.setMessage(act.getResources().getString(R.string.problemInGettingData));
                                                    alert.setNegativeButton(act.getResources().getString(R.string.no), (dialog, which) -> act.finish());
                                                    alert.setPositiveButton(act.getResources().getString(R.string.yes), (dialog, which) -> redirectActivity());
                                                    alert.create().show();
                                                }
                                            }, null);
                                        }

                                        @Override
                                        public void onFailed(String error) {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                            alert.setTitle(act.getResources().getString(R.string.problemInGettingDataُTitle));
                                            alert.setMessage(act.getResources().getString(R.string.problemInGettingData));
                                            alert.setNegativeButton(act.getResources().getString(R.string.no), (dialog, which) -> act.finish());
                                            alert.setPositiveButton(act.getResources().getString(R.string.yes), (dialog, which) -> redirectActivity());
                                            alert.create().show();
                                        }
                                    }, null);
                                }

                                @Override
                                public void onFailed(String error) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                    alert.setTitle(act.getResources().getString(R.string.problemInGettingDataُTitle));
                                    alert.setMessage(act.getResources().getString(R.string.problemInGettingData));
                                    alert.setNegativeButton(act.getResources().getString(R.string.no), (dialog, which) -> act.finish());
                                    alert.setPositiveButton(act.getResources().getString(R.string.yes), (dialog, which) -> redirectActivity());
                                    alert.create().show();
                                }
                            });
                        }
                        else {
                            act.runOnUiThread(() -> {
                                MyApp.db.logOut();
                                prepareLoginForm();
                                LinearLayout progressLayout = act.findViewById(R.id.progressLayout);
                                progressLayout.setVisibility(View.GONE);
                            });
                        }
                    }
                    else {
                        act.runOnUiThread(Login::prepareLoginForm);
                    }
                }
                else {
                    act.runOnUiThread(() -> new MESSAGE_DIALOG(act,"No Internet","No internet connection \nPlease check your internet connection and try again",0));
                }
        });
        t.start();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    static void setMyFireBaseReference() {
        if (MyApp.RefUSERS != null) {
            if (MyApp.MyUser != null) {
                MyApp.RefME = MyApp.RefUSERS.child(String.valueOf(MyApp.MyUser.JobNumber));
            }
        }
    }

    public static void refreshMyData(VollyCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, ProjectUrls.updateMyDataUrl, response -> {
            List<Object> list = JsonToObject.translate(response,USER.class,act);
            Log.d("RefreshMe" , list.size()+" "+response);
            try {
                THE_USER = (USER) list.get(0);
                Log.d("hrProblem",THE_USER.Department);
                callback.onSuccess("Done");
            }
            catch (Exception e){
                callback.onFailed("error "+e.getMessage());
            }
        }, error -> callback.onFailed("error "+error)){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> par = new HashMap<>();
                par.put("jn" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    static void setCountersToZero() {
        MyApp.ProjectsCounters[0] = 0 ;
        MyApp.ProjectsCounters[1] = 0 ;
        MyApp.HRCounter = 0 ;
    }

    static void prepareLoginForm() {
        SpinKitView spin = act.findViewById(R.id.spin_kit0);
        LinearLayout l = act.findViewById(R.id.loginLayout);
        spin.setVisibility(View.GONE);
        l.setVisibility(View.VISIBLE);
    }
}