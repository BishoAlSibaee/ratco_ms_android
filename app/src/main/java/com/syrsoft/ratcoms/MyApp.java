package com.syrsoft.ratcoms;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.syrsoft.ratcoms.HRActivities.HR_ORDER_TYPE;
import com.syrsoft.ratcoms.HRActivities.JobTitle;
import com.syrsoft.ratcoms.SALESActivities.AddNewClient;
import com.syrsoft.ratcoms.SALESActivities.Client;
import com.syrsoft.ratcoms.SALESActivities.ClientVisitReport;
import com.syrsoft.ratcoms.SALESActivities.ViewMyVisitDetailes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class MyApp extends Application {
    public static USERDataBase db  ;
    public static List<Activity> ActList = new ArrayList<Activity>();
    public static FirebaseDatabase database ;
    public static DatabaseReference RefUSERS ;
    public static DatabaseReference RefME ;
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    final static private String serverKey = "key=" + "AAAANacBb74:APA91bHRas6269tP9BLt2_qghgMf_UuiQPQfrP5KZscRwNX1MgsqdwF_rLlAcAKO2Bs-ZPaYWQE4c-TlFXgP7E4UnOEkzmIzLevwlYCuhusz4knqvZCxWfQ0AzfRM37eL6-V10l42QRh";
    final static private String contentType = "application/json";
    public static String token ;
    public static List<USER> EMPS ;
    public static USER DIRECT_MANAGER , DEPARTMENT_MANAGER;
    public static List<String> ManagersTokens = new ArrayList<String>() ;
    public static List<HR_ORDER_TYPE> Types ;
    public static List<JobTitle> PurchaseOrdersJobTitles,BonusAuthsJobTitles,PurchaseOrderJobTitles,ExitAuthsJobTitles,JobTitles,RequestCustodyAuthsJobTitles , ResignationsAuthsJobtitles , VacationsAuthJobtitles , BacksAuthJobtitles , AdvancePaymentsAuthJobtitles , VacationSalaryAuthJobtitles ;
    public static List<JobTitle> ARequestCustodyAuthsJobTitles , AResignationsAuthsJobtitles , AVacationsAuthJobtitles , ABacksAuthJobtitles , AAdvancePaymentsAuthJobtitles , AVacationSalaryAuthJobtitles ;
    public static List<USER> BonusAuthUsers,PurchaseAuthUsers,ExitAuthUsers,CustodyAuthUsers , ResignationsAuthUsers , VacationsAuthUsers , BacksAuthUsers , AdvancePaymentaAuthUsers , VacationSalaryAuthUsers ;
    //public static List<USER> ACustodyAuthUsers , AResignationsAuthUsers , AVacationsAuthUsers , ABacksAuthUsers , AAdvancePaymentaAuthUsers , AVacationSalaryAuthUsers ;
    public static Application app ;
    public static String MainUrl = "https://ratco-solutions.com/RatcoManagementSystem/" ;
    private static String sendErrorUrl ;
    private static String saveImageUrl  ;
    private static String saveLinkToTable ,insertLinkToTable;
    private static String saveLinkToTableAndField , saveFileToServerUrl ;
    public static ADSDatabase ADS_DB ;
    public static int ADS_Counter = 0 ;
    public static int RatingCounter = 0, TempRatingCounter = 0 ;
    public static int SiteVisitOrdersCounter = 0 , ProjectsCounter = 0 , HRCounter = 0 , MYApprovalsCounter = 0 ,MaintenanceCounter=0 ,PurchaseOrdersCounter=0 ;
    public static boolean ManagerStatus = false ;
    public static List<List<USER>> PurchaseOrdersAuthUsers,BonusOrdersAuthUsers,VacationOrdersAuthUsers,ResignationOrdersAuthUsers,BacksOrdersAuthUsers,AdvancesOrdersAuthUsers,VacationSalaryOrdersAuthUsers,CustodyOrdersAuthUsers,ExitOrdersAuthUsers ;
    static String upload_URL ;
    static RequestQueue Q ;
    public static USER MyUser ;
    public static USER SELECTED_USER ;
    public static int[] ProjectsCounters ;




    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("appLife" , "onCreate");
        app = this ;
        db = new USERDataBase(app);
        db.createCountersTble();
        ADS_DB = new ADSDatabase(app);
        FirebaseApp.initializeApp(app);
        database = FirebaseDatabase.getInstance();
        RefUSERS = MyApp.database.getReference("USERS");
        Q = Volley.newRequestQueue(app);
        EMPS = new ArrayList<>();
        sendErrorUrl = MainUrl+"insertError.php";
        saveImageUrl = MainUrl+"insertPhotoToFolderAndTable.php" ;
        saveLinkToTable = MainUrl+"updateLinkField.php";
        saveLinkToTableAndField = MainUrl+ "updateTableLinkField.php" ;
        saveFileToServerUrl = MainUrl+"insertFile.php";
        insertLinkToTable = MainUrl+"insertLinkToTable.php" ;
        upload_URL = MainUrl + "insertFile.php";
        ResignationsAuthsJobtitles = new ArrayList<>();
        VacationsAuthJobtitles = new ArrayList<>();
        BacksAuthJobtitles = new ArrayList<>();
        AdvancePaymentsAuthJobtitles = new ArrayList<>();
        VacationSalaryAuthJobtitles = new ArrayList<>();
        RequestCustodyAuthsJobTitles = new ArrayList<>();
        PurchaseOrdersJobTitles = new ArrayList<>();
        ExitAuthsJobTitles = new ArrayList<>();
        BonusAuthsJobTitles = new ArrayList<>();
        ProjectsCounters = new int[] {0,0,0}; // MaintenanceOrders , SiteVisitOrders
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("appLife" , "onTerminate");
    }

    public static void  CloudMessage(String Title , String Message , String Name , int JobNumber , String token , String order , Context co){

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", Title);
            notifcationBody.put("message", Message);
            notifcationBody.put("Name", Name);
            notifcationBody.put("JobNumber", JobNumber);
            notifcationBody.put("order", order);
            notification.put("to", token); //registration_ids
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_MESSAGE_URL, notification,
                response -> Log.d("messageresponse" , response.toString()),
                error -> {
                    Log.d("messageresponse" , error.toString());
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        Volley.newRequestQueue(co).add(jsonObjectRequest);
    }

    public static void  CloudMessageForAdWithImage(String Title , String Message , String img , int JobNumber , String token , String order , Context co){

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", Title);
            notifcationBody.put("message", Message);
            notifcationBody.put("img", img);
            notifcationBody.put("JobNumber", JobNumber);
            notifcationBody.put("order", order);
            notification.put("to", token);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_MESSAGE_URL, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //ToastMaker.Show(1,"message sent" , act);
                        Log.d("messageresponse" , response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // ToastMaker.Show(1,error.getMessage() , act);
                        Log.d("messageresponse" , error.getMessage());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        Volley.newRequestQueue(co).add(jsonObjectRequest);
    }

    public static Bitmap convertBase64ToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static String convertImageToBase64(Bitmap b) {
        //b = BitmapFactory.decodeFile("/path/to/image.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
        byte[] bb = baos.toByteArray();
        String encodedImage = Base64.encodeToString(bb, Base64.DEFAULT);
        return encodedImage ;
    }

    public static void sendNotificationsToGroup(List<USER> list , String Title , String Message , String Name , int JobNumber , String order , Context co,VolleyCallback callback ) {
        for (int i=0;i<list.size();i++) {
            Log.d("destinationTokens" , list.get(i).Token );
            CloudMessage(Title,Message,Name,JobNumber,list.get(i).Token,order,co);
        }
        callback.onSuccess();
    }

    public static void savePhoto ( Bitmap bitmap , String Table , int ID , int source , String sourceImage ) {

        Log.d("saveImageResponse" , "save image started");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, saveImageUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("saveImageResponse" , new String(response.data)+"response");
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getString("status").equals("1")) {
                                Toast.makeText(app, "Image Saved", Toast.LENGTH_SHORT).show();
                                String Link = "https://ratco-solutions.com/RatcoManagementSystem/images/"+obj.getString("file_name");
                                setLinkInTable(Table,ID,Link);
                                if(source == 0 ) {
                                    if (sourceImage.equals("C")) {
                                        ClientVisitReport.setCardImageNull();
                                    }
                                    else if (sourceImage.equals("Q")) {
                                        ClientVisitReport.setQuotationImageNull();
                                    }
                                    else if (sourceImage.equals("L")) {
                                        ClientVisitReport.setLocationImageNull();
                                    }
                                }
                                else if (source ==1) {
                                    if (sourceImage.equals("C")){
                                        AddNewClient.setCardImageNull();
                                    }
                                    else if (sourceImage.equals("Q")) {
                                        AddNewClient.setQuotationImageNull();
                                    }
                                    else if (sourceImage.equals("L")) {
                                        AddNewClient.setLocationImageNull();
                                    }

                                }
                                else if (source == 2) {
                                    Client.B = null ;
                                }

                            }
                            else {
                                ToastMaker.Show(0,"image not saved",app);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("saveImageResponse" , e.getMessage()+"response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(app, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("saveImageResponse" , error.getMessage()+"error");
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(app).add(volleyMultipartRequest);
    }

    public static void savePhoto ( Bitmap bitmap , String Table , int ID , String Field , int source , String sourceImage ) {

        Log.d("saveImageResponse" , "save image started");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, saveImageUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("saveImageResponse" , new String(response.data)+"response");
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getString("status").equals("1")) {
                                Toast.makeText(app, "Image Saved", Toast.LENGTH_SHORT).show();
                                String Link = "https://ratco-solutions.com/RatcoManagementSystem/images/"+obj.getString("file_name");
                                setLinkInTable(Table,ID,Link,Field);
                                if(source == 0 ) {
                                    if (sourceImage.equals("C")) {
                                        ClientVisitReport.setCardImageNull();
                                    }
                                    else if (sourceImage.equals("Q")) {
                                        ClientVisitReport.setQuotationImageNull();
                                    }
                                    else if (sourceImage.equals("L")) {
                                        ClientVisitReport.setLocationImageNull();
                                    }
                                }
                                else if (source ==1) {
                                    if (sourceImage.equals("C")){
                                        AddNewClient.setCardImageNull();
                                    }
                                    else if (sourceImage.equals("Q")) {
                                        AddNewClient.setQuotationImageNull();
                                    }
                                    else if (sourceImage.equals("L")) {
                                        AddNewClient.setLocationImageNull();
                                    }
                                }
                                else if (source == 3) {
                                    if (sourceImage.equals("Q")) {
                                        ViewMyVisitDetailes.QuotationBitmap = null;
                                    }
                                    else if (sourceImage.equals("L")) {
                                        ViewMyVisitDetailes.ClientLocation = null;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("saveImageResponse" , e.getMessage()+"response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(app, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("saveImageResponse" , error.getMessage()+"error");
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(app).add(volleyMultipartRequest);

    }

    public static void savePhoto ( Bitmap bitmap , String Table , int ID ,String Field ) {

        Log.d("saveImageResponse" , "save image started");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, saveImageUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("saveImageResponse" , new String(response.data)+"response");
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getString("status").equals("1")) {
                                Toast.makeText(app, "Image Saved", Toast.LENGTH_SHORT).show();
                                String Link = "https://ratco-solutions.com/RatcoManagementSystem/images/"+obj.getString("file_name");
                                insertLinkInTable(Table,ID,Link,Field);
//                                if(source == 0 ) {
//                                    if (sourceImage.equals("C")) {
//                                        ClientVisitReport.setCardImageNull();
//                                    }
//                                    else if (sourceImage.equals("Q")) {
//                                        ClientVisitReport.setQuotationImageNull();
//                                    }
//                                    else if (sourceImage.equals("L")) {
//                                        ClientVisitReport.setLocationImageNull();
//                                    }
//                                }
//                                else if (source ==1) {
//                                    if (sourceImage.equals("C")){
//                                        AddNewClient.setCardImageNull();
//                                    }
//                                    else if (sourceImage.equals("Q")) {
//                                        AddNewClient.setQuotationImageNull();
//                                    }
//                                    else if (sourceImage.equals("L")) {
//                                        AddNewClient.setLocationImageNull();
//                                    }
//
//                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("saveImageResponse" , e.getMessage()+"response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(app, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("saveImageResponse" , error.getMessage()+"error");
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(app).add(volleyMultipartRequest);
    }

    public static void savePhoto ( Bitmap bitmap , VollyCallback callback ) {
        Log.d("saveImageResponse" , "save image started");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, saveImageUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("saveImageResponse" , new String(response.data)+"response");
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getString("status").equals("1")) {
                                Toast.makeText(app, "Image Saved", Toast.LENGTH_SHORT).show();
                                String Link = "https://ratco-solutions.com/RatcoManagementSystem/images/"+obj.getString("file_name");
                                callback.onSuccess(Link);
                            }
                            else {
                                callback.onSuccess("0");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("saveImageResponse" , e.getMessage()+"response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(app, error.getMessage(), Toast.LENGTH_LONG).show();
                        //Log.d("saveImageResponse" , error.getMessage()+"error");
                        callback.onFailed(error.toString());
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(app).add(volleyMultipartRequest);
    }

    public static void savePhotoForAd ( Bitmap bitmap , String title , String message , Activity act ) {

        Log.d("saveImageResponse" , "save image started");
        Loading l = new Loading(act); l.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, saveImageUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        l.close();
                        Log.d("saveImageResponse" , new String(response.data)+"response");
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getString("status").equals("1")) {
                                Toast.makeText(app, "Image Saved", Toast.LENGTH_SHORT).show();
                                String Link = "https://ratco-solutions.com/RatcoManagementSystem/images/"+obj.getString("file_name");
                                Random r = new Random();
                                int x = r.nextInt(10000);
                                for (USER u : EMPS) {
                                        CloudMessageForAdWithImage(title,message,Link,x,u.Token,"AD",app);
                                }
                                act.finish();
                                //MyApp.sendNotificationsToGroup(MyApp.EMPS,title,message,"",x,"AD",app);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("saveImageResponse" , e.getMessage()+"response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        l.close();
                        Toast.makeText(app, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("saveImageResponse" , error.getMessage()+"error");
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(app).add(volleyMultipartRequest);
    }

    public static void insertLinkInTable( String Table , int ID , String Link ,String Field) {

        StringRequest request = new StringRequest(Request.Method.POST, insertLinkToTable, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saveImageResponse" , response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("saveImageResponse" , error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Table", Table);
                par.put("ID" , String.valueOf(ID));
                par.put("Link" , Link );
                par.put("Field" , Field);
                return par;
            }
        };
        Volley.newRequestQueue(app).add(request);
    }

    public static void setLinkInTable( String Table , int ID , String Link , String Field ) {
        StringRequest request = new StringRequest(Request.Method.POST, saveLinkToTableAndField, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saveImageResponse" , response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("saveImageResponse" , error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Table", Table);
                par.put("ID" , String.valueOf(ID));
                par.put("Link" , Link );
                par.put("Field" , Field );
                return par;
            }
        };
        Volley.newRequestQueue(app).add(request);
    }

    public static void setLinkInTable( String Table , int ID , String Link ) {

        StringRequest request = new StringRequest(Request.Method.POST, saveLinkToTable, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saveImageResponse" , response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("saveImageResponse" , error.getMessage());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Table", Table);
                par.put("ID" , String.valueOf(ID));
                par.put("Link" , Link );
                return par;
            }
        };
        Volley.newRequestQueue(app).add(request);
    }

    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public  static void sendError (String error,String activity , String user,String method) {

        if (error != null || !error.isEmpty()) {
            StringRequest request = new StringRequest(Request.Method.POST, sendErrorUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("ErrorSaveResponse" , response);
                    if (response.equals("1")) {
                        for(USER u : MyApp.EMPS) {
                            if (u.JobTitle.equals("Programmer")){
                                CloudMessage(activity, method ,user ,0,u.Token,"Error",app);
                            }
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Calendar c = Calendar.getInstance(Locale.getDefault());
                    String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                    String Time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                    Map<String,String> par = new HashMap<String, String>();
                    par.put("Error",error);
                    par.put("Activity",activity);
                    par.put("User",user);
                    par.put("MethodName",method);
                    par.put("Date",Date);
                    par.put("Time",Time);
                    return par;
                }
            };
            Volley.newRequestQueue(app).add(request);
        }
    }

    public static void logOut(int ID) {
        StringRequest request = new StringRequest(Request.Method.POST, MainUrl + "eraseToken.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("logOutResp", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("logOutResp", error.getMessage()+" from here "+String.valueOf(db.getUser().id));
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Log.d("logOutResp", String.valueOf(db.getUser().id));
                Map<String,String> par = new HashMap<String, String>();
                par.put("ID",String.valueOf(ID));
                return par;
            }
        };
        Volley.newRequestQueue(app).add(request);
    }

    public static void uploadPDF (final String pdfname,Uri pdffile, VollyCallback callback) {

        InputStream iStream = null;
        try {
            iStream = app.getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo", new String(response.data));
                            Q.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                String res = jsonObject.getString("status");
                                if (res.equals("1")) {
                                    String message = jsonObject.getString("message") ;
                                    message.replace("\\\\", "");
                                    callback.onSuccess(message);
                                }
                                else {
                                    String message = jsonObject.getString("error") ;
                                    callback.onSuccess("0");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) {
                                callback.onSuccess(error.toString());
                            }
                            else {
                                callback.onSuccess("error saving file ");
                            }
                        }
                    }) {
                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("image", new DataPart(pdfname, inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Q.add(volleyMultipartRequest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastMaker.Show(1,e.getMessage(),app);
            Log.d("ressssssoo", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            ToastMaker.Show(1,e.getMessage(),app);
            Log.d("ressssssoo", e.getMessage());
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static boolean BinarySearch(int[] arr , int search) {
        int l = 0 ;
        int h = arr.length-1;
        boolean isFound = false ;

        while (!isFound) {
            if (l > h ) {
                isFound = false ;
                break;
            }
            int m = l + ((h-l) / 2) ;
            if (arr[m] == search) {
                isFound = true ;
            }
            if (search > arr[m]) {
                l = m+1 ;
            }
            if (search< arr[m]) {
                h = m-1 ;
            }
        }

        return isFound ;
    }

    public static String getStringMonth (int month) {
        String res = "";
        switch (month) {
            case 0:
                res = "Jan" ;
            case 1:
                res = "Feb" ;
            case 2:
                res = "Mar" ;
            case 3:
                res = "Apr" ;
            case 4:
                res = "May" ;
            case 5:
                res = "Jun" ;
            case 6:
                res = "Jul" ;
            case 7:
                res = "Aug" ;
            case 8:
                res = "Sep" ;
            case 9:
                res = "Oct" ;
            case 10:
                res = "Nov" ;
            case 11:
                res = "Dec" ;
        }
        return res ;
    }

    public static String getNameSalesMan(int salesId) {
        String na = null;
        for (int i = 0 ;i<EMPS.size();i++){
            if (EMPS.get(i).JobNumber == salesId) {
                na =EMPS.get(i).FirstName + " " + EMPS.get(i).LastName;
            }
        }
        return na;
    }

}

