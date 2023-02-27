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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PURCHASE_UPDATE_CLASS {
    public int id ;
    public int OrderId ;
    public int UserId ;
    public String UpdateDate ;
    public String Type ;
    public String Notes ;
    public List<ImportUpdateAttachment> Attachments ;


    public PURCHASE_UPDATE_CLASS(int id, int orderId, int userId, String updateDate, String type, String notes) {
        this.id = id;
        OrderId = orderId;
        UserId = userId;
        UpdateDate = updateDate;
        Type = type;
        Notes = notes;
    }

}
