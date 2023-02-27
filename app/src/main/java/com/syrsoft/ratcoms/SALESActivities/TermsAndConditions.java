package com.syrsoft.ratcoms.SALESActivities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.MyApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermsAndConditions {

    String DeliveryLocation ;
    String Availability ;
    String Installation ;
    String Warranty ;
    List<PaymentTerms> Payment ;

    public void setDeliveryLocation(String deliveryLocation) {
        DeliveryLocation = deliveryLocation;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public void setInstallation(String installation) {
        Installation = installation;
    }

    public void setWarranty(String warranty) {
        Warranty = warranty;
    }

    public void setPayment(List<PaymentTerms> payment) {
        Payment = payment;
    }

    public String getDeliveryLocation() {
        return DeliveryLocation;
    }

    public String getAvailability() {
        return Availability;
    }

    public String getInstallation() {
        return Installation;
    }

    public String getWarranty() {
        return Warranty;
    }

    public List<PaymentTerms> getPayment() {
        return Payment;
    }

    public static void getContractTerms(Context c,int ID,ContractTermsCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, MyApp.MainUrl + "getContractTermsAndConditions.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("0")) {
                    Log.d("getTermsResp", response);
                } else if (response.equals("-1")) {
                    Log.d("getTermsResp", response);
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
                        callback.onSuccess(T);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("getTermsResp", e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getTermsResp", error.toString());
                callback.onFailed();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<String, String>();
                par.put("ContractID", String.valueOf(ID));
                return par;
            }
        };
        Volley.newRequestQueue(c).add(request);
    }
}

interface ContractTermsCallback {
    void onSuccess(TermsAndConditions terms);
    void onFailed();
}

