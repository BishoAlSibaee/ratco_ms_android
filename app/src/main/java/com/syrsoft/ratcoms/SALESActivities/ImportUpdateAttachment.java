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

import java.util.HashMap;
import java.util.Map;

public class ImportUpdateAttachment {
    public int id;
    public int UpdateId;
    public String Link;

    public ImportUpdateAttachment(int id, int updateId, String link) {
        this.id = id;
        UpdateId = updateId;
        Link = link;
    }
}
