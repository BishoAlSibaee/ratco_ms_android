package com.syrsoft.ratcoms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.VACATION_CLASS;
import com.syrsoft.ratcoms.Interfaces.UserVacationTodayCallback;
import com.syrsoft.ratcoms.Interfaces.getUserAttendance;

import java.util.Calendar;
import java.util.Locale;

public class ReceiveAlarm extends BroadcastReceiver {

    int attendNotificationReqCode = 35;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceive","received");
        USERDataBase db = new USERDataBase(context);
        USER u = db.getUser();
        Calendar c = Calendar.getInstance(Locale.getDefault());
        RequestQueue Q =  Volley.newRequestQueue(context);
        boolean isFriday = c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ;
        Log.d("AlarmReceive","is friday "+isFriday);
        if (!isFriday) {
            u.getIsUserAttendToday(Q, new getUserAttendance() {
                @Override
                public void onResultBack(boolean result) {
                    Log.d("AlarmReceive","attend today result "+result);
                    if (!result) {
                        u.getIsUserInVacationToday(Q, new UserVacationTodayCallback() {
                            @Override
                            public void onSuccess(boolean result, VACATION_CLASS vacation) {
                                Log.d("AlarmReceive","vacation result "+result);
                                if (!result) {
                                    showNotification(context,u.FirstName+".. "+context.getResources().getString(R.string.attendAlarmTitle),context.getResources().getString(R.string.attendAlarmMessage),attendNotificationReqCode);
                                }
                            }

                            @Override
                            public void onFil(String error) {

                            }
                        });
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    public void showNotification(Context context, String title, String message, int reqCode) {

        Intent i = new Intent(context,Login.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(context, reqCode, i, PendingIntent.FLAG_IMMUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(context, reqCode, i,PendingIntent.FLAG_UPDATE_CURRENT);
        }
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.small_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.attend_notification_sound))
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
