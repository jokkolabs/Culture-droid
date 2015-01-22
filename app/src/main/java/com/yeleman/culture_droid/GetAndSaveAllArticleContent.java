package com.yeleman.culture_droid;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.orm.query.Condition;


public class GetAndSaveAllArticleContent extends AsyncTask<String, Void, Void> {

    private static final String TAG = Constants.getLogTag("GetAndSaveAllArticleContent");

    private final Activity context;
    private CultureHome.ArticleFragment fragment;
    private ProgressDialog progressDialog;
    boolean isOnline;
    String articleContent = null;
    List<ArticleData> listArticle;

    public GetAndSaveAllArticleContent(Activity applicationContext,
                                       CultureHome.ArticleFragment fragment) {
        this.fragment = fragment;
        context = applicationContext;
    }

    @Override
    protected void onPreExecute() {

        isOnline = Tools.isOnline(context);
        listArticle = ArticleData.select().where(Condition.prop("content").eq("")).list();

        // Loading
        if (!isOnline) {
            Tools.toast(context, R.string.required_connexion_body);
            return;
        } else {
            if (listArticle.isEmpty()){
                Tools.toast(context, R.string.updated);
                return;
            }
            Tools.lockScreenOrientation(context);
            progressDialog = Tools.getStandardProgressDialog(context, "",
                   context.getString(R.string.getAllArticleContentmessage), false);
            progressDialog.setMax(listArticle.size());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //progressDialog.setMessage(context.getString(R.string.loading));
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel), new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int whichButton){
                    try {
                        finalize();
                        Thread.interrupted();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
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
                progressDialog.incrementProgressBy(1);
            } catch (Exception e) {
                Log.d(TAG, "Exception " + e.toString());
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (isOnline) {
            if (fragment != null){
                fragment.setupUI();
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                Tools.unlockScreenOrientation(context);
            }
            // registration to notifications
            Tools.registerToNotifications(context);
        }
    }
}