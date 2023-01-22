 package com.syrsoft.ratcoms.HRActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.syrsoft.ratcoms.VolleyCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

 public class Vacation extends AppCompatActivity {

    private Activity act ;
    private TextView StartDate , EndDate , Balance , fileName;
    private EditText NumberOfDays , Notes , VacationLocation;
    private RadioButton Annual , Sick , Emergency ;
    private Spinner Alternative , Location ;
    private String[] Locations , alternativesArray ;
    private ArrayAdapter<String> forlocations , foralternatives ;
    private String getAlternativesUrl = "https://ratco-solutions.com/RatcoManagementSystem/getVacationAlternatives.php";
    private String sendVacaton = "https://ratco-solutions.com/RatcoManagementSystem/insertVacation.php" ;
    private String getDirectManagerUrl = "https://ratco-solutions.com/RatcoManagementSystem/getDirectManager.php" ;
    private String getMeUrl = "https://ratco-solutions.com/RatcoManagementSystem/getMe.php";
    private String insertAttachment = "https://ratco-solutions.com/RatcoManagementSystem/insertAttachment.php";
    private List<USER> Alternatives ;
    private CalendarView StartDateCalender ;
    private String VACATION_TYPE ;
    private int VTYPE ;
    private USER manager , me ;
    private String DirectManagerText ="";
    private LinearLayout attachFileLayout ;
    private Button attach ;
    private int ATTACHFILE_REQCODE = 10 , CAM_REQCODE = 11 , CAM_PERMISSION_REQCODE = 5 ;
    Random r = new Random();
    int x  ,VCATION_NUMBER;
    private ImageView image ;
    String ConvertedImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation);
        setActivity();
        getAlternatives();
    }

    void setActivity(){
        act = this ;
        attachFileLayout = (LinearLayout) findViewById(R.id.attachFile_layout);
        attach = (Button)findViewById(R.id.attach);
        image = (ImageView) findViewById(R.id.attachment_image);
        fileName = (TextView) findViewById(R.id.textView33);
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
                selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
                selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
                selectDialog.setNegativeButton(getResources().getString(R.string.cam), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                        {
                            ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},CAM_PERMISSION_REQCODE);
                        }
                        else {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                startActivityForResult(takePictureIntent, CAM_REQCODE);
                            } catch (ActivityNotFoundException e) {
                                // display error state to the user
                            }

                        }
                    }
                });
                selectDialog.setPositiveButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                        open.setType("image/*");
                        startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE);
                    }
                });
                selectDialog.create().show();
            }
        });
        Alternatives = new ArrayList<USER>();
        StartDate = (TextView) findViewById(R.id.Backtowork_selectedStartDate);
        EndDate = (TextView) findViewById(R.id.Vacation_selectedEndDate);
        Balance = (TextView) findViewById(R.id.Vacation_vacationBalance);
        NumberOfDays = (EditText) findViewById(R.id.Backtowork_EndDate);
        Notes = (EditText) findViewById(R.id.Vacation_Notes);
        VacationLocation = (EditText) findViewById(R.id.Vacation_LocationText);
        Annual = (RadioButton) findViewById(R.id.Vacation_annual);
        Emergency = (RadioButton) findViewById(R.id.Vacation_emergency);
        Sick=(RadioButton)findViewById(R.id.Vacation_sick);
        Alternative = (Spinner) findViewById(R.id.alternative_spinnar);
        Location = (Spinner)findViewById(R.id.vacationLocation_spinnar);
        Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Location.getSelectedItem().toString().equals(getResources().getString(R.string.abroad))){
                    VacationLocation.setVisibility(View.VISIBLE);
                }
                else{
                    VacationLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        StartDateCalender = (CalendarView) findViewById(R.id.Backtowork_backDate);
        Locations =new String[] {"",getResources().getString(R.string.local) , getResources().getString(R.string.abroad)};
        forlocations = new ArrayAdapter<String>(act,R.layout.spinner_item,Locations);
        Location.setAdapter(forlocations);
        attachFileLayout.setVisibility(View.GONE);
        Annual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Emergency.setChecked(false);
                    Sick.setChecked(false);
                    VACATION_TYPE = "Annual";
                    Balance.setText(String.valueOf(me.VacationDays));
                    attachFileLayout.setVisibility(View.GONE);
                }
            }
        });
        Emergency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Annual.setChecked(false);
                    Sick.setChecked(false);
                    VACATION_TYPE = "Emergency";
                    Balance.setText(String.valueOf(me.EmergencyDays));
                    attachFileLayout.setVisibility(View.GONE);
                }
            }
        });
        Sick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Annual.setChecked(false);
                    Emergency.setChecked(false);
                    VACATION_TYPE = "Sick";
                    Balance.setText(String.valueOf(me.SickDays));
                    attachFileLayout.setVisibility(View.GONE);
                }
            }
        });
        StartDateCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if (NumberOfDays.getText() != null  && !NumberOfDays.getText().toString().isEmpty()){
                    try {
                        StartDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(StartDate.getText().toString());
                        Calendar c = Calendar.getInstance();
                        c.setTime(end);
                        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(NumberOfDays.getText().toString()));
                        EndDate.setText(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Write Number Of Days","Please Write The Number Of Days");
                }

            }
        });
        getMe();
        getDirectManager();
    }

    void getAlternatives(){

        Loading d = new Loading(act);d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getAlternativesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                if (!response.equals("0")){
                    List<Object> list = JsonToObject.translate(response, USER.class,act);
                    if (list.size()>0){
                        alternativesArray = new String[list.size()+1];
                        alternativesArray[0]="";
                        for (int i=0;i<list.size();i++){
                            USER u =(USER) list.get(i);
                            Alternatives.add(u);
                            alternativesArray[i+1] = Alternatives.get(i).FirstName+" "+Alternatives.get(i).LastName;
                        }
                        foralternatives = new ArrayAdapter<String>(act,R.layout.spinner_item,alternativesArray);
                        Alternative.setAdapter(foralternatives);
                    }

                }
                else{
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("DirectManager" , String.valueOf(MyApp.db.getUser().DirectManager));
                par.put("jn" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getMe () {

        Loading d = new Loading(act);d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getMeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                if (response != null) {
                    List<Object> list = JsonToObject.translate(response, USER.class,act);
                    me = (USER) list.get(0);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("jn" , String.valueOf(MyApp.db.getUser().JobNumber));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    void getDirectManager(){
        Loading d = new Loading(act);
        d.show();
        StringRequest request = new StringRequest(Request.Method.POST, getDirectManagerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d.close();
                Log.d("getDirectManager",response);
                if (response != null){
                    List<Object> list = JsonToObject.translate(response, USER.class,act);
                    manager = (USER) list.get(0);
                    DirectManagerText = manager.FirstName+" "+manager.LastName ;
                    //directManager.setText(DirectManagerText);
                    Log.d("getDirectManager",manager.Department);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> par = new HashMap<String, String>();
                par.put("jn" , String.valueOf(MyApp.db.getUser().DirectManager));
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    public void sendVacation(View view) {

        if (VACATION_TYPE == null || VACATION_TYPE.isEmpty()){
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.VacationTypeTitle) , getResources().getString(R.string.VacationTypeMessage));
            return;
        }
        else {
            if (VACATION_TYPE.equals("Annual")){
                VTYPE = 0 ;
            }
            else if (VACATION_TYPE.equals("Emergency")){
                VTYPE = 1 ;
            }
            else if (VACATION_TYPE.equals("Sick")){
                VTYPE = 2 ;
            }
        }
        if (NumberOfDays.getText() == null || NumberOfDays.getText().toString().isEmpty()){
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.daysNumberTitle) , getResources().getString(R.string.daysNumberMessage));
            return;
        }
//        if ( Double.parseDouble(NumberOfDays.getText().toString()) >= Double.parseDouble(Balance.getText().toString()) ) {
//            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.daysNumberErrorTitle) , getResources().getString(R.string.daysNumberErrorMessage));
//            return;
//        }
        if ( StartDate.getText() == null || StartDate.getText().toString().isEmpty() || EndDate.getText() == null || EndDate.getText().toString().isEmpty() ){
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.selectStartDateTitle) , getResources().getString(R.string.selectStartDateMessage));
            return;
        }
        if (Alternative.getSelectedItem() == null || Alternative.getSelectedItem().toString().isEmpty() ){
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.selectalternativeTitle) , getResources().getString(R.string.selectalternativeMessage));
            return;
        }
        if (Location.getSelectedItem() == null || Location.getSelectedItem().toString().isEmpty()){
            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.selectLocationTitle) , getResources().getString(R.string.selectLocationMessage));
            return;
        }
        Loading d = new Loading(act) ; d.show();
        StringRequest request = new StringRequest(Request.Method.POST, sendVacaton, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Log.d("vacation" , response);
                d.close();
                if ( Integer.parseInt(response) > 0) {
                    VCATION_NUMBER = Integer.parseInt(response);
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.vacationSent),getResources().getString(R.string.vacationSent) , 0);
                    MyApp.sendNotificationsToGroup(MyApp.VacationsAuthUsers, getResources().getString(R.string.vacation), getResources().getString(R.string.vacation), MyApp.db.getUser().FirstName + " " + MyApp.db.getUser().LastName, MyApp.db.getUser().JobNumber, "NewLeave", act, new VolleyCallback() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                d.close();
                Log.e("vacation" , error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar c = Calendar.getInstance(Locale.getDefault());
                String Date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                Log.e("vacation" , Alternative.getSelectedItemPosition()+"");
                Map<String,String> par = new HashMap<String, String>();
                par.put("EmpID" , String.valueOf( MyApp.db.getUser().id ));
                par.put("JobNumber" , String.valueOf(MyApp.db.getUser().JobNumber));
                par.put("FName" , MyApp.db.getUser().FirstName);
                par.put("LName" , MyApp.db.getUser().LastName);
                par.put("DirectManager" ,String.valueOf(MyApp.db.getUser().DirectManager));
                par.put("DirectManagerName" , DirectManagerText);
                par.put("JobTitle" , MyApp.db.getUser().JobTitle);
                par.put("VacationType" , String.valueOf( VTYPE ));
                par.put("SendDate" , Date);
                par.put("StartDate" , StartDate.getText().toString());
                par.put("VacationDays" , NumberOfDays.getText().toString());
                par.put("EndDate" , EndDate.getText().toString());
                par.put("AlternativeID" , String.valueOf( Alternatives.get( Alternative.getSelectedItemPosition()-1).id ));
                par.put("AlternativeName" ,Alternatives.get( Alternative.getSelectedItemPosition()-1).FirstName+" "+Alternatives.get( Alternative.getSelectedItemPosition()-1).LastName );
                par.put("Location" , Location.getSelectedItem().toString());
                par.put("Notes" , Notes.getText().toString());
                return par;
            }
        };
        Volley.newRequestQueue(act).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null ){
            return;
        }
        if (requestCode == ATTACHFILE_REQCODE){
            x = r.nextInt(10000);
            if (resultCode == RESULT_OK)
            {
                if (data == null) {
                    //Display an error
                    return;
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    image.setImageBitmap(bmp);
                    fileName.setText(String.valueOf(x)+".jpg");
                    ConvertedImage = convertImageToBase64(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.d("path" , "error result "+data.getData().getPath());
            }

        }
        else if (requestCode == CAM_REQCODE)
        {
            x = r.nextInt(10000);
            if (resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                image.setImageBitmap(imageBitmap);
                fileName.setText(String.valueOf(x)+".jpg");
                ConvertedImage = convertImageToBase64(imageBitmap);
            }
            else
            {
                ToastMaker.Show(1,"error getting Image",act);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_PERMISSION_REQCODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAM_REQCODE);
            }
        }
    }

     public String convertImageToBase64(Bitmap b)
     {
         //b = BitmapFactory.decodeFile("/path/to/image.jpg");
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         b.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
         byte[] bb = baos.toByteArray();
         String encodedImage = Base64.encodeToString(bb, Base64.DEFAULT);
         return encodedImage ;
     }


}