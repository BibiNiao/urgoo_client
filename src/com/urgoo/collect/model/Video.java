package com.urgoo.collect.model;

/**
 * Created by bb on 2016/9/23.
 */
public class Video {
    /**
     * title
     */
    private String person;
    private String pic;
    /**
     * 描述
     */
    private String title;
    /**
     * 4：收藏专辑 5：收藏专辑视频 6:收藏直播
     */
    private String type;
    private String targetId;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
