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

}
