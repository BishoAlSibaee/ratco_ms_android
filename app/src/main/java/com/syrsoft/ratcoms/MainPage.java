package com.syrsoft.ratcoms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.syrsoft.ratcoms.Developer.Developer;
import com.syrsoft.ratcoms.HRActivities.ManagePassports;
import com.syrsoft.ratcoms.HRActivities.VACATION_CLASS;
import com.syrsoft.ratcoms.SALESActivities.CLIENT_CLASS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainPage extends AppCompatActivity {

    private static Activity act;
    private FrameLayout HR, SALES, SERVICE, STORE , Management ,DEVELOPER;
    private Location CurrentLocation ;
    private LocationManager locationManager  ;
    private LocationListener listener ;
    private String registerWorkTimeUrl = MyApp.MainUrl+"attendRecordInsert.php";
    private String setTokenUrl = MyApp.MainUrl+"setUserToken.php";
    String getClientUrl = MyApp.MainUrl+"getClient.php";
    private String getTasksUrl = MyApp.MainUrl+"getDailyJobs.php";
    String getLastAdsUrl = MyApp.MainUrl+"getLast5Ads.php";
    public static List<USER> EMPS ;
    private NotificationManager notificationManager ;
    private Random r ;
    public static Ads_Adapter adsadapter ;
    private List<ADS_CLASS> Adslist;
    private RecyclerView.LayoutManager adManager , warningManager , tasksManager;
    private RecyclerView adsRecycler , warningRecycler , tasksRecycler;
    public static List<WARNING_CLASS> WarningList ;
    public static Warning_Adapter warningADApter ;
    static LinearLayout DotsLayout,AdsCaptionLayout , warningsCaptionLayout , AdsLayout ,WarningsLayout ;
    ImageView warIcon ;
    boolean attendanceTriger = false ;
    Loading AttendanceLoadingDialog ;
    int warningCounter = 0 ;
    static TextView warningCounterTV , adsCounterTV , ProjectsCounterText , HRCounterText , SalesCounterText ;
    private Button ErrorsBtn , StartWortBtn ;
    static CardView AdsRedDot , SalesCounterCard , HRCounterCard , ProjectsCounterCard ;
    public static boolean isRunning = false ;
    List<TASK> TASKs ;
    RequestQueue Q ;
    TasksAdapter Task_Adapter ;
    public static int CurrentAd = 0 ;
    private String getMyVacationsUrl = MyApp.MainUrl + "getMyVacations.php" ;
    private String setMyVacationStatusUrl = MyApp.MainUrl + "setIamInVacation.php" ;
    String checkRatingAvaliability = MyApp.MainUrl+"checkAvailableRatings.php";
    SharedPreferences prefs ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        setActivity();
        setActivityActions();
        Log.d("myUser",MyApp.MyUser.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLast5Ads();
        MyApp.MyUser.getNumberOfSiteVisitOrders(new VollyCallback() {
            @Override
            public void onSuccess(String s) {
                setCounters();
            }

            @Override
            public void onFailed(String error) {

            }
        });
        MyApp.MyUser.getMaintenanceOrders(new VollyCallback() {
            @Override
            public void onSuccess(String s) {
                setCounters();
            }

            @Override
            public void onFailed(String error) {

            }

        });
        getIfThereIsNewRating();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false ;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    void setActivity() {
        act = this;
        isRunning = true ;
        prefs = getSharedPreferences("com.syrsoft.ratcoms", MODE_PRIVATE);
        StartWortBtn = findViewById(R.id.button52);
        StartWortBtn.setVisibility(View.INVISIBLE);
        ErrorsBtn = findViewById(R.id.errorsBtn);
        AttendanceLoadingDialog = new Loading(act) ;
        warningCounterTV = (TextView) findViewById(R.id.warningCounter);
        adsCounterTV = findViewById(R.id.adsCounter);
        AdsRedDot = findViewById(R.id.AdsCardView);
        EMPS = new ArrayList<USER>();
        TASKs = new ArrayList<TASK>();
        r = new Random();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        HR = findViewById(R.id.hr_Btn);
        HR.setVisibility(View.GONE);
        SALES = findViewById(R.id.sales_Btn);
        SALES.setVisibility(View.GONE);
        SERVICE = findViewById(R.id.service_Btn);
        SERVICE.setVisibility(View.GONE);
        STORE = findViewById(R.id.store_Btn);
        STORE.setVisibility(View.GONE);
        DEVELOPER = (FrameLayout) findViewById(R.id.Developer_Btn);
        DEVELOPER.setVisibility(View.GONE);
        Management = (FrameLayout) findViewById(R.id.Management_Btn);
        Management.setVisibility(View.GONE);
        SalesCounterCard = (CardView) findViewById(R.id.SalesCounterCard);
        HRCounterCard = (CardView) findViewById(R.id.HRCounterCard);
        ProjectsCounterCard = (CardView) findViewById(R.id.VSMarker);
        ProjectsCounterText = (TextView) findViewById(R.id.CustodyMarkerText);
        HRCounterText = (TextView) findViewById(R.id.HRCounterText);
        SalesCounterText = (TextView) findViewById(R.id.SalesCounterText);
        DotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        AdsLayout = (LinearLayout) findViewById(R.id.adsLayout);
        AdsCaptionLayout = (LinearLayout) findViewById(R.id.adsCaptionLayout);
        tasksRecycler = (RecyclerView) findViewById(R.id.tasksRecycler);
        tasksManager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        tasksRecycler.setLayoutManager(tasksManager);
        adsRecycler = (RecyclerView) findViewById(R.id.adsRecycler);
        adManager = new LinearLayoutManager(act,LinearLayoutManager.HORIZONTAL,false);
        Adslist = new ArrayList<ADS_CLASS>();
        warIcon = (ImageView) findViewById(R.id.imageView8);
        warningsCaptionLayout = (LinearLayout) findViewById(R.id.warningsCaptionLayout);
        WarningsLayout = (LinearLayout) findViewById(R.id.warningLayout);
        WarningList = new ArrayList<WARNING_CLASS>();
        warningRecycler = (RecyclerView) findViewById(R.id.warningRecycler);
        warningManager = new LinearLayoutManager(act,LinearLayoutManager.VERTICAL,false);
        warningADApter = new Warning_Adapter(WarningList);
        warningRecycler.setLayoutManager(warningManager);
        warningRecycler.setAdapter(warningADApter);
        Q = Volley.newRequestQueue(act);
    }

    void setActivityActions () {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location != null){
                    CurrentLocation = location ;
                    locationManager.removeUpdates(listener); Log.d("locationService", "i am stopped");
                    registerWorkAttendance(1);
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };
        ErrorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act,Errors.class);
                startActivity(i);
            }
        });
        HR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, com.syrsoft.ratcoms.HR.class);
                startActivity(i);
            }
        });
        SALES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(act,SalesActivity.class);
                    startActivity(i);
            }
        });
        SERVICE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, Projects_Activity.class);
                startActivity(i);
            }
        });
        STORE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(act, ManagementActivity.class);
                    startActivity(i);
            }
        });
        DEVELOPER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, Developer.class);
                startActivity(i);
            }
        });
        setMainButtonsVisibility();
        setWarningVisibility();
        setActivitiesFinish();
        checkMyVacationStatus();
        getAndSetMessagingToken();
        getDirectManager();
        setCounters();
        getEmps();
        getTasks();
        setStartBtn();
    }

    void setMainButtonsVisibility () {
        if (MyApp.MyUser != null ) {
            if (MyApp.MyUser.MyPermissions != null && MyApp.MyUser.MyPermissions.size() > 0) {
                for (Permission p :MyApp.MyUser.MyPermissions) {
                    if (p.getId() == 37) {
                        if (p.getResult()) {
                            SALES.setVisibility(View.VISIBLE);
                        }
                    }
                    if (p.getId() == 38) {
                        if (p.getResult()) {
                            SERVICE.setVisibility(View.VISIBLE);
                        }
                    }
                    if (p.getId() == 39) {
                        if (p.getResult()) {
                            HR.setVisibility(View.VISIBLE);
                        }
                    }
                    if (p.getId() == 40) {
                        if (p.getResult()) {
                            Management.setVisibility(View.VISIBLE);
                        }
                    }
                    if (p.getId() == 41) {
                        if (p.getResult()) {
                            DEVELOPER.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    void setWarningVisibility () {
        if (MyApp.MyUser.Department.contains("Account") || MyApp.db.getUser().JobTitle.equals("Manager")|| MyApp.db.getUser().JobTitle.equals("Sales Manager")) {
            WarningsLayout.setVisibility(View.VISIBLE);
        }
        else {
            WarningsLayout.setVisibility(View.GONE);
        }
        warningRecycler.setVisibility(View.GONE);
        warningsCaptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int TheStatus = warningRecycler.getVisibility();
                if (TheStatus == View.VISIBLE) {
                    warIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.drop_down_icon , null));
                    warningRecycler.setVisibility(View.GONE);
                }
                else if (TheStatus == View.GONE) {
                    warIcon.setImageDrawable(ResourcesCompat.getDrawable( getResources(),R.drawable.bring_up_icon , null));
                    warningRecycler.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void setStartBtn() {
        Log.d("startBtn" , "started");
        if (MyApp.RefME != null) {
            MyApp.RefME.child("AtWork").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            Log.d("startBtn" , "not null");
                            if (snapshot.getValue().toString().equals("1")) {
                                MyApp.RefME.child("AtWorkDate").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue() != null) {
                                                Log.d("attendanceDate" , "not null");
                                                String date = snapshot.getValue().toString();
                                                StartWortBtn.setText("started "+date);
                                            }
                                            else {
                                                MyApp.RefME.child("AtWorkDate").setValue("");
                                            }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                StartWortBtn.setBackgroundResource(R.drawable.mainpage_btns);
                                StartWortBtn.setVisibility(View.VISIBLE);
                            }
                            else if (snapshot.getValue().toString().equals("0")) {
                                StartWortBtn.setText(getResources().getString(R.string.startworkDay));
                                StartWortBtn.setBackgroundResource(R.drawable.btns);
                                StartWortBtn.setVisibility(View.VISIBLE);
                            }
                            else {
                                MyApp.RefME.child("AtWork").setValue("0");
                                StartWortBtn.setText(getResources().getString(R.string.startworkDay));
                                StartWortBtn.setBackgroundResource(R.drawable.btns);
                                StartWortBtn.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            Log.d("startBtn" , "null");
                            MyApp.RefME.child("AtWork").setValue("0");
                            StartWortBtn.setText(getResources().getString(R.string.startworkDay));
                            StartWortBtn.setBackgroundResource(R.drawable.btns);
                            StartWortBtn.setVisibility(View.VISIBLE);
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    setStartBtn();
                }
            });
        }
//        if (MyApp.db.getAtWork() == 0) {
//            StartWortBtn.setText(getResources().getString(R.string.startworkDay));
//            StartWortBtn.setBackgroundResource(R.drawable.btns);
//        }
//        else if (MyApp.db.getAtWork() == 1) {
//            StartWortBtn.setText("started");
//            StartWortBtn.setBackgroundResource(R.drawable.mainpage_btns);
//        }
    }

    void setActivitiesFinish () {
        MyApp.ActList.add(act);
        Log.d("activityCount" ,MyApp.ActList.size()+" befor finishing" );
        if (MyApp.ActList.size() == 2) {
            for (Activity c :MyApp.ActList) {
                Log.d("activityCount" ,c.getLocalClassName());
            }
            MyApp.ActList.get(0).finish();
            MyApp.ActList.remove(0);
        } else if (MyApp.ActList.size() > 2) {
            for (Activity c :MyApp.ActList) {
                Log.d("activityCount" ,c.getLocalClassName());
            }
            MyApp.ActList.get(0).finish();
            MyApp.ActList.remove(0);
            MyApp.ActList.get(0).finish();
            MyApp.ActList.remove(0);
        }
        Log.d("activityCount" ,MyApp.ActList.size()+" after finishing" );
    }

    public static void setCounters() {
        int SalesCounter = MyApp.db.getSalesCounter();

        if (SalesCounter <= 0 ) {
            SalesCounterCard.setVisibility(View.GONE);
        }
        else {
            SalesCounterCard.setVisibility(View.VISIBLE);
            SalesCounterText.setText(String.valueOf(SalesCounter));
        }

        if (MyApp.HRCounter <= 0 ) {
            HRCounterCard.setVisibility(View.GONE);
        }
        else {
            HRCounterCard.setVisibility(View.VISIBLE);
            HRCounterText.setText(String.valueOf(MyApp.HRCounter));
        }

        // set projects _______________
        int PJCounter = 0 ;
        for (int x :MyApp.ProjectsCounters) {
            Log.d("projectsCounter " , String.valueOf(x));
            PJCounter+=x;
        }
        Log.d("projectsCounter" , String.valueOf(PJCounter));
        if (PJCounter <= 0) {
            ProjectsCounterCard.setVisibility(View.GONE);
        }
        else {
            ProjectsCounterCard.setVisibility(View.VISIBLE);
            ProjectsCounterText.setText(String.valueOf(PJCounter));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults != null && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, listener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, listener);
                        Log.d("locationService", "i am started");
                    }
                }
                else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    AttendanceLoadingDialog.close();
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Accept Permission" , "You Must Accept Location Permission");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.getItem(0).setTitle(MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.LogOut ){
            int ID = MyApp.db.getUser().id ;
            MyApp.logOut(ID);
            MyApp.db.logOut();
            prefs.edit().putInt("JobNumber",0).commit();
            Intent i = new Intent(act,Login.class);
            startActivity(i);
            MyApp.MyUser = null ;
            MyApp.ProjectsCounters[0] = MyApp.ProjectsCounters[1] = 0 ;
            MyApp.HRCounter = 0 ;
            act.finish();
        }
        else if (item.getItemId() == R.id.changeMyPassword) {
            ChangeMyPasswordDialog d = new ChangeMyPasswordDialog(act);
            d.show();
        }
        else if (item.getItemId() == R.id.goToHR) {
            Intent i = new Intent(act,HR.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.goToProjects) {
            if (MyApp.MyUser.Department.equals("Sales") || MyApp.MyUser.Department.equals("Management") || MyApp.MyUser.Department.equals("Projects") || MyApp.MyUser.Department.equals("Aluminum Factory") || MyApp.MyUser.Department.equals("Programming")) {
                Intent i = new Intent(act,Projects_Activity.class);
                startActivity(i);
            }
            else {
                ToastMaker.Show(1,"You Are Not Allowed to log to projects",act);
            }
        }
        else if (item.getItemId() == R.id.goToSales) {
            if (MyApp.MyUser.Department.equals("Sales") || MyApp.MyUser.Department.equals("Management")) {
                Intent i = new Intent(act,SalesActivity.class);
                startActivity(i);
            }
            else {
                ToastMaker.Show(1,"You Are Not Sales Employee",act);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void registerWorkTime( String status){
        StringRequest request = new StringRequest(Request.Method.POST, registerWorkTimeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //ToastMaker.Show(1,"INSERTED" , act);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastMaker.Show(1,error.getMessage(),act);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {  //
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH)+1)+"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                String Time = String.valueOf(c.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(c.get(Calendar.MINUTE))+":"+String.valueOf(c.get(Calendar.SECOND));
                Map<String,String> par = new HashMap<String, String>();
                par.put("id", String.valueOf(MyApp.db.getUser().id));
                par.put("jn" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("fn" , MyApp.db.getUser().FirstName);
                par.put("ln" , MyApp.db.getUser().LastName);
                par.put("date" , Date);
                par.put("time" , Time);
                par.put("status" , status);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    private void getAndSetMessagingToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("gettingTokenError", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("Token" , token);
                        sendTokenToDB(token);
                        MyApp.RefME.child("token").setValue(token);
                    }
                });
    }

    private void sendTokenToDB(String token){
        StringRequest request = new StringRequest(Request.Method.POST, setTokenUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sendingToken" , token+" Token Sent "+ response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("id" , String.valueOf(MyApp.db.getUser().id));
                par.put("jn" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("token" , token);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getDirectManager() {
        MyApp.DIRECT_MANAGER = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DirectManager);
        if (MyApp.DIRECT_MANAGER != null) {
            if (MyApp.DIRECT_MANAGER.VacationStatus == 1) {
                MyApp.DIRECT_MANAGER = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.DIRECT_MANAGER.VacationAlternative);
            }
        }
        MyApp.DEPARTMENT_MANAGER = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.MyUser.DepartmentManager);
        if (MyApp.DEPARTMENT_MANAGER != null) {
            if (MyApp.DEPARTMENT_MANAGER.VacationStatus == 1) {
                MyApp.DEPARTMENT_MANAGER = USER.searchUserByJobNumber(MyApp.EMPS,MyApp.DEPARTMENT_MANAGER.VacationAlternative);
            }
        }
        if (MyApp.DIRECT_MANAGER != null && MyApp.DEPARTMENT_MANAGER != null) {
            Log.d("directManager&Dep" ,MyApp.DIRECT_MANAGER.FirstName+ " "+MyApp.DEPARTMENT_MANAGER.FirstName ) ;
        }
    }

    void getEmps() {
        Intent r = new Intent(act,CountersService.class);
        MyApp.app.startService(r);
        if (MyApp.RefME != null) {
            if (MyApp.MyUser.Department.equals("Account") || MyApp.MyUser.JobTitle.equals("Manager") || MyApp.MyUser.JobTitle.equals("Sales Manager")) {
                MyApp.RefME.child("IDsWarningNotifications").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            if (snapshot.getValue().toString().equals("1")) {
                                makeIdsWarningNotification(EMPS);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if (MyApp.MyUser.Department.equals("Account") || MyApp.MyUser.JobTitle.equals("Manager") || MyApp.MyUser.JobTitle.equals("Sales Manager") ) {
                MyApp.RefME.child("PASSPORTsWarningNotification").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            if (snapshot.getValue().toString().equals("1")) {
                                makePassportssWarningNotification(EMPS);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if (MyApp.MyUser.Department.equals("Account") || MyApp.MyUser.JobTitle.equals("Manager") || MyApp.MyUser.JobTitle.equals("Sales Manager")) {
                MyApp.RefME.child("CONTRACTsWarningNotification").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            if (snapshot.getValue().toString().equals("1")) {
                                makeContractssWarningNotification(EMPS);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        for (USER emp : EMPS) {
            if (emp.DirectManager == MyApp.db.getUser().JobNumber) {
                MyApp.ManagerStatus = true ;
                break;
            }
        }
        if (warningCounter > 0 ) {
            WarningsLayout.setVisibility(View.VISIBLE);
        }
        else {
            WarningsLayout.setVisibility(View.GONE);
        }
    }

    void makeIdsWarningNotification(List<USER> list)
    {
        Calendar now = Calendar.getInstance(Locale.getDefault());

        for (USER u : list)
        {
            String t = u.IDExpireDate;
            try {

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(t);
                Calendar ExpireDate = Calendar.getInstance();
                ExpireDate.setTime(date);
                ExpireDate.add(Calendar.MONTH,-1);
                Log.d("directWarning" , now.getTime().toString() +" expire: "+ExpireDate.getTime().toString());
                if (now.after(ExpireDate))
                {
//                    int ReqCode = r.nextInt() ;
//                    Intent i = new Intent(act , ManageIds.class);
//                    showNotification(act,"ID Expire Warning ","ID Expire Warning For "+u.FirstName+" "+u.LastName,i,ReqCode);
                    warningCounter++;
                    boolean status = false ;
                    for(WARNING_CLASS ww :WarningList ){
                        if (ww.JobNumber == u.JobNumber && ww.FirstName.equals(u.FirstName) && ww.LastName.equals(u.LastName)&& ww.warning.equals("ID")){
                            status = true ;
                        }
                    }
                    if (!status){
                        WARNING_CLASS w = new WARNING_CLASS(u.JobNumber,u.FirstName,u.LastName,"ID",u.IDExpireDate);
                        WarningList.add(w);
                        warningADApter.notifyDataSetChanged();
                    }

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        warningCounterTV.setText(warningCounter+"");
        if (WarningList.size() == 0){
            LinearLayout l = (LinearLayout)findViewById(R.id.warningLayout);
            l.setVisibility(View.GONE);
        }
        else {
            LinearLayout l = (LinearLayout)findViewById(R.id.warningLayout);
            l.setVisibility(View.VISIBLE);
        }
        for (WARNING_CLASS w : WarningList) {
            if (w.warning.equals("ID")) {
                int ReqCode = r.nextInt() ;
                Intent i = new Intent(act , ManagePassports.class);
                showNotification(act,"ID Expire Warning ","You Have ID Expire Warning",i,ReqCode);
                break;
            }
        }
    }

    void makePassportssWarningNotification(List<USER> list)
    {
        Calendar now = Calendar.getInstance(Locale.getDefault());

        for (USER u : list)
        {
            String t = u.PassportExpireDate;
            try {

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(t);
                Calendar ExpireDate = Calendar.getInstance();
                ExpireDate.setTime(date);
                if (now.after(ExpireDate))
                {
//                    int ReqCode = r.nextInt() ;
//                    Intent i = new Intent(act , ManagePassports.class);
//                    showNotification(act,"PASSPORT Expire Warning ","PASSPORT Expire Warning For "+u.FirstName+" "+u.LastName,i,ReqCode);
                    warningCounter++;
                    boolean status = false ;
                    for(WARNING_CLASS ww :WarningList ){
                        if (ww.JobNumber == u.JobNumber && ww.FirstName.equals(u.FirstName) && ww.LastName.equals(u.LastName) && ww.warning.equals("Passport")){
                            status = true ;
                        }
                    }
                    if (!status){
                        WARNING_CLASS w = new WARNING_CLASS(u.JobNumber,u.FirstName,u.LastName,"Passport",u.PassportExpireDate);
                        WarningList.add(w);
                        warningADApter.notifyDataSetChanged();

                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        warningCounterTV.setText(warningCounter+"");
        if (WarningList.size() == 0){
            LinearLayout l = (LinearLayout)findViewById(R.id.warningLayout);
            l.setVisibility(View.GONE);
        }
        else {
            LinearLayout l = (LinearLayout)findViewById(R.id.warningLayout);
            l.setVisibility(View.VISIBLE);
        }
        for (WARNING_CLASS w : WarningList) {
            if (w.warning.equals("Passport")) {
                int ReqCode = r.nextInt() ;
                Intent i = new Intent(act , ManagePassports.class);
                showNotification(act,"PASSPORT Expire Warning ","You Have PASSPORT Expire Warning",i,ReqCode);
                break;
            }
        }
    }

    void makeContractssWarningNotification(List<USER> list)
    {
        Calendar now = Calendar.getInstance(Locale.getDefault());

        for (USER u : list)
        {
            String t = u.ContractExpireDate;
            try {

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(t);
                Calendar ExpireDate = Calendar.getInstance();
                ExpireDate.setTime(date);
                if (now.after(ExpireDate))
                {
//                    int ReqCode = r.nextInt() ;
//                    Intent i = new Intent(act , ManageContracts.class);
//                    showNotification(act,"CONTRACT Expire Warning ","CONTRACT Expire Warning For "+u.FirstName+" "+u.LastName,i,ReqCode);
                    warningCounter++;
                    boolean status = false ;
                    for(WARNING_CLASS ww :WarningList ){
                        if (ww.JobNumber == u.JobNumber && ww.FirstName.equals(u.FirstName) && ww.LastName.equals(u.LastName) && ww.warning.equals("Contract")){
                            status = true ;
                        }
                    }
                    if (!status){
                        WARNING_CLASS w = new WARNING_CLASS(u.JobNumber,u.FirstName,u.LastName,"Contract",u.ContractExpireDate);
                        WarningList.add(w);
                        warningADApter.notifyDataSetChanged();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        warningCounterTV.setText(warningCounter+"");
        if (WarningList.size() == 0){
            LinearLayout l = (LinearLayout)findViewById(R.id.warningLayout);
            l.setVisibility(View.GONE);
        }
        else {
            LinearLayout l = (LinearLayout)findViewById(R.id.warningLayout);
            l.setVisibility(View.VISIBLE);
        }
        for (WARNING_CLASS w : WarningList) {
            if (w.warning.equals("Contract")) {
                int ReqCode = r.nextInt() ;
                Intent i = new Intent(act , ManagePassports.class);
                showNotification(act,"CONTRACT Expire Warning ","You Have CONTRACT Expire Warning",i,ReqCode);
                break;
            }
        }
    }

    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }

    void registerWorkAttendance( int op) {
        StringRequest request = new StringRequest(Request.Method.POST, registerWorkTimeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("locationService", response);
                AttendanceLoadingDialog.close();
                if (response.equals("0")) {
                    new MESSAGE_DIALOG(act,"error","error registering attendance");
                }
                else if (response.equals("-1")) {
                    new MESSAGE_DIALOG(act,"error","No Parameters");
                }
                else if (Integer.parseInt(response) > 0 ) {
                    ToastMaker.Show(1,getResources().getString(R.string.saved),act);
                    if (op == 1) {
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        String date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                        attendanceTriger = true ;
                        if (MyApp.RefME != null) {
                            if (MyApp.RefME.child("AtWork") != null) {
                                MyApp.RefME.child("AtWork").setValue("1");
                            }
                            if (MyApp.RefME.child("AtWorkDate") != null) {
                                MyApp.RefME.child("AtWorkDate").setValue(date);
                            }
                        }
                        StartWortBtn.setText("started "+date);
                        StartWortBtn.setBackgroundResource(R.drawable.mainpage_btns);
                    }
                    else if (op == 0) {
                        attendanceTriger = false ;
                        CurrentLocation = null ;
                        MyApp.RefME.child("AtWork").setValue("0");
                        StartWortBtn.setText(getResources().getString(R.string.startworkDay));
                        StartWortBtn.setBackgroundResource(R.drawable.btns);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("locationService", error.getMessage());
                AttendanceLoadingDialog.close();
                if (op == 1) {
                    StartWortBtn.setText("started");
                    StartWortBtn.setBackgroundResource(R.drawable.mainpage_btns);
                }
                else {
                    StartWortBtn.setText("Ended");
                    StartWortBtn.setBackgroundResource(R.drawable.btns);
                }

            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                String Time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                Log.d("locationService", Date + " "+ Time);
                Map<String,String> par = new HashMap<String, String>();
                par.put("JobNumber",String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("EmpID",String.valueOf(MyApp.db.getUser().id));
                par.put("Name",MyApp.db.getUser().FirstName+" "+MyApp.db.getUser().LastName);
                par.put("Date",Date);
                par.put("Time" , Time);
                par.put("Op" , String.valueOf(op));
                if (CurrentLocation == null ){
                    par.put("LA","");
                    par.put("LO","");
                }
                else {
                    par.put("LA",String.valueOf(CurrentLocation.getLatitude()));
                    par.put("LO",String.valueOf(CurrentLocation.getLongitude()));
                }

                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getTasks () {
        Q = Volley.newRequestQueue(act);
        StringRequest req = new StringRequest(Request.Method.POST, getTasksUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("gettingTasks" , response);
                if (response.equals("0")) {
                    LinearLayout tasksLayout = (LinearLayout) findViewById(R.id.taskLayout);
                    tasksLayout.setVisibility(View.GONE);
                }
                else {
                    LinearLayout tasksLayout = (LinearLayout) findViewById(R.id.taskLayout);
                    tasksLayout.setVisibility(View.VISIBLE);
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            TASK t = new TASK(row.getInt("id"),row.getInt("Salesman"),row.getString("Date"),row.getInt("ClientID"),row.getString("Action"),row.getInt("RefrenceID"));
                            TASKs.add(t);
                            getClient(t.Client,i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("gettingTasks" , e.toString());
                    }
                    Task_Adapter = new TasksAdapter(TASKs);
                    tasksRecycler.setAdapter(Task_Adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("gettingTasks" , error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Map<String,String> par = new HashMap<String, String>();
                par.put("Salesman", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Date",Date);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

    void getClient(int ID , int index) {
        final CLIENT_CLASS[] CLIENT = {null};
        Loading l = new Loading(act);
        //l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("clientResponse" ,response+" "+ID);
                //l.close();
                if (response.equals("0")) {

                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        CLIENT[0] = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                        //City.setText(CLIENT.City);
                        //Phone.setText(CLIENT.PhonNumber);
                        //Field.setText(CLIENT.FieldOfWork);
                        TASKs.get(index).setClient(CLIENT[0]);
                        Task_Adapter = new TasksAdapter(TASKs);
                        tasksRecycler.setAdapter(Task_Adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //getResponsible();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //l.close();
                Log.d("clientResponse" ,error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map <String,String> par = new HashMap<String, String>();
                par.put("ID", String.valueOf(ID) );
                return par;
            }
        };
        Q.add(request);
    }

    void getLast5Ads() {
        Log.d("getAdsResp" , "i am started");
        Adslist.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getLastAdsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getAdsResp" , response);
                if (!response.equals("0")) {
                    try {
                        JSONArray arr = new JSONArray(response) ;
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ADS_CLASS a = new ADS_CLASS(row.getInt("id"),row.getString("Title"),row.getString("Message"),row.getString("ImageLink"),row.getString("FileLink"),row.getString("Date"));
                            Adslist.add(a);
                        }
                        if (Adslist.size() == 0 ){
                            LinearLayout l = (LinearLayout)findViewById(R.id.adsLayout);
                            l.setVisibility(View.GONE);
                        }
                        else {
                            LinearLayout l = (LinearLayout)findViewById(R.id.adsLayout);
                            l.setVisibility(View.VISIBLE);
                            Collections.reverse(Adslist);
                            adsadapter = new Ads_Adapter(Adslist);
                            adsRecycler.setLayoutManager(adManager);
                            AdsRedDot.setVisibility(View.VISIBLE);
                            DotsLayout.removeAllViews();
                            for (int i=0;i<Adslist.size();i++) {
                                ImageView dot = new ImageView(act);
                                dot.setImageResource(R.drawable.off_dot);
                                dot.setLayoutParams(new LinearLayout.LayoutParams(20,20));
                                dot.setPadding(0,0,3,0);
                                DotsLayout.addView(dot);
                            }
                            adsRecycler.setAdapter(adsadapter);
                            setAdsCounter();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    adsadapter = new Ads_Adapter(Adslist);
                    adsRecycler.setLayoutManager(adManager);
                    adsRecycler.setAdapter(adsadapter);
                }

            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getAdsResp" , error.toString());
            }
        });
        Volley.newRequestQueue(act).add(request) ;
    }

    void setAdsCounter() {
        MyApp.ADS_Counter = 0 ;
//        for (int i=0;i<Adslist.size();i++) {
//            Log.d("adsCounterProb",Adslist.get(i).id+" ");
//        }
        if (MyApp.ADS_DB.getLastAdId() != -1) {
            if (MyApp.ADS_DB.getLastAdId() < Adslist.get(0).id) {
                Log.d("adsCounterProb", "DB size "+MyApp.ADS_DB.getAds().size()+" last id in DB "+MyApp.ADS_DB.getLastAdId() + " last first id in list " + Adslist.get(0).id);
                MyApp.ADS_Counter = Adslist.get(0).id - MyApp.ADS_DB.getLastAdId();
                adsCounterTV.setText(String.valueOf(MyApp.ADS_Counter));
                int last = MyApp.ADS_Counter - 1 ;
                Log.d("adsCounterProb", "start "+last + " list size " + Adslist.size() + " counter value " + MyApp.ADS_Counter);
                for (int i = last ; i >= 0 ; i--) {
                    Log.d("adsCounterProb", Adslist.get(i).id + " "+i);
                    MyApp.ADS_DB.insertAd(Adslist.get(i).id, Adslist.get(i).Title, Adslist.get(i).Message, Adslist.get(i).ImageLink, Adslist.get(i).FileLink, Adslist.get(i).Date);
                }
                AdsRedDot.setVisibility(View.VISIBLE);
            }
            else {
                Log.d("adsCounterProb", MyApp.ADS_DB.getAds().size()+" "+MyApp.ADS_DB.getLastAdId() + " " + Adslist.get(0).id);
                AdsRedDot.setVisibility(View.GONE);
            }
        }
        else {
            MyApp.ADS_Counter = Adslist.size();
            adsCounterTV.setText(String.valueOf(MyApp.ADS_Counter));
            //Log.d("adsCounterProb", start + " " + Adslist.size() + " " + MyApp.ADS_Counter);
            Log.d("adsCounterProb", "list size "+Adslist.size() + " counter value " + MyApp.ADS_Counter);
            for (int i = Adslist.size()-1 ; i >= 0 ; i--) {
                Log.d("adsCounterProb", Adslist.get(i).id+" "+ i);
                MyApp.ADS_DB.insertAd(Adslist.get(i).id, Adslist.get(i).Title, Adslist.get(i).Message, Adslist.get(i).ImageLink, Adslist.get(i).FileLink, Adslist.get(i).Date);
            }

        }
    }

    static void setAdsDots() {
        for (int i=0 ;i< DotsLayout.getChildCount();i++) {
            ImageView img = (ImageView) DotsLayout.getChildAt(i) ;
            img.setImageResource(R.drawable.off_dot);
        }
        ImageView img = (ImageView) DotsLayout.getChildAt(CurrentAd) ;
        img.setImageResource(R.drawable.on_dot);
    }

    public void startWorkGo(View view) {
        if (MyApp.RefME != null) {
            MyApp.RefME.child("AtWork").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        if (snapshot.getValue().toString().equals("1")) {
                            AttendanceLoadingDialog.show();
                            registerWorkAttendance(0);
                        }
                        else if (snapshot.getValue().toString().equals("0")) {
                            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                                    AttendanceLoadingDialog.show();
                                    CurrentLocation = null ;
                                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 2, 1, listener);
                                    }
                                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 2, 1, listener);
                                    }
                                }
                                else {
                                    final AlertDialog.Builder builder =  new AlertDialog.Builder(act);
                                    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                                    final String message = getResources().getString(R.string.locationNotEnabledMessage);
                                    String title = getResources().getString(R.string.locationNotEnabledTitle);
                                    builder.setMessage(message).setTitle(title).setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface d, int id) {
                                                    act.startActivity(new Intent(action));
                                                    d.dismiss();
                                                }
                                            }).setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface d, int id) {
                                                    d.dismiss();
                                                    //startEndWorkDay.setChecked(false);
                                                }
                                            });
                                    builder.create().show();
                                }
                            }
                            else {
                                Log.d("locationService", "no permission");
                                AlertDialog.Builder B = new AlertDialog.Builder(act);
                                B.setTitle(getResources().getString(R.string.acceptLocationPermissionTitle));
                                B.setMessage(getResources().getString(R.string.acceptLocationPermissionMessage));
                                B.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},5);
                                    }
                                });
                                B.create();
                                B.show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    startWorkGo(view);
                }
            });
        }
//        if (MyApp.db.getAtWork() == 0) {
//            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
//                    AttendanceLoadingDialog.show();
//                    CurrentLocation = null ;
//                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 2, 1, listener);
//                    }
//                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 2, 1, listener);
//                    }
//                }
//                else {
//                    final AlertDialog.Builder builder =  new AlertDialog.Builder(act);
//                    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
//                    final String message = getResources().getString(R.string.locationNotEnabledMessage);
//                    String title = getResources().getString(R.string.locationNotEnabledTitle);
//                    builder.setMessage(message).setTitle(title).setPositiveButton("OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface d, int id) {
//                                            act.startActivity(new Intent(action));
//                                            d.dismiss();
//                                        }
//                                    }).setNegativeButton("Cancel",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface d, int id) {
//                                            d.dismiss();
//                                            //startEndWorkDay.setChecked(false);
//                                        }
//                                    });
//                    builder.create().show();
//                }
//            }
//            else {
//                Log.d("locationService", "no permission");
//                AlertDialog.Builder B = new AlertDialog.Builder(act);
//                B.setTitle(getResources().getString(R.string.acceptLocationPermissionTitle));
//                B.setMessage(getResources().getString(R.string.acceptLocationPermissionMessage));
//                B.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},5);
//                    }
//                });
//                B.create();
//                B.show();
//            }
//        }
//        else if (MyApp.db.getAtWork() == 1) {
//            AttendanceLoadingDialog.show();
//            registerWorkAttendance(0);
//            MyApp.db.setAtWork(0);
//        }
    }

    void checkMyVacationStatus() {
        Log.d("setIamInVacation" , "i am in");
        StringRequest Req = new StringRequest(Request.Method.POST, getMyVacationsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setIamInVacation" , response);
                if (response.equals("0")) {

                }
                else {
                    List<VACATION_CLASS> vacations = new ArrayList<>();
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            VACATION_CLASS v = new VACATION_CLASS(row.getInt("id"),row.getInt("EmpID"),row.getInt("JobNumber"),row.getString("FName"),row.getString("LName"),row.getInt("DirectManager"),row.getString("DirectManagerName"),row.getString("JobTitle"),row.getInt("VacationType"),row.getString("SendDate"),row.getString("StartDate"),row.getInt("VacationDays"),row.getString("EndDate"),row.getInt("AlternativeID"),row.getString("AlternativeName"),row.getString("Location"),row.getString("Notes"),null,row.getInt("Status"),row.getInt("BackStatus"),row.getInt("VSalaryStatus"));
                            vacations.add(v);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (vacations.size()>0) {
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        String today = c.get(Calendar.YEAR) + "-" +(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                        for (int i = 0 ; i < vacations.size() ; i ++) {
                            try {
                                Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(vacations.get(i).StartDate);
                                Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(vacations.get(i).EndDate);
                                Date now=new SimpleDateFormat("yyyy-MM-dd").parse(today);
                                if (now.after(date1)  && now.before(date2)) {
                                    setIamInVacation(vacations.get(i).AlternativeID);
                                }
                                else {
                                    setIamInOutOfVacation();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setIamInVacation" , error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String,String>();
                par.put("JobNumber", String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Q.add(Req);
    }

    void setIamInVacation (int alternative) {
        Button workButton = findViewById(R.id.button52);
        workButton.setText("I am In Vacation Now");
        workButton.setEnabled(false);
        MyApp.MyUser.VacationStatus = 1;
        StringRequest Req = new StringRequest(Request.Method.POST, setMyVacationStatusUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setIamInVacation" , response);
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setIamInVacation" , error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String,String>();
                par.put("jn", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Status","1");
                par.put("alt", String.valueOf(alternative));
                return par;
            }
        };
        Q.add(Req);
    }

    void setIamInOutOfVacation () {
        Button workButton = findViewById(R.id.button52);
        workButton.setEnabled(true);
        MyApp.MyUser.VacationStatus = 0;
        StringRequest Req = new StringRequest(Request.Method.POST, setMyVacationStatusUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setIamInVacation" , response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setIamInVacation" , error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String,String>();
                par.put("jn", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Status","0");
                par.put("alt","0");
                return par;
            }
        };
        Q.add(Req);
    }

    void getIfThereIsNewRating () {
        final int[] counter = {0};
        MyApp.RatingCounter = 0 ;
        if (MyApp.ManagerStatus) {
            if (MyApp.MyUser.MyStaff != null && MyApp.MyUser.MyStaff.size() != 0) {
                StringRequest req = new StringRequest(Request.Method.POST, checkRatingAvaliability, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ratingCounter", response);
                        int res = Integer.parseInt(response);
                            MyApp.RatingCounter = res ;
                            MyApp.HRCounter = MyApp.HRCounter - MyApp.TempRatingCounter ;
                            MyApp.HRCounter = MyApp.HRCounter + MyApp.RatingCounter ;
                            //Log.d("ratingCounter", "temp "+MyApp.TempRatingCounter+" hr "+ MyApp.HRCounter+" rat "+MyApp.RatingCounter);
                            MyApp.TempRatingCounter = MyApp.RatingCounter ;
                            setCounters();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ratingCounter", error.toString());
                    }
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Calendar ca = Calendar.getInstance(Locale.getDefault());
                        Map<String,String> par = new HashMap<String,String>();
                        for (int i=0;i<MyApp.MyUser.MyStaff.size();i++) {
                            par.put("jn"+i, String.valueOf(MyApp.MyUser.MyStaff.get(i).JobNumber));
                        }
                        par.put("Count", String.valueOf(MyApp.MyUser.MyStaff.size()));
                        par.put("Month", String.valueOf(ca.get(Calendar.MONTH)));
                        par.put("Year", String.valueOf(ca.get(Calendar.YEAR)));
                        return par;
                    }
                };
                Volley.newRequestQueue(act).add(req);
//                for (int i = 0; i < MyApp.MyUser.MyStaff.size(); i++) {
//                    int finalI = i;
//                    getIfEmpHasRating(MyApp.MyUser.MyStaff.get(finalI).id, MyApp.MyUser.MyStaff.get(finalI).JobNumber, new VollyCallback() {
//                        @Override
//                        public void onSuccess(String s) {
//                            if (s.equals("1")) {
//                                counter[0]++;
//                                MyApp.RatingCounter++ ;
//                            }
//                            Log.d("ratingCounter", "i "+finalI+" res "+ s+" rt "+MyApp.RatingCounter);
//                            if (finalI == (MyApp.MyUser.MyStaff.size()-1)) {
//                                Log.d("ratingCounter", " i am in");
//                                MyApp.HRCounter = MyApp.HRCounter - MyApp.TempRatingCounter ;
//                                MyApp.HRCounter = MyApp.HRCounter + MyApp.RatingCounter ;
//                                Log.d("ratingCounter", "temp "+MyApp.TempRatingCounter+" hr "+ MyApp.HRCounter+" rat "+MyApp.RatingCounter);
//                                MyApp.TempRatingCounter = MyApp.RatingCounter ;
//                                setCounters();
//                            }
//                        }
//                        @Override
//                        public void onFailed(String error) {
//
//                        }
//                    });
//                }
                //Log.d("ratingCounter", counter[0] + " my staff are "+MyApp.MyUser.MyStaff.size());
            }
            else {
                Log.d("ratingCounter", counter[0] + " my staff false");
            }
        }
        else {
            Log.d("ratingCounter", counter[0] + " manager status false");
        }
    }

    //_____________________________________
}