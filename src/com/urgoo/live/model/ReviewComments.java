package com.urgoo.live.model;

/**
 * Created by bb on 2016/10/13.
 */
public class ReviewComments {
    private String insertDatetime;
    private String nickName;
    private int commentId;
    private String showDel;
    private String content;
    private String userIcon;

    public String getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(String insertDatetime) {
        this.insertDatetime = insertDatetime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getShowDel() {
        return showDel;
    }

    public void setShowDel(String showDel) {
        this.showDel = showDel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
