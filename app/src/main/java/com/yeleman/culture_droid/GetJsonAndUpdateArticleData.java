package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fad on 19/01/15.
 */
public class GetJsonAndUpdateArticleData extends AsyncTask<String, Void, Void> {

    private static final String TAG = Constants.getLogTag("CultureHome");
    private final Activity context;
    JSONObject jObject;
    JSONParser jParser = new JSONParser();
    String data = null;
    private ProgressDialog progressDialog;

    public GetJsonAndUpdateArticleData(Activity applicationContext) {
         context = applicationContext;
    }

    public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                Popups.toast(context, R.string.required_connexion_body);
                return;
            } else {
                progressDialog = Popups.getStandardProgressDialog(context,
                        "", context.getString(R.string.loading), false);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                data = JSONParser.getJSONFromUrl(params[0]);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground Exception" + e + "\nLe lien (url) vers la liste des articles est mort.");
                return null;
            }
            try {
                jObject = new JSONObject(data);
            } catch (JSONException e) {
                Log.d(TAG, "JSONException " + e.toString());
            }

            List<HashMap<String, Object>> resources = null;
            resources = jParser.parse(jObject);

            for (HashMap<String, Object> article : resources) {
                String publishedOn = article.get(Constants.KEY_PUBLISHED_ON).toString();
                String articleId = article.get(Constants.KEY_ARTICLE_ID).toString();
                String thumbnail = article.get(Constants.KEY_THUMBNAIL).toString();
                String title = article.get(Constants.KEY_TITLE).toString();
                String nbComments = article.get(Constants.KEY_NB_COMMENTS).toString();
                String contentSize = article.get(Constants.KEY_CONTENT_SIZE).toString();
                String content = article.get(Constants.KEY_CONTENT).toString();
                JSONArray tags = (JSONArray) article.get(Constants.KEY_TAGS);

                Log.d(TAG, "ArticleID:" + articleId);
                List<ArticleData> news = ArticleData.find(ArticleData.class, "articleId = ?", articleId);
                if (news.isEmpty()) {
                    Date date = Constants.strDateToDate(publishedOn);
                    ArticleData articleData = new ArticleData(date,
                            articleId,
                            thumbnail,
                            title,
                            nbComments,
                            contentSize,
                            content);
                    articleData.saveWithId();
                    Log.d(TAG, "Creating article: " + articleData.getArticleId() + " with ID: " + articleData.getId());
                    articleData.articleTagSave(Constants.listStringFromJsonArray(tags));
                } else {
                }
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
        }

    }

