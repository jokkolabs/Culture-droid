package com.yeleman.culture_droid;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Select;

import java.util.Date;
import java.util.StringTokenizer;

public class TagArticleData extends SugarRecord {

    @Ignore
    private static final String TAG = Constants.getLogTag("TagArticleData");

    private long tag_id;
    private long article_id;

    public TagArticleData() {
    }

    public TagArticleData(long tag_id,
                          long article_id) {
        this.tag_id = tag_id;
        this.article_id = article_id;
    }

    public void setArticleID(long article_id) {
        this.article_id = article_id;
    }

    public void setTagID(long tag_id) {
        this.tag_id = tag_id;
    }

    public long getTagID() {
        return this.tag_id;
    }

    public long getArticleID() {
        return this.article_id;
    }

    public void saveWithId() {
        this.setId(this.save());
    }

    public static TagArticleData getOrCreate(long tagId, long articleId) {
        //Log.d("Sugar", com.orm.util.NamingHelper.toSQLNameDefault("tag_id"));
        TagArticleData ta;
        try {
            String[] args = new String[2];
            args[0] = String.valueOf(tagId);
            args[1] = String.valueOf(articleId);
            return Select.from(TagArticleData.class).where("TAGID = ? AND ARTICLEID = ?", args).list().get(0);
        } catch (Exception e){
            //Log.e(TAG, e.toString());
            ta = new TagArticleData(tagId, articleId);
            ta.saveWithId();
            Log.i(TAG, "Save TagArticle");
        }
        return ta;
    }
}