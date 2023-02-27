package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.CLIENT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.CONTRACT_ITEMS_CLASS;
import com.syrsoft.ratcoms.SALESActivities.ClientVisitReport;
import com.syrsoft.ratcoms.SALESActivities.PROJECT_CONTRACT_CLASS;
import com.syrsoft.ratcoms.SALESActivities.PaymentTerms;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.Contracts_Adapter;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.Items_Adapter;
import com.syrsoft.ratcoms.SALESActivities.ShowVisitsOnMap;
import com.syrsoft.ratcoms.SALESActivities.TermsAndConditions;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AddMaintenanceOrder extends AppCompatActivity {

    Activity act;
    TextView ProjectName, ClientName, Warranty, WarrantyDate, ProjectDescTV, ProjectResponsibleTV, ResponsibleMobileTV, ContractDateTV, SupplyTV, InstallTV, HandoverTV;
    TextView DeliveryLocationTV, AvailabilityTV, InstallationTV, WarrantyTV, Payment1TV, Payment1TVtext, Payment2TV, Payment2TVtext, Payment3TV, Payment3TVtext, Payment4TV, Payment4TVtext;
    EditText DamageDescET, NotesET, ContactNumberET, ContactNameET;
    LinearLayout FilesLayout, DamageLayout;
    CheckBox MaintenanceCB, AluminumCB, DoorsCB;
    String[] SearchByArr;
    PROJECT_CONTRACT_CLASS CONTRACT;
    CLIENT_CLASS CLIENT;
    TermsAndConditions TERMS;
    List<CONTRACT_ITEMS_CLASS> ITEMS;
    String searchProjectUrl = MyApp.MainUrl + "searchProject.php";
    String getContractTermsURL = MyApp.MainUrl + "getContractTermsAndConditions.php";
    String getContractItemsURL = MyApp.MainUrl + "getContractItems.php";
    String getClientUrl = MyApp.MainUrl + "getClient.php";
    String saveMaintenanceOrderUrl = MyApp.MainUrl + "insertMaintenanceOrder.php";
    RequestQueue Q;
    List<PROJECT_CONTRACT_CLASS> ContractsResult;
    Dialog D;
    int ATTACHFILE_REQCODE = 20;
    int CAM_REQCODE = 21;
    List<Bitmap> FILES;
    RecyclerView ItemsRecycler;
    RecyclerView.LayoutManager Manager;
    Items_Adapter items_adapter;
    List<USER> RespectiveUsers;
    String City;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_add_maintenance_order_activity);
        setActivity();
        setActivityAction();
        D.show();
    }

    void setActivity() {
        act = this;
        ProjectName = (TextView) findViewById(R.id.AddNewProject_ClientName);
        ClientName = (TextView) findViewById(R.id.AddNewProject_ClientNamee);
        DamageDescET = (EditText) findViewById(R.id.AddMaintenanceOrder_damageDesc);
        NotesET = (EditText) findViewById(R.id.AddMaintenanceOrder_notes);
        ContactNameET = (EditText) findViewById(R.id.AddMaintenanceOrder_contactName);
        FilesLayout = (LinearLayout) findViewById(R.id.Files_layout);
        DamageLayout = (LinearLayout) findViewById(R.id.addMaintenanceOrder_DamageLayout);
        DamageLayout.setVisibility(View.GONE);
        Warranty = (TextView) findViewById(R.id.AddNewProject_warranty);
        WarrantyDate = (TextView) findViewById(R.id.AddNewProject_warrantyDate);
        ProjectDescTV = (TextView) findViewById(R.id.Contract_ProjectDesc);
        ContractDateTV = (TextView) findViewById(R.id.Contract_ContractDate);
        SupplyTV = (TextView) findViewById(R.id.supply_text);
        InstallTV = (TextView) findViewById(R.id.install_text);
        HandoverTV = (TextView) findViewById(R.id.handover_text);
        ProjectResponsibleTV = (TextView) findViewById(R.id.Contract_ProjectResponsible);
        ResponsibleMobileTV = (TextView) findViewById(R.id.Contract_ProjectResponsibleMobile);
        DeliveryLocationTV = (TextView) findViewById(R.id.addCLient_deliveryLocationTV);
        AvailabilityTV = (TextView) findViewById(R.id.Contract_avaliabilityTV);
        Payment1TV = (TextView) findViewById(R.id.Contract_paymentED1);
        Payment1TVtext = (TextView) findViewById(R.id.Contract_paymentED1text);
        Payment2TV = (TextView) findViewById(R.id.Contract_paymentED2);
        Payment2TVtext = (TextView) findViewById(R.id.Contract_paymentED2text);
        Payment3TV = (TextView) findViewById(R.id.Contract_paymentED3);
        Payment3TVtext = (TextView) findViewById(R.id.Contract_paymentED3text);
        Payment4TV = (TextView) findViewById(R.id.Client_paymentED4);
        Payment4TVtext = (TextView) findViewById(R.id.Contract_paymentED4text);
        InstallationTV = (TextView) findViewById(R.id.Contract_installationET);
        WarrantyTV = (TextView) findViewById(R.id.Contract_warrantyET);
        ContactNumberET = (EditText) findViewById(R.id.AddMaintenanceOrder_contactNumber);
        MaintenanceCB = (CheckBox) findViewById(R.id.addMaintenance_maintenanceCB);
        AluminumCB = (CheckBox) findViewById(R.id.addMaintenance_aluminumCB);
        DoorsCB = (CheckBox) findViewById(R.id.addMaintenance_doorsCB);
        ItemsRecycler = (RecyclerView) findViewById(R.id.Items_Recycler);
        Manager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        ItemsRecycler.setLayoutManager(Manager);
        Q = Volley.newRequestQueue(act);
        SearchByArr = getResources().getStringArray(R.array.searchProjectByArray);
        ContractsResult = new ArrayList<PROJECT_CONTRACT_CLASS>();
        ITEMS = new ArrayList<CONTRACT_ITEMS_CLASS>();
        FILES = new ArrayList<Bitmap>();
        RespectiveUsers = new ArrayList<USER>();
        for (USER u : MyApp.EMPS) {
            if (u.JobTitle.equals("Sales Manager")) {
                RespectiveUsers.add(u);
            }
            if (u.JobNumber == MyApp.db.getUser().DirectManager) {
                if (!u.JobTitle.equals("Manager") && !u.JobTitle.equals("Sales Manager")) {
                    RespectiveUsers.add(u);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Random r = new Random();
        int x = r.nextInt(10000);
        if (requestCode == ATTACHFILE_REQCODE) {

            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    FILES.add(bmp);
                    View v = LayoutInflater.from(act).inflate(R.layout.images_to_save_unit, null, false);
                    ImageView image = (ImageView) v.findViewById(R.id.imagesToSave_image);
                    image.setImageBitmap(bmp);
                    FilesLayout.addView(v);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.d("path" , "error result "+data.getData().getPath());
            }

        } else if (requestCode == CAM_REQCODE) {
            //x = r.nextInt(10000);
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                FILES.add(imageBitmap);
                View v = LayoutInflater.from(act).inflate(R.layout.images_to_save_unit, null, false);
                ImageView image = (ImageView) v.findViewById(R.id.imagesToSave_image);
                image.setImageBitmap(imageBitmap);
                FilesLayout.addView(v);
            } else {
                ToastMaker.Show(1, "error getting Image", act);
            }
        }

    }

    void setActivityAction() {
        D = new Dialog(act);
        D.setContentView(R.layout.project_search_project_dialog);
        Window w = D.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        D.setCancelable(false);
        ArrayAdapter<String> SearchByAdapter = new ArrayAdapter<String>(act, R.layout.spinner_item, SearchByArr);
        Spinner SearchBySpinner = (Spinner) D.findViewById(R.id.SearchProject_searchBySpinner);
        SearchBySpinner.setAdapter(SearchByAdapter);
        Spinner ResultSpinner = (Spinner) D.findViewById(R.id.SearchProject_searchResultSpinner);
        TextView SearchField = (TextView) D.findViewById(R.id.SearchProject_searchWord);
        ProgressBar P = (ProgressBar) D.findViewById(R.id.progressBar3);
        P.setVisibility(View.GONE);
        SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (SearchField.getText() != null && !SearchField.getText().toString().isEmpty()) {
                    ContractsResult.clear();
                    String[] resArr = new String[0];
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, R.layout.spinner_item, resArr);
                    ResultSpinner.setAdapter(adapter);
                    if (SearchBySpinner.getSelectedItem() != null && !SearchBySpinner.getSelectedItem().toString().isEmpty()) {
                        P.setVisibility(View.VISIBLE);
                        StringRequest request = new StringRequest(Request.Method.POST, searchProjectUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("searchProjectResp", response);
                                P.setVisibility(View.GONE);
                                if (response.equals("0")) {

                                } else if (response.equals("-1")) {

                                } else {
                                    ContractsResult.clear();
                                    try {
                                        JSONArray arr = new JSONArray(response);
                                        String[] resArr = new String[arr.length()];
                                        for (int i = 0; i < arr.length(); i++) {
                                            JSONObject row = arr.getJSONObject(i);
                                            ContractsResult.add(new PROJECT_CONTRACT_CLASS(row.getInt("id"), row.getInt("ClientID"), row.getString("ProjectName"), row.getString("Date"), row.getString("City"), row.getString("Address"), row.getDouble("LA"), row.getDouble("LO"), row.getString("ProjectDescription"), row.getString("ProjectManager"), row.getString("MobileNumber"), row.getInt("SalesMan"), row.getString("HandOverDate"), row.getString("WarrantyExpireDate"), row.getString("ContractLink"), row.getInt("Supplied"), row.getInt("Installed"), row.getInt("Handovered"), row.getString("SupplyDate"), row.getString("InstallDate"), row.getString("HandOverDate")));
                                            resArr[i] = row.getString("ProjectName");
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, R.layout.spinner_item, resArr);
                                        ResultSpinner.setAdapter(adapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("searchProjectResp", e.getMessage());
                                    }

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                P.setVisibility(View.GONE);
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> par = new HashMap<String, String>();
                                par.put("searchBy", String.valueOf(SearchBySpinner.getSelectedItemPosition()));
                                par.put("Field", SearchField.getText().toString());
                                par.put("SalesMan", String.valueOf(MyApp.db.getUser().JobNumber));
                                return par;
                            }
                        };
                        Q.add(request);
                    } else {

                    }
                } else {
                    ContractsResult.clear();
                    String[] resArr = new String[0];
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, R.layout.spinner_item, resArr);
                    ResultSpinner.setAdapter(adapter);
                }
            }
        });
        Button Cancel = (Button) D.findViewById(R.id.SearchProject_cancelBtn);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
            }
        });
        Button Select = (Button) D.findViewById(R.id.SearchProject_selectBtn);
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ResultSpinner.getSelectedItem() != null) {
                    ProjectName.setText(ResultSpinner.getSelectedItem().toString());
                    CONTRACT = ContractsResult.get(ResultSpinner.getSelectedItemPosition());
                    Log.d("warrantyEDate", CONTRACT.WarrantyExpireDate + " " + CONTRACT.ContractLink);
                    ProjectDescTV.setText(CONTRACT.ProjectDescription);
                    ProjectResponsibleTV.setText(CONTRACT.ProjectManager);
                    ResponsibleMobileTV.setText(CONTRACT.MobileNumber);
                    ContractDateTV.setText(CONTRACT.Date);
                    SupplyTV.setText(CONTRACT.getSupplied());
                    InstallTV.setText(CONTRACT.getInstalled());
                    HandoverTV.setText(CONTRACT.getHandovered());
                    Warranty.setText(CONTRACT.getWarranty());
                    WarrantyDate.setText(CONTRACT.WarrantyExpireDate);
                    DamageLayout.setVisibility(View.VISIBLE);
                    getContractTerms();
                    getContractItems();
                    getClient(CONTRACT.ClientID);
                    D.dismiss();
                    if (CONTRACT != null) {
                        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
                        if (CONTRACT.LA != null && CONTRACT.LO != null) {
                            try {
                                List<Address> addresses = geocoder.getFromLocation(CONTRACT.LA, CONTRACT.LO, 1);
                                City = addresses.get(0).getLocality();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    ToastMaker.Show(0, "please select project contract", act);
                }
            }
        });
        ProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                D = new Dialog(act);
//                D.setContentView(R.layout.project_search_project_dialog);
//                Window w = D.getWindow();
//                w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                D.setCancelable(false);
//                ArrayAdapter<String> SearchByAdapter = new ArrayAdapter<String>(act,R.layout.spinner_item,SearchByArr);
//                Spinner SearchBySpinner = (Spinner) D.findViewById(R.id.SearchProject_searchBySpinner);
//                SearchBySpinner.setAdapter(SearchByAdapter);
//                Spinner ResultSpinner = (Spinner) D.findViewById(R.id.SearchProject_searchResultSpinner);
//                TextView SearchField = (TextView) D.findViewById(R.id.SearchProject_searchWord);
//                ProgressBar P = (ProgressBar) D.findViewById(R.id.progressBar3);
//                P.setVisibility(View.GONE);
//                SearchField.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                        if (SearchField.getText() != null && !SearchField.getText().toString().isEmpty()) {
//                            if (SearchBySpinner.getSelectedItem() != null && !SearchBySpinner.getSelectedItem().toString().isEmpty()) {
//                                P.setVisibility(View.VISIBLE);
//                                StringRequest request = new StringRequest(Request.Method.POST, searchProjectUrl, new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        Log.d("searchProjectResp" , response);
//                                        P.setVisibility(View.GONE);
//                                        if (response.equals("0")) {
//
//                                        }
//                                        else if (response.equals("-1")) {
//
//                                        }
//                                        else {
//                                            ContractsResult.clear();
//                                            try {
//                                                JSONArray arr = new JSONArray(response);
//                                                String[] resArr = new String[arr.length()];
//                                                for (int i=0;i<arr.length();i++) {
//                                                    JSONObject row = arr.getJSONObject(i);
//                                                    ContractsResult.add(new PROJECT_CONTRACT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ProjectName"),row.getString("Date"),row.getString("City"),row.getString("Address"),row.getDouble("LA"),row.getDouble("LO"),row.getString("ProjectDescription"),row.getString("ProjectManager"),row.getString("MobileNumber"),row.getInt("SalesMan"),row.getString("HandOverDate"),row.getString("WarrantyExpireDate"),row.getString("ContractLink"),row.getInt("Supplied"),row.getInt("Installed"),row.getInt("Handovered"),row.getString("SupplyDate"),row.getString("InstallDate"),row.getString("HandOverDate")));
//                                                    resArr[i] = row.getString("ProjectName");
//                                                }
//                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resArr);
//                                                ResultSpinner.setAdapter(adapter);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                                Log.d("searchProjectResp" , e.getMessage());
//                                            }
//
//                                        }
//                                    }
//                                }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//                                        P.setVisibility(View.GONE);
//                                    }
//                                })
//                                {
//                                    @Nullable
//                                    @Override
//                                    protected Map<String, String> getParams() throws AuthFailureError {
//                                        Map<String,String> par = new HashMap<String, String>();
//                                        par.put("searchBy" , String.valueOf(SearchBySpinner.getSelectedItemPosition()));
//                                        par.put("Field" , SearchField.getText().toString());
//                                        par.put("SalesMan", String.valueOf(MyApp.db.getUser().JobNumber));
//                                        return par;
//                                    }
//                                };
//                                Q.add(request);
//                            }
//                            else {
//
//                            }
//                        }
//                        else {
//
//                        }
//                    }
//                });
//                Button Cancel = (Button) D.findViewById(R.id.SearchProject_cancelBtn);
//                Cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        D.dismiss();
//                    }
//                });
//                Button Select = (Button) D.findViewById(R.id.SearchProject_selectBtn);
//                Select.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (ResultSpinner.getSelectedItem() != null ) {
//                            ProjectName.setText(ResultSpinner.getSelectedItem().toString());
//                            CONTRACT = ContractsResult.get(ResultSpinner.getSelectedItemPosition()) ;
//                            ProjectDescTV.setText(CONTRACT.ProjectDescription);
//                            ProjectResponsibleTV.setText(CONTRACT.ProjectManager);
//                            ResponsibleMobileTV.setText(CONTRACT.MobileNumber);
//                            ContractDateTV.setText(CONTRACT.Date);
//                            SupplyTV.setText(CONTRACT.getSupplied());
//                            InstallTV.setText(CONTRACT.getInstalled());
//                            HandoverTV.setText(CONTRACT.getHandovered());
//                            Warranty.setText(CONTRACT.getWarranty());
//                            WarrantyDate.setText(CONTRACT.WarrantyExpireDate);
//                            DamageLayout.setVisibility(View.VISIBLE);
//                            getContractTerms();
//                            getContractItems();
//                            getClient(CONTRACT.ClientID);
//                            D.dismiss();
//                        }
//                        else {
//                            ToastMaker.Show(0,"please select project contract",act);
//                        }
//                    }
//                });
                D.show();
                D.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //Log.d("contractInformation" , CONTRACT.getCLIENT().ClientName+" "+CONTRACT.getTerms().getDeliveryLocation()+" "+CONTRACT.getItems().size());
                    }
                });
            }
        });
    }

    void getContractTerms() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getContractTermsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getTermsResp", response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(0, "No Terms", act);
                } else if (response.equals("-1")) {

                } else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        TermsAndConditions T = new TermsAndConditions();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            if (row.getString("Name").equals("DeliveryLocation")) {
                                T.setDeliveryLocation(row.getString("Term"));
                            } else if (row.getString("Name").equals("Availability")) {
                                T.setAvailability(row.getString("Term"));
                            } else if (row.getString("Name").equals("Installation")) {
                                T.setInstallation(row.getString("Term"));
                            } else if (row.getString("Name").equals("Warranty")) {
                                T.setWarranty(row.getString("Term"));
                            } else if (row.getString("Name").equals("Payment")) {
                                String TT = row.getString("Term");
                                String arrT[] = TT.split("-");
                                List<PaymentTerms> paymentTerms = new ArrayList<PaymentTerms>();
                                if (arrT.length > 0) {
                                    Log.d("paymentarrlength", arrT.length + "");
                                    for (String s : arrT) {
                                        Log.d("paymentarrlength", s);
                                        String[] XX = s.split("%");
                                        if (XX.length == 2) {
                                            paymentTerms.add(new PaymentTerms(XX[0], XX[1]));
                                        }
                                    }
                                }
                                T.setPayment(paymentTerms);
                            }
                        }
                        TERMS = T;
                        CONTRACT.setTerms(TERMS);
//                        if (CONTRACT.getItems() != null && CLIENT != null ) {
//                            D.dismiss();
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (TERMS != null) {
                        if (TERMS.getDeliveryLocation() != null && !TERMS.getDeliveryLocation().isEmpty()) {
                            DeliveryLocationTV.setText(TERMS.getDeliveryLocation());
                        }
                        if (TERMS.getAvailability() != null && !TERMS.getAvailability().isEmpty()) {
                            AvailabilityTV.setText(TERMS.getAvailability());
                        }
                        if (TERMS.getInstallation() != null && !TERMS.getInstallation().isEmpty()) {
                            InstallationTV.setText(TERMS.getInstallation());
                        }
                        if (TERMS.getWarranty() != null && !TERMS.getWarranty().isEmpty()) {
                            WarrantyTV.setText(TERMS.getWarranty());
                        }
                        if (TERMS.getPayment() != null && TERMS.getPayment().size() > 0) {
                            Payment1TV.setText(TERMS.getPayment().get(0).Percent);
                            Payment1TVtext.setText(TERMS.getPayment().get(0).Condition);
                            if (TERMS.getPayment().size() > 1) {
                                Payment2TV.setText(TERMS.getPayment().get(1).Percent);
                                Payment2TVtext.setText(TERMS.getPayment().get(1).Condition);
                            }
                            if (TERMS.getPayment().size() > 2) {
                                Payment3TV.setText(TERMS.getPayment().get(2).Percent);
                                Payment3TVtext.setText(TERMS.getPayment().get(2).Condition);
                            }
                            if (TERMS.getPayment().size() > 3) {
                                Payment4TV.setText(TERMS.getPayment().get(3).Percent);
                                Payment4TVtext.setText(TERMS.getPayment().get(3).Condition);
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("getTermsResp", error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("ContractID", String.valueOf(CONTRACT.id));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getContractItems() {

        Loading l = new Loading(act);
        l.show();
        ITEMS.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getContractItemsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1, "No Items", act);
                } else if (response.equals("-1")) {
                    ToastMaker.Show(1, "error getting Items", act);
                } else {
                    //ItemsLayout.setVisibility(View.VISIBLE);
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ITEMS.add(new CONTRACT_ITEMS_CLASS(row.getInt("id"), row.getInt("ProjectID"), row.getString("ItemName"), row.getInt("Quantity"), row.getDouble("Price")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CONTRACT.setItems(ITEMS);
                    items_adapter = new Items_Adapter(ITEMS);
                    ItemsRecycler.setAdapter(items_adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("ContractID", String.valueOf(CONTRACT.id));
                return par;
            }
        };
        Q.add(request);
    }

    void getClient(int ID) {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getClientUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xxxxxx", response);
                l.close();
                if (response.equals("0")) {
                    Log.d("xxxxxx", "0");
                } else {
                    try {
                        JSONArray arr = new JSONArray(response);
                        JSONObject row = arr.getJSONObject(0);
                        CLIENT = new CLIENT_CLASS(row.getInt("id"), row.getString("ClientName"), row.getString("City"), row.getString("PhonNumber"), row.getString("Address"), row.getString("Email"), row.getInt("SalesMan"), row.getDouble("LA"), row.getDouble("LO"), row.getString("FieldOfWork"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xxxxxx", e.getMessage());
                    }
                    CONTRACT.setCLIENT(CLIENT);
                    ClientName.setText(CONTRACT.getCLIENT().ClientName);
//                    if (CONTRACT.getTerms() != null && CONTRACT.getItems() != null ) {
//                        D.dismiss();
//                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("xxxxxx", error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> par = new HashMap<String, String>();
                par.put("ID", String.valueOf(ID));
                return par;
            }
        };
        Q.add(request);
    }

    public void attachFile(View view) {
        if (CONTRACT == null) {
            ToastMaker.Show(0, getResources().getString(R.string.pleaseSelectProjectFirst), act);
        } else {
            AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
            selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
            selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
            selectDialog.setNegativeButton(getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, ClientVisitReport.CAM_PERMISSION_REQCODE);
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        try {
                            act.startActivityForResult(takePictureIntent, CAM_REQCODE);
                        } catch (ActivityNotFoundException e) {
                            // display error state to the user
                        }

                    }
                }
            });
            selectDialog.setPositiveButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                    open.setType("image/*");
                    act.startActivityForResult(Intent.createChooser(open, "select Image"), ATTACHFILE_REQCODE);
                }
            });
            selectDialog.create().show();
        }
    }

    public void viewContractFile(View view) {
        if (CONTRACT.ContractLink == null || CONTRACT.ContractLink.equals("0") || CONTRACT.ContractLink.isEmpty()) {
            ToastMaker.Show(1, "No File Attached", act);
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CONTRACT.ContractLink));
            startActivity(browserIntent);
        }

    }

    public void showOnMap(View view) {
        if (CONTRACT.LA != 0 && CONTRACT.LO != 0) {
            Intent i = new Intent(act, ShowVisitsOnMap.class);
            i.putExtra("LA", CONTRACT.LA);
            i.putExtra("LO", CONTRACT.LO);
            i.putExtra("ClientName", CONTRACT.ProjectName);
            startActivity(i);
        }
    }

    public void sendMaintenanceOrder(View view) {

        if (CONTRACT == null) {
            ToastMaker.Show(0, "select Contract", act);
            ProjectName.setHintTextColor(Color.RED);
            return;
        }
        if (DamageDescET.getText() == null || DamageDescET.getText().toString().isEmpty()) {
            ToastMaker.Show(0, "enter Damage Description", act);
            DamageDescET.setHint("enter Damage Description");
            DamageDescET.setHintTextColor(Color.RED);
            return;
        }
        if (ContactNumberET.getText() == null || ContactNumberET.getText().toString().isEmpty()) {
            ToastMaker.Show(0, "enter contact number", act);
            ContactNumberET.setHint("enter contact number");
            ContactNumberET.setHintTextColor(Color.RED);
            return;
        } else {
            if (ContactNumberET.getText().toString().length() < 10) {
                ToastMaker.Show(1, "Contact Number is less than 10 numbers", act);
                ContactNumberET.setTextColor(Color.RED);
                return;
            } else {
                ContactNumberET.setTextColor(Color.WHITE);
            }
        }
        if (ContactNameET.getText() == null || ContactNameET.getText().toString().isEmpty()) {
            ToastMaker.Show(0, "enter contact name", act);
            ContactNumberET.setHint("enter contact name");
            ContactNumberET.setHintTextColor(Color.RED);
            return;
        }
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, saveMaintenanceOrderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("saveOrderResp", response);
                if (response.equals("0")) {
                    new MESSAGE_DIALOG(act, "Error .. ", "Try again later");
                } else if (response.equals("-1")) {
                    new MESSAGE_DIALOG(act, "Error", "No Parameters");
                } else if (Integer.parseInt(response) > 0) {
                    int ID = Integer.parseInt(response);
                    for (Bitmap b : FILES) {
                        MyApp.savePhoto(b, "MaintenanceOrderLinks", ID, "MaintenanceID");
                    }
                    for (USER u : MyApp.EMPS) {
                        if (MaintenanceCB.isChecked()) {
                            if (u.JobTitle.equals("Project Manager")) {
                                RespectiveUsers.add(u);
                            }
                        }
                        if (AluminumCB.isChecked()) {
                            if (u.JobTitle.equals("Factory Manager") && u.Department.equals("Aluminum Factory")) {
                                RespectiveUsers.add(u);
                            }
                        }
                        if (DoorsCB.isChecked()) {
                            if (u.JobNumber == 50007) {
                                RespectiveUsers.add(u);
                            }
                        }
                    }
                    MyApp.sendNotificationsToGroup(RespectiveUsers, getResources().getString(R.string.maintenanceOrder), getResources().getString(R.string.maintenanceOrder) + " " + CONTRACT.ProjectName, MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "MaintenanceOrder", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, getResources().getString(R.string.saved), getResources().getString(R.string.saved), 0);
                        }
                    });
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                new MESSAGE_DIALOG(act, "error", "error " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
                Map<String, String> par = new HashMap<String, String>();
                par.put("ProjectID", String.valueOf(CONTRACT.id));
                par.put("ProjectName", CONTRACT.ProjectName);
                par.put("ClientID", String.valueOf(CONTRACT.ClientID));
                par.put("LA", String.valueOf(CONTRACT.LA));
                par.put("LO", String.valueOf(CONTRACT.LO));
                par.put("DamageDesc", DamageDescET.getText().toString());
                par.put("ContactName", ContactNameET.getText().toString());
                par.put("SalesMan", String.valueOf(CONTRACT.SalesMan));
                par.put("Date", Date);
                if (NotesET.getText() != null && !NotesET.getText().toString().isEmpty()) {
                    par.put("Notes", NotesET.getText().toString());
                }
                par.put("Contact", ContactNumberET.getText().toString());
                if (MaintenanceCB.isChecked()) {
                    par.put("ToMaintenance", "1");
                }
                if (AluminumCB.isChecked()) {
                    par.put("ToAluminum", "1");
                }
                if (DoorsCB.isChecked()) {
                    par.put("ToDoors", "1");
                }
                return par;
            }
        };
        Q.add(request);
    }

}