package com.urgoo.domain;

/**
 * Created by admin on 2016/5/10.
 */
public class AccountInformationDetailEntity {
    private  String title;

    public String getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(String insertDatetime) {
        this.insertDatetime = insertDatetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private  String content;
    private  String insertDatetime;
}
