package com.urgoo.counselor.model;

/**
 * Created by urgoo_01 on 2016/8/24.
 */
public class Works {

    private String auther;
    private String title;
    private String workId;
    private String content;
    private String insertDateTime;

    public String getInsertDateTime() {
        return insertDateTime;
    }

    public void setInsertDateTime(String mInsertDateTime) {
        insertDateTime = mInsertDateTime;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String mAuther) {
        auther = mAuther;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String mWorkId) {
        workId = mWorkId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String mContent) {
        content = mContent;
    }

    @Override
    public String toString() {
        return "WorksBean{" +
                "auther='" + auther + '\'' +
                ", title='" + title + '\'' +
                ", workId='" + workId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
