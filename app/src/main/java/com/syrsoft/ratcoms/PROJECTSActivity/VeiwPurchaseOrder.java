package com.syrsoft.ratcoms.PROJECTSActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HR;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MainPage;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER.PurchaseQuotations_Adapter;
import com.syrsoft.ratcoms.Projects_Activity;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VollyCallback;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VeiwPurchaseOrder extends AppCompatActivity {

    public static Activity act ;
    int Index ;
    public static LOCAL_PURCHASE_ORDER ORDER ;
    TextView OrderNumber , ProjectName ;
    EditText Notes ;
    public static TextView SupplierNameTV ;
    public static LinearLayout ApproveRejectLayout ;
    String getQuotationsUrl = MyApp.MainUrl + "getPurchaseOrdersQuotations.php";
    String getQuotationFilesUrl = MyApp.MainUrl + "getPurchaseOrdersQuotationFiles.php";
    String responseOrderURL = MyApp.MainUrl + "responsePurchaseOrder.php";
    List<PURCHASE_ORDER_QUOTATION> Quotations ;
    RecyclerView QuotationsRecycler  ;
    public static RecyclerView FilesRecycler ;
    LinearLayoutManager Manager , FManager ;
    public static List<List<PurchaseOrderQuotationFILES>> Files ;
    int myApproveIndex ;
    boolean myApprovalStatus = false , previousApprovalStatus = false ;
    public static PURCHASE_ORDER_QUOTATION Quotation ;
    public static String Source ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_veiw_purchase_order_activity);
        if (getIntent().getExtras() != null) {
            Index = getIntent().getExtras().getInt("Index");
            Source = getIntent().getExtras().getString("Source");
            if (Source.equals("com.syrsoft.ratcoms.PROJECTSActivity.AcceptLocalPurchaseOrder")) {
                ORDER = AcceptLocalPurchaseOrder.MyList.get(Index) ;
            }
            else if (Source.equals("com.syrsoft.ratcoms.PROJECTSActivity.MyLocalPurchaseOrders")) {
                ORDER = MyLocalPurchaseOrders.MyList.get(Index) ;
            }

        }
        setActivity();
        getOrderQuotations();
        searchMyApproveIndex();
        checkIfICanApprove();
    }

    void setActivity () {
        act = this ;
        OrderNumber = findViewById(R.id.textView119);
        ProjectName = findViewById(R.id.textView120);
        SupplierNameTV = findViewById(R.id.textView122);
        ApproveRejectLayout = findViewById(R.id.approveRejectLayout);
        Notes = findViewById(R.id.editTextTextMultiLine2);
        ApproveRejectLayout.setVisibility(View.GONE);
        OrderNumber.setText("Order Number "+ORDER.getId());
        ProjectName.setText("Project Name "+ORDER.getProjectName());
        QuotationsRecycler = findViewById(R.id.quotations_recycler);
        QuotationsRecycler.setNestedScrollingEnabled(false);
        FilesRecycler = findViewById(R.id.filesRecycler);
        FilesRecycler.setNestedScrollingEnabled(false);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        FManager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        QuotationsRecycler.setLayoutManager(Manager);
        FilesRecycler.setLayoutManager(FManager);
        Quotations = new ArrayList<>();
        Files = new ArrayList<>();
    }

    void getOrderQuotations() {
        Loading l = new Loading(act);
        l.show();
        StringRequest req = new StringRequest(Request.Method.POST,getQuotationsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (!response.equals("0")) {
                    try {
                        JSONArray arr = new JSONArray(response);
                        Quotations.clear();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            PURCHASE_ORDER_QUOTATION q = new PURCHASE_ORDER_QUOTATION(row.getInt("id"),row.getInt("PurchaseID"),row.getInt("SupplierID"),row.getString("SupplierName")
                                    ,row.getInt("ProjectID"),row.getInt("FilesCount"),row.getInt("Accepted"));
                            getQuotationFiles(q.id);
                            Quotations.add(q);
                        }
                        PurchaseQuotations_Adapter adapter = new PurchaseQuotations_Adapter(Quotations);
                        QuotationsRecycler.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Quotations.clear();
                    PurchaseQuotations_Adapter adapter = new PurchaseQuotations_Adapter(Quotations);
                    QuotationsRecycler.setAdapter(adapter);
                    ToastMaker.Show(0,"No Purchase Quotations" , act);
                }
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("ordersRes" , error.toString());
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error" , error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("OrderID", String.valueOf(ORDER.id));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

    void getQuotationFiles(int Qid) {
        Loading l = new Loading(act);
        l.show();
        StringRequest req = new StringRequest(Request.Method.POST,getQuotationFilesUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (!response.equals("0")) {
                    try {
                        JSONArray arr = new JSONArray(response);
                        List<PurchaseOrderQuotationFILES> lll = new ArrayList<>();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            PurchaseOrderQuotationFILES f = new PurchaseOrderQuotationFILES(row.getInt("id"),row.getInt("QuotationID"),row.getInt("PurchaseID"),row.getString("Link"));
                            lll.add(f);
                            Log.d("gettingFiles" , f.Link);
                        }
                        Files.add(lll);
                        Log.d("gettingFiles" , Files.size()+ " ");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    List<PurchaseOrderQuotationFILES> lll = new ArrayList<>();
                    Files.add(lll);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("QuotationID", String.valueOf(Qid));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(req);
    }

    void searchMyApproveIndex() {
        if (Source.equals("com.syrsoft.ratcoms.PROJECTSActivity.AcceptLocalPurchaseOrder")) {
            for (int i = 0; i < AcceptLocalPurchaseOrder.ApproveUsers.size(); i++) {
                if (MyApp.MyUser.JobNumber == AcceptLocalPurchaseOrder.ApproveUsers.get(i).JobNumber) {
                    myApproveIndex = i;
                    if (myApproveIndex == 0 && ORDER.Acc1Status == 0) {
                        break;
                    }
                    if (myApproveIndex == 1 && ORDER.Acc2Status == 0) {
                        break;
                    }
                    if (myApproveIndex == 2 && ORDER.Acc3Status == 0) {
                        break;
                    }
                    if (myApproveIndex == 3 && ORDER.Acc4Status == 0) {
                        break;
                    }
                    if (myApproveIndex == 4 && ORDER.Acc5Status == 0) {
                        break;
                    }
                }
            }
        }
        else if (Source.equals("com.syrsoft.ratcoms.PROJECTSActivity.MyLocalPurchaseOrders")) {
            for (int i = 0; i < MyLocalPurchaseOrders.ApproveUsers.size(); i++) {
                if (MyApp.MyUser.JobNumber == MyLocalPurchaseOrders.ApproveUsers.get(i).JobNumber) {
                    myApproveIndex = i;
                    if (myApproveIndex == 0 && ORDER.Acc1Status == 0) {
                        break;
                    }
                    if (myApproveIndex == 1 && ORDER.Acc2Status == 0) {
                        break;
                    }
                    if (myApproveIndex == 2 && ORDER.Acc3Status == 0) {
                        break;
                    }
                    if (myApproveIndex == 3 && ORDER.Acc4Status == 0) {
                        break;
                    }
                    if (myApproveIndex == 4 && ORDER.Acc5Status == 0) {
                        break;
                    }
                }
            }
        }
    }

    void checkIfICanApprove() {
        for (int i = 0 ; i < 5 ; i++) {
            if (i == 0) {
                if (i == myApproveIndex) {
                    if (ORDER.Acc1Status == 0) {
                        previousApprovalStatus = true ;
                        myApprovalStatus = true ;
                    }
                    else {
                        previousApprovalStatus = false ;
                        myApprovalStatus = false ;
                    }
                }
            }
            if (i == 1) {
                if (i == myApproveIndex) {
                    if (ORDER.Acc1Status > 0 ) {
                        previousApprovalStatus = true ;
                        if (ORDER.Acc2Status == 0) {
                            myApprovalStatus = true ;
                        }
                        else {
                            myApprovalStatus = false ;
                        }
                    }
                    else if (ORDER.Acc1Status == 0) {
                        previousApprovalStatus = false ;
                    }
                }
            }
            if (i == 2) {
                if (i == myApproveIndex) {
                    if (ORDER.Acc1Status > 0 && ORDER.Acc2Status > 0) {
                        previousApprovalStatus = true ;
                        if (ORDER.Acc3Status == 0) {
                            myApprovalStatus = true ;
                        }
                        else {
                            myApprovalStatus = false ;
                        }
                    }
                    else {
                        previousApprovalStatus = false ;
                    }
                }
            }
            if (i == 3) {
                if (i == myApproveIndex) {
                    if (ORDER.Acc1Status > 0 && ORDER.Acc2Status > 0 && ORDER.Acc3Status > 0 ) {
                        previousApprovalStatus = true ;
                        if (ORDER.Acc4Status == 0) {
                            myApprovalStatus = true ;
                        }
                        else {
                            myApprovalStatus = false ;
                        }
                    }
                    else {
                        previousApprovalStatus = false ;
                    }
                }
            }
            if (i == 4) {
                if (i == myApproveIndex) {
                    if (ORDER.Acc1Status > 0  && ORDER.Acc2Status > 0 && ORDER.Acc3Status > 0 && ORDER.Acc4Status >0) {
                        previousApprovalStatus = true ;
                        if (ORDER.Acc5Status == 0) {
                            myApprovalStatus = true ;
                        }
                        else {
                            myApprovalStatus = false ;
                        }
                    }
                    else {
                        previousApprovalStatus = false ;
                    }
                }
            }
        }
        Log.d("myApproveStatus" , String.valueOf(myApprovalStatus));
    }

    public void rejectPurchaseOrder(View view) {
        if (Notes.getText() == null || Notes.getText().toString().isEmpty()) {
            ToastMaker.Show(0,"enter your notes",act);
            return;
        }
        Loading l = new Loading(act);
        StringRequest req = new StringRequest(Request.Method.POST, responseOrderURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {
                    MESSAGE_DIALOG m0 = new MESSAGE_DIALOG(act,"Error","Not Saved");
                }
                else if (response.equals("1")) {
                    MESSAGE_DIALOG m1 = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    List<USER> x = new ArrayList<>();
                    USER u = USER.searchUserByJobNumber(MyApp.EMPS,ORDER.Supmitter) ;
                    x.add(u);
                    MyApp.sendNotificationsToGroup(x, getResources().getString(R.string.localPurchaseOrder), "updates on  " + getResources().getString(R.string.localPurchaseOrder), MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, MyApp.MyUser.JobNumber, "PurchaseOrder", MyApp.app, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else {
                    MESSAGE_DIALOG m2 = new MESSAGE_DIALOG(act,"Error",response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error",error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String,String>();
                par.put("Response" , "2");
                par.put("orderID",String.valueOf(ORDER.id));
                par.put("QID",String.valueOf(Quotation.id));
                par.put("Index",String.valueOf(++myApproveIndex));
                par.put("Total",String.valueOf(AcceptLocalPurchaseOrder.ApproveUsers.size()));
                par.put("Notes",Notes.getText().toString());
                par.put("Jnum",String.valueOf(MyApp.MyUser.JobNumber));
                return par;
            }
        };
        if (previousApprovalStatus) {
            if (myApprovalStatus) {
                l.show();
                Volley.newRequestQueue(act).add(req);
            }
            else {
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"You already response this order","You already response this order");
            }
        }
        else {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.previousAuthsMustDone),getResources().getString(R.string.previousAuthsMustDone));
        }
    }

    public void approvePurchaseOrder(View view) {
        if (Notes.getText() == null || Notes.getText().toString().isEmpty()) {
            ToastMaker.Show(0,"enter your notes",act);
            return;
        }
        Loading l = new Loading(act);
        StringRequest req = new StringRequest(Request.Method.POST, responseOrderURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {
                    MESSAGE_DIALOG m0 = new MESSAGE_DIALOG(act,"Error","Not Saved");
                }
                else if (response.equals("1")) {
                    new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
                    MyApp.MyUser.getEmployeesData(act, new VollyCallback() {
                        @Override
                        public void onSuccess(String s) {
                            if (Projects_Activity.isRunning) {
                                Projects_Activity.setCounters();
                            }
                            if (MainPage.isRunning) {
                                MainPage.setCounters();
                            }
                            if (HR.isRunning) {
                                HR.setHRCounters();
                            }
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
                    List<USER> x = new ArrayList<>();
                    USER u = USER.searchUserByJobNumber(MyApp.EMPS,ORDER.Supmitter) ;
                    x.add(u);
                    MyApp.sendNotificationsToGroup(x, getResources().getString(R.string.localPurchaseOrder), "updates on  " + getResources().getString(R.string.localPurchaseOrder), MyApp.MyUser.FirstName + " " + MyApp.MyUser.LastName, MyApp.MyUser.JobNumber, "PurchaseOrder", MyApp.app, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
                else {
                    new MESSAGE_DIALOG(act,"Error",response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error",error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String,String>();
                par.put("Response" , "1");
                par.put("orderID",String.valueOf(ORDER.id));
                par.put("QID",String.valueOf(Quotation.id));
                par.put("Index",String.valueOf(++myApproveIndex));
                par.put("Total",String.valueOf(AcceptLocalPurchaseOrder.ApproveUsers.size()));
                par.put("Notes",Notes.getText().toString());
                par.put("Jnum",String.valueOf(MyApp.MyUser.JobNumber));
                return par;
            }
        };
        if (previousApprovalStatus) {
            if (myApprovalStatus) {
                l.show();
                Volley.newRequestQueue(act).add(req);
            }
            else {
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"You already response this order","You already response this order");
            }
        }
        else {
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.previousAuthsMustDone),getResources().getString(R.string.previousAuthsMustDone));
        }
    }
}