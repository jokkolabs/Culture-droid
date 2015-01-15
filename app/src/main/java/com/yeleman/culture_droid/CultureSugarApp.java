package com.yeleman.culture_droid;

import com.orm.SugarApp;
import com.orm.SugarContext;

import android.util.Log;

/**
 * Created by fad on 21/11/14.
 */
public class CultureSugarApp extends SugarApp {

    private static final String TAG = Constants.getLogTag("CultureSugarApp");

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Init Database");
        Log.d("Sugar", "DB init");

        SugarContext.init(this);

        Log.d("Sugar", "DB inited");
        Log.i(TAG, "DB inited");
        //ArticleData x = ArticleData.findById(ArticleData.class, 1);

    }
    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }

}