package com.syrsoft.ratcoms.SALESActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.SALES_ADAPTERS.Items_Adapter;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewContract extends AppCompatActivity {

    Activity act ;
    TextView ClientNameTV , ResponsibleTV , ProjectNameTV , ProjectDescTV , ProjectResponsibleTV , ResponsibleMobileTV , ContractDateTV ,SupplyTV,InstallTV,HandoverTV,warranty,warrantyDate ;
    TextView DeliveryLocationTV,AvailabilityTV,Payment1TV,Payment1TVtext,Payment2TV,Payment2TVtext,Payment3TV,Payment3TVtext,Payment4TV,Payment4TVtext,InstallationTV,WarrantyTV ;
    LinearLayout ViewContractL , AttachFileL , ItemsLayout , TermsLayout ;
    Button ViewContractBtn , AttachFileBtn , ViewOnMapBtn ;
    int ContractID ;
    PROJECT_CONTRACT_CLASS CONTRACT ;
    String getContractTermsURL = MyApp.MainUrl + "getContractTermsAndConditions.php" ;
    String getContractItemsURL = MyApp.MainUrl + "getContractItems.php" ;
    String upload_URL = MyApp.MainUrl + "insertFile.php";
    String recordFileLinkInTableUrl = MyApp.MainUrl+"updateTableLinkField.php" ;
    TermsAndConditions TERMS ;
    RecyclerView ItemsRecycler ;
    RecyclerView.LayoutManager Manager ;
    List<CONTRACT_ITEMS_CLASS> ITEMS ;
    Items_Adapter items_adapter ;
    RequestQueue Q ;
    Uri  UriFile ;
    String FileName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_view_contract_activity);
        Bundle B = getIntent().getExtras();
        ContractID = B.getInt("ContractID");
        setActivity();
        getTheContract();
    }

    void setActivity() {
        act = this ;
        ClientNameTV = (TextView) findViewById(R.id.Contract_ClientName);
        ResponsibleTV = (TextView) findViewById(R.id.Contract_Responsible);
        ProjectNameTV = (TextView) findViewById(R.id.Contract_ProjectName);
        ProjectDescTV = (TextView) findViewById(R.id.Contract_ProjectDesc);
        ContractDateTV = (TextView) findViewById(R.id.Contract_ContractDate);
        SupplyTV = (TextView) findViewById(R.id.supply_text);
        InstallTV = (TextView) findViewById(R.id.install_text);
        HandoverTV = (TextView) findViewById(R.id.handover_text);
        ProjectResponsibleTV = (TextView) findViewById(R.id.Contract_ProjectResponsible);
        ResponsibleMobileTV = (TextView) findViewById(R.id.Contract_ProjectResponsibleMobile);
        DeliveryLocationTV = (TextView) findViewById(R.id.addCLient_deliveryLocationTV);
        AvailabilityTV = (TextView) findViewById(R.id.Contract_avaliabilityTV);
        Payment1TV = (TextView) findViewById(R.id.Contract_paymentED1);
        Payment1TVtext = (TextView) findViewById(R.id.Contract_paymentED1text);
        Payment2TV = (TextView) findViewById(R.id.Contract_paymentED2);
        Payment2TVtext = (TextView) findViewById(R.id.Contract_paymentED2text);
        Payment3TV = (TextView) findViewById(R.id.Contract_paymentED3);
        Payment3TVtext = (TextView) findViewById(R.id.Contract_paymentED3text);
        Payment4TV = (TextView) findViewById(R.id.Client_paymentED4);
        Payment4TVtext = (TextView) findViewById(R.id.Contract_paymentED4text);
        InstallationTV = (TextView) findViewById(R.id.Contract_installationET);
        WarrantyTV = (TextView) findViewById(R.id.Contract_warrantyET);
        ViewContractL = (LinearLayout) findViewById(R.id.viewContract_layout);
        AttachFileL = (LinearLayout) findViewById(R.id.attachContract_layout);
        ItemsLayout = (LinearLayout) findViewById(R.id.Items_layout);
        TermsLayout = (LinearLayout) findViewById(R.id.termsAndConditions_layout);
        ItemsRecycler = (RecyclerView) findViewById(R.id.Items_Recycler);
        warranty = (TextView) findViewById(R.id.warranty);
        warrantyDate = (TextView) findViewById(R.id.warrantyDate);
        Manager = new LinearLayoutManager(act,RecyclerView.VERTICAL,false);
        ItemsRecycler.setLayoutManager(Manager);
        TERMS = new TermsAndConditions() ;
        ITEMS = new ArrayList<CONTRACT_ITEMS_CLASS>();
        Q = Volley.newRequestQueue(act) ;
    }

    void setContractTexts() {
        ClientNameTV.setText(CONTRACT.getCLIENT().ClientName);
        ResponsibleTV.setText(CONTRACT.ProjectManager);
        ProjectNameTV.setText(CONTRACT.ProjectName);
        ProjectDescTV.setText(CONTRACT.ProjectDescription);
        ProjectResponsibleTV.setText(CONTRACT.ProjectManager);
        ResponsibleMobileTV.setText(CONTRACT.MobileNumber);
        ContractDateTV.setText(CONTRACT.Date);
        warrantyDate.setText(CONTRACT.WarrantyExpireDate);
        warranty.setText(CONTRACT.getWarranty());
        SupplyTV.setText(CONTRACT.getSupplied());
        InstallTV.setText(CONTRACT.getInstalled());
        HandoverTV.setText(CONTRACT.getHandovered());
        if (CONTRACT.ContractLink == null || CONTRACT.ContractLink.isEmpty()) {
            ViewContractL.setVisibility(View.GONE);
            AttachFileL.setVisibility(View.VISIBLE);
        }
        else {
            ViewContractL.setVisibility(View.VISIBLE);
            AttachFileL.setVisibility(View.GONE);
        }
    }

    void getTheContract() {
        if (ViewMySalesProjectContracts.ContractsList != null ) {
            for(PROJECT_CONTRACT_CLASS C :ViewMySalesProjectContracts.ContractsList) {
                if (C.id == ContractID) {
                    CONTRACT = C ;
                    setContractTexts();
                    getContractTerms();
                    getContractItems();
                    break;
                }
            }
        }

    }

    void getContractTerms() {
        Loading l = new Loading(act);
        l.show();
        StringRequest request = new StringRequest(Request.Method.POST, getContractTermsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getTermsResp" ,response);
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(0,"No Terms",act);
                    TermsLayout.setVisibility(View.GONE);
                }
                else if (response.equals("-1")) {

                }
                else {
                    TermsLayout.setVisibility(View.VISIBLE);
                    try {
                        JSONArray arr = new JSONArray(response);
                        TermsAndConditions T = new TermsAndConditions();
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            if (row.getString("Name").equals("DeliveryLocation")) {
                                T.setDeliveryLocation(row.getString("Term"));
                            }
                            else if (row.getString("Name").equals("Availability")) {
                                T.setAvailability(row.getString("Term"));
                            }
                            else if (row.getString("Name").equals("Installation")) {
                                T.setInstallation(row.getString("Term"));
                            }
                            else if (row.getString("Name").equals("Warranty")) {
                                T.setWarranty(row.getString("Term"));
                            }
                            else if (row.getString("Name").equals("Payment")) {
                                String TT = row.getString("Term");
                                String arrT[] = TT.split("-");
                                List<PaymentTerms> paymentTerms = new ArrayList<PaymentTerms>();
                                if ( arrT.length > 0 ) {
                                    Log.d("paymentarrlength" ,arrT.length+"");
                                    for (String s : arrT){
                                        Log.d("paymentarrlength" ,s);
                                        String[] XX = s.split("%");
                                        if (XX.length == 2) {
                                            paymentTerms.add(new PaymentTerms(XX[0],XX[1]));
                                        }
                                    }
//                                    if (arrT.length >= 2 ) {
//                                        paymentTerms.add(new PaymentTerms(arrT[0],arrT[1]));
//                                    }
//                                    if (arrT.length >= 4) {
//                                        paymentTerms.add(new PaymentTerms(arrT[2],arrT[3]));
//                                    }
//                                    if (arrT.length >= 6) {
//                                        paymentTerms.add(new PaymentTerms(arrT[4],arrT[5]));
//                                    }
//                                    if (arrT.length >= 8) {
//                                        paymentTerms.add(new PaymentTerms(arrT[6],arrT[7]));
//                                    }
                                }
                                T.setPayment(paymentTerms);
                            }
                        }
                        TERMS = T ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (TERMS != null ) {
                        if (TERMS.getDeliveryLocation() != null && !TERMS.getDeliveryLocation().isEmpty()) {
                            DeliveryLocationTV.setText(TERMS.getDeliveryLocation());
                        }
                        if (TERMS.getAvailability() != null && !TERMS.getAvailability().isEmpty()) {
                            AvailabilityTV.setText(TERMS.getAvailability());
                        }
                        if (TERMS.getInstallation() != null && !TERMS.getInstallation().isEmpty()) {
                            InstallationTV.setText(TERMS.getInstallation());
                        }
                        if (TERMS.getWarranty() != null && !TERMS.getWarranty().isEmpty()) {
                            WarrantyTV.setText(TERMS.getWarranty());
                        }
                        if (TERMS.getPayment() != null && TERMS.getPayment().size()>0) {
                            Payment1TV.setText(TERMS.getPayment().get(0).Percent);
                            Payment1TVtext.setText(TERMS.getPayment().get(0).Condition);
                            if (TERMS.getPayment().size() > 1) {
                                Payment2TV.setText(TERMS.getPayment().get(1).Percent);
                                Payment2TVtext.setText(TERMS.getPayment().get(1).Condition);
                            }
                            if (TERMS.getPayment().size() > 2) {
                                Payment3TV.setText(TERMS.getPayment().get(2).Percent);
                                Payment3TVtext.setText(TERMS.getPayment().get(2).Condition);
                            }
                            if (TERMS.getPayment().size() > 3) {
                                Payment4TV.setText(TERMS.getPayment().get(3).Percent);
                                Payment4TVtext.setText(TERMS.getPayment().get(3).Condition);
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
                Log.d("getTermsResp" ,error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("ContractID", String.valueOf(CONTRACT.id));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getContractItems() {

        Loading l = new Loading(act);
        l.show();
        ITEMS.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getContractItemsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.close();
                if (response.equals("0")) {
                    ToastMaker.Show(1,"No Items" ,act);
                    ItemsLayout.setVisibility(View.GONE);
                }
                else if (response.equals("-1")) {
                    ToastMaker.Show(1,"error getting Items" ,act);
                }
                else {
                    ItemsLayout.setVisibility(View.VISIBLE);
                    try {
                        JSONArray arr = new JSONArray(response) ;
                        for (int i=0;i<arr.length();i++) {
                            JSONObject row = arr.getJSONObject(i);
                            ITEMS.add(new CONTRACT_ITEMS_CLASS(row.getInt("id"),row.getInt("ProjectID"),row.getString("ItemName"),row.getInt("Quantity"),row.getDouble("Price")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    items_adapter = new Items_Adapter(ITEMS);
                    ItemsRecycler.setAdapter(items_adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.close();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("ContractID", String.valueOf(CONTRACT.id));
                return par;
            }
        };
        Q.add(request);
    }

    public void showOnMap(View view) {
        Intent i = new Intent(act, ShowVisitsOnMap.class);
        i.putExtra("LA",CONTRACT.LA);
        i.putExtra("LO" , CONTRACT.LO);
        i.putExtra("ClientName" , CONTRACT.ProjectName);
        startActivity(i);
    }

    public void viewContractFile(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CONTRACT.ContractLink));
        startActivity(browserIntent);
    }

    public void attachImageFile(View view) {
//        AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
//        selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
//        selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
//        selectDialog.setNegativeButton(getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
//                {
//                    ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},ClientVisitReport.CAM_PERMISSION_REQCODE);
//                }
//                else {
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    try {
//                        act.startActivityForResult(takePictureIntent, CAM_REQCODE);
//                    } catch (ActivityNotFoundException e) {
//                        // display error state to the user
//                    }
//
//                }
//            }
//        });
//        selectDialog.setPositiveButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent open = new Intent(Intent.ACTION_GET_CONTENT);
//                open.setType("image/*");
//                act.startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE);
//            }
//        });
//        selectDialog.create().show();

//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/pdf");
//        // Optionally, specify a URI for the file that should appear in the
//        // system file picker when it loads.
//        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
//        act.startActivityForResult(Intent.createChooser(intent,"select File"), ATTACHFILE_REQCODE);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        act.startActivityForResult(Intent.createChooser(intent, "select File"), 6);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 6) {
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
                                FileName = displayName;
                                Log.d("fileselection", displayName);
                                //uploadPDF(displayName,uri);
                                //FileNameTV.setText(FileName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uri.toString().startsWith("file://")) {
                        displayName = F.getName();
                        Log.d("fileselection", displayName);
                    }
                    AlertDialog.Builder  builder = new AlertDialog.Builder(act);
                    builder.setTitle("Save File ?");
                    builder.setMessage("Save This File as Contract Document ?");
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (UriFile != null && FileName != null ) {
                                uploadPDF(FileName,UriFile,CONTRACT.id);
                            }
                            else {
                                ToastMaker.Show(1,"please select file ",act);
                            }
                        }
                    });

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
    }

    private void uploadPDF(final String pdfname, Uri pdffile , int ID) {

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo", new String(response.data));
                            Q.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                jsonObject.toString().replace("\\\\", "");
                                recordFileLinkInTable(jsonObject.getString("message"),"Projects","ContractLink",ID);
//
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    // params.put("tags", "ccccc");  add string parameters
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
            Q = Volley.newRequestQueue(act);
            Q.add(volleyMultipartRequest);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    void recordFileLinkInTable(String Link , String Table , String Column , int ID) {

        StringRequest request = new StringRequest(Request.Method.POST, recordFileLinkInTableUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("insertIntoTable" , response);
                if (response.equals("1")) {
                    ToastMaker.Show(0,"file saved" ,act);
                }
                else if (response.equals("0")) {
                    ToastMaker.Show(0,"file not saved" ,act);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("insertIntoTable" , error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("Table",Table);
                par.put("ID",String.valueOf(ID));
                par.put("Link",Link);
                par.put("Field",Column);
                return par;
            }
        };
        Q.add(request);
    }

    public void shareLocation(View view) {
        String uri = "https://www.google.com/maps/?q=" + CONTRACT.LA+ "," +CONTRACT.LO ;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,  uri);
        startActivity(Intent.createChooser(sharingIntent, "Share in..."));
    }
}