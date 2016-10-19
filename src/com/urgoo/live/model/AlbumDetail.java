package com.urgoo.live.model;

import com.urgoo.domain.ShareDetail;

import java.util.List;

/**
 * Created by bb on 2016/10/12.
 */
public class AlbumDetail {
    /**
     * 头像
     */
    private String cover;
    /**
     * 背景
     */
    private String coverBack;
    private String des;
    /**
     * 视频时间
     */
    private String timeLong;
    private String isFollowed;
    private String title;
    private com.urgoo.domain.ShareDetail shareDetail;
    /**
     * 专辑视频集合
     */
    private List<String> totalVideoUrlList;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCoverBack() {
        return coverBack;
    }

    public void setCoverBack(String coverBack) {
        this.coverBack = coverBack;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(String timeLong) {
        this.timeLong = timeLong;
    }

    public String getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(String isFollowed) {
        this.isFollowed = isFollowed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ShareDetail getShareDetail() {
        return shareDetail;
    }

    public void setShareDetail(ShareDetail shareDetail) {
        this.shareDetail = shareDetail;
    }

    public List<String> getTotalVideoUrlList() {
        return totalVideoUrlList;
    }

    public void setTotalVideoUrlList(List<String> totalVideoUrlList) {
        this.totalVideoUrlList = totalVideoUrlList;
    }
}
