package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Popups {

    public static AlertDialog.Builder getDialogBuilder(Activity activity,
                                                       String title,
                                                       String message,
                                                       boolean cancelable) {
        AlertDialog.Builder smsDialogBuilder = new AlertDialog.Builder(activity);
        smsDialogBuilder.setCancelable(cancelable);
        smsDialogBuilder.setTitle(title);
        smsDialogBuilder.setMessage(message);
        smsDialogBuilder.setIcon(R.drawable.ic_launcher);
        return smsDialogBuilder;
    }

    public static ProgressDialog getStandardProgressDialog(Activity activity,
                                                           String title,
                                                           String message,
                                                           boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIcon(R.drawable.ic_launcher);
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }

    public static DialogInterface.OnClickListener getBlankClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        };
    }

    public static void startIntentIfOnline(final Activity activity, final Intent intent) {
        if (isOnline(activity)) {
            activity.startActivity(intent);
            return;
        }

        AlertDialog.Builder dialogBuilder = getDialogBuilder(
                activity, activity.getString(R.string.required_connexion_title),
                activity.getString(R.string.required_connexion_body), true);
        dialogBuilder.setNegativeButton(activity.getString(R.string.required_connexion_cancel), getBlankClickListener());
        dialogBuilder.setPositiveButton(activity.getString(R.string.required_connexion_retry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startIntentIfOnline(activity, intent);
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
