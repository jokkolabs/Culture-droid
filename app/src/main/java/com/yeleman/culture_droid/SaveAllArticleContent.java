package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.orm.query.Condition;

import java.io.IOException;
import java.util.List;


public class SaveAllArticleContent extends Activity {

    private static final String TAG = Constants.getLogTag("SaveAllArticle");

    private ProgressDialog progressDialog;
    boolean isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetAndSaveAllArticleContent().execute();
    }

    private class GetAndSaveAllArticleContent extends AsyncTask<String, Void, Void> {

        String articleContent = null;
        List<ArticleData> listArticle;

        @Override
        protected void onPreExecute() {

            isOnline = Tools.isOnline(SaveAllArticleContent.this);
            listArticle = ArticleData.select().where(Condition.prop("content").eq("")).list();
            // Loading
            if (!isOnline) {
                finish();
                if (ArticleData.select().count() == 0) {
                    //Tools.gotoActivity(CultureHome.AboutFragment.newInstance(1));
                }
                Tools.toast(getApplicationContext(), R.string.required_connexion_body);
                return;
            } else {
                if (listArticle.isEmpty()){
                    finish();
                    Tools.toast(getApplicationContext(), R.string.updated);
                    return;
                }
                progressDialog = Tools.getStandardProgressDialog(SaveAllArticleContent.this, "",
                        getString(R.string.getAllArticleContentmessage), false);
                progressDialog.setMax(listArticle.size());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        finish();
                    }
                });
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (progressDialog.getProgress() <= progressDialog
                                    .getMax()) {
                                Thread.sleep(200);
                                if (progressDialog.getProgress() == progressDialog
                                        .getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            for (ArticleData article : listArticle) {
                String strUrl = Constants.getUrl(String.format(Constants.pathArticleHtml, article.getArticleId()));
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
                progressDialog.incrementProgressBy(1);
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
                // registration to notifications
                Constants.registerToNotifications(SaveAllArticleContent.this);
            }
            finish();
        }
    }
}
