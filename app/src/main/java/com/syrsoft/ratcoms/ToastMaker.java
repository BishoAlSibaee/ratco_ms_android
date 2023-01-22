package com.syrsoft.ratcoms;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {


    public static void Show(int d , String Message , Context c){
        if (d ==1){
            d = Toast.LENGTH_LONG ;
        }
        else
        {
            d = Toast.LENGTH_SHORT ;
        }
        Toast.makeText(c,Message , d).show();
    }
}
