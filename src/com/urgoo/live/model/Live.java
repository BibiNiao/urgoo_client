package com.urgoo.live.model;

/**
 * Created by bb on 2016/9/23.
 */
public class Live {
    /**
     * 直播头像
     */
    private String masterPic;
    /**
     * 直播介绍
     */
    private String des;
    /**
     * 直播抬头
     */
    private String title;
    private int liveId;
    /**
     * 倒计时时间
     */
    private long balanceTime;
    /**
     * 1正在直播 3往期回顾
     */
    private String status;

    public String getMasterPic() {
        return masterPic;
    }

    public void setMasterPic(String masterPic) {
        this.masterPic = masterPic;
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

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public long getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(long balanceTime) {
        this.balanceTime = balanceTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
