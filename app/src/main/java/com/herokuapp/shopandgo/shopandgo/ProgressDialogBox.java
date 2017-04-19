package com.herokuapp.shopandgo.shopandgo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * Created by bukbukbukh on 4/12/17.
 */

public class ProgressDialogBox extends Dialog {

    Context activity;
    private static ProgressDialogBox box;
    private static boolean isShow = false;

    public ProgressDialogBox(Context a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("GOING IN", "GOING IN");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.progress_bar);
        android.widget.ProgressBar bar = (android.widget.ProgressBar) findViewById(R.id.thinking_dialog);
        bar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#dfb8c2"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);

    }

    public static void createBox(Context context) {
        if (!isShow) {
            isShow = true;
            box = new ProgressDialogBox(context);

            box.show();
        } else {
            Log.e("HIDE", "ISSHown already");
            box.dismiss();
        }

    }

    public static void hideBox(Context context) {
        if (isShow) {
            isShow = false;
            box.dismiss();
        }
    }
}
