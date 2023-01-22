package com.syrsoft.ratcoms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ADS_Activity extends AppCompatActivity {

    Activity act ;
    List<ADS_CLASS> list ;
    Ads_Adapter Adapter ;
    RecyclerView.LayoutManager Manager ;
    RecyclerView ADS ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_d_s_activity);
        setActivity();
    }

    void setActivity() {
        act = this ;
        list = new ArrayList<ADS_CLASS>();
        list = MyApp.ADS_DB.getAds() ;
        Collections.reverse(list);
        Adapter = new Ads_Adapter(list);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        ADS = (RecyclerView) findViewById(R.id.ADS_Recycler);
        ADS.setLayoutManager(Manager);
        ADS.setAdapter(Adapter);
    }
}