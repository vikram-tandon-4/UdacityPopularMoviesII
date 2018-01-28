package com.udacity.gradle.builditbigger;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;


public class Utils {


    /**
     * display progress dialog
     *
     * @param mContext
     * @return
     */

    public static Dialog showProgressDialog(Context mContext) {
        Dialog progressDialog = new Dialog(mContext);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }


    /**
     * cancel progress dialog
     *
     * @param progressDialog
     */
    public static void cancelProgressDialog(Dialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
