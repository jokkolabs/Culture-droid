package com.yeleman.culture_droid;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fad on 19/12/14.
 */

public class Constants {

    private static final String TAG = Constants.getLogTag("Constants");

    public static final String KEY_THUMBNAIL = "thumbnail";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PUBLISHED_ON = "published_on";
    public static final String KEY_CONTENT_SIZE = "content_size";
    public static final String KEY_ARTICLE_ID = "article_id";
    public static final String KEY_NB_COMMENTS = "nb_comments";
    public static final String KEY_CONTENT = "content";
    public static final String DEFAULTHUMBNAIL = "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAABdklEQVR4nO2bwRKDIBBD107//5fppesgogKuhoXk0o6dkeSZtR5kCSHIS2pZaDF3kej74LktyKbnMAdiDeDpOsXnN4FhBeC1OcqseQvEx9AISrfWv9MAdPBYzW1oAdBT8FTVIGpHoOfwsYp91gDwEl5V5LcUgLfwqkvfJQC8hled+r8C4D286jDHGYBRwquyeSwehFzrCMBoV1+1y8UGZI6NevVVm3wpgNHDq9acHIHo+yxXXxVE2AACUACz1V8V2AC0AbQIQOadfxFhAwiAANAG0CIAtAG0CABtAC0CQBtAiwDkhTexehYbgDaAFgH8P2e9DyxsANoAWjGA2cZgEWEDdgBmacGaM9eA0SFs8nEEDo6P2oJdLjbg5LfRWpDNc9WAUSAc5igZAe8QTv2X3gO8Qrj0XXMT9AahyG/tv4AXCMU+W/YM6cl7fK/glU1T6WI9gGhupsWDEHosbq1vtXMU0Yauts6qYlNPwOh+83Ss1GyX2+d/qB8skeca7swAAAAASUVORK5CYII=";

    protected static String nameDomaine = "192.168.5.55:8000";

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

    public static String displaySizeForArticleContent(NewsData newsData) {
        if (newsData.getContent().isEmpty())
            return  convertSizeOctToKo(newsData.getContentSize());
        else
            return "✔";
    }

    public static void getAllArticle() {
        Log.d(TAG, "TOTO");
    }
}
