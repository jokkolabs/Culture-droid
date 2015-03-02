package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;

/**
 * Created by fad on 23/12/14.
 */

public class ArticleContent extends Activity {

    private static final String TAG = Constants.getLogTag("ArticleContent");
    private WebView contentView;
    private String sid;
    boolean isOnline;
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
        contentView.getSettings().setBuiltInZoomControls(true);
        contentView.loadDataWithBaseURL("file:///android_asset/", article.getContent(), "text/html", "utf-8", null);

    }

    private class GetHtmlAndUpdateArticleContent extends AsyncTask<String, Void, Void> {

        String articleContent = null;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // Loading
            isOnline = Tools.isOnline(ArticleContent.this);
            if (!isOnline) {
                finish();
                Tools.toast(getApplicationContext(), R.string.required_connexion_body);
                return;
            } else {
                progressDialog = Tools.getStandardProgressDialog(ArticleContent.this, "",
                        getString(R.string.loading), false);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            String strUrl = Constants.getUrl(String.format("article_%s.html", sid));
            try {
                articleContent = Tools.getFromUrl(strUrl);
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
            if (isOnline) {
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
