package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fad on 23/12/14.
 */

public class Details extends Activity {

    private static final String TAG = Constants.getLogTag("DetailNews");
    private WebView contentView;
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.details);

        sid = getIntent().getExtras().getString("articleId");
        ArticleData news =  ArticleData.findById(ArticleData.class, Long.valueOf(sid));
        String urlDetail = Constants.getUrl(String.format("article_%s.html", news.getArticleId()));
        if (news.getContent().isEmpty()) {
            new GetHtml().execute(urlDetail, sid);
        } else {
            Log.d(TAG, "Existe déjà");
            setupUI();
        }
    }

    public void setupUI() {
        Log.d(TAG, "setupUI");
        contentView = (WebView) findViewById(R.id.detailWeb);
        ArticleData news =  ArticleData.findById(ArticleData.class, Long.valueOf(sid));
        String strHtml = news.getContent();
        contentView.loadDataWithBaseURL(Constants.nameDomaine, strHtml, "text/html", "UTF-8", "");
    }

    private class GetHtml extends AsyncTask<String, Void, Void> {

        String data = null;
        private ProgressDialog progressDialog = new ProgressDialog(Details.this);

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            // Loading
            if (!isOnline()) {
                finish();
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.required_connexion_title, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
                return;
            } else {
                progressDialog.setMessage("Chargement en cours ...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            String strUrl = params[0];
            int sid = Integer.parseInt(params[1]);
            try {
                data = JSONParser.getJSONFromUrl(strUrl);
            } catch (IOException e) {
                Log.d(TAG, "IOException " + e.toString());
            } catch (Exception e) {
                Log.d(TAG, "Exception " + e.toString());
                return null;
            }
            ArticleData news =  ArticleData.findById(ArticleData.class, Long.valueOf(sid));
            news.setContent(data);
            news.save();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isOnline()) {
                setupUI();
                progressDialog.dismiss();
            }else{
                //Dialog.
            }
        }
    }
}
