package com.syrsoft.ratcoms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.SpinKitView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Login extends AppCompatActivity {

    public static boolean isConnected = false ;
    ConnectivityManager connectivityManager ;
    static Activity act  ;
    private EditText UserName , Password ;
    private Button LoginBtn ;
    private String USERNAME , PASSWORD ;
    private String LoginUrl = MyApp.MainUrl + "appLoginEmployees.php" ;
    static String updateMyDataUrl = MyApp.MainUrl+"getDirectManager.php";
    public static USER THE_USER ;
    private ImageView logo ;
    private Animation loginAnimation;
    private TextView Version ;
    String versionName ;
    static ProgressBar P ;
    static TextView ProgressText ;
    static LinearLayout progressLayout ;
    SharedPreferences prefs ;
    static boolean FirstRun = false ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        prefs = getSharedPreferences("com.syrsoft.ratcoms", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).commit();
            Log.d("firstRun" , "it is first run");
            MyApp.db.logOut();
            FirstRun = true ;
            deleteCache(MyApp.app);
        }
        else {
            Log.d("firstRun" , "it is not the first run");
            FirstRun = false ;
        }
        setActivity();
        isConnected = isNetworkAvailable();
        if (isConnected) {
            redirectActivity();
        }
        else {
            ToastMaker.Show(1,"No Internet Connection" , MyApp.app);
            act.finish();
        }
        checkConnectivity(new getConnectionResult() {
            @Override
            public void onGetResult(boolean res) {
                if (!res) {
                    //ToastMaker.Show(1,"No Internet Connection" , MyApp.app);
                }
                else {
                    //ToastMaker.Show(1,"Internet Connection Back" , MyApp.app);
                }
            }
        });
    }

    void setActivity() {
        act = this ;
        MyApp.ActList.add(act);
        //MyApp.db.logOut();
        loginAnimation = AnimationUtils.loadAnimation(act,R.anim.logon_animation);
        logo = (ImageView) findViewById(R.id.imageView2);
        P = (ProgressBar) findViewById(R.id.progressBar6);
        progressLayout = findViewById(R.id.progressLayout);
        ProgressText = (TextView) findViewById(R.id.textView94) ;
        loginAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SpinKitView spin = (SpinKitView)findViewById(R.id.spin_kit0);
                LinearLayout l = (LinearLayout) findViewById(R.id.loginLayout);
                spin.setVisibility(View.GONE);
                l.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        UserName = (EditText)findViewById(R.id.Login_username);
        Password = (EditText)findViewById(R.id.Login_password);
        LoginBtn = (Button) findViewById(R.id.Login_loginbtn);
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
        versionName = BuildConfig.VERSION_NAME;
        Version = (TextView) findViewById(R.id.version);
        if (versionName != null) {
            Version.setText("Version "+versionName);
        }
    }

    public void login(View view) {

        if (USERNAME == null || USERNAME.isEmpty() || USERNAME.length() == 0){
            ToastMaker.Show(1,"Enter UserName" , act);
            return;
        }
        if (PASSWORD == null || PASSWORD.isEmpty() || PASSWORD.length() == 0){
            ToastMaker.Show(1,"Enter Password" , act);
            return;
        }
        Loading l = new Loading(act);
        l.show();
        progressLayout.setVisibility(View.VISIBLE);
        ProgressText.setText("0");
        P.setMax(4);
        LinearLayout loginLayout = (LinearLayout) act.findViewById(R.id.loginLayout);
        loginLayout.setVisibility(View.GONE);
        StringRequest loginrequest = new StringRequest(Request.Method.POST, LoginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LoginResponse" , response);
                if (response.equals("0")) {
                    l.close();
                    ToastMaker.Show(1,"Wrong Username Or Password ",act);
                    loginLayout.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.GONE);
                }
                else {
                    ProgressText.setText(((1*100)/P.getMax())+"%");
                    P.setProgress(1);
                    l.close();
                    List<Object> list = JsonToObject.translate(response,USER.class,act);
                    Log.d("LoginResponse" , list.size()+"");
                    try {
                        THE_USER = (USER) list.get(0);
                        Log.d("hrProblem",THE_USER.Department);
                        prefs.edit().putInt("JobNumber",THE_USER.JobNumber).commit();
                        THE_USER.getPermissions(new VollyCallback() {
                            @Override
                            public void onSuccess(String s) {
                                ProgressText.setText(((2*100)/P.getMax())+"%");
                                P.setProgress(2);
                                THE_USER.getMyPermissions(THE_USER.id, THE_USER.JobNumber, new VollyCallback() {
                                    @Override
                                    public void onSuccess(String s) {
                                        ProgressText.setText(((3*100)/P.getMax())+"%");
                                        P.setProgress(3);
                                        MyApp.MyUser = THE_USER ;
                                        setMyFireBaseRiference();
                                        MyApp.db.insertUser(THE_USER.id,THE_USER.JobNumber,THE_USER.User,THE_USER.FirstName,THE_USER.LastName,THE_USER.Department,THE_USER.JobTitle,THE_USER.DirectManager,THE_USER.DepartmentManager,THE_USER.WorkLocationLa,THE_USER.WorkLocationLo,THE_USER.Mobile,THE_USER.Email,THE_USER.Pic,THE_USER.IDNumber,THE_USER.IDExpireDate,THE_USER.BirthDate,THE_USER.Nationality,THE_USER.PassportNumber,THE_USER.PassportExpireDate,THE_USER.ContractNumber,THE_USER.ContractStartDate,THE_USER.ContractDuration,THE_USER.ContractExpireDate,THE_USER.InsuranceExpireDate,THE_USER.Bank,THE_USER.BankAccount,THE_USER.BankIban,THE_USER.IDsWarningNotifications,THE_USER.PASSPORTsWarningNotification,THE_USER.CONTRACTsWarningNotification,THE_USER.Salary,THE_USER.VacationDays,THE_USER.SickDays,THE_USER.EmergencyDays,THE_USER.VacationStatus,THE_USER.VacationAlternative,THE_USER.JoinDate);
                                        ToastMaker.Show(1,"Welcome "+THE_USER.FirstName+" "+THE_USER.LastName ,act);
                                        THE_USER.getEmployeesData(act, new VollyCallback() {
                                            @Override
                                            public void onSuccess(String res) {
                                                ProgressText.setText(((4*100)/P.getMax())+"%");
                                                P.setProgress(4);
                                                Intent i = new Intent(act,MainPage.class);
                                                startActivity(i);
                                            }

                                            @Override
                                            public void onFailed(String error) {

                                                AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                                alert.setTitle(getResources().getString(R.string.problemInGettingDataُTitle));
                                                alert.setMessage(getResources().getString(R.string.problemInGettingData));
                                                alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        act.finish();
                                                    }
                                                });
                                                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        redirectActivity();
                                                    }
                                                });
                                                alert.create().show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                        alert.setTitle(getResources().getString(R.string.problemInGettingDataُTitle));
                                        alert.setMessage(getResources().getString(R.string.problemInGettingData));
                                        alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                act.finish();
                                            }
                                        });
                                        alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                redirectActivity();
                                            }
                                        });
                                        alert.create().show();
                                    }
                                },null);
                            }

                            @Override
                            public void onFailed(String error) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                alert.setTitle(getResources().getString(R.string.problemInGettingDataُTitle));
                                alert.setMessage(getResources().getString(R.string.problemInGettingData));
                                alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        act.finish();
                                    }
                                });
                                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        redirectActivity();
                                    }
                                });
                                alert.create().show();
                            }
                        },null);
                    }
                    catch (Exception e){

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                AlertDialog.Builder alert = new AlertDialog.Builder(act);
                alert.setTitle(getResources().getString(R.string.problemInGettingDataُTitle));
                alert.setMessage(getResources().getString(R.string.problemInGettingData));
                alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        act.finish();
                    }
                });
                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        redirectActivity();
                    }
                });
                alert.create().show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> pars = new HashMap<String, String>();
                pars.put("user" , USERNAME);
                pars.put("password" , PASSWORD);
                return pars;
            }
        };
        Volley.newRequestQueue(act).add(loginrequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void checkConnectivity (getConnectionResult callback) {
        Log.d("checkConnect", "started") ;
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback()
        {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                isConnected = true ;
                callback.onGetResult(isConnected);
                Log.d("checkConnect", "available") ;
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                isConnected = false ;
                callback.onGetResult(isConnected);
                Log.d("checkConnect", "unavailable") ;
            }

            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                isConnected = false ;
                callback.onGetResult(isConnected);
                Log.d("checkConnect", "losing") ;
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                isConnected = false ;
                callback.onGetResult(isConnected);
                Log.d("checkConnect", "lost") ;
            }
        } );
    }

    static void redirectActivity() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                    if (isConnected) {
                        if (MyApp.db != null) {
                            if (MyApp.db.isLoggedIn()) {
                                SpinKitView spin = (SpinKitView) act.findViewById(R.id.spin_kit0);
                                act.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spin.setVisibility(View.VISIBLE);
                                        progressLayout.setVisibility(View.VISIBLE);
                                        ProgressText.setText("0");
                                        P.setMax(4);
                                    }
                                });
                                refreshMyData(new VollyCallback() {
                                    @Override
                                    public void onSuccess(String s) {
                                        setCountersToZero();
                                        ProgressText.setText(((1*100)/P.getMax())+"%");
                                        P.setProgress(1);
                                        MyApp.db.logOut();
                                        MyApp.db.insertUser(THE_USER.id,THE_USER.JobNumber,THE_USER.User,THE_USER.FirstName,THE_USER.LastName,THE_USER.Department,THE_USER.JobTitle,THE_USER.DirectManager,THE_USER.DepartmentManager,THE_USER.WorkLocationLa,THE_USER.WorkLocationLo,THE_USER.Mobile,THE_USER.Email,THE_USER.Pic,THE_USER.IDNumber,THE_USER.IDExpireDate,THE_USER.BirthDate,THE_USER.Nationality,THE_USER.PassportNumber,THE_USER.PassportExpireDate,THE_USER.ContractNumber,THE_USER.ContractStartDate,THE_USER.ContractDuration,THE_USER.ContractExpireDate,THE_USER.InsuranceExpireDate,THE_USER.Bank,THE_USER.BankAccount,THE_USER.BankIban,THE_USER.IDsWarningNotifications,THE_USER.PASSPORTsWarningNotification,THE_USER.CONTRACTsWarningNotification,THE_USER.Salary,THE_USER.VacationDays,THE_USER.SickDays,THE_USER.EmergencyDays,THE_USER.VacationStatus,THE_USER.VacationAlternative,THE_USER.JoinDate);
                                        //THE_USER = MyApp.db.getUser();
                                        THE_USER.getPermissions(new VollyCallback() {
                                            @Override
                                            public void onSuccess(String s) {
                                                ProgressText.setText(((2*100)/P.getMax())+"%");
                                                P.setProgress(2);
                                                THE_USER.getMyPermissions(THE_USER.id, THE_USER.JobNumber, new VollyCallback() {
                                                    @Override
                                                    public void onSuccess(String s) {
                                                        ProgressText.setText(((3*100)/P.getMax())+"%");
                                                        P.setProgress(3);
                                                        MyApp.MyUser = THE_USER;
                                                        setMyFireBaseRiference();
                                                        THE_USER.getEmployeesData(act, new VollyCallback() {
                                                            @Override
                                                            public void onSuccess(String res) {
                                                                ProgressText.setText(((4*100)/P.getMax())+"%");
                                                                P.setProgress(4);
                                                                Intent i = new Intent(act, MainPage.class);
                                                                act.runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        ToastMaker.Show(1, "Welcome " + MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, act);
                                                                    }
                                                                });
                                                                act.startActivity(i);
                                                            }

                                                            @Override
                                                            public void onFailed(String error) {
                                                                AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                                                alert.setTitle(act.getResources().getString(R.string.problemInGettingDataُTitle));
                                                                alert.setMessage(act.getResources().getString(R.string.problemInGettingData));
                                                                alert.setNegativeButton(act.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        act.finish();
                                                                    }
                                                                });
                                                                alert.setPositiveButton(act.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        redirectActivity();
                                                                    }
                                                                });
                                                                alert.create().show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onFailed(String error) {
                                                        AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                                        alert.setTitle(act.getResources().getString(R.string.problemInGettingDataُTitle));
                                                        alert.setMessage(act.getResources().getString(R.string.problemInGettingData));
                                                        alert.setNegativeButton(act.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                act.finish();
                                                            }
                                                        });
                                                        alert.setPositiveButton(act.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                redirectActivity();
                                                            }
                                                        });
                                                        alert.create().show();
                                                    }
                                                }, null);
                                            }

                                            @Override
                                            public void onFailed(String error) {
                                                AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                                alert.setTitle(act.getResources().getString(R.string.problemInGettingDataُTitle));
                                                alert.setMessage(act.getResources().getString(R.string.problemInGettingData));
                                                alert.setNegativeButton(act.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        act.finish();
                                                    }
                                                });
                                                alert.setPositiveButton(act.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        redirectActivity();
                                                    }
                                                });
                                                alert.create().show();
                                            }
                                        }, null);
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                        alert.setTitle(act.getResources().getString(R.string.problemInGettingDataُTitle));
                                        alert.setMessage(act.getResources().getString(R.string.problemInGettingData));
                                        alert.setNegativeButton(act.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                act.finish();
                                            }
                                        });
                                        alert.setPositiveButton(act.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                redirectActivity();
                                            }
                                        });
                                        alert.create().show();
                                    }
                                });

                            } else {
                                act.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("loginProp", "not logged in");
                                        MyApp.db.logOut();
                                        SpinKitView spin = (SpinKitView) act.findViewById(R.id.spin_kit0);
                                        LinearLayout l = (LinearLayout) act.findViewById(R.id.loginLayout);
                                        spin.setVisibility(View.GONE);
                                        l.setVisibility(View.VISIBLE);
                                        progressLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                        else {
                            act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("loginProp", "not logged in");
                                    SpinKitView spin = (SpinKitView) act.findViewById(R.id.spin_kit0);
                                    LinearLayout l = (LinearLayout) act.findViewById(R.id.loginLayout);
                                    spin.setVisibility(View.GONE);
                                    l.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                    else {
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastMaker.Show(1,"No internet connection",act);
                                act.finish();
                            }
                        });

                    }
            }
        });
        t.start();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    static void setMyFireBaseRiference () {
        if (MyApp.RefUSERS != null) {
            if (MyApp.MyUser != null) {
                MyApp.RefME = MyApp.RefUSERS.child(String.valueOf(MyApp.MyUser.JobNumber));
                if (FirstRun) {
                    MyApp.RefME.child("AtWork").setValue("0");
                    MyApp.RefME.child("AtWorkDate").setValue("");
                }
            }
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            Log.d("deleteCache" ,"started");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("deleteCache" ,e.getMessage());
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void refreshMyData(VollyCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, updateMyDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Object> list = JsonToObject.translate(response,USER.class,act);
                Log.d("RefreshMe" , list.size()+" "+response);
                try {
                    THE_USER = (USER) list.get(0);
                    Log.d("hrProblem",THE_USER.Department);
                    callback.onSuccess("Done");
                    //MyApp.db.updateUser(THE_USER.id,THE_USER.JobNumber,THE_USER.User,THE_USER.FirstName,THE_USER.LastName,THE_USER.Department,THE_USER.JobTitle,THE_USER.DirectManager,THE_USER.WorkLocationLa,THE_USER.WorkLocationLo,THE_USER.Mobile,THE_USER.Email,THE_USER.Pic,THE_USER.IDNumber,THE_USER.IDExpireDate,THE_USER.BirthDate,THE_USER.Nationality,THE_USER.PassportNumber,THE_USER.PassportExpireDate,THE_USER.ContractNumber,THE_USER.ContractStartDate,THE_USER.ContractDuration,THE_USER.ContractExpireDate,THE_USER.InsuranceExpireDate,THE_USER.Bank,THE_USER.BankAccount,THE_USER.BankIban,THE_USER.IDsWarningNotifications,THE_USER.PASSPORTsWarningNotification,THE_USER.CONTRACTsWarningNotification,THE_USER.Salary,THE_USER.VacationDays,THE_USER.SickDays,THE_USER.EmergencyDays,THE_USER.JoinDate);
                }
                catch (Exception e){
                    // ToastMaker.Show(1,e.getMessage(),act);
                    callback.onFailed("error "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed("error "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
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

}

    interface getConnectionResult {
        void onGetResult(boolean res);
    }