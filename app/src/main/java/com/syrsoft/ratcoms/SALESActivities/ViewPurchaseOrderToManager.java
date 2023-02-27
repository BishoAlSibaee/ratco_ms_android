package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.PurchaseUpdateAdapter;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VollyCallback;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewPurchaseOrderToManager extends AppCompatActivity {
    TextView PN, CN, SM, ED, txtDate, txtAcceptSalesManager, txtAcceptImportManager, txtOrderStatus, txtExpectedDate, txtReceiveStatus, txtAccDateSalesManager, txtAccDateImportManager, txtDateOrderStatus, txtDateReceiveStatus;
    EditText txtNotes;
    Button ViewCon, AddFile, btnOK, btnNO;
    CheckBox Checkreceived, CheckDone;
    int Index;
    PURCHASE_CLASS p;
    PROJECT_CONTRACT_CLASS CONTRACT;
    Activity act;
    LinearLayout LinearBtnAttachment, LinearBtnFile, LinearProcurement, acceptLayout;
    Uri UriFile;
    List<FileParameters> FILES;
    String FileName;
    RecyclerView ResOrderUpdate;
    LinearLayoutManager manager;
    PurchaseUpdateAdapter adapter;
    String UrlUpdateAcc = "http://192.168.100.101/EmployeeManagement/UpdateSalesManagerAccept.php";
    String UrlTestInsertLink = "http://192.168.100.101/EmployeeManagement/testInsertLink.php";
    String UrlStatusOrder = "http://192.168.100.101/EmployeeManagement/OrderStatus.php";
    String UrlOrderUpdate = "http://192.168.100.101/EmployeeManagement/OrderUpdate.php";
    private RequestQueue Q;
    List<USER> sendTo;
    String MyJobTitle, EcpectedDate;
    String Status = " ";
    Loading l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_buy_purchase_to_manager);
        Index = getIntent().getExtras().getInt("index");
        p = ViewPurchaseOrders.listPurchase.get(Index);
        setActivity();
        setActivityActions();
        adapter = new PurchaseUpdateAdapter(p.listPurchaseUpdate);
        ResOrderUpdate.setAdapter(adapter);
        Q = Volley.newRequestQueue(act);
    }

    void setActivity() {
        act = this;
        FILES = new ArrayList<>();
        PROJECT_CONTRACT_CLASS.getProjectById(p.Project_id, act, new GetProjectCallback() {
            @Override
            public void onSuccess(PROJECT_CONTRACT_CLASS p) {
                CONTRACT = p;
            }

            @Override
            public void onFailed() {
            }
        });
        PN = findViewById(R.id.txtProName);
        CN = findViewById(R.id.textView168);
        SM = findViewById(R.id.textView169);
        ED = findViewById(R.id.textView172);
        txtNotes = findViewById(R.id.txtNotes);
        txtDate = findViewById(R.id.textView167);
        txtAcceptSalesManager = findViewById(R.id.txtAcceptSalesManager);
        txtAcceptImportManager = findViewById(R.id.txtAcceptImportManager);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);
        txtExpectedDate = findViewById(R.id.txtExpectedDate);
        txtReceiveStatus = findViewById(R.id.txtReceiveStatus);
        txtAccDateSalesManager = findViewById(R.id.txtAccDateSalesManager);
        txtAccDateImportManager = findViewById(R.id.txtAccDateImportManager);
        txtDateOrderStatus = findViewById(R.id.txtDateOrderStatus);
        txtDateReceiveStatus = findViewById(R.id.txtDateReceiveStatus);
        btnOK = findViewById(R.id.btnOK);
        btnNO = findViewById(R.id.btnNO);
        ViewCon = findViewById(R.id.button58);
        AddFile = findViewById(R.id.button74);
        acceptLayout = findViewById(R.id.acceptLayout);
        LinearBtnAttachment = findViewById(R.id.LinearBtnAttachment);
        LinearBtnFile = findViewById(R.id.LinearBtnFile);
        LinearProcurement = findViewById(R.id.LinearProcurement);
        sendTo = new ArrayList<>();
        Checkreceived = findViewById(R.id.checkBox9);
        CheckDone = findViewById(R.id.checkBox8);
        setInfoOrder();
        ResOrderUpdate = findViewById(R.id.ResOrderUpdate);
        manager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        ResOrderUpdate.setLayoutManager(manager);
        ResOrderUpdate.setNestedScrollingEnabled(false);
        MyJobTitle = MyApp.MyUser.JobTitle;
        l = new Loading(act);
        if (MyJobTitle.equals("Procurement Manager")) {
            acceptLayout.setVisibility(View.VISIBLE);
            LinearProcurement.setVisibility(View.VISIBLE);
        }
        if (MyJobTitle.equals("Sales Manager")) {
            acceptLayout.setVisibility(View.VISIBLE);
        }
        if (MyJobTitle.equals("Sales Man") || MyJobTitle.equals("Sales Coordinator")) {
            acceptLayout.setVisibility(View.VISIBLE);
            btnOK.setVisibility(View.GONE);
            btnNO.setText(getResources().getString(R.string.send));
            btnNO.setBackgroundResource(R.drawable.approve_btns);
        }
        if (p.ReceiveStatus.equals("0")) {
            acceptLayout.setVisibility(View.VISIBLE);
        } else {
            acceptLayout.setVisibility(View.GONE);
        }
    }

    @Override

    @SuppressLint("Range")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6) {
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
                                FileName = displayName;
                                TextView tv = new TextView(act);
                                tv.setText(FileName);
                                LinearBtnFile.addView(tv);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uri.toString().startsWith("file://")) {
                        displayName = F.getName();
                        TextView tv = new TextView(act);
                        tv.setText(FileName);
                        LinearBtnFile.addView(tv);
                        Log.d("fileselection", displayName);
                    }
                    FILES.add(new FileParameters(displayName, uri));
                }
            } else {
            }
        }
    }

    void setActivityActions() {
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog D = new Dialog(act);
                D.setContentView(R.layout.dialog_select_date_dialog);
                Window window = D.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                D.setCancelable(false);
                CalendarView C = (CalendarView) D.findViewById(R.id.SelectDateDialog_calender);
                TextView date = (TextView) D.findViewById(R.id.SelectDateDialog_dateTv);
                C.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        EcpectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        date.setText(EcpectedDate);
                    }
                });
                Button Cancel = (Button) D.findViewById(R.id.SelectDateDialog_cancel);
                Button Select = (Button) D.findViewById(R.id.SelectDateDialog_select);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        D.dismiss();
                    }
                });
                Select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtDate.setText(EcpectedDate);
                        D.dismiss();
                    }
                });
                D.show();
            }
        });
        Checkreceived.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CheckDone.setChecked(false);
                    Status = "1";
                }
            }
        });
        CheckDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Checkreceived.setChecked(false);
                    Status = "1";
                }
            }
        });
    }

    public void goAccept(View view) {
        if (MyJobTitle.equals("Procurement Manager")) {
            if (p.SalesManagerAccept.equals("0")) {
                new MESSAGE_DIALOG(act, "موافقة مدير المبيعات", "موافقة مدير المبيعات يجب ان تتم اولا");
                return;
            }
        }
        if (txtNotes.getText().toString().isEmpty()) {
            ToastMaker.Show(1, "Enter Note", act);
        } else {
            l.show();
            StringRequest request = new StringRequest(Request.Method.POST, UrlUpdateAcc, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("0")) {
                        l.close();
                        Log.d("respoUbpdate", response);
                        List<FileParameters> failedFiles = new ArrayList<>();
                        for (int i = 0; i < FILES.size(); i++) {
                            int finalI = i;
                            MyApp.uploadPDF(FILES.get(i).pdfName, FILES.get(i).pdfUri, new VollyCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    StringRequest req = new StringRequest(Request.Method.POST, UrlTestInsertLink, new Response.Listener<String>() {
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
                                            Map<String, String> parm = new HashMap<String, String>();
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
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "ارسال", "تم الإرسال", 0);
                            setSendToList(CONTRACT.SalesMan);
                            MyApp.sendNotificationsToGroup(sendTo, "تحديث جديد", "تحديث لطلب الشراء", MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, CONTRACT.SalesMan, "ImportOrder", act, new VolleyCallback() {
                                @Override
                                public void onSuccess() {
                                }
                            });
                        } else {
                            String files = "";
                            for (int i = 0; i < failedFiles.size(); i++) {
                                files = files + "-" + failedFiles.get(i).pdfName;
                            }
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "ارسال", "تم الإرسال", 0);
                            setSendToList(CONTRACT.SalesMan);
                            MyApp.sendNotificationsToGroup(sendTo, "تحديث جديد", "يوجد تحديث لطلب الشراء", MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, CONTRACT.SalesMan, "ImportOrder", act, new VolleyCallback() {
                                @Override
                                public void onSuccess() {
                                }
                            });
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    l.close();
                    new MESSAGE_DIALOG(act, "خطأ", "لم تتم الموافقة على الطلب ");
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Calendar c = Calendar.getInstance(Locale.getDefault());
                    String Date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
                    Map<String, String> parm = new HashMap<String, String>();
                    parm.put("id", String.valueOf(p.id));
                    parm.put("UserDepartment", MyJobTitle);
                    parm.put("salesmanager_accept", "1");
                    parm.put("salesmanager_accept_date", Date);
                    parm.put("user_id", String.valueOf(MyApp.MyUser.id));
                    parm.put("Update_date", Date);
                    parm.put("type", "Update");
                    parm.put("notes", txtNotes.getText().toString());
                    parm.put("importmanager_accept", "1");
                    parm.put("importmanager_accept_date", Date);
                    return parm;
                }
            };
            if (MyJobTitle.equals("Procurement Manager")) {
                OrderStatus();
            }
            Q.add(request);
        }
    }

    public void goUpdate(View view) {
        if (MyJobTitle.equals("Procurement Manager")) {
            if (p.SalesManagerAccept.equals("0")) {
                new MESSAGE_DIALOG(act, "تحديث مدير المبيعات", "تحديث مدير المبيعات يجب ان يتم اولا");
                return;
            }
        }
        if (txtNotes.getText().toString().isEmpty()) {
            ToastMaker.Show(1, "Enter Note", act);
        } else {
            l.show();
            StringRequest request = new StringRequest(Request.Method.POST, UrlOrderUpdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("0")) {
                        l.close();
                        List<FileParameters> failedFiles = new ArrayList<>();
                        for (int i = 0; i < FILES.size(); i++) {
                            int finalI = i;
                            MyApp.uploadPDF(FILES.get(i).pdfName, FILES.get(i).pdfUri, new VollyCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    StringRequest req = new StringRequest(Request.Method.POST, UrlTestInsertLink, new Response.Listener<String>() {
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
                                            Map<String, String> parm = new HashMap<String, String>();
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
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act, "إرسال", "تم الإرسال", 0);
                            setSendToList(CONTRACT.SalesMan);
                            MyApp.sendNotificationsToGroup(sendTo, "تحديث جديد", "تحديث لطلب الشراء", MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, CONTRACT.SalesMan, "ImportOrder", act, new VolleyCallback() {
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
                            setSendToList(CONTRACT.SalesMan);
                            MyApp.sendNotificationsToGroup(sendTo, "تحديث جديد", "تحديث لطلب الشراء", MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, CONTRACT.SalesMan, "ImportOrder", act, new VolleyCallback() {
                                @Override
                                public void onSuccess() {
                                }
                            });
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    l.close();
                    new MESSAGE_DIALOG(act, "خطأ", "لم يتم تحديث الطلب .. حاول مرة اخرى ");
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Calendar c = Calendar.getInstance(Locale.getDefault());
                    String Date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
                    Map<String, String> parm = new HashMap<String, String>();
                    parm.put("order_id", String.valueOf(p.id));
                    parm.put("user_id", String.valueOf(MyApp.MyUser.id));
                    parm.put("Update_date", Date);
                    parm.put("type", "Update");
                    parm.put("notes", txtNotes.getText().toString());
                    return parm;
                }
            };
            if (MyJobTitle.equals("Procurement Manager")) {
                OrderStatus();
            }
            Q.add(request);
        }
    }

    public void goViewContract(View view) {
        if (CONTRACT == null) {
            new MESSAGE_DIALOG(act, "تنبية", "لايوجد مرفقات");
            return;
        }
        if (CONTRACT.ContractLink != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CONTRACT.ContractLink));
            startActivity(browserIntent);
        } else {
            Log.d("link", "IsNull");
        }
    }

    public void goAddFile(View view) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            act.startActivityForResult(Intent.createChooser(intent, "select File"), 6);
        } catch (Exception e) {
            Log.d("AttacheFileError", e.getMessage());
        }

    }

    public void OrderStatus() {
        if (Checkreceived.isChecked() || CheckDone.isChecked() || !txtDate.getText().toString().isEmpty()) {
            StringRequest request = new StringRequest(Request.Method.POST, UrlStatusOrder, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("RESPoooo", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("RESP", error.toString());
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Calendar c = Calendar.getInstance(Locale.getDefault());
                    String Date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
                    Log.d("RESPoooo", "Sta " + Status);
                    Map<String, String> parm = new HashMap<String, String>();
                    parm.put("id", String.valueOf(p.id));
                    if (EcpectedDate != null) {
                        parm.put("ecpected_delevary_date", EcpectedDate);
                    }
                    if (Checkreceived.isChecked()) {
                        parm.put("receive_status", Status);
                        parm.put("receive_date", Date);
                    } else if (CheckDone.isChecked()) {
                        parm.put("order_status", Status);
                        parm.put("order_date", Date);
                    }
                    return parm;
                }
            };
            Q.add(request);
        }
    }

    void setSendToList(int salesmanJobNumber) {
        USER salesman = USER.searchUserByJobNumber(MyApp.EMPS, salesmanJobNumber);
        USER salesManager = USER.searchUserByJobNumber(MyApp.EMPS, salesman.DepartmentManager);
        USER purchaseManager = USER.searchUserByJobtitle(MyApp.EMPS, "Procurement Manager");
        USER posender = USER.searchUserByID(MyApp.EMPS, p.listPurchaseUpdate.get(0).UserId);
        if (salesman != posender) {
            sendTo.add(posender);
        }
        sendTo.add(salesman);
        sendTo.add(salesManager);
        sendTo.add(purchaseManager);
        if (sendTo.contains(MyApp.MyUser)) {
            sendTo.remove(MyApp.MyUser);
        }
    }

    void setInfoOrder() {
        PN.setText(ViewPurchaseOrders.listPurchase.get(Index).Project_Name);
        CN.setText(ViewPurchaseOrders.listPurchase.get(Index).Client_Name);
        SM.setText(MyApp.getNameSalesMan(ViewPurchaseOrders.listPurchase.get(Index).salesman));
        ED.setText(ViewPurchaseOrders.listPurchase.get(Index).DeliveryDate);

        if (ViewPurchaseOrders.listPurchase.get(Index).SalesManagerAccept.equals("1")) {
            txtAcceptSalesManager.setTextColor(getResources().getColor(R.color.ok));
            txtAccDateSalesManager.setTextColor(getResources().getColor(R.color.ok));
            txtAcceptSalesManager.setText("تمت الموافقة");
            txtAccDateSalesManager.setText(ViewPurchaseOrders.listPurchase.get(Index).SalesManagerAcceptDate);
        } else {
            txtAcceptSalesManager.setTextColor(getResources().getColor(R.color.gray));
            txtAccDateSalesManager.setTextColor(getResources().getColor(R.color.gray));
            txtAcceptSalesManager.setText("لم تتم الموافقة");
            txtAccDateSalesManager.setText("لايوجد");
        }

        if (ViewPurchaseOrders.listPurchase.get(Index).ImportManagerAccept.equals("1")) {
            txtAcceptImportManager.setTextColor(getResources().getColor(R.color.ok));
            txtAccDateImportManager.setTextColor(getResources().getColor(R.color.ok));
            txtAcceptImportManager.setText("تمت الموافقة");
            txtAccDateImportManager.setText(ViewPurchaseOrders.listPurchase.get(Index).ImportManagerAcceptDate);
        } else {
            txtAcceptImportManager.setTextColor(getResources().getColor(R.color.gray));
            txtAccDateImportManager.setTextColor(getResources().getColor(R.color.gray));
            txtAcceptImportManager.setText("لم تتم الموافقة");
            txtAccDateImportManager.setText("لا يوجد");
        }

        if (ViewPurchaseOrders.listPurchase.get(Index).OrderStatus.equals("1")) {
            txtOrderStatus.setTextColor(getResources().getColor(R.color.ok));
            txtDateOrderStatus.setTextColor(getResources().getColor(R.color.ok));
            txtOrderStatus.setText("تم طلب المواد");
            txtDateOrderStatus.setText(ViewPurchaseOrders.listPurchase.get(Index).OrderDate);
        } else {
            txtOrderStatus.setTextColor(getResources().getColor(R.color.gray));
            txtDateOrderStatus.setTextColor(getResources().getColor(R.color.gray));
            txtOrderStatus.setText("لم يتم طلب المواد");
            txtDateOrderStatus.setText("لا يوجد");
        }

        if (ViewPurchaseOrders.listPurchase.get(Index).ReceiveStatus.equals("1")) {
            txtReceiveStatus.setTextColor(getResources().getColor(R.color.ok));
            txtDateReceiveStatus.setTextColor(getResources().getColor(R.color.ok));
            txtReceiveStatus.setText("تم اتسلام المواد");
            txtDateReceiveStatus.setText(ViewPurchaseOrders.listPurchase.get(Index).ReceiveDate);
        } else {
            txtReceiveStatus.setTextColor(getResources().getColor(R.color.gray));
            txtDateReceiveStatus.setTextColor(getResources().getColor(R.color.gray));
            txtReceiveStatus.setText("لم يتم إستلام المواد");
            txtDateReceiveStatus.setText("لا يوجد");
        }
        txtDate.setText(ViewPurchaseOrders.listPurchase.get(Index).EcpectedDelevaryDate);
        txtExpectedDate.setText(ViewPurchaseOrders.listPurchase.get(Index).EcpectedDelevaryDate);
    }
}