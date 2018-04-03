package com.android.djs.useimooc_asyncloading;

public class NewsBean {

    private String imageURL;
    private String title;
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {

        return content;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }
}
