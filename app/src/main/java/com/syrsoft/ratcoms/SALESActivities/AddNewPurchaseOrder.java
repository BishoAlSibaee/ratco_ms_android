package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.syrsoft.ratcoms.FileParameters;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VollyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddNewPurchaseOrder extends AppCompatActivity {
    TextView txtProjectName, txtClientName, txtSalesMan, txtDateSupply;
    EditText txtMyNotes;
    Button btnContract, btnAddFile, btnSaveOrder;
    Dialog D;
    CheckBox checkBoxAgree;
    Activity act;
    String[] SearchByArr;
    List<PROJECT_CONTRACT_CLASS> ContractsResult;
    String searchProjectUrl = MyApp.MainUrl + "searchProject.php";
    private RequestQueue Q;
    PROJECT_CONTRACT_CLASS CONTRACT;
    Uri UriFile;
    String FileName, expectedSupplyDate;
    List<FileParameters> FILES;
    String UrlInsertImportOrder = MyApp.MainUrl + "insertImportOrder.php";
    String UrlinsertImportOrderAttachement = MyApp.MainUrl + "insertImportOrderAttachement.php";
    LinearLayout filesLayout, LinearParentTerms;
    List<USER> sendTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_purchase_order);
        setActivity();
        setActivityActions();
    }

    void setActivity() {
        act = this;
        FILES = new ArrayList<>();
        sendTo = new ArrayList<>();
        txtProjectName = (TextView) findViewById(R.id.txtProjectName);
        txtClientName = (TextView) findViewById(R.id.txtClientName);
        txtSalesMan = (TextView) findViewById(R.id.txtSalesMan);
        txtDateSupply = (TextView) findViewById(R.id.txtDateSupply);
        txtMyNotes = (EditText) findViewById(R.id.txtMyNotes);
        btnContract = (Button) findViewById(R.id.btnContract);
        btnAddFile = (Button) findViewById(R.id.btnAddFile);
        btnSaveOrder = (Button) findViewById(R.id.btnSaveOrder);
        filesLayout = findViewById(R.id.fileNamesLayout);
        LinearParentTerms = findViewById(R.id.LinearParentTerms);
        Q = Volley.newRequestQueue(act);
        SearchByArr = getResources().getStringArray(R.array.searchProjectByArray);
        ContractsResult = new ArrayList<>();
        checkBoxAgree = findViewById(R.id.checkBoxAgree);
    }

    void setActivityActions() {
        D = new Dialog(act);
        D.setContentView(R.layout.project_search_project_bysalesman_dialog);
        Window w = D.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        D.setCancelable(false);
        ArrayAdapter<String> SearchByAdapter = new ArrayAdapter<String>(act, R.layout.spinner_item, SearchByArr);
        Spinner SearchBySpinner = (Spinner) D.findViewById(R.id.SearchProject_searchBySpinner);
        SearchBySpinner.setAdapter(SearchByAdapter);
        Spinner ResultSpinner = (Spinner) D.findViewById(R.id.SearchProject_searchResultSpinner);
        Spinner SalesmanSpinner = (Spinner) D.findViewById(R.id.SearchProject_salesmanSpinner);
        LinearLayout LinearSalesMan = (LinearLayout) D.findViewById(R.id.LinearSalesMan);
        List<USER> salesmen = MyApp.MyUser.getSalesEmployees();
        String[] names = MyApp.MyUser.convertListToArrayOfNames(salesmen);
        ArrayAdapter<String> salesAdapter = new ArrayAdapter<String>(act, R.layout.spinner_item, names);
        SalesmanSpinner.setAdapter(salesAdapter);
        if (MyApp.MyUser.JobTitle.equals("SalesMan")) {
            LinearSalesMan.setVisibility(View.GONE);
        }
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
                P.setVisibility(View.VISIBLE);
                if (SearchField.getText() != null && !SearchField.getText().toString().isEmpty()) {
                    ContractsResult.clear();
                    String[] resArr = new String[0];
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act, R.layout.spinner_item, resArr);
                    ResultSpinner.setAdapter(adapter);
                    if (SearchBySpinner.getSelectedItem() != null && !SearchBySpinner.getSelectedItem().toString().isEmpty()) {
                        StringRequest request = new StringRequest(Request.Method.POST, searchProjectUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("searchProjectResp", response);
                                P.setVisibility(View.GONE);
                                if (response.equals("0")) {
                                    ToastMaker.Show(1, "no results", act);
                                } else if (response.equals("-1")) {
                                    ToastMaker.Show(1, "error", act);
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
                                if (MyApp.MyUser.JobTitle.equals("SalesMan")) {
                                    par.put("SalesMan", String.valueOf(MyApp.MyUser.JobNumber));
                                } else {
                                    par.put("SalesMan", String.valueOf(salesmen.get(SalesmanSpinner.getSelectedItemPosition()).JobNumber));
                                }
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
                    txtProjectName.setText(ResultSpinner.getSelectedItem().toString());
                    CONTRACT = ContractsResult.get(ResultSpinner.getSelectedItemPosition());
                    TermsAndConditions.getContractTerms(act, CONTRACT.id, new ContractTermsCallback() {
                        @Override
                        public void onSuccess(TermsAndConditions terms) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                int d = Integer.parseInt(terms.Availability);
                                date = sdf.parse(CONTRACT.Date);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                cal.add(Calendar.DAY_OF_MONTH, d);
                                Calendar now = Calendar.getInstance(Locale.getDefault());
                                expectedSupplyDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
                                txtDateSupply.setText(expectedSupplyDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed() {
                        }
                    });
                    CLIENT_CLASS.getClient(act, CONTRACT.ClientID, new ClientCallback() {
                        @Override
                        public void onSuccess(CLIENT_CLASS client) {
                            txtClientName.setText(client.ClientName);
                        }

                        @Override
                        public void onFailed() {
                        }
                    });
                    txtSalesMan.setText(MyApp.getNameSalesMan(CONTRACT.SalesMan));
                    D.dismiss();
                }
            }
        });
        txtProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.show();
            }
        });
        btnContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CONTRACT == null) {
                    new MESSAGE_DIALOG(act, "اختر المشروع", "اختر المشروع اولا");
                    return;
                }
                if (CONTRACT.ContractLink != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CONTRACT.ContractLink));
                    startActivity(browserIntent);
                } else {
                }

            }
        });
        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    act.startActivityForResult(Intent.createChooser(intent, "select File"), 6);
                } catch (Exception e) {
                }
            }
        });
        checkBoxAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxAgree.isChecked()) {
                    checkBoxAgree.setTextColor(getResources().getColor(R.color.ok));
                } else {
                    checkBoxAgree.setTextColor(getResources().getColor(R.color.bg));
                }
            }
        });
    }

    @SuppressLint("Range")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
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
                                FileName = displayName;
                                TextView tv = new TextView(act);
                                tv.setText(FileName);
                                filesLayout.addView(tv);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uri.toString().startsWith("file://")) {
                        displayName = F.getName();
                        TextView tv = new TextView(act);
                        tv.setText(FileName);
                        filesLayout.addView(tv);
                        Log.d("fileselection", displayName);
                    }
                    FILES.add(new FileParameters(displayName, uri));
                }
            } else {
            }
        }
    }

    public void saveMyOrder(View view) {
        Loading d = new Loading(act);
        d.show();
        if (txtProjectName.getText() == "") {
            d.close();
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "اختر المشروع", "اختر المشروع اولاُ");
        } else {
            if (!checkBoxAgree.isChecked()) {
                d.close();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "قراءة الشروط", "يرجى التأكد من قراء شروط تقديم الطلب و الموافقة");
            } else {
                StringRequest request = new StringRequest(Request.Method.POST, UrlInsertImportOrder, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        d.close();
                        if (Integer.parseInt(response) > 0) {
                            List<FileParameters> failedFiles = new ArrayList<>();
                            for (int i = 0; i < FILES.size(); i++) {
                                int finalI = i;
                                MyApp.uploadPDF(FILES.get(i).pdfName, FILES.get(i).pdfUri, new VollyCallback() {
                                    @Override
                                    public void onSuccess(String s) {
                                        StringRequest req = new StringRequest(Request.Method.POST, UrlinsertImportOrderAttachement, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response1) {
                                                if (Integer.parseInt(response1) != 1) {
                                                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "Error", "Error File ");
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                            }
                                        }) {
                                            @Nullable
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                HashMap<String, String> parm = new HashMap<String, String>();
                                                parm.put("update_id", response);
                                                parm.put("link", s);
                                                return parm;
                                            }
                                        };
                                        Q.add(req);
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        failedFiles.add(FILES.get(finalI));
                                    }
                                });
                            }
                            if (failedFiles.size() == 0) {
                                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "حفظ", "تم تقديم الطلب", 0);
                                setSendToList(CONTRACT.SalesMan);
                                MyApp.sendNotificationsToGroup(sendTo, "طلب شراء", "طلب شراء جديد ", MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, CONTRACT.SalesMan, "ImportOrder", act, new VolleyCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                });
                            } else {
                                String files = "";
                                for (int i = 0; i < failedFiles.size(); i++) {
                                    files = files + "-" + failedFiles.get(i).pdfName;
                                }
                                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "Saved", "تم الحفظ ولكن لم يتم حفظ المرفقات التالية \n" + files, 0);
                                MyApp.sendNotificationsToGroup(sendTo, "طلب شراء", "طلب شراء جديد ", MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, CONTRACT.SalesMan, "ImportOrder", act, new VolleyCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                });
                            }
                        } else if (response.equals("0")) {
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "Error", "Error Try Again");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        d.close();
                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "خطأ", "حدث خطأ .. حاول مرة أخرى");
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        String Date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
                        Map<String, String> par = new HashMap<String, String>();
                        par.put("project_id", String.valueOf(CONTRACT.id));
                        par.put("project_name", txtProjectName.getText().toString());
                        par.put("client_id", String.valueOf(CONTRACT.ClientID));
                        par.put("client_name", txtClientName.getText().toString());
                        par.put("salesman", String.valueOf(CONTRACT.SalesMan));
                        par.put("date", Date);
                        par.put("delivery_date", expectedSupplyDate);
                        par.put("salesmanager_accept", "0");
                        par.put("importmanager_accept", "0");
                        par.put("order_status", "0");
                        par.put("receive_status", "0");
                        par.put("user_id", String.valueOf(MyApp.MyUser.id));
                        par.put("Update_date", Date);
                        par.put("notes", txtMyNotes.getText().toString());
                        return par;
                    }
                };
                Q.add(request);
            }
        }
    }

    void setSendToList(int salesmanJobNumber) {
        USER salesman = USER.searchUserByJobNumber(MyApp.EMPS, salesmanJobNumber);
        USER salesManager = USER.searchUserByJobNumber(MyApp.EMPS, salesman.DepartmentManager);
        USER purchaseManager = USER.searchUserByJobtitle(MyApp.EMPS, "Procurement Manager");
        sendTo.add(salesman);
        sendTo.add(salesManager);
        sendTo.add(purchaseManager);
    }
}