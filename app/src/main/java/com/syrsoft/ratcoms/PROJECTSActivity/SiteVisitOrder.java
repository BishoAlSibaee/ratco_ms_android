package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.ShowVisitsOnMap;
import com.syrsoft.ratcoms.USER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteVisitOrder extends AppCompatActivity {

    Activity act ;
    RequestQueue Q ;
    int ID ;
    String getSiteVisitOrderUrl = MyApp.MainUrl+"getSiteVisitOrderByID.php";
    String getOrderLinksUrl = MyApp.MainUrl+"getSiteVisitOrderLinks.php";
    List<SITE_VISIT_ORDER_class> list ;
    TextView  DoneBy,date , projectName , responsibleName , responsibleMobile ,reason, notes , visitDate , visitTime , visitResult , visitNotes , visitDoneDate ;
    SITE_VISIT_ORDER_class VISIT ;
    LinearLayout responseLayout , picsLayout ;
    List<String> Links ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_site_visit_order_activity);
        ID = getIntent().getExtras().getInt("ID");
        setActivity();
        getOrder();
        getOrderLinks();
    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        list = new ArrayList<SITE_VISIT_ORDER_class>();
        date = (TextView) findViewById(R.id.SiteVisitOrder_date);
        projectName = (TextView) findViewById(R.id.SiteVisitOrder_pname);
        responsibleName = (TextView) findViewById(R.id.SiteVisitOrder_Responsible);
        responsibleMobile = (TextView) findViewById(R.id.SiteVisitOrder_ResponsibleMobile);
        notes = (TextView) findViewById(R.id.SiteVisitOrder_notes);
        reason = (TextView) findViewById(R.id.SiteVisitOrder_reason);
        visitDate = (TextView) findViewById(R.id.VisitDateTV);
        visitTime = (TextView) findViewById(R.id.VisitTimeTV);
        visitResult = (TextView) findViewById(R.id.SiteVisitOrder_visitResult);
        visitNotes = (TextView) findViewById(R.id.SiteVisitOrder_visitNotes);
        visitDoneDate = (TextView) findViewById(R.id.SiteVisitOrder_visitDateTime);
        DoneBy = findViewById(R.id.SiteVisitOrder_DoneBy);
        responseLayout = (LinearLayout) findViewById(R.id.responseLayout);
        Links = new ArrayList<String>();
        picsLayout = (LinearLayout) findViewById(R.id.Pics_Layout);
    }

    void getOrder() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getSiteVisitOrderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject row = arr.getJSONObject(0);
                    VISIT = new SITE_VISIT_ORDER_class(row.getInt("id"),row.getInt("SalesMan"),row.getInt("ForwardedTo"),row.getString("VisitReason"),row.getString("ProjectName"),row.getString("ResponsibleName"),row.getString("ResponsibleMobile"),row.getDouble("LA"),row.getDouble("LO"),row.getString("Notes"),row.getString("Date"),row.getString("VisitDate"),row.getString("VisitTime"),row.getString("VisitResult"),row.getString("VisitNotes"),row.getString("DoneDate"),row.getString("DoneTime"),row.getInt("Status"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                USER f = USER.searchUserByJobNumber(MyApp.EMPS,VISIT.ForwardedTo);
                if (f!=null) {
                    DoneBy.setText(f.FirstName+" "+f.LastName);
                }
                date.setText(VISIT.Date);
                projectName.setText(VISIT.ProjectName);
                responsibleName.setText(VISIT.ResponsibleName);
                responsibleMobile.setText(VISIT.ResponsibleMobile);
                notes.setText(VISIT.Notes);
                reason.setText(VISIT.VisitReason);
                visitDate.setText(VISIT.VisitDate);
                visitTime.setText(VISIT.VisitTime);
                visitResult.setText(VISIT.VisitResult);
                visitNotes.setText(VISIT.VisitNotes);
                visitDoneDate.setText(VISIT.DoneDate+"  "+VISIT.DoneTime);
                if (VISIT.VisitResult.isEmpty()) {
                    responseLayout.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("ID" , String.valueOf(ID));
                return par;
            }
        };
        Q.add(request);
    }

    void getOrderLinks() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getOrderLinksUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("linksResponse" , response);
                    if (response.equals("0")) {

                    }
                    else {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0;i<arr.length();i++) {
                                JSONObject row = arr.getJSONObject(i);
                                Links.add(row.getString("Link"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (Links.size() > 0 ) {
                            for (int i=0; i<Links.size();i++) {
                                View v = LayoutInflater.from(act).inflate(R.layout.images_to_save_unit,null,false);
                                ImageView image = (ImageView) v.findViewById(R.id.imagesToSave_image);
                                int finalI = i;
                                image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Dialog D = new Dialog(act);
                                        D.setContentView(R.layout.view_zoomable_image);
                                        Window window = D.getWindow();
                                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                        PhotoView image = (PhotoView)D.findViewById(R.id.photo_view);
                                        ImageButton x = (ImageButton) D.findViewById(R.id.close);
                                        x.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                D.dismiss();
                                            }
                                        });
                                        Picasso.get().load(Links.get(finalI)).into(image);
                                        D.show();
                                    }
                                });
                                Picasso.get().load(Links.get(i)).into(image);
                                picsLayout.addView(v);
                            }
                        }
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    l.close();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("ID" , String.valueOf(ID));
                return par;
            }
        };
        Q.add(request);
    }

    public void showLocationOnMap(View view) {

        Intent i = new Intent(act, ShowVisitsOnMap.class);
        i.putExtra("LA",VISIT.LA);
        i.putExtra("LO" , VISIT.LO);
        i.putExtra("ClientName" , VISIT.ProjectName);
        startActivity(i);
    }
}