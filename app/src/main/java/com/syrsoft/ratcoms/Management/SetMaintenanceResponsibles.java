package com.syrsoft.ratcoms.Management;

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
import com.syrsoft.ratcoms.Management.Adapters.Branches_Adapter;
import com.syrsoft.ratcoms.Management.Adapters.DepartmentMaintenanceResponsible_Adapter;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.VolleyCallback;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetMaintenanceResponsibles extends AppCompatActivity {

    static Activity act ;
    String getBranchesUrl = MyApp.MainUrl+"getBranches.php";
    String getDepartmentsUrl = MyApp.MainUrl+"getDepartments.php";
    String getMaintenanceResponsiblesUrl = MyApp.MainUrl+"getMaintenanceResponsiblesByDepartment.php";
    String saveResponsibleUrl = MyApp.MainUrl+"addMaintenanceResponsible.php";
    List<Branch> Branches ;
    List<MaintenanceResponsible> Responsibles ;
    public static List<MaintenanceDepartment> Departments ;
    public static RecyclerView BranchesRecycler ;
    public static RecyclerView DepartmentsRecycler ;
    LinearLayoutManager Manager , dManager ;
    public static Branches_Adapter bAdapter ;
    public static DepartmentMaintenanceResponsible_Adapter dAdapter ;
    public static Branch SelectedBranch ;
    public static TextView DepartmentsTV ;
    public static int selectedBranchIndex ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_set_maintenance_responsibles_activity);
        setActivity();
        getBranches(new VolleyCallback() {
            @Override
            public void onSuccess() {
                getMaintenanceDepartments(new VolleyCallback() {
                    @Override
                    public void onSuccess() {
                        for (int i=0;i<Branches.size();i++) {
                            for (int j=0;j<Departments.size();j++) {
                                Branches.get(i).MaintenanceDepartments.add(new MaintenanceDepartment(Departments.get(j).id,Departments.get(j).Department,Departments.get(j).Maintenance));
                            }
                            //Branches.get(i).setMaintenanceDepartments(Departments);
                        }
                        getMaintenanceResponsibles(new VolleyCallback() {
                            @Override
                            public void onSuccess() {
                                if (Responsibles.size() > 0) {
                                    setMaintenanceResponibles();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    void setActivity() {
        act =this ;
        Branches = new ArrayList<Branch>();
        Departments = new ArrayList<MaintenanceDepartment>();
        BranchesRecycler = (RecyclerView) findViewById(R.id.branchesRecycler);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        dManager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        BranchesRecycler.setLayoutManager(Manager);
        DepartmentsRecycler = (RecyclerView) findViewById(R.id.departmentsRecycler);
        DepartmentsRecycler.setLayoutManager(dManager);
        DepartmentsTV = (TextView) findViewById(R.id.departmentsTV);
        Responsibles = new ArrayList<MaintenanceResponsible>();
    }

    void getBranches (VolleyCallback callback) {
        Branches.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getBranchesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0 ; i < arr.length(); i++) {
                            JSONObject row = arr.getJSONObject(i);
                            Branches.add(new Branch(row.getInt("id"),row.getString("BranchName"),row.getString("BranchType"),row.getString("City"),row.getDouble("LA"),row.getDouble("LO")));
                        }
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                    bAdapter = new Branches_Adapter(Branches);
                    BranchesRecycler.setAdapter(bAdapter);
                    callback.onSuccess();
                }

                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess();
            }
        });
        Volley.newRequestQueue(act).add(request);
    }

    void getMaintenanceDepartments(VolleyCallback callback) {
        Departments.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getDepartmentsUrl
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            if (row.getInt("Maintenance") == 1) {
                                Departments.add(new MaintenanceDepartment(row.getInt("id"),row.getString("Department"),row.getInt("Maintenance")));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.onSuccess();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess();
            }
        });
        Volley.newRequestQueue(act).add(request);
    }

    void getMaintenanceResponsibles (VolleyCallback callback) {
        Responsibles.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getMaintenanceResponsiblesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("foundResps" ,response);
                if (!response.equals("0")) {
                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            Responsibles.add(new MaintenanceResponsible(row.getInt("id"),row.getInt("BranchID"),row.getString("BranchName"),row.getInt("DepartmentID"),row.getString("DepartmentName"),row.getInt("EmpID"),row.getString("EmpName")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("foundResps" ,error.toString());
            }
        });
        Volley.newRequestQueue(act).add(request);
    }

    public static void setDepartments() {
        for (int i=0;i<bAdapter.getItemCount();i++) {
            BranchesRecycler.getChildAt(i).setBackgroundResource(R.drawable.transparent_btn);
        }
        BranchesRecycler.getChildAt(selectedBranchIndex).setBackgroundResource(R.drawable.btns);
        for (int i=0;i<SelectedBranch.MaintenanceDepartments.size();i++) {
            if (SelectedBranch.MaintenanceDepartments.get(i).getUsers() != null) {
                Log.d("departmentUser" , SelectedBranch.BranchName+" "+SelectedBranch.MaintenanceDepartments.get(i).Department+" "+SelectedBranch.MaintenanceDepartments.get(i).getUsers().EmpName );
            }
            else {
                Log.d("departmentUser" , SelectedBranch.BranchName+" "+SelectedBranch.MaintenanceDepartments.get(i).Department+" is null" );
            }

        }
        DepartmentsTV.setText(act.getResources().getString(R.string.maintenanceDepartmentsIn)+" " +SelectedBranch.BranchName);
        dAdapter = new DepartmentMaintenanceResponsible_Adapter(SelectedBranch.getMaintenanceDepartments());
        DepartmentsRecycler.setAdapter(dAdapter);
    }

    void setMaintenanceResponibles () {
        Log.d("foundResps" ,Responsibles.size() +" "+Departments.size()+" "+Branches.size() );
        for (int x=0;x<Responsibles.size();x++) {
            for (int i=0;i<Branches.size();i++) {
                for (int j=0;j<Branches.get(i).MaintenanceDepartments.size();j++) {
                    if (Responsibles.get(x).DepartmentID == Branches.get(i).MaintenanceDepartments.get(j).id && Responsibles.get(x).BranchID == Branches.get(i).id) {
                        Log.d("foundResps" ,Responsibles.get(x).EmpName +" "+Responsibles.get(x).BranchID +" "+ Branches.get(i).id );
                        Branches.get(i).MaintenanceDepartments.get(j).setUsers(Responsibles.get(x));
                        break;
                    }
                }
            }
        }
    }


    public void goSaveBranch(View view) {

        if (SelectedBranch.MaintenanceDepartments.size() > 0) {
            for (int i=0 ;i<SelectedBranch.MaintenanceDepartments.size();i++) {
                int finalI = i;
                StringRequest request = new StringRequest(Request.Method.POST, saveResponsibleUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("savingMainResp",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("savingMainResp",error.toString());
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> par = new HashMap<String,String>();
                        par.put("BranchID", String.valueOf(SelectedBranch.id));
                        par.put("BranchName",SelectedBranch.BranchName);
                        par.put("DepartmentID", String.valueOf(Departments.get(finalI).id));
                        par.put("DepartmentName",SelectedBranch.MaintenanceDepartments.get(finalI).Department);
                        par.put("EmpID", String.valueOf(SelectedBranch.MaintenanceDepartments.get(finalI).Users.EmpID));
                        par.put("EmpName",String.valueOf(SelectedBranch.MaintenanceDepartments.get(finalI).Users.EmpName));
                        return par;
                    }
                };
                Volley.newRequestQueue(act).add(request);
            }

        }
    }
}