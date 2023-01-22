package com.syrsoft.ratcoms;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

public class Loading {

    private Dialog d ;

    public Loading(Context c){
        d = new Dialog(c) ;
    }

    public void show (){
        d.setContentView(R.layout.loading_layout);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.show();
    }
    public void close(){
        d.dismiss();
    }
}
