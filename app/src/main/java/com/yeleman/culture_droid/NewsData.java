package com.yeleman.culture_droid;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Date;
import java.util.StringTokenizer;

public class NewsData extends SugarRecord {

    @Ignore
    private static final String TAG = Constants.getLogTag("NewsData");

    private String title;
    private Date pubDate;
    private String description;
    private String status;
    private String category;
    private String content;

    public NewsData() {
    }

    public NewsData(Date pubDate,
                    String title,
                    String description,
                    String category,
                    String status,
                    String content) {
        this.pubDate = pubDate;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.content = content;
    }

    public void setpubDate(Date date) {
        this.pubDate = date;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }
    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getpubDate() {return this.pubDate; }

    public String getStatus() {
        return this.status;
    }

    public String getContent() {
        return this.content;
    }
}