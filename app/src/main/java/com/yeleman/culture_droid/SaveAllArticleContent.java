package com.yeleman.culture_droid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by fad on 16/01/15.
 */

public class SaveAllArticleContent extends Activity {

    private static final String TAG = Constants.getLogTag("SaveAllArticle");

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetAndSaveAllArticleContent().execute();
    }


    private class GetAndSaveAllArticleContent extends AsyncTask<String, Void, Void> {

        String articleContent = null;
        List<ArticleData> listArticle;

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

            listArticle = Select.from(ArticleData.class).where(Condition.prop("content").eq("")).list();
            // Loading
            if (!isOnline()) {
                finish();
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.required_connexion_title, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
                return;
            } else {

                progressDialog = Popups.getStandardProgressDialog(SaveAllArticleContent.this, "",
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
                String strUrl = Constants.getUrl(String.format("article_%s.html", article.getArticleId()));
                try {
                    articleContent = JSONParser.getJSONFromUrl(strUrl);
                } catch (IOException e) {
                    Log.d(TAG, "IOException " + e.toString());
                } catch (Exception e) {
                    Log.d(TAG, "Exception " + e.toString());
                    return null;
                }
                article.setContent(articleContent);
                article.save();
                progressDialog.incrementProgressBy(1);
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
            finish();
        }
    }
}
