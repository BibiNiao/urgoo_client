package com.urgoo.live.model;

/**
 * Created by bb on 2016/10/12.
 */
public class AlbumDetailList {
    /**
     * 头像
     */
    private String coverSmall;
    private String timeLong;
    private String albumId;
    /**
     * title
     */
    private String enName;
    /**
     * 内容
     */
    private String title;

    public String getCoverSmall() {
        return coverSmall;
    }

    public void setCoverSmall(String coverSmall) {
        this.coverSmall = coverSmall;
    }

    public String getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(String timeLong) {
        this.timeLong = timeLong;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
