package com.syrsoft.ratcoms.SALESActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.Catalog_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Catalogs extends AppCompatActivity {

    Activity act;
    RequestQueue Q;
    List<Catalog_Class> CatalogsList;
    List<Catalog_Class> filter;

    String getCatalogsUrl = MyApp.MainUrl + "getCatalog.php";
    RecyclerView CatalogsRecycler;
    EditText SearchField;
    Catalog_Adapter Adapter;
    RecyclerView.LayoutManager Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_catalogs_activity);
        setActivity();
        setActivityActions();
        getDataSheet();
    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        CatalogsList = new ArrayList<Catalog_Class>();
        filter = new ArrayList<Catalog_Class>();
        SearchField = (EditText) findViewById(R.id.editTextTextPersonName3);
        Manager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        CatalogsRecycler = (RecyclerView) findViewById(R.id.itemsRecycler);
        CatalogsRecycler.setLayoutManager(Manager);
    }

    void setActivityActions() {
        SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter.clear();
                for (Catalog_Class catalog : CatalogsList) {
                    if (catalog.FileName.contains(s.toString())) {
                        filter.add(catalog);
                    }
                }
                Adapter = new Catalog_Adapter(filter);
                CatalogsRecycler.setAdapter(Adapter);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    void getDataSheet() {
//        CatalogsList.clear();
//        Adapter = new Catalog_Adapter(CatalogsList);
//        CatalogsRecycler.setAdapter(Adapter);
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getCatalogsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {

                } else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CatalogsList.add(new Catalog_Class(row.getInt("id"), row.getString("FileName"), row.getString("Link")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Adapter = new Catalog_Adapter(CatalogsList);
                    CatalogsRecycler.setAdapter(Adapter);
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