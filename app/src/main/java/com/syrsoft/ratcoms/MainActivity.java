package com.syrsoft.ratcoms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean isConnected = false ;
    ConnectivityManager connectivityManager ;
    Activity act;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivity();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void setActivity()
    {
        act = this ;
        MyApp.ActList.add(act);
        //MyApp.db = new USERDataBase(act);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        checkConnectivity() ;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void checkConnectivity ()
    {

        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback()
        {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                //Toast.makeText(getApplicationContext(),"Internet Is On",Toast.LENGTH_LONG).show();
                isConnected = true ;
                Log.d("Connectivity" , String.valueOf(isConnected));
                redirectActivity();
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                //Toast.makeText(getApplicationContext(),"no network",Toast.LENGTH_LONG).show();
                isConnected = false ;
                Log.d("Connectivity" , String.valueOf(isConnected));
                act.finish();
                ToastMaker.Show(1,"No Internet Connection" ,act);
            }

            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                //Toast.makeText(getApplicationContext(),"network losing",Toast.LENGTH_LONG).show();
                isConnected = false ;
                Log.d("Connectivity" , String.valueOf(isConnected));
                act.finish();
                ToastMaker.Show(1,"No Internet Connection" ,act);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                //Toast.makeText(getApplicationContext(), "network lost", Toast.LENGTH_LONG).show();
                isConnected = false ;
                Log.d("Connectivity" , String.valueOf(isConnected));
                act.finish();
                ToastMaker.Show(1,"No Internet Connection" ,act);
            }
        } );
    }

    void redirectActivity(){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if (MyApp.db.isLoggedIn()){
                        Intent i = new Intent();
                    }
                    else {
                        Intent i = new Intent(act,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    ToastMaker.Show(1,e.getMessage(),act);
                }
            }
        });
        t.start();
    }
}