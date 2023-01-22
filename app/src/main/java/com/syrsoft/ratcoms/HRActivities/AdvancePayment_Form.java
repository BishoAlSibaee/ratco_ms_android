package com.syrsoft.ratcoms.HRActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AdvancePayment_Form extends AppCompatActivity {

    Activity act ;
    ImageView ImageV ;
    int Position ;
    ADVANCEPAYMENT_CLASS ADVANCE ;
    Bitmap BB ;
    String directoryPath ;
    USER OrderUser ;
    USER OrderDirectManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_advance_payment__form_activity);
        Position = getIntent().getExtras().getInt("Position");
        ADVANCE = MyApprovals.advanceList.get(Position);
        setActivity();
    }

    void setActivity() {
        act = this;
        for (USER u : MyApp.EMPS) {
            if (u.id == ADVANCE.EmpID) {
                OrderUser = u ;
                break;
            }
        }
        for (USER u : MyApp.EMPS) {
            if (u.JobNumber == OrderUser.DirectManager) {
                OrderDirectManager = u ;
                break;
            }
        }
        ImageV = (ImageView) findViewById(R.id.imageView24);
        ImageV.setImageResource(R.drawable.advance_form);
        BitmapDrawable bd = (BitmapDrawable) ImageV.getDrawable();
        Bitmap B = bd.getBitmap();
        Bitmap workingBitmap = Bitmap.createBitmap(B);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Log.d("bitmapSize", mutableBitmap.getWidth() + " " + mutableBitmap.getHeight());
        writeDataTOBitmap(mutableBitmap,ADVANCE.SendDate,(mutableBitmap.getWidth()/4)*3,420, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,ADVANCE.FName+" "+ADVANCE.LName,(mutableBitmap.getWidth()/4)*3,460, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, String.valueOf(ADVANCE.JobNumber),(mutableBitmap.getWidth()/4)*3,500, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, OrderUser.Department,(mutableBitmap.getWidth()/4)*3,540, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, OrderUser.JobTitle,(mutableBitmap.getWidth()/4)*3,570, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, String.valueOf(ADVANCE.Amount),(mutableBitmap.getWidth()/4)*3,610, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, ADVANCE.Reason,(mutableBitmap.getWidth()/4)*2,700, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, String.valueOf(ADVANCE.Installment),(mutableBitmap.getWidth()/4)*3-100,840, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,ADVANCE.SendDate,(mutableBitmap.getWidth()/4)*3-100,920, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,ADVANCE.DirectManagerName,(mutableBitmap.getWidth()/4)*2+120,1030, Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,OrderDirectManager.JobTitle,(mutableBitmap.getWidth()/4)*2,1030, Color.BLUE,20);
        if (ADVANCE.Auths.get(0).isAuthExists()) {
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(0).getAuthResult(),(mutableBitmap.getWidth()/4)*2+220,1030, Color.BLUE,20);
        }
        if (ADVANCE.Auths.get(1).isAuthExists()) {
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(1).getAuthUser().FirstName+" "+ADVANCE.Auths.get(1).getAuthUser().LastName,(mutableBitmap.getWidth()/4)*2,1240, Color.BLUE,20);
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(1).getAuthUser().JobTitle,(mutableBitmap.getWidth()/4)*2+120,1240, Color.BLUE,20);
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(1).getAuthResult(),(mutableBitmap.getWidth()/4)*2+260,1240, Color.BLUE,20);
        }
        if (ADVANCE.Auths.get(2).isAuthExists()) {
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(2).getAuthUser().FirstName+" "+ADVANCE.Auths.get(2).getAuthUser().LastName,(mutableBitmap.getWidth()/4)*2,1450, Color.BLUE,20);
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(2).getAuthUser().JobTitle,(mutableBitmap.getWidth()/4)*2+120,1450, Color.BLUE,20);
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(2).getAuthResult(),(mutableBitmap.getWidth()/4)*2+260,1450, Color.BLUE,20);
        }
        if (ADVANCE.Auths.get(3).isAuthExists()) {
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(3).getAuthUser().FirstName+" "+ADVANCE.Auths.get(3).getAuthUser().LastName,(mutableBitmap.getWidth()/4)*2,1490, Color.BLUE,20);
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(3).getAuthUser().JobTitle,(mutableBitmap.getWidth()/4)*2+120,1490, Color.BLUE,20);
            writeDataTOBitmap(mutableBitmap,ADVANCE.Auths.get(3).getAuthResult(),(mutableBitmap.getWidth()/4)*2+260,1490, Color.BLUE,20);
        }

        BB = mutableBitmap ;
        ImageV.setImageBitmap(mutableBitmap);
        convertBitmapToPdf(BB);
    }

    public void goPrint(View view) {
        PrintHelper photoPrinter = new PrintHelper(act);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("droids.jpg - test print", BB);
    }

    public void share(View view) {
        Uri uri = Uri.parse(directoryPath + "/Vacation_"+ADVANCE.FName+" "+ADVANCE.LName+"_"+ADVANCE.id+".pdf");
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        act.startActivity(share);
    }

    void writeDataTOBitmap(Bitmap b , String text , int x, int y , int c , int fontSize) {
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setColor(c);
        paint.setTextSize(fontSize);
        canvas.drawText(text, x, y, paint);
    }

    void convertBitmapToPdf(Bitmap b) {
        Document document = new Document();
        directoryPath = act.getExternalFilesDir(null).toString()+"/PDF";
        Log.d("makePdf" , directoryPath);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/Vacation_"+ADVANCE.FName+" "+ADVANCE.LName+"_"+ADVANCE.id+".pdf")); //  Change pdf's name.
        } catch (DocumentException e) {
            Log.d("makePdf" , "1 "+e.getMessage());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.d("makePdf" , "2 "+e.getMessage());
            e.printStackTrace();
        }
        document.open();
        Image image = null;  // Change image's name and extension.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            image = Image.getInstance(byteArray);
            //image = Image.getInstance(directoryPath + "/" + "example.jpg");
        } catch (BadElementException e) {
            e.printStackTrace();
            Log.d("makePdf" , "3 "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("makePdf" , "4 "+e.getMessage());
        }
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler);
        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

        try {
            document.add(image);
        } catch (DocumentException e) {
            e.printStackTrace();
            Log.d("makePdf" , "5 "+e.getMessage());
        }
        document.close();
    }
}