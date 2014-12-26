package com.yeleman.culture_droid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by fad on 23/12/14.
 */

public class Details extends Activity {

    private static final String TAG = Constants.getLogTag("DetailNews");
    private WebView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        String sid = getIntent().getExtras().getString("selectId");
        setContentView(R.layout.details);
        contentView = (WebView) findViewById(R.id.detailWeb);

        WebSettings webSettings = contentView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.getLoadsImagesAutomatically();
        webSettings.getDisplayZoomControls();
        webSettings.getTextZoom();

        NewsData news =  NewsData.findById(NewsData.class, Long.valueOf(sid));
        String nHtml = news.getContent();
        Log.d(TAG, news.getDescription());
        contentView.loadDataWithBaseURL("http://quandlevillagesereveille.wordpress.com/",
                nHtml, "text/html", "UTF-8", "");
    }
}
