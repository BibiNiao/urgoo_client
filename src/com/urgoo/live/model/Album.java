package com.urgoo.live.model;

/**
 * Created by bb on 2016/9/20.
 */
public class Album {
    /**
     * 图片
     */
    private String cover;
    /**
     * 描述
     */
    private String des;
    /**
     * 标题
     */
    private String title;
    private int albumTypeId;

    public int getAlbumTypeId() {
        return albumTypeId;
    }

    public void setAlbumTypeId(int albumTypeId) {
        this.albumTypeId = albumTypeId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
