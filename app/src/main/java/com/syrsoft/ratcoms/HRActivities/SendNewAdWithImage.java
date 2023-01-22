package com.syrsoft.ratcoms.HRActivities;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syrsoft.ratcoms.Loading;
import com.syrsoft.ratcoms.MESSAGE_DIALOG;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.SALESActivities.ClientVisitReport;
import com.syrsoft.ratcoms.ToastMaker;
import com.syrsoft.ratcoms.USER;
import com.syrsoft.ratcoms.VolleyCallback;
import com.syrsoft.ratcoms.VollyCallback;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SendNewAdWithImage extends AppCompatActivity {

    Activity act ;
    EditText title , message ;
    ImageView image ;
    Button attach ;
    int ATTACHFILE_REQCODE = 55 ;
    Bitmap IMAGE ;
    private String insertNewAdUrl = MyApp.MainUrl + "insertNewAd.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_new_ad_with_image_activity);
        setActivity();
    }

    void setActivity () {
        act = this;
        title = (EditText) findViewById(R.id.sendAdWithImage_title);
        message = (EditText) findViewById(R.id.sendAdWithImage_message);
        image = (ImageView) findViewById(R.id.attachment_image);
        attach = (Button) findViewById(R.id.sendAdWithImage_attach);
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder selectDialog = new AlertDialog.Builder(act);
                selectDialog.setTitle(getResources().getString(R.string.selectFileTitle));
                selectDialog.setMessage(getResources().getString(R.string.selectFileMessage));
                selectDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                selectDialog.setPositiveButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent open = new Intent(Intent.ACTION_GET_CONTENT);
                        open.setType("image/*");
                        act.startActivityForResult(Intent.createChooser(open,"select Image"),ATTACHFILE_REQCODE);
                    }
                });
                selectDialog.create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ATTACHFILE_REQCODE) {

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
                    IMAGE = bmp ;
                    image.setImageBitmap(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.d("path" , "error result "+data.getData().getPath());
            }
        }
    }

    public void sendAdWithImage(View view) {

        if (title.getText() == null || title.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.enterAdTitle),act);
            return;
        }
        if (message.getText() == null || message.getText().toString().isEmpty()) {
            ToastMaker.Show(1,getResources().getString(R.string.enterAdText),act);
            return;
        }
        if (IMAGE == null ) {
            ToastMaker.Show(1,"please select image",act);
            return;
        }
        Loading l = new Loading(act); l .show();
        MyApp.savePhoto(IMAGE, new VollyCallback() {
            @Override
            public void onSuccess(String s) {
                if (s.equals("0")) {
                    l.close();
                    MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error","Sending Message Failed ");
                }
                else {
                    StringRequest request = new StringRequest(Request.Method.POST, insertNewAdUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            l.close();
                            if (response.equals("1")) {
                                Random r = new Random();
                                int x = r.nextInt(10000);
                                MyApp.sendNotificationsToGroup(MyApp.EMPS, title.getText().toString(), message.getText().toString(), "", x, "AD", MyApp.app, new VolleyCallback() {
                                    @Override
                                    public void onSuccess() {
                                        MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,getResources().getString(R.string.sent),getResources().getString(R.string.sent));
                                    }
                                });
                            }
                            else {
                                MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error","Sending Message Failed ");
                            }
                        }
                    }
                            , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            l.close();
                            MESSAGE_DIALOG m = new MESSAGE_DIALOG(act,"Error","Sending Message Failed "+error.toString());
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> par = new HashMap<String,String>();
                            par.put("Title",title.getText().toString());
                            par.put("Message", message.getText().toString() );
                            par.put("ImageLink",s);
                            return par;
                        }
                    };
                    Volley.newRequestQueue(act).add(request);
                }

            }

            @Override
            public void onFailed(String error) {

            }
        });

        //MyApp.savePhotoForAd(IMAGE,title.getText().toString(),message.getText().toString(),act);
    }
}