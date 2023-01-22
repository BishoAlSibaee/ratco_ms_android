package com.syrsoft.ratcoms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MESSAGE_DIALOG {

    Dialog d ;

    public MESSAGE_DIALOG(Context c , String title , String message ) {
        d = new Dialog(c);
        d.setContentView(R.layout.message_dialog);
        TextView Title = (TextView) d.findViewById(R.id.MessagDialog_Title);
        TextView Message = (TextView) d.findViewById(R.id.MessageDialog_Message);
        Button ok = (Button) d.findViewById(R.id.MessageDialog_OK);
        Title.setText(title);
        Message.setText(message);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public MESSAGE_DIALOG(Context c , String title , String message , int x) {
        d = new Dialog(c);
        d.setContentView(R.layout.message_dialog);
        TextView Title = (TextView) d.findViewById(R.id.MessagDialog_Title);
        TextView Message = (TextView) d.findViewById(R.id.MessageDialog_Message);
        Button ok = (Button) d.findViewById(R.id.MessageDialog_OK);
        Title.setText(title);
        Message.setText(message);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x==1){
                    d.dismiss();
                }
                else if (x == 0){
                    d.dismiss();
                    Activity act = (Activity) c ;
                    act.finish();
                }
            }
        });
        d.show();
    }


}
