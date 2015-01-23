package com.yeleman.culture_droid;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleData extends SugarRecord {

    @Ignore
    private static final String TAG = Constants.getLogTag("ArticleData");

    private Date published_on;
    private String article_id;
    private String thumbnail;
    private String title;
    private String nb_comments;
    private String content_size;
    private String content;

    public ArticleData() {
    }

    public ArticleData(Date published_on,
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

    public void saveWithId() {
        this.setId(this.save());
    }

    public void articleTagSave(List<String> tagNames) {

        for (String tagName: tagNames) {
            TagData tag = TagData.getOrCreate(tagName);
            TagArticleData.getOrCreate(tag.getId(), this.getId());
        }
    }

    public static List<ArticleData> allByTagName(String tagName) {
        List<ArticleData> articleDataList = new ArrayList<ArticleData>();
        Long tagId = TagData.getByName(tagName).getId();
        for(TagArticleData tagArticle : Select.from(TagArticleData.class).where(Condition.prop("TAGID").eq(tagId)).list()){
            ArticleData article = Select.from(ArticleData.class).where(Condition.prop("id").eq(tagArticle.getArticleID())).first();
            if (article != null) {
                articleDataList.add(article);
            }
         }
        return articleDataList;
    }

    public static ArticleData  getById(String id) {
        return ArticleData.findById(ArticleData.class, Long.valueOf(id));
    }

    public static Select<ArticleData> select() {
        return Select.from(ArticleData.class);
    }
}