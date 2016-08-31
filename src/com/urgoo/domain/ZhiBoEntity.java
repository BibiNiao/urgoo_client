package com.urgoo.domain;

/**
 * Created by Administrator on 2016/7/14.
 */
public class ZhiBoEntity {

    private  String masterPic;
    private  String des;

    public String getPaticipateCount() {
        return paticipateCount;
    }

    public void setPaticipateCount(String paticipateCount) {
        this.paticipateCount = paticipateCount;
    }

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

    public String getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(String balanceTime) {
        this.balanceTime = balanceTime;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    private  String paticipateCount;
    private  String title;
    private  String balanceTime;
    private  String liveId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private  String status;

}
