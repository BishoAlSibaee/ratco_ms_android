package com.syrsoft.ratcoms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ADSDatabase extends SQLiteOpenHelper {

    private static final int DBVersion = 1 ;
    private static String DBName  = "ADS";
    SQLiteDatabase db ;


    public ADSDatabase(@Nullable Context context) {
        super(context, DBName, null, DBVersion);
        db = getWritableDatabase() ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS AD ( 'id' INTEGER  ,'title' VARCHAR,'message' VARCHAR ,'imageLink' VARCHAR ,'fileLink' VARCHAR,'date' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void logOut() {
        db.execSQL("DROP TABLE IF EXISTS 'AD'");
        onCreate(db);
    }

    public boolean insertAd (String  title ,String text , String img , String number) {
        boolean result = false ;
        ContentValues values = new ContentValues();
        int x = 0 ;
        if (getAds().size() < 0 ) {
            x=1 ;
        }
        else {
            x = getAds().size()+1 ;
        }
        Log.d("adInsertId" , x+"" );
        values.put("id" , x);
        values.put( "title" , title );
        values.put("text" , text );
        values.put( "img" , img );
        values.put("number" , number);


        try {
            db.insert("AD", null, values);
            result = true ;
            if (MyApp.app != null ){
                MyApp.ADS_Counter++ ;
            }

        }catch (Exception e )
        {
            result = false ;
        }

        return result ;
    }

    public boolean insertAd (int id ,String  title ,String message , String img , String fileLink , String date) {
        boolean result = false ;
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put( "title" , title );
        values.put("message" , message );
        values.put( "imageLink" , img );
        values.put("fileLink" , fileLink);
        values.put("date",date);

        if (db.insert("AD", null, values) > 0) {
            result = true ;
        }

        return result ;
    }

    public  void deleteAd (int number ) {
        //String[] ID = new String []{String.valueOf(id)};
        int res = db.delete("AD","number="+number ,null );
        Log.d("adDeleteResult" , res+" "+number );
    }

    public int getLastAdId() {
        Cursor c = db.rawQuery("SELECT * FROM 'AD' ; " , null);
        if (c.moveToLast()) {
            return c.getInt(0);
        }
        else {
            return -1 ;
        }
    }

    public List<ADS_CLASS> getAds() {
        List<ADS_CLASS> list = new ArrayList<ADS_CLASS>();
        int id ;
        String title ;
        String text ;
        String img ;
        String number ;
        String link ;

        Cursor c = db.rawQuery("SELECT * FROM 'AD' ; " , null);
        c.moveToFirst() ;
        //int i=0;
       /* while( i < c.getCount() )
        {
            i++ ;
            id = c.getInt(0);
            title = c.getString(1);
            text = c.getString(2);
            img = c.getString(3);
            number = c.getString(4);
            ADS_CLASS a = new ADS_CLASS(id,title,text,img , number);
            list.add(a);
        }*/

        for (int i=0;i<c.getCount();i++)
        {
            id = c.getInt(0);
            title = c.getString(1);
            text = c.getString(2);
            img = c.getString(3);
            number = c.getString(4);
            link = c.getString(5);
            ADS_CLASS a = new ADS_CLASS(id,title,text,img , number , link);
            list.add(a);
            if ((i+1) != c.getCount()){
                c.moveToNext();
            }
        }
        return list ;
    }
}
