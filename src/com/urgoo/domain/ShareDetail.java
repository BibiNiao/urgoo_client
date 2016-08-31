package com.urgoo.domain;

/**
 * Created by bb on 2016/7/28.
 */
public class ShareDetail {
    public String url;
    public String title;
    public String text;
    public String pic;

    @Override
    public String toString() {
        return "ShareDetail{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String mUrl) {
        url = mUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String mText) {
        text = mText;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String mPic) {
        pic = mPic;
    }
}
