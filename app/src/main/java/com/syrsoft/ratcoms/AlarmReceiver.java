package com.syrsoft.ratcoms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.syrsoft.ratcoms.HRActivities.ManageIds;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager manager ;
    private int ReqCode ;
    private Random r = new Random();
    private String VacationDaysModifierUrl = "https://ratco-solutions.com/RatcoManagementSystem/modifyVacationDays.php" ;
    @Override
    public void onReceive(Context context, Intent intent) {

//        Log.d("receivedInReceiver" , "received");
//
//        if (intent.getExtras().get("order").toString().equals("ID")){
//            //Intent i = new Intent(context, WarningNotification.class);
//            //context.startService(i);
//            //showNotification(context,intent.getExtras().get("title").toString(),"ID Expire Warning "+intent.getExtras().get("date").toString(),i,ReqCode);
//            final String[] Token = {""};
//            MyApp.RefME.child("token").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.getValue()!=null)
//                    {
//                        Token[0] = snapshot.getValue().toString();
//                        MyApp.CloudMessage("ID Warning " ,"ID Expiry Warning For "+intent.getExtras().getString("title"),intent.getExtras().getString("title"),MyApp.db.getUser().JobNumber,Token[0],"IDWarning",context);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        else if (intent.getExtras().get("order").toString().equals("VacationDays")){
//            Log.d("VacationDaysModify" , "I Received");
//            StringRequest request = new StringRequest(Request.Method.POST, VacationDaysModifierUrl, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.d("VacationDaysModify" , "ModifiedForToday "+response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });
//            Volley.newRequestQueue(context).add(request);
//        }


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
}
