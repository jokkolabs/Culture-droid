package com.yeleman.culture_droid;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by fad on 19/12/14.
 */

public class Constants {

    private static final String TAG = Constants.getLogTag("Constants");

    protected static String nameDomaine = "192.168.5.55:8000";

    public static final String KEY_THUMBNAIL = "thumbnail";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PUBLISHED_ON = "published_on";
    public static final String KEY_CONTENT_SIZE = "content_size";
    public static final String KEY_ARTICLE_ID = "article_id";
    public static final String KEY_NB_COMMENTS = "nb_comments";
    public static final String KEY_CONTENT = "content";
    public static final String DEFAULTHUMBNAIL = "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAYAAABccqhmAA" +
            "AAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAmFJREFUeNrs1DEBAAAIwzDmX/QwgAMSCT" +
            "2atgP8FAMAAwAMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAw" +
            "AMADAAwAAAAwAMADAAwAAAAwAMADAAwADAAAwADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAA" +
            "MADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMAAzAAMADAAAADAAwAMADAAA" +
            "ADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAA" +
            "ADAAwADEAFMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMA" +
            "DAAAADAAwAMADAAAADAAwAMADAAAADAAwADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMADA" +
            "AwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMADAAwAMAAAAMADAAMADAAwAAAAwAMADAAwAAAAw" +
            "AMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAwAMADAAwAAAAw" +
            "AMAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAAADAAwAMADAAA" +
            "ADAAwAMADAAAADAAwAuKwAAwC/bP4fP9glIQAAAABJRU5ErkJggg==";

    public static final String KEY_TAGS = "tags";
    public static final String TAG_ALL = "Tous les articles";

    public static final String getLogTag(String activity) {
        return String.format("CultureLog-%s", activity);
    }

    public static String getUrl(String page)  {
        return String.format("http://%s/%s", nameDomaine, page);
    }

    public static String convertSizeOctToKo(String strSize) {
        int sizeKo = Integer.valueOf(strSize) / 1024;
        return String.valueOf(sizeKo) + "k";
    }

    public static String formatDatime(Date date) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = df.format(date.getTime());
        return strDate;
    }

    public static String displaySizeForArticleContent(ArticleData newsData) {
        if (newsData.getContent().isEmpty())
            return  convertSizeOctToKo(newsData.getContentSize());
        else
            return "✔";
    }

    public static List<String> listStringFromJsonArray(JSONArray array){

        int len = array.length();
        List list = new ArrayList();
        for (int i = 0; i<len; i++) {
            try {
                list.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
