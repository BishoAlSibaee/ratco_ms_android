package com.syrsoft.ratcoms;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressLoadingDialog {

    Context c ;
    Dialog D ;
    ProgressBar P ;
    TextView text ;

    public ProgressLoadingDialog(Context c, int total) {
        this.c = c ;
        D = new Dialog(this.c);
        D.setContentView(R.layout.progress_loading_dialog);
        Window w = D.getWindow();
        w.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        P = (ProgressBar) D.findViewById(R.id.progressBar6);
        text = (TextView) D.findViewById(R.id.textView94) ;
        text.setText("0");
        P.setMax(total);
        D.setCancelable(false);
        D.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        D.show();
    }

    public void setProgress (int prog) {
        P.setProgress(prog);

        text.setText(((prog*100)/P.getMax())+"%");
        if (P.getProgress() == P.getMax()) {
            D.dismiss();
        }
    }

    public void stop () {
        D.dismiss();
    }

    public int getProgress() {
        return P.getProgress();
    }
}
