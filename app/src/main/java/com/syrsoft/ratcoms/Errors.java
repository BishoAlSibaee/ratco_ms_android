package com.syrsoft.ratcoms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Errors extends AppCompatActivity {

    RecyclerView Errors ;
    List<ERROR_CLASS> list ;
    Activity act ;
    String getErrorsUrl = MyApp.MainUrl+"getErrors.php" ;
    Error_Adapter Adapter ;
    RecyclerView.LayoutManager Manager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errors_activity);
        setActivity();
        getErrors();
    }

    void setActivity() {
        act = this ;
        Errors = (RecyclerView) findViewById(R.id.errorsRecycler);
        list = new ArrayList<ERROR_CLASS>();
        Adapter = new Error_Adapter(list);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        Errors.setLayoutManager(Manager);
    }

    void getErrors() {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getErrorsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("errorResponse",response);
                l.close();

                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Errors" ,act);
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ERROR_CLASS E = new ERROR_CLASS(row.getInt("id"),row.getString("ErrorText"),row.getString("Activity"),row.getString("User"),row.getString("MethodName"),row.getString("Date"),row.getString("Time"));
                            list.add(E);
                        }
                        Collections.reverse(list);
                        Adapter = new Error_Adapter(list);
                        Errors.setAdapter(Adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorResponse",error.getMessage());
                l.close();
            }
        });
        Volley.newRequestQueue(act).add(request);
    }
}