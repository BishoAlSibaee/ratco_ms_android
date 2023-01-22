package com.syrsoft.ratcoms.SALESActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
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
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.DataSheet_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalogs extends AppCompatActivity {

    Activity act ;
    RequestQueue Q ;
    List<Catalog_Class> CatalogsList;
    String searchCatalogsUrl = MyApp.MainUrl+"searchCatalog.php";
    RecyclerView CatalogsRecycler;
    EditText SearchField ;
    Catalog_Adapter Adapter ;
    RecyclerView.LayoutManager Manager ;
    boolean status = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_catalogs_activity);
        setActivity();
        setActivityActions();
    }

    void setActivity() {
        act = this;
        Q = Volley.newRequestQueue(act);
        CatalogsList = new ArrayList<Catalog_Class>();
        SearchField = (EditText) findViewById(R.id.editTextTextPersonName3);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
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

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (SearchField.getText().toString().isEmpty()) {
                    CatalogsList.clear();
                    Adapter = new Catalog_Adapter(CatalogsList);
                    CatalogsRecycler.setAdapter(Adapter);
                }
                else {
                    status = true ;
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                if (status) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            searchDataSheet();
                                        }
                                    });

                                    status = false ;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                }

            }
        });
    }

    void searchDataSheet() {
        CatalogsList.clear();
        Adapter = new Catalog_Adapter(CatalogsList);
        CatalogsRecycler.setAdapter(Adapter);
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, searchCatalogsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {

                }
                else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            CatalogsList.add(new Catalog_Class(row.getInt("id"),row.getString("FileName"),row.getString("Link")));
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Field" , SearchField.getText().toString()) ;
                return par;
            }
        };
        Q.add(request);
    }
}