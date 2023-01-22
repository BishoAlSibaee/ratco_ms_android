package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.GetInsertResultCallback;
import com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE;
import com.syrsoft.ratcoms.HRActivities.JobTitle;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.ProgressLoadingDialog;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.PROJECT_CONTRACT_CLASS;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VollyCallback;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddPurchaseOrder extends AppCompatActivity {

    Activity act ;
    int indicatorImage ;
    int FileReqCode = 1 , CAM_REQCODE = 2 , IMG_REQCODE = 3 , CAM_PERMISSION_CODE = 20;
    ImageView projectIndicator,quotationsIndicator ;
    TextView ProjectNameTV ;
    EditText notesET ;
    Dialog D,SupplierD,AddSupplierD ;
    String[] SearchByArr ;
    String searchProjectUrl = MyApp.MainUrl + "searchProject.php" ;
    String searchSupplier = MyApp.MainUrl + "searchSupplier.php" ;
    String insertNewSupplier = MyApp.MainUrl + "insertNewSupplier.php" ;
    String insertPurchaseOrderUrl = MyApp.MainUrl + "insertPurchaseOrder.php" ;
    String insertPurchaseOrderQuotationUrl = MyApp.MainUrl + "insertPurchaseOrderQuotation.php";
    String insertPurchaseOrderQuotationFileUrl = MyApp.MainUrl + "insertPurchaseOrderQuotationFile.php";
    //String getAuthEmps
    List<PROJECT_CONTRACT_CLASS> ContractsResult ;
    PROJECT_CONTRACT_CLASS CONTRACT ;
    List<QUOTATION_CLASS> Quotations ;
    List<SUPPLIER_CLASS> SuppliersListSearch;
    SUPPLIER_CLASS SUPPLIER ;
    LinearLayout QuotationsLayout ;
    Uri UriFile;
    List<List<String>> FILE_NAMES;
    List<List<Object>> FILES ;
    List<LinearLayout> FILES_LAYOUTS ;
    int QuotationIndex ;
    List<USER> RespectiveUsers ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_add_purchase_order_activity);
        setActivity();
        getAcceptEmployees();
        setActivityActions();
        setTheIndicatorImage();
        setIndicators();
        createInstructionsDialog();
    }

    void setActivity() {
        act = this ;
        projectIndicator = (ImageView) findViewById(R.id.imageView25);
        quotationsIndicator = (ImageView) findViewById(R.id.imageView26);
        ProjectNameTV = (TextView) findViewById(R.id.AddNewProject_ClientName);
        SearchByArr = getResources().getStringArray(R.array.searchProjectByArray);
        ContractsResult = new ArrayList<PROJECT_CONTRACT_CLASS>();
        Quotations = new ArrayList<QUOTATION_CLASS>();
        SuppliersListSearch = new ArrayList<SUPPLIER_CLASS>();
        FILE_NAMES = new ArrayList<List<String>>();
        FILES = new ArrayList<List<Object>>();
        FILES_LAYOUTS = new ArrayList<LinearLayout>();
        QuotationsLayout = (LinearLayout) findViewById(R.id.quotationsLayout);
        notesET  = (EditText) findViewById(R.id.editTextTextMultiLine);
        RespectiveUsers = new ArrayList<>();
    }

    void setActivityActions () {
        createSearchProjectDialog();
        ProjectNameTV.setOnClickListener(new View.OnClickListener() {
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

    void getAcceptEmployees () {
        if (MyApp.DIRECT_MANAGER != null) {
            RespectiveUsers.add(MyApp.DIRECT_MANAGER);
        }
        if (MyApp.DEPARTMENT_MANAGER != null ) {
            if (MyApp.DIRECT_MANAGER != MyApp.DEPARTMENT_MANAGER) {
                RespectiveUsers.add(MyApp.DEPARTMENT_MANAGER);
            }
        }
        HR_ORDER_TYPE myType = null ;
        if (MyApp.Types != null && MyApp.Types.size() > 0 ) {
            for (int i=0;i<MyApp.Types.size();i++) {
                if (MyApp.Types.get(i).HROrderName.equals("Local Purchase Order")) {
                    myType = MyApp.Types.get(i);
                    break;
                }
            }
            List<JobTitle> titles = new ArrayList<>();
            if (myType != null) {
                if (myType.Auth1 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth1 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth2 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth2 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth3 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth3 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth4 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth4 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth5 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth5 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth6 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth6 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth7 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth7 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth8 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth8 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth9 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth9 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
                if (myType.Auth10 != 0) {
                    for (int i = 0;i<MyApp.JobTitles.size();i++) {
                        if (myType.Auth10 == MyApp.JobTitles.get(i).id) {
                            titles.add(MyApp.JobTitles.get(i));
                            break;
                        }
                    }
                }
            }
            if (titles.size() > 0 ) {
                for (int i=0;i<titles.size();i++) {
                    if (USER.searchUserByJobtitle(MyApp.EMPS,titles.get(i).JobTitle) != null) {
                        RespectiveUsers.add(USER.searchUserByJobtitle(MyApp.EMPS,titles.get(i).JobTitle));
                    }
                }
            }
        }
    }

    void createInstructionsDialog () {
        AlertDialog.Builder D = new AlertDialog.Builder(act);
        D.setTitle(getResources().getString(R.string.instructions))
                .setMessage(getResources().getString(R.string.addPurchaseOrderInstructions))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        D.create().show();
    }

    void setTheIndicatorImage() {
        //Log.d("currentLanguage" , Locale.getDefault().getDisplayLanguage());
        //Toast.makeText(act,Locale.getDefault().getDisplayLanguage(),Toast.LENGTH_LONG).show();
    if (Locale.getDefault().getLanguage().equals("en")) {
            indicatorImage = R.drawable.current_item_arrow_en;
        }
        else if (Locale.getDefault().getLanguage().equals("ar")) {
            indicatorImage = R.drawable.current_item_arrow_ar;
        }
        projectIndicator.setImageResource(indicatorImage);
        quotationsIndicator.setImageResource(indicatorImage);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        projectIndicator.startAnimation(animation);
        quotationsIndicator.startAnimation(animation);
    }

    void setIndicators() {
        if (CONTRACT == null ) {
            projectIndicator.setAlpha(1.0f);
            quotationsIndicator.setAlpha(0.0f);
            //Toast.makeText(act,"contract is null" ,Toast.LENGTH_LONG).show();
        }
        else if (CONTRACT != null && Quotations != null ) {
            projectIndicator.setAlpha(0.0f);
            quotationsIndicator.setAlpha(1.0f);
            //Toast.makeText(act,"contract is not null" ,Toast.LENGTH_LONG).show();
        }
    }

    void createSearchProjectDialog() {
        D = new Dialog(act);
        D.setContentView(R.layout.project_search_project_dialog);
        Window w = D.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        D.setCancelable(false);
        ArrayAdapter<String> SearchByAdapter = new ArrayAdapter<String>(act,R.layout.spinner_item,SearchByArr);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resArr);
                    ResultSpinner.setAdapter(adapter);
                    if (SearchBySpinner.getSelectedItem() != null && !SearchBySpinner.getSelectedItem().toString().isEmpty()) {
                        P.setVisibility(View.VISIBLE);
                        StringRequest request = new StringRequest(Request.Method.POST, searchProjectUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("searchProjectResp" , response);
                                P.setVisibility(View.GONE);
                                if (response.equals("0")) {

                                }
                                else if (response.equals("-1")) {

                                }
                                else {
                                    ContractsResult.clear();
                                    try {
                                        JSONArray arr = new JSONArray(response);
                                        String[] resArr = new String[arr.length()];
                                        for (int i=0;i<arr.length();i++) {
                                            JSONObject row = arr.getJSONObject(i);
                                            ContractsResult.add(new PROJECT_CONTRACT_CLASS(row.getInt("id"),row.getInt("ClientID"),row.getString("ProjectName"),row.getString("Date"),row.getString("City"),row.getString("Address"),row.getDouble("LA"),row.getDouble("LO"),row.getString("ProjectDescription"),row.getString("ProjectManager"),row.getString("MobileNumber"),row.getInt("SalesMan"),row.getString("HandOverDate"),row.getString("WarrantyExpireDate"),row.getString("ContractLink"),row.getInt("Supplied"),row.getInt("Installed"),row.getInt("Handovered"),row.getString("SupplyDate"),row.getString("InstallDate"),row.getString("HandOverDate")));
                                            resArr[i] = row.getString("ProjectName");
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resArr);
                                        ResultSpinner.setAdapter(adapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("searchProjectResp" , e.getMessage());
                                    }

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                P.setVisibility(View.GONE);
                            }
                        })
                        {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> par = new HashMap<String, String>();
                                par.put("searchBy" , String.valueOf(SearchBySpinner.getSelectedItemPosition()));
                                par.put("Field" , SearchField.getText().toString());
                                return par;
                            }
                        };
                        Volley.newRequestQueue(act).add(request);
                    }
                    else {

                    }
                }
                else {
                    ContractsResult.clear();
                    String[] resArr = new String[0];
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resArr);
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
                if (ResultSpinner.getSelectedItem() != null ) {
                    ProjectNameTV.setText(ResultSpinner.getSelectedItem().toString());
                    ProjectNameTV.setCompoundDrawables(null,null,null,null);
                    D.dismiss();
                    CONTRACT = ContractsResult.get(ResultSpinner.getSelectedItemPosition()) ;
                    setIndicators();
                    //ProjectDescTV.setText(CONTRACT.ProjectDescription);
                    //ProjectResponsibleTV.setText(CONTRACT.ProjectManager);
                    //ResponsibleMobileTV.setText(CONTRACT.MobileNumber);
                    //ContractDateTV.setText(CONTRACT.Date);
                    //SupplyTV.setText(CONTRACT.getSupplied());
                    //InstallTV.setText(CONTRACT.getInstalled());
                    //HandoverTV.setText(CONTRACT.getHandovered());
                    //Warranty.setText(CONTRACT.getWarranty());
                    //WarrantyDate.setText(CONTRACT.WarrantyExpireDate);
                    //DamageLayout.setVisibility(View.VISIBLE);
                    //getContractTerms();
                    //getContractItems();
                    //getClient(CONTRACT.ClientID);

//                    if (CONTRACT != null) {
//                        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
//                        if (CONTRACT.LA != null && CONTRACT.LO != null) {
//                            try {
//                                List<Address> addresses = geocoder.getFromLocation(CONTRACT.LA, CONTRACT.LO, 1);
//                                City = addresses.get(0).getLocality();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
                }
                else {
                    ToastMaker.Show(0,"please select project contract",act);
                }
            }
        });
    }

    void createSearchSupplierDialog() {
        SupplierD = new Dialog(act);
        SupplierD.setContentView(R.layout.dialog_search_supplier);
        Window w = SupplierD.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        SupplierD.setCancelable(false);
        ArrayAdapter<String> SearchByAdapter = new ArrayAdapter<String>(act,R.layout.spinner_item,getResources().getStringArray(R.array.searchSupplierByArray));
        Spinner SearchBySpinner = (Spinner) SupplierD.findViewById(R.id.SearchSupplier_searchBySpinner);
        SearchBySpinner.setAdapter(SearchByAdapter);
        Spinner ResultSpinner = (Spinner) SupplierD.findViewById(R.id.SearchSupplier_searchResultSpinner);
        TextView SearchField = (TextView) SupplierD.findViewById(R.id.SearchSupplier_searchWord);
        ProgressBar P = (ProgressBar) SupplierD.findViewById(R.id.progressBar3);
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
                    SuppliersListSearch.clear();
                    String[] resArr = new String[0];
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resArr);
                    ResultSpinner.setAdapter(adapter);
                    if (SearchBySpinner.getSelectedItem() != null && !SearchBySpinner.getSelectedItem().toString().isEmpty()) {
                        P.setVisibility(View.VISIBLE);
                        StringRequest request = new StringRequest(Request.Method.POST, searchSupplier, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("searchSupplierRes" , response);
                                P.setVisibility(View.GONE);
                                if (response.equals("0")) {
                                    Log.d("searchSupplierRes" , "response is 0");
                                }
                                else if (response.equals("-1")) {
                                    Log.d("searchSupplierRes" , "response is -1");
                                }
                                else {
                                    SuppliersListSearch.clear();
                                    try {
                                        JSONArray arr = new JSONArray(response);
                                        String[] resArr = new String[arr.length()];
                                        for (int i=0; i < arr.length() ; i++ ) {
                                            JSONObject row = arr.getJSONObject(i);
                                            SuppliersListSearch.add(new SUPPLIER_CLASS(row.getInt("id"),row.getString("Name"),row.getString("Address"),row.getString("Phone"),row.getString("Contact"),row.getString("ContactMobile"),row.getString("Email"),row.getString("IBAN")));
                                            resArr[i] = row.getString("Name");
                                            Log.d("searchSupplierRes" , resArr[i]);
                                        }
                                        Log.d("searchSupplierRes" , "items are "+resArr.length+"");
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resArr);
                                        ResultSpinner.setAdapter(adapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("searchProjectResp" , e.getMessage());
                                    }

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                P.setVisibility(View.GONE);
                            }
                        })
                        {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> par = new HashMap<String, String>();
                                par.put("searchBy" , String.valueOf(SearchBySpinner.getSelectedItemPosition()));
                                par.put("Field" , SearchField.getText().toString());
                                return par;
                            }
                        };
                        Volley.newRequestQueue(act).add(request);
                    }
                    else {

                    }
                }
                else {
                    ContractsResult.clear();
                    String[] resArr = new String[0];
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,resArr);
                    ResultSpinner.setAdapter(adapter);
                }
            }
        });
        Button Cancel = (Button) SupplierD.findViewById(R.id.SearchSupplier_cancelBtn);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplierD.dismiss();
            }
        });
        Button Select = (Button) SupplierD.findViewById(R.id.SearchSupplier_selectBtn);
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ResultSpinner.getSelectedItem() != null ) {
                    SupplierD.dismiss();
                    SUPPLIER = SuppliersListSearch.get(ResultSpinner.getSelectedItemPosition());
                    addQuotationModuleWithSupplierName(SUPPLIER.Name,SUPPLIER);
                    createAddFilesDialog();
                }
                else {
                    ToastMaker.Show(0,"please select project contract",act);
                }
            }
        });
        SupplierD.show();
    }

    void createAddNewSupplierDialog() {
        AddSupplierD = new Dialog(act);
        AddSupplierD.setContentView(R.layout.dialog_add_supplier);
        Window w = AddSupplierD.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        AddSupplierD.setCancelable(false);
        EditText name = AddSupplierD.findViewById(R.id.editTextTextPersonName4);
        EditText address = AddSupplierD.findViewById(R.id.editTextTextPersonName5);
        EditText phone = AddSupplierD.findViewById(R.id.editTextTextPersonName6);
        EditText emp = AddSupplierD.findViewById(R.id.editTextTextPersonName7);
        EditText mobile = AddSupplierD.findViewById(R.id.editTextTextPersonName8);
        EditText email = AddSupplierD.findViewById(R.id.editTextTextPersonName9);
        EditText iban = AddSupplierD.findViewById(R.id.editTextTextPersonName10);
        ProgressBar p = AddSupplierD.findViewById(R.id.progressBar5);
        p.setVisibility(View.GONE);
        Button Cancel = (Button) AddSupplierD.findViewById(R.id.button42);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSupplierD.dismiss();
            }
        });
        Button Select = (Button) AddSupplierD.findViewById(R.id.button41);
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText() == null || name.getText().toString().isEmpty() || name.getText().toString().equals("") ) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Supplier Name","enter supplier name");
                }
                else {
                    p.setVisibility(View.VISIBLE);
                    StringRequest req = new StringRequest(Request.Method.POST, insertNewSupplier, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            p.setVisibility(View.GONE);
                            if (response.equals("0")) {
                                Toast.makeText(act,"not Saved",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(act,"Saved",Toast.LENGTH_SHORT).show();
                                AddSupplierD.dismiss();
                                try {
                                    JSONArray arr = new JSONArray(response);
                                    JSONObject row = arr.getJSONObject(0);
                                    SUPPLIER_CLASS sup = new SUPPLIER_CLASS(row.getInt("id"),row.getString("Name"),row.getString("Address"),row.getString("Phone"),row.getString("Contact"),row.getString("ContactMobile"),row.getString("Email"),row.getString("IBAN"));
                                    SUPPLIER = sup;
                                    addQuotationModuleWithSupplierName(SUPPLIER.Name,SUPPLIER);
                                    createAddFilesDialog();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            p.setVisibility(View.GONE);
                        }
                    })
                    {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> par = new HashMap<String,String>();
                            par.put("Name" ,name.getText().toString());
                            if (address.getText() != null && !address.getText().toString().isEmpty()) {
                                par.put("Address",address.getText().toString());
                            }
                            if (phone.getText() != null && !phone.getText().toString().isEmpty()) {
                                par.put("Phone",phone.getText().toString());
                            }
                            if (emp.getText() != null && !emp.getText().toString().isEmpty()) {
                                par.put("Contact",emp.getText().toString());
                            }
                            if (mobile.getText() != null && !mobile.getText().toString().isEmpty()) {
                                par.put("ContactMobile",mobile.getText().toString());
                            }
                            if (email.getText() != null && !email.getText().toString().isEmpty()) {
                                par.put("Email",email.getText().toString());
                            }
                            if (iban.getText() != null && !iban.getText().toString().isEmpty()) {
                                par.put("IBAN",iban.getText().toString());
                            }
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(req);
                }

            }
        });
        AddSupplierD.show();
    }

    public void addSupplier(View view) {
        if (CONTRACT == null) {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Select Project",getResources().getString(R.string.selectProjectFirst));
        }
        else {
            AlertDialog.Builder d = new AlertDialog.Builder(act);
            d.setTitle(getResources().getString(R.string.selectSupplierType))
                    .setMessage(getResources().getString(R.string.selectSupplierType))
                    .setNegativeButton(getResources().getString(R.string.oldSupplier), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            createSearchSupplierDialog();
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.newSupplier), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            createAddNewSupplierDialog();
                        }
                    });
            d.create().show();
        }
    }

    void addQuotationModuleWithSupplierName(String name , SUPPLIER_CLASS s) {
        int index = 0 ;
        ToastMaker.Show(0,name,act);
        View v = LayoutInflater.from(act).inflate(R.layout.unit_quotation,null);
        TextView supName = (TextView) v.findViewById(R.id.supplierName);
        supName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Button addFile = (Button) v.findViewById(R.id.button44);
        LinearLayout filesLayout = (LinearLayout) v.findViewById(R.id.filesLayout);
        supName.setText(name);
        QuotationsLayout.addView(v);
        List<String> list = new ArrayList<String>();
        List<Object> flist = new ArrayList<Object>();
        FILE_NAMES.add(list);
        FILES.add(flist);
        FILES_LAYOUTS.add(filesLayout);
        Quotations.add(new QUOTATION_CLASS(1,s,list));
        QuotationIndex = Quotations.size() - 1 ;
        index = Quotations.size() - 1 ;
        int finalIndex = index;
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuotationIndex = finalIndex;
                createAddFilesDialog();
            }
        });
    }

    void refreshFiles () {
        for (int i=0 ; i < FILES.size();i++) {
            FILES_LAYOUTS.get(i).removeAllViews();
            for (int j=0;j<FILES.get(i).size();j++) {
                TextView t = new TextView(act);
                t.setTextColor(getResources().getColor(R.color.purple_700));
                t.setText("file "+j+1);
                FILES_LAYOUTS.get(i).addView(t);
            }
        }
    }

    void createAddFilesDialog() {
        attachImageFile();
    }

    public void attachImageFile() {
        AlertDialog.Builder B = new AlertDialog.Builder(act);
        B.setTitle(getResources().getString(R.string.fileType))
                .setMessage(getResources().getString(R.string.selectFileType))
                .setPositiveButton("PDF file", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastMaker.Show(1,"arrived" , act);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        act.startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.selectQuotationFile)), FileReqCode);
                    }
                })
                .setNegativeButton("JPG file", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                        open.setType("image/*");
                        act.startActivityForResult(Intent.createChooser(open,"select Image"),IMG_REQCODE);
                    }
                })
                .setNeutralButton("CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                        {
                            ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},CAM_PERMISSION_CODE);
                        }
                        else {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                act.startActivityForResult(takePictureIntent, CAM_REQCODE);
                            } catch (ActivityNotFoundException e) {
                                // display error state to the user
                            }

                        }
                    }
                });
        B.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQCODE) {
            if (resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                FILES.get(QuotationIndex).add(imageBitmap);
                refreshFiles();
                //CardBitmap = imageBitmap ;
                //AddNewdialog.image.setImageBitmap(imageBitmap);
                //AddNewdialog.fileName.setText(String.valueOf(x)+".jpg");
                //ConvertedImage = convertImageToBase64(imageBitmap);
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }

        }
        else if (requestCode == FileReqCode) {
            Log.d("fileselection", "here");
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Log.d("fileselection", "error");
                    return;
                }
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    UriFile = uri;
                    // Perform operations on the document using its URI.
                    File F = new File(uri.toString());
                    String path = F.getAbsolutePath();
                    String displayName = null;
                    if (uri.toString().startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = this.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                //FileName = displayName;
                                //Log.d("fileselection", displayName);
                                //uploadPDF(displayName,uri);
                                //FileNameTV.setText(FileName);
                                FILES.get(QuotationIndex).add(uri);
                                refreshFiles();
                                TextView x = (TextView) FILES_LAYOUTS.get(QuotationIndex).getChildAt(FILES.size()-1);
                                x.setText(displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                    else if (uri.toString().startsWith("file://")) {
                        displayName = F.getName();
                        Log.d("fileselection", displayName);
                    }
                }
//                try {
//                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
//                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
//                    try {
//                        File F = bufferedInputStream.read() ;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    CardBitmap = bmp ;
//                    image.setImageBitmap(bmp);
//                    fileName.setText(x+".jpg");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            } else {
                //Log.d("path", "error result " + data.getData().getPath());
            }
        }
        else if (requestCode == IMG_REQCODE) {
            if (resultCode == RESULT_OK)
            {
                if (data == null) {
                    //Display an error
                    return;
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    FILES.get(QuotationIndex).add(bmp);
                    refreshFiles();
                    //ClientLocation = bmp ;
                    //ImageView image = (ImageView) findViewById(R.id.locationImage);
                    //image.setImageBitmap(ClientLocation);
                    //AddNewdialog.fileName.setText(x+".jpg");
                    //ConvertedImage = convertImageToBase64(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //Log.d("path" , "error result "+data.getData().getPath());
            }
        }
    }

    public void savePurchaseOrder(View view) {
        if (CONTRACT == null ) {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Project ?",getResources().getString(R.string.pleaseSelectProjectFirst));
            return;
        }
        if (Quotations.size() == 0) {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Quotation ?",getResources().getString(R.string.selectQuotations));
            return;
        }
        String notes = "no" ;
        if (notesET.getText() != null && !notesET.getText().toString().isEmpty()) {
            notes = notesET.getText().toString() ;
        }
        int filesTotal = 0 ;
        for (List<Object> l : FILES) {
            filesTotal = filesTotal + l.size();
        }
        int total = 1+Quotations.size()+filesTotal ;
        ProgressLoadingDialog d = new ProgressLoadingDialog(act,total);

        insertPurchaseOrder(CONTRACT.id, CONTRACT.ProjectName,notes, new GetInsertResultCallback() {
            @Override
            public void onSuccess(String res) {
                if (Integer.parseInt(res) > 0) {
                    Log.d("savingPurchase P" , "correct");
                    d.setProgress(1);
                    int PurchaseId = Integer.parseInt(res);
                    int [] Qres = new int [Quotations.size()];
                    final boolean[] stat = {true};
                    for (int i=0 ; i<Quotations.size();i++) {
                        if (stat[0]) {
                            int finalI = i;
                            insertPurchaseOrderQuotation(PurchaseId, Quotations.get(i).supplier.id, Quotations.get(i).supplier.Name, CONTRACT.id, FILES.size(), new GetInsertResultCallback() {
                                @Override
                                public void onSuccess(String res) {
                                    if (Integer.parseInt(res) > 0) {
                                        Log.d("savingPurchase Q", "correct");
                                        d.setProgress(d.getProgress() + 1);
                                        Qres[finalI] = 1;
                                        int QuotationId = Integer.parseInt(res);
                                        boolean[] filesStat = new boolean[]{true} ;
                                        for (int j = 0; j < FILES.get(finalI).size(); j++) {
                                            if (filesStat[0]) {
                                                int finalJ = j;
                                                int[] results = new int[FILES.get(finalI).size()];
                                                if (FILES.get(finalI).get(j) != null) {
                                                    if (FILES.get(finalI).get(j).getClass().getName().equals("android.graphics.Bitmap")) {
                                                        MyApp.savePhoto((Bitmap) FILES.get(finalI).get(j), new VollyCallback() {
                                                            @Override
                                                            public void onSuccess(String s) {
                                                                if (!s.equals("0")) {
                                                                    Log.d("savingPurchase F", "correct");
                                                                    results[finalJ] = 1 ;
                                                                    insertPurchaseOrderQuotationFile(QuotationId, PurchaseId, s, new GetInsertResultCallback() {
                                                                        @Override
                                                                        public void onSuccess(String res) {
                                                                            Log.d("savingPurchase Ft", res);
                                                                            if (res.equals("1")) {
                                                                                Log.d("savingPurchase Ft", "correct");
                                                                                results[finalJ] = 1;
                                                                                d.setProgress(d.getProgress() + 1);
                                                                                //Log.d("savingPurchase", (finalI+1)+" "+Quotations.size()+" "+finalJ+" "+FILES.get(FILES.size()-1).size()+" "+ filesStat[0]);
                                                                                if ((finalI+1) == Quotations.size() && (finalJ+1) == FILES.get(FILES.size()-1).size() && filesStat[0]) {
                                                                                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                                                                                    resetActivity();
                                                                                }
                                                                            } else {
                                                                                results[finalJ] = 0;
                                                                            }
                                                                            if (results[finalJ] == 0) {
                                                                                Log.d("savingPurchase result" + finalJ, results[finalJ] + "");
                                                                                d.stop();
                                                                                Qres[finalI] = 0;
                                                                                filesStat[0] = false;
                                                                            }
                                                                        }
                                                                    });
                                                                } else {
                                                                    //ToastMaker.Show(0, s, act);
                                                                    results[finalJ] = 0;
                                                                }
                                                                if (results[finalJ] == 0) {
                                                                    Log.d("savingPurchase result" + finalJ, results[finalJ] + " image");
                                                                    d.stop();
                                                                    Qres[finalI] = 0;
                                                                    filesStat[0] = false;
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailed(String error) {

                                                            }
                                                        });
                                                    }
                                                    else {
                                                        Log.d("savingPurchase F", FILES.get(finalI).get(j).getClass().getName());
                                                        TextView x = (TextView) FILES_LAYOUTS.get(finalI).getChildAt(j);
                                                        MyApp.uploadPDF(x.getText().toString(), (Uri) FILES.get(finalI).get(j), new VollyCallback() {
                                                            @Override
                                                            public void onSuccess(String s) {
                                                                Log.d("savingPurchase F", s);
                                                                if (!s.equals("0")) {
                                                                    results[finalJ] = 1;
                                                                    d.setProgress(d.getProgress() + 1);
                                                                    insertPurchaseOrderQuotationFile(QuotationId, PurchaseId, s, new GetInsertResultCallback() {
                                                                        @Override
                                                                        public void onSuccess(String res) {
                                                                            Log.d("savingPurchase Ft", res);
                                                                            if (res.equals("1")) {
                                                                                results[finalJ] = 1;
                                                                                d.setProgress(d.getProgress() + 1);
                                                                                //Log.d("savingPurchase", (finalI+1)+" "+Quotations.size()+" "+finalJ+" "+FILES.get(FILES.size()-1).size()+" "+ filesStat[0]);
                                                                                if ((finalI+1) == Quotations.size() && (finalJ+1) == FILES.get(FILES.size()-1).size() && filesStat[0]) {
                                                                                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                                                                                    resetActivity();
                                                                                }
                                                                            } else {
                                                                                results[finalJ] = 0;
                                                                            }
                                                                            if (results[finalJ] == 0) {
                                                                                Log.d("savingPurchase result" + finalJ, results[finalJ] + " pdf");
                                                                                d.stop();
                                                                                Qres[finalI] = 0;
                                                                                filesStat[0] = false;
                                                                            }
                                                                        }
                                                                    });
                                                                } else {
                                                                    results[finalJ] = 0;
                                                                }
                                                                if (results[finalJ] == 0) {
                                                                    Log.d("savingPurchase result" + finalJ, results[finalJ] + " pdf");
                                                                    d.stop();
                                                                    Qres[finalI] = 0;
                                                                    filesStat[0] = false;
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailed(String error) {

                                                            }
                                                        });
                                                    }
                                                }
                                                else {
                                                    Log.d("savingPurchase F", "file is null");
                                                }
                                            }
//                                            if (results[finalJ] == 0) {
//                                                Log.d("savingPurchase result" + finalJ, results[finalJ] + "");
//                                                d.stop();
//                                                Qres[finalI] = 0;
//                                                break;
//                                            }
                                        }
                                    } else if (res.equals("0")) {
                                        Qres[finalI] = 0;
                                        d.stop();
                                        //MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Quotation "+Quotations.get(finalI).supplier.Name+" Not Saved" ,"Quotation "+Quotations.get(finalI).supplier.Name+" Not Saved");
                                    }
                                    if (Qres[finalI] == 0) {
                                        Log.d("savingPurchase Qres" + finalI, Qres[finalI] + "");
                                        stat[0] = false;
                                    }
                                }
                            });
                        }
                        else {
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "Saving Failed", "Saving Failed");
                            break;
                        }
                    }
                }
                else if (res.equals("0")) {
                    d.stop();
                    MESSAGE_DIALOG mm = new MESSAGE_DIALOG(act,"Purchase Order Save Failed" ,"Purchase Order Save Failed");
                }
            }
        });
    }

    public void insertPurchaseOrder (int ProjectID ,String ProjectName,String notes,GetInsertResultCallback callback) {
        StringRequest p_req = new StringRequest(Request.Method.POST,insertPurchaseOrderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , error.toString()) ;
                }
                else {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , "unexpected error") ;
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String,String>();
                par.put("ProjectID", String.valueOf(ProjectID));
                par.put("ProjectName",ProjectName);
                par.put("Notes",notes);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(p_req);
    }

    public void insertPurchaseOrderQuotation (int PurchaseID ,int SupplierID,String SupplierName,int ProjectID,int FilesCount,GetInsertResultCallback callback) {
        StringRequest req = new StringRequest(Request.Method.POST,insertPurchaseOrderQuotationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyApp.sendNotificationsToGroup(RespectiveUsers, getResources().getString(R.string.localPurchaseOrder), "new " + getResources().getString(R.string.localPurchaseOrder), MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, MyApp.MyUser.JobNumber, "PurchaseOrder", MyApp.app, new VolleyCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , error.toString()) ;
                }
                else {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , "unexpected error") ;
                }
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String,String>();
                par.put("PurchaseID", String.valueOf(PurchaseID));
                par.put("SupplierID", String.valueOf(SupplierID));
                par.put("SupplierName",SupplierName);
                par.put("ProjectID", String.valueOf(ProjectID));
                par.put("FilesCount", String.valueOf(FilesCount));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

    public void insertPurchaseOrderQuotationFile(int QuotationID,int PurchaseID,String Link,GetInsertResultCallback callback) {
        Log.d("fileSaveProblem",Link);
        StringRequest req = new StringRequest(Request.Method.POST, insertPurchaseOrderQuotationFileUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , error.toString()) ;
                }
                else {
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , "unexpected error") ;
                }
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("QuotationID", String.valueOf(QuotationID));
                par.put("PurchaseID", String.valueOf(PurchaseID));
                par.put("Link",Link);
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

    public void resetActivity() {
        CONTRACT = null ;
        SUPPLIER = null ;
        UriFile = null ;
        Quotations.clear();
        QuotationsLayout.removeAllViews();
        FILE_NAMES.clear();
        FILES.clear();
        FILES_LAYOUTS.clear();
        QuotationIndex = 0 ;
        ProjectNameTV.setText("");
        ProjectNameTV.setCompoundDrawablesWithIntrinsicBounds(0,0,0,android.R.drawable.ic_menu_add);
        setIndicators();
    }
}