package com.yeleman.culture_droid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.yeleman.culure_droid.backend.registration.Registration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, Void> {
    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    private Context context;
    private String msg = "";

    // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
    private static final String SENDER_ID = "554324386880";

    public GcmRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (regService == null) {

            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://culture-app-push.appspot.com/_ah/api/");

            regService = builder.build();
        }

        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            String regId = gcm.register(SENDER_ID);
//            msg = "Device registered, registration ID=" + regId;
            msg = "Réception des notifications activée";

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            regService.register(regId).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error: " + ex.getMessage();
        }
//        return msg;
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Logger.getLogger("REGISTRATION").log(Level.INFO, msg);

        // save in preferences
        if (msg.length() > 0) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("registrationID", msg);
            editor.apply();
        }
    }
}