package com.yeleman.culture_droid;

public class ArticleElement {

    int articleId;
    String title;
    String publishedOn;
    String contentSize;
    String encodedThumbnail;
    boolean isLocal;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getContentSize() {
        return contentSize;
    }

    public void setContentSize(String contentSize) {
        this.contentSize = contentSize;
    }

    public String getEncodedThumbnail() {
        return encodedThumbnail;
    }

    public void setEncodedThumbnail(String encodedThumbnail) {
        this.encodedThumbnail = encodedThumbnail;
    }

    public boolean getLocal() {
        return isLocal;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }


}