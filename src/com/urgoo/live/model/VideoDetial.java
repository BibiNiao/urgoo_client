package com.urgoo.live.model;

/**
 * Created by bb on 2016/10/13.
 */
public class VideoDetial {
    /**
     * 封面
     */
    private String cover;
    private String videoDate;
    private String enName;
    private String userIcon;
    private String video;
    private String des;
    private String isFollowed;
    private String userInfoSubId;

    public String getUserInfoSubId() {
        return userInfoSubId;
    }

    public void setUserInfoSubId(String userInfoSubId) {
        this.userInfoSubId = userInfoSubId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(String isFollowed) {
        this.isFollowed = isFollowed;
    }
}
