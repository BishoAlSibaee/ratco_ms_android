package com.syrsoft.ratcoms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.syrsoft.ratcoms.Developer.Developer;
import com.syrsoft.ratcoms.HRActivities.ManagePassports;
import com.syrsoft.ratcoms.HRActivities.VACATION_CLASS;
import com.syrsoft.ratcoms.Interfaces.AttendTimeCallback;
import com.syrsoft.ratcoms.Interfaces.UserVacationTodayCallback;
import com.syrsoft.ratcoms.Interfaces.getUserAttendance;
import com.syrsoft.ratcoms.SALESActivities.CLIENT_CLASS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.MessageFormat;
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
    public static List<USER> EMPLOYEES;
    private Random r ;
    public static Ads_Adapter adsAdapter;
    private List<ADS_CLASS> AdsList;
    private RecyclerView.LayoutManager adManager;
    private RecyclerView adsRecycler , warningRecycler , tasksRecycler;
    public static List<WARNING_CLASS> WarningList ;
    public static Warning_Adapter warningADApter ;
    ImageView warIcon ;
    boolean attendanceTrigger = false ;
    Loading AttendanceLoadingDialog ;
    int warningCounter = 0 ;
    private Button ErrorsBtn , StartWortBtn ;
    static CardView AdsRedDot , SalesCounterCard , HRCounterCard , ProjectsCounterCard ;
    public static boolean isRunning = false ;
    List<TASK> TASKs ;
    RequestQueue Q ;
    TasksAdapter Task_Adapter ;
    public static int CurrentAd = 0 ;
    SharedPreferences prefs ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        setActivity();
        setActivityActions();
        setUserAttendReminder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true ;
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
        prefs = getSharedPreferences("com.syrsoft.ratcoms", MODE_PRIVATE);
        r = new Random();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Q = Volley.newRequestQueue(act);
        setLists();
        setViews();
        setViewsVisibility();
    }

    void setActivityActions () {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                CurrentLocation = location ;
                locationManager.removeUpdates(listener);
                Log.d("locationService", "i am stopped");
                registerWorkAttendance(1);
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
        ErrorsBtn.setOnClickListener(v -> {
            Intent i = new Intent(act,Errors.class);
            startActivity(i);
        });
        HR.setOnClickListener(v -> {
            Intent i = new Intent(act, com.syrsoft.ratcoms.HR.class);
            startActivity(i);
        });
        SALES.setOnClickListener(v -> {
                Intent i = new Intent(act,SalesActivity.class);
                startActivity(i);
        });
        SERVICE.setOnClickListener(v -> {
            Intent i = new Intent(act, Projects_Activity.class);
            startActivity(i);
        });
        STORE.setOnClickListener(v -> {

        });
        Management.setOnClickListener(v -> {
                Intent i = new Intent(act, ManagementActivity.class);
                startActivity(i);
        });
        DEVELOPER.setOnClickListener(v -> {
            Intent i = new Intent(act, Developer.class);
            startActivity(i);
        });
        setMainButtonsVisibility();
        setWarningVisibility();
        setActivitiesFinish();
        checkMyVacationStatus();
        getAndSetMessagingToken();
        getDirectManager();
        setCounters();
        getEmployees();
        getTasks();
        setStartBtn();
    }

    void setViews() {
        StartWortBtn = findViewById(R.id.button52);
        ErrorsBtn = findViewById(R.id.errorsBtn);
        AdsRedDot = findViewById(R.id.AdsCardView);
        HR = findViewById(R.id.hr_Btn);
        SALES = findViewById(R.id.sales_Btn);
        SERVICE = findViewById(R.id.service_Btn);
        STORE = findViewById(R.id.store_Btn);
        DEVELOPER = findViewById(R.id.Developer_Btn);
        Management = findViewById(R.id.Management_Btn);
        SalesCounterCard = findViewById(R.id.SalesCounterCard);
        HRCounterCard = findViewById(R.id.HRCounterCard);
        ProjectsCounterCard = findViewById(R.id.VSMarker);
        tasksRecycler = findViewById(R.id.tasksRecycler);
        adsRecycler = findViewById(R.id.adsRecycler);
        warIcon = findViewById(R.id.imageView8);
        warningRecycler = findViewById(R.id.warningRecycler);
        AttendanceLoadingDialog = new Loading(act) ;
        RecyclerView.LayoutManager tasksManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        adManager = new LinearLayoutManager(act,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager warningManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        tasksRecycler.setLayoutManager(tasksManager);
        warningRecycler.setLayoutManager(warningManager);
        warningADApter = new Warning_Adapter(WarningList);
        warningRecycler.setAdapter(warningADApter);
    }

    void setViewsVisibility() {
        HR.setVisibility(View.GONE);
        SALES.setVisibility(View.GONE);
        SERVICE.setVisibility(View.GONE);
        STORE.setVisibility(View.GONE);
        DEVELOPER.setVisibility(View.GONE);
        Management.setVisibility(View.GONE);
        StartWortBtn.setVisibility(View.INVISIBLE);
    }

    void setLists() {
        EMPLOYEES = new ArrayList<>();
        TASKs = new ArrayList<>();
        AdsList = new ArrayList<>();
        WarningList = new ArrayList<>();
    }

    void setMainButtonsVisibility() {
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

    void setWarningVisibility() {
        LinearLayout warningsCaptionLayout = findViewById(R.id.warningsCaptionLayout);
        LinearLayout WarningsLayout = findViewById(R.id.warningLayout);
        if (MyApp.MyUser.Department.contains("Account") || MyApp.db.getUser().JobTitle.equals("Manager")|| MyApp.db.getUser().JobTitle.equals("Sales Manager")) {
            WarningsLayout.setVisibility(View.VISIBLE);
        }
        else {
            WarningsLayout.setVisibility(View.GONE);
        }
        warningRecycler.setVisibility(View.GONE);
        warningsCaptionLayout.setOnClickListener(v -> {
            int TheStatus = warningRecycler.getVisibility();
            if (TheStatus == View.VISIBLE) {
                warIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.drop_down_icon , null));
                warningRecycler.setVisibility(View.GONE);
            }
            else if (TheStatus == View.GONE) {
                warIcon.setImageDrawable(ResourcesCompat.getDrawable( getResources(),R.drawable.bring_up_icon , null));
                warningRecycler.setVisibility(View.VISIBLE);
            }
        });
    }

    void setStartBtn() {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        String Today = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
        if (MyApp.MyUser != null) {
            MyApp.MyUser.getIsUserAttendNow(Q, new getUserAttendance() {
                @Override
                public void onResultBack(boolean result) {
                    Log.d("AttendToday",result+"");
                    if (result) {
                        StartWortBtn.setVisibility(View.VISIBLE);
                        StartWortBtn.setBackgroundResource(R.drawable.mainpage_btns);
                        StartWortBtn.setText(MessageFormat.format("started {0}", Today));
                    }
                    else {
                        StartWortBtn.setVisibility(View.VISIBLE);
                        StartWortBtn.setBackgroundResource(R.drawable.btns);
                        StartWortBtn.setText(getResources().getString(R.string.startworkDay));
                    }
                }

                @Override
                public void onError(String error) {
                    Log.d("AttendToday",error);
                    StartWortBtn.setVisibility(View.VISIBLE);
                    StartWortBtn.setBackgroundResource(R.drawable.btns);
                    StartWortBtn.setText(getResources().getString(R.string.startworkDay));
                }
            });
        }
    }

    void setActivitiesFinish () {
        MyApp.ActList.add(act);
        if (MyApp.ActList.size() == 2) {
            MyApp.ActList.get(0).finish();
            MyApp.ActList.remove(0);
        } else if (MyApp.ActList.size() > 2) {
            MyApp.ActList.get(0).finish();
            MyApp.ActList.remove(0);
            MyApp.ActList.get(0).finish();
            MyApp.ActList.remove(0);
        }
    }

    public static void setCounters() {
        TextView ProjectsCounterText = act.findViewById(R.id.CustodyMarkerText);
        TextView HRCounterText = act.findViewById(R.id.HRCounterText);
        TextView SalesCounterText = act.findViewById(R.id.SalesCounterText);
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
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, listener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, listener);
                        Log.d("locationService", "i am started");
                    }
                }
                else if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    AttendanceLoadingDialog.close();
                    new MESSAGE_DIALOG(act,"Accept Permission" , "You Must Accept Location Permission");
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
            prefs.edit().putInt("JobNumber",0).apply();
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

    private void getAndSetMessagingToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("gettingTokenError", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    Log.d("Token" , token);
                    sendTokenToDB(token);
                    MyApp.RefME.child("token").setValue(token);
                });
    }

    private void sendTokenToDB(String token){
        StringRequest request = new StringRequest(Request.Method.POST, ProjectUrls.setTokenUrl, response -> Log.d("sendingToken" , token+" Token Sent "+ response), error -> {
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> par = new HashMap<>();
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

    void getEmployees() {
        LinearLayout WarningsLayout = findViewById(R.id.warningLayout);
        Intent r = new Intent(act,CountersService.class);
        MyApp.app.startService(r);
        if (MyApp.RefME != null) {
            if (MyApp.MyUser.Department.equals("Account") || MyApp.MyUser.JobTitle.equals("Manager") || MyApp.MyUser.JobTitle.equals("Sales Manager")) {
                MyApp.RefME.child("IDsWarningNotifications").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            if (snapshot.getValue().toString().equals("1")) {
                                makeIdsWarningNotification(EMPLOYEES);
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
                                makePassportsWarningNotification(EMPLOYEES);
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
                                makeContractsWarningNotification(EMPLOYEES);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        for (USER emp : EMPLOYEES) {
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

    @SuppressLint("NotifyDataSetChanged")
    void makeIdsWarningNotification(List<USER> list) {
        TextView warningCounterTV = findViewById(R.id.warningCounter);
        Calendar now = Calendar.getInstance(Locale.getDefault());
        for (USER u : list) {
            String t = u.IDExpireDate;
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(t);
                Calendar ExpireDate = Calendar.getInstance();
                if (date != null) {
                    ExpireDate.setTime(date);
                }
                ExpireDate.add(Calendar.MONTH,-1);
                Log.d("directWarning" , now.getTime()+" expire: "+ExpireDate.getTime());
                if (now.after(ExpireDate)) {
                    warningCounter++;
                    boolean status = false ;
                    for (WARNING_CLASS ww :WarningList ) {
                        if (ww.JobNumber == u.JobNumber && ww.FirstName.equals(u.FirstName) && ww.LastName.equals(u.LastName) && ww.warning.equals("ID")) {
                            status = true;
                            break;
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
        warningCounterTV.setText(MessageFormat.format("{0}", warningCounter));
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

    @SuppressLint("NotifyDataSetChanged")
    void makePassportsWarningNotification(List<USER> list) {
        TextView warningCounterTV = findViewById(R.id.warningCounter);
        Calendar now = Calendar.getInstance(Locale.getDefault());
        for (USER u : list) {
            String t = u.PassportExpireDate;
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(t);
                Calendar ExpireDate = Calendar.getInstance();
                if (date != null) {
                    ExpireDate.setTime(date);
                }
                if (now.after(ExpireDate)) {
                    warningCounter++;
                    boolean status = false ;
                    for(WARNING_CLASS ww :WarningList ){
                        if (ww.JobNumber == u.JobNumber && ww.FirstName.equals(u.FirstName) && ww.LastName.equals(u.LastName) && ww.warning.equals("Passport")) {
                            status = true;
                            break;
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
        warningCounterTV.setText(MessageFormat.format("{0}", warningCounter));
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

    @SuppressLint("NotifyDataSetChanged")
    void makeContractsWarningNotification(List<USER> list) {
        TextView warningCounterTV = findViewById(R.id.warningCounter);
        Calendar now = Calendar.getInstance(Locale.getDefault());
        for (USER u : list) {
            String t = u.ContractExpireDate;
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(t);
                Calendar ExpireDate = Calendar.getInstance();
                if (date != null) {
                    ExpireDate.setTime(date);
                }
                if (now.after(ExpireDate)) {
                    warningCounter++;
                    boolean status = false ;
                    for(WARNING_CLASS ww :WarningList ){
                        if (ww.JobNumber == u.JobNumber && ww.FirstName.equals(u.FirstName) && ww.LastName.equals(u.LastName) && ww.warning.equals("Contract")) {
                            status = true;
                            break;
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
        warningCounterTV.setText(MessageFormat.format("{0}", warningCounter));
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
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_IMMUTABLE);
        }
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
        StringRequest request = new StringRequest(Request.Method.POST, ProjectUrls.registerWorkTimeUrl, response -> {
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
                    attendanceTrigger = true ;
                    MyApp.MyUser.isAttendNow = true;
                    StartWortBtn.setText(MessageFormat.format("started {0}", date));
                    StartWortBtn.setBackgroundResource(R.drawable.mainpage_btns);
                }
                else if (op == 0) {
                    attendanceTrigger = false ;
                    CurrentLocation = null ;
                    MyApp.MyUser.isAttendNow = false;
                    StartWortBtn.setText(getResources().getString(R.string.startworkDay));
                    StartWortBtn.setBackgroundResource(R.drawable.btns);
                }

            }

        }, error -> {
            Log.d("locationService", error.getMessage());
            AttendanceLoadingDialog.close();
            new MESSAGE_DIALOG(act,"Failed",error.toString());
        })
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                String Time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                Log.d("locationService", Date + " "+ Time);
                Map<String,String> par = new HashMap<>();
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
        Q.add(request);
    }

    void getTasks() {
        Q = Volley.newRequestQueue(act);
        StringRequest req = new StringRequest(Request.Method.POST, ProjectUrls.getTasksUrl, response -> {
            Log.d("gettingTasks" , response);
            LinearLayout tasksLayout = (LinearLayout) findViewById(R.id.taskLayout);
            if (response.equals("0")) {
                tasksLayout.setVisibility(View.GONE);
            }
            else {
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
        }, error -> Log.d("gettingTasks" , error.toString()))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Map<String,String> par = new HashMap<>();
                par.put("Salesman", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Date",Date);
                return par;
            }
        };
        Q.add(req);
    }

    void getClient(int ID , int index) {
        final CLIENT_CLASS[] CLIENT = {null};
        StringRequest request = new StringRequest(Request.Method.POST, ProjectUrls.getClientUrl, response -> {
            Log.d("clientResponse" ,response+" "+ID);
            if (!response.equals("0")) {
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject row = arr.getJSONObject(0);
                    CLIENT[0] = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                    TASKs.get(index).setClient(CLIENT[0]);
                    Task_Adapter = new TasksAdapter(TASKs);
                    tasksRecycler.setAdapter(Task_Adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, error -> Log.d("clientResponse" ,error.getMessage()))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {

                Map <String,String> par = new HashMap<>();
                par.put("ID", String.valueOf(ID) );
                return par;
            }
        };
        Q.add(request);
    }

    void getLast5Ads() {
        Log.d("getAdsResp" , "i am started");
        LinearLayout DotsLayout = findViewById(R.id.dotsLayout);
        AdsList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, ProjectUrls.getLastAdsUrl, response -> {
            Log.d("getAdsResp" , response);
            if (!response.equals("0")) {
                try {
                    JSONArray arr = new JSONArray(response) ;
                    for (int i=0;i<arr.length();i++) {
                        JSONObject row = arr.getJSONObject(i);
                        ADS_CLASS a = new ADS_CLASS(row.getInt("id"),row.getString("Title"),row.getString("Message"),row.getString("ImageLink"),row.getString("FileLink"),row.getString("Date"));
                        AdsList.add(a);
                    }
                    if (AdsList.size() == 0 ){
                        LinearLayout l = (LinearLayout)findViewById(R.id.adsLayout);
                        l.setVisibility(View.GONE);
                    }
                    else {
                        LinearLayout l = (LinearLayout)findViewById(R.id.adsLayout);
                        l.setVisibility(View.VISIBLE);
                        Collections.reverse(AdsList);
                        adsAdapter = new Ads_Adapter(AdsList);
                        adsRecycler.setLayoutManager(adManager);
                        AdsRedDot.setVisibility(View.VISIBLE);
                        DotsLayout.removeAllViews();
                        for (int i = 0; i< AdsList.size(); i++) {
                            ImageView dot = new ImageView(act);
                            dot.setImageResource(R.drawable.off_dot);
                            dot.setLayoutParams(new LinearLayout.LayoutParams(20,20));
                            dot.setPadding(0,0,3,0);
                            DotsLayout.addView(dot);
                        }
                        adsRecycler.setAdapter(adsAdapter);
                        setAdsCounter();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                adsAdapter = new Ads_Adapter(AdsList);
                adsRecycler.setLayoutManager(adManager);
                adsRecycler.setAdapter(adsAdapter);
            }

        }
                , error -> Log.d("getAdsResp" , error.toString()));
        Q.add(request) ;
    }

    void setAdsCounter() {
        TextView adsCounterTV = findViewById(R.id.adsCounter);
        MyApp.ADS_Counter = 0 ;
        if (MyApp.ADS_DB.getLastAdId() != -1) {
            if (MyApp.ADS_DB.getLastAdId() < AdsList.get(0).id) {
                Log.d("adsCounterProb", "DB size "+MyApp.ADS_DB.getAds().size()+" last id in DB "+MyApp.ADS_DB.getLastAdId() + " last first id in list " + AdsList.get(0).id);
                MyApp.ADS_Counter = AdsList.get(0).id - MyApp.ADS_DB.getLastAdId();
                adsCounterTV.setText(String.valueOf(MyApp.ADS_Counter));
                int last = MyApp.ADS_Counter - 1 ;
                Log.d("adsCounterProb", "start "+last + " list size " + AdsList.size() + " counter value " + MyApp.ADS_Counter);
                for (int i = last ; i >= 0 ; i--) {
                    Log.d("adsCounterProb", AdsList.get(i).id + " "+i);
                    MyApp.ADS_DB.insertAd(AdsList.get(i).id, AdsList.get(i).Title, AdsList.get(i).Message, AdsList.get(i).ImageLink, AdsList.get(i).FileLink, AdsList.get(i).Date);
                }
                AdsRedDot.setVisibility(View.VISIBLE);
            }
            else {
                Log.d("adsCounterProb", MyApp.ADS_DB.getAds().size()+" "+MyApp.ADS_DB.getLastAdId() + " " + AdsList.get(0).id);
                AdsRedDot.setVisibility(View.GONE);
            }
        }
        else {
            MyApp.ADS_Counter = AdsList.size();
            adsCounterTV.setText(String.valueOf(MyApp.ADS_Counter));
            Log.d("adsCounterProb", "list size "+ AdsList.size() + " counter value " + MyApp.ADS_Counter);
            for (int i = AdsList.size()-1; i >= 0 ; i--) {
                Log.d("adsCounterProb", AdsList.get(i).id+" "+ i);
                MyApp.ADS_DB.insertAd(AdsList.get(i).id, AdsList.get(i).Title, AdsList.get(i).Message, AdsList.get(i).ImageLink, AdsList.get(i).FileLink, AdsList.get(i).Date);
            }

        }
    }

    static void setAdsDots() {
        LinearLayout DotsLayout = act.findViewById(R.id.dotsLayout);
        for (int i=0 ;i< DotsLayout.getChildCount();i++) {
            ImageView img = (ImageView) DotsLayout.getChildAt(i) ;
            img.setImageResource(R.drawable.off_dot);
        }
        ImageView img = (ImageView) DotsLayout.getChildAt(CurrentAd) ;
        img.setImageResource(R.drawable.on_dot);
    }

    public void startWorkGo(View view) {
        if (MyApp.MyUser.isAttendNow) {
            AttendanceLoadingDialog.show();
            registerWorkAttendance(0);
        }
        else {
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
                            (d, id) -> {
                                act.startActivity(new Intent(action));
                                d.dismiss();
                            }).setNegativeButton("Cancel",
                            (d, id) -> {
                                d.dismiss();
                                //startEndWorkDay.setChecked(false);
                            });
                    builder.create().show();
                }
            }
            else {
                Log.d("locationService", "no permission");
                AlertDialog.Builder B = new AlertDialog.Builder(act);
                B.setTitle(getResources().getString(R.string.acceptLocationPermissionTitle));
                B.setMessage(getResources().getString(R.string.acceptLocationPermissionMessage));
                B.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},5);
                });
                B.create();
                B.show();
            }
        }
    }

    void checkMyVacationStatus() {
        MyApp.MyUser.getIsUserInVacationToday(Q, new UserVacationTodayCallback() {
            @Override
            public void onSuccess(boolean result, VACATION_CLASS vacation) {
                Log.d("checkVacation",result+" ");
                if (result) {
                    setIamInVacation(vacation.AlternativeID);
                }
                else {
                    setIamInOutOfVacation();
                }
            }

            @Override
            public void onFil(String error) {
                Log.d("checkVacation",error);
            }
        });
    }

    void setIamInVacation(int alternative) {
        Button workButton = findViewById(R.id.button52);
        workButton.setText(getResources().getString(R.string.iamInVaactionNow));
        workButton.setEnabled(false);
        MyApp.MyUser.VacationStatus = 1;
        StringRequest Req = new StringRequest(Request.Method.POST, ProjectUrls.setMyVacationStatusUrl, response -> Log.d("setIamInVacation" , response)
                , error -> Log.d("setIamInVacation" , error.toString())){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> par = new HashMap<>();
                par.put("jn", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Status","1");
                par.put("alt", String.valueOf(alternative));
                return par;
            }
        };
        Q.add(Req);
    }

    void setIamInOutOfVacation() {
        Button workButton = findViewById(R.id.button52);
        workButton.setEnabled(true);
        MyApp.MyUser.VacationStatus = 0;
        StringRequest Req = new StringRequest(Request.Method.POST, ProjectUrls.setMyVacationStatusUrl, response -> Log.d("setIamInVacation" , response)
                , error -> Log.d("setIamInVacation" , error.toString())){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> par = new HashMap<>();
                par.put("jn", String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("Status","0");
                par.put("alt","0");
                return par;
            }
        };
        Q.add(Req);
    }

    void getIfThereIsNewRating() {
        final int[] counter = {0};
        MyApp.RatingCounter = 0 ;
        if (MyApp.ManagerStatus) {
            if (MyApp.MyUser.MyStaff != null && MyApp.MyUser.MyStaff.size() != 0) {
                StringRequest req = new StringRequest(Request.Method.POST, ProjectUrls.checkRatingAvailability, response -> {
                    Log.d("ratingCounter", response);
                    MyApp.RatingCounter = Integer.parseInt(response);
                    MyApp.HRCounter = MyApp.HRCounter - MyApp.TempRatingCounter ;
                    MyApp.HRCounter = MyApp.HRCounter + MyApp.RatingCounter ;
                    MyApp.TempRatingCounter = MyApp.RatingCounter ;
                    setCounters();
                }, error -> Log.d("ratingCounter", error.toString()))
                {
                    @NonNull
                    @Override
                    protected Map<String, String> getParams() {
                        Calendar ca = Calendar.getInstance(Locale.getDefault());
                        Map<String,String> par = new HashMap<>();
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
            }
            else {
                Log.d("ratingCounter", counter[0] + " my staff false");
            }
        }
        else {
            Log.d("ratingCounter", counter[0] + " manager status false");
        }
    }

    void setUserAttendReminder() {
        Log.d("SetAlarm","start");
        MyApp.MyUser.getUserAttendTime(Q, new AttendTimeCallback() {
            @SuppressLint("UnspecifiedImmutableFlag")
            @Override
            public void onSuccess(AttendLeaveTime al) {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String time = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH)+" "+al.attend;
                @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try {
                    Date date = sdf.parse(time);
                    Calendar alarmCa = Calendar.getInstance();
                    if (date != null) {
                        alarmCa.setTime(date);
                        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent myIntent = new Intent(getApplicationContext(),ReceiveAlarm.class);
                        PendingIntent pendingIntent;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            pendingIntent = PendingIntent.getBroadcast(act, 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
                        }
                        else {
                            pendingIntent = PendingIntent.getBroadcast(act, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        manager.setRepeating(AlarmManager.RTC_WAKEUP,alarmCa.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                        Log.d("SetAlarm","done");
                    }
                } catch (ParseException e) {
                    Log.d("SetAlarm",e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                Log.d("SetAlarm",error);
            }
        });
    }

    void getIsAttendYesterday() {
        MyApp.MyUser.getIsEmployeeHasVacationYesterday(Q, new UserVacationTodayCallback() {
            @Override
            public void onSuccess(boolean result, VACATION_CLASS vacation) {
                if (!result) {
                    MyApp.MyUser.getIsUserAttendYesterday(Q, new getUserAttendance() {
                        @Override
                        public void onResultBack(boolean result) {
                            if (!result) {
                                // show justification dialog
                            }
                            else {

                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }

            @Override
            public void onFil(String error) {

            }
        });

    }

}