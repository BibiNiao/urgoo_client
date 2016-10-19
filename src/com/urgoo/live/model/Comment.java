package com.urgoo.live.model;

import java.util.List;

/**
 * Created by bb on 2016/10/13.
 */
public class Comment {
    private String insertDatetime;
    private String nickName;
    private int commentId;
    /**
     * 0不显示删除 1显示
     */
    private String showDel;
    private String userIcon;
    private int userId;
    private String content;
    /**
     * 2级回复数量
     */
    private int replySize;
    /**
     * 二级评论
     */
    private List<ReviewComments> listSubCommentAll;

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

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReplySize() {
        return replySize;
    }

    public void setReplySize(int replySize) {
        this.replySize = replySize;
    }

    public List<ReviewComments> getListSubComment() {
        return listSubCommentAll;
    }

    public void setListSubComment(List<ReviewComments> listSubCommentAll) {
        this.listSubCommentAll = listSubCommentAll;
    }
}
