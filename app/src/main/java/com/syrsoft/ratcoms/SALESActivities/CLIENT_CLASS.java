package com.syrsoft.ratcoms.SALESActivities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIENT_CLASS {

    public int id ;
    public String ClientName ;
    public String City ;
    public String PhonNumber ;
    public String Address ;
    public String Email ;
    public int SalesMan ;
    public double  LA ;
    public double LO ;
    public String FieldOfWork ;
    public List<RESPONSIBLE_CLASS> Responsibles ;

    public CLIENT_CLASS(int id, String clientName, String city, String phonNumber, String address, String email, int salesMan, double LA, double LO, String fieldOfWork) {
        this.id = id;
        ClientName = clientName;
        City = city;
        PhonNumber = phonNumber;
        Address = address;
        Email = email;
        SalesMan = salesMan;
        this.LA = LA;
        this.LO = LO;
        FieldOfWork = fieldOfWork;
    }

    public void setResponsibles (List<RESPONSIBLE_CLASS> Responsibles){
        this.Responsibles = Responsibles ;
    }

    public static void getClient(Context c, int ID, ClientCallback callback){
        final CLIENT_CLASS[] CLIENT = new CLIENT_CLASS[1];
        StringRequest request = new StringRequest(Request.Method.POST, MyApp.MainUrl+"getClient.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("0")) {
                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        CLIENT[0] = new CLIENT_CLASS(row.getInt("id"),row.getString("ClientName"),row.getString("City"),row.getString("PhonNumber"),row.getString("Address"),row.getString("Email"),row.getInt("SalesMan"),row.getDouble("LA"),row.getDouble("LO"),row.getString("FieldOfWork"));
                        callback.onSuccess(CLIENT[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("clientResponse" ,error.getMessage());
                callback.onFailed();
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
        Volley.newRequestQueue(c).add(request);
    }

    public List<RESPONSIBLE_CLASS> getResponsibles() {
        return Responsibles ;
    }
}

interface ClientCallback {
    void onSuccess(CLIENT_CLASS client);
    void onFailed();
}