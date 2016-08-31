package com.urgoo.domain;

import java.io.Serializable;

/**
 * Created by lijie on 2016/4/27.
 */
public class InformationBean implements Serializable {
    private String title;
    private String content;
    private String time;

    @Override
    public String toString() {
        return "InformationBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
