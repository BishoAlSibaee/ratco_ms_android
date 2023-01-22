package com.syrsoft.ratcoms.HRActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;
import com.syrsoft.ratcoms.HRActivities.HR_Adapters.MyApprovals_Vacation_Adapter;
import com.syrsoft.ratcoms.MyApp;
import com.syrsoft.ratcoms.R;
import com.syrsoft.ratcoms.USER;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class VacationForm extends AppCompatActivity {

    Activity act ;
    ImageView ImageV ;
    int Position ;
    VACATION_CLASS VACATION ;
    Bitmap BB ;
    String directoryPath ;
    USER OWNER ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_vacation__form_sctivity);
        Position = getIntent().getExtras().getInt("Position");
        VACATION = MyApprovals.vacationslist.get(Position);
        setActivity();
    }

    void setActivity() {
        act = this ;
        ImageV = (ImageView) findViewById(R.id.imageView23);
        ImageV.setImageResource(R.drawable.vacation_form);
        BitmapDrawable bd = (BitmapDrawable) ImageV.getDrawable();
        for (USER u : MyApp.EMPS) {
            if (u.JobNumber == VACATION.JobNumber) {
                OWNER = u ;
                break;
            }
        }
        Bitmap B = bd.getBitmap();
        Bitmap workingBitmap = Bitmap.createBitmap(B);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Log.d("bitmapSize" , mutableBitmap.getWidth()+" "+mutableBitmap.getHeight());
        writeDataTOBitmap(mutableBitmap,VACATION.SendDate,(mutableBitmap.getWidth()/4)*3+50,170,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,VACATION.FName+" "+VACATION.LName,(mutableBitmap.getWidth()/4)*3,325,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,OWNER.Nationality,(mutableBitmap.getWidth()/4)*3,360,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,OWNER.JobTitle,(mutableBitmap.getWidth()/4)*3,425,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,String.valueOf(VACATION.JobNumber),(mutableBitmap.getWidth()/4),325,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,String.valueOf(OWNER.IDNumber),(mutableBitmap.getWidth()/4),360,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,OWNER.JoinDate,(mutableBitmap.getWidth()/4),395,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,OWNER.Department,(mutableBitmap.getWidth()/4),430,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,String.valueOf(VACATION.id),(mutableBitmap.getWidth()/4)*3,560,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap,VACATION.StartDate,(mutableBitmap.getWidth()/4)*3,600,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, String.valueOf(VACATION.VacationDays),(mutableBitmap.getWidth()/4),560,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, VACATION.EndDate,(mutableBitmap.getWidth()/4),600,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, VACATION.DirectManagerName,(mutableBitmap.getWidth()/4)+70,1100,Color.BLUE,20);
        writeDataTOBitmap(mutableBitmap, VACATION.AlternativeName,(mutableBitmap.getWidth()/4)-150,1100,Color.BLUE,20);
        if (VACATION.Auths.get(0).isAuthExists()) {
            for (USER u : MyApp.EMPS) {
                if (u.id == VACATION.Auths.get(0).AuthID) {
                    writeDataTOBitmap(mutableBitmap,u.FirstName+" "+u.LastName ,(mutableBitmap.getWidth()/4)*2-200,1150,Color.BLUE,20);
                    writeDataTOBitmap(mutableBitmap,u.JobTitle ,(mutableBitmap.getWidth()/4)*2-400,1150,Color.BLUE,20);
                    break;
                }
            }
            writeDataTOBitmap(mutableBitmap,VACATION.getFirstAuthResult(),(mutableBitmap.getWidth()/4)*2,1150,Color.BLUE,20);
        }
        if (VACATION.Auths.get(1).isAuthExists()) {
            for (USER u : MyApp.EMPS) {
                if (u.id == VACATION.Auths.get(1).AuthID) {
                    writeDataTOBitmap(mutableBitmap,u.FirstName+" "+u.LastName ,(mutableBitmap.getWidth()/4)-100,1430,Color.BLUE,20);
                    writeDataTOBitmap(mutableBitmap,u.JobTitle ,(mutableBitmap.getWidth()/4)-250,1430,Color.BLUE,20);
                    break;
                }
            }
            writeDataTOBitmap(mutableBitmap,VACATION.getFirstAuthResult(),(mutableBitmap.getWidth()/4),1430,Color.BLUE,20);
        }
        if (VACATION.Auths.get(2).isAuthExists()) {
            for (USER u : MyApp.EMPS) {
                if (u.id == VACATION.Auths.get(2).AuthID) {
                    writeDataTOBitmap(mutableBitmap,u.FirstName+" "+u.LastName ,(mutableBitmap.getWidth()/4)*2+200,1430,Color.BLUE,20);
                    writeDataTOBitmap(mutableBitmap,u.JobTitle ,(mutableBitmap.getWidth()/4)*2+40,1430,Color.BLUE,20);
                    break;
                }
            }
            writeDataTOBitmap(mutableBitmap,VACATION.getFirstAuthResult(),(mutableBitmap.getWidth()/4)*2+300,1430,Color.BLUE,20);
        }
        BB = mutableBitmap ;
        ImageV.setImageBitmap(mutableBitmap);
        convertBitmapToPdf(BB);
    }

    public void share(View view) {
//        PdfGenerator.getBuilder()
//                .setContext(act)
//                .fromLayoutXMLSource()
//                .fromLayoutXML(R.layout.hr_vacation__form_sctivity)
//                /* "fromLayoutXML()" takes array of layout resources.
//                 * You can also invoke "fromLayoutXMLList()" method here which takes list of layout resources instead of array. */
//                .setFileName("Test-PDF")
//                /* It is file name */
//                .setFolderName("FolderA/FolderB/FolderC")
//                /* It is folder name. If you set the folder name like this pattern (FolderA/FolderB/FolderC), then
//                 * FolderA creates first.Then FolderB inside FolderB and also FolderC inside the FolderB and finally
//                 * the pdf file named "Test-PDF.pdf" will be store inside the FolderB. */
//                .openPDFafterGeneration(true)
//                /* It true then the generated pdf will be shown after generated. */
//                .build(new PdfGeneratorListener() {
//                    @Override
//                    public void onFailure(FailureResponse failureResponse) {
//                        super.onFailure(failureResponse);
//                        /* If pdf is not generated by an error then you will findout the reason behind it
//                         * from this FailureResponse. */
//                        Log.d("makingpdf" , failureResponse.getErrorMessage());
//                    }
//                    @Override
//                    public void onStartPDFGeneration() {
//                        /*When PDF generation begins to start*/
//                    }
//
//                    @Override
//                    public void onFinishPDFGeneration() {
//                        /*When PDF generation is finished*/
//                    }
//
//                    @Override
//                    public void showLog(String log) {
//                        super.showLog(log);
//                        /*It shows logs of events inside the pdf generation process*/
//                    }
//
//                    @Override
//                    public void onSuccess(SuccessResponse response) {
//                        super.onSuccess(response);
//                        /* If PDF is generated successfully then you will find SuccessResponse
//                         * which holds the PdfDocument,File and path (where generated pdf is stored)*/
//                        Log.d("makingpdf" , response.getPath());
//                    }
//                });

//        PdfDocument Do = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(100, 100, 1).create();
//        PdfDocument.Page page = Do.startPage(pageInfo);
//        View content = findViewById(R.id.Frame);
//        content.draw(page.getCanvas());
//        Do.finishPage(page);
//        File dir = new File(act.getExternalFilesDir(null), "");
//        if(!dir.exists()) {
//            if (dir.mkdirs()) {
//                File file = new File(act.getExternalFilesDir(null), "newFile.pdf");
//                //File outputFile = new File("/storage/emulated/0/Android/data/package/files/", "cot.pdf");
//                try {
//                    OutputStream stream = new FileOutputStream(file);
//                    Do.writeTo(stream);
//                    Do.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Log.d("pdfError",e.getMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d("pdfError",e.getMessage());
//                }
//            }
//            else {
//                Log.d("pdfError","not created");
//            }
//        }
//        else {
//            Log.d("pdfError","dir exists");
//            File file = new File(act.getExternalFilesDir(null), "newFile.pdf");
//            try {
//                OutputStream stream = new FileOutputStream(file);
//                Do.writeTo(stream);
//                Do.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Log.d("pdfError",e.getMessage());
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d("pdfError",e.getMessage());
//            }
//        }B
        Uri uri = Uri.parse(directoryPath + "/Vacation_"+VACATION.FName+" "+VACATION.LName+"_"+VACATION.id+".pdf");
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
            PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/Vacation_"+VACATION.FName+" "+VACATION.LName+"_"+VACATION.id+".pdf")); //  Change pdf's name.
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

    public void goPrint(View view) {
        PrintHelper photoPrinter = new PrintHelper(act);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("droids.jpg - test print", BB);
    }

}