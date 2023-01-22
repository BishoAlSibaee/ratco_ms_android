package com.syrsoft.ratcoms.HRActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.HR_OrderTypes_Adapter;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.Login;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MainPage;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R ;
import com.syrsoft.ratcoms.ToastMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.interfaces.PBEKey;

public class ManageOrdersAuthority extends AppCompatActivity {

    static Activity act ;
    String getHrOrdersTypes = "https://ratco-solutions.com/RatcoManagementSystem/getHrOrdersTypes.php" ;
    String getAuthorizedEmpsUrl = "https://ratco-solutions.com/RatcoManagementSystem/getAuthorizedEmps.php" ;
    String saveAuthoritiesUrl = "https://ratco-solutions.com/RatcoManagementSystem/updateOrderTypeAuthorities.php" ;
    List<HR_ORDER_TYPE> Types ;
    static List<Authrized_Emp> authorizedEmps ;
    RecyclerView ordersTypes ;
    HR_OrderTypes_Adapter adapter ;
    LinearLayoutManager manager ;
    public static LinearLayout selectedOrdertypeLayout ;
    public static TextView slectedOrdertypeName ;
    static ListView authsList ;
    static List<Authrized_Emp> CurrentAutheList ;
    public static HR_ORDER_TYPE SelectedOrderType ;
    private Button addBtn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_orders_authority);
        setActivity();
        getHrOrderTypes();
    }

    void setActivity () {
        act = this ;
        addBtn = (Button) findViewById(R.id.button9);
        Types = new ArrayList<HR_ORDER_TYPE>();
        authorizedEmps = new ArrayList<Authrized_Emp>();
        CurrentAutheList = new ArrayList<Authrized_Emp>();
        ordersTypes = (RecyclerView) findViewById(R.id.hrOrdertypes_recycler);
        manager = new LinearLayoutManager(act,LinearLayoutManager.VERTICAL,false);
        adapter = new HR_OrderTypes_Adapter(Types);
        ordersTypes.setLayoutManager(manager);
        selectedOrdertypeLayout = (LinearLayout) findViewById(R.id.selectedOrdertypeLayout);
        selectedOrdertypeLayout.setVisibility(View.GONE);
        slectedOrdertypeName = (TextView) findViewById(R.id.selectedOrderType);
        authsList = (ListView) findViewById(R.id.auths_list);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Authrized_Emp[] selectedAuth = new Authrized_Emp[1];
                Dialog d = new Dialog(act);
                d.setContentView(R.layout.dialog_add_authority_toodrder_dialog);
                //d.setCancelable(false);
                ListView list = (ListView) d.findViewById(R.id.AddAuthorityToOrderDialog_list);
                TextView selected = (TextView) d.findViewById(R.id.AddAuthorityToOrderDialog_text);
                Button add = (Button) d.findViewById(R.id.AddAuthorityToOrderDialog_addBtn);
                List<Authrized_Emp> myList = new ArrayList<Authrized_Emp>();
                myList = authorizedEmps ;
                for (int i=0;i<CurrentAutheList.size();i++) {
                    for (int j=0;j<myList.size();j++) {
                        if (CurrentAutheList.get(i) == myList.get(j)) {
                            myList.remove(j);
                        }
                    }
                }
                if (myList.size() > 0) {
                    String[] rrr = new String[myList.size()];
                    for (int t =0;t<myList.size();t++ ) {
                        rrr[t] = myList.get(t).AuthorizationName+" "+myList.get(t).ArabicName ;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,rrr);
                    list.setAdapter(adapter);
                }
                List<Authrized_Emp> finalMyList = myList;
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //ToastMaker.Show(1,"selected",act);
                        selected.setText(finalMyList.get(position).AuthorizationName+" "+ finalMyList.get(position).ArabicName);
                        selectedAuth[0] = finalMyList.get(position);
                    }
                });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Authname" , selectedAuth[0].AuthorizationName );
                        boolean DD = false ;
                        if (selectedAuth[0] != null ) {
                            for (int z =0;z< MyApp.EMPS.size();z++) {
                                Log.d("Authname" , MyApp.EMPS.get(z).JobTitle +" "+ selectedAuth[0].AuthorizationName);
                                if (MyApp.EMPS.get(z).JobTitle.equals(selectedAuth[0].AuthorizationName) || selectedAuth[0].AuthorizationName.equals("Direct Manager") || selectedAuth[0].AuthorizationName.equals("Department Manager")) {
                                    DD = true ;
                                    break;
                                }
                            }
                            if (DD) {
                                CurrentAutheList.add(selectedAuth[0]);
                                if (CurrentAutheList.size()>0) {
                                    String[] xxx = new String[CurrentAutheList.size()];
                                    for (int x = 0 ; x < CurrentAutheList.size();x++) {
                                        xxx[x] = CurrentAutheList.get(x).AuthorizationName+" " + CurrentAutheList.get(x).ArabicName ;
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,xxx);
                                    authsList.setAdapter(adapter);
                                }
                                d.dismiss();
                            }
                            else {
                                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Job Title Empty","This Jobtitle is empty now");
                            }
                        }

                    }
                });
                d.show();
            }
        });
        authsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                builder.setTitle("Delete "+ CurrentAutheList.get(position).AuthorizationName +"..?");
                builder.setMessage("are you sure ..?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CurrentAutheList.remove(position);
                        String[] xxx = new String[CurrentAutheList.size()];
                        for (int x = 0 ; x < CurrentAutheList.size();x++) {
                            xxx[x] = CurrentAutheList.get(x).AuthorizationName+" " + CurrentAutheList.get(x).ArabicName ;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,xxx);
                        authsList.setAdapter(adapter);
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    void getHrOrderTypes() {
        Loading l = new Loading(act); l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getHrOrdersTypes, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("hrOrdersResponse" , response );
                l.close();
                if (response != null ) {
                    if (response.equals("0")) {

                    }
                    else {
                        List<Object> list = JsonToObject.translate(response,HR_ORDER_TYPE.class,act);
                        Types.clear();
                        for(int i=0 ; i<list.size() ; i++) {
                            HR_ORDER_TYPE x = (HR_ORDER_TYPE) list.get(i) ;
                            Types.add(x);
                        }
                        ordersTypes.setAdapter(adapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        });
        Volley.newRequestQueue(act).add(request);
        getAuthorizedEmps();
    }

    void getAuthorizedEmps() {
        StringRequest request = new StringRequest(Request.Method.POST,getAuthorizedEmpsUrl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("authoriedEmpsResponse" , response );
                if (response.equals("0")) {

                }
                else {
                    authorizedEmps.clear();
                    List<Object> list = JsonToObject.translate(response,Authrized_Emp.class,act);
                    for (Object x : list) {
                        authorizedEmps.add((Authrized_Emp) x) ;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(act).add(request);
        makeCurrentAuthsList();
    }

    public static void makeCurrentAuthsList () {
        if (SelectedOrderType == null ) {
            ToastMaker.Show(1,"Please Select Order Type",act);
            return;
        }
        CurrentAutheList.clear();
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth1 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth2 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth3 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth4 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth5 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth6 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth7 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth8 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth9 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }
        for (int j =0;j<authorizedEmps.size();j++) {
            if (SelectedOrderType.Auth10 == authorizedEmps.get(j).JobTitleId) {
                CurrentAutheList.add(authorizedEmps.get(j));
            }
        }


            String[] xxx = new String[CurrentAutheList.size()];
            for (int x = 0 ; x < CurrentAutheList.size();x++) {
                xxx[x] = CurrentAutheList.get(x).AuthorizationName+" " + CurrentAutheList.get(x).ArabicName ;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(act,R.layout.spinner_item,xxx);
            authsList.setAdapter(adapter);
    }

    public void saveAuthorities(View view) {

        if (CurrentAutheList.size() == 0 ) {
            ToastMaker.Show(1,"please select first",act);
            return;
        }
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, saveAuthoritiesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                Log.d("SaveResponse" , response) ;
                getHrOrderTypes();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.authoritiesSaved),getResources().getString(R.string.authoritiesSaved));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("SaveResponse" , error.getMessage()) ;
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),getResources().getString(R.string.default_error_msg));
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> par = new HashMap<String, String>();
                par.put("id", String.valueOf( SelectedOrderType.id) );
                for ( int i = 0 ; i < 10 ; i++ ) {
                    if ( i < CurrentAutheList.size() ) {
                        par.put("auth"+(i+1), String.valueOf(CurrentAutheList.get(i).JobTitleId));
                    }
                    else {
                        par.put("auth"+(i+1), "0");
                    }

                }

                return par ;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

}