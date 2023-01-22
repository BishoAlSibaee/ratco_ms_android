package com.syrsoft.ratcoms.SALESActivities;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.Search_Item_Adapter;
import com.syrsoft.ratcoms.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search_Item_Dialog {

    Context C ;
    Dialog D ;
    RadioButton byModel , byName ;
    EditText SearchWord ;
    public static RecyclerView ItemsRes ;
    public static TextView SelectedTV ;
    Button Cancel , Select ;
    LinearLayoutManager Manager ;
    public static Search_Item_Adapter Adapter ;
    String SearchUrl = MyApp.MainUrl+"searchItemForContract.php" ;
    List<DBItem> ResList ;
    String By = "M" ;
    ProgressBar P ;
    public static DBItem SelectedItem ;




    public Search_Item_Dialog (Context C) {
        this.C = C ;
        D = new Dialog(C);
        D.setContentView(R.layout.search_item);
        Window w = D.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        setSearchDialog();
    }

    void setSearchDialog() {
        ResList = new ArrayList<>();
        byModel = D.findViewById(R.id.radioButton7);
        byName = D.findViewById(R.id.radioButton6);
        SearchWord = D.findViewById(R.id.editTextTextPersonName11);
        ItemsRes = D.findViewById(R.id.Res_items);
        SelectedTV = D.findViewById(R.id.textView1144);
        Cancel = D.findViewById(R.id.button57);
        Select = D.findViewById(R.id.button56);
        P = D.findViewById(R.id.progressBar7);
        P.setVisibility(View.GONE);
        Manager = new LinearLayoutManager(C,RecyclerView.VERTICAL,false);
        ItemsRes.setLayoutManager(Manager);
        byModel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    byName.setChecked(false);
                    By = "M" ;
                }
            }
        });
        byName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    byModel.setChecked(false);
                    By = "N" ;
                }
            }
        });
        SearchWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                P.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (SearchWord.getText() != null) {
                    getResult(SearchWord.getText().toString());
                }
                else {
                    P.setVisibility(View.GONE);
                    ToastMaker.Show(0,C.getResources().getString(R.string.enterSearchWord),C);
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
            }
        });
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView ItemName = AddNewProjectContract.ItemsList.get(AddNewProjectContract.ItemsList.size()-1).findViewById(R.id.itemUnit_name);
                ItemName.setText(SelectedItem.Model+" "+SelectedItem.ItemNameArabic);
                D.dismiss();
            }
        });
    }

    void getResult (String word) {
        if (word != null && !word.isEmpty()) {
            StringRequest req = new StringRequest(Request.Method.POST, SearchUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    P.setVisibility(View.GONE);
                    Log.d("searchRes" , response);
                    if (!response.equals("0")) {
                        Log.d("searchRes" , "not 0 ");
                        try {
                            JSONArray arr = new JSONArray(response);
                            ResList.clear();
                            for (int i = 0 ; i < arr.length();i++) {
                                JSONObject row = arr.getJSONObject(i);
                                ResList.add(new DBItem(row.getInt("id"),row.getInt("StoreId"),row.getString("ItemCode"),row.getString("Model"),row.getString("ItemName"),row.getString("ItemNameArabic"),row.getString("ItemDesc"),row.getInt("ManufacturId"),row.getString("Manufactur"),row.getInt("SupplierId"),row.getString("Supplier"),row.getInt("ItemSource")
                                ,row.getInt("Quantity"),row.getInt("ReservedQuantity"),row.getInt("RequestedQuantity"),row.getDouble("Price"),row.getDouble("MinPrice"),row.getString("Pic"),row.getString("Unit")));
                            }
                            Log.d("searchRes" , ResList.size()+" ");
                            Adapter = new Search_Item_Adapter(ResList);
                            ItemsRes.setAdapter(Adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("searchRes" , e.toString());
                        }
                    }
                    else {
                        Log.d("searchRes" , " 0 ");
                        ResList.clear();
                        Adapter = new Search_Item_Adapter(ResList);
                        ItemsRes.setAdapter(Adapter);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> par = new HashMap<String, String>();
                    par.put("Word",word);
                    par.put("By",By);
                    return par;
                }
            };
            Volley.newRequestQueue(C).add(req);
        }
        else {
            P.setVisibility(View.GONE);
            ToastMaker.Show(0,C.getResources().getString(R.string.enterSearchWord),C);
        }

    }

    void show () {
        D.show();
    }

}
