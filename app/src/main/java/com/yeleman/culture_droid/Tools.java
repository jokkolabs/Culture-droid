package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Tools {

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

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void toast (Context context , int msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String getFromUrl(String strUrl) throws IOException {
        Log.d(Constants.getLogTag("getFromUrl: "), strUrl.toString());
        String data = "";
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d(Constants.getLogTag("Exception while downloading url"), e.toString());
        }finally{
            iStream.close();
        }
        return data;
    }

    public static void registerToNotifications(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String registrationID = sharedPrefs.getString("registrationID", "");
        if (registrationID.length() == 0) {
            new GcmRegistrationAsyncTask(context).execute();
        }
    }
    public static void downloadAllContent(Activity activity, CultureHome.ArticleFragment fragment){

        if (ArticleData.select().count() == 0) {
            new GetJsonAndUpdateArticleData(activity, fragment, true).execute(Constants.getUrlJson());
        } else {
            new GetAndSaveAllArticleContent(activity, fragment).execute();
        }
    }

    public static void lockScreenOrientation(Activity context) {
        int currentOrientation = context.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public static void unlockScreenOrientation(Activity context) {
        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
