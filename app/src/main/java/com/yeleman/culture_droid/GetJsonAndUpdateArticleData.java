package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class GetJsonAndUpdateArticleData extends AsyncTask<String, Void, Void> {

    private static final String TAG = Constants.getLogTag("GetJsonAndUpdateArticleData");

    private final Activity context;
    private CultureHome.ArticleFragment fragment;
    private ProgressDialog progressDialog;
    boolean isOnline;
    boolean contentArticle;

    JSONObject jObject;
    JSONParser jParser = new JSONParser();
    String data = null;

    public GetJsonAndUpdateArticleData(Activity applicationContext, CultureHome.ArticleFragment fragment, boolean contentArticle) {
         context = applicationContext;
         this.fragment = fragment;
         this.contentArticle = contentArticle;
    }

    @Override
    protected void onPreExecute() {
        // Loading
        isOnline = Tools.isOnline(context);
        if (!isOnline) {

            if (ArticleData.select().count() == 0) {
                //Tools.gotoAbout(context);
            }
            Tools.toast(context, R.string.required_connexion_body);
            return;
        } else {
            progressDialog = Tools.getStandardProgressDialog(context,
                    "", context.getString(R.string.loading), false);
            progressDialog.show();
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            data = Tools.getFromUrl(params[0]);
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
        if (isOnline) {
            try {
                if ((progressDialog != null) && progressDialog.isShowing()) {

                    if (fragment != null) {
                        fragment.setupUI();
                        if(contentArticle){
                            new GetAndSaveAllArticleContent(context, fragment).execute();
                        }
                    }
                    progressDialog.dismiss();
                }
            } catch (final Exception e) {
                progressDialog = null;
            }
            // register to notifications if not already
            Constants.registerToNotifications(context.getApplicationContext());
        }
    }

}