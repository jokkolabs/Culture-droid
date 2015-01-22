package com.yeleman.culture_droid;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fad on 19/12/14.
 */

public class Constants {

    private static final String TAG = Constants.getLogTag("Constants");

    protected static String nameDomaine = "192.168.5.55:8000";
    public static String pathArticleHtml = "article_%s.html";

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

    public static String formatDateToStrDate(Date date) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = df.format(date.getTime());
        return strDate;
    }

    public static Date strDateToDate(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(strDate.replace("T", " "));
        } catch (ParseException e) {
            Log.d(TAG, "ParseException" + e.toString());
        }
        return date;
    }

/*
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now =  Calendar.getInstance().getTime().getTime();
        if (time > now || time <= 0) {
            return null;
        }
        final long diff = now - time;
        Log.d(TAG, diff + " now: "+now);
        if (diff < MINUTE_MILLIS) {
            return "Aujourd'hui";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "il ya une minute";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "il ya une heure";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " Heures";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Hier";
        } else {
            DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
            String strDate = df.format(time);
            return strDate;
        }
    }*/
}
