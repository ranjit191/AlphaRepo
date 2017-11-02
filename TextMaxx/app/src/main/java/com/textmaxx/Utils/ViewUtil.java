package com.textmaxx.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.textmaxx.R;


public class ViewUtil {
    //static ProgressDialog pDialog;
    static Dialog pDialog;
    Context context;

    public static void Toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean connected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showProgressDialog(Context context) {

        try {

            pDialog = new Dialog(context);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setContentView(R.layout.progress_dialog_view);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ProgressWheel wheel = new ProgressWheel(context);
            wheel.setBarColor(Color.RED);
            pDialog.setCancelable(true);
            if (pDialog.isShowing()) {

            } else {
                pDialog.show();
            }
        } catch (Exception e) {
            Log.e("show", "" + e);
        }

    }


    public static void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            try {
                pDialog.dismiss();
                pDialog = null;
            } catch (Exception e) {
                Log.e("hide", "" + e);
            }
        }
    }

    public static void showAlertDialog(final Context context2, String title,
                                       String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context2);
        // set title
        alertDialogBuilder.setTitle(title);


        // set dialog message
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // if this button is clicked, close
                        // current fragmentalbumanagement

                        dialog.cancel();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}
