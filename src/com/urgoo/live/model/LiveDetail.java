package com.urgoo.live.model;

/**
 * Created by bb on 2016/10/18.
 */
public class LiveDetail {
    private String masterPic;
    private String zoomNo;
    /**
     * 嘉宾介绍
     */
    private String introduce;
    private String userIcon;
    private String enName;
    private String liveStartTime;
    /**
     * 精彩看点
     */
    private String liveNotice;
    /**
     * 状态：1-未直播   2-结束无视频    3-结束有视频
     */
    private String status;
    private String isFollowed;
    private String video;
    /**
     * 大于0表示直播未开始 小于0表示已经开始
     */
    private long balanceTime;

    public long getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(long balanceTime) {
        this.balanceTime = balanceTime;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getMasterPic() {
        return masterPic;
    }

    public void setMasterPic(String masterPic) {
        this.masterPic = masterPic;
    }

    public String getZoomNo() {
        return zoomNo;
    }

    public void setZoomNo(String zoomNo) {
        this.zoomNo = zoomNo;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getLiveStartTime() {
        return liveStartTime;
    }

    public void setLiveStartTime(String liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    public String getLiveNotice() {
        return liveNotice;
    }

    public void setLiveNotice(String liveNotice) {
        this.liveNotice = liveNotice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(String isFollowed) {
        this.isFollowed = isFollowed;
    }
}
