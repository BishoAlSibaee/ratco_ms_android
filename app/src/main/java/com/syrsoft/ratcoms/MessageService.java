package com.syrsoft.ratcoms;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.syrsoft.ratcoms.HRActivities.HR_Orders;
import com.syrsoft.ratcoms.HRActivities.MyApprovals;
import com.syrsoft.ratcoms.HRActivities.MySalaryReports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MessageService extends FirebaseMessagingService {

    private String setTokenUrl = MyApp.MainUrl+ "setUserToken.php";
    private Service ser = this ;
    private NotificationManager manager ;
    private int RequestCode ;
    SharedPreferences prefs ;
    private String updateMyDataUrl = MyApp.MainUrl+"getDirectManager.php";

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("com.syrsoft.ratcoms", MODE_PRIVATE);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Random r = new Random();
        RequestCode = r.nextInt();
        if (remoteMessage.getData() != null ) {
            if (MyApp.MyUser == null) {
                updateMyData(new VollyCallback() {
                    @Override
                    public void onSuccess(String s) {
                        if (remoteMessage.getData().get("order") != null) {
                            if (remoteMessage.getData().get("order").equals("AD")) {
                                if (remoteMessage.getData().get("title") != null) {
                                    if (remoteMessage.getData().get("title").equals("SalaryReport")) {
                                        Intent i = new Intent(getApplicationContext(), MySalaryReports.class);
                                        String title = remoteMessage.getData().get("title");
                                        String text = remoteMessage.getData().get("message");
                                        showNotification(getApplicationContext(), title, text, i, RequestCode);
                                    } else if (remoteMessage.getData().get("title").equals("UpdateApplication")) {
                                        Intent i = new Intent(getApplicationContext(), MainPage.class);
                                        String title = remoteMessage.getData().get("title");
                                        String text = remoteMessage.getData().get("Message");
                                        showNotification(getApplicationContext(), title, text, i, RequestCode);
                                    } else {
                                        Intent i = new Intent(getApplicationContext(), MainPage.class);
                                        String title = remoteMessage.getData().get("title");
                                        String text = remoteMessage.getData().get("message");
                                        showNotification(getApplicationContext(), title, text, i, RequestCode);
                                    }
                                }


                            } else if (remoteMessage.getData().get("order").equals("Error")) {
                                if (MyApp.MyUser.JobTitle.equals("Programmer")) {
                                    Intent i = new Intent(getApplicationContext(), Errors.class);
                                    showNotification(getApplicationContext(), "New Error " + remoteMessage.getData().get("title"), " on " + remoteMessage.getData().get("message"), i, RequestCode);
                                }
                            } else if (remoteMessage.getData().get("order").equals("MaintenanceOrder")) {
                                Intent i = new Intent(MyApp.app, MainPage.class);
                                showNotification(MyApp.app, remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), i, RequestCode);
                                //resetCountersService();
                            } else if (remoteMessage.getData().get("order").equals("MaintenanceOrderUpdates")) {
                                Intent i = new Intent(MyApp.app, MainPage.class);
                                showNotification(MyApp.app, remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), i, RequestCode);
                                //resetCountersService();
                            } else if (remoteMessage.getData().get("order").equals("NewSiteVisitOrder")) {
                                if (remoteMessage.getData().get("JobNumber").equals(String.valueOf(MyApp.MyUser.JobNumber))) {
                                    Intent i = new Intent(MyApp.app, MainPage.class);
                                    showNotification(MyApp.app, remoteMessage.getData().get("title"), "updates for your " + remoteMessage.getData().get("message") + " " + remoteMessage.getData().get("Name"), i, RequestCode);
                                } else {
                                    Intent i = new Intent(MyApp.app, MainPage.class);
                                    showNotification(MyApp.app, remoteMessage.getData().get("title"), remoteMessage.getData().get("message") + " " + remoteMessage.getData().get("Name"), i, RequestCode);
                                }

                            } else {
                                if (remoteMessage.getData().get("JobNumber") != null) {
                                    if (remoteMessage.getData().get("JobNumber").equals(String.valueOf(MyApp.MyUser.JobNumber))) {
                                        Intent i = new Intent(MyApp.app, MainPage.class);
                                        showNotification(MyApp.app, "updates on Your " + remoteMessage.getData().get("title"), "updates on Your " + remoteMessage.getData().get("message"), i, RequestCode);
                                    } else {
                                        if (remoteMessage.getData().get("order").contains("Updates")) {
                                            Intent i = new Intent(MyApp.app, MyApprovals.class);
                                            showNotification(MyApp.app, "updates on " + remoteMessage.getData().get("title"), "updates on " + remoteMessage.getData().get("message") + " " + remoteMessage.getData().get("Name"), i, RequestCode);
                                        } else {
                                            Intent i = new Intent(MyApp.app, MyApprovals.class);
                                            showNotification(MyApp.app, remoteMessage.getData().get("title"), remoteMessage.getData().get("message") + remoteMessage.getData().get("Name"), i, RequestCode);
                                        }

                                    }
                                }
                                //resetCountersService();
                            }
                        }
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });
            }
            else {
                if (remoteMessage.getData().get("order") != null) {
                    if (remoteMessage.getData().get("order").equals("AD")) {
                        if (remoteMessage.getData().get("title") != null) {
                            if (remoteMessage.getData().get("title").equals("SalaryReport")) {
                                Intent i = new Intent(getApplicationContext(), MySalaryReports.class);
                                String title = remoteMessage.getData().get("title");
                                String text = remoteMessage.getData().get("message");
                                showNotification(getApplicationContext(), title, text, i, RequestCode);
                            }
                            else if (remoteMessage.getData().get("title").equals("UpdateApplication")) {
                                Intent i = new Intent(getApplicationContext(), MainPage.class);
                                String title = remoteMessage.getData().get("title");
                                String text = remoteMessage.getData().get("Message");
                                showNotification(getApplicationContext(), title, text, i, RequestCode);
                            }
                            else {
                                Intent i = new Intent(getApplicationContext(), MainPage.class);
                                String title = remoteMessage.getData().get("title");
                                String text = remoteMessage.getData().get("message");
                                showNotification(getApplicationContext(), title, text, i, RequestCode);
                            }
                        }


                    }
                    else if (remoteMessage.getData().get("order").equals("Error")) {
                        if (MyApp.MyUser.JobTitle.equals("Programmer")) {
                            Intent i = new Intent(getApplicationContext(), Errors.class);
                            showNotification(getApplicationContext(), "New Error " + remoteMessage.getData().get("title"), " on " + remoteMessage.getData().get("message"), i, RequestCode);
                        }
                    }
                    else if (remoteMessage.getData().get("order").equals("MaintenanceOrder")) {
                        Intent i = new Intent(MyApp.app, MainPage.class);
                        showNotification(MyApp.app, remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), i, RequestCode);
                        //resetCountersService();
                    }
                    else if (remoteMessage.getData().get("order").equals("MaintenanceOrderUpdates")) {
                        Intent i = new Intent(MyApp.app, MainPage.class);
                        showNotification(MyApp.app, remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), i, RequestCode);
                        //resetCountersService();
                    }
                    else if (remoteMessage.getData().get("order").equals("NewSiteVisitOrder")) {
                        if (remoteMessage.getData().get("JobNumber").equals(String.valueOf(MyApp.MyUser.JobNumber))) {
                            Intent i = new Intent(MyApp.app, MainPage.class);
                            showNotification(MyApp.app, remoteMessage.getData().get("title"), "updates for your " + remoteMessage.getData().get("message") + " " + remoteMessage.getData().get("Name"), i, RequestCode);
                        } else {
                            Intent i = new Intent(MyApp.app, MainPage.class);
                            showNotification(MyApp.app, remoteMessage.getData().get("title"), remoteMessage.getData().get("message") + " " + remoteMessage.getData().get("Name"), i, RequestCode);
                        }

                    }
                    else {
                        if (remoteMessage.getData().get("JobNumber") != null) {
                            if (remoteMessage.getData().get("JobNumber").equals(String.valueOf(MyApp.MyUser.JobNumber))) {
                                Intent i = new Intent(MyApp.app, MainPage.class);
                                showNotification(MyApp.app, "updates on Your " + remoteMessage.getData().get("title"), "updates on Your " + remoteMessage.getData().get("message"), i, RequestCode);
                            } else {
                                if (remoteMessage.getData().get("order").contains("Updates")) {
                                    Intent i = new Intent(MyApp.app, MainPage.class);
                                    showNotification(MyApp.app, "updates on " + remoteMessage.getData().get("title"), "updates on " + remoteMessage.getData().get("message") + " " + remoteMessage.getData().get("Name"), i, RequestCode);
                                } else {
                                    Intent i = new Intent(MyApp.app, MainPage.class);
                                    showNotification(MyApp.app, remoteMessage.getData().get("title"), remoteMessage.getData().get("message") + remoteMessage.getData().get("Name"), i, RequestCode);
                                }

                            }
                        }
                        //resetCountersService();
                    }
                }
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        sendTokenToDB(s);
        if (MyApp.MyUser != null ) {
            MyApp.RefME = MyApp.RefUSERS.child(String.valueOf(MyApp.MyUser.JobNumber));
            MyApp.RefME.child("Token").setValue(s);
        }
        else {
            if (prefs.getInt("JobNumber",0) != 0 ) {
                MyApp.RefME = MyApp.RefUSERS.child(String.valueOf(prefs.getInt("JobNumber",0)));
                MyApp.RefME.child("Token").setValue(s);
            }
        }
        MyApp.token = s ;
    }

    private void sendTokenToDB(String token){
        if (MyApp.MyUser != null ) {
            StringRequest request = new StringRequest(Request.Method.POST, setTokenUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("sendingToken", token + " Token Sent");
                    MyApp.RefME.child("token").setValue(token);
                    MyApp.token = token;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> par = new HashMap<String, String>();
                    par.put("id", String.valueOf(MyApp.MyUser.id));
                    par.put("jn", String.valueOf(MyApp.MyUser.JobNumber));
                    par.put("token", token);
                    return par;
                }
            };
            if (MyApp.MyUser != null) {
                Volley.newRequestQueue(ser).add(request);
            }
            if (MyApp.RefME != null) {
                MyApp.RefME.child("token").setValue(token);
            }
            MyApp.token = token;
        }
        else {
            updateMyData(new VollyCallback() {
                @Override
                public void onSuccess(String s) {
                    StringRequest request = new StringRequest(Request.Method.POST, setTokenUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("sendingToken", token + " Token Sent");
                            MyApp.RefME.child("token").setValue(token);
                            MyApp.token = token;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> par = new HashMap<String, String>();
                            par.put("id", String.valueOf(MyApp.MyUser.id));
                            par.put("jn", String.valueOf(MyApp.MyUser.JobNumber));
                            par.put("token", token);
                            return par;
                        }
                    };
                    if (MyApp.MyUser != null) {
                        Volley.newRequestQueue(ser).add(request);
                    }
                    if (MyApp.RefME != null) {
                        MyApp.RefME.child("token").setValue(token);
                    }
                    MyApp.token = token;
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }
    }

    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_IMMUTABLE);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
         manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            manager.createNotificationChannel(mChannel);
        }
        manager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }

    static void resetCountersService() {
        MyApp.HRCounter = 0 ;
        MyApp.MYApprovalsCounter = 0 ;
        MyApp.SiteVisitOrdersCounter = 0 ;
        Intent I = new Intent(MyApp.app, CountersService.class);
        if (CountersService.isRunning) {
            MyApp.app.stopService(I);
            MyApp.app.startService(I);
        }
        else {
            MyApp.app.startService(I);
        }
    }

    private void updateMyData(VollyCallback callback){
        if (prefs.getInt("JobNumber",0) > 0) {
            StringRequest request = new StringRequest(Request.Method.POST, updateMyDataUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    List<Object> list = JsonToObject.translate(response, USER.class, null);
                    Log.d("LoginResponse", list.size() + "");
                    try {
                        USER THE_USER = (USER) list.get(0);
                        MyApp.MyUser = THE_USER ;
                        MyApp.db.updateUser(THE_USER.id, THE_USER.JobNumber, THE_USER.User, THE_USER.FirstName, THE_USER.LastName, THE_USER.Department, THE_USER.JobTitle, THE_USER.DirectManager, THE_USER.WorkLocationLa, THE_USER.WorkLocationLo, THE_USER.Mobile, THE_USER.Email, THE_USER.Pic, THE_USER.IDNumber, THE_USER.IDExpireDate, THE_USER.BirthDate, THE_USER.Nationality, THE_USER.PassportNumber, THE_USER.PassportExpireDate, THE_USER.ContractNumber, THE_USER.ContractStartDate, THE_USER.ContractDuration, THE_USER.ContractExpireDate, THE_USER.InsuranceExpireDate, THE_USER.Bank, THE_USER.BankAccount, THE_USER.BankIban, THE_USER.IDsWarningNotifications, THE_USER.PASSPORTsWarningNotification, THE_USER.CONTRACTsWarningNotification, THE_USER.Salary, THE_USER.VacationDays, THE_USER.SickDays, THE_USER.EmergencyDays, THE_USER.JoinDate);
                        //MyApp.RefME.setValue(MyApp.MyUser);
                        callback.onSuccess(THE_USER.FirstName+" "+THE_USER.LastName);
                    } catch (Exception e) {
                        // ToastMaker.Show(1,e.getMessage(),act);
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> par = new HashMap<String, String>();
                    par.put("jn", String.valueOf(prefs.getInt("JobNumber",0)));
                    return par;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }
        else {
            callback.onFailed("no jobNumber");
        }
    }

}
