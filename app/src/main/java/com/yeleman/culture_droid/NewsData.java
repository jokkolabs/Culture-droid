package com.yeleman.culture_droid;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Date;
import java.util.StringTokenizer;

public class NewsData extends SugarRecord {

    @Ignore
    private static final String TAG = Constants.getLogTag("NewsData");
;
    private Date published_on;
    private String article_id;
    private String thumbnail;
    private String title;
    private String nb_comments;
    private String content_size;
    private String content;

    public NewsData() {
    }

    public NewsData(Date published_on,
                    String article_id,
                    String thumbnail,
                    String title,
                    String nb_comments,
                    String content_size,
                    String content) {
        this.published_on = published_on;
        this.article_id = article_id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.nb_comments = nb_comments;
        this.content_size = content_size;
        this.content = content;
    }

    public void setPublishedOn(Date published_on) {
        this.published_on = published_on;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNbComments(String nb_comments) {
        this.nb_comments = nb_comments;
    }
    public String getTitle() {
        return this.title;
    }

    public void setArticleId(String articleId) {
        this.article_id = article_id;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setContentSize(String content_size) {
        this.content_size = content_size;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticleId() {
        return this.article_id;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getNbComments() {
        return this.nb_comments;
    }

    public String getContentSize() {
        return this.content_size;
    }

    public Date getPublishedOn() {
        return this.published_on;
    }

    public String getContent() {
        return this.content;
    }
}