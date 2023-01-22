package com.syrsoft.ratcoms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeMyPasswordDialog {

    Dialog D ;
    Context C ;
    private String LoginUrl = "https://ratco-solutions.com/RatcoManagementSystem/appLoginEmployees.php" ;
    private String setPasswordUrl = "https://ratco-solutions.com/RatcoManagementSystem/changeMyPassword.php" ;
    RequestQueue Q ;

    ChangeMyPasswordDialog(Context C){
        this.C = C ;
        D = new Dialog(C) ;
        Q = Volley.newRequestQueue(C);
        D.setContentView(R.layout.change_my_password_dialog);
        EditText OldPW = (EditText) D.findViewById(R.id.ChangePassword_old);
        EditText NewPW = (EditText) D.findViewById(R.id.ChangePassword_new);
        EditText ConfermNewPW = (EditText) D.findViewById(R.id.ChangePassword_Confermnew);
        Button cancel = (Button) D.findViewById(R.id.ChangePassword_cancel) ;
        Button send  = (Button) D.findViewById(R.id.ChangePassword_send);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OldPW.getText() == null || OldPW.getText().toString().isEmpty()) {
                    ToastMaker.Show(1,C.getResources().getString(R.string.enterOldPassword),C);
                    return;
                }
                if (NewPW.getText() == null || NewPW.getText().toString().isEmpty()) {
                    ToastMaker.Show(1,C.getResources().getString(R.string.enterNewPassword),C);
                    return;
                }
                if (ConfermNewPW.getText() == null || ConfermNewPW.getText().toString().isEmpty()) {
                    ToastMaker.Show(1,C.getResources().getString(R.string.reEnterNewPassword),C);
                    return;
                }
                if (!NewPW.getText().toString().equals(ConfermNewPW.getText().toString())) {
                    ToastMaker.Show(1,"please check new Password Confermation",C);
                    return;
                }
                Loading l = new Loading(C);
                l.show();
                StringRequest loginrequest = new StringRequest(Request.Method.POST, LoginUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        l.close();
                        Log.d("LoginResponse" , response);
                        if (response.equals("0")){
                            ToastMaker.Show(1,"Old Password is Wrong",C);
                        }
                        else
                        {
                            Loading l = new Loading(C);
                            l.show();
                            StringRequest request = new StringRequest(Request.Method.POST, setPasswordUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    l.close();
                                    if (response.equals("0")) {
                                        ToastMaker.Show(1,"error while process.. trye again",C);
                                    }
                                    else if (response.equals("1")) {
                                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(C,"Password Changed","Password Changed Successfully");
                                        D.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    l.close();
                                    ToastMaker.Show(1,"error while process.. trye again",C);
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String ,String> pars = new HashMap<String, String>();
                                    pars.put("id" , String.valueOf(MyApp.db.getUser().id));
                                    pars.put("Password" , NewPW.getText().toString());
                                    return pars;
                                }
                            };
                            Q.add(request);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //ToastMaker.Show(1,error.getMessage(),act);
                        l.close();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String ,String> pars = new HashMap<String, String>();
                        pars.put("user" , MyApp.db.getUser().User);
                        pars.put("password" , OldPW.getText().toString());
                        return pars;
                    }
                };
                Volley.newRequestQueue(C).add(loginrequest);
            }
        });
    }

    void show() {
        D.show();
    }
    void close() {
        D.dismiss();
    }
}
