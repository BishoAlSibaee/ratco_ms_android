package com.syrsoft.ratcoms.HRActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.JsonToObject;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageStaffAttendanceByLocation extends AppCompatActivity {

    List<StaffAttendByLocation_CLASS> Users ;
    public static List<USER> THE_USERS , NotAddedUsers , selectedList , selectedUsersList ;
    Activity act ;
    String getUsersUrl = "https://ratco-solutions.com/RatcoManagementSystem/getStaffByLocation.php";
    String saveUrl = "https://ratco-solutions.com/RatcoManagementSystem/modifyStaffByLocation.php";
    ListView usersListView , selectedUsersLV;
    addUsersToByLocationDialog_Adapter ada , selectedUsersAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.syrsoft.ratcoms.R.layout.manage_staff_attendance_by_location_activity);
        setActivity();
        getUsers();
    }

    void setActivity() {
        act = this ;
        Users = new ArrayList<StaffAttendByLocation_CLASS>();
        THE_USERS = new ArrayList<USER>();
        NotAddedUsers = new ArrayList<USER>();
        selectedUsersList = new ArrayList<USER>();
        usersListView = (ListView) findViewById(R.id.listView_Users);
        selectedUsersLV = (ListView) findViewById(R.id.selectedusers_lv);
        ada = new addUsersToByLocationDialog_Adapter(THE_USERS,act);
        selectedUsersAdapter = new addUsersToByLocationDialog_Adapter(selectedUsersList,act);
        selectedUsersLV.setAdapter(selectedUsersAdapter);
        usersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder b = new AlertDialog.Builder(act);
                b.setTitle("Delete .. ?");
                b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        THE_USERS.remove(position);
                        ada.notifyDataSetChanged();
                    }
                });
                b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                b.show();
                return false;

            }
        });
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean s = false ;
                int ind = 0 ;
                USER U = THE_USERS.get(position) ;

                for (int i=0;i< selectedUsersList.size();i++) {
                    //ToastMaker.Show(0,id+" ",act);
                    if (selectedUsersList.get(i).JobNumber == U.JobNumber) {
                        s = true ;
                        ind = i ;
                    }
                }
                if (s) {
                    selectedUsersList.remove(ind);
                    selectedUsersAdapter.notifyDataSetChanged();
                    //view.setBackgroundColor(getResources().getColor(R.color.purple_700,null));
                }
                else {
                    selectedUsersList.add(THE_USERS.get(position));
                    selectedUsersAdapter.notifyDataSetChanged();
                    //view.setBackgroundColor(Color.DKGRAY);
                    //finalAdapter.getView(position,view,parent).setBackgroundColor(Color.DKGRAY);
                }
                //selectedUsersList.add(THE_USERS.get(position));
                //selectedUsersAdapter.notifyDataSetChanged();
            }
        });
    }

    void getUsers() {
        Loading l = new Loading(act); l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getUsersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    l.close();
                    Log.d("getUsersByLocation" , response);
                if (!response.equals("0") ) {
                    List<Object> list = JsonToObject.translate(response,StaffAttendByLocation_CLASS.class,act);
                    for (Object o :list) {
                        StaffAttendByLocation_CLASS a = (StaffAttendByLocation_CLASS) o ;
                        Users.add(a);
                    }
                    if (Users.size() > 0 ) {
                        for (int i = 0; i< MyApp.EMPS.size(); i++ ) {
                            for (int j=0; j<Users.size(); j++) {
                                if ( MyApp.EMPS.get(i).id == Users.get(j).EmpID ) {
                                    THE_USERS.add(MyApp.EMPS.get(i));
                                    //continue;
                                }
                            }
                        }

                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,aa);
                        ada.notifyDataSetChanged();
                        usersListView.setAdapter(ada);

                    }
                    else {
                        ToastMaker.Show(1,"no users",act);
                    }
                }
                else {
                    ToastMaker.Show(1,"no users",act);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        });

        Volley.newRequestQueue(act).add(request);
    }

    public void openAddUsersDialog(View view) {
        NotAddedUsers = MyApp.EMPS ;

        selectedList = new ArrayList<USER>();
        for (int i=0;i<MyApp.EMPS.size();i++) {

            for (int j=0;j<THE_USERS.size();j++) {

                if (MyApp.EMPS.get(i).id == THE_USERS.get(j).id ) {

                    NotAddedUsers.remove(i);
                }
            }
        }
        //ToastMaker.Show(1,NotAddedUsers.size()+" "+MyApp.EMPS.size(),act);
        Dialog d = new Dialog(act);
        d.setContentView(R.layout.dialog_add_user_to_bylocation_dialog);
        ListView LV = (ListView) d.findViewById(R.id.notAddedEmps);
        ListView SLV = (ListView) d.findViewById(R.id.selectedEmps);
        Button add = (Button) d.findViewById(R.id.dialogAdd);
        addUsersToByLocationDialog_Adapter  adapter = new addUsersToByLocationDialog_Adapter(NotAddedUsers,act);
        addUsersToByLocationDialog_Adapter  Sadapter = new addUsersToByLocationDialog_Adapter(selectedList,act);
        LV.setAdapter(adapter);
        SLV.setAdapter(Sadapter);
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean s = false ;
                int ind = 0 ;
                USER U = NotAddedUsers.get(position) ;

                for (int i=0;i< selectedList.size();i++) {
                    //ToastMaker.Show(0,id+" ",act);
                    if (selectedList.get(i).JobNumber == U.JobNumber) {
                        s = true ;
                        ind = i ;
                    }
                }
                if (s) {
                    selectedList.remove(ind);
                    Sadapter.notifyDataSetChanged();
                    //view.setBackgroundColor(getResources().getColor(R.color.purple_700,null));
                }
                else {
                    selectedList.add(NotAddedUsers.get(position));
                    Sadapter.notifyDataSetChanged();
                    //view.setBackgroundColor(Color.DKGRAY);
                    //finalAdapter.getView(position,view,parent).setBackgroundColor(Color.DKGRAY);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                THE_USERS.addAll(selectedList);
                String[] arr = new String[THE_USERS.size()];
                for (int i=0;i<THE_USERS.size();i++) {
                    arr[i] = THE_USERS.get(i).FirstName+" "+THE_USERS.get(i).LastName ;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,R.layout.spinner_item,arr);
                usersListView.setAdapter(adapter);
                d.dismiss();
            }
        });
        d.show();
    }

    public void saveUsers(View view) {

        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, saveUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                //ToastMaker.Show(1,response,act);
                getUsers();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.saved),getResources().getString(R.string.saved));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.default_error_msg),"");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("count" , String.valueOf(THE_USERS.size()));
                for (int i=0 ; i < THE_USERS.size(); i++ ) {
                    par.put(("Eid"+i ), String.valueOf( THE_USERS.get(i).id ));
                    par.put(("Jn"+i) , String.valueOf(THE_USERS.get(i).JobNumber));
                    Log.d("saveProblem",THE_USERS.get(i).id+" "+THE_USERS.get(i).JobNumber);
                }
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request) ;
    }

    public void goToSetLocation(View view) {
        Intent i = new Intent(act,SelectLocation.class);
        startActivity(i);
    }
}