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

public class ArticleContent extends Activity {

    private static final String TAG = Constants.getLogTag("ArticleContent");
    private WebView contentView;
    private String sid;
    ArticleData article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.web_view);

        sid = getIntent().getExtras().getString("articleId");
        article =  ArticleData.getById(sid);
        if (article.getContent().isEmpty()) {
            new GetHtmlAndUpdateArticleContent().execute();
        } else {
            setupUI();
        }
    }

    public void setupUI() {
        Log.d(TAG, "setupUI");
        contentView = (WebView) findViewById(R.id.webView);
        contentView.loadDataWithBaseURL("file:///android_asset/", article.getContent(), "text/html", "utf-8", null);

    }

    private class GetHtmlAndUpdateArticleContent extends AsyncTask<String, Void, Void> {

        String articleContent = null;
        private ProgressDialog progressDialog;

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
                Popups.toast(getApplicationContext(), R.string.required_connexion_body);
                return;
            } else {
                progressDialog = Popups.getStandardProgressDialog(ArticleContent.this, "",
                        getString(R.string.loading), false);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            //String sid = params[0];
            String strUrl = Constants.getUrl(String.format("article_%s.html", sid));
            try {
                articleContent = JSONParser.getJSONFromUrl(strUrl);
                article.setContent(articleContent);
                article.save();
            } catch (IOException e) {
                Log.d(TAG, "IOException " + e.toString());
            } catch (Exception e) {
                Log.d(TAG, "Exception " + e.toString());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isOnline()) {
                try {
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (final Exception e) {
                    progressDialog = null;
                }
            }
            setupUI();
        }
    }
}
