package com.syrsoft.ratcoms.SALESActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.DataSheet_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataSheets extends AppCompatActivity {

    Activity act;
    RequestQueue Q;
    List<DataSheet_Class> SheetsList;
    List<DataSheet_Class> filter;
    String getDataSheetUrl = MyApp.MainUrl + "getDataSheet.php";
    RecyclerView SheetsRecycler;
    EditText SearchField;
    DataSheet_Adapter Adapter;
    RecyclerView.LayoutManager Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_data_sheets_activity);
        setActivity();
        setActivityActions();
        searchDataSheet();
    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        SheetsList = new ArrayList<DataSheet_Class>();
        filter = new ArrayList<DataSheet_Class>();
        SearchField = (EditText) findViewById(R.id.editTextTextPersonName3);
        Manager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        SheetsRecycler = (RecyclerView) findViewById(R.id.itemsRecycler);
        SheetsRecycler.setLayoutManager(Manager);
    }

    void setActivityActions() {
        SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter.clear();
                for (DataSheet_Class sheet : SheetsList) {
                    if (sheet.FileName.toLowerCase().contains(s.toString())) {
                        filter.add(sheet);
                    }
                }
                Adapter = new DataSheet_Adapter(filter);
                SheetsRecycler.setAdapter(Adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    void searchDataSheet() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getDataSheetUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {
                    Log.d("ResponseDataSheet", response);
                } else {
                    Log.d("ResponseDataSheet", response);
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            SheetsList.add(new DataSheet_Class(row.getInt("id"), row.getString("FileName"), row.getString("Link")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Adapter = new DataSheet_Adapter(SheetsList);
                    SheetsRecycler.setAdapter(Adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        });
        Q.add(request);
    }
}