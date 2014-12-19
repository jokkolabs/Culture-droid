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
        NewsData x = NewsData.findById(NewsData.class, 1);
    }
    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }

}