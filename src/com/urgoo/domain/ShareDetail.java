package com.urgoo.domain;

/**
 * Created by bb on 2016/7/28.
 */
public class ShareDetail {
    public String url;
    public String title;
    public String text;
    public String pic;
    public String pengyouquan;
    public String weibo;

    @Override
    public String toString() {
        return "ShareDetail{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }

    public String getPengyouquan() {
        return pengyouquan;
    }

    public void setPengyouquan(String pengyouquan) {
        this.pengyouquan = pengyouquan;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
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
